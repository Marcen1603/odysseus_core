package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public abstract class AbstractIntervalTransformationRule<T extends ILogicalOperator>
		extends AbstractTransformationRule<T> {

	@Override
	public boolean isExecutable(T operator, TransformationConfiguration config) {
		if ((getStreamType() == null || operator.getInputSchema(0).getType() == getStreamType())
				&& config.getMetaTypes().contains(
						ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	protected Class<? extends IStreamObject<?>> getStreamType(){
		return null;
	}

}
