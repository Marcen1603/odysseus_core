package de.uniol.inf.is.odysseus.systemload.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

public class SystemLoadPO<R extends IStreamObject<? extends ISystemLoad>> extends AbstractPipe<R, R> {

	
	
	private String loadName;

	public SystemLoadPO(String loadName){
		this.loadName = loadName;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		object.getMetadata().addSystemLoad(loadName);
		transfer(object);
	}

}
