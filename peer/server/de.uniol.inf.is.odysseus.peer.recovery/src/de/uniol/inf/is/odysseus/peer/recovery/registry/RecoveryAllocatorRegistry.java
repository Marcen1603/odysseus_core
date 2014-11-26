package de.uniol.inf.is.odysseus.peer.recovery.registry;

import de.uniol.inf.is.odysseus.peer.distribute.util.NamedInterfaceRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;

public final class RecoveryAllocatorRegistry extends NamedInterfaceRegistry<IRecoveryAllocator> {

	private static RecoveryAllocatorRegistry instance;

	private RecoveryAllocatorRegistry() {
		
	}
	
	public static RecoveryAllocatorRegistry getInstance() {
		if( instance == null ) {
			instance = new RecoveryAllocatorRegistry();
		}
		return instance;
	}
}
