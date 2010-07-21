package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class CloneDefaultRootStrategy implements IDefaultRootStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator subscribeDefaultRootToSource(ISink<?> defaultRoot,
			IPhysicalOperator source) {
		IPhysicalOperator clonedRoot = defaultRoot.clone();
		((ISink) clonedRoot).subscribeToSource((ISource) source, 0,
				0, source.getOutputSchema());
		return clonedRoot;
	}

}
