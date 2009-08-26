package de.uniol.inf.is.odysseus.base.planmanagement.query;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;

public interface IQuery extends IReoptimizeRequester<AbstractQueryReoptimizeRule>, IReoptimizeHandler<IQueryReoptimizeListener> {
	public int getID();

	public boolean isStarted();

	public ILogicalOperator getSealedLogicalPlan();

	public IPhysicalOperator getSealedRoot();

	public int getPriority();

	public void setPriority(int priority);
}
