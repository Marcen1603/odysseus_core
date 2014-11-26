package de.uniol.inf.is.odysseus.peer.recovery.service;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryAllocatorRegistry;

public class RecoveryAllocatorService {

	// called by OSGi-DS
	public void bindRecoveryAllocator( IRecoveryAllocator allocator ) {
		RecoveryAllocatorRegistry.getInstance().add(allocator);
	}
	
	// called by OSGi-DS
	public void unbindRecoveryAllocator( IRecoveryAllocator allocator ) {
		RecoveryAllocatorRegistry.getInstance().remove(allocator);
	}
}
