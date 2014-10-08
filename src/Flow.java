import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * Flow class
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * @Date: 10/8/2014
 *
 */
public class Flow {
	
	private int flowId;
	private String sourceType;
	private int avgPktSize;
	private double avgInterarrivalTime;
	private int pktsTotal;
	private LinkedList<Packet> pkts = new LinkedList<Packet>();
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @param sourceType TELNET, FTP or ROGUE
	 * @param pktsTotal number of packets generated.
	 * @param flowId flow id
	 * @throws Exception Source Type Error.
	 */
	public Flow(int expNumber, String sourceType, int pktsTotal, int flowId) throws Exception{
		this.sourceType = sourceType;
		this.flowId = flowId;
		this.pktsTotal = pktsTotal;
		if(this.sourceType.equals(Constants.TELNET)){
			this.avgPktSize = Constants.TELNET_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.TELNET_FTP_DATA_GENERATION_RATE[expNumber];
		}else if(this.sourceType.equals(Constants.FTP)){
			this.avgPktSize = Constants.FTP_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.TELNET_FTP_DATA_GENERATION_RATE[expNumber];
		}else if(this.sourceType.equals(Constants.ROGUE)){
			this.avgPktSize = Constants.ROGUE_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.ROGUE_DATA_GENERATION_RATE;
		}else{
			throw new Exception("Source Type Error.");
		}
		
		// generate pkts
		generatePakets();
	}
	
	/**
	 * Getter for flowId
	 * @return int
	 */
	public int getFlowId(){
		return this.flowId;
	}
	
	/**
	 * Getter for pkts
	 * @return LinkedList<Packet>
	 */
	public LinkedList<Packet> getPkts(){
		return this.pkts;
	}
	
	/**
	 * Generate exponential distributed number.
	 * @param lambda double
	 * @return double
	 */
	private double getExpoRanNum(double lambda){
		Random uniRandom = new Random();
		double r = uniRandom.nextDouble();
		return -Math.log(1-r)/lambda;
	}
	
	/**
	 * Generate packets from this source.
	 * @return List<Packet>
	 */
	private void generatePakets(){
		double lastPktArrivalTime = 0.0;
		LinkedList<Packet> pktQueue = new LinkedList<Packet>();
		
		for(int i = 0; i < this.pktsTotal; i++){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			double curPktArrivalTime = lastPktArrivalTime + getExpoRanNum(1.0/avgInterarrivalTime);
			int curPktSize = (int)getExpoRanNum(1.0/avgPktSize);
			Packet pkt = new Packet(uuid,curPktSize,curPktArrivalTime);
			pkt.setFlowId(this.flowId);
			pktQueue.add(pkt);
			lastPktArrivalTime = curPktArrivalTime;
		}
		this.pkts = pktQueue;
	}
	
	@Override
	public String toString(){
		return "Flow Id: " + this.flowId + "\t" + this.sourceType + "\t" + "avg pkt size: " + avgPktSize + "bits" + "\t" + "avg interarrival time: " + avgInterarrivalTime;
	}
}
