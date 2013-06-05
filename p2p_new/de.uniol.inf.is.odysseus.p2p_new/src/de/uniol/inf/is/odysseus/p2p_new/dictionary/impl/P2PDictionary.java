package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.MessageTransport;
import net.jxta.endpoint.router.RouteController;
import net.jxta.id.IDFactory;
import net.jxta.impl.endpoint.router.EndpointRouter;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.protocol.RouteAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class P2PDictionary implements IP2PDictionary, IDataDictionaryListener, IPlanModificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionary.class);
	
	private static final String AUTOIMPORT_SYS_PROPERTY = "peer.autoimport";
	private static final String AUTOEXPORT_SYS_PROPERTY = "peer.autoexport";
	
	private static final int EXPORT_INTERVAL_MILLIS = 15000;
	private static final int EXPORT_LIFETIME_MILLIS = 60000;
	private static final int REACHABLE_TIMEOUT_MILLIS = 5000;
	private static final int CHECK_INTERVAL_MILLIS = 10000;
	
	private static P2PDictionary instance;
	private static IDataDictionary dataDictionary;

	private static String localPeerName;
	private static PeerID localPeerID;
	private static PeerGroup localPeerGroup;
	private static PeerGroupID localPeerGroupID;
	
	private final List<IP2PDictionaryListener> listeners = Lists.newArrayList();

	private final List<SourceAdvertisement> publishedSources = Lists.newArrayList();
	private final Map<SourceAdvertisement, List<SourceAdvertisement>> sameSourceMap = Maps.newHashMap();
	private final Map<SourceAdvertisement, List<SourceAdvertisement.Same>> cachedSameMap = Maps.newHashMap();
	private SourceChecker sourceChecker = new SourceChecker(CHECK_INTERVAL_MILLIS, this);

	private final Map<SourceAdvertisement, String> importedSources = Maps.newHashMap();

	private final Map<SourceAdvertisement, Integer> exportedSourcesQueryMap = Maps.newHashMap();
	private final ViewExporterThread viewExporterThread = new ViewExporterThread(EXPORT_INTERVAL_MILLIS, EXPORT_LIFETIME_MILLIS, this);
	private AutoExporter autoExporter;
	
	private final Map<PeerID, String> knownPeersMap = Maps.newHashMap();
	private final Map<PeerID, String> peersAddressMap = Maps.newHashMap();

	// called by OSGi-DS
	public void activate() {
		instance = this;
		
		viewExporterThread.start();
		sourceChecker.start();
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		
		sourceChecker.stopRunning();
		viewExporterThread.stopRunning();
	}
	
	// called by OSGi-DS
	public void bindDataDictionary( IDataDictionary dd ) {
		dataDictionary = dd;
		dataDictionary.addListener(this);
		if( isAutoExport()) {
			autoExporter = new AutoExporter(this);
			dataDictionary.addListener(autoExporter);
		}
		
		LOG.debug("DataDictionary bound {}", dd);
	}
	
	// called by OSGi-DS
	public void unbindDataDictionary( IDataDictionary dd ) {
		if( dataDictionary == dd ) {
			dataDictionary.removeListener(this);
			if( autoExporter != null ) {
				dataDictionary.removeListener(autoExporter);
				autoExporter = null;
			}
			
			dataDictionary = null;
			
			LOG.debug("DataDictionary unbound {}", dd);
		}
	}

	public static P2PDictionary getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	public void addSource(SourceAdvertisement srcAdvertisement, boolean checkIt) throws InvalidP2PSource {
		Preconditions.checkNotNull(srcAdvertisement, "Sourceadvertisement must not be null!");
		Preconditions.checkArgument(!existsSource(srcAdvertisement), "SourceAdvertisement already added!");

		if( checkIt && !checkSource( srcAdvertisement ) ) {
			throw new InvalidP2PSource("Source {} is invalid");
		}
		
		publishedSources.add(srcAdvertisement);
		cachedSameMap.put(srcAdvertisement, Lists.<SourceAdvertisement.Same> newArrayList());
		sameSourceMap.put(srcAdvertisement, Lists.<SourceAdvertisement> newArrayList());

		if (srcAdvertisement.getSameAs() != null) {
			for (SourceAdvertisement.Same sameAs : srcAdvertisement.getSameAs()) {
				Optional<SourceAdvertisement> optSameAdvertisement = find(sameAs.getPeerID(), sameAs.getName());
				if (optSameAdvertisement.isPresent()) {
					addSame(srcAdvertisement, optSameAdvertisement.get());
				} else {
					List<SourceAdvertisement.Same> sameList = cachedSameMap.get(srcAdvertisement);
					sameList.add(sameAs);
				}
			}

			checkSameCache(srcAdvertisement);
		}

		fireSourceAddEvent(srcAdvertisement);
		
		if( isAutoImport() && !isImported(srcAdvertisement) && !isExported(srcAdvertisement.getName())) {
			try {
				importSource(srcAdvertisement, srcAdvertisement.getName());
			} catch (PeerException e) {
				LOG.error("Could not autoimport {}", srcAdvertisement.getName(), e);
			}
		}
	}
	
	@Override
	public boolean checkSource(SourceAdvertisement srcAdvertisement) {
		if( srcAdvertisement.isStream() ) {
			return checkStreamAdvertisement(srcAdvertisement);
		} else if( srcAdvertisement.isView() ) {
			return checkViewAdvertisement(srcAdvertisement);
		}
		
		throw new IllegalArgumentException("SourceAdvertisement is not a view nor a stream: " + srcAdvertisement.getName());
	}
	


	public boolean removeSource(SourceAdvertisement srcAdvertisement) {
		boolean result = false;
		if (srcAdvertisement != null) {

			result = removeSourceImport(srcAdvertisement);
			result |= removeSourceExport(srcAdvertisement.getName());

			if (publishedSources.contains(srcAdvertisement)) {
				publishedSources.remove(srcAdvertisement);
				cachedSameMap.remove(srcAdvertisement);
				sameSourceMap.remove(srcAdvertisement);

				for (List<SourceAdvertisement> sameList : sameSourceMap.values()) {
					sameList.remove(srcAdvertisement);
				}

				fireSourceRemoveEvent(srcAdvertisement);
				result = true;
			}
		}
		return result;
	}

	@Override
	public ImmutableList<SourceAdvertisement> getSources() {
		return ImmutableList.copyOf(publishedSources);
	}

	@Override
	public ImmutableList<SourceAdvertisement> getSources(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename for getting advertisements must not be null or empty!");

		final String realSourceName = removeUserFromName(sourceName);
		ImmutableList.Builder<SourceAdvertisement> builder = new ImmutableList.Builder<>();
		for (SourceAdvertisement adv : publishedSources) {
			if (adv.getName().equals(realSourceName)) {
				builder.add(adv);
			}
		}

		return builder.build();
	}

	@Override
	public boolean existsSource(SourceAdvertisement srcAdvertisement) {
		return publishedSources.contains(srcAdvertisement);
	}

	@Override
	public boolean existsSource(String srcName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(srcName), "srcname must not be null or empty!");
		
		final String realSrcName = removeUserFromName(srcName);
		for (SourceAdvertisement adv : publishedSources) {
			if (adv.getName().equals(realSrcName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ImmutableList<SourceAdvertisement> getSame(SourceAdvertisement srcAdvertisement) {
		return ImmutableList.copyOf(sameSourceMap.get(srcAdvertisement));
	}

	@Override
	public boolean isSame(SourceAdvertisement a, SourceAdvertisement b) {
		return sameSourceMap.get(a).contains(b);
	}

	@Override
	public void importSource(SourceAdvertisement srcAdvertisement, String sourceNameToUse) throws PeerException, InvalidP2PSource {
		Preconditions.checkNotNull(srcAdvertisement, "SourceAdvertisement to import must not be null!");
		Preconditions.checkArgument(existsSource(srcAdvertisement), "SourceAdvertisement to import is not known to the p2p dictionary");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceNameToUse), "Sourcename to use for import must be null or empty!");

		if( !checkSource(srcAdvertisement )) {
			throw new InvalidP2PSource("Source is invalid!");
		}
		
		final String realSrcNameToUse = removeUserFromName(sourceNameToUse);
		if (dataDictionary.containsViewOrStream(realSrcNameToUse, SessionManagementService.getActiveSession())) {
			throw new PeerException("SourceName '" + realSrcNameToUse + "' is locally already in use");
		}

		if (isImported(srcAdvertisement)) {
			throw new PeerException("ViewAdvertisement already imported");
		}

		List<SourceAdvertisement> srcAdvs = Lists.newArrayList();
		srcAdvs.add(srcAdvertisement);
		if (sameSourceMap.containsKey(srcAdvertisement)) {
			srcAdvs.addAll(sameSourceMap.get(srcAdvertisement));
		}

		List<SourceAdvertisement> nonLocalSrcAdvs = determineNonLocalSourceAdvertisements(srcAdvs);
		if (nonLocalSrcAdvs.isEmpty()) {
			throw new PeerException("SourceAdvertisements contains only views which are published at this peer");
		}

		// TODO: Geschickte auswahl eines SourceAdvertisements! (bei mehr als
		// einen)
		SourceAdvertisement advertisement = nonLocalSrcAdvs.get(0);

		if (advertisement.isStream()) {
			final AccessAO accessAO = advertisement.getAccessAO();

			final ILogicalOperator timestampAO = addTimestampAO(accessAO, null);			
			dataDictionary.setStream(advertisement.getName(), timestampAO, SessionManagementService.getActiveSession());

		} else {
			final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
			receiverOperator.setPipeID(advertisement.getPipeID().toString());
			receiverOperator.setOutputSchema(advertisement.getOutputSchema());
			receiverOperator.setSchema(advertisement.getOutputSchema().getAttributes());
			receiverOperator.setName(realSrcNameToUse + "_Receive");
			receiverOperator.setDestinationName("local");

			final RenameAO renameNoOp = new RenameAO();
			renameNoOp.setDestinationName("local");
			renameNoOp.setNoOp(true);

			receiverOperator.subscribeSink(renameNoOp, 0, 0, receiverOperator.getOutputSchema());
			renameNoOp.initialize();

			dataDictionary.setView(realSrcNameToUse, renameNoOp, SessionManagementService.getActiveSession());
		}

		importedSources.put(advertisement, realSrcNameToUse);

		fireSourceImportEvent(advertisement, realSrcNameToUse);
	}

	@Override
	public boolean removeSourceImport(SourceAdvertisement advertisement) {
		if (importedSources.containsKey(advertisement)) {
			String name = importedSources.get(advertisement);
			importedSources.remove(advertisement);

			dataDictionary.removeViewOrStream(name, SessionManagementService.getActiveSession());

			fireSourceImportRemoveEvent(advertisement, name);
			return true;
		}
		return false;
	}

	@Override
	public Optional<String> getImportedSourceName(SourceAdvertisement advertisement) {
		return Optional.fromNullable(importedSources.get(advertisement));
	}

	@Override
	public Optional<SourceAdvertisement> getImportedSource(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename must not be null or empty!");
		
		final String realSourceName = removeUserFromName(sourceName);
		for (SourceAdvertisement adv : importedSources.keySet()) {
			if (importedSources.get(adv).equals(realSourceName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	@Override
	public boolean isImported(SourceAdvertisement advertisement) {
		return importedSources.containsKey(advertisement);
	}

	@Override
	public boolean isImported(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename must not be null or empty!");

		return getImportedSource(removeUserFromName(sourceName)).isPresent();
	}

	@Override
	public ImmutableList<SourceAdvertisement> getImportedSources() {
		return ImmutableList.copyOf(importedSources.keySet());
	}

	@Override
	public SourceAdvertisement exportSource(String sourceName, String queryBuildConfigurationName) throws PeerException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sorcename must not be null or empty");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(queryBuildConfigurationName), "queryBuildConfigurationName must not be null or empty");
		
		final String realSourceName = removeUserFromName(sourceName);
		if (isExported(realSourceName)) {
			throw new PeerException("Source " + realSourceName + " is already exported");
		}

		if (isImported(realSourceName)) {
			throw new PeerException("Source " + realSourceName + " is imported and cannot be exported directly");
		}

		ILogicalOperator stream = dataDictionary.getStreamForTransformation(realSourceName, SessionManagementService.getActiveSession());
		if (stream != null && stream instanceof TimestampAO) {
			return exportStream(realSourceName, queryBuildConfigurationName, stream);
		}

		ILogicalOperator view = dataDictionary.getView(realSourceName, SessionManagementService.getActiveSession());
		if (view != null) {
			return exportView(realSourceName, queryBuildConfigurationName, view);
		}

		throw new PeerException("Could not find view or stream '" + realSourceName + "' in datadictionary");
	}

	@Override
	public boolean removeSourceExport(String realSourceName) {
		Optional<SourceAdvertisement> optExportAdvertisement = getExportedSource(realSourceName);
		if (optExportAdvertisement.isPresent()) {
			SourceAdvertisement exportAdvertisement = optExportAdvertisement.get();

			Integer queryID = exportedSourcesQueryMap.get(exportAdvertisement);
			exportedSourcesQueryMap.remove(exportAdvertisement);
			if (queryID != -1 && ServerExecutorService.get().getExecutionPlan().getQueryById(queryID) != null) {
				ServerExecutorService.get().removeQuery(queryID, SessionManagementService.getActiveSession());
			}

			fireSourceExportRemoveEvent(exportAdvertisement, realSourceName);

			tryFlushAdvertisement(exportAdvertisement);
			removeSource(exportAdvertisement);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExported(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename must not be null or empty!");
		
		return getExportedSource(removeUserFromName(sourceName)).isPresent();
	}

	@Override
	public Optional<SourceAdvertisement> getExportedSource(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename must not be null or empty!");

		final String realSourceName = removeUserFromName(sourceName);
		for (SourceAdvertisement adv : exportedSourcesQueryMap.keySet()) {
			if (adv.getName().equals(realSourceName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	@Override
	public ImmutableList<SourceAdvertisement> getExportedSources() {
		return ImmutableList.copyOf(exportedSourcesQueryMap.keySet());
	}
	
	public void addPeer( PeerID peerID, String peerName ) {
		Preconditions.checkNotNull(peerID, "PeerID to add must not be null!");
		Preconditions.checkArgument(!existsRemotePeer(peerID), "Peerid is already added");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(peerName), "Peername to add must not be null or empty!");
		
		knownPeersMap.put(peerID, peerName);
		Optional<String> optAddress = determineRemotePeerAddress(peerID);
		if( optAddress.isPresent() ) {
			peersAddressMap.put(peerID, optAddress.get());
		}
		
		firePeerAddEvent(peerID, peerName);
	}
	
	public void removePeer( PeerID peerID ) {
		if( existsRemotePeer(peerID)) {
			String peerName = knownPeersMap.remove(peerID);
			peersAddressMap.remove(peerID);
			firePeerRemoveEvent(peerID, peerName);
		}
	}
	
	public ImmutableMap<PeerID, String> getKnownPeersMap() {
		return ImmutableMap.copyOf(knownPeersMap);
	}
	
	@Override
	public boolean existsRemotePeer(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to add must not be null!");

		return knownPeersMap.containsKey(peerID);
	}
	
	@Override
	public boolean existsRemotePeer(String peerName) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(peerName), "Peername to add must not be null or empty!");

		return knownPeersMap.containsValue(peerName);
	}
	
	@Override
	public PeerID getLocalPeerID() {
		return localPeerID;
	}
	
	@Override
	public String getLocalPeerName() {
		return localPeerName;
	}
	
	@Override
	public PeerGroupID getLocalPeerGroupID() {
		return localPeerGroupID;
	}
	
	public PeerGroup getLocalPeerGroup() {
		return localPeerGroup;
	}
	
	@Override
	public String getLocalPeerGroupName() {
		return localPeerGroup.getPeerGroupName();
	}
	
	// called by activator
	public static void setLocalPeerID( PeerID peerID ) {
		Preconditions.checkNotNull(peerID, "PeerID to set locally must not be null!");
		Preconditions.checkArgument(localPeerID == null, "Local peer id can only be set once");
		
		localPeerID = peerID;
	}
	
	// called by activator
	public static void setLocalPeerName( String peerName ) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(peerName), "Peername to set locally must not be null or empty!");
		Preconditions.checkArgument(localPeerName == null, "Local peer name can only be set once");
		
		localPeerName = peerName;
	}
	
	// called by activator
	public static void setLocalPeerGroup( PeerGroup id ) {
		Preconditions.checkNotNull(id, "PeerGroup must not be null!");
		Preconditions.checkArgument(localPeerGroup == null, "Local peer group id can only be set once");
		
		localPeerGroup = id;
		localPeerGroupID = id.getPeerGroupID();
	}
	
	@Override
	public ImmutableList<PeerID> getRemotePeerIDs() {
		return ImmutableList.copyOf(knownPeersMap.keySet());
	}
	
	@Override
	public Optional<String> getRemotePeerName(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get the name from must not be null!");
		
		if( peerID.equals(localPeerID)) {
			return Optional.of(localPeerName);
		}
		return Optional.fromNullable(knownPeersMap.get(peerID));
	}
	
	@Override
	public Optional<String> getRemotePeerAddress(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get the address from must not be null!");

		String address = peersAddressMap.get(peerID);
		if( Strings.isNullOrEmpty(address)) {
			Optional<String> address2 = determineRemotePeerAddress(peerID);
			if( address2.isPresent() ) {
				peersAddressMap.put(peerID, address2.get());
				return Optional.of(address2.get());
			} 
			return Optional.absent();
		}
		return Optional.of(address);
	}

	// called by DataDictionary
	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		// do nothing
	}

	// called by DataDictionary
	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		final String realSourceName = removeUserFromName(name);

		Optional<SourceAdvertisement> optImportedSrcAdvertisement = getImportedSource(realSourceName);
		if (optImportedSrcAdvertisement.isPresent()) {
			removeSourceImport(optImportedSrcAdvertisement.get());
		}
		removeSourceExport(realSourceName);

		Optional<SourceAdvertisement> optOwnAdvertisement = find(localPeerID, realSourceName);
		if (optOwnAdvertisement.isPresent()) {
			SourceAdvertisement ownAdvertisement = optOwnAdvertisement.get();

			tryFlushAdvertisement(ownAdvertisement);

			removeSource(ownAdvertisement);
		}
	}

	// called by DataDictionary
	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

	// called by ServerExecutor
	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		final int queryID = query.getID();
		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {

			String exportedViewName = null;
			for (SourceAdvertisement exportAdv : exportedSourcesQueryMap.keySet()) {
				if (exportedSourcesQueryMap.get(exportAdv).equals(queryID)) {
					exportedViewName = exportAdv.getName();
					break; // to avoid ConcurrentMod-Exception
				}
			}
			if (exportedViewName != null) {
				removeSourceExport(exportedViewName);
			}
		}
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

	protected final void fireSourceAddEvent(SourceAdvertisement advertisement) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceAdded(this, advertisement);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireSourceRemoveEvent(SourceAdvertisement advertisement) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceRemoved(this, advertisement);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireSourceImportEvent(SourceAdvertisement advertisement, String viewName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceImported(this, advertisement, viewName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireSourceImportRemoveEvent(SourceAdvertisement advertisement, String viewName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceImportRemoved(this, advertisement, viewName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	private void fireSourceExportEvent(SourceAdvertisement advertisement, String viewName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceExported(this, advertisement, viewName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void fireSourceExportRemoveEvent(SourceAdvertisement exportAdvertisement, String name) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.sourceExportRemoved(this, exportAdvertisement, name);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	protected final void firePeerAddEvent(PeerID peerID, String peerName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.remotePeerAdded(this, peerID, peerName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}
	
	protected final void firePeerRemoveEvent(PeerID peerID, String peerName) {
		synchronized (listeners) {
			for (IP2PDictionaryListener listener : listeners) {
				try {
					listener.remotePeerRemoved(this, peerID, peerName);
				} catch (Throwable t) {
					LOG.error("Exception during invokinf p2p dictionary listener", t);
				}
			}
		}
	}

	private SourceAdvertisement exportStream(String streamName, String queryBuildConfigurationName, ILogicalOperator stream) throws PeerException {
		Optional<AccessAO> optAccessAO = determineAccessAO(stream);
		if (optAccessAO.isPresent()) {

			SourceAdvertisement srcAdvertisement = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
			srcAdvertisement.setID(IDFactory.newPipeID(localPeerGroupID));
			srcAdvertisement.setName(removeUserFromName(streamName));
			srcAdvertisement.setPeerID(localPeerID);
			srcAdvertisement.setAccessAO(optAccessAO.get());
			srcAdvertisement.setOutputSchema(stream.getOutputSchema());

			try {
				JxtaServicesProvider.getInstance().getDiscoveryService().publish(srcAdvertisement);
				JxtaServicesProvider.getInstance().getDiscoveryService().remotePublish(srcAdvertisement);
				addSource(srcAdvertisement, false);

				exportedSourcesQueryMap.put(srcAdvertisement, -1);

				fireSourceExportEvent(srcAdvertisement, streamName);

				return srcAdvertisement;
			} catch (final IOException | InvalidP2PSource ex) {
				throw new PeerException("Could not advertise stream '" + streamName + "'", ex);
			} 
		}
		throw new PeerException("Could not find accessAO of stream '" + stream + "'");
	}

	private SourceAdvertisement exportView(String viewName, String queryBuildConfigurationName, final ILogicalOperator view) throws PeerException {
		try {
			final PipeID pipeID = IDFactory.newPipeID(localPeerGroupID);

			SourceAdvertisement viewAdvertisement = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
			viewAdvertisement.setID(IDFactory.newPipeID(localPeerGroupID));
			viewAdvertisement.setOutputSchema(view.getOutputSchema());
			viewAdvertisement.setPipeID(pipeID);
			viewAdvertisement.setName(removeUserFromName(viewName));
			viewAdvertisement.setPeerID(localPeerID);

			JxtaServicesProvider.getInstance().getDiscoveryService().publish(viewAdvertisement, EXPORT_LIFETIME_MILLIS, EXPORT_LIFETIME_MILLIS);
			JxtaServicesProvider.getInstance().getDiscoveryService().remotePublish(viewAdvertisement, EXPORT_LIFETIME_MILLIS);
			addSource(viewAdvertisement, false);

			final JxtaSenderAO jxtaSender = new JxtaSenderAO();
			jxtaSender.setName(viewName + "_Send");
			jxtaSender.setPipeID(pipeID.toString());
			jxtaSender.setUseUDP(false);
			view.subscribeSink(jxtaSender, 0, 0, view.getOutputSchema());
			
			Integer queryID = ServerExecutorService.get().addQuery(jxtaSender, SessionManagementService.getActiveSession(), queryBuildConfigurationName);
			IPhysicalQuery physicalQuery = ServerExecutorService.get().getExecutionPlan().getQueryById(queryID);
			ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
			logicalQuery.setName(viewName);
			logicalQuery.setParserId("P2P");
			logicalQuery.setUser(SessionManagementService.getActiveSession());

			exportedSourcesQueryMap.put(viewAdvertisement, queryID);

			fireSourceExportEvent(viewAdvertisement, viewName);

			return viewAdvertisement;

		} catch (IOException | InvalidP2PSource e) {
			throw new PeerException("Could not publish view '" + viewName + "'", e);
		}
	}

	private Optional<SourceAdvertisement> find(PeerID peerID, String sourceName) {
		for (SourceAdvertisement adv : publishedSources) {
			if (adv.getPeerID().equals(peerID) && adv.getName().equals(sourceName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	private void addSame(SourceAdvertisement adv1, SourceAdvertisement adv2) {
		List<SourceAdvertisement> same1 = sameSourceMap.get(adv1);
		List<SourceAdvertisement> same2 = sameSourceMap.get(adv2);
		same1.add(adv2);
		same2.add(adv1);
	}

	private void checkSameCache(SourceAdvertisement srcAdvertisement) {
		for (SourceAdvertisement potencialSameAdv : cachedSameMap.keySet().toArray(new SourceAdvertisement[0])) {
			for (SourceAdvertisement.Same potencialSame : cachedSameMap.get(potencialSameAdv).toArray(new SourceAdvertisement.Same[0])) {
				if (potencialSame.getPeerID().equals(srcAdvertisement.getPeerID()) && potencialSame.getName().equals(srcAdvertisement.getName())) {
					addSame(srcAdvertisement, potencialSameAdv);

					cachedSameMap.get(potencialSameAdv).remove(potencialSame);
				}
			}
		}
	}

	private static void tryFlushAdvertisement(SourceAdvertisement srcAdvertisement) {
		try {
			JxtaServicesProvider.getInstance().getDiscoveryService().flushAdvertisement(srcAdvertisement);
		} catch (IOException e) {
			LOG.error("Could not flush view advertisement {}", srcAdvertisement, e);
		}
	}

	private static List<SourceAdvertisement> determineNonLocalSourceAdvertisements(List<SourceAdvertisement> srcAdvs) {
		List<SourceAdvertisement> result = Lists.newArrayList();
		for (SourceAdvertisement srcAdv : srcAdvs) {
			if (!srcAdv.isLocal()) {
				result.add(srcAdv);
			}
		}
		return result;
	}

	private static Optional<AccessAO> determineAccessAO(ILogicalOperator start) {
		if (start instanceof AccessAO) {
			return Optional.of((AccessAO) start);
		}

		for (final LogicalSubscription subscription : start.getSubscribedToSource()) {
			final Optional<AccessAO> optAcccessAO = determineAccessAO(subscription.getTarget());
			if (optAcccessAO.isPresent()) {
				return optAcccessAO;
			}
		}

		return Optional.absent();
	}

	private static ILogicalOperator addTimestampAO(ILogicalOperator operator, String dateFormat) {
		final TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDestinationName("local");
		timestampAO.setDateFormat(dateFormat);
		if (operator.getOutputSchema() != null) {

			for (final SDFAttribute attr : operator.getOutputSchema()) {
				if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI()) || SDFDatatype.START_TIMESTAMP_STRING.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setStartTimestamp(attr);
				}

				if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI()) || SDFDatatype.END_TIMESTAMP_STRING.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setEndTimestamp(attr);
				}

			}
		}
		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		timestampAO.setName(timestampAO.getStandardName());
		return timestampAO;
	}
	
	private static String removeUserFromName(String streamName) {
		int pos = streamName.indexOf(".");
		if (pos >= 0) {
			return streamName.substring(pos + 1);
		}
		return streamName;
	}
	
	private static boolean isAutoImport() {
		String property = System.getProperty(AUTOIMPORT_SYS_PROPERTY);
		if( !Strings.isNullOrEmpty(property)) {
			return property.equalsIgnoreCase("true");
		}
		return false;
	}
	
	private static boolean isAutoExport() {
		String property = System.getProperty(AUTOEXPORT_SYS_PROPERTY);
		if( !Strings.isNullOrEmpty(property)) {
			return property.equalsIgnoreCase("true");
		}
		return false;
	}

	private static boolean checkViewAdvertisement(SourceAdvertisement srcAdvertisement) {
		PeerID sourcePeerID = srcAdvertisement.getPeerID();
		if( sourcePeerID.equals(P2PDictionary.getInstance().getLocalPeerID())) {
			return true;
		}
		
		if( !JxtaServicesProvider.getInstance().getEndpointService().isReachable(sourcePeerID, false)) {
			LOG.error("Could not reach peer with exported source {}", srcAdvertisement.getName());
			return false;
		}
		return true;
	}
	
	private static boolean checkStreamAdvertisement(SourceAdvertisement srcAdvertisement) {
		final AccessAO accessAO = srcAdvertisement.getAccessAO();
		final Map<String, String> optionsMap = accessAO.getOptionsMap();
		
		String host = optionsMap.get("host");
		if( Strings.isNullOrEmpty(host)) {
			LOG.error("Host not specified. Invalid sourceAdvertisement in p2p-context: {}", srcAdvertisement.getName());
			return false;
		}
		
		if( host.equalsIgnoreCase("localhost") && srcAdvertisement.getPeerID().equals(P2PDictionary.getInstance().getLocalPeerID())) {
			LOG.error("SourceAdvertisement has localhost as host (with non local origin peer): {}", srcAdvertisement.getName());
			return false;
		}
		
		try {
			// Nicht zuverlässig. Kann true zurückgeben, obwohl der host
			// nicht erreichbar ist
			InetAddress hostAddress = InetAddress.getByName(host);
			if( !hostAddress.isReachable(REACHABLE_TIMEOUT_MILLIS) ) {
				LOG.error("Host {} is not reachable");
				return false;
			}
		} catch (UnknownHostException e) {
			LOG.error("Unknown host {}", host, e );
			return false;
		} catch (IOException e) {
			LOG.error("Could not test for reachability host {}", host, e);
			return false;
		} 
		
		return true;
	}
	
	private static Optional<String> determineRemotePeerAddress(PeerID peerID) {
		try {
			Iterator<MessageTransport> allMessageTransports = JxtaServicesProvider.getInstance().getEndpointService().getAllMessageTransports();
			while (allMessageTransports.hasNext()) {
				MessageTransport next = allMessageTransports.next();
				if (next instanceof EndpointRouter) {
					RouteController routeController = ((EndpointRouter) next).getRouteController();

					Collection<RouteAdvertisement> routes = routeController.getRoutes(peerID);
					for (RouteAdvertisement route : routes) {
						if (peerID.equals(route.getDestPeerID())) {
							List<EndpointAddress> destEndpointAddresses = route.getDestEndpointAddresses();
							for (EndpointAddress destEndpointAddress : destEndpointAddresses) {
								String address = destEndpointAddress.getProtocolAddress();
								return Optional.of(address);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOG.error("Could not determine address of peerid {}", peerID, t);
		}
		return Optional.absent();
	}
}
