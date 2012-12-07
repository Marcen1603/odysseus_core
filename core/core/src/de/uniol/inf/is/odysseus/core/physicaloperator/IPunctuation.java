package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeComparable;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface IPunctuation extends ITimeComparable, IStreamable {

	/**
	 * Every punctuation needs a time to allow the ordering regarding the stream
	 * @return
	 */
	PointInTime getTime();
	/**
	 * States that this puncutation is a heartbeat (i.e. states time progress). This is used to avoid
	 * instanceof calls at runtime. If the element returns true, sequent punctuations may compress (i.e.
	 * only the youngest punctuation is send)
	 * @return
	 */
	boolean isHeartbeat();
	
	IPunctuation clone();
	
}
