package de.uniol.inf.is.odysseus.generator.telephone;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Simulation extends Thread implements ICallDecriptionRecordReceiver {

	final private List<String> available = new ArrayList<>();
	private int currentCalls;
	final private int parallelCallCount;
	final private Random random = new Random(1);

	public Simulation(int parallelCallCount, int noOfTelefones) {
		for (int i = 0; i < noOfTelefones; i++) {
			available.add("No " + i);
		}
		this.parallelCallCount = parallelCallCount;
	}

	@Override
	public void run() {
		while (!interrupted()) {
			synchronized (this) {
				while (currentCalls == parallelCallCount) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				createNewCall();
			}
		}
	}

	private void createNewCall() {
		CallDescriptionRecord cdr = new CallDescriptionRecord();
		long sleepingTime = random.nextInt(50000)+1000;
		synchronized(available){
			cdr.src = available.remove(random.nextInt(available.size()));
			cdr.dst = available.remove(random.nextInt(available.size()));
			currentCalls++;
		}
		new TelephoneCall(cdr, sleepingTime, this).start();
		
	}

	@Override
	public void newCDR(CallDescriptionRecord cdr) {
		synchronized(available){
			available.add(cdr.src);
			available.add(cdr.dst);
			currentCalls--;
		}
		cdr.ts = System.currentTimeMillis();
		System.out.println(cdr);
		synchronized (this) {
			notifyAll();
		}
	}
	
	public static void main(String[] args) {
		System.err.print("Init ");
		long start = System.currentTimeMillis();
		Simulation sim = new Simulation(5000000, 50000000);
		System.err.println(" took "+((System.currentTimeMillis()-start)/60)+" seconds");
		System.err.println("Starting ");
		sim.start();
	}

}
