package de.uniol.inf.is.odysseus.recovery.rollbackrecovery;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

/**
 * The rollback recovery executor represents a recovery strategy, which backups
 * and recovery query states (incl. sinks and sources), operator states (incl.
 * queue states), and incoming data streams. <br />
 * <br />
 * The rollback recovery fulfills the requirement of completeness, but not the
 * requirement of correctness.
 * 
 * @author Michael Brand
 *
 */
public class RollbackRecoveryExecutor implements IRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(RollbackRecoveryExecutor.class);

	@Override
	public String getName() {
		return "RollbackRecovery";
	}

	/**
	 * The recovery component for the incoming data stream elements (to be bound
	 * by OSGi).
	 */
	private static IRecoveryComponent cSourceRecoveryComponent;

	// TODO RollbackRecoveryExecutor misses Operator Recovery

	/**
	 * Binds either a recovery component for the incoming data stream elements
	 * or for the operator states.
	 * 
	 * @param component
	 *            The component to bind, if its name matches "Source Recovery"
	 *            or "Operator Recovery".
	 */
	public static void bindComponent(IRecoveryComponent component) {
		if (component.getName().equals("Source Recovery")) {
			cSourceRecoveryComponent = component;
		}
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

	/**
	 * Unbinds a recovery component
	 * 
	 * @param component
	 *            The component to unbind, if it has been bound before.
	 */
	public static void unbindComponent(IRecoveryComponent component) {
		if (cSourceRecoveryComponent.equals(component)) {
			cSourceRecoveryComponent = null;
		}
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

	/**
	 * The configuration of the executor.
	 */
	@SuppressWarnings("unused")
	private Properties mConfig;

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		RollbackRecoveryExecutor executor = new RollbackRecoveryExecutor();
		executor.mConfig = config;
		return executor;
	}

	@Override
	public void recover(List<Integer> queryIds, ISession caller, List<ISysLogEntry> log) throws Exception {
		// TODO implement RollbackRecoveryExecutor.recover
	}

	/**
	 * Nothing to do, because backup of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public void activateBackup(List<Integer> queryIds, ISession caller) {
		if (cSourceRecoveryComponent == null) {
			cLog.error("RollBackRecovery executor misses Source Recovery component!");
			return;
		}
		// TODO What about protection points?
		cSourceRecoveryComponent.activateBackup(queryIds, caller);
		// TODO RollbackRecoveryExecutor misses Operator Recovery
	}

}