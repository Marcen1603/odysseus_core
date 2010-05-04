package de.uniol.inf.is.odysseus.broker.metric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MetricMeasurePO<T extends IMetaAttribute> extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int attributePosition = -1;
	private int count = 0;
	private long sum = 0;
	private String filename = "measure.csv";
	BufferedWriter out;
	private HashMap<String, LastRun> firstOcc = new HashMap<String, LastRun>();
	private long startTime;

	public MetricMeasurePO(int attributePosition) {
		this.attributePosition = attributePosition;
	}

	public MetricMeasurePO(MetricMeasurePO<T> original) {
		super(original);
		this.attributePosition = original.attributePosition;
		this.firstOcc = original.firstOcc;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public MetricMeasurePO<T> clone() throws CloneNotSupportedException {
		return new MetricMeasurePO<T>(this);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.startTime = System.currentTimeMillis();
		super.process_open();
		try {
			out = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			OpenFailedException ex = new OpenFailedException(e);
			ex.fillInStackTrace();
			throw ex;
		}

	}

	@Override
	protected void process_done() {
		super.process_done();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process_next(RelationalTuple<T> tuple, int port) {
		measure(tuple);
		transfer(tuple);
	}

	private void measure(RelationalTuple<T> tuple) {
		if (attributePosition >= 0) {
			try {
				Long attribute = (Long) tuple.getAttribute(attributePosition);
				long currentTime = System.currentTimeMillis();
				long offset = currentTime - attribute.longValue();
				Integer idO = (Integer) tuple.getAttribute(1);
				int id = idO.intValue();
				Integer runO = (Integer) tuple.getAttribute(2);
				int run = runO.intValue();
				String type = (String) tuple.getAttribute(3);

				int lastrun = 0;
				if (firstOcc.containsKey(type)) {
					lastrun = firstOcc.get(type).lastRun[idO];
				} else {
					firstOcc.put(type, new LastRun());
				}

				if (run == 11) {
					System.out.println("Result: " + count + " Tuples | " + (sum / count) + " average | " + (currentTime - startTime) + " total time");
					System.exit(0);
				}

				if (lastrun < run) {
					long avg = 0;
					try {
						count++;
						sum = sum + offset;
						avg = sum / count;
						String line = id + ";" + offset + ";" + currentTime;
						out.write("" + line);
						out.newLine();
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					firstOcc.get(type).lastRun[id] = run;
					System.out.println("Tuple needed " + offset + " [" + (avg) + "] (" + currentTime + ") ms \t" + tuple);

				}
			} catch (ClassCastException e) {
				System.err.println("Only Long is supported for measuring!");
			}
		}

	}

	private class LastRun {
		public int lastRun[] = new int[4];

		public LastRun() {
			lastRun[0] = 0;
			lastRun[1] = 0;
			lastRun[2] = 0;
			lastRun[3] = 0;
		}
	}

}
