package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

public class ContractRegistry {
	private static ConcurrentHashMap<PeerID,MedusaPricingContract> contracts = new ConcurrentHashMap<PeerID, MedusaPricingContract>();
	
	public static void addContract(MedusaPricingContract contract) {
		if(contracts.containsKey(contract.getContractPartner())) {
			contracts.remove(contract.getContractPartner());
		}
		contracts.put(contract.getContractPartner(),contract);
	}
	
	public static void removeContract(PeerID peer) {
		if(contracts.containsKey(peer)) {
			contracts.remove(peer);
		}
	}
	
	public static MedusaPricingContract getContractforPeer(PeerID peer) {
		return contracts.get(peer);
	}
	
	public static boolean hasContract(PeerID peer) {
		return contracts.containsKey(peer);
	}
	
	public static Collection<MedusaPricingContract> getAllContracts() {
		return contracts.values();
	}
	
	public static PeerID getCheapestOffer() {
		double min = Double.POSITIVE_INFINITY;
		PeerID cheapestPeer = null;
		for(MedusaPricingContract contract : contracts.values()) {
			if(contract.getPrice()<min) {
				cheapestPeer = contract.getContractPartner();
			}
		}
		return cheapestPeer;
	}
}
