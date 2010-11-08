package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.ruleengine.system.AbstractWorkingEnvironment;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * Handles the local state: current loaded configuration for each transformation instance etc.
 * 
 * 
 * @author Dennis Geesen
 *
 */
public class TransformationEnvironment extends AbstractWorkingEnvironment<TransformationConfiguration>{
		
		
	public TransformationEnvironment(TransformationConfiguration transformConfig, IRuleFlow ruleFlow, User caller){
		super(transformConfig, ruleFlow, caller);		
	}				
	
}
