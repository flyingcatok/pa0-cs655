import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * Source class.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * @Date: 10/4/2014
 * 
 */
public class Source {
	
	private int sourceType;
	private int avgPktSize;
	private double avgInterarrivalTime;
//	private long totalNumOfPkts;
	
	/**
	 * Constructor.
	 * @param expNumber experiment number
	 * @param sourceType TELNET, FTP or ROGUE
	 * @param totalNumOfPkts
	 * @throws Exception
	 */
	public Source(int expNumber, int sourceType, long totalNumOfPkts) throws Exception{
		this.sourceType = sourceType;
//		this.totalNumOfPkts = totalNumOfPkts;
		
		if(this.sourceType == Constants.TELNET){
			this.avgPktSize = Constants.TELNET_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.TELNET_FTP_DATA_GENERATION_RATE[expNumber];
		}else if(this.sourceType == Constants.FTP){
			this.avgPktSize = Constants.FTP_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.TELNET_FTP_DATA_GENERATION_RATE[expNumber];
		}else if(this.sourceType == Constants.ROGUE){
			this.avgPktSize = Constants.ROGUE_PKT_SIZE_AVG;
			this.avgInterarrivalTime = this.avgPktSize / Constants.ROGUE_DATA_GENERATION_RATE;
		}else{
			throw new Exception("Source Type Error.");
		}
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
	public List<Packet> generatePakets(long totalNumOfPkts){
		double lastPktArrivalTime = 0.0;
		List<Packet> pktQueue = new ArrayList<Packet>();
		
		for(int i = 0; i < totalNumOfPkts; i++){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			double curPktArrivalTime = lastPktArrivalTime + getExpoRanNum(avgInterarrivalTime);
			int curPktSize = (int)getExpoRanNum(avgPktSize);
			Packet pkt = new Packet(uuid,curPktSize,curPktArrivalTime);
			pktQueue.add(pkt);
			lastPktArrivalTime = curPktArrivalTime;
		}
		return pktQueue;
	}

}
