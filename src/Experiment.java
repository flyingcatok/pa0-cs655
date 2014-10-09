
public class Experiment {

	public static void main(String[] args) throws Exception {
//		FIFORouter fifo = new FIFORouter(0);
//		fifo.controller();
//		RRRouter rr = new RRRouter(0);
//		rr.controller();
		DRRRouter drr = new DRRRouter(0);
		drr.controller();
	}
}
