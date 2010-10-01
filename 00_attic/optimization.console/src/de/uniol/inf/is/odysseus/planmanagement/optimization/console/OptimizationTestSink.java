package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class OptimizationTestSink extends AbstractSink<Object> {
	
	public enum OutputMode {
		NONE,
		NORMAL,
		COUNT,
		HASH
	}
	
	private OutputMode mode;
	private Integer count;
	private long nextOut;
	private int outputHash;
	
	public OptimizationTestSink(OutputMode mode) {
		this.mode = mode;
		reset();
	}
	
	public OptimizationTestSink(OptimizationTestSink oSink) {
		this.mode = oSink.mode;
		this.count = oSink.count;
		this.nextOut = oSink.nextOut;
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		switch (this.mode) {
		case NONE:
			break;
		case NORMAL:
			System.out.println("Port:" + port + ", Object:" + object.toString());
//			break; // auch bei Normal die Tupel zählen
		case COUNT:
			synchronized(count){
				this.count++;
			}
			break;
		case HASH:
			RelationalTuple<?> t = (RelationalTuple<?>)object;
			this.outputHash += t.getAttribute(0).toString().hashCode();
			if (++this.count % 50 == 0) {
				System.out.println("Output-Hash after "+this.count+" Objects: "+this.outputHash);
			}
			break;

		default:
			break;
		}
	}

//	@Override
//	protected void process_open() throws OpenFailedException {
//		reset();
//	}
	
//	@Override
//	protected void process_close() {
//		reset();
//	}
	
	@Override
	public OptimizationTestSink clone()  {
		return new OptimizationTestSink(this);
	}
	
	public void reset() {
		this.nextOut = System.currentTimeMillis() + 1000;
		this.count = 0;
		this.outputHash = 1;
	}
	
	public void doPendingOutput() {
		System.out.println("Sink: "+count+" Objects");
		this.nextOut = System.currentTimeMillis() + 1000;
	}

	public int getCount() {
		synchronized(count){
			return count;
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// Nothing to do
	}
	

	
}
