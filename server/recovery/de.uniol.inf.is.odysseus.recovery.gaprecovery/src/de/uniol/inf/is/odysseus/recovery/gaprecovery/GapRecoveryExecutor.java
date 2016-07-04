package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.Collections;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.ConvergenceDetectorComponent;

/**
 * The gap recovery executor represents a complete non-distributed recovery
 * (NDR) strategy that backs up and recovers sources, sinks and queries.
 * 
 * @author Michael Brand
 *
 */
public class GapRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(GapRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "GapRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		GapRecoveryExecutor instance = new GapRecoveryExecutor();
		instance.init(config, Collections.singletonList(new ConvergenceDetectorComponent()));
		return instance;
	}

	@Override
	protected Logger setLogger() {
		return LOG;
	}

}