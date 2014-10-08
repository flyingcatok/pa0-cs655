import java.util.LinkedList;

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
	private LinkedList<Packet> pkts = new LinkedList<Packet>();
	
	/**
	 * Constructor
	 * @param expNumber experiment number
	 * @param flowType TELNET, FTP or ROGUE
	 * @param pktsTotal number of packets generated.
	 * @param flowId flow id
	 * @throws Exception Source Type Error.
	 */
	public Flow(int expNumber, String flowType, int pktsTotal, int flowId) throws Exception{
		this.pkts = new Source(expNumber, flowType).generatePakets(pktsTotal);
		this.flowId = flowId;
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
}
