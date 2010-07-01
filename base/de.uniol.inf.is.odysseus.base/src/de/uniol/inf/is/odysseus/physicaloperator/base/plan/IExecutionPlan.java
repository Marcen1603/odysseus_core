package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public interface IExecutionPlan {
	
//	public void setRoots(List<IPhysicalOperator> roots);	
//	public List<IPhysicalOperator> getRoots();
	
	public void setLeafSources(List<IIterableSource<?>> leafSources);
	public List<IIterableSource<?>> getLeafSources();
	
	public void setPartialPlans(List<IPartialPlan> patialPlans);
	public List<IPartialPlan> getPartialPlans();
	
	public void close();
	public void open() throws OpenFailedException;

	public void initWith(IExecutionPlan newExecutionPlan);
}
