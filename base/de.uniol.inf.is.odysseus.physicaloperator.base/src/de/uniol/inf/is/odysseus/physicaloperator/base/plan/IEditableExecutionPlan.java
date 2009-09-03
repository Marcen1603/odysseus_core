package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public interface IEditableExecutionPlan extends IExecutionPlan {
	public void setSources(List<IIterableSource<?>> iterableSources);
	
	public void setPartialPlans(List<IPartialPlan> patialPlans);
	
	public void setRoots(List<IPhysicalOperator> roots);
	
	public void close();

	public void open() throws OpenFailedException;
}
