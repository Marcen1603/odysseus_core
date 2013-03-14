package de.uniol.inf.is.odysseus.p2p_new.distributor.user;

import java.util.Collection;
import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

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
				
				// shared parts are always started
				for( Integer id : ids ) {
					executor.startQuery(id, SessionManagementService.getActiveSession());
				}
				QueryPartController.getInstance().registerAsSlave( ids, adv.getSharedQueryID());
				
			} catch( Throwable t ) {
				LOG.error("Could not execute query part", t);
			} finally {
				consumedAdvertisementIDs.add(adv.getID());				
			}
		}
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
}
