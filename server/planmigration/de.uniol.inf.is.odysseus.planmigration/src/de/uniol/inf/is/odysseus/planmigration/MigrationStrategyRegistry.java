/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmigration.exception.PlanMigrationStrategyException;

/**
 * @author Dennis Nowak
 *
 */
public class MigrationStrategyRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(MigrationStrategyRegistry.class);
	
	Map<String,IPlanMigrationStrategy> strategyRegistry = new HashMap<>();
	
	public void registerStrategy(IPlanMigrationStrategy migrationStrategy) {
		LOG.debug("Register new PlanMigrationStrategy " + migrationStrategy.getName() + ".");
		String name = migrationStrategy.getName().toLowerCase();
		if(this.strategyRegistry.containsKey(name)){
			LOG.error("Plan migration strategy with name "+ name + " already registered.");
		} else {
			this.strategyRegistry.put(name, migrationStrategy);
		}
	}
	
	public void unregisterStrategy(IPlanMigrationStrategy migrationStrategy) {
		LOG.debug("Unregister PlanMigrationStrategy " + migrationStrategy.getName() + ".");
		this.strategyRegistry.remove(migrationStrategy.getName().toLowerCase());
	}
	
	public IPlanMigrationStrategy getPlanMigrationStrategyById(String id) throws PlanMigrationStrategyException{
		IPlanMigrationStrategy strategy = this.strategyRegistry.get(id);
		if(strategy == null) {
			throw new PlanMigrationStrategyException("Plan migration strategy is not registered.");
		}
		return strategy;
	}

}
