package de.uniol.inf.is.odysseus.peer.recovery.service;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyManagerRegistry;

public class RecoveryStrategyManagerService {

	// called by OSGi-DS
	public void bindRecoveryStrategyManager( IRecoveryStrategyManager strategyMangager ) {
		RecoveryStrategyManagerRegistry.getInstance().add(strategyMangager);
	}
	
	// called by OSGi-DS
	public void unbindRecoveryStrategyManager( IRecoveryStrategyManager strategyMangager ) {
		RecoveryStrategyManagerRegistry.getInstance().remove(strategyMangager);
	}
}
