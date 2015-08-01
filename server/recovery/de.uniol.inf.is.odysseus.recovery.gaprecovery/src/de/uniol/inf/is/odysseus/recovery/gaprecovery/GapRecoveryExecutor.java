package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

/**
 * The gap recovery executor represents a recovery strategy, which backups and
 * recovery query states (incl. sinks and sources).
 * 
 * @author Michael Brand
 *
 */
public class GapRecoveryExecutor implements IRecoveryExecutor {

	@Override
	public String getName() {
		return "GapRecovery";
	}

	/**
	 * Nothing to do, because recovery of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public void recover(List<Integer> queryIds, ISession caller, List<ISysLogEntry> log) throws Exception {
		// Nothing to do.
	}

	/**
	 * Nothing to do, because backup of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public void activateBackup(List<Integer> queryIds, ISession caller) {
		// Nothing to do.
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		return new GapRecoveryExecutor();
	}

}