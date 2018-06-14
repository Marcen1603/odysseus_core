package de.uniol.inf.is.odysseus.recovery.preciserecovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.badast.BaDaStRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.checkpointing.CheckpointingRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.DuplicateEliminationRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.processingimage.ProcessingImageRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

/**
 * The precise recovery executor represents a complete non-distributed recovery
 * (NDR) strategy that backs up and recovers sources, sinks and queries as well
 * as not processed stream elements and the states of operators and
 * subscriptions. Additionally it eliminates duplicates, so all results are the
 * same as without crash.
 *
 * @author Michael Brand
 *
 */
public class PreciseRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PreciseRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "PreciseRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		PreciseRecoveryExecutor instance = new PreciseRecoveryExecutor();
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new CheckpointingRecoveryComponent());
		components.add(new ProcessingImageRecoveryComponent());
		components.add(new BaDaStRecoveryComponent());
		components.add(new DuplicateEliminationRecoveryComponent());
		components.add(new RecoveryTimeCalculatorComponent());
		instance.init(config, components);
		((CheckpointingRecoveryComponent) instance.components.get(0))
				.addCheckpointManagerListener((ProcessingImageRecoveryComponent) instance.components.get(1));
		return instance;
	}

	@Override
	protected Logger setLogger() {
		return LOG;
	}

}