package de.uniol.inf.is.odysseus.scars.operator.test.po;

import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

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
	protected void process_next(T object, int port) {
		System.out.println("TESTPO: Dataelement processed : " + object);
		transfer(object);
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TestPO<T>(this);
	}
}
