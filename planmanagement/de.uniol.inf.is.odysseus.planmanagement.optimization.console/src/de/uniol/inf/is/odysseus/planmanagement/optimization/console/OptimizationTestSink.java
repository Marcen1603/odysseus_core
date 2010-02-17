package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class OptimizationTestSink extends AbstractSink<Object> {
	
	private boolean doOutput;
	
	public OptimizationTestSink(boolean doOutput) {
		this.doOutput = doOutput;
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		if (doOutput) {
			System.out.println("Port:" + port + ", Object:" + object.toString());
		}
	}

	@Override
	public OptimizationTestSink clone() throws CloneNotSupportedException {
		return new OptimizationTestSink(doOutput);
	}
	
}
