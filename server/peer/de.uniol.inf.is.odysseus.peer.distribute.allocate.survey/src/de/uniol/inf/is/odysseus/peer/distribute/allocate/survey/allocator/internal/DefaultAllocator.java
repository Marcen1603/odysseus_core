package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.allocator.internal;

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

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.SubPlanManipulator;

public class DefaultAllocator extends AbstractAllocator {
	private static final Logger log = LoggerFactory.getLogger(DefaultAllocator.class);

	@Override
	protected List<SubPlan> evaluateBids(List<AuctionSummary> auctions, List<SubPlan> subPlans) {
		Map<String, List<SubPlan>> destinationMap = determineWinners(auctions);

		addLocalPlans(destinationMap, subPlans);

		return SubPlanManipulator.mergeSubPlans(destinationMap);
	}

	private void addLocalPlans(Map<String, List<SubPlan>> destinationMap, List<SubPlan> subPlans) {
		if (!destinationMap.containsKey("local")) {
			destinationMap.put("local", getLocalPlans(subPlans));
		} else {
			List<SubPlan> localPlans = getLocalPlans(subPlans);
			Set<SubPlan> toAdd = Sets.newHashSet();
			for (SubPlan localPlan : localPlans) {
				boolean alreadyExists = false;
				for (SubPlan subPlan : destinationMap.get("local")) {
					if (localPlan == subPlan) {
						alreadyExists = true;
						break;
					}
				}
				if (!alreadyExists) {
					toAdd.add(localPlan);
				}
			}
			destinationMap.get("local").addAll(toAdd);
		}
	}

	protected Map<String, List<SubPlan>> determineWinners(List<AuctionSummary> auctions) {
		Map<String, List<SubPlan>> winners = Maps.newHashMap();
		try {
			for (AuctionSummary auction : auctions) {
				List<AuctionResponseAdvertisement> bids = auction.getResponses().get();

				log.info("Got {} bids from remote peers", bids.size());

				// calc own bid
				bids.add(wrapInAuctionResponseAdvertisement(auction.getAuction().getAuctionId(), calcOwnBid(auction.getAuction().getPqlStatement(), auction.getAuction().getTransCfgName())));

				Collections.sort(bids);
				AuctionResponseAdvertisement bid = bids.get(bids.size() - 1);

				String peerName = bid.getOwnerPeerId().toString();
				if (peerName.equals(communicator.getLocalPeerID().toString())) {
					peerName = "local";
				}

				List<SubPlan> list = winners.get(peerName);

				if (list == null) {
					list = Lists.newArrayList();
					winners.put(peerName, list);
				}
				auction.getSubPlan().addDestinationName(peerName);
				list.add(auction.getSubPlan());

				log.debug("Peer {} won auction {} with bid {}", new String[] { peerName, bid.getAuctionId(), bid.getBid() + "" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return winners;
	}

	private AuctionResponseAdvertisement wrapInAuctionResponseAdvertisement(String auctionId, double bid) {
		final AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
		adv.setBid(bid);
		adv.setAuctionId(auctionId);
		adv.setOwnerPeerId(communicator.getLocalPeerID());
		return adv;
	}
}
