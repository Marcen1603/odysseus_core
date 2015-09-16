package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ContractRegistry {
	

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ContractRegistry.class);

	private static ConcurrentHashMap<PeerID,MedusaPricingContract> contracts = new ConcurrentHashMap<PeerID, MedusaPricingContract>();
	
	public static void addContract(MedusaPricingContract contract) {
		LOG.debug("Adding/Replacing Contract for {} with price {}",contract.getContractPartner(),contract.getPrice());
		contracts.put(contract.getContractPartner(),contract);
	}
	
	public static void removeContract(PeerID peer) {
			contracts.remove(peer);
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
		
		long currentTime = System.currentTimeMillis();
		
		double min = Double.POSITIVE_INFINITY;
		
		
		
		List<PeerID> toRemove = Lists.newArrayList();
		List<PeerID> alternatives = Lists.newArrayList();
		for(MedusaPricingContract contract : contracts.values()) {
			
			if(contract.getValidUntil()<currentTime) {
				toRemove.add(contract.getContractPartner());
				continue;
			}
			
			if(contract.getPrice()<min) {
				alternatives.clear();
				LOG.debug("{} < {}",contract.getPrice(),min);
				min = contract.getPrice();
			}
			
			if(contract.getPrice() == min) {
				LOG.debug("Adding {} to cheapes Peers",contract.getContractPartner());
				alternatives.add(contract.getContractPartner());
			}
			
		}
		
		
		for(PeerID peer : toRemove) {
			LOG.debug("Removing Contract from {} as contract is too old.",peer);
			contracts.remove(peer);
		}
		
		LOG.debug("Choosing Random Peer from Cheapest Peers.");
			
		
		PeerID cheapestPeer = chooseRandom(alternatives);
		
		LOG.debug("Cheapest Peer is {}",cheapestPeer);
		return cheapestPeer;
	}
	
	
	private static PeerID chooseRandom(List<PeerID> peerIDs) {
		int len = peerIDs.size();
		Random rnd = new Random(System.currentTimeMillis());
		int chosenIndex = rnd.nextInt(len);
		
		return peerIDs.get(chosenIndex);
	}
}
