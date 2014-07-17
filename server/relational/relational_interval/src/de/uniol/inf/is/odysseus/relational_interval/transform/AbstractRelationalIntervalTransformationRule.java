package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public abstract class AbstractRelationalIntervalTransformationRule<T extends ILogicalOperator>
		extends AbstractTransformationRule<T> {

	@Override
	public boolean isExecutable(T operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class
				&& config.getMetaTypes().contains(
						ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

}
