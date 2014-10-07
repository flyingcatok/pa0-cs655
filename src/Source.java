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
	
	private String sourceType;
	private int avgPktSize;
	private double avgInterarrivalTime;
	
	/**
	 * Constructor.
	 * @param expNumber experiment number
	 * @param sourceType TELNET, FTP or ROGUE
	 * @throws Exception
	 */
	public Source(int expNumber, String sourceType) throws Exception{
		this.sourceType = sourceType;
		
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
			double curPktArrivalTime = lastPktArrivalTime + getExpoRanNum(1.0/avgInterarrivalTime);
			int curPktSize = (int)getExpoRanNum(1.0/avgPktSize);
			Packet pkt = new Packet(uuid,curPktSize,curPktArrivalTime);
			pktQueue.add(pkt);
			lastPktArrivalTime = curPktArrivalTime;
		}
		return pktQueue;
	}
	
	@Override
	public String toString(){
		return sourceType + "\t" + "avg pkt size: " + avgPktSize + "bits" + "\t" + "avg interarrival time: " + avgInterarrivalTime;
	}

}
