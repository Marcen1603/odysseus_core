package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Data Structure to hold information about sets of Queries and their Cpu, Mem and Net Costs.
 * Used in Dynamic Load Balancing
 * @author Carsten Cordes
 *
 */
public class QueryCostMap implements Cloneable {

	private TreeMap<Double,List<Integer>> internalCostMap = new TreeMap<Double,List<Integer>>();
	private HashMap<Integer,QueryLoadInformation> internalInformationMap = new HashMap<Integer,QueryLoadInformation>();
	
	private double individualCostSum = 0.0;
	private double totalCpuLoad = 0.0;
	private double totalMemLoad = 0.0;
	private double totalNetLoad = 0.0;
	
	
	/**
	 * 
	 */
	public QueryCostMap() {
		
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public QueryCostMap clone() {
		return new QueryCostMap(this);
	}
	
	/**
	 * Copy Constructor
	 * @param old Query Cost Map to clone.
	 */
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
	
	/**
	 * Adds Query to Cost Map
	 * @param query {@link QueryLoadInformation} of Query to add.
	 */
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
	

	/**
	 * Adds Query to cost Map
	 * @param queryId QueryID to add
	 * @param individualCost Migration Cost for Query.
	 * @param cpuLoad Cpu Load of Query
	 * @param memLoad Mem Load of Query
	 * @param netLoad Net Load of Query
	 */
	public void add(Integer queryId, double individualCost, double cpuLoad, double memLoad, double netLoad) {
		QueryLoadInformation info = new QueryLoadInformation(queryId,cpuLoad,memLoad,netLoad,individualCost);
		add(info);
	}
	
	/**
	 * Removes Query from Cost Map
	 * @param queryId QueryID to remove.
	 */
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
	
	/**
	 * Gets Information about Query
	 * @param queryId QueryID to get Information for
	 * @return @link{QueryLoadInformation} with Information about chosen Query or null if Query not in Cost Map.
	 */
	public QueryLoadInformation getQueryInformation(int queryId) {
		if(internalInformationMap.containsKey(queryId)) {
			return internalInformationMap.get(queryId);
		}
		return null;
	}

	/**
	 * Checks if Cost Map contains particular Query
	 * @param queryId Query ID to check for.
	 * @return true if Query is in Cost Map, false if not.
	 */
	public boolean containsQuery(Integer queryId) {
		return internalInformationMap.containsKey(queryId);
	}
	
	/**
	 * Returns sorted List of @link{QueryLoadInformation} of all Queries in Cost Map.
	 * @return sorted List of @link{QueryLoadInformation} of all Queries in Cost Map
	 */
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
	

	/**
	 * Get total Costs of Query Cost Map
	 * @return total Costs of Cost Map.
	 */
	public double getCosts() {
		return individualCostSum;
	}
	

	/**
	 * Get all QueryIDs in Cost Map
	 * @return List of queryIDs in Cost Map.
	 */
	public List<Integer> getQueryIds() {
		return new ArrayList<Integer>(internalInformationMap.keySet());
	}
	
	
	/**
	 * Returns a nice formatted output of Query Costs in Cost Map (e.g. for debug purpose).
	 * @see java.lang.Object#toString()
	 */
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
		sb.append(String.format( "%.4f",getTotalCpuLoad()));
		sb.append("\n");
		
		sb.append("Total MEM Load:");
		sb.append(String.format( "%.4f",getTotalMemLoad()));
		sb.append("\n");
		
		sb.append("Total NET Load:");
		sb.append(String.format( "%.4f",getTotalNetLoad()));
		sb.append("\n");
		
		sb.append("Migration cost:");
		sb.append(String.format( "%.2f",getCosts()));
		sb.append("\n");
		
		return sb.toString();
		
	}

	/**
	 * Returns Total Cpu Load of Cost Map.
	 * @return total Cpu Load of Cost Map.
	 */
	public double getTotalCpuLoad() {
		return totalCpuLoad;
	}

	/**
	 * Returns Total Mem Load of Cost Map.
	 * @return total Mem Load of Cost Map.
	 */
	public double getTotalMemLoad() {
		return totalMemLoad;
	}
	/**
	* Returns Total Net Load of Cost Map.
	* @return total Net Load of Cost Map.
	*/
	public double getTotalNetLoad() {
		return totalNetLoad;
	}
	
}
