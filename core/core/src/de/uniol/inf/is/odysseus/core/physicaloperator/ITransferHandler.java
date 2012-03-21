package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface ITransferHandler<W> {

	void transfer(W toTransfer);
	void sendPunctuation(PointInTime pointInTime);

}
