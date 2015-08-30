package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.monitoring;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class OsgiServiceProvider {
	private static IPeerResourceUsageManager usageManager;
	private static IPeerDictionary peerDictionary;
	
	
	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}
	
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if(usageManager==serv){
			usageManager = null;
		}
	}
	
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv){
			peerDictionary = null;
		}
	}
	
	public static IPeerResourceUsageManager getUsageManager() {
		return usageManager;
	}
	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}
}
