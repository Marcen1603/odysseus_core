package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.checkpointing.CheckpointingRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.ConvergenceDetectorComponent;
import de.uniol.inf.is.odysseus.recovery.processingimage.ProcessingImageRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.recoverytime.RecoveryTimeCalculatorComponent;

/**
 * The stateful gap recovery executor represents a complete non-distributed
 * recovery (NDR) strategy that backs up and recovers sources, sinks and queries
 * as well as processing images. Since there are no incoming elements backed up,
 * results after system restart might differ (convergence phase). But the
 * recovery of a processing image may shorten the convergence phase.
 *
 * @author Michael Brand
 *
 */
public class StatefulGapRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(StatefulGapRecoveryExecutor.class);

	/**
	 * The unique name of the executor.
	 */
	private static final String NAME = "StatefulGapRecovery";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		StatefulGapRecoveryExecutor instance = new StatefulGapRecoveryExecutor();
		List<IRecoveryComponent> components = new ArrayList<>();
		components.add(new CheckpointingRecoveryComponent());
		components.add(new ProcessingImageRecoveryComponent());
		components.add(new ConvergenceDetectorComponent());
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