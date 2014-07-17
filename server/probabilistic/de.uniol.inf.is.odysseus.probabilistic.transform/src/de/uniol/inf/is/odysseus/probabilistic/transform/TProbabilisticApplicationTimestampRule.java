package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.relational_interval.transform.TApplicationTimestampRule;

public class TProbabilisticApplicationTimestampRule extends TApplicationTimestampRule{

	@Override
	public boolean isExecutable(TimestampAO operator,
			TransformationConfiguration transformConfig) {
		if (operator.getInputSchema(0).getType() == ProbabilisticTuple.class
				&& transformConfig.getMetaTypes().contains(
						ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}
	
}

