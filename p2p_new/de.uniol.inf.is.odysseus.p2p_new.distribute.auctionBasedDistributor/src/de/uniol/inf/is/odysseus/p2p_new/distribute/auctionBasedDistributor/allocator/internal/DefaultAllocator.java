package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.allocator.internal;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.document.AdvertisementFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.AuctionBasedDistributor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.AuctionSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public class DefaultAllocator extends AbstractAllocator {
	private static final Logger log = LoggerFactory.getLogger(DefaultAllocator.class);

	public final void activate() {
		log.debug("{} activated (all dependencies statisfied)", DefaultAllocator.class.getName());
	}

	public final void deactivate() {
		log.debug("{} deactivated", AuctionBasedDistributor.class.getName());
	}	
	

	@Override
	protected List<SubPlan> evaluateBids(List<AuctionSummary> auctions,
			List<SubPlan> subPlans) {
		Map<String, List<SubPlan>> destinationMap = Maps.newHashMap();
		// Gebot des lokalen peers wird auch berücksichtigt
		destinationMap.putAll(determineWinners(auctions));
		addLocalPlans(destinationMap, subPlans);
		
		return partManipulator.mergeSubPlans(destinationMap);
	}
	
	private void addLocalPlans(Map<String, List<SubPlan>> destinationMap,
			List<SubPlan> subPlans) {
		if(destinationMap.get("local") == null) {
			destinationMap.put("local", getLocalPlans(subPlans));
		}
		else {
			List<SubPlan> localPlans = getLocalPlans(subPlans);
			Set<SubPlan> toAdd = Sets.newHashSet();
			for(SubPlan localPlan : localPlans) {
				boolean alreadyExists = false;
				for(SubPlan subPlan : destinationMap.get("local")) {
					if(localPlan == subPlan) {
						alreadyExists = true;
						break;
					}
				}
				if(!alreadyExists) {
					toAdd.add(localPlan);
				}
			}
			destinationMap.get("local").addAll(toAdd);
		}
	}

	private Map<String, List<SubPlan>> determineWinners(
			List<AuctionSummary> auctions) {
		Map<String, List<SubPlan>> winners = Maps.newHashMap();
		try {
			for(AuctionSummary auction : auctions) {
				List<AuctionResponseAdvertisement> bids = auction.getResponses().get();
				
				// calc own bid
				bids.add(wrapInAuctionResponseAdvertisement(
						auction.getAuction().getAuctionId(),
						calcOwnBid(auction.getAuction().getPqlStatement(),
								auction.getAuction().getTransCfgName())));
				
				Collections.sort(bids);
				AuctionResponseAdvertisement bid = bids.get(bids.size()-1);
				
				String peerName = bid.getOwnerPeerId().toString();
				if(peerName.equals(communicator.getLocalPeerID().toString())) {
					peerName = "local";
				}
				
				List<SubPlan> list = winners.get(peerName);
				
				if(list==null) {
					list = Lists.newArrayList();
					winners.put(peerName, list);
				}
				auction.getSubPlan().setDestinationName(peerName);
				list.add(auction.getSubPlan());			
				
				log.debug("Peer {} won auction {} with bid {}", new String[]{peerName,
						bid.getAuctionId(), bid.getBid()+""});				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return winners;
	}

	private AuctionResponseAdvertisement wrapInAuctionResponseAdvertisement (
			String auctionId,
			double bid) {
		final AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) AdvertisementFactory
				.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
		adv.setBid(bid);
		adv.setAuctionId(auctionId);
		adv.setOwnerPeerId(communicator.getLocalPeerID());
		return adv;
	}
}
