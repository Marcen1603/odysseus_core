package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.ConvergenceDetectorComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

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
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new ConvergenceDetectorComponent());
		components.add(new RecoveryTimeCalculatorComponent());
		instance.init(config, components);
		return instance;
	}

	@Override
	protected Logger setLogger() {
		return LOG;
	}

}