package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.QueryLoadInformation;


public class QueryCostMap implements Cloneable {

	private TreeMap<Double,List<Integer>> internalCostMap = new TreeMap<Double,List<Integer>>();
	private HashMap<Integer,QueryLoadInformation> internalInformationMap = new HashMap<Integer,QueryLoadInformation>();
	
	private double individualCostSum = 0.0;
	private double totalCpuLoad = 0.0;
	private double totalMemLoad = 0.0;
	private double totalNetLoad = 0.0;
	
	
	public QueryCostMap() {
		
	}
	
	public QueryCostMap clone() {
		return new QueryCostMap(this);
	}
	
	public QueryCostMap(QueryCostMap old) {
		this.totalCpuLoad = old.totalCpuLoad;
		this.totalMemLoad = old.totalMemLoad;
		this.totalNetLoad = old.totalNetLoad;
		this.individualCostSum = old.individualCostSum;
		this.internalCostMap = new TreeMap<Double,List<Integer>>();
		
		for (Double key : old.internalCostMap.keySet()) {
			List<Integer> list = old.internalCostMap.get(key);
			List<Integer> copy = new ArrayList<Integer>(list);
			this.internalCostMap.put(key,copy);
		}
		
		this.internalInformationMap = new HashMap<Integer,QueryLoadInformation>();
		for(Integer key: old.internalInformationMap.keySet()) {
			QueryLoadInformation info = old.internalInformationMap.get(key).clone();
			internalInformationMap.put(key,info);
		}
	}
	
	public void add(QueryLoadInformation query) {
		int queryId = query.getQueryId();
		
		if(internalInformationMap.containsKey(queryId))
			return;
		
		internalInformationMap.put(queryId,query);
		
		if(internalCostMap.containsKey(query.getIndividualMigrationCost())) {
			List<Integer> queryListForCost = internalCostMap.get(query.getIndividualMigrationCost());
			queryListForCost.add(query.getQueryId());
		}
		else {
			List<Integer> queryListForCost = new ArrayList<Integer>();
			queryListForCost.add(query.getQueryId());
			internalCostMap.put(query.getIndividualMigrationCost(), queryListForCost);
		}
		
		totalCpuLoad +=query.getCpuLoad();
		totalNetLoad +=query.getNetLoad();
		if(totalNetLoad!=0.0) {
			
		}
		totalMemLoad +=query.getMemLoad();
		individualCostSum += query.getIndividualMigrationCost();
		
	}
	

	public void add(Integer queryId, double individualCost, double cpuLoad, double memLoad, double netLoad) {
		QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,memLoad,netLoad,individualCost);
		add(info);
	}
	
	public void remove(int queryId) {
		if(!internalInformationMap.containsKey(queryId))
			return;
		
		QueryLoadInformation info = internalInformationMap.get(queryId);
		internalInformationMap.remove(Integer.valueOf(queryId));
		double cost = info.getIndividualMigrationCost();
		
		
		if(internalCostMap.containsKey(cost)) {
			List<Integer> queriesForCost = internalCostMap.get(cost);
			if(queriesForCost.contains(queryId)) {
				queriesForCost.remove(Integer.valueOf(queryId));
			}
		}
		
		totalCpuLoad -=info.getCpuLoad();
		totalNetLoad -=info.getNetLoad();
		totalMemLoad -=info.getMemLoad();
		individualCostSum -= info.getIndividualMigrationCost();
	}
	
	public QueryLoadInformation getQueryInformation(int queryId) {
		if(internalInformationMap.containsKey(queryId)) {
			return internalInformationMap.get(queryId);
		}
		return null;
	}

	public boolean containsQuery(Integer queryId) {
		return internalInformationMap.containsKey(queryId);
	}
	
	public List<QueryLoadInformation> getQueriesSortedByCost() {
		Iterator<Entry<Double,List<Integer>>> iter = internalCostMap.entrySet().iterator();
		List<QueryLoadInformation> queries = new ArrayList<QueryLoadInformation>();
		while(iter.hasNext()) {
			Entry<Double,List<Integer>> nextEntry = iter.next();
			for(Integer query : nextEntry.getValue()) {
				queries.add(internalInformationMap.get(query));
			}	
		}
		return queries;
	}
	

	public double getCosts() {
		Double generalCosts = internalCostMap.size() * OdyLoadConstants.WEIGHT_GENERAL_COSTS;
		Double individualCostsTotal = individualCostSum * OdyLoadConstants.WEIGHT_INDIVIDUAL_COSTS;
		return generalCosts + individualCostsTotal;
	}
	

	public List<Integer> getQueryIds() {
		return new ArrayList<Integer>(internalInformationMap.keySet());
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Iterator<Integer> iter = getQueryIds().iterator();
		while(iter.hasNext()) {
			sb.append(iter.next());
			if(iter.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("}\n");
		sb.append("Total CPU Load:");
		sb.append(getTotalCpuLoad());
		sb.append("\n");
		
		sb.append("Total MEM Load:");
		sb.append(getTotalMemLoad());
		sb.append("\n");
		
		sb.append("Total NET Load:");
		sb.append(getTotalNetLoad());
		sb.append("\n");
		
		sb.append("Migration cost:");
		sb.append(getCosts());
		sb.append("\n");
		
		return sb.toString();
		
	}

	public double getTotalCpuLoad() {
		return totalCpuLoad;
	}

	public double getTotalMemLoad() {
		return totalMemLoad;
	}

	public double getTotalNetLoad() {
		return totalNetLoad;
	}
	
}
