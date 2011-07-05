package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

/**
 * factory for building cost model instances
 * @author Thomas Vogelgesang
 *
 */
public class QuerySharingCostModelFactory {
	
	public static final String STATIC_COST_MODEL = "static";
	public static final String NONE = "none";

	/**
	 * builds a cost model matching the given name
	 * @param costModelName the name of the cost model to be built
	 * @return a new cost model instance
	 */
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
