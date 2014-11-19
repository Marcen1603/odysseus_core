package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.physicaloperator.RecoveryBufferPO;

/**
 * This class has some helper-methods for the recovery.
 * 
 * @author Tobias Brandt & Michael Brand
 *
 */
public class RecoveryHelper {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryHelper.class);

	/**
	 * The PQL parser, if there is one bound.
	 */
	private static Optional<IQueryParser> cPQLParser = Optional.absent();

	/**
	 * Binds a query parser. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The query parser to bind. <br />
	 *            Must be not null.
	 */
	public static void bindParser(IQueryParser serv) {

		Preconditions.checkNotNull(serv);
		if (serv.getLanguage().equals("PQL")) {

			cPQLParser = Optional.of(serv);
			LOG.debug("Bound {} as a pql parser.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Unbinds a query parser, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The query parser to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindParser(IQueryParser serv) {

		Preconditions.checkNotNull(serv);

		if (cPQLParser.isPresent() && cPQLParser.get() == serv) {

			cPQLParser = Optional.absent();
			LOG.debug("Unbound {} as a pql parser.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Installs and executes a query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(String pql) {

		Collection<Integer> installedQueries = Lists.newArrayList();

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return installedQueries;

		}

		IServerExecutor executor = RecoveryCommunicator.getExecutor().get();
		ISession session = RecoveryCommunicator.getActiveSession();

		installedQueries = executor.addQuery(pql, "PQL", session, "Standard", Context.empty());

		// TODO send success message

		return installedQueries;
	}

	/**
	 * If you need the PeerId for a given PeerName Copied from PeerConsole.
	 * 
	 * @param peerName
	 *            Name of the peer you want to have the ID from
	 * @return PeerID
	 */
	public static Optional<PeerID> determinePeerID(String peerName) {

		if (!RecoveryCommunicator.getPeerDictionary().isPresent()) {

			LOG.error("No P2P dictionary bound!");
			return Optional.absent();

		}

		for (PeerID pid : RecoveryCommunicator.getPeerDictionary().get().getRemotePeerIDs()) {
			if (RecoveryCommunicator.getPeerDictionary().get().getRemotePeerName(pid).equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

	/**
	 * If you have the peerId and want to have the name of the peer
	 * 
	 * @param peerId
	 *            Peer you want to have the name from
	 * @return Name of the peer
	 */
	public static String determinePeerName(PeerID peerId) {
		return RecoveryCommunicator.getPeerDictionary().get().getRemotePeerName(peerId);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID.
	 * Copied from LoadBalancingHelper
	 * @param lookForSender true if we look for a sender, false if we look for receiver.
	 * @param pipeID Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender, String pipeID) {

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return null;

		}

		IServerExecutor executor = RecoveryCommunicator.getExecutor().get();

		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			for (IPhysicalOperator operator : query.getAllOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;
						if (sender.getPipeIDString().equals(pipeID)) {
							return sender;
						}
					}
				} else {
					if (operator instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
						if (receiver.getPipeIDString().equals(pipeID)) {
							return receiver;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get Logical JxtaOperator (Sender or Receiver) by PipeID. Copied from LoadBalancingHelper
	 * 
	 * @param lookForSender
	 *            true if we look for a sender, false if we look for receiver.
	 * @param pipeID
	 *            Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static ILogicalOperator getLogicalJxtaOperator(boolean lookForSender, String pipeID) {

		for (ILogicalQueryPart part : getInstalledQueryParts()) {
			for (ILogicalOperator operator : part.getOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						if (sender.getPipeID().equals(pipeID)) {
							return sender;
						}

					}
				} else {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						if (receiver.getPipeID().equals(pipeID)) {
							return receiver;
						}

					}
				}

			}
		}
		return null;
	}

	/**
	 * Get all currently Installed Queries as Query Parts. Copied from LoadBalancingHelper
	 * 
	 * @return List of installed LogicalQueryParts.
	 */
	public static Collection<ILogicalQueryPart> getInstalledQueryParts() {

		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return parts;

		}

		IServerExecutor executor = RecoveryCommunicator.getExecutor().get();
		ISession session = RecoveryCommunicator.getActiveSession();

		for (int queryId : executor.getLogicalQueryIds(session)) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, session);

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}

	/**
	 * 
	 * @return All JxtaSenderPOs which can be found in the execution plan on this peer
	 */
	public static List<JxtaSenderPO<?>> getJxtaSenders() {

		List<JxtaSenderPO<?>> senders = new ArrayList<JxtaSenderPO<?>>();

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return senders;

		}

		Iterator<IPhysicalQuery> queryIterator = RecoveryCommunicator.getExecutor().get().getExecutionPlan()
				.getQueries().iterator();
		// Iterate through all queries we have installed
		while (queryIterator.hasNext()) {
			IPhysicalQuery query = queryIterator.next();
			List<IPhysicalOperator> roots = query.getRoots();

			// Get the roots for each installed query (there we can find the
			// JxtaSenders)
			for (IPhysicalOperator root : roots) {
				if (root instanceof JxtaSenderPO) {
					JxtaSenderPO<?> sender = (JxtaSenderPO<?>) root;
					senders.add(sender);
				}
			}
		}

		return senders;
	}

	/**
	 * 
	 * @return A list of all RecoveryBufferPOs which are currently in the execution plan
	 */
	@SuppressWarnings("rawtypes")
	public static List<RecoveryBufferPO> getRecoveryBuffers() {
		List<RecoveryBufferPO> buffers = new ArrayList<RecoveryBufferPO>();

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return buffers;

		}

		Iterator<IPhysicalQuery> queryIterator = RecoveryCommunicator.getExecutor().get().getExecutionPlan()
				.getQueries().iterator();
		// Iterate through all queries we have installed
		while (queryIterator.hasNext()) {
			IPhysicalQuery query = queryIterator.next();
			Set<IPhysicalOperator> ops = query.getAllOperators();

			// Get the roots for each installed query (there we can find the
			// JxtaSenders)
			for (IPhysicalOperator op : ops) {
				if (op instanceof RecoveryBufferPO) {
					RecoveryBufferPO sender = (RecoveryBufferPO) op;
					buffers.add(sender);
				}
			}
		}

		return buffers;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<ControllablePhysicalSubscription> getSubscriptions(PipeID pipeId) {
		List<ControllablePhysicalSubscription> subscriptions = new ArrayList<ControllablePhysicalSubscription>();

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return subscriptions;

		}

		Iterator<IPhysicalQuery> queryIterator = RecoveryCommunicator.getExecutor().get().getExecutionPlan()
				.getQueries().iterator();
		// Iterate through all queries we have installed
		while (queryIterator.hasNext()) {
			IPhysicalQuery query = queryIterator.next();
			Set<IPhysicalOperator> ops = query.getAllOperators();

			// Get the roots for each installed query (there we can find the
			// JxtaSenders)
			for (IPhysicalOperator op : ops) {
				if (op instanceof JxtaSenderPO) {
					JxtaSenderPO sender = (JxtaSenderPO) op;
					if (sender.getPipeIDString().equals(pipeId.toString())) {
						List<AbstractPhysicalSubscription> subs = sender.getSubscribedToSource();
						for (AbstractPhysicalSubscription sub : subs) {
							if (sub instanceof ControllablePhysicalSubscription) {
								ControllablePhysicalSubscription subscription = (ControllablePhysicalSubscription) sub;
								subscriptions.add(subscription);
							}
						}
					}
				}
			}
		}

		return subscriptions;
	}

	@SuppressWarnings("rawtypes")
	public static void resumeSubscriptions(PipeID pipeId) {
		for (ControllablePhysicalSubscription sub : getSubscriptions(pipeId)) {
			sub.resume();
		}
	}

	/**
	 * 
	 * @param pipeId
	 * @return The buffer which is before the sender with the given pipeId. Null, if there is no such buffer.
	 */
	@SuppressWarnings("rawtypes")
	public static RecoveryBufferPO getRecoveryBuffer(PipeID pipeId) {
		for (RecoveryBufferPO buffer : getRecoveryBuffers()) {
			if (buffer.getPipeId().equals(pipeId))
				return buffer;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addNewSender(JxtaSenderPO originalSender, PeerID newPeer) {

		if (!RecoveryCommunicator.getExecutor().isPresent()) {

			LOG.error("No executor bound!");
			return;

		}

		// TEST Update sender
		// Goal: install a new sender which officially sends to the new
		// receiver so that if we stop a query, it will stop on the new
		// peer, too

		// New JxtaSender which should send to the new peer
		JxtaSenderAO originalSenderAO = (JxtaSenderAO) RecoveryHelper.getLogicalJxtaOperator(true,
				originalSender.getPipeIDString());
		JxtaSenderAO jxtaSenderAO = (JxtaSenderAO) originalSenderAO.clone();
		jxtaSenderAO.setPeerID(newPeer.toString());

		JxtaSenderPO jxtaSender = null;
		try {
			jxtaSender = new JxtaSenderPO(jxtaSenderAO);
		} catch (DataTransmissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now do the subscriptions
		jxtaSender.setOutputSchema(originalSender.getOutputSchema());
		AbstractPhysicalSubscription subscription = originalSender.getSubscribedToSource(0);

		if (subscription.getTarget() instanceof AbstractPipe) {
			((AbstractPipe) subscription.getTarget()).subscribeSink(jxtaSender, 0, subscription.getSourceOutPort(),
					subscription.getSchema(), true, subscription.getOpenCalls());

			jxtaSender.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		} else if (subscription.getTarget() instanceof AbstractSource) {
			((AbstractSource) subscription.getTarget()).subscribeSink(jxtaSender, 0, subscription.getSourceOutPort(),
					subscription.getSchema(), true, subscription.getOpenCalls());

			jxtaSender.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}

		List<IPhysicalOperator> plan = new ArrayList<IPhysicalOperator>();
		plan.add(jxtaSender);
		RecoveryCommunicator.getExecutor().get().addQuery(plan, RecoveryCommunicator.getActiveSession(), "Standard");

	}

	/**
	 * Inserts an operator between a sink and its sources on a specific port. Copied from LoadBalancingHelper.
	 * 
	 * @param sink
	 *            Operator after operator to insert
	 * @param operatorToInsert
	 *            Operator to insert before sink
	 * @param port
	 *            input port of sink
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertOperatorBefore(ISink sink, AbstractPipe operatorToInsert, int port) {

		AbstractPhysicalSubscription subscription = (AbstractPhysicalSubscription) sink.getSubscribedToSource(port);
		ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();

		sink.unsubscribeFromSource(subscription);
		operatorToInsert.subscribeToSource(subscription.getTarget(), subscription.getSinkInPort(),
				subscription.getSourceOutPort(), subscription.getSchema());
		operatorToInsert.subscribeSink(sink, subscription.getSinkInPort(), subscription.getSinkInPort(),
				subscription.getSchema(), true, subscription.getOpenCalls());
		operatorToInsert.setOutputSchema(subscription.getSchema());
		operatorToInsert.addOwner(sink.getOwner());
		operatorToInsert.open(sink, subscription.getSinkInPort(), subscription.getSinkInPort(), emptyCallPath,
				sink.getOwner());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void suspendSink(ISink sink) {
		Collection<AbstractPhysicalSubscription> subscriptions = sink.getSubscribedToSource();
		for (AbstractPhysicalSubscription subscription : subscriptions) {
			if (subscription instanceof ControllablePhysicalSubscription) {
				ControllablePhysicalSubscription sub = (ControllablePhysicalSubscription) subscription;
				sub.suspend();
			} else {
				// TODO Errorhandling
			}
		}
	}

	/***
	 * 
	 * 
	 * @param pipeID
	 *            PipeID of JxtaSender
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void startBuffering(String pipeID) {
		IPhysicalOperator operator = getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			LOG.error("No Sender with PipeID " + pipeID + " found.");
			// TODO Error
		}
		JxtaSenderPO sender = (JxtaSenderPO) operator;
		suspendSink(sender);
	}

	public static PipeID convertToPipeId(String pipeId) {
		PipeID pipe = null;
		try {
			URI uri = new URI(pipeId);
			pipe = PipeID.create(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return pipe;
	}

	/**
	 * Converts a given PQL-String to logical queries
	 * 
	 * @param pql
	 *            The given PQL String. <br />
	 *            Must be not null.
	 * @return The resulting logical queries.
	 */
	public static List<ILogicalQuery> convertToLogicalQueries(String pql) {

		Preconditions.checkNotNull(pql);

		if (!cPQLParser.isPresent()) {

			LOG.error("No PQL parser bound!");
			return Lists.newArrayList();

		}

		List<IExecutorCommand> cmds = Lists.newArrayList();
		List<ILogicalQuery> queries = Lists.newArrayList();

		try {

			cmds = cPQLParser.get().parse(pql, RecoveryCommunicator.getActiveSession(),
					DataDictionaryProvider.getDataDictionary(RecoveryCommunicator.getActiveSession().getTenant()),
					Context.empty());

		} catch (QueryParseException e) {

			LOG.error("Could not parse {}", pql, e);
			return queries;

		}

		for (IExecutorCommand cmd : cmds) {

			if (!(cmd instanceof CreateQueryCommand)) {

				LOG.error("PQL parser did return an executor command, which is not a create query command!");
				continue;

			}

			CreateQueryCommand createCmd = (CreateQueryCommand) cmd;
			queries.add(createCmd.getQuery());

		}

		return queries;

	}

	/**
	 * This method is deprecated. Try to use {@link #convertToLogicalQueries(String)} if you just need the operators and
	 * not exact the physical ones. <br />
	 * 
	 * Converts a PQL-String to a physical query.
	 * 
	 * @param pql
	 *            The PQL String you want to have as a physical query
	 * @return A list of physical queries
	 */
	@Deprecated
	public static List<IPhysicalQuery> convertToPhysicalPlan(String pql) {

		TransformationConfiguration trafoConfig = new TransformationConfiguration(ITimeInterval.class.getName());
		trafoConfig.setVirtualTransformation(true);
		List<IPhysicalQuery> physicalQueries = RecoveryCommunicator
				.getExecutor()
				.get()
				.getCompiler()
				.translateAndTransformQuery(pql, "PQL", RecoveryCommunicator.getActiveSession(),
						DataDictionaryProvider.getDataDictionary(RecoveryCommunicator.getActiveSession().getTenant()),
						trafoConfig, Context.empty());
		return physicalQueries;
	}

	/**
	 * Updates the local backup-information with the given new backup-PQL and prepares a list with information for all
	 * the other peer which may have interest in this information
	 * 
	 * @param newBackupPQL
	 * @param pipeId
	 * @param sharedQueryId
	 * @return
	 */
	public static List<IRecoveryBackupInformation> updateLocalPQL(String newBackupPQL, PipeID pipeId, ID sharedQueryId) {
		List<IRecoveryBackupInformation> infosToDistribute = new ArrayList<IRecoveryBackupInformation>();

		ImmutableCollection<String> localPQLs = LocalBackupInformationAccess.getLocalPQL(sharedQueryId);
		sharedQueryId.toString();
		for (String localPQL : localPQLs) {
			for (ILogicalQuery logicalQuery : RecoveryHelper.convertToLogicalQueries(localPQL)) {
				for (ILogicalOperator logicalOp : LogicalQueryHelper.getAllOperators(logicalQuery.getLogicalPlan())) {
					if (logicalOp instanceof JxtaReceiverAO) {
						if (((JxtaReceiverAO) logicalOp).getPipeID().equals(pipeId.toString())) {
							// This is the information we search for
							IRecoveryBackupInformation updatedInfo = LocalBackupInformationAccess.updateLocalPQL(
									sharedQueryId, localPQL, newBackupPQL);

							// Distribute to other peers
							IRecoveryBackupInformation infoToDistribute = new BackupInformation();
							infoToDistribute.setSharedQuery(updatedInfo.getSharedQuery());
							infoToDistribute.setAboutPeer(RecoveryCommunicator.getP2PNetworkManager().get()
									.getLocalPeerID());
							infoToDistribute.setPQL(updatedInfo.getLocalPQL());

							infosToDistribute.add(infoToDistribute);

						}
					}
				}
			}
		}
		return infosToDistribute;
	}

	/**
	 * Updates the local backup-information with the given new backup-PQL and prepares a list with information for all
	 * the other peer which may have interest in this information. In opposite to the same method with the sharedQueryId
	 * this method searches within the whole backup-information, hence it's less efficient
	 * 
	 * @param newBackupPQL
	 * @param pipeId
	 * @return
	 */
	public static List<IRecoveryBackupInformation> updateLocalPQL(String newBackupPQL, PipeID pipeId) {
		List<IRecoveryBackupInformation> infosToDistribute = new ArrayList<IRecoveryBackupInformation>();
		for (ID sharedQueryId : LocalBackupInformationAccess.getStoredIDs()) {
			infosToDistribute.addAll(updateLocalPQL(newBackupPQL, pipeId, sharedQueryId));
		}
		return infosToDistribute;
	}

	/**
	 * Finds an installed JxtaReceiverPO by the given PipeID (searches for a JxtaReceiverPO with this pipeID)
	 * 
	 * @param pipeId
	 *            The pipeId which identifies the JxtareceiverPO
	 * @return The JxtaReceiverPO with the given pipeId or null, if such a receiver can't be found
	 */
	@SuppressWarnings("rawtypes")
	public static JxtaReceiverPO findInstalledJxtaReceiverPO(PipeID pipeId) {
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor().get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO receiver = (JxtaReceiverPO) op;
					if (receiver.getPipeIDString().equals(pipeId.toString())) {
						return receiver;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Searches for an installed query with an JxtaReceiverPO or JxtaSenderPO which has this pipeId
	 * 
	 * @param pipeId
	 *            The pipeId to search for in the JxtaReceiverPOs
	 * @param searchForReceiver
	 *            If true, this method will search for a receiver with the given pipeId, if false, for a sender
	 * @return The query with an JxtaReceicerPO with the given pipeId
	 */
	@SuppressWarnings("rawtypes")
	public static IPhysicalQuery findInstalledQueryWithJxtaOperator(PipeID pipeId, boolean searchForReceiver) {
		Collection<IPhysicalQuery> queries = RecoveryCommunicator.getExecutor().get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (searchForReceiver) {
					if (op instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) op;
						if (receiver.getPipeIDString().equals(pipeId.toString())) {
							return query;
						}
					}
				} else {
					if (op instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) op;
						if (sender.getPipeIDString().equals(pipeId.toString())) {
							return query;
						}
					}
				}

			}
		}
		return null;
	}

	/**
	 * Returns a JxtaReceiverPO from a given query.
	 * 
	 * @param query
	 *            The query where the JxtaReceiverPO is in
	 * @return The JxtaReceiverPO or null, if no one was found
	 */
	@SuppressWarnings("rawtypes")
	public static JxtaReceiverPO findJxtaReceiverPO(PhysicalQuery query) {
		for (IPhysicalOperator op : query.getAllOperators()) {
			if (op instanceof JxtaReceiverPO) {
				return (JxtaReceiverPO) op;
			}
		}
		return null;
	}

	/**
	 * Returns a JxtaSenderPO from a given query.
	 * 
	 * @param query
	 *            The query where the JxtaSenderPO is in
	 * @return The JxtaSenderPO or null, if no one was found
	 */
	@SuppressWarnings("rawtypes")
	public static JxtaSenderPO findJxtaSenderPO(PhysicalQuery query) {
		for (IPhysicalOperator op : query.getAllOperators()) {
			if (op instanceof JxtaSenderPO) {
				return (JxtaSenderPO) op;
			}
		}
		return null;
	}
}
