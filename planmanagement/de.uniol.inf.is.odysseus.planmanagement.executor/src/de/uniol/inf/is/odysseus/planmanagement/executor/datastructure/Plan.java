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

public class Plan implements IEditablePlan {

	private Map<Integer, Query> queries;

	private List<IPlanReoptimizeListener> reoptimizeListener = Collections.synchronizedList(new ArrayList<IPlanReoptimizeListener>());

	private List<AbstractPlanReoptimizeRule> reoptimizeRule = Collections.synchronizedList(new ArrayList<AbstractPlanReoptimizeRule>());

	public Plan() {
		queries = Collections.synchronizedMap(new HashMap<Integer, Query>());
	}

	@Override
	public synchronized boolean addQuery(IEditableQuery query) {
		if (query == null || queries.containsKey(query.getID())) {
			return false;
		}

		this.queries.put(query.getID(), (Query) query);

		return true;
	}

	@Override
	public synchronized Query removeQuery(int queryID) {
		return this.queries.remove(queryID);
	}

	@Override
	public synchronized Query getQuery(int queryID) {
		return this.queries.get(queryID);
	}

	@Override
	public synchronized ArrayList<IQuery> getQueries() {
		return new ArrayList<IQuery>(this.queries.values());
	}

	@Override
	public void reoptimize() {
		for (IPlanReoptimizeListener reoptimizationListener : this.reoptimizeListener) {
			reoptimizationListener.reoptimizeRequest(this);
		}
	}

	@Override
	public void addReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			if (!this.reoptimizeListener.contains(reoptimizationListener)) {
				this.reoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	@Override
	public void removeReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			this.reoptimizeListener.remove(reoptimizationListener);
		}
	}

	@Override
	public void addReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			if (!this.reoptimizeRule.contains(reoptimizeRule)) {
				this.reoptimizeRule.add(reoptimizeRule);
			}
		}
	}

	@Override
	public void removeReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			this.reoptimizeRule.remove(reoptimizeRule);
		}
	}

	@Override
	public ArrayList<IEditableQuery> getEdittableQueries() {
		return new ArrayList<IEditableQuery>(this.queries.values());
	}

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
