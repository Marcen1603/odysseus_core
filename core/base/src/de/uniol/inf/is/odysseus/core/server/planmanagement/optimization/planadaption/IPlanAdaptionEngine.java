/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


/**
 * Interface for the PlanAdaption.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionEngine extends IPlanModificationListener, IMigrationListener {

	public void adaptPlan(IPhysicalQuery query, ISession user);
	
	public IPlanMigrationStrategy getPlanMigrationStrategy();
	public IPlanAdaptionResourceMonitor getAdaptionResourceMonitor();
	public IPlanAdaptionFitness getFitness();
	public IPlanAdaptionMigrationFuzzyRuleEngine getFuzzyRuleEngine();
	public IPlanAdaptionPolicyRuleEngine getPolicyRuleEngine();
	public IServerExecutor getExecutor();
	
	public long getBlockedTime();
	public long getTimer();
	public boolean isRunning();
}
