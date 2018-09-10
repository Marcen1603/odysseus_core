/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.pdr;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmigration.AbstractPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.IMigrationStrategy;

/**
 * @author Dennis Nowak
 *
 */
public class PauseDrainResumeMigrationStrategy extends AbstractPlanMigrationStrategy {
	
	private static final String NAME = "PauseDrainResumeMigrationStrategy";
	
	private static Logger LOG = LoggerFactory.getLogger(PauseDrainResumeMigrationStrategy.class);

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IMigrationStrategy getNewInstance() {
		return new PauseDrainResumeMigrationStrategy();
	}

	@Override
	public void migrateQuery(IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoot) {
		
		LOG.debug("Starting migration of query " + runningQuery.getID());
		
		//TODO buffer incoming elements
		
		//TODO pause and drain
		
		//TODO change sources
		List<IPhysicalOperator> oldSources= new ArrayList<>();
		for(IPhysicalOperator op:runningQuery.getAllOperators()){
			if(op.getLogicalOperator().getSubscribedToSource().isEmpty()) {
				oldSources.add(op);
			}
		}
		//TODO change sinks
		
		//TODO remove old plan
		
		//TODO resume
		
		LOG.debug("Migration of query " + runningQuery.getID() + " finished.");
	}

}
