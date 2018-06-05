package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class QueryManager implements IUpdateEventListener {
	private static final Logger LOG = LoggerFactory.getLogger(QueryManager.class);

	private static Map<IPhysicalQuery, List<_SubQuery>> subquerys = new HashMap<IPhysicalQuery, List<_SubQuery>>();
	private static QueryManager instance;
	private static IServerExecutor executor = null;
	private ISession superUser = null;

	private static Map<IPhysicalQuery, _AverageLatency> averageLatencys = new HashMap<IPhysicalQuery, _AverageLatency>();
	private static ThreadCalculateLatency thread = new ThreadCalculateLatency();

	public QueryManager() {
		thread.start();
		instance = this;
	}

	public static QueryManager getInstance() {
		return instance;
	}

	public void bindExecutor(IExecutor ex) throws PlanManagementException {
		executor = (IServerExecutor) ex;
		String name = executor.getName();
		if (name == null) {
			name = "";
		}
		superUser = SessionManagement.instance.loginSuperUser(null);
		executor.addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
	}

	public void unbindExecutor(IExecutor ex) {
		if (executor == (IServerExecutor) ex) {
			executor.removeUpdateEventListener(this, IUpdateEventListener.QUERY, null);
			executor = null;
		}
	}

	/**
	 * Divides a query in a set of subqueries to handle the calculation of the
	 * latency
	 * 
	 * @param q
	 *            IPhysicalQuery
	 */
	private void divideQuery(IPhysicalQuery q) {
		// adding is used to end the divide process
		boolean adding = true;
		while (adding) {
			adding = false;
			for (IPhysicalOperator operator : q.getPhysicalChilds()) {
				// Exclude sources from monitoring
				if (!q.getLeafSources().contains(operator) && !q.getIterableSources().contains(operator)
						&& !q.getIteratableLeafSources().contains(operator)) {
					List<IPhysicalOperator> subscriptions = getSubscriptions(operator);
					boolean added = false;
					if (subqueriesContainsOperator(q, operator)) {
						added = true;
					}
					// TODO: Remove identification by the operator name
					if (!added && (operator.getName().contains("Buffer") || subscriptions.size() > 1)) {
						for (_SubQuery subquery : subquerys.get(q)) {
							if (!subquery.hasRoot()) {
								subquery.setRoot(operator);
								added = true;
								adding = true;
							}
						}
						if (!added) {
							_SubQuery s = new _SubQuery(thread);
							s.setRoot(operator);
							subquerys.get(q).add(s);
							added = true;
							adding = true;
						}
					}
					// No Subscriptions -> End of Query
					if (subscriptions.isEmpty() && !added) {
						for (_SubQuery subquery : subquerys.get(q)) {
							if (!subquery.hasRoot()) {
								subquery.setRoot(operator);
								subquery.setIsEnd(true);
								added = true;
							}
						}
						if (!added) {
							_SubQuery s = new _SubQuery(thread);
							s.setRoot(operator);
							s.setIsEnd(true);
							subquerys.get(q).add(s);
							added = true;
							adding = true;
						}
					}

					if (!added) {
						// TODO: Remove identification by the operator name
						for (IPhysicalOperator subscription : subscriptions) {
							if (!added && (subscription.getName().contains("Buffer") || subscriptions.size() > 1)) {
								_SubQuery s = new _SubQuery(thread);
								s.setRoot(operator);
								subquerys.get(q).add(s);
								added = true;
								adding = true;
							}
						}

						for (_SubQuery subquery : subquerys.get(q)) {
							if (!added) {
								added = addOperatorToSubQuery(operator, subscriptions, subquery);
							}
						}

						if (!added) {
							_SubQuery s = new _SubQuery(thread);
							s.addOperator(operator);
							subquerys.get(q).add(s);
						}
					}
				}
			}
			if (adding) {
				for (_SubQuery subquery : subquerys.get(q)) {
					for (int i = 0; i < subquery.getOperator().size(); i++) {
						subquery.removeOperator(subquery.getOperator().get(i));
					}
				}
			}
		}
		for (_SubQuery subquery : subquerys.get(q)) {
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
		for (_SubQuery subquery : subquerys.get(query)) {
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
			_SubQuery subquery) {
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
			for (ControllablePhysicalSubscription subscription : sourceSubscriptions) {
				subscriptions.add((IPhysicalOperator) subscription.getSink());
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
		_AverageLatency l = averageLatencys.get(q);
		if (l == null) {
			l = new _AverageLatency();
			l.addLatency(getLastQueryLatency(q));
		} else {
			l.addLatency(getLastQueryLatency(q));
		}
		averageLatencys.put(q, l);
		QueryManager.LOG.info("AverageLatency: " + averageLatencys.get(q).getAverageLatency());
	}

	private static long getLastQueryLatency(IPhysicalQuery q) {
		long sum = 0;
		for (_SubQuery subquery : subquerys.get(q)) {
			sum += subquery.getMeasurement().getCurrentLatency();
		}
		return sum;
	}

	@Override
	public void eventOccured(String type) {
		IExecutionPlan executionPlan = executor.getExecutionPlan(superUser);
		Collection<IPhysicalQuery> queries = executionPlan.getQueries(superUser);
		for (IPhysicalQuery query : queries) {
			if (!thread.isAlive()){
				thread.start();
			}
			addQuery(query);
		}
		for (IPhysicalQuery query : subquerys.keySet()) {
			if (!queries.contains(query)) {
				subquerys.remove(query);
			}
		}
		if (queries.isEmpty()) {
			thread.shutdown();
		}
	}

	/**
	 * Adds a new query to the QueryManager if it not exists
	 * 
	 * @param q
	 *            Query which will be added
	 */
	public void addQuery(IPhysicalQuery q) {
		if (!subquerys.keySet().contains(q)) {
			List<_SubQuery> list = new ArrayList<_SubQuery>();
			list.add(new _SubQuery(thread));
			subquerys.put(q, list);
			divideQuery(q);
		}
	}

	/**
	 * Get the average latency for an query about the last 20 Tupel
	 * 
	 * @param query
	 *            IPhysicalQuery
	 * @return long Latency for an query
	 */
	public Long getAverageLatency(IPhysicalQuery query) {
		return averageLatencys.get(query).getAverageLatency();
	}

	/**
	 * Sets latency for an SubQuery
	 * 
	 * @param measurement
	 * @param currentLatency
	 */
	public synchronized void setLatencyForSubQuery(Measurement measurement, long currentLatency) {
		for (IPhysicalQuery q : subquerys.keySet()) {
			for (_SubQuery subQuery : subquerys.get(q)) {
				if (subQuery.getMeasurement() == measurement) {
					subQuery.setCurrentLatency(currentLatency);
					// Tupel reached end of query
					if (subQuery.isEnd()) {
						calcAverageLatency(q);
					}
					return;
				}
			}
		}
	}
}

class _SubQuery {
	private ThreadCalculateLatency thread;
	private Measurement measurement;
	private ArrayList<IPhysicalOperator> operator;
	private IPhysicalOperator root;
	private QueryEventListener eventListener;
	private boolean isEnd;
	private long currentLatency;

	public _SubQuery() {
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

	public _SubQuery(ThreadCalculateLatency t) {
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

	/**
	 * Has the SubQuery an last operator
	 * 
	 * @return true if an operator is set as end of SubQuery, false if not
	 */
	public boolean hasRoot() {
		if (getRoot() == null) {
			return false;
		}
		return true;
	}

	/**
	 * Adds an Operator to the SubQuery
	 * 
	 * @param o
	 *            IPhysicalOperator
	 */
	public void addOperator(IPhysicalOperator o) {
		if (getOperator().contains(o)) {
			return;
		}
		getOperator().add(o);
	}

	/**
	 * Creates the EventListener for all operators in this SubQuery
	 */
	public void addEventListener() {
		for (IPhysicalOperator o : this.getOperator()) {
			getEventListener().addOperator(o);
		}
		getEventListener().addOperator(getRoot());
	}

	/**
	 * Removes an Operator from this SubQuery
	 * 
	 * @param o
	 *            IPhysicalOperator
	 */
	public void removeOperator(IPhysicalOperator o) {
		if (getOperator() != null && getOperator().contains(o)) {
			getOperator().remove(o);
		}

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

	public long getCurrentLatency() {
		return currentLatency;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
	}
}

class _AverageLatency {
	private long averageLatency;
	private int pointer = 0;
	private long[] lastLatencys = new long[20];

	public _AverageLatency() {

	}

	/**
	 * Adds an new latency to calculate the average latency about the last 20
	 * values
	 * 
	 * @param latency
	 */
	public void addLatency(long latency) {
		averageLatency = 0;
		lastLatencys[pointer] = latency;
		int averageLatencyCount = 0;
		for (long l : lastLatencys) {
			averageLatency += l;
			if (l != 0)
				averageLatencyCount++;
		}
		averageLatency = averageLatency / averageLatencyCount;
		pointer++;
		if (pointer == lastLatencys.length)
			pointer = 0;
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