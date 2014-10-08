import java.util.LinkedList;


public class Flow {
	
	private int flowId;
	private LinkedList<Packet> pkts = new LinkedList<Packet>();
	
	public Flow(int expNumber, String flowType, int pktsTotal) throws Exception{
		this.pkts = new Source(expNumber, flowType).generatePakets(pktsTotal);
	}
	
	public int getFlowId(){
		return this.flowId;
	}
	
	public LinkedList<Packet> getPkts(){
		return this.pkts;
	}
}
