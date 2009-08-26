package de.uniol.inf.is.odysseus.base.planmanagement.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;

public interface IPlan extends
		IReoptimizeRequester<AbstractPlanReoptimizeRule>,
		IReoptimizeHandler<IPlanReoptimizeListener> {
	public IQuery getQuery(int queryID);

	public ArrayList<IQuery> getQueries();

	public ArrayList<IPhysicalOperator> getRoots();
}
