package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;


public class QuerySharingCostModelFactory {
	
	public static final String STATIC_COST_MODEL = "static";
	public static final String NONE = "none";

	public IQuerySharingCostModel buildCostModel(String costModelName) {
		if (STATIC_COST_MODEL.equals(costModelName)) {
			return new StaticQuerySharingCostModel();
		} else if (NONE.equals(costModelName)) {
			return null;
		} else {
			throw new RuntimeException("unknown cost model: " + costModelName);
		}
	}

}
