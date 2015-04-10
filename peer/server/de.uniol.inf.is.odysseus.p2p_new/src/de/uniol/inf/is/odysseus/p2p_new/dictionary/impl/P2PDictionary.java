package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.service.PQLParserService;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;

public class P2PDictionary implements IP2PDictionary, IDataDictionaryListener, IPlanModificationListener, IDatadictionaryProviderListener, IAdvertisementDiscovererListener {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionary.class);

	private static final String AUTOIMPORT_SYS_PROPERTY = "peer.autoimport";
	private static final String AUTOEXPORT_SYS_PROPERTY = "peer.autoexport";

	private static P2PDictionary instance;
	private static IPQLGenerator pqlGenerator;
	private static IPeerDictionary peerDictionary;

	private final List<IP2PDictionaryListener> listeners = Lists.newArrayList();

	private final Map<SourceAdvertisement, String> importedSources = Maps.newHashMap();
	private final Map<SourceAdvertisement, Integer> exportedSourcesQueryMap = Maps.newHashMap();
	private final Map<SourceAdvertisement, JxtaSenderAO> exportedSenderMap = Maps.newHashMap();

	private final RemoveSourceAdvertisementCollector removeSourceAdvCollector = new RemoveSourceAdvertisementCollector();
	private final SourceAdvertisementCollector sourceAdvCollector = new SourceAdvertisementCollector();
	private final Collection<SourceAdvertisement> srcAdvs = Lists.newArrayList();

	private AutoExporter autoExporter;

	// called by OSGi-DS
	public void bindListener(IP2PDictionaryListener serv) {
		addListener(serv);
	}

	// called by OSGi-DS
	public void unbindListener(IP2PDictionaryListener serv) {
		removeListener(serv);
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;

		ITenant tenant = SessionManagementService.getTenant();
		DataDictionaryProvider.subscribe(tenant, this);

		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
		if (dd != null) {
			newDatadictionary(dd);
		}

		Thread waitingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				P2PNetworkManagerService.waitForStart();
				P2PNetworkManagerService.getInstance().addAdvertisementListener(P2PDictionary.this);
			}

		});
		waitingThread.setDaemon(true);
		waitingThread.setName("Waiting for p2p network instance");
		waitingThread.start();

		removeSourceAdvCollector.start();
		sourceAdvCollector.start();
	}

	// called by OSGi-DS
	public void deactivate() {
		P2PNetworkManagerService.getInstance().removeAdvertisementListener(this);

		instance = null;
		removeSourceAdvCollector.stopRunning();
		sourceAdvCollector.stopRunning();

		if (JxtaServicesProviderService.isBound()) {
			removeAllExportedViews();
		}
		DataDictionaryProvider.unsubscribe(this);
	}

	private void removeAllExportedViews() {
		List<String> srcs = Lists.newArrayList();
		for (SourceAdvertisement srcAdvertisement : exportedSourcesQueryMap.keySet()) {
			if (srcAdvertisement.isView()) {
				srcs.add(srcAdvertisement.getName());
			}
		}
		removeSourcesExport(srcs);
	}

	@Override
	public void newDatadictionary(IDataDictionary dataDictionary) {
		// TODO: Handle unbinding of dd?

		dataDictionary.addListener(this);
		if (isAutoExport()) {
			autoExporter = new AutoExporter(this);
			dataDictionary.addListener(autoExporter);
		}

		LOG.debug("DataDictionary bound {}", dataDictionary);
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		dd.removeListener(this);
		if (isAutoExport()) {
			dd.removeListener(autoExporter);
		}
		LOG.debug("DataDictionary unbound {}", dd);
	}

	private IDataDictionaryWritable getDataDictionary() {
		return (IDataDictionaryWritable) DataDictionaryProvider.getDataDictionary(SessionManagementService.getActiveSession().getTenant());
	}

	public static P2PDictionary getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	@Override
	public Collection<SourceAdvertisement> getSources() {
		synchronized (srcAdvs) {
			return srcAdvs;
		}
	}

	@Override
	public void updateAdvertisements() {
		synchronized (srcAdvs) {
			srcAdvs.clear();
			srcAdvs.addAll(collectAllReachableSourceAdvertisements());
			applyRemoveSourceAdvertisements(srcAdvs);

			ImmutableList<SourceAdvertisement> importedSources = getImportedSources();
			for (SourceAdvertisement importedSource : importedSources) {
				if (!srcAdvs.contains(importedSource)) {
					removeSourceImport(importedSource);
					tryFlushAdvertisement(importedSource);
				}
			}
		}
	}

	private void applyRemoveSourceAdvertisements(Collection<SourceAdvertisement> srcAdvs) {
		Collection<SourceAdvertisement> srcAdvsToRemove = Lists.newLinkedList();
		for (RemoveSourceAdvertisement remAdv : JxtaServicesProviderService.getInstance().getLocalAdvertisements(RemoveSourceAdvertisement.class)) {
			for (SourceAdvertisement srcAdv : srcAdvs) {
				if (srcAdv.getID().equals(remAdv.getSourceAdvertisementID())) {
					srcAdvsToRemove.add(srcAdv);
					tryFlushAdvertisement(srcAdv);

					removeSourceImport(srcAdv);
				}
			}
		}
		srcAdvs.removeAll(srcAdvsToRemove);

		srcAdvsToRemove.clear();
		for (RemoveMultipleSourceAdvertisement remMultiSrcAdvertisement : JxtaServicesProviderService.getInstance().getLocalAdvertisements(RemoveMultipleSourceAdvertisement.class)) {
			for (ID id : remMultiSrcAdvertisement.getSourceAdvertisementIDs()) {
				for (SourceAdvertisement srcAdv : srcAdvs) {
					if (srcAdv.getID().equals(id)) {
						srcAdvsToRemove.add(srcAdv);
						tryFlushAdvertisement(srcAdv);

						removeSourceImport(srcAdv);
					}
				}
			}
		}
		srcAdvs.removeAll(srcAdvsToRemove);
	}

	private Collection<SourceAdvertisement> collectAllReachableSourceAdvertisements() {
		Collection<SourceAdvertisement> srcAdvs = JxtaServicesProviderService.getInstance().getLocalAdvertisements(SourceAdvertisement.class);
		Iterator<SourceAdvertisement> it = srcAdvs.iterator();
		while (it.hasNext()) {
			SourceAdvertisement srcAdv = it.next();
			if (!srcAdv.isLocal() && (srcAdv.isView() && !peerDictionary.getRemotePeerIDs().contains(srcAdv.getPeerID()))) {
				removeSourceImport(srcAdv);
				tryFlushAdvertisement(srcAdv);
				it.remove();
			}
		}

		Collection<MultipleSourceAdvertisement> multiSrcAdvs = JxtaServicesProviderService.getInstance().getLocalAdvertisements(MultipleSourceAdvertisement.class);

		for (MultipleSourceAdvertisement multiSrcAdv : multiSrcAdvs) {

			if (!multiSrcAdv.isLocal() && !peerDictionary.getRemotePeerIDs().contains(multiSrcAdv.getPeerID())) {
				for (SourceAdvertisement srcAdv : multiSrcAdv.getSourceAdvertisements()) {
					removeSourceImport(srcAdv);
				}
				tryFlushAdvertisement(multiSrcAdv);

			} else {
				srcAdvs.addAll(multiSrcAdv.getSourceAdvertisements());
			}
		}
		return srcAdvs;
	}

	@Override
	public Collection<SourceAdvertisement> getSources(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename for getting advertisements must not be null or empty!");

		String realSourceName = removeUserFromName(sourceName);

		Collection<SourceAdvertisement> namedAdvs = Lists.newArrayList();
		for (SourceAdvertisement srcAdv : getSources()) {
			if (srcAdv.getName().equals(realSourceName)) {
				namedAdvs.add(srcAdv);
			}
		}

		return namedAdvs;
	}

	@Override
	public boolean isSourceNameAlreadyInUse(String sourceName) {
		String realSrcNameToUse = removeUserFromName(sourceName);
		if (getDataDictionary().containsViewOrStream(realSrcNameToUse, SessionManagementService.getActiveSession())) {
			return true;
		}
		return false;
	}

	@Override
	public void importSource(SourceAdvertisement srcAdvertisement, String sourceNameToUse) throws PeerException, InvalidP2PSource {
		Preconditions.checkNotNull(srcAdvertisement, "SourceAdvertisement to import must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceNameToUse), "Sourcename to use for import must be null or empty!");

		String realSrcNameToUse = removeUserFromName(sourceNameToUse);
		if (isSourceNameAlreadyInUse(sourceNameToUse)) {
			throw new PeerException("SourceName '" + realSrcNameToUse + "' is locally already in use");
		}
		LOG.debug("Beginning to import source {}", sourceNameToUse);

		if (isImported(srcAdvertisement)) {
			throw new PeerException("Source already imported");
		}

		List<SourceAdvertisement> srcAdvs = Lists.newArrayList();
		srcAdvs.add(srcAdvertisement);

		List<SourceAdvertisement> nonLocalSrcAdvs = determineNonLocalSourceAdvertisements(srcAdvs);
		if (nonLocalSrcAdvs.isEmpty()) {
			throw new PeerException("SourceAdvertisements contains only views which are published at this peer");
		}

		// TODO: Geschickte auswahl eines SourceAdvertisements! (bei mehr als
		// einen)
		SourceAdvertisement advertisement = nonLocalSrcAdvs.get(0);

		if (advertisement.isStream()) {
			LOG.debug("Importing source is a stream!");
			String pqlText = advertisement.getPQLText();
			LOG.debug("PQL-Text: " + pqlText);

			try {
				LOG.debug("Parsing with pql-Parser");
				List<IExecutorCommand> commands = PQLParserService.getParser().parse(pqlText, SessionManagementService.getActiveSession(), getDataDictionary(), Context.empty());
				ILogicalQuery query = null;

				for (IExecutorCommand cmd : commands) {

					if (!(cmd instanceof CreateQueryCommand)) {
						LOG.error("PQL parser did return an executor command, which is not a create query command!");
						continue;
					}

					CreateQueryCommand createCmd = (CreateQueryCommand) cmd;
					if (query != null) {
						throw new PeerException("PQLParser returned multiple logical queries");
					}
					query = createCmd.getQuery();
				}

				if( query != null ) {
					Collection<File> files = determineNeededFiles(query);
					LOG.info("Query is dependent from {} files.", files.size() );
					for( File file : files ) {
						if( !file.exists() ) {
							throw new PeerException("Could not import '" + realSrcNameToUse + "' since the specified file '" + file.getName() + "' does not exist.");
						}
					}
					
					importedSources.put(advertisement, realSrcNameToUse);
					getDataDictionary().setStream(realSrcNameToUse, query.getLogicalPlan(), SessionManagementService.getActiveSession());
				} else {
					throw new PeerException("Could not import '" + realSrcNameToUse + "' since the parser did not return a runnable query");
				}

			} catch (QueryParseException e) {
				throw new PeerException("Could not import source " + realSrcNameToUse, e);
			}

		} else {
			LOG.debug("Importing source is a view");

			final JxtaReceiverAO receiverOperator = new JxtaReceiverAO();
			receiverOperator.setPipeID(advertisement.getPipeID().toString());
			receiverOperator.setOutputSchema(advertisement.getOutputSchema());
			receiverOperator.setBaseTimeunit(advertisement.getBaseTimeunit());
			receiverOperator.setSchema(advertisement.getOutputSchema().getAttributes());
			receiverOperator.setSchemaName(advertisement.getOutputSchema().getURI());
			receiverOperator.setName(realSrcNameToUse + "_Receive");
			receiverOperator.setImportedSourceAdvertisement(advertisement);
			receiverOperator.setPeerID(advertisement.getPeerID().toString());

			importedSources.put(advertisement, realSrcNameToUse);
			getDataDictionary().setView(realSrcNameToUse, receiverOperator, SessionManagementService.getActiveSession());
		}

		fireSourceImportEvent(advertisement, realSrcNameToUse);
		LOG.debug("Import finished");
	}

	private static Collection<File> determineNeededFiles(ILogicalQuery query) {
		Collection<ILogicalOperator> operators = getAllOperators(query);
		Collection<File> result = Lists.newArrayList();
		
		for( ILogicalOperator operator : operators ) {
			if (operator instanceof AbstractAccessAO) {
				AbstractAccessAO fileAccess = (AbstractAccessAO) operator;
				
				String filename = fileAccess.getOptionsMap().get(FileHandler.FILENAME);
				if (!Strings.isNullOrEmpty(filename)) {
					result.add(new File(filename));
				}
			}
		}
		
		return result;
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalQuery plan) {
		return getAllOperators(plan.getLogicalPlan());
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}

	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}

	@Override
	public boolean removeSourceImport(SourceAdvertisement advertisement) {
		if (importedSources.containsKey(advertisement)) {
			String name = importedSources.get(advertisement);
			importedSources.remove(advertisement);

			getDataDictionary().removeViewOrStream(name, SessionManagementService.getActiveSession());

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
	public SourceAdvertisement exportSource(String sourceName) throws PeerException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "Sourcename must not be null or empty");
		LOG.debug("Exporting source {}", sourceName);

		SourceAdvertisement adv = createSourceAdvertisement(sourceName);
		sourceAdvCollector.add(adv);

		LOG.debug("Export finished");
		return adv;
	}

	@Override
	public Collection<SourceAdvertisement> exportSources(Collection<String> sourceNames) throws PeerException {
		Preconditions.checkNotNull(sourceNames, "sourceNames must not be null!");

		if (sourceNames.size() == 1) {
			return Lists.newArrayList(exportSource(sourceNames.iterator().next()));
		}

		LOG.debug("Exporting multiple sources {}", sourceNames);

		Collection<SourceAdvertisement> sourceAdvertisements = Lists.newArrayList();
		for (String sourceName : sourceNames) {
			sourceAdvertisements.add(createSourceAdvertisement(sourceName));
		}

		if (!sourceAdvertisements.isEmpty()) {
			MultipleSourceAdvertisement multipleSrcAdv = (MultipleSourceAdvertisement) AdvertisementFactory.newAdvertisement(MultipleSourceAdvertisement.getAdvertisementType());
			multipleSrcAdv.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
			multipleSrcAdv.setPeerID(P2PNetworkManagerService.getInstance().getLocalPeerID());
			multipleSrcAdv.setSourceAdvertisements(sourceAdvertisements);

			try {
				JxtaServicesProviderService.getInstance().publishInfinite(multipleSrcAdv);
			} catch (IOException e) {
				throw new PeerException("Could not publish multiple source", e);
			}
		}

		LOG.debug("Export finished");
		return sourceAdvertisements;
	}

	private SourceAdvertisement createSourceAdvertisement(String sourceName) throws PeerException {
		LOG.debug("Beginning exporting source {}", sourceName);

		final String realSourceName = removeUserFromName(sourceName);
		if (isExported(realSourceName)) {
			throw new PeerException("Source " + realSourceName + " is already exported");
		}

		if (isImported(realSourceName)) {
			throw new PeerException("Source " + realSourceName + " is imported and cannot be exported directly");
		}

		ILogicalOperator originalStream = getDataDictionary().getStreamForTransformation(realSourceName, SessionManagementService.getActiveSession());

		if (originalStream != null) {
			LOG.debug("Stream is logical operator {}", originalStream);
			LOG.debug("Stream --> exportStream()");
			return exportStream(realSourceName, copyLogicalPlan(originalStream));
		}

		LOG.debug("No stream {} found. Trying to get view with this name.", realSourceName);

		ILogicalOperator view = getDataDictionary().getView(realSourceName, SessionManagementService.getActiveSession());
		if (view != null) {
			LOG.debug("Got logical operator for view " + view);
			return exportView(realSourceName, copyLogicalPlan(view));
		}

		throw new PeerException("Could not find view or stream '" + realSourceName + "' in datadictionary");
	}

	private static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		Preconditions.checkNotNull(originPlan, "Logical plan to copy must not be null!");

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<>();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
	}

	@Override
	public boolean removeSourceExport(String realSourceName) {
		Optional<SourceAdvertisement> optExportAdvertisement = getExportedSource(realSourceName);
		if (optExportAdvertisement.isPresent()) {
			removeSourceExportImpl(realSourceName, optExportAdvertisement.get());

			if (optExportAdvertisement.get().isView()) {
				publishRemoveSourceAdvertisement(optExportAdvertisement.get());
			}
			return true;
		}
		return false;
	}

	private void removeSourceExportImpl(String realSourceName, SourceAdvertisement exportAdvertisement) {
		Integer queryID = exportedSourcesQueryMap.get(exportAdvertisement);
		exportedSourcesQueryMap.remove(exportAdvertisement);
		if (queryID != -1 && ServerExecutorService.isBound() && ServerExecutorService.getServerExecutor().getExecutionPlan().getQueryById(queryID) != null) {
			ServerExecutorService.getServerExecutor().removeQuery(queryID, SessionManagementService.getActiveSession());
		}
		exportedSenderMap.remove(exportAdvertisement);

		fireSourceExportRemoveEvent(exportAdvertisement, realSourceName);

		tryFlushAdvertisement(exportAdvertisement);
	}

	@Override
	public boolean removeSourcesExport(Collection<String> sourceNames) {
		Preconditions.checkNotNull(sourceNames, "sourceNames must not be null!");

		if (sourceNames.size() == 1) {
			return removeSourceExport(sourceNames.iterator().next());
		}

		Collection<ID> sourceIDRemoved = Lists.newArrayList();
		for (String sourceName : sourceNames) {
			Optional<SourceAdvertisement> optExportAdvertisement = getExportedSource(sourceName);
			if (optExportAdvertisement.isPresent()) {
				removeSourceExportImpl(sourceName, optExportAdvertisement.get());
				sourceIDRemoved.add(optExportAdvertisement.get().getID());
			}
		}

		if (!sourceIDRemoved.isEmpty()) {
			RemoveMultipleSourceAdvertisement adv = (RemoveMultipleSourceAdvertisement) AdvertisementFactory.newAdvertisement(RemoveMultipleSourceAdvertisement.getAdvertisementType());
			adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
			adv.setSourceAdvertisementIDs(sourceIDRemoved);

			try {
				JxtaServicesProviderService.getInstance().publish(adv);
				JxtaServicesProviderService.getInstance().remotePublish(adv);
			} catch (IOException e) {
				LOG.error("Could not publish removeSourceAdvertisement", e);
			}
		}

		return false;
	}

	private void publishRemoveSourceAdvertisement(SourceAdvertisement sourceAdvToRemove) {
		RemoveSourceAdvertisement removeSourceAdvertisement = (RemoveSourceAdvertisement) AdvertisementFactory.newAdvertisement(RemoveSourceAdvertisement.getAdvertisementType());
		removeSourceAdvertisement.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
		removeSourceAdvertisement.setSourceAdvertisementID(sourceAdvToRemove.getID());

		removeSourceAdvCollector.add(removeSourceAdvertisement);
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

		Optional<SourceAdvertisement> optOwnAdvertisement = find(P2PNetworkManagerService.getInstance().getLocalPeerID(), realSourceName);
		if (optOwnAdvertisement.isPresent()) {
			SourceAdvertisement ownAdvertisement = optOwnAdvertisement.get();

			tryFlushAdvertisement(ownAdvertisement);
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

	private SourceAdvertisement exportStream(String streamName, ILogicalOperator stream) {
		LOG.debug("Exporting stream {} with logical operator {}", streamName, stream);

		SourceAdvertisement srcAdvertisement = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
		srcAdvertisement.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
		srcAdvertisement.setName(removeUserFromName(streamName));
		srcAdvertisement.setPeerID(P2PNetworkManagerService.getInstance().getLocalPeerID());
		srcAdvertisement.setPQLText(pqlGenerator.generatePQLStatement(stream));
		srcAdvertisement.setOutputSchema(stream.getOutputSchema());
		Optional<String> optBaseTimeunitString = getBaseTimeunitString(stream.getOutputSchema());
		if( optBaseTimeunitString.isPresent() ) {
			srcAdvertisement.setBaseTimeunit(optBaseTimeunitString.get());
		}
		
		LOG.debug("Generated PQL-Text: " + srcAdvertisement.getPQLText());

		exportedSourcesQueryMap.put(srcAdvertisement, -1);

		fireSourceExportEvent(srcAdvertisement, streamName);

		return srcAdvertisement;
	}

	private SourceAdvertisement exportView(String viewName, final ILogicalOperator view) {
		LOG.debug("Exporting view {} with logical operator {}", viewName, view);

		PipeID pipeID = IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID());

		SourceAdvertisement viewAdvertisement = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
		viewAdvertisement.setID(IDFactory.newPipeID(P2PNetworkManagerService.getInstance().getLocalPeerGroupID()));
		viewAdvertisement.setOutputSchema(view.getOutputSchema());
		viewAdvertisement.setPipeID(pipeID);
		viewAdvertisement.setName(removeUserFromName(viewName));
		viewAdvertisement.setPeerID(P2PNetworkManagerService.getInstance().getLocalPeerID());
		Optional<String> optBaseTimeunitString = getBaseTimeunitString(view.getOutputSchema());
		if( optBaseTimeunitString.isPresent() ) {
			viewAdvertisement.setBaseTimeunit(optBaseTimeunitString.get());
		}

		JxtaSenderAO jxtaSender = new JxtaSenderAO();
		jxtaSender.setName(viewName + "_Send");
		jxtaSender.setPipeID(pipeID.toString());
		jxtaSender.setUseUDP(false);

		StreamAO streamAO = new StreamAO();
		streamAO.setSourceName(new Resource(SessionManagementService.getActiveSession().getUser(), viewName));
		streamAO.setOutputSchema(view.getOutputSchema());
		streamAO.subscribeSink(jxtaSender, 0, 0, streamAO.getOutputSchema());

		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER PQL\n");
		sb.append("#METADATA IntervalLatency\n");
		sb.append("#QNAME Exporting " + viewName + "\n");
		sb.append("#ADDQUERY\n");
		sb.append(pqlGenerator.generatePQLStatement(jxtaSender));
		sb.append("\n");
		String scriptText = sb.toString();
		LOG.debug("Created script text for execution: {}", scriptText);

		Collection<Integer> queryIDs = ServerExecutorService.getServerExecutor().addQuery(scriptText, "OdysseusScript", SessionManagementService.getActiveSession(), Context.empty());
		Integer queryID = queryIDs.iterator().next();
		LOG.debug("Script executed --> query id is {}", queryID);

		IPhysicalQuery physicalQuery = ServerExecutorService.getServerExecutor().getExecutionPlan().getQueryById(queryID);
		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
		logicalQuery.setName(viewName);
		logicalQuery.setParserId("P2P");
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText("Exporting " + viewName);

		exportedSourcesQueryMap.put(viewAdvertisement, queryID);
		exportedSenderMap.put(viewAdvertisement, jxtaSender);

		fireSourceExportEvent(viewAdvertisement, viewName);

		return viewAdvertisement;
	}
	
	private static Optional<String> getBaseTimeunitString(SDFSchema schema) {
		Preconditions.checkNotNull(schema, "Schema to get basetimeunit from must not be null!");

		SDFConstraint baseTimeunitConstraint = schema.getConstraint(SDFConstraint.BASE_TIME_UNIT);
		if (baseTimeunitConstraint != null) {
			Object value = baseTimeunitConstraint.getValue();
			if (value != null && value instanceof TimeUnit) {
				return Optional.of(((TimeUnit) value).toString());
			}
		}
		
		return Optional.absent();
	}
	
	@Override
	public Optional<JxtaSenderAO> getExportingSenderAO(SourceAdvertisement advertisement) {
		return Optional.fromNullable(exportedSenderMap.get(advertisement));
	}

	private Optional<SourceAdvertisement> find(PeerID peerID, String sourceName) {
		for (SourceAdvertisement adv : getSources()) {
			if (adv.getPeerID().equals(peerID) && adv.getName().equals(sourceName)) {
				return Optional.of(adv);
			}
		}
		return Optional.absent();
	}

	private static void tryFlushAdvertisement(Advertisement srcAdvertisement) {
		try {
			if (JxtaServicesProviderService.isBound()) {
				JxtaServicesProviderService.getInstance().flushAdvertisement(srcAdvertisement);
			}
		} catch (IOException e) {
			LOG.error("Could not flush advertisement {}", srcAdvertisement, e);
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

	private static String removeUserFromName(String streamName) {
		int pos = streamName.indexOf(".");
		if (pos >= 0) {
			return streamName.substring(pos + 1);
		}
		return streamName;
	}

	private static boolean isAutoExport() {
		String property = System.getProperty(AUTOEXPORT_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(property)) {
			return property.equalsIgnoreCase("true");
		}
		return false;
	}

	private static boolean isAutoImport() {
		String property = System.getProperty(AUTOIMPORT_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(property)) {
			return property.equalsIgnoreCase("true");
		}
		return false;
	}

	private final List<ID> processedAdvIDs = Lists.newArrayList();

	@Override
	public void advertisementDiscovered(Advertisement adv) {
		synchronized (processedAdvIDs) {
			if (processedAdvIDs.contains(adv.getID())) {
				return;
			}

			if (adv instanceof RemoveSourceAdvertisement) {

				RemoveSourceAdvertisement removeSourceAdvertisement = (RemoveSourceAdvertisement) adv;

				LOG.debug("Got a remove source advertisement");

				ImmutableList<SourceAdvertisement> importedSources = getImportedSources();
				for (SourceAdvertisement importedSourceAdv : importedSources) {
					if (removeSourceAdvertisement.getSourceAdvertisementID().equals(importedSourceAdv.getID())) {
						removeSourceImport(importedSourceAdv);
						break;
					}
				}
				processedAdvIDs.add(adv.getID());

			} else if (adv instanceof RemoveMultipleSourceAdvertisement) {

				RemoveMultipleSourceAdvertisement removeMultipleSrcAdvertisement = (RemoveMultipleSourceAdvertisement) adv;
				LOG.debug("Got a multiple remove source advertisement");

				Collection<ID> idsToRemove = removeMultipleSrcAdvertisement.getSourceAdvertisementIDs();

				ImmutableList<SourceAdvertisement> importedSources = getImportedSources();
				for (SourceAdvertisement sourceAdv : importedSources) {
					if (idsToRemove.contains(sourceAdv.getID())) {
						removeSourceImport(sourceAdv);
					}
				}

				processedAdvIDs.add(adv.getID());
			} else if (adv instanceof SourceAdvertisement) {
				SourceAdvertisement srcAdv = (SourceAdvertisement) adv;

				LOG.debug("Got source advertisement. Name is {} provided from {}", srcAdv.getName(), peerDictionary.getRemotePeerName(srcAdv.getPeerID()));

				if (!srcAdv.isLocal() && isAutoImport()) {
					LOG.debug("Do autoimport of it");
					tryAutoImportSource(srcAdv);
				}

				processedAdvIDs.add(adv.getID());
			} else if (adv instanceof MultipleSourceAdvertisement) {
				MultipleSourceAdvertisement multSrcAdv = (MultipleSourceAdvertisement) adv;

				LOG.debug("Got multiple source advertisement. It contains {} sources provided from {}", multSrcAdv.getSourceAdvertisements().size(), peerDictionary.getRemotePeerName(multSrcAdv.getPeerID()));

				if (!multSrcAdv.isLocal() && isAutoImport()) {
					LOG.debug("Autoimporting them");

					for (SourceAdvertisement srcAdv : multSrcAdv.getSourceAdvertisements()) {
						tryAutoImportSource(srcAdv);
					}
				}
				processedAdvIDs.add(adv.getID());
			}
		}
	}

	private void tryAutoImportSource(SourceAdvertisement srcAdv) {
		if (isImported(srcAdv)) {
			return;
		}

		if (getDataDictionary().containsViewOrStream(srcAdv.getName(), SessionManagementService.getActiveSession())) {
			LOG.error("Could not autoimport source '{}' since it is already used locally", srcAdv.getName());
			return;
		}

		if (!srcAdv.isLocal() && !isImported(srcAdv)) {
			try {
				importSource(srcAdv, srcAdv.getName());
				LOG.debug("Autoimporting source {}", srcAdv.getName());
			} catch (PeerException | InvalidP2PSource e) {
				LOG.error("Could not autoimport source {}", srcAdv.getName(), e);
			}
		}
	}
}
