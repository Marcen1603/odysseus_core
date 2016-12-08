/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmigration.exception.MigrationException;

/**
 * @author Dennis Nowak
 *
 */
public class MigrationStrategyRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(MigrationStrategyRegistry.class);
	
	private static Map<String,IPlanMigrationStrategy> strategyRegistry = new HashMap<>();
	
	public static void registerStrategy(IPlanMigrationStrategy migrationStrategy) {
		LOG.debug("Register new PlanMigrationStrategy " + migrationStrategy.getName() + ".");
		String name = migrationStrategy.getName().toLowerCase();
		if(strategyRegistry.containsKey(name)){
			LOG.error("Plan migration strategy with name "+ name + " already registered.");
		} else {
			strategyRegistry.put(name, migrationStrategy);
		}
	}
	
	public static void unregisterStrategy(IPlanMigrationStrategy migrationStrategy) {
		LOG.debug("Unregister PlanMigrationStrategy " + migrationStrategy.getName() + ".");
		strategyRegistry.remove(migrationStrategy.getName().toLowerCase());
	}
	
	public static IPlanMigrationStrategy getPlanMigrationStrategyById(String id) throws MigrationException{
		IPlanMigrationStrategy strategy = strategyRegistry.get(id.toLowerCase());
		if(strategy == null) {
			throw new MigrationException("Plan migration strategy is not registered.");
		}
		return strategy;
	}

}
