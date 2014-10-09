import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * DRR Router
 * @author Rakshit Sachdev
 * @collaborator Feiyu Shi
 * @Date: 10/8/2014
 *
 */
public class DRRRouter implements Simulator {

	private LinkedList<Event> schedule = new LinkedList<Event>();
	private List<Flow> sourceQueues;

	private LinkedList<Packet> DRRPkts = new LinkedList<Packet>();
	
	private int expNumber;
	private double[] firstEventTime;
	private double[] lastEventTime;
	private double[] tput;
	private long[] totalBits;
	private double[] avgLatency;
	
	public DRRRouter(int expNumber) throws Exception{
		this.sourceQueues = new IncomingFlows(expNumber, 1000).getIncomingFlows();
		this.expNumber = expNumber;
		this.lastEventTime = new double[this.sourceQueues.size()];
		this.tput = new double[this.sourceQueues.size()];
		this.firstEventTime = getFirstPacketArrivalTimeFromFlows();
		this.totalBits = new long[this.sourceQueues.size()];
		this.avgLatency = new double[this.sourceQueues.size()];
		LinkedList<Packet> temp = new LinkedList<Packet>();
		for(int i = 0; i < this.sourceQueues.size(); i++){
			LinkedList<Packet> t = this.sourceQueues.get(i).getPkts();
			temp.addAll(t);
			this.totalBits[i] = getLoadForEachFlow(this.sourceQueues.get(i));
		}
		Collections.sort(temp);
		this.DRRPkts = new LinkedList<Packet>(temp);
	}
	
	private double[] getFirstPacketArrivalTimeFromFlows(){
		double[] temp = new double[this.sourceQueues.size()];
		for(int i = 0; i< this.sourceQueues.size();i++){
			temp[i] = this.sourceQueues.get(i).getPkts().peek().getPktArrivalTime();
		}
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
	
	@Override
	public void initSchedule() {
		// TODO Auto-generated method stub
		Packet firstPkt = this.DRRPkts.poll();
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
		if(this.DRRPkts.size() != 0){
			Packet nextPkt = this.DRRPkts.poll();
			
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
		this.sourceQueues.get(currFlowId).getPkts().poll();
		// update the value of defCounter
		int dcount = this.sourceQueues.get(currFlowId).getDefCounter();
		int esize = pkt.getpktSize();
		dcount = dcount - esize;
		this.sourceQueues.get(currFlowId).setDefCounter(dcount);
		
		// get transmission End Time
		double timeOnSchedule = deathEvent.getScheduledTime();
		double transmissionEndTime = timeOnSchedule + pkt.getpktSize() / Constants.TRANSMISSION_RATE;
		this.lastEventTime[currFlowId] = transmissionEndTime;
		// latency
		double latency = timeOnSchedule - pkt.getPktArrivalTime();
		this.avgLatency[currFlowId] += latency;
		// schedule next death event
		checkSameQueueForDeathEvent(currFlowId, transmissionEndTime);
		//checkNextQueueForDeathEvent(currFlowId, transmissionEndTime);

	}
	private void checkSameQueueForDeathEvent(int currFlowId, double transmissionEndTime){
		// check same queue if more packets can be sent
		
		if(this.sourceQueues.get(currFlowId).getPkts().size()!=0){
			Packet Pkt = this.sourceQueues.get(currFlowId).getPkts().peek();
			int esize = Pkt.getpktSize();
			int dcount = this.sourceQueues.get(currFlowId).getDefCounter();
			double nextPktArrivalTime = Pkt.getPktArrivalTime();
			if(nextPktArrivalTime <= transmissionEndTime && dcount >= esize){
				double nextDeathTime = transmissionEndTime;
				Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
				nextDeathEvent.setPacket(Pkt);
				this.schedule.add(nextDeathEvent);
				Collections.sort(this.schedule);
				return;
			}else{
				dcount = dcount + Constants.QUANTUM;
				this.sourceQueues.get(currFlowId).setDefCounter(dcount);
				int nextFlowId = (currFlowId + 1) % this.sourceQueues.size();
				checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime,1);	
		}}
		else{
			int dcount = this.sourceQueues.get(currFlowId).getDefCounter();
			dcount = dcount + Constants.QUANTUM;
			this.sourceQueues.get(currFlowId).setDefCounter(dcount);
			int nextFlowId = (currFlowId + 1) % this.sourceQueues.size();
			checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime,1);	
	}
		
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
		
			if(this.sourceQueues.get(currFlowId).getPkts().size()!=0){
				Packet nextFlowFirstPkt = this.sourceQueues.get(currFlowId).getPkts().peek();
				int esize = nextFlowFirstPkt.getpktSize();
				int dcount = this.sourceQueues.get(currFlowId).getDefCounter();
				double nextFlowFirstPktArrivalTime = nextFlowFirstPkt.getPktArrivalTime();
				if(nextFlowFirstPktArrivalTime <= transmissionEndTime && dcount >= esize){
					double nextDeathTime = transmissionEndTime;
					Event nextDeathEvent = new Event(nextDeathTime, Constants.PKT_TXED);
					nextDeathEvent.setPacket(nextFlowFirstPkt);
					this.schedule.add(nextDeathEvent);
					Collections.sort(this.schedule);
					return;
				}else{
					dcount = dcount + Constants.QUANTUM;
					this.sourceQueues.get(currFlowId).setDefCounter(dcount);
					int nextFlowId = (currFlowId + 1) % this.sourceQueues.size();
					counterTemp++;
					checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime, counterTemp);	
				}
			}else{
				int dcount = this.sourceQueues.get(currFlowId).getDefCounter();
				dcount = dcount + Constants.QUANTUM;
				this.sourceQueues.get(currFlowId).setDefCounter(dcount);
				int nextFlowId = (currFlowId + 1) % this.sourceQueues.size();
				counterTemp++;
				checkNextQueueForDeathEvent(nextFlowId, transmissionEndTime, counterTemp);	
			}
		}
			
			
			
			
		// check next queue, if there is a packet there

	

	@Override
	public void controller() {
		// initialize schedule
		initSchedule();
		
		// print
		// label of MM1
		String ith = "DRR_"+"exp_" + this.expNumber+ "";
		try {
			PrintWriter writer = new PrintWriter(ith.concat("_schedule.txt"), "UTF-8");
				
			writer.println("<-------------------- DRR router queuing system schedule -------------------->");
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
			writer.println("<---------- End of DRR router queuing system schedule ---------->");
			writer.close();
			// compute statistics
			// write to file
			PrintWriter writer1 = new PrintWriter(ith.concat("_statistics.txt"), "UTF-8");
						
			double totalTput = 0;
			double avgLatencyForAllSrcs = 0;
			for(Flow f: this.sourceQueues){
				this.tput[f.getFlowId()] = getThroughput(f);
				writer1.println("tput of flow "+f.getFlowId()+": "+this.tput[f.getFlowId()]);
				totalTput += this.tput[f.getFlowId()];
				this.avgLatency[f.getFlowId()] /= Constants.TOTAL_PKTS_IN_SIMULATION;
				writer1.println("avg lantency of flow "+f.getFlowId()+": "+ this.avgLatency[f.getFlowId()]);
			}
			for(int i=0; i<this.avgLatency.length;i++){
				avgLatencyForAllSrcs += this.avgLatency[i];
			}
			avgLatencyForAllSrcs /= this.sourceQueues.size();
			writer1.println("avg lantency of all flows: " + avgLatencyForAllSrcs);
			writer1.println("total tput: "+totalTput);
			writer1.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

}


