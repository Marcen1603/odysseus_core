package de.uniol.inf.is.odysseus.test.sinks.physicaloperator;

import de.uniol.inf.is.odysseus.test.StatusCode;

public interface ICompareSinkListener {
	public void compareSinkProcessingDone(TICompareSink sink, boolean done, StatusCode result);	
}
