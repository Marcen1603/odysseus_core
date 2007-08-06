package weasimulation;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Simulation extends Thread {
	List<Writer> outputs;

	private TimePeriod timePeriod;

	private FileChannel simData;

	public Simulation(TimePeriod timePeriod, FileChannel simData) {
		this.timePeriod = timePeriod;
		this.simData = simData;
		this.outputs = new ArrayList<Writer>();
	}

	public void addOutput(Writer out) {
		synchronized (this.outputs) {
			this.outputs.add(out);
			this.outputs.notifyAll();
		}
	}

	public void run() {
		ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
		int n = 0;

		try {
			synchronized (this.outputs) {
				if (this.outputs.isEmpty()) {
					this.outputs.wait();
				}
			}

			long size = simData.size();
			while (true) {
				String data = new String();
				while (n > 0 || simData.position() < size) {
					if (n < 1) {
						buffer.clear();
						n = simData.read(buffer);
						buffer.rewind();
						continue;
					}

					char c = (char) buffer.get();
					--n;
					data += c;
					if (c == '\n') {
						break;
					}
				}

				// if end of file is reached, start reading from the
				// beginning
				if (simData.position() == size) {
					simData.position(0);
				}

				// ohne synchronisation darf remove nicht aufgerufen werden
				// und man muesste das entfernen zu einem spaeteren zeitpunkt
				// durchfuehren
				synchronized (this.outputs) {
					ListIterator<Writer> it = this.outputs.listIterator();
					while (it.hasNext()) {
						try {
							System.out.println(System.currentTimeMillis());
							Writer w = it.next();
							w.write(data);
							w.flush();
						} catch (IOException e) {
							System.out.println("Client disconnected");
							it.remove();
						}
					}
					if (this.outputs.isEmpty()) {
						this.outputs.wait();
					}
				}
				synchronized (this) {
					this.wait(timePeriod.getNextWaitTime());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
