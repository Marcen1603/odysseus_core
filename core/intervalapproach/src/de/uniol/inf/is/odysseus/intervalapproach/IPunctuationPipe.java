package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;

/**
 * Encapsulates the functionality to handle the cleaning process of internal states
 * of an operator in presence of punctuations getting out of date.
 * @author jan
 *
 */
public interface IPunctuationPipe<W,R> extends IPipe<W,R>{
	/**
	 * This function should be called if a punctuation is out of date and some internal
	 * cleaning up is required.
	 * @param punctuation the punctuation getting out of date
	 * @param current the current processed data stream element
	 */
	public boolean cleanInternalStates(PointInTime punctuation, IMetaAttributeContainer<?> current);
}
