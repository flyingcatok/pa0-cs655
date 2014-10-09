
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
	public static final String PKT_ARV = "PKT_ARVED";
	public static final String PKT_TXED = "PKT_TXED";
	public static final String LOG = "LOG";
	
	// scheduler
	public static final int IDLE = 102;
	public static final int BUSY = 103;
	
	// traffic sources
	public static final String TELNET = "TELNET";
	public static final int TELNET_PKT_SIZE_AVG = 512; //bits
	public static final String FTP = "FTP";
	public static final int FTP_PKT_SIZE_AVG = 8192; //bits
	public static final String ROGUE = "ROGUE";
	public static final int ROGUE_PKT_SIZE_AVG = 5000; //bits
	public static final int TRANSMISSION_RATE = 1; //bit/s
	public static final double[] TELNET_FTP_DATA_GENERATION_RATE = {0.04,0.06,0.08,0.1,0.12,0.14,0.16,0.18,0.2};
	public static final double ROGUE_DATA_GENERATION_RATE = 0.5*TRANSMISSION_RATE;
	
	// algorithms
	public final static String FIFO = "FIFO";
	public final static String RR = "RR";
	public final static String DRR = "DRR";
	
	// simulation
	public final static int TOTAL_PKTS_IN_SIMULATION = 1000;
	
}
