package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import net.jxta.peergroup.PeerGroup;


/**
 * Hilfsklasse für die PeerGroup, da diese nicht serialisierbar ist, aber für die Transformation benötigt wird.
 * 
 * @author Mart Köhler
 *
 */
public final class PeerGroupTool {
	private static PeerGroup peerGroup = null;

	public static PeerGroup getPeerGroup() {
		return peerGroup;
	}

	public static void setPeerGroup(PeerGroup peerGroup) {
		PeerGroupTool.peerGroup = peerGroup;
	}
	
	
}
