package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.system.SystemUsage;

public class QueryManager implements IUpdateEventListener {
	Map<IPhysicalQuery, Measurement> subquerys = new HashMap<IPhysicalQuery, Measurement>();
	private IPhysicalQuery query;
	// private static IExecutor executor;
	private long totalQueryLatency;
	private long averageLatency;
	private long[] lastLatencys = new long[10];
	private int pointer = 0;
	private SystemUsage systemUsage;

	// TODO: IUpdateEventListener.QUERY see AbstractExecutor
	public QueryManager(IPhysicalQuery q) {
		query = q;
		devideQuery();
		addEventListener();
//		this.systemUsage=new SystemUsage();
//		new Thread(this.systemUsage).start();
		// IExecutor executor = QueryManager.getExecutor();
		// executor.addUpdateEventListener(this, IUpdateEventListener.QUERY,
		// null);
//		query.getRoots().get(0).
	}

	private void devideQuery() {
		// TODO: Threaded Buffer splitting the plan
		 ILogicalQuery q = query.getLogicalQuery();
		for (LogicalSubscription o : q.getLogicalPlan().getSubscriptions()){
			System.out.println("Subscription: "+o.toString());
		}
		subquerys.put(query, new Measurement(query));
	}

	private void addEventListener() {
		for (IPhysicalQuery q : subquerys.keySet()) {
			new QueryEventListener(q, subquerys.get(q));
		}
	}

	private void calcLatency() {
		this.totalQueryLatency = 0;
		for (IPhysicalQuery q : subquerys.keySet()) {
			this.setTotalQueryLatency(this.getTotalQueryLatency() + subquerys.get(q).getCurrentLatency());
		}
		calcAverageLatency();
	}

	/**
	 * Calculates latency over the last 20 tupel for the whole Query
	 */
	private void calcAverageLatency() {
		this.averageLatency=0;
		lastLatencys[pointer]=this.getTotalQueryLatency();
		for (int i = 0; i < lastLatencys.length; i++) {
			this.setAverageLatency(this.getAverageLatency() + lastLatencys[i]);
		}
		this.setAverageLatency(this.getAverageLatency()/lastLatencys.length);
		pointer=(pointer++)%lastLatencys.length;
	}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.QUERY) {
			refreshQuerys();
		}
	}

	private void refreshQuerys() {
		// TODO Auto-generated method stub

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
}
