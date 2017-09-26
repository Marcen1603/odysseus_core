package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
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
//		this.systemUsage = SystemUsage.getInstance();
//		new Thread(this.systemUsage).start();

		// executor.addUpdateEventListener(this, IUpdateEventListener.QUERY,
		// null);
		// query.getRoots().get(0).
	}

	@SuppressWarnings("rawtypes")
	private void devideQuery() {
		// TODO: Split bei Threaded Buffern
		// TODO: If Abfrage entfernen

//		if (query.getRoots().size() >= 1) {
//			
//			HashMap<Integer, ArrayList<IPhysicalOperator>> sQuerys = new HashMap<Integer, ArrayList<IPhysicalOperator>>();
//			for (IPhysicalOperator o : query.getPhysicalChilds()) {
//				ISource source = (ISource) o;
//				if (o instanceof AbstractSource) {
//					if (source.getSubscriptions().isEmpty()) {
//						ArrayList<IPhysicalOperator> temp = new ArrayList<IPhysicalOperator>();
//						temp.add(source);
//						sQuerys.put(sQuerys.size()+1, temp);
//					}else {
//						for (Integer key : sQuerys.keySet()){
//							ArrayList<IPhysicalOperator> temp = sQuerys.get(key);
//							for (Object object : source.getSubscriptions()){
//								for (IPhysicalOperator operator : temp){
//									System.out.println(operator.toString());
//									System.out.println(temp.toString());
//									
//									if (temp.toString().contains(operator.toString())){
//										System.out.println(operator.toString());
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		} else {
//
//		}
		subquerys.put(query, new Measurement(query));
	}

	private void addEventListener() {
		for (IPhysicalQuery q : subquerys.keySet()) {
			ThreadCalculateLatency thread = new ThreadCalculateLatency();
			thread.start();
			new QueryEventListener(q, subquerys.get(q), thread);
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
