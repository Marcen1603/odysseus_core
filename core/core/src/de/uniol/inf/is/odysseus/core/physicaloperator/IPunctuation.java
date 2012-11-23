package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeComparable;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface IPunctuation extends ITimeComparable, IStreamable {

	PointInTime getTime();
	
	IPunctuation clone();
	
}
