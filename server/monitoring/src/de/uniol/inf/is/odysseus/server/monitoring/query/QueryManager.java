package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class QueryManager implements IUpdateEventListener {
	private static Map<IPhysicalQuery, List<SubQuery>> subquerys = new HashMap<IPhysicalQuery, List<SubQuery>>();
	private static final QueryManager manager = new QueryManager();

	private static Map<IPhysicalQuery, AverageLatency> latencys = new HashMap<IPhysicalQuery, AverageLatency>();
	private ThreadCalculateLatency thread = new ThreadCalculateLatency();

	// TODO: IUpdateEventListener.QUERY see AbstractExecutor
	private QueryManager() {
		OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
		thread.start();
	}

	public static QueryManager getInstance() {
		return manager;
	}

	/**
	 * Divides a query in a set of subqueries to handle the calculation of the
	 * latency
	 * 
	 * @param q
	 *            IPhysicalQuery
	 */
	private void divideQuery(IPhysicalQuery q) {
		boolean adding = true;
		while (adding) {
			adding = false;
			for (IPhysicalOperator operator : q.getPhysicalChilds()) {
				if (!q.getLeafSources().contains(operator) && !q.getIterableSources().contains(operator)
						&& !q.getIteratableLeafSources().contains(operator)) {
					List<IPhysicalOperator> subscriptions = getSubscriptions(operator);
					boolean added = false;
					if (subqueriesContainsOperator(q, operator)) {
						added = true;
					}
					// TODO: Remove identification about their name
					if (!added && (operator.getName().contains(
							"Buffer") /*
										 * || operator.getName().contains(
										 * "Aggregate")
										 */)) {
						for (SubQuery subquery : subquerys.get(q)) {
							if (!subquery.hasRoot()) {
								subquery.setRoot(operator);
								added = true;
								adding = true;
							}
						}
						if (!added) {
							SubQuery s = new SubQuery(thread);
							s.setRoot(operator);
							subquerys.get(q).add(s);
							added = true;
							adding = true;
						}
					}
					if (subscriptions.isEmpty() && !added) {
						for (SubQuery subquery : subquerys.get(q)) {
							if (!subquery.hasRoot()) {
								subquery.setRoot(operator);
								added = true;
							}
						}
						if (!added) {
							SubQuery s = new SubQuery(thread);
							s.setRoot(operator);
							subquerys.get(q).add(s);
							added = true;
							adding = true;
						}
					}

					if (!added) {
						// TODO: if (subscriptions.contains(JOIN) ||
						// subscriptions.contains(Aggregate)) {
						// setRoot(JOIN/Aggregate);
						// adding = true;
						// }
						for (IPhysicalOperator subscription : subscriptions) {
							if (subscription.getName().contains("Buffer")
							/*
							 * || subscription.getName().contains("Aggregate")
							 */) {
								SubQuery s = new SubQuery(thread);
								s.setRoot(operator);
								subquerys.get(q).add(s);
								added = true;
								adding = true;
							}
						}

						for (SubQuery subquery : subquerys.get(q)) {
							if (!added) {
								added = addOperatorToSubQuery(operator, subscriptions, subquery);
							}
						}

						if (!added) {
							SubQuery s = new SubQuery(thread);
							s.addOperator(operator);
							subquerys.get(q).add(s);
						}
					}
				}
			}
			if (adding) {
				for (SubQuery subquery : subquerys.get(q)) {
					for (int i = 0; i < subquery.getOperator().size(); i++) {
						subquery.removeOperator(subquery.getOperator().get(i));
					}
				}
			}
		}
		for (SubQuery subquery : subquerys.get(q)) {
			subquery.addEventListener();
		}
	}

	/**
	 * Checks if the operator is in any SubQuery for the given query.
	 * 
	 * @param query
	 *            IPhysicalQuery
	 * @param operator
	 *            IPhysicalOperator
	 * @return True if operator is in any SubQuery, false if not.
	 */
	private boolean subqueriesContainsOperator(IPhysicalQuery query, IPhysicalOperator operator) {
		for (SubQuery subquery : subquerys.get(query)) {
			if (subquery.containsOperator(operator)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds Operator to a SubQuery
	 * 
	 * @param o
	 *            IPhysicalOperator
	 * @param subscriptions
	 *            Subscriptions for o
	 * @param subquery
	 *            SubQuery
	 * @return True if the operator has been added to the subquery, false if
	 *         not.
	 */
	private boolean addOperatorToSubQuery(IPhysicalOperator o, List<IPhysicalOperator> subscriptions,
			SubQuery subquery) {
		for (IPhysicalOperator subscription : subscriptions) {
			if (subquery.containsOperator(subscription)) {
				subquery.addOperator(o);
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds all subscriptions to other Operator for the given Operator.
	 * 
	 * @param o
	 *            IPhysicalOperator to look for subsciptions.
	 * @return List of operator to which o is subscribed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<IPhysicalOperator> getSubscriptions(IPhysicalOperator o) {
		List<IPhysicalOperator> subscriptions = new ArrayList<IPhysicalOperator>();

		ISource source = (ISource) o;
		if (source instanceof AbstractSource) {
			Collection<ControllablePhysicalSubscription> sourceSubscriptions = (source).getSubscriptions();
			System.out.println(sourceSubscriptions.toString());
			for (ControllablePhysicalSubscription subscription : sourceSubscriptions) {
				subscriptions.add((IPhysicalOperator) subscription.getTarget());
			}
		}
		return subscriptions;
	}

	/**
	 * Calculates latency over the last 20 tuple for the whole Query
	 * 
	 * @param q
	 *            Query for which the latency is calculated
	 */
	public static void calcAverageLatency(IPhysicalQuery q) {
		for (SubQuery subquery : subquerys.get(q)) {
			AverageLatency l = latencys.get(q);
			if (l == null) {
				l = new AverageLatency();
				l.addLatency(getTotalQueryLatency(q));
			} else {
				l.addLatency(getTotalQueryLatency(q));
			}
			latencys.put(q, l);
		}
		System.out.println(latencys.get(q).getAverageLatency());
	}

	private static long getTotalQueryLatency(IPhysicalQuery q) {
		long sum = 0;
		for (SubQuery subquery : subquerys.get(q)) {
			sum += subquery.getMeasurement().getCurrentLatency();
		}
		return sum;
	}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.QUERY) {
			System.out.println("IUpdateEventListener.QUERY");
			refreshQuerys();
		}
	}

	// TODO: Where do i get the queries from
	private void refreshQuerys() {
		ISession session = OdysseusRCPPlugIn.getActiveSession();
		List<StoredProcedure> storedProcedures = OdysseusRCPPlugIn.getExecutor().getStoredProcedures(session);
		System.out.println(storedProcedures.toString());
	}

	/**
	 * Adds a new query to the querymanager if it not exists
	 * 
	 * @param q
	 *            Query which will be added
	 */
	public void addQuery(IPhysicalQuery q) {
		if (!subquerys.keySet().contains(q)) {
			List<SubQuery> list = new ArrayList<SubQuery>();
			list.add(new SubQuery(thread));
			subquerys.put(q, list);
			divideQuery(q);
		}
	}
}

class SubQuery {
	private ThreadCalculateLatency thread;
	private Measurement measurement;
	private ArrayList<IPhysicalOperator> operator;
	private IPhysicalOperator root;
	private QueryEventListener eventListener;
	private long lastLatency;

	public SubQuery() {
		if (getThread() == null) {
			setThread(new ThreadCalculateLatency());
			getThread().start();
		}
		if (getMeasurement() == null) {
			setMeasurement(new Measurement());
		}
		if (getOperator() == null) {
			setOperator(new ArrayList<IPhysicalOperator>());
		}
		if (getEventListener() == null) {
			setEventListener(new QueryEventListener(getMeasurement(), getThread()));
		}
	}

	public SubQuery(ThreadCalculateLatency t) {
		if (getThread() == null) {
			setThread(t);
		}
		if (getMeasurement() == null) {
			setMeasurement(new Measurement());
		}
		if (getOperator() == null) {
			setOperator(new ArrayList<IPhysicalOperator>());
		}
		if (getEventListener() == null) {
			setEventListener(new QueryEventListener(getMeasurement(), getThread()));
		}
	}

	/**
	 * Checks if the SubQuery contains an Operator.
	 * 
	 * @param o
	 *            IPhysicalOperator to check.
	 * @return true if the operator is included, false if not.
	 */
	public boolean containsOperator(IPhysicalOperator o) {
		if (getRoot() != null && getRoot().equals(o)) {
			return true;
		}
		if (getOperator() != null && getOperator().contains(o)) {
			return true;
		}
		return false;
	}

	public boolean hasRoot() {
		if (getRoot() == null) {
			return false;
		}
		return true;
	}

	public void addOperator(IPhysicalOperator o) {
		if (getOperator().contains(o)) {
			return;
		}
		getOperator().add(o);
	}

	public void addEventListener() {
		for (IPhysicalOperator o : this.getOperator()) {
			getEventListener().addOperator(o);
		}
		if (getRoot().getName().contains("Buffer")) {
			getEventListener().addBuffer(getRoot());
		} else {
			getEventListener().addOperator(getRoot());
		}
	}

	public void removeOperator(IPhysicalOperator o) {
		if (getOperator() != null && getOperator().contains(o)) {
			// getEventListener().removeOperator(o);
			getOperator().remove(o);
			if (operator.isEmpty()) {
				// stopThread();
			}
		}

	}

	public void stopThread() {
		thread.shutdown();
	}

	public ThreadCalculateLatency getThread() {
		return thread;
	}

	public void setThread(ThreadCalculateLatency thread) {
		this.thread = thread;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public ArrayList<IPhysicalOperator> getOperator() {
		return operator;
	}

	public void setOperator(ArrayList<IPhysicalOperator> operator) {
		this.operator = operator;
	}

	public IPhysicalOperator getRoot() {
		return root;
	}

	public void setRoot(IPhysicalOperator root) {
		this.root = root;
		if (getMeasurement() != null) {
			getMeasurement().addRoot(root);
		}
	}

	public QueryEventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(QueryEventListener eventListener) {
		this.eventListener = eventListener;
	}

	public long getLastLatency() {
		return lastLatency;
	}

	public void setLastLatency(long latency) {
		this.lastLatency = latency;
	}
}

class AverageLatency {
	private long averageLatency;
	private int pointer = 0;
	private long[] lastLatencys = new long[20];

	public AverageLatency() {

	}

	public void addLatency(long latency) {
		averageLatency = 0;
		lastLatencys[pointer] = latency;
		for (long l : lastLatencys) {
			averageLatency += l;
		}
		averageLatency = averageLatency / lastLatencys.length;
		pointer = (pointer++) % lastLatencys.length;
	}

	public long getAverageLatency() {
		return averageLatency;
	}

	public void setAverageLatency(long averageLatency) {
		this.averageLatency = averageLatency;
	}

	public long[] getLastLatencys() {
		return lastLatencys;
	}

	public void setLastLatencys(long[] lastLatencys) {
		this.lastLatencys = lastLatencys;
	}

}