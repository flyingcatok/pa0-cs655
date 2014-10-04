
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
	private int eventName;
	
	public Event(double scheduledTime, int eventName){
		this.scheduledTime = scheduledTime;
		this.eventName = eventName;
	}
	
	public double getScheduledTime(){
		return this.scheduledTime;
	}
	
	public int getEventName(){
		return this.eventName;
	}
	
	public void setScheduledTime(double scheduledTime){
		this.scheduledTime = scheduledTime;
	}
	
	public void setEventName(int eventName){
		this.eventName = eventName;
	}

	@Override
	public int compareTo(Event o) {
		return Double.compare(this.scheduledTime, o.getScheduledTime());
	}
}
