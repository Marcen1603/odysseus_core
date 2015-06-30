package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public class QueryLoadInformation implements Cloneable {
	private int queryId;
	private double cpuLoad;
	private double memLoad;
	private double netLoad;
	private double individualMigrationCost;
	
	public QueryLoadInformation(int queryId, double cpuLoad, double memLoad, double netLoad, double individualMigrationCost) {
		this.queryId = queryId;
		this.cpuLoad = cpuLoad;
		this.memLoad = memLoad;
		this.netLoad = netLoad;
		this.individualMigrationCost = individualMigrationCost;
	}
	
	@Override
	public QueryLoadInformation clone() {
		return new QueryLoadInformation(this);
	}
	
	public QueryLoadInformation(QueryLoadInformation old) {
		this.queryId = old.queryId;
		this.cpuLoad = old.cpuLoad;
		this.memLoad = old.memLoad;
		this.netLoad = old.netLoad;
		this.individualMigrationCost = old.individualMigrationCost;
	}

	public int getQueryId() {
		return queryId;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public double getMemLoad() {
		return memLoad;
	}

	public double getNetLoad() {
		return netLoad;
	}

	public double getIndividualMigrationCost() {
		return individualMigrationCost;
	}
}
