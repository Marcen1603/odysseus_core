package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class PingMapService {

	private static IPingMap pingMap;

	// called by OSGi-DS
	public void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	public static boolean isBound() {
		return get() != null;
	}

	public static IPingMap get() {
		return pingMap;
	}

	public static void waitFor() {
		while (!isBound()) {
			try {
				Thread.sleep(250);
			} catch (Throwable t) {
			}
		}
	}
}
