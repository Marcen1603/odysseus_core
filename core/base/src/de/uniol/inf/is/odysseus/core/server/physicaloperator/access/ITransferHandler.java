package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface ITransferHandler {

	void transfer();
	void sendPunctuation(PointInTime pointInTime);

}
