package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;


public class MySink extends AbstractSink<Object> {

	public MySink(){
	}
	
	public MySink(MySink original){
		super();
	}
	
	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		System.out.println("Port:" + port + ", Object:" + object.toString());
	}

	@Override
	public MySink clone()  {
		return new MySink(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		System.out.println("Port:" + port + ", PUNCTUATION: " + timestamp);
	}

	
}
