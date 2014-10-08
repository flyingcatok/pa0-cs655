import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * FIFO Router class.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * Date: 10/7/2014
 *
 */
public class FIFORouter implements Simulator {

	private LinkedList<Event> schedule = new LinkedList<Event>();
	private LinkedList<Packet> FIFOQueue = new LinkedList<Packet>();
	private List<LinkedList<Packet>> flows;

	private LinkedList<Packet> FIFOPkts = new LinkedList<Packet>();
	
	private int expNumber;
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @throws Exception Source Type Error.
	 */
	public FIFORouter(int expNumber) throws Exception{
		this.flows = new IncomingFlows(expNumber).getIncomingFlows();
		this.expNumber = expNumber;
		// initialize FIFO pkts
		LinkedList<Packet> temp = new LinkedList<Packet>();
		for(int i = 0; i < this.flows.size(); i++){
			temp.addAll(this.flows.get(i));
		}
		Collections.sort(temp);
		this.FIFOPkts = new LinkedList<Packet>( temp.subList(0, 10));
	}
	
	@Override
	public void initSchedule(){
		// schedule first birth
		Packet firstPkt = this.FIFOPkts.poll();
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
	public void birth(Event birthEvent){
		// add newly-born request to FIFO queue
		Packet pkt = birthEvent.getPacket();
		this.FIFOQueue.add(pkt);
		
		// schedule the next birth
		if(this.FIFOPkts.size() != 0){
			Packet nextPkt = this.FIFOPkts.poll();
			double nextBirthTime = nextPkt.getPktArrivalTime();
			Event nextBirthEvent = new Event(nextBirthTime, Constants.PKT_ARV);
			nextBirthEvent.setPacket(nextPkt);
			this.schedule.add(nextBirthEvent);
			Collections.sort(this.schedule);
		}
			
	}
	
	@Override
	public void death(Event deathEvent){
		// remove the pkt from queue
		Packet pkt = this.FIFOQueue.poll();
		
		// pkt should be equal to pkt from event
		Packet pkt2 = deathEvent.getPacket();
		
		if(!pkt.equals(pkt2)){
			System.out.println("Pkts in FIFO queue is not the same as pkts in event.");
		}

		double transmissionEndTime = deathEvent.getScheduledTime() + pkt.getpktSize() / Constants.TRANSMISSION_RATE;

		// schedule next death event
		if(this.FIFOQueue.size() != 0){
			Packet nextPkt = this.FIFOQueue.peek();
			double nextBirthTime = nextPkt.getPktArrivalTime();
			double nextDeathTime = Math.max(nextBirthTime, transmissionEndTime);
			Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
			nextDeathEvent.setPacket(nextPkt);
			this.schedule.add(nextDeathEvent);
			Collections.sort(this.schedule);
		}else{
			if(this.schedule.size() != 0){
				Packet nextPkt = getNextBirthEventInSchedule(this.schedule).getPacket();
				double nextBirthTime = nextPkt.getPktArrivalTime();
				double nextDeathTime = Math.max(nextBirthTime, transmissionEndTime);
				Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
				nextDeathEvent.setPacket(nextPkt);
				this.schedule.add(nextDeathEvent);
				Collections.sort(this.schedule);
			}
			
		}
		
	}
	
	/**
	 * Find the first birth event in the schedule.
	 * @param schedule LinkedList<Event>
	 * @return Event
	 */
	private Event getNextBirthEventInSchedule(LinkedList<Event> schedule){
		LinkedList<Event> temp = new LinkedList<Event>(schedule);
		Event e = temp.poll();
		if(e.getEventName().equals(Constants.PKT_ARV)){
			return e;
		}else{
			return getNextBirthEventInSchedule(temp);
		}
		
	}
	
	@Override
	public void controller(){
		// initialize schedule
		initSchedule();
		
		// print
		// label of MM1
		String ith = "exp_" + this.expNumber+ "";
		try {
			PrintWriter writer = new PrintWriter(ith.concat("_schedule.txt"), "UTF-8");
		
			writer.println("<-------------------- FIFO router queuing system schedule -------------------->");
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
			writer.println("<---------- End of FIFO router queuing system schedule ---------->");
			writer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
}
