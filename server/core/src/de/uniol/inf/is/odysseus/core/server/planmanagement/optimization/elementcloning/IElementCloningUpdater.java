package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning;

import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;

public interface IElementCloningUpdater {

	/**
	 * Global unique name of the strategy
	 * @return
	 */
	String getName();
	
	/**
	 * Process the update on the execution plan
	 * @param currentExecPlan
	 */
	void updateCloning(IExecutionPlan currentExecPlan);
	

}
