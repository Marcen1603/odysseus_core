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
import de.uniol.inf.is.odysseus.recovery.checkpointing.CheckpointingRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.duplicatesdetector.DuplicatesDetectorRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.processingimage.ProcessingImageRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

/**
 * The rollback recovery executor represents a complete non-distributed recovery
 * (NDR) strategy that backs up and recovers sources, sinks and queries as well
 * as not processed stream elements and the states of operators and
 * subscriptions. It may result in duplicates after recovery, because all
 * elements after the last checkpoint are reprocessed.
 *
 * @author Michael Brand
 *
 */
public class RollbackRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RollbackRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "RollbackRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		RollbackRecoveryExecutor instance = new RollbackRecoveryExecutor();
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new CheckpointingRecoveryComponent());
		components.add(new ProcessingImageRecoveryComponent());
		components.add(new BaDaStRecoveryComponent());
		components.add(new DuplicatesDetectorRecoveryComponent());
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