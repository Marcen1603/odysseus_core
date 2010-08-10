package de.uniol.inf.is.odysseus.scars.operator.test.po;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.util.LoggerHelper;

public class TestPO<T> extends AbstractPipe<T, T>{

	private String name;
	
	public TestPO(String debugName) {
		this.name = debugName;
	}
	
	public TestPO(TestPO<T> copy ) {
		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		//BENUTZT GEFAELLIGST DEN LOGGER FUER SOWAS! (ja auch nur um kurz was zu testen)!
		//Ich will keine Sysouts irgendwo im code lesen! :D
		LoggerHelper.getInstance(this.name).debug("TESTPO: Dataelement processed : " + object);
		transfer(object);
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
