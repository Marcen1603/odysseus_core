/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.preexecution;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AutomaticStrategyInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator.InterOperatorStrategyInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorStrategyInitializer;
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
		LOG.debug("Parameter String: " + parameterString);
	}

	@Override
	public void preExecute(String parameterString, List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		LOG.debug("Automatic parallelization starting");
		new AutomaticStrategyInitializer().initialize(settings);
		new IntraOperatorStrategyInitializer().initialize(settings);
		new InterOperatorStrategyInitializer().initialize(settings);
		LOG.debug("Parallelization strategies initialized");
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
