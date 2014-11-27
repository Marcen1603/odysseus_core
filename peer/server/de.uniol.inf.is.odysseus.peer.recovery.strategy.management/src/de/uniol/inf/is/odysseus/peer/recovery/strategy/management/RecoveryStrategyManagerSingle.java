package de.uniol.inf.is.odysseus.peer.recovery.strategy.management;

import java.util.UUID;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;

public class RecoveryStrategyManagerSingle implements IRecoveryStrategyManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryStrategyManagerSingle.class);

	private IRecoveryStrategy recoveryStrategy;

	@Override
	public void startRecovery(PeerID failedPeer, UUID recoveryStateIdentifier) {
		if (recoveryStrategy == null) {
			recoveryStrategy.recover(failedPeer, recoveryStateIdentifier);
		} else {
			LOG.error("No recovery strategy bound");
		}
	}

	@Override
	public void restartRecovery(PeerID failedPeer,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier) {
		if (recoveryStrategy == null) {
			recoveryStrategy.recoverSingleQueryPart(failedPeer,
					recoveryStateIdentifier, recoverySubStateIdentifier);
		} else {
			LOG.error("No recovery strategy bound");
		}
	}

	@Override
	public String getName() {
		return "SingleStrategy";
	}

	@Override
	public void setRecoveryStrategy(IRecoveryStrategy strategy) {
		this.recoveryStrategy = strategy;
	}

}
