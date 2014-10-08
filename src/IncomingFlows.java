import java.util.ArrayList;
import java.util.List;

public class IncomingFlows {
	
	private List<Flow> incomingFlows = new ArrayList<Flow>();
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @throws Exception Source Type Error
	 */
	public IncomingFlows(int expNumber, int total) throws Exception{
		// initialize source pkts
		Flow f0 = new Flow(expNumber, Constants.TELNET, total);
		Flow f1 = new Flow(expNumber, Constants.TELNET, total);
		Flow f2 = new Flow(expNumber, Constants.TELNET, total);
		Flow f3 = new Flow(expNumber, Constants.TELNET, total);
		Flow f4 = new Flow(expNumber, Constants.FTP, total);
		Flow f5 = new Flow(expNumber, Constants.FTP, total);
		Flow f6 = new Flow(expNumber, Constants.FTP, total);
		Flow f7 = new Flow(expNumber, Constants.FTP, total);
		Flow f8 = new Flow(expNumber, Constants.FTP, total);
		Flow f9 = new Flow(expNumber, Constants.FTP, total);
		Flow f10 = new Flow(expNumber, Constants.ROGUE, total);

		// add all flows together
		this.incomingFlows.add(f0);
		this.incomingFlows.add(f1);
		this.incomingFlows.add(f2);
		this.incomingFlows.add(f3);
		this.incomingFlows.add(f4);
		this.incomingFlows.add(f5);
		this.incomingFlows.add(f6);
		this.incomingFlows.add(f7);
		this.incomingFlows.add(f8);
		this.incomingFlows.add(f9);
		this.incomingFlows.add(f10);
	}
	
	/**
	 * Getter for incomingFlows
	 * @return List<LinkedList<Packet>>
	 */
	List<Flow> getIncomingFlows(){
		return this.incomingFlows;
	}
}
