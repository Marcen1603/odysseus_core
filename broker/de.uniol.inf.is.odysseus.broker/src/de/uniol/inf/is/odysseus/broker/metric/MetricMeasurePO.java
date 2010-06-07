package de.uniol.inf.is.odysseus.broker.metric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.base.CloseFailedException;
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
	private long startTime;

	public MetricMeasurePO(int attributePosition) {
		this.attributePosition = attributePosition;
	}

	public MetricMeasurePO(MetricMeasurePO<T> original) {
		super(original);
		this.attributePosition = original.attributePosition;
		this.startTime = original.startTime;
		this.count = original.count;
		this.sum = original.sum;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public MetricMeasurePO<T> clone(){
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
	protected void process_close() throws CloseFailedException {
		System.out.println("Processing takes " + (System.currentTimeMillis() - this.startTime) + " ms");
		super.process_close();
		try {
			out.close();
		} catch (IOException e) {
			CloseFailedException ex = new CloseFailedException(e);
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

				long avg = 0;
				try {
					count++;
					sum = sum + offset;
					avg = sum / count;
					String line = offset + ";" + currentTime;
					out.write("" + line);
					out.newLine();
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Tuple needed " + offset + " [" + (avg) + "] (" + currentTime + ") ms \t" + tuple);

			} catch (ClassCastException e) {
				System.err.println("Only Long is supported for measuring!");
			}
		}

	}

}
