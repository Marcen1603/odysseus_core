package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public interface IExecutionPlan {
	public List<IIterableSource<?>> getSources();
	
	public List<IPartialPlan> getPartialPlans();
	
	public List<IPhysicalOperator> getRoots();
}
