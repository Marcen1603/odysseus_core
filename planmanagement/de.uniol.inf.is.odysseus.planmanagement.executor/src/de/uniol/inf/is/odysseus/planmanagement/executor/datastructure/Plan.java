package de.uniol.inf.is.odysseus.planmanagement.executor.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;

/**
 * Plan represents a map of all registered queries. 
 * 
 * @author Wolf Bauer
 *
 */
public class Plan implements IEditablePlan {

	/**
	 * Map of all registered queries.
	 */
	private Map<Integer, Query> queries;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<IPlanReoptimizeListener> reoptimizeListener = Collections.synchronizedList(new ArrayList<IPlanReoptimizeListener>());

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<AbstractPlanReoptimizeRule> reoptimizeRule = Collections.synchronizedList(new ArrayList<AbstractPlanReoptimizeRule>());

	/**
	 * Creates a new Plan.
	 */
	public Plan() {
		queries = Collections.synchronizedMap(new HashMap<Integer, Query>());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan#addQuery(de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery)
	 */
	@Override
	public synchronized boolean addQuery(IEditableQuery query) {
		if (query == null || queries.containsKey(query.getID())) {
			return false;
		}

		this.queries.put(query.getID(), (Query) query);

		return true;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan#removeQuery(int)
	 */
	@Override
	public synchronized Query removeQuery(int queryID) {
		return this.queries.remove(queryID);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan#getQuery(int)
	 */
	@Override
	public synchronized Query getQuery(int queryID) {
		return this.queries.get(queryID);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan#getQueries()
	 */
	@Override
	public synchronized ArrayList<IQuery> getQueries() {
		return new ArrayList<IQuery>(this.queries.values());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#reoptimize()
	 */
	@Override
	public void reoptimize() {
		for (IPlanReoptimizeListener reoptimizationListener : this.reoptimizeListener) {
			reoptimizationListener.reoptimizeRequest(this);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler#addReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void addReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			if (!this.reoptimizeListener.contains(reoptimizationListener)) {
				this.reoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler#removeReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void removeReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			this.reoptimizeListener.remove(reoptimizationListener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#addReoptimzeRule(de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void addReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			if (!this.reoptimizeRule.contains(reoptimizeRule)) {
				this.reoptimizeRule.add(reoptimizeRule);
				reoptimizeRule.addReoptimieRequester(this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester#removeReoptimzeRule(de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void removeReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			this.reoptimizeRule.remove(reoptimizeRule);
			reoptimizeRule.deinitialize();
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan#getEdittableQueries()
	 */
	@Override
	public ArrayList<IEditableQuery> getEdittableQueries() {
		return new ArrayList<IEditableQuery>(this.queries.values());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan#getRoots()
	 */
	@Override
	public ArrayList<IPhysicalOperator> getRoots() {
		ArrayList<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();

		for (IEditableQuery query : getEdittableQueries()) {
			if (!roots.contains(query.getSealedRoot())) {
				roots.add(query.getRoot());
			}
		}

		return roots;
	}
}
