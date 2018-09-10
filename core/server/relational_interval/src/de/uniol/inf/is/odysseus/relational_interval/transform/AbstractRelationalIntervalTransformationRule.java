package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;

public abstract class AbstractRelationalIntervalTransformationRule<T extends ILogicalOperator>
		extends AbstractIntervalTransformationRule<T> {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends IStreamObject<?>> getStreamType() {
		return (Class<? extends IStreamObject<?>>) Tuple.class;
	}


}
