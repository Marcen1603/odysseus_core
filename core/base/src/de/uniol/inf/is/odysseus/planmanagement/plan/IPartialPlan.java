package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IPartialPlan {
	public long getId();
	public List<IIterableSource<?>> getIterableSources();
	public boolean hasIteratableSources();
	public IIterableSource<?> getIterableSource(int id);
	public int getSourceId(IIterableSource<?> source);
	public List<IPhysicalOperator> getRoots();
	
	public long getCurrentPriority();
	public void setCurrentPriority(long newPriority);
	public long getBasePriority();

	public void setSLARate(double rate);
	public double getSLARate();

	public void setSLAInfo(String info);
	public String getSLAInfo();

	
	@Override
	public int hashCode();
	
	@Override
	public String toString();
	List<IPhysicalOperator> getQueryRoots();
	List<IQuery> getQueries();

}
