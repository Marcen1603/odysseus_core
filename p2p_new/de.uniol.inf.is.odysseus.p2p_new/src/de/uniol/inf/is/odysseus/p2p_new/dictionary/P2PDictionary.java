package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;

public class P2PDictionary implements IP2PDictionary, IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionary.class);

	private static IP2PDictionary instance;

	private final List<IP2PDictionaryListener> listeners = Lists.newArrayList();
	
	private final List<ViewAdvertisement> publishedViews = Lists.newArrayList();
	private final Map<ViewAdvertisement, List<ViewAdvertisement>> sameViewsMap = Maps.newHashMap();
	private final Map<ViewAdvertisement, List<ViewAdvertisement.Same>> cachedSameMap = Maps.newHashMap();

	private final Map<ViewAdvertisement, String> importedViews = Maps.newHashMap();

	private static IDataDictionary dataDictionary;

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

	public void bindDataDictionary(IDataDictionary dd) {
		dataDictionary = dd;
		dataDictionary.addListener(this);

		LOG.debug("DataDictionary bound {}", dd);
	}

	public void unbindDataDictionary(IDataDictionary dd) {
		if (dataDictionary == dd) {
			dataDictionary.removeListener(this);
			dataDictionary = null;
			LOG.debug("DataDictionary unbound {}", dd);
		}
	}

	@Override
	public void addView(ViewAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Viewadvertisement must not be null!");
		Preconditions.checkArgument(!existsView(viewAdvertisement), "ViewAdvertisement already added!");

		publishedViews.add(viewAdvertisement);
		cachedSameMap.put(viewAdvertisement, Lists.<ViewAdvertisement.Same> newArrayList());
		sameViewsMap.put(viewAdvertisement, Lists.<ViewAdvertisement> newArrayList());

		if (viewAdvertisement.getSameAs() != null) {
			for (ViewAdvertisement.Same sameAs : viewAdvertisement.getSameAs()) {
				Optional<ViewAdvertisement> optSameAdvertisement = find(sameAs.getPeerID(), sameAs.getViewName());
				if (optSameAdvertisement.isPresent()) {
					addSame(viewAdvertisement, optSameAdvertisement.get());
				} else {
					List<ViewAdvertisement.Same> sameList = cachedSameMap.get(viewAdvertisement);
					sameList.add(sameAs);
				}
			}

			checkSameCache(viewAdvertisement);
		}

		fireViewAddEvent(viewAdvertisement);
	}

	@Override
	public void removeView(ViewAdvertisement advertisement) {
		if( advertisement != null && importedViews.containsKey(advertisement)) {
			importedViews.remove(advertisement);
			
			fireViewImportRemoveEvent(advertisement, importedViews.get(advertisement));
		}
		
		if (advertisement != null && publishedViews.contains(advertisement)) {
			publishedViews.remove(advertisement);
			cachedSameMap.remove(advertisement);
			sameViewsMap.remove(advertisement);
			
			for (List<ViewAdvertisement> sameList : sameViewsMap.values()) {
				sameList.remove(advertisement);
			}

			fireViewRemoveEvent(advertisement);
		}
	}

	@Override
	public ImmutableList<ViewAdvertisement> getSame(ViewAdvertisement advertisement) {
		return ImmutableList.copyOf(sameViewsMap.get(advertisement));
	}

	@Override
	public boolean isSame(ViewAdvertisement a, ViewAdvertisement b) {
		return sameViewsMap.get(a).contains(b);
	}

	@Override
	public ImmutableList<ViewAdvertisement> getViews() {
		return ImmutableList.copyOf(publishedViews);
	}

	@Override
	public ImmutableList<ViewAdvertisement> getViews(String viewName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(viewName), "Viewname for getting advertisements must not be null or empty!");

		ImmutableList.Builder<ViewAdvertisement> builder = new ImmutableList.Builder<>();
		for (ViewAdvertisement adv : publishedViews) {
			if (adv.getViewName().equals(viewName)) {
				builder.add(adv);
			}
		}

		return builder.build();
	}

	@Override
	public boolean existsView(ViewAdvertisement viewAdvertisement) {
		return publishedViews.contains(viewAdvertisement);
	}

	@Override
	public boolean existsView(String viewName) {
		for (ViewAdvertisement adv : publishedViews) {
			if (adv.getViewName().equals(viewName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void importView(ViewAdvertisement viewAdvertisement, String viewNameToUse) throws PeerException {
		Preconditions.checkNotNull(viewAdvertisement, "ViewAdvertisement to import must not be null!");
		Preconditions.checkArgument(existsView(viewAdvertisement), "ViewAdvertisement to import is not known to the p2p dictionary");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(viewNameToUse), "Viewname to use for import must be null or empty!");

		if (dataDictionary.containsViewOrStream(viewNameToUse, SessionManagementService.getActiveSession())) {
			throw new PeerException("ViewName '" + viewNameToUse + "' is locally already in use");
		}

		if (isImported(viewAdvertisement)) {
			throw new PeerException("ViewAdvertisement already imported");
		}

		List<ViewAdvertisement> viewAdvs = Lists.newArrayList();
		viewAdvs.add(viewAdvertisement);
		if (sameViewsMap.containsKey(viewAdvertisement)) {
			viewAdvs.addAll(sameViewsMap.get(viewAdvertisement));
		}

		List<ViewAdvertisement> nonLocalViewAdvs = determineNonLocalViewAdvertisements(viewAdvs);
		if (nonLocalViewAdvs.isEmpty()) {
			throw new PeerException("ViewAdvertisements contains only views which are published at this peer");
		}

		// TODO: Geschickte auswahl eines ViewAdvertisements! (bei mehr als
		// einen)
		ViewAdvertisement advertisement = nonLocalViewAdvs.get(0);

		final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
		receiverOperator.setPipeID(advertisement.getPipeID().toString());
		receiverOperator.setOutputSchema(advertisement.getOutputSchema());
		receiverOperator.setSchema(advertisement.getOutputSchema().getAttributes());
		receiverOperator.setName(viewNameToUse + "_Receive");
		receiverOperator.setDestinationName("local");

		final RenameAO renameNoOp = new RenameAO();
		renameNoOp.setDestinationName("local");
		renameNoOp.setNoOp(true);

		receiverOperator.subscribeSink(renameNoOp, 0, 0, receiverOperator.getOutputSchema());
		renameNoOp.initialize();

		dataDictionary.setView(viewNameToUse, renameNoOp, SessionManagementService.getActiveSession());
		importedViews.put(advertisement, viewNameToUse);

		fireViewImportEvent(advertisement, viewNameToUse);
	}

	@Override
	public Optional<String> getImportedViewName(ViewAdvertisement advertisement) {
		return Optional.fromNullable(importedViews.get(advertisement));
	}

	@Override
	public Optional<ViewAdvertisement> getImportedAdvertisement(String viewName) {
		for (ViewAdvertisement adv : importedViews.keySet()) {
			if (importedViews.get(adv).equals(viewName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	@Override
	public boolean isImported(ViewAdvertisement advertisement) {
		return importedViews.containsKey(advertisement);
	}

	// called by DataDictionary
	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		// do nothing
	}

	// called by DataDictionary
	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		int pos = name.indexOf(".");
		if( pos >= 0 ) {
			name = name.substring(pos+1);
		}
		Optional<ViewAdvertisement> optAdvertisement = getImportedAdvertisement(name);
		if( optAdvertisement.isPresent() ) {
			ViewAdvertisement advertisement = optAdvertisement.get();
			if( importedViews.containsKey(advertisement) ) {
				importedViews.remove(advertisement);
				
				fireViewImportRemoveEvent(advertisement, name);
			}
		}
		
		Optional<ViewAdvertisement> optOwnAdvertisement = find(P2PNewPlugIn.getOwnPeerID(), name);
		if( optOwnAdvertisement.isPresent() ) {
			ViewAdvertisement ownAdvertisement = optOwnAdvertisement.get();
			
			try {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisement(ownAdvertisement);
			} catch (IOException e) {
				LOG.error("Could not flush view advertisement {}", ownAdvertisement, e);
			}
			
			removeView(ownAdvertisement);
		}
	}

	// called by DataDictionary
	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
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
					listener.viewAdded(this, advertisement);
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
					listener.viewRemoved(this, advertisement);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireViewImportEvent(ViewAdvertisement advertisement, String viewName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.viewImported(this, advertisement, viewName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}
	
	protected final void fireViewImportRemoveEvent(ViewAdvertisement advertisement, String viewName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.viewImportRemoved(this, advertisement, viewName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	private Optional<ViewAdvertisement> find(PeerID peerID, String viewName) {
		for (ViewAdvertisement adv : publishedViews) {
			if (adv.getPeerID().equals(peerID) && adv.getViewName().equals(viewName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	private void addSame(ViewAdvertisement adv1, ViewAdvertisement adv2) {
		List<ViewAdvertisement> same1 = sameViewsMap.get(adv1);
		List<ViewAdvertisement> same2 = sameViewsMap.get(adv2);
		same1.add(adv2);
		same2.add(adv1);
	}

	private void checkSameCache(ViewAdvertisement viewAdvertisement) {
		for (ViewAdvertisement potencialSameAdv : cachedSameMap.keySet().toArray(new ViewAdvertisement[0])) {
			for (ViewAdvertisement.Same potencialSame : cachedSameMap.get(potencialSameAdv).toArray(new ViewAdvertisement.Same[0])) {
				if (potencialSame.getPeerID().equals(viewAdvertisement.getPeerID()) && potencialSame.getViewName().equals(viewAdvertisement.getViewName())) {
					addSame(viewAdvertisement, potencialSameAdv);

					cachedSameMap.get(potencialSameAdv).remove(potencialSame);
				}
			}
		}
	}

	private static List<ViewAdvertisement> determineNonLocalViewAdvertisements(List<ViewAdvertisement> viewAdvs) {
		List<ViewAdvertisement> result = Lists.newArrayList();
		for (ViewAdvertisement viewAdv : viewAdvs) {
			if (!viewAdv.isLocal()) {
				result.add(viewAdv);
			}
		}
		return result;
	}
}
