import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private List<Flow> flows;
	private LinkedList<Packet> RRPkts = new LinkedList<Packet>();
	private int expNumber;
	private double[] firstEventTime;
	private double[] lastEventTime;
	private double[] tput;
	private long[] totalBits;
	private double[] avgLatency;
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @throws Exception Source Type Error.
	 */
	public RRRouter(int expNumber) throws Exception{
		
		this.flows = new IncomingFlows(expNumber, Constants.TOTAL_PKTS_IN_SIMULATION).getIncomingFlows();
		this.expNumber = expNumber;
		this.lastEventTime = new double[this.flows.size()];
		this.tput = new double[this.flows.size()];
		this.firstEventTime = getFirstPacketArrivalTimeFromFlows();
		this.totalBits = new long[this.flows.size()];
		this.avgLatency = new double[this.flows.size()];
		
		LinkedList<Packet> temp = new LinkedList<Packet>();
		for(int i = 0; i < this.flows.size(); i++){
			LinkedList<Packet> t = this.flows.get(i).getPkts();
			temp.addAll(t);
			this.totalBits[i] = getLoadForEachFlow(this.flows.get(i));
		}
		Collections.sort(temp);
		this.RRPkts = new LinkedList<Packet>(temp);
	}
	
	/**
	 * Determine which packet is the first to come
	 * @return Packet
	 */
	private double[] getFirstPacketArrivalTimeFromFlows(){
//		List<Packet> temp = new ArrayList<Packet>(this.flows.size());
		double[] temp = new double[this.flows.size()];
		for(int i = 0; i< this.flows.size();i++){
			temp[i] = this.flows.get(i).getPkts().peek().getPktArrivalTime();
//			temp.add(this.flows.get(i).getPkts().peek());
		}
//		Collections.sort(temp);
//		return temp.get(0);
		return temp;
		
	}
	
	private long getLoadForEachFlow(Flow flow){
		long totalBits = 0;
		int sz = flow.getPkts().size();
		for(int i = 0; i < sz; i++){
			totalBits += flow.getPkts().get(i).getpktSize();
		}
		return totalBits;
	}
	
	private double getThroughput(Flow flow){
		int flowId = flow.getFlowId();
		double arrivalTimeOfFirstPkt = this.firstEventTime[flowId];
		long totalBits = this.totalBits[flowId];
		double departureTimeOfLastPkt = this.lastEventTime[flowId];
		
		return totalBits/(departureTimeOfLastPkt - arrivalTimeOfFirstPkt);
		
	}
	
	private double getAvgLatencyForEachFlow(int flowId, double latency){
		this.avgLatency[flowId] += latency;
		return expNumber;
		
	}
	
	@Override
	public void initSchedule() {
		// schedule first birth
		Packet firstPkt = this.RRPkts.poll();
		double pktArrivaltime = firstPkt.getPktArrivalTime();
		Event birth = new Event(pktArrivaltime,Constants.PKT_ARV);
		birth.setPacket(firstPkt);
		this.schedule.add(birth);
		
		// schedule first death
		Event death = new Event(pktArrivaltime, Constants.PKT_TXED);
		death.setPacket(firstPkt);
		this.schedule.add(death);
		//
//		this.lastEventTime.add(firstPkt.getFlowId(), pktArrivaltime);
	}

	@Override
	public void birth(Event birthEvent) {
		// do nothing, for all pkts are in the flow
		// schedule the next birth
		if(this.RRPkts.size() != 0){
			Packet nextPkt = this.RRPkts.poll();
			
			double nextBirthTime = nextPkt.getPktArrivalTime();
			Event nextBirthEvent = new Event(nextBirthTime, Constants.PKT_ARV);
			nextBirthEvent.setPacket(nextPkt);
			this.schedule.add(nextBirthEvent);
			Collections.sort(this.schedule);
		}
	}

	@Override
	public void death(Event deathEvent) {
		Packet pkt = deathEvent.getPacket();
		int currFlowId = pkt.getFlowId();
		
		// dequeue current pkt from flow
		this.flows.get(currFlowId).getPkts().poll();

		// get transmission End Time
		double timeOnSchedule = deathEvent.getScheduledTime();
		double transmissionEndTime = timeOnSchedule + pkt.getpktSize() / Constants.TRANSMISSION_RATE;
		
		// add departure time to list
		this.lastEventTime[currFlowId] = transmissionEndTime;
		
		// latency
		double latency = timeOnSchedule - pkt.getPktArrivalTime();
		this.avgLatency[currFlowId] += latency;
		
		// schedule next death event
		int nextFlowId = (currFlowId + 1) % this.flows.size();
		checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime, 1);
	}
	
	private void checkNextQueueForDeathEvent(int currFlowId, double transmissionEndTime, int counter){

		int counterTemp = counter;
		if(counterTemp % 11 ==0){
			// schedule next death event
			if(this.schedule.size()!=0){
				Event birth = this.schedule.peek();
				double time = birth.getScheduledTime();
				Event death = new Event(time, Constants.PKT_TXED);
				death.setPacket(birth.getPacket());
				this.schedule.add(death);
				Collections.sort(this.schedule);
			}
			
			return;
		}
		// check next queue, if there is a packet there
		if(this.flows.get(currFlowId).getPkts().size()!=0){
			Packet nextFlowFirstPkt = this.flows.get(currFlowId).getPkts().peek();
			double nextFlowFirstPktArrivalTime = nextFlowFirstPkt.getPktArrivalTime();

			if(nextFlowFirstPktArrivalTime <= transmissionEndTime){
				double nextDeathTime = transmissionEndTime;
				Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
				nextDeathEvent.setPacket(nextFlowFirstPkt);
				this.schedule.add(nextDeathEvent);
				Collections.sort(this.schedule);
				return;
			}else{
				int nextFlowId = (currFlowId + 1) % this.flows.size();
				counterTemp++;
				checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime, counterTemp);	
			}
		}else{
			int nextFlowId = (currFlowId + 1) % this.flows.size();
			counterTemp++;
			checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime, counterTemp);	
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
			writer.println("Time"+"\t\t\t|\t"+"Event"+"\t\t|\t"+"Flow ID"+"\t"+"Pkt Size"+"\t"+"Pkt Arrival Time");
	
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
			writer.println("<-------------------- End of RR router queuing system schedule -------------------->");
			writer.close();
			
			// compute statistics
			// write to file
			PrintWriter writer1 = new PrintWriter(ith.concat("_statistics.txt"), "UTF-8");
			writer1.println("<---------- RR router queuing system statistics ---------->");
			//tput
			double totalTput = 0;
			for(Flow f: this.flows){
				this.tput[f.getFlowId()] = getThroughput(f);
				writer1.println("tput of flow "+f.getFlowId()+": \t"+this.tput[f.getFlowId()]);
				totalTput += this.tput[f.getFlowId()];
				this.avgLatency[f.getFlowId()] /= Constants.TOTAL_PKTS_IN_SIMULATION;
				writer1.println("avg lantency of flow "+f.getFlowId()+": "+ this.avgLatency[f.getFlowId()]);
			}
			
			writer1.println("total tput: \t\t"+totalTput);
			writer1.println("<---------- End of RR router queuing system statistics ---------->");
			writer1.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

}
