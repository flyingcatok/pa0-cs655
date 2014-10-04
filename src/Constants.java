
/**
 * The class contains all the constants needed.
 * @author Feiyu Shi
 * Date: 10/4/2014
 */
public final class Constants {
	
	// package events
	public final static int PKT_ARV = 100;
	public final static int PKT_TXED = 101;
	
	// scheduler
	public final static int IDLE = 102;
	public final static int BUSY = 103;
	
	// traffic sources
	public final static int TELNET = 104;
	public final static int TELNET_PKT_SIZE_AVG = 512; //bits
	public final static int FTP = 105;
	public final static int FTP_PKT_SIZE_AVG = 8192; //bits
	public final static int ROGUE = 106;
	public final static int ROGUE_PKT_SIZE_AVG = 5000; //bits
	
	// algorithms
	public final static int FIFO = 107;
	public final static int RR = 108;
	public final static int DRR = 109;
	
}
