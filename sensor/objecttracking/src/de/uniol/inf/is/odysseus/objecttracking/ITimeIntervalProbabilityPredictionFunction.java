package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface ITimeIntervalProbabilityPredictionFunction<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends
		ITimeInterval, IProbability, IPredictionFunction<T, M> {

}
