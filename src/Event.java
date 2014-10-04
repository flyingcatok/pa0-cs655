
/**
 * 
 * Event class for scheduling.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * @Date: 10/4/2014
 * 
 */
public class Event implements Comparable<Event> {

	private double scheduledTime;
	private String eventName;
	private Packet packet;
	
	/**
	 * Constructor
	 * @param scheduledTime double
	 * @param eventName String
	 */
	public Event(double scheduledTime, String eventName){
		this.scheduledTime = scheduledTime;
		this.eventName = eventName;
	}
	
	/**
	 * Getter for scheduledTime.
	 * @return double
	 */
	public double getScheduledTime(){
		return this.scheduledTime;
	}
	
	/**
	 * Getter for eventName.
	 * @return String
	 */
	public String getEventName(){
		return this.eventName;
	}

	/**
	 * Setter for packet.
	 * @param packet Packet
	 */
	public void setPacket(Packet packet){
		this.packet = packet;
	}
	
	/**
	 * Getter for packet.
	 * @return Packet
	 */
	public Packet getPacket(){
		return this.packet;
	}
	
	@Override
	public int compareTo(Event o) {
		return Double.compare(this.scheduledTime, o.getScheduledTime());
	}
	
	@Override
	public String toString(){
		return this.scheduledTime + "\t|\t" + this.eventName;
	}
	
}
