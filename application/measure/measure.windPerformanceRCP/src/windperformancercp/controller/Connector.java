package windperformancercp.controller;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;


/**
 * Connects the windperformance measurer with odysseus
 * @author blackunicorn
 *
 */
public class Connector {
	IExecutor executor = new StandardExecutor();
}
