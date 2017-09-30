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
	Map<IPhysicalQuery, List<SubQuery>> subquerys = new HashMap<IPhysicalQuery, List<SubQuery>>();
	private List<IPhysicalQuery> queries = new ArrayList<IPhysicalQuery>();
	private static final QueryManager manager = new QueryManager();

	private long totalQueryLatency;
	private long averageLatency;
	private long[] lastLatencys = new long[20];
	private int pointer = 0;
	private ThreadCalculateLatency thread = new ThreadCalculateLatency();

	// TODO: IUpdateEventListener.QUERY see AbstractExecutor
	private QueryManager() {

		// divideQuery();
		// this.systemUsage = SystemUsage.getInstance();
		// new Thread(this.systemUsage).start();

		// executor.addUpdateEventListener(this, IUpdateEventListener.QUERY,
		// null);
		// query.getRoots().get(0).
		OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.QUERY, null);
		thread.start();
		// StandardExecutor.getInstance().addUpdateEventListener(this,
		// "IUpdateEventListener.QUERY", null);
	}

	public static QueryManager getInstance() {
		return manager;
	}

	/**
	 * Devides a query in a set of subquerys to handle the calculation for the
	 * latency
	 * 
	 * @param p
	 */
	@SuppressWarnings("rawtypes")
	private void divideQuery(IPhysicalQuery q) {
		// SubQuery subquery = new SubQuery();
		// subquery.setRoot(q.getRoots().get(0));
		// for (IPhysicalOperator o : q.getPhysicalChilds()) {
		// List<IIterableSource<?>> test = q.getIterableSources();
		// List<IIterableSource<?>> test1 = q.getIteratableLeafSources();
		// List<IPhysicalOperator> test2 = q.getLeafSources();
		// if (!q.getLeafSources().contains(o) &&
		// !q.getIterableSources().contains(o)
		// && !q.getIteratableLeafSources().contains(o)) {
		// subquery.addOperator(o);
		// List<SubQuery> list = new ArrayList<SubQuery>();
		// list.add(subquery);
		// subquerys.put(q, list);
		// } else {
		// System.out.println(o.toString());
		// }
		// }
		// subquery.addEventListener();

		// countSubscriptions(q);
		boolean adding = true;
		while (adding) {
			adding = false;
			for (IPhysicalOperator operator : q.getPhysicalChilds()) {
				if (!q.getLeafSources().contains(operator) && !q.getIterableSources().contains(operator)
						&& !q.getIteratableLeafSources().contains(operator)) {
					List<IPhysicalOperator> subscriptions = getSubscriptions(operator);
					boolean added = false;
					// TODO: IF Operator is Join or Aggregate or Buffer -> new
					// SubQuery -> setRoot(o)
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
							added=true;
						}
					}

					if (!added) {
						// TODO: if (subscriptions.contains(JOIN) ||
						// subscriptions.contains(Aggregate)) {
						// setRoot(JOIN/Aggregate);
						// adding = true;
						// }
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
				if (adding) {
					for (SubQuery subquery : subquerys.get(q)) {
						for (IPhysicalOperator o : subquery.getOperator()) {
							subquery.removeOperator(o);
						}
					}
				}
			}
		}
		for (SubQuery subquery : subquerys.get(q)) {
			subquery.addEventListener();
		}
	}

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
	 * TODO: Evtl. umbenennen zu hasSeveralSubsciptions und Rückgabe
	 * ergänzen|remove test
	 * 
	 * @param q
	 */
	private void countSubscriptions(IPhysicalQuery q) {
		HashMap<IPhysicalOperator, Integer> subscriptioncount = new HashMap<IPhysicalOperator, Integer>();
		for (IPhysicalOperator operator : q.getPhysicalChilds()) {
			ISource o = (ISource) operator;
			if (o instanceof AbstractSource) {
				List<?> subscriptions = ((AbstractSource) o).getSubscriptions();
				for (int i = 0; i < subscriptions.size(); i++) {
					ControllablePhysicalSubscription subscription = (ControllablePhysicalSubscription) subscriptions
							.get(i);
					IPhysicalOperator test = (IPhysicalOperator) subscription.getTarget();
					if (subscriptioncount.containsKey(test)) {
						int value = subscriptioncount.get(test) + 1;
						subscriptioncount.put(test, value);
					} else {
						subscriptioncount.put(test, 1);
					}
				}

			}
		}
	}

	private void calcLatency() {
		// this.totalQueryLatency = 0;
		// for (SubQuery q : subquerys.keySet()) {
		// this.setTotalQueryLatency(this.getTotalQueryLatency() +
		// q.getMeasurement().getCurrentLatency());
		// }
		// calcAverageLatency();
	}

	/**
	 * Calculates latency over the last 20 tupel for the whole Query
	 */
	private void calcAverageLatency() {
		this.averageLatency = 0;
		lastLatencys[pointer] = this.getTotalQueryLatency();
		for (int i = 0; i < lastLatencys.length; i++) {
			this.setAverageLatency(this.getAverageLatency() + lastLatencys[i]);
		}
		this.setAverageLatency(this.getAverageLatency() / lastLatencys.length);
		pointer = (pointer++) % lastLatencys.length;
	}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.QUERY) {
			System.out.println("IUpdateEventListener.QUERY");
			refreshQuerys();
		}
	}

	// TODO
	private void refreshQuerys() {
		ISession session = OdysseusRCPPlugIn.getActiveSession();
		List<StoredProcedure> storedProcedures = OdysseusRCPPlugIn.getExecutor().getStoredProcedures(session);
		System.out.println(storedProcedures.toString());
	}

	public long getTotalQueryLatency() {
		return totalQueryLatency;
	}

	public void setTotalQueryLatency(long totalQueryLatency) {
		this.totalQueryLatency = totalQueryLatency;
	}

	public long getAverageLatency() {
		return averageLatency;
	}

	private void setAverageLatency(long averageLatency) {
		this.averageLatency = averageLatency;
	}

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

	public boolean containsOperator(IPhysicalOperator o) {
		if (getOperator().contains(o) || getRoot().equals(o)) {
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
		getEventListener().addOperator(getRoot());
	}

	public void removeOperator(IPhysicalOperator o) {
		for (IPhysicalOperator op : operator) {
			if (op.equals(o)) {
				eventListener.removeOperator(op);
				operator.remove(op);
				if (operator.isEmpty()) {
					stopThread();
				}
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
}
