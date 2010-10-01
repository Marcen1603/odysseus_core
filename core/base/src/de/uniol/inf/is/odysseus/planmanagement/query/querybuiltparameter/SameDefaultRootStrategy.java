package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;

public class SameDefaultRootStrategy implements IDefaultRootStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator connectDefaultRootToSource(
			ISink<?> defaultRoot, IPhysicalOperator source) {
		((ISource) source).connectSink((ISink) defaultRoot, 0, 0, source.getOutputSchema());		
		return defaultRoot;
	}

}
