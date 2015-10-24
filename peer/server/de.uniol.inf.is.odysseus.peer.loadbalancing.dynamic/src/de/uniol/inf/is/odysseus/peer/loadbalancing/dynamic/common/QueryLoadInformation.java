package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.common;

/**
 * Class that represents CPU, MEM and NET load and Migration Cost of a single Query. 
 * Used in Combination with @link{QueryCostMap} in dynamic load balancing.
 * @author Carsten Cordes
 *
 */
public class QueryLoadInformation implements Cloneable {
	private int queryId;
	private double cpuLoad;
	private double memLoad;
	private double netLoad;
	private double individualMigrationCost;
	
	/**
	 * Constructor
	 * @param queryId Query ID of Query that is referenced
	 * @param cpuLoad Cpu load of Query 
	 * @param memLoad Mem load of Query
	 * @param netLoad Net load of Query
	 * @param individualMigrationCost Migration Cost for Query
	 */
	public QueryLoadInformation(int queryId, double cpuLoad, double memLoad, double netLoad, double individualMigrationCost) {
		this.queryId = queryId;
		this.cpuLoad = cpuLoad;
		this.memLoad = memLoad;
		this.netLoad = netLoad;
		this.individualMigrationCost = individualMigrationCost;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public QueryLoadInformation clone() {
		return new QueryLoadInformation(this);
	}
	
	/**
	 * Copy Constructor
	 * @param old QueryLoadInformation to copy.
	 */
	public QueryLoadInformation(QueryLoadInformation old) {
		this.queryId = old.queryId;
		this.cpuLoad = old.cpuLoad;
		this.memLoad = old.memLoad;
		this.netLoad = old.netLoad;
		this.individualMigrationCost = old.individualMigrationCost;
	}

	/**
	 * Returns Query ID that is represented
	 * @return Query ID that is represented
	 */
	public int getQueryId() {
		return queryId;
	}

	/**
	 * Returns Cpu Load of Query that is represented
	 * @return Cpu Load of Query that is represented
	 */
	public double getCpuLoad() {
		return cpuLoad;
	}

	/**
	 * Returns Mem Load of Query that is represented
	 * @return Mem Load of Query that is represented
	 */
	public double getMemLoad() {
		return memLoad;
	}

	/**
	 * Returns Net Load of Query that is represented
	 * @return Net Load of Query that is represented
	 */
	public double getNetLoad() {
		return netLoad;
	}

	/**
	 * Returns Migration Cost of Query that is represented
	 * @return Migration Cost of Query that is represented
	 */
	public double getIndividualMigrationCost() {
		return individualMigrationCost;
	}
}
