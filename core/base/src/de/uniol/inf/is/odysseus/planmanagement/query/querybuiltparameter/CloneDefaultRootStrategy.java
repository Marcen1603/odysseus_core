package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyPhysicalGraphVisitor;

public class CloneDefaultRootStrategy implements IDefaultRootStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator connectDefaultRootToSource(ISink<?> defaultRoot,
			IPhysicalOperator source) {
		// the default root can be a whole plan. So the whole plan must be cloned.
		CopyPhysicalGraphVisitor copyVis = new CopyPhysicalGraphVisitor<IPhysicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalkPhysical(defaultRoot, copyVis);
		
		
		ISink<?> defaultRootCopy = (ISink<?>)copyVis.getResult();
		
		((ISource) source).connectSink((ISink) defaultRootCopy, 0, 0, source.getOutputSchema());
		return defaultRootCopy;
	}

}
