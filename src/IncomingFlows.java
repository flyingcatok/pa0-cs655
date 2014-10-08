import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IncomingFlows {
	
	private List<LinkedList<Packet>> incomingFlows = new ArrayList<LinkedList<Packet>>();
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @throws Exception Source Type Error
	 */
	public IncomingFlows(int expNumber) throws Exception{
		// initialize source pkts
		int total = Constants.TOTAL_PKTS_IN_SIMULATION;
		LinkedList<Packet> pkts0 = new Source(expNumber, Constants.TELNET).generatePakets(total);
		LinkedList<Packet> pkts1 = new Source(expNumber, Constants.TELNET).generatePakets(total);
		LinkedList<Packet> pkts2 = new Source(expNumber, Constants.TELNET).generatePakets(total);
		LinkedList<Packet> pkts3 = new Source(expNumber, Constants.TELNET).generatePakets(total);
		LinkedList<Packet> pkts4 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts5 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts6 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts7 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts8 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts9 = new Source(expNumber, Constants.FTP).generatePakets(total);
		LinkedList<Packet> pkts10 = new Source(expNumber, Constants.ROGUE).generatePakets(total);
		
		// add all flows together
		this.incomingFlows.add(pkts0);
		this.incomingFlows.add(pkts1);
		this.incomingFlows.add(pkts2);
		this.incomingFlows.add(pkts3);
		this.incomingFlows.add(pkts4);
		this.incomingFlows.add(pkts5);
		this.incomingFlows.add(pkts6);
		this.incomingFlows.add(pkts7);
		this.incomingFlows.add(pkts8);
		this.incomingFlows.add(pkts9);
		this.incomingFlows.add(pkts10);
	}
	
	/**
	 * Getter for incomingFlows
	 * @return List<LinkedList<Packet>>
	 */
	List<LinkedList<Packet>> getIncomingFlows(){
		return this.incomingFlows;
	}
}
