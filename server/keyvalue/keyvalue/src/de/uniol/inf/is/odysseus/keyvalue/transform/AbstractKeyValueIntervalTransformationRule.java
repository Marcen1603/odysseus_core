package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Jan Soeren Schwarz
 */
public abstract class AbstractKeyValueIntervalTransformationRule<T extends ILogicalOperator> extends AbstractTransformationRule<T> {

	@Override
	public boolean isExecutable(T operator, TransformationConfiguration config) {
		if (KeyValueObject.class.isAssignableFrom(operator.getInputSchema(0).getType())
				&& config.getMetaTypes().contains(
						ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

}
