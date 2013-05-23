package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.util.List;
import java.util.Map;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;

public class P2PDictionary implements IP2PDictionary {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionary.class);

	private static IP2PDictionary instance;

	private final List<IP2PDictionaryListener> listeners = Lists.newArrayList();
	private final Map<ID, List<ViewAdvertisement>> publishedViews = Maps.newHashMap();

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}

	public static IP2PDictionary getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	@Override
	public void addView(ViewAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Viewadvertisement must not be null!");
		Preconditions.checkArgument(!existsView(viewAdvertisement), "ViewAdvertisement already added!");

		
		List<ViewAdvertisement> newAdvList = publishedViews.get(viewAdvertisement.getViewID());
		if( newAdvList == null ) {
			newAdvList = Lists.newArrayList();
			publishedViews.put(viewAdvertisement.getViewID(), newAdvList);
		}
		newAdvList.add(viewAdvertisement);
		fireViewAddEvent(viewAdvertisement);
	}

	@Override
	public void removeView(ViewAdvertisement advertisement) {
		if (advertisement != null && publishedViews.containsKey(advertisement.getViewID())) {
			List<ViewAdvertisement> advs = publishedViews.get(advertisement.getViewID());
			advs.remove(advertisement);
			
			if( advs.isEmpty() ) {
				publishedViews.remove(advertisement.getViewID());
			}
			
			fireViewRemoveEvent(advertisement);
		}
	}

	@Override
	public ImmutableList<ViewAdvertisement> getViews() {
		ImmutableList.Builder<ViewAdvertisement> resultBuild = new ImmutableList.Builder<>();
		for( List<ViewAdvertisement> advs : publishedViews.values() ) {
			resultBuild.addAll(advs);
		}
		return resultBuild.build();
	}

	@Override
	public ImmutableList<ViewAdvertisement> getViews(ID viewID) {
		Preconditions.checkNotNull(viewID, "ViewID to get advertisements must not be null!");
		
		return ImmutableList.copyOf(publishedViews.get(viewID));
	}

	@Override
	public ImmutableList<ViewAdvertisement> getViews(String viewName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(viewName), "Viewname for getting advertisements must not be null or empty!");
		
		ImmutableList.Builder<ViewAdvertisement> builder = new ImmutableList.Builder<>();
		for( List<ViewAdvertisement> advs : publishedViews.values()) {
			for( ViewAdvertisement adv : advs ) {
				if( adv.getViewName().equals(viewName)) {
					builder.add(adv);
				}
			}
		}
		
		return builder.build();
	}
	
	@Override
	public ImmutableList<ID> getViewIDs() {
		return ImmutableList.copyOf(publishedViews.keySet());
	}
	
	@Override
	public boolean existsView(ViewAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Viewadvertisement must not be null!");
		
		List<ViewAdvertisement> advs = publishedViews.get(viewAdvertisement.getViewID());
		return ( advs != null && advs.contains(viewAdvertisement) );
	}
	

	@Override
	public boolean existsView(ID id) {
		Preconditions.checkNotNull(id, "Id must not be null!");
		
		return publishedViews.containsKey(id);
	}

	@Override
	public boolean existsView(String viewName) {
		for( List<ViewAdvertisement> advs : publishedViews.values()) {
			for( ViewAdvertisement adv : advs ) {
				if( adv.getViewName().equals(viewName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void importView(ID viewID, String viewNameToUse) throws PeerException {
		Preconditions.checkNotNull(viewID, "ViewID to import must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(viewNameToUse), "Viewname to use for import must be null or empty!");

		if (DataDictionaryService.get().containsViewOrStream(viewNameToUse, SessionManagementService.getActiveSession())) {
			throw new PeerException("ViewName '" + viewNameToUse + "' is locally already in use");
		}

		List<ViewAdvertisement> viewAdvs = publishedViews.get(viewID);
		if (viewAdvs == null) {
			throw new PeerException("ViewID " + viewID + " is not known for import");
		}

		List<ViewAdvertisement> nonLocalViewAdvs = determineNonLocalViewAdvertisements(viewAdvs);
		if (nonLocalViewAdvs.isEmpty()) {
			throw new PeerException("ViewID contains only views locally published");
		}

		// TODO: Geschickte auswahl eines ViewAdvertisements! ( bei mehr als einen)
		ViewAdvertisement viewAdvertisement = nonLocalViewAdvs.get(0);

		final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
		receiverOperator.setPipeID(viewAdvertisement.getPipeID().toString());
		receiverOperator.setOutputSchema(viewAdvertisement.getOutputSchema());
		receiverOperator.setSchema(viewAdvertisement.getOutputSchema().getAttributes());
		receiverOperator.setName(viewNameToUse + "_Receive");
		receiverOperator.setDestinationName("local");

		final RenameAO renameNoOp = new RenameAO();
		renameNoOp.setDestinationName("local");
		renameNoOp.setNoOp(true);

		receiverOperator.subscribeSink(renameNoOp, 0, 0, receiverOperator.getOutputSchema());
		renameNoOp.initialize();

		// TODO: Zuordnung viewNameToUse <-> ViewID merken!
		DataDictionaryService.get().setView(viewNameToUse, renameNoOp, SessionManagementService.getActiveSession());
	}

	private static List<ViewAdvertisement> determineNonLocalViewAdvertisements(List<ViewAdvertisement> viewAdvs) {
		List<ViewAdvertisement> result = Lists.newArrayList();
		for( ViewAdvertisement viewAdv : viewAdvs ) {
			if( !viewAdv.isLocal() ) {
				result.add(viewAdv);
			}
		}
		return result;
	}

	@Override
	public void addListener(IP2PDictionaryListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IP2PDictionaryListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected final void fireViewAddEvent(ViewAdvertisement advertisement) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.publishedViewAdded(this, advertisement);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireViewRemoveEvent(ViewAdvertisement advertisement) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.publishedViewRemoved(this, advertisement);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}
}
