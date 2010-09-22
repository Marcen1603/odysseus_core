package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class SameDefaultRootStrategy implements IDefaultRootStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator connectDefaultRootToSource(
			ISink<?> defaultRoot, IPhysicalOperator source) {
		((ISource) source).connectSink((ISink) defaultRoot, 0, 0, source.getOutputSchema());		
		return defaultRoot;
	}

}
