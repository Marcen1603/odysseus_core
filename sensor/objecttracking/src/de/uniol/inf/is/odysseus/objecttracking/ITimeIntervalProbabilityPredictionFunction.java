package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface ITimeIntervalProbabilityPredictionFunction<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends
		ITimeInterval, IProbability, IPredictionFunction<T, M> {

}
