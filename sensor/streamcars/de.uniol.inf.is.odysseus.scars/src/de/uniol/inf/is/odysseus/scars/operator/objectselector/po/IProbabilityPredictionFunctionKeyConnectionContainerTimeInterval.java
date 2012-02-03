package de.uniol.inf.is.odysseus.scars.operator.objectselector.po;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;

public interface IProbabilityPredictionFunctionKeyConnectionContainerTimeInterval<K>
		extends IProbability, IPredictionFunctionKey<K>, IConnectionContainer,
		ITimeInterval {

}
