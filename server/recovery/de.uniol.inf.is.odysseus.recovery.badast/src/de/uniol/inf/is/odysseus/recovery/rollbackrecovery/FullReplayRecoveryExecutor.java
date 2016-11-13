package de.uniol.inf.is.odysseus.recovery.rollbackrecovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.badast.BaDaStRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.duplicatesdetector.DuplicatesDetectorRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

/**
 * The full replay recovery executor represents a complete non-distributed
 * recovery (NDR) strategy that backs up and recovers sources, sinks and queries
 * as well as all incoming elements. Since there are no operator states backed
 * up, the stream processing is redone from the very first element in case of
 * recovery.
 *
 * @author Michael Brand
 *
 */
public class FullReplayRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FullReplayRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "FullReplayRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		FullReplayRecoveryExecutor instance = new FullReplayRecoveryExecutor();
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new BaDaStRecoveryComponent());
		components.add(new DuplicatesDetectorRecoveryComponent());
		components.add(new RecoveryTimeCalculatorComponent());
		instance.init(config, components);
		return instance;
	}

	@Override
	protected Logger setLogger() {
		return LOG;
	}

}
