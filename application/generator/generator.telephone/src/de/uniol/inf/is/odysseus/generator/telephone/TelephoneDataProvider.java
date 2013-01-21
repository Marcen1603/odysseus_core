package de.uniol.inf.is.odysseus.generator.telephone;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class TelephoneDataProvider extends StreamClientHandler implements
		ICallDecriptionRecordReceiver {

	Simulation sim = null;
	List<CallDescriptionRecord> elems = new LinkedList<>();

	final private int parallelCallCount;
	final private int noOfTelephones;
	final private int blockSize;

	public TelephoneDataProvider(int parallelCallCount, int noOfTelephones, int blockSize){
		this.parallelCallCount = parallelCallCount;
		this.noOfTelephones = noOfTelephones;
		this.blockSize = blockSize;
	}

	public TelephoneDataProvider(TelephoneDataProvider telephoneDataProvider) {
		super();
		this.parallelCallCount = telephoneDataProvider.parallelCallCount;
		this.noOfTelephones = telephoneDataProvider.noOfTelephones;
		this.blockSize = telephoneDataProvider.blockSize;
	}

	@Override
	public void init() {
		elems.clear();
		sim = new Simulation(parallelCallCount, noOfTelephones);
		sim.start();
		sim.addListener(this);
	}

	@Override
	public void close() {
		sim.interrupt();
		sim.removeListener(this);
	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		CallDescriptionRecord cdr;
		List<DataTuple> ret = new LinkedList<>();
		synchronized (elems) {
			while (elems.size() <= blockSize) {
				elems.wait(1000);
			}
			for (int i = 0; i < blockSize; i++) {

				cdr = elems.remove(0);

				DataTuple tuple = new DataTuple();
				tuple.addAttribute(new Long(cdr.ts));
				tuple.addAttribute(cdr.src);
				tuple.addAttribute(cdr.dst);
				tuple.addAttribute(new Long(cdr.start));
				tuple.addAttribute(new Long(cdr.end));
				tuple.addAttribute(new Integer(cdr.district));
				tuple.addAttribute(new Double(cdr.lat));
				tuple.addAttribute(new Double(cdr.lon));
				ret.add(tuple);
			}
		}

		return ret;
	}

	@Override
	public StreamClientHandler clone() {
		return new TelephoneDataProvider(this);
	}

	@Override
	public void newCDR(CallDescriptionRecord cdr) {
		synchronized (elems) {
			elems.add(cdr);
			elems.notifyAll();
		}
	}
}
