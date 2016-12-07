/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Nowak
 *
 */
public class ReoptimzeParallelizationOsgiConsoleCommand implements CommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(ReoptimzeParallelizationOsgiConsoleCommand.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	@Override
	public String getHelp() {
		return "Starts an optimization of parallelilized operators in odysseus.";
	}

	public void _reparallelize(CommandInterpreter ci) throws Exception {
		LOG.info("Optimzation of parallelized operators started by osgi console.");
		String queryId = ci.nextArgument();
		if (queryId == null) {
			throw new IllegalArgumentException("No queryId given.");
		}
		try {
			//TODO 
			ParallelizationOptimizer.getInstance().reoptimzeQuery(Integer.parseInt(queryId));
		} catch (Exception e) {
			LOG.error("Optimization of Parallelizationn was not successfull", e);
		}
	}

}
