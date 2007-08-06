package weasim;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Simulation extends Thread {
	List<ObjectOutputStream> outputs;

	private TimePeriod timePeriod;

	private List<Object> simData;

	public Simulation(TimePeriod timePeriod, List<Object> simData) {
		this.timePeriod = timePeriod;
		this.simData = simData;
		if (simData.size() < 1) {
			throw new IllegalArgumentException("empty simulation data");
		}
		this.outputs = new ArrayList<ObjectOutputStream>();
	}

	public void addOutput(ObjectOutputStream out) {
		synchronized (this.outputs) {
			this.outputs.add(out);
			this.outputs.notifyAll();
		}
	}

	public void run() {
		try {
			synchronized (this.outputs) {
				if (this.outputs.isEmpty()) {
					this.outputs.wait();
				}
			}

			ListIterator<Object> it = this.simData.listIterator();
			while (!isInterrupted()) {
				//wenn das ende der daten erreicht wurde, von vorne beginnen
				if (!it.hasNext()) {
					it = this.simData.listIterator();
					continue;
				}

				Object data = it.next();
				// ohne synchronisation darf remove nicht aufgerufen werden
				// und man muesste das entfernen zu einem spaeteren zeitpunkt
				// durchfuehren
				synchronized (this.outputs) {
					ListIterator<ObjectOutputStream> itOut = this.outputs
							.listIterator();
					while (itOut.hasNext()) {
						try {
							itOut.next().writeObject(data);
						} catch (IOException e) {
							System.out.println("Client disconnected");
							itOut.remove();
						}
					}
					if (this.outputs.isEmpty()) {
						this.outputs.wait();
					}
				}
				synchronized (this) {
					long tP = this.timePeriod.getNextWaitTime();
					sleep(tP);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
