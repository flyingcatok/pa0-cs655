
/**
 * 
 * Packet class.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * @Date: 10/4/2014
 * 
 */
public class Packet {
	
	private String pktId; // 32 bit UUID
	private int pktSize; // bits
	private double pktArrivalTime;
	
	/**
	 * 
	 * Constructor.
	 * @param pktId String
	 * @param pktSize int
	 * @param pktArrivalTime double
	 * 
	 */ 
	public Packet(String pktId, int pktSize, double pktArrivalTime){
		this.pktId = pktId;
		this.pktSize = pktSize;
		this.pktArrivalTime = pktArrivalTime;
	}
	
	/**
	 * Getter for pktSize.
	 * @return int
	 */
	public int getpktSize(){
		return this.pktSize;
	}
	
	/**
	 * Getter for pktId
	 * @return String
	 */
	public String getpktId(){
		return this.pktId;
	}

	/**
	 * Getter for pktArrivalTime
	 * @return double.
	 */
	public double getPktArrivalTime(){
		return this.pktArrivalTime;
	}
	
	@Override
	public String toString(){
		return pktId + "\t" + pktSize + "bits" + "\t" + "@" + pktArrivalTime;
	}
	
}
