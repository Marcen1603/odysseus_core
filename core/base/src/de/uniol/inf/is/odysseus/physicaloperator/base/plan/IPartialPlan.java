package de.uniol.inf.is.odysseus.physicaloperator.base.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;

public interface IPartialPlan {
	public List<IIterableSource<?>> getIterableSource();
	public IIterableSource<?> getIterableSource(int id);
	public int getSourceId(IIterableSource<?> source);
	public List<IPhysicalOperator> getRoots();
	
	public int getCurrentPriority();
	public void setCurrentPriority(int newPriority);
	public int getBasePriority();

	public int hashCode();
	
	public String toString();
}
