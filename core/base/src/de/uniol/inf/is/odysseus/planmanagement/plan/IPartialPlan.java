package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IPartialPlan {
	public List<IIterableSource<?>> getIterableSource();
	public boolean hasIteratableSources();
	public IIterableSource<?> getIterableSource(int id);
	public int getSourceId(IIterableSource<?> source);
	public List<IPhysicalOperator> getRoots();
	
	public long getCurrentPriority();
	public void setCurrentPriority(long newPriority);
	public long getBasePriority();

	@Override
	public int hashCode();
	
	@Override
	public String toString();
	List<IPhysicalOperator> getQueryRoots();
	List<IQuery> getQueries();
}
