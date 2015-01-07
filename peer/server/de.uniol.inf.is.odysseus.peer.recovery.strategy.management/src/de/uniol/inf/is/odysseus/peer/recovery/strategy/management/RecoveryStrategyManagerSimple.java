package de.uniol.inf.is.odysseus.peer.recovery.strategy.management;

import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.simplestrategy.SimpleRecoveryStrategy;

public class RecoveryStrategyManagerSimple implements IRecoveryStrategyManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryStrategyManagerSimple.class);

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
		LOG.debug("Bound {} as a recovery strategy.", serv.getClass()
				.getSimpleName());

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

		if (serv != null && serv == recoveryStrategy) {
			recoveryStrategy = null;
			LOG.debug("Unbound {} as a recovery strategy.", serv.getClass()
					.getSimpleName());

		}

	}

	@Override
	public void startRecovery(PeerID failedPeer, UUID recoveryStateIdentifier) {
		if (recoveryStrategy != null) {
			recoveryStrategy.recover(failedPeer, recoveryStateIdentifier);
		} else {
			LOG.warn("No recovery strategy bound");
			new SimpleRecoveryStrategy().recover(failedPeer,
					recoveryStateIdentifier);
		}
	}

	@Override
	public void restartRecovery(PeerID failedPeer,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier) {
		if (recoveryStrategy != null) {
			recoveryStrategy.recover(failedPeer, recoveryStateIdentifier);
		} else {
			LOG.warn("No recovery strategy bound");
			new SimpleRecoveryStrategy().recoverSingleQueryPart(failedPeer,
					recoveryStateIdentifier, recoverySubStateIdentifier);
		}
	}

	@Override
	public String getName() {
		return "simple";
	}

}
