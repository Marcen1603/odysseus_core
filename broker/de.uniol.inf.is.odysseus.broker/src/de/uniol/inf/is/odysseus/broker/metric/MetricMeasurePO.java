package de.uniol.inf.is.odysseus.broker.metric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MetricMeasurePO<T extends IMetaAttribute> extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int attributePosition = -1;
	private String filename = "measure.txt";
	BufferedWriter out;
	private long currentLine[] = new long[4];
	private int currentrun = 0;

	public MetricMeasurePO(int attributePosition) {
		this.attributePosition = attributePosition;
	}

	public MetricMeasurePO(MetricMeasurePO<T> original) {
		super(original);
		this.attributePosition = original.attributePosition;
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
				currentLine[id] = offset;
				currentrun++;
				if(currentrun==4){
					currentrun=0;
					try {
						String line = "";
						String sep ="";
						for(int k=0;k<currentLine.length;k++){
							line=line+sep+currentLine[k];
							sep=";";
						}
						out.write(""+line);
						out.newLine();
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Tuple needed " + offset + " ("+currentTime+") ms \t"+tuple);
			} catch (ClassCastException e) {
				System.err.println("Only Long is supported for measuring!");
			}
		}

	}

}
