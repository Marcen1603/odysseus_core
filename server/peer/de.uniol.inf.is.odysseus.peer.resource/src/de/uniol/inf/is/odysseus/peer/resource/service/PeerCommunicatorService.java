package de.uniol.inf.is.odysseus.peer.resource.service;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;

public class PeerCommunicatorService {

	private static IPeerCommunicator communicator;

	public void bindPeerCommunicator(IPeerCommunicator serv) {
		communicator = serv;
	}

	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (communicator == serv) {
			communicator = null;
		}
	}

	public static boolean isBound() {
		return get() != null;
	}

	public static IPeerCommunicator get() {
		return communicator;
	}
	
}
