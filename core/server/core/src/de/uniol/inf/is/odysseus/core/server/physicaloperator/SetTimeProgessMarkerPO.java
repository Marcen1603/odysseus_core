package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class SetTimeProgessMarkerPO<R extends IStreamObject<?>> extends AbstractPipe<R, R> {

	private boolean newValue;
	
	public SetTimeProgessMarkerPO(boolean newValue) {
		this.newValue = newValue;
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
		object.setTimeProgressMarker(newValue);
		transfer(object);
	}

}
