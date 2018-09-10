package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.relational_interval.transform.TApplicationTimestampRule;

public class TProbabilisticApplicationTimestampRule extends TApplicationTimestampRule{

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends IStreamObject<?>> getStreamType() {
		return (Class<? extends IStreamObject<?>>) ProbabilisticTuple.class;
	}
		
}

