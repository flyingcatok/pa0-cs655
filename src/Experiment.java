
public class Experiment {

	public static void main(String[] args) throws Exception {
		for(int i = 0; i<9; i++){
			FIFORouter fifo = new FIFORouter(i);
			fifo.controller();
			RRRouter rr = new RRRouter(i);// 0-8
			rr.controller();
			DRRRouter drr = new DRRRouter(i);
			drr.controller();
		}
	}
}
