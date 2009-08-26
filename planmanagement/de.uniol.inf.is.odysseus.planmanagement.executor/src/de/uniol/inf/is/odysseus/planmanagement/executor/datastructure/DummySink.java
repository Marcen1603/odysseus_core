package de.uniol.inf.is.odysseus.planmanagement.executor.datastructure;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class DummySink extends AbstractSink<Object>{
	
	private int step;
	
	public DummySink() {
		super();
		this.step = 0;
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		this.step++;
		System.out.println(object);
	}

	public int getStep() {
		return this.step;
	}
	
}
