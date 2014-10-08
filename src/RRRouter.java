import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 * Round Robin Router.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * Date: 10/8/2014
 *
 */
public class RRRouter implements Simulator {

	private LinkedList<Event> schedule = new LinkedList<Event>();
	private LinkedList<Packet> RRQueue = new LinkedList<Packet>();
	private List<Flow> flows;
	private LinkedList<Packet> RRPkts = new LinkedList<Packet>();
	private int totalPktInSystem;
	private int expNumber;
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @throws Exception Source Type Error.
	 */
	public RRRouter(int expNumber) throws Exception{
		this.totalPktInSystem = 0;
		this.flows = new IncomingFlows(expNumber, 20).getIncomingFlows();
		this.expNumber = expNumber;
		LinkedList<Packet> temp = new LinkedList<Packet>();
		for(int i = 0; i < this.flows.size(); i++){
			LinkedList<Packet> t = this.flows.get(i).getPkts();
			temp.addAll(t);
		}
		Collections.sort(temp);
		this.RRPkts = new LinkedList<Packet>(temp);
	}
	
//	/**
//	 * Determine which packet is the first to come
//	 * @return Packet
//	 */
//	private Packet getFirstPacketFromFlows(){
//		List<Packet> temp = new ArrayList<Packet>(this.flows.size());
//		for(int i = 0; i< this.flows.size();i++){
//			temp.add(this.flows.get(i).getPkts().peek());
//		}
//		Collections.sort(temp);
//		return temp.get(0);
//		
//	}
	
	@Override
	public void initSchedule() {
		// schedule first birth
//		Packet firstPkt = getFirstPacketFromFlows();
		Packet firstPkt = this.RRPkts.poll();
		double pktArrivaltime = firstPkt.getPktArrivalTime();
		Event birth = new Event(pktArrivaltime,Constants.PKT_ARV);
		birth.setPacket(firstPkt);
		this.schedule.add(birth);
		
		// schedule first death
		Event death = new Event(pktArrivaltime, Constants.PKT_TXED);
		death.setPacket(firstPkt);
		this.schedule.add(death);
	}

	@Override
	public void birth(Event birthEvent) {
		// do nothing, for all pkts are in the flow
		// schedule the next birth
		if(this.RRPkts.size() != 0){
			Packet nextPkt = this.RRPkts.poll();
			this.totalPktInSystem++;
			double nextBirthTime = nextPkt.getPktArrivalTime();
			Event nextBirthEvent = new Event(nextBirthTime, Constants.PKT_ARV);
			nextBirthEvent.setPacket(nextPkt);
			this.schedule.add(nextBirthEvent);
			Collections.sort(this.schedule);
		}
		
		// check if only 1 packet in system, schedule death event
		if(this.totalPktInSystem == 1){
			double nextDeathTime = birthEvent.getScheduledTime();
			Event death = new Event(nextDeathTime, Constants.PKT_TXED);
			death.setPacket(birthEvent.getPacket());
			this.schedule.add(death);
		}
	}

	@Override
	public void death(Event deathEvent) {
		Packet pkt = deathEvent.getPacket();
		int currFlowId = pkt.getFlowId();
		
		// dequeue current pkt from flow
		this.flows.get(currFlowId).getPkts().poll();
		this.totalPktInSystem--;
		// get transmission End Time
		double timeOnSchedule = deathEvent.getScheduledTime();
		double transmissionEndTime = timeOnSchedule + pkt.getpktSize() / Constants.TRANSMISSION_RATE;
		
		// schedule next death event
		checkNextQueueForDeathEvent(currFlowId, transmissionEndTime);
	}

	private void checkNextQueueForDeathEvent(int currFlowId, double transmissionEndTime){
		if(this.totalPktInSystem == 0){
			return;
		}
		// check next queue, if there is a packet there
		int nextFlowId = (currFlowId + 1) % this.flows.size();
		Packet nextFlowFirstPkt = this.flows.get(nextFlowId).getPkts().peek();
		double nextFlowFirstPktArrivalTime = nextFlowFirstPkt.getPktArrivalTime();

		if(nextFlowFirstPktArrivalTime <= transmissionEndTime){
			double nextDeathTime = transmissionEndTime;
			Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
			nextDeathEvent.setPacket(nextFlowFirstPkt);
			return;
		}else{
			checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime);	
		}
	}
	
	@Override
	public void controller() {
		// initialize schedule
		initSchedule();
		
		// print
		// label of MM1
		String ith = "RR_"+"exp_" + this.expNumber+ "";
		try {
			PrintWriter writer = new PrintWriter(ith.concat("_schedule.txt"), "UTF-8");
				
			writer.println("<-------------------- RR router queuing system schedule -------------------->");
			writer.println("Time"+"\t\t\t|\t"+"Event"+"\t\t|\t"+"Pkt ID"+"\t\t\t\t\t"+"Pkt Size"+"\t"+"Pkt Arrival Time");
				
			while(this.schedule.size()!=0){
				Event currEvent = this.schedule.poll();
				String currEventName = currEvent.getEventName();
				switch(currEventName){
				case Constants.PKT_ARV:
					birth(currEvent);
					writer.println(currEvent.toString());
					break;
				case Constants.PKT_TXED:
					death(currEvent);
					writer.println(currEvent.toString());
					break;
				default:
					try {
						throw new Exception("No events in the schedule!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			writer.println("<---------- End of RR router queuing system schedule ---------->");
			writer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

}
