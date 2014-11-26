package de.uniol.inf.is.odysseus.peer.recovery.service;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyRegistry;

public class RecoveryStrategyService {

	// called by OSGi-DS
	public void bindRecoveryStrategy( IRecoveryStrategy strategy ) {
		RecoveryStrategyRegistry.getInstance().add(strategy);
	}
	
	// called by OSGi-DS
	public void unbindRecoveryStrategy( IRecoveryStrategy strategy ) {
		RecoveryStrategyRegistry.getInstance().remove(strategy);
	}
}
