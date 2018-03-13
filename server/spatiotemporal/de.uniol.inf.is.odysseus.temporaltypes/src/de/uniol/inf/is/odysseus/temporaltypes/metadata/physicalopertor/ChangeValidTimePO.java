package de.uniol.inf.is.odysseus.temporaltypes.metadata.physicalopertor;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.logicaloperator.ChangeValidTimeAO;

public class ChangeValidTimePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	
	private int valueToAddStart;
	private int valueToAddEnd;
	
	public ChangeValidTimePO(ChangeValidTimeAO ao) {
		this.valueToAddStart = ao.getValueToAddStart();
		this.valueToAddEnd = ao.getValueToAddEnd();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do nothing	
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			ITimeInterval streamTime = (ITimeInterval) metadata;
			PointInTime newValidStart = streamTime.getStart().plus(valueToAddStart);
			PointInTime newValidEnd = streamTime.getStart().plus(valueToAddEnd);
			if (metadata instanceof IValidTime) {
				IValidTime validTime = (IValidTime) metadata;
				validTime.setValidStartAndEnd(newValidStart, newValidEnd);
			}
		}
		transfer(object);
	}
}
