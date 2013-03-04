package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.parser.pql.generator.PQLGenerator;

public class QueryPartManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);
	private static QueryPartManager instance;
	
	private final List<ID> consumedAdvertisementIDs = Lists.newArrayList();

	private IExecutor executor;

	public QueryPartManager() {
		instance = this;
	}
	
	public static QueryPartManager getInstance() {
		return instance;
	}
	
	@Override
	public boolean isSelected(Advertisement advertisement) {
		if (advertisement instanceof QueryPartAdvertisement) {
			QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;
			return adv.getPeerID().equals(P2PNewPlugIn.getOwnPeerID());
		}
		return false;
	}

	@Override
	public void advertisementOccured(IAdvertisementManager sender, Advertisement advertisement) {
		QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;
		if (!consumedAdvertisementIDs.contains(adv.getID())) {
			LOG.debug("Got query part");
			LOG.debug("PQL-Statement is {}", adv.getPqlStatement());
			
			try {
				Collection<Integer> ids = executor.addQuery(adv.getPqlStatement(), "PQL", SessionManagementService.getActiveSession(), "Standard");
				for( Integer id : ids ) {
					executor.startQuery(id, SessionManagementService.getActiveSession());
				}
			} catch( Throwable t ) {
				LOG.error("Could not execute query part", t);
			} finally {
				consumedAdvertisementIDs.add(adv.getID());				
			}
		}
	}
	
	public void publish( QueryPart part, PeerID destinationPeer ) {
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		
		QueryPartAdvertisement adv = (QueryPartAdvertisement)AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
		adv.setPeerID(destinationPeer);
		part.removeDestinationName();
		adv.setPqlStatement(PQLGenerator.generatePQLStatement(part.getOperators().iterator().next()));
		tryPublish(adv);
		LOG.debug("QueryPart {} published", part);
	}

	// called by OSGi-DS
	public void bindAdvertisementManager(IAdvertisementManager manager) {
		manager.addAdvertisementListener(this);

		LOG.debug("Bound AdvertisementManager {}", manager);
	}

	// called by OSGi-DS
	public void unbindAdvertisementManager(IAdvertisementManager manager) {
		manager.removeAdvertisementListener(this);

		LOG.debug("Unbound AdvertisementManager {}", manager);
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exe) {
		executor = exe;

		LOG.debug("Bound Executor {}", exe);
	}

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			LOG.debug("Unbound Executor {}", exe);

			executor = null;
		}
	}
	
	private static void tryPublish(QueryPartAdvertisement adv) {
		try {
			P2PNewPlugIn.getDiscoveryService().publish(adv, 5000, 5000);
		} catch (IOException ex) {
			LOG.error("Could not publish query part", ex);
		}
	}

}
