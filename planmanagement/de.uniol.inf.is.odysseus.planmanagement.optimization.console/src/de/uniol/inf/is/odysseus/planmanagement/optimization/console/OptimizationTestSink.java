package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.util.Date;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class OptimizationTestSink extends AbstractSink<Object> {
	
	public enum OutputMode {
		NONE,
		NORMAL,
		COUNT
	}
	
	private OutputMode mode;
	private int count;
	private long nextOut;
	
	public OptimizationTestSink(OutputMode mode) {
		this.mode = mode;
		this.nextOut = System.currentTimeMillis() + 1000;
		this.count = 0;
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		switch (this.mode) {
		case NONE:
			break;
		case NORMAL:
			System.out.println("Port:" + port + ", Object:" + object.toString());
			break;
		case COUNT:
			this.count++;
			if (System.currentTimeMillis() > this.nextOut) {
				System.out.println("Port:" + port + ", "+count+" Objects");
				this.nextOut = System.currentTimeMillis() + 1000;
				this.count = 0;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public OptimizationTestSink clone() throws CloneNotSupportedException {
		return new OptimizationTestSink(mode);
	}
	
}
