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
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.DuplicateEliminationRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

/**
 * The full replay without duplicates recovery executor represents a complete
 * non-distributed recovery (NDR) strategy that backs up and recovers sources,
 * sinks and queries as well as all incoming elements. Since there are no
 * operator states backed up, the stream processing is redone from the very
 * first element in case of recovery. Additionally it eliminates duplicates, so
 * all results are the same as without crash.
 *
 * @author Michael Brand
 *
 */
public class FullReplayWithoutDuplicatesRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FullReplayWithoutDuplicatesRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "FullReplayWithoutDuplicatesRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		FullReplayWithoutDuplicatesRecoveryExecutor instance = new FullReplayWithoutDuplicatesRecoveryExecutor();
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new BaDaStRecoveryComponent());
		components.add(new DuplicateEliminationRecoveryComponent());
		components.add(new RecoveryTimeCalculatorComponent());
		instance.init(config, components);
		return instance;
	}

	@Override
	protected Logger setLogger() {
		return LOG;
	}

}