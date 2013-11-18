package de.uniol.inf.is.odysseus.generator.telephone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;

public class Simulation extends Thread implements ICallDecriptionRecordReceiver {

	final private List<String> availableTelephones = new ArrayList<>();
	final private List<IPair<IPair<Double, Double>, Integer>> availableLocations = new ArrayList<>(); 
	// inner pair: latitude, longitude; outer pair: coordinates, district
	private int currentCalls;
	final private int parallelCallCount;
	final private Random random = new Random(1);

	final List<ICallDecriptionRecordReceiver> listeners = new ArrayList<>();
	
	public Simulation(int parallelCallCount, int noOfTelefones, int noLatitudes, int noLongitudes) {
		for (int i = 0; i < noOfTelefones; i++) {
			availableTelephones.add("No " + i);
		}
		for (double latitude = 0.0; latitude < noLatitudes; latitude += 1.0) {
			for (double longitude = 0.0; longitude < noLongitudes; longitude += 1.0) {
				IPair<Double,Double> coordinates = new Pair<Double,Double>(latitude, longitude);
				int district = (int) (Math.floor(noLongitudes / 5) * Math.floor(latitude / 5) + Math.floor(longitude / 5));
				// creates districts with a size of 5*5 coordinates
				availableLocations.add(new Pair<IPair<Double,Double>, Integer>(coordinates,district));
			}
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
		synchronized(availableTelephones){
			cdr.src = availableTelephones.remove(random.nextInt(availableTelephones.size()));
			cdr.dst = availableTelephones.remove(random.nextInt(availableTelephones.size()));
			currentCalls++;
		}
		synchronized(availableLocations){
			IPair<IPair<Double,Double>,Integer> location = availableLocations.remove(random.nextInt(availableLocations.size()));
			cdr.lat = location.getE1().getE1();
			cdr.lon = location.getE1().getE2();
			cdr.district = location.getE2();
		}
		new TelephoneCall(cdr, sleepingTime, this).start();
		
	}

	@Override
	public void newCDR(CallDescriptionRecord cdr) {
		synchronized(availableTelephones){
			availableTelephones.add(cdr.src);
			availableTelephones.add(cdr.dst);
			currentCalls--;
		}
		synchronized (availableLocations) {
			IPair<Double,Double> coordinates = new Pair<Double,Double>(cdr.lat, cdr.lon);
			availableLocations.add(new Pair<IPair<Double,Double>, Integer>(coordinates,cdr.district));
		}
		cdr.ts = System.currentTimeMillis();
		for (ICallDecriptionRecordReceiver l: listeners){
			l.newCDR(cdr);
		}
		synchronized (this) {
			notifyAll();
		}
	}
	
	public static void main(String[] args) {
		System.err.print("Init ");
		long start = System.currentTimeMillis();
		Simulation sim = new Simulation(5000000, 7000000, 180, 360);
		System.err.println(" took "+((System.currentTimeMillis()-start)/60)+" seconds");
		System.err.println("Starting ");
		sim.start();
	}

	public void addListener(ICallDecriptionRecordReceiver listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ICallDecriptionRecordReceiver listener){
		listeners.remove(listener);
	}

}
