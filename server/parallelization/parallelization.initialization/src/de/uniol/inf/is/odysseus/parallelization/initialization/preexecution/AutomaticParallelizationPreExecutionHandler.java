/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.preexecution;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.preexecution.AbstractParallelizationPreExecutionHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class AutomaticParallelizationPreExecutionHandler extends AbstractParallelizationPreExecutionHandler {

	public static final String TYPE = "AUTOMATIC";
	private static final Logger LOG = LoggerFactory.getLogger(AutomaticParallelizationPreExecutionHandler.class);

	@Override
	public void validateParameters(String parameterString) {
		LOG.info("Validating parameters for " + TYPE + " parallelization");
	}

	@Override
	public void preExecute(String parameterString, List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// TODO Auto-generated method stub
		LOG.info("Automatic parallelization starting");
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
