package de.uniol.inf.is.odysseus.scars.operator.test.po;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class TestPO<T> extends AbstractPipe<T, T>{

	public TestPO() {
	}
	
	public TestPO(TestPO<T> copy ) {
		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	public void process_open(){
	}

	@Override
	protected void process_next(T object, int port) {
		transfer(object);
	}
	
	@Override
	public void process_done(){
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TestPO<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
}
