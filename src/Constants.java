
/**
 * 
 * The class contains all the constants needed.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * @Date: 10/4/2014
 * 
 */
public final class Constants {
	
	// package events
	public static final int PKT_ARV = 100;
	public static final int PKT_TXED = 101;
	
	// scheduler
	public static final int IDLE = 102;
	public static final int BUSY = 103;
	
	// traffic sources
	public static final int TELNET = 104;
	public static final int TELNET_PKT_SIZE_AVG = 512; //bits
	public static final int FTP = 105;
	public static final int FTP_PKT_SIZE_AVG = 8192; //bits
	public static final int ROGUE = 106;
	public static final int ROGUE_PKT_SIZE_AVG = 5000; //bits
	public static final int SOURCE_TRANSMISSION_RATE = 1; //bit
	public static final double[] TELNET_FTP_DATA_GENERATION_RATE = {0.04,0.06,0.08,0.1,0.12,0.14,0.16,0.18,0.2};
	public static final double ROGUE_DATA_GENERATION_RATE = 0.5;
	
	// algorithms
	public final static int FIFO = 107;
	public final static int RR = 108;
	public final static int DRR = 109;
	
}
