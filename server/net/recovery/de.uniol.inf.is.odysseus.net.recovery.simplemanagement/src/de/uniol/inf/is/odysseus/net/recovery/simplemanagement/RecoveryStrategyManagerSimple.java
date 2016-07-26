package de.uniol.inf.is.odysseus.net.recovery.simplemanagement;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.net.recovery.IRecoveryStrategyManager;

public class RecoveryStrategyManagerSimple implements IRecoveryStrategyManager {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyManagerSimple.class);

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Simple recovery strategy manager activated.");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Simple recovery strategy manager deactivated.");
	}

	/**
	 * The recovery strategy, if there is one bound.
	 */
	private static IRecoveryStrategy recoveryStrategy;

	/**
	 * Binds a recovery strategy. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery strategy to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryStrategy(IRecoveryStrategy serv) {

		Preconditions.checkNotNull(serv);
		recoveryStrategy = serv;
		LOG.debug("Bound {} as a recovery strategy.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery strategy, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery strategy to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryStrategy(IRecoveryStrategy serv) {

		Preconditions.checkNotNull(serv);

		if (serv == recoveryStrategy) {
			recoveryStrategy = null;
			LOG.debug("Unbound {} as a recovery strategy.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public void startRecovery(OdysseusNodeID failedNode, UUID recoveryStateIdentifier) {
		if (recoveryStrategy != null) {
			recoveryStrategy.recover(failedNode, recoveryStateIdentifier);
		} else {
			LOG.warn("No recovery strategy bound");
			// TODO new SimpleRecoveryStrategy().recover(failedNode,
			// recoveryStateIdentifier);
		}
	}

	@Override
	public void restartRecovery(OdysseusNodeID failedNode, UUID recoveryStateIdentifier,
			UUID recoverySubStateIdentifier) {
		if (recoveryStrategy != null) {
			recoveryStrategy.recoverSingleQueryPart(failedNode, recoveryStateIdentifier, recoverySubStateIdentifier);
		} else {
			LOG.warn("No recovery strategy bound");
			// TODO new
			// SimpleRecoveryStrategy().recoverSingleQueryPart(failedPeer,
			// recoveryStateIdentifier,
			// recoverySubStateIdentifier);
		}
	}

	@Override
	public String getName() {
		return "simple";
	}

}
