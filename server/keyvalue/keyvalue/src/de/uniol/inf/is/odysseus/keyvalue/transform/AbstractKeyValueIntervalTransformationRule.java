package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;

/**
 * @author Jan Soeren Schwarz
 */
public abstract class AbstractKeyValueIntervalTransformationRule<T extends ILogicalOperator> extends AbstractIntervalTransformationRule<T> {


	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends IStreamObject<?>> getStreamType() {
		return (Class<? extends IStreamObject<?>>) KeyValueObject.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends IStreamObject<?>> getStreamType2() {
		return (Class<? extends IStreamObject<?>>) NestedKeyValueObject.class;
	}

}
