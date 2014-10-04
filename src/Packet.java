
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
	
	public Packet(String pktId, int pktSize, double pktArrivalTime){
		this.pktId = pktId;
		this.pktSize = pktSize;
		this.pktArrivalTime = pktArrivalTime;
	}
	
	public int getpktSize(){
		return this.pktSize;
	}
	
	public String getpktId(){
		return this.pktId;
	}

	public double getPktArrivalTime(){
		return this.pktArrivalTime;
	}

}
