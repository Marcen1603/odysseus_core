package de.uniol.inf.is.odysseus.peer.recovery.registry;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;



public final class RecoveryCommunicatorRegistry {
	
	

	private static Collection<IRecoveryCommunicator> communicators = new HashSet<IRecoveryCommunicator>();
	
	
	
	public static void bindRecoveryCommunicator(IRecoveryCommunicator communicator) {
		communicators.add(communicator);
	}
	
	public static void unbindRecoveryCommunicator(IRecoveryCommunicator communicator) {
		communicators.remove(communicator);
	}
	
	public static Collection<IRecoveryCommunicator> getRecoveryCommunicators() {
		return communicators;
	}
}
