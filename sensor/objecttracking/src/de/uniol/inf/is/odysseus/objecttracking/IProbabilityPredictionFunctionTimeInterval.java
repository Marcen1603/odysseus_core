package de.uniol.inf.is.odysseus.objecttracking;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface IProbabilityPredictionFunctionTimeInterval<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends
		IProbability, IPredictionFunction<T, M>, ITimeInterval {

}
