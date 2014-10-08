/**
 * 
 * Simulator interface.
 * @author Feiyu Shi
 * @collaborator Rakshit Sachdev
 * Date: 10/7/2014
 *
 */
public interface Simulator {
	
	/**
	 * Initialize schedule.
	 */
	public void initSchedule();
	
	/**
	 * Execute birth event.
	 * @param birthEvent Event
	 */
	public void birth(Event birthEvent);
	
	/**
	 * Execute death event.
	 * @param deathEvent Event
	 */
	public void death(Event deathEvent);
	
	/**
	 * Control the simulator according to the schedule.
	 */
	public void controller();
	
}
