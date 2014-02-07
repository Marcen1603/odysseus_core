package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;


public class NumericalHistogram<K extends Number,V extends IStreamObject<? extends ITimeInterval>> extends Histogram<K,V>{

	@Override
	K getMedian() {
		throw new IllegalArgumentException("Currently not implemented");
	}

}
