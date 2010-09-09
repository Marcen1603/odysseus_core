package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.base.CopyPhysicalPlanVisitor;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyPhysicalGraphVisitor;

public class CloneDefaultRootStrategy implements IDefaultRootStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator subscribeDefaultRootToSource(ISink<?> defaultRoot,
			IPhysicalOperator source) {
		// the default root can be a whole plan. So the whole plan must be cloned.
		CopyPhysicalGraphVisitor copyVis = new CopyPhysicalGraphVisitor<IPhysicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalkPhysical(defaultRoot, copyVis);
		
		
		ISink<?> defaultRootCopy = (ISink<?>)copyVis.getResult();
		
		((ISink) defaultRootCopy).subscribeToSource((ISource) source, 0,
				0, source.getOutputSchema());
		return defaultRootCopy;
	}

}
