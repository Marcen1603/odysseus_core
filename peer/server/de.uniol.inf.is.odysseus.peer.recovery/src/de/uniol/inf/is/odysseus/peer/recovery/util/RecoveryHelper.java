package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
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

	private static Optional<IPQLGenerator> cPQLGenerator = Optional.absent();
	private static Optional<IQueryPartController> cQueryPartController = Optional.absent();

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

	public static void bindPQLGenerator(IPQLGenerator generator) {
		cPQLGenerator = Optional.of(generator);
	}

	public static void unbindPQLGenerator(IPQLGenerator generator) {
		if (cPQLGenerator.isPresent() && cPQLGenerator.get().equals(generator)) {
			cPQLGenerator = Optional.absent();
		}
	}

	public static void bindQueryPartController(IQueryPartController controller) {
		cQueryPartController = Optional.of(controller);
	}

	public static void unbindQueryPartController(IQueryPartController controller) {
		if (cQueryPartController.isPresent() && cQueryPartController.get().equals(controller)) {
			cQueryPartController = Optional.absent();
		}
	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);

		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {

			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Installs and executes a query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 * @param sharedQueryId
	 *            The id of the shared query where this PQL belongs to. To save, that this is a shared query.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(String pql, QueryState queryState) throws QueryParseException {

		Collection<Integer> installedQueries = Lists.newArrayList();

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return installedQueries;
		}

		ISession session = RecoveryCommunicator.getActiveSession();

		installedQueries = cExecutor.get().addQuery(pql, "PQL", session, Context.empty());
		for(int query : installedQueries) {
			switch(queryState) {
				case RUNNING:
					cExecutor.get().startQuery(query, session);
					break;
				case SUSPENDED:
					cExecutor.get().suspendQuery(query, session);
					break;
				default:
					// PARTIAL_SUSPENDED:
					// PARTIAL:
					// nothing to do
			}
		}

		// TODO send success message

		return installedQueries;
	}

	public static String getPQLFromRunningQuery(int queryId) {
		Preconditions.checkArgument(cPQLGenerator.isPresent());

		ILogicalQuery query = cExecutor.get().getLogicalQueryById(queryId, RecoveryCommunicator.getActiveSession());
		String pql = cPQLGenerator.get().generatePQLStatement(query.getLogicalPlan());

		return pql;
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

	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID. Copied from LoadBalancingHelper
	 * 
	 * @param lookForSender
	 *            true if we look for a sender, false if we look for receiver.
	 * @param pipeID
	 *            Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender, String pipeID) {

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return null;
		}

		for (IPhysicalQuery query : cExecutor.get().getExecutionPlan().getQueries()) {
			for (IPhysicalOperator operator : query.getAllOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO<?> sender = (JxtaSenderPO<?>) operator;
						if (sender.getPipeIDString().equals(pipeID)) {
							return sender;
						}
					}
				} else {
					if (operator instanceof JxtaReceiverPO) {
						JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) operator;
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

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return parts;
		}

		IServerExecutor executor = cExecutor.get();
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

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return senders;
		}

		Iterator<IPhysicalQuery> queryIterator = cExecutor.get().getExecutionPlan().getQueries().iterator();
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
	public static List<RecoveryBufferPO<?>> getRecoveryBuffers() {
		List<RecoveryBufferPO<?>> buffers = new ArrayList<RecoveryBufferPO<?>>();

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return buffers;
		}

		Iterator<IPhysicalQuery> queryIterator = cExecutor.get().getExecutionPlan().getQueries().iterator();
		// Iterate through all queries we have installed
		while (queryIterator.hasNext()) {
			IPhysicalQuery query = queryIterator.next();
			Set<IPhysicalOperator> ops = query.getAllOperators();

			// Get the roots for each installed query (there we can find the
			// JxtaSenders)
			for (IPhysicalOperator op : ops) {
				if (op instanceof RecoveryBufferPO) {
					RecoveryBufferPO<?> sender = (RecoveryBufferPO<?>) op;
					buffers.add(sender);
				}
			}
		}

		return buffers;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<ControllablePhysicalSubscription> getSubscriptions(PipeID pipeId) {
		List<ControllablePhysicalSubscription> subscriptions = new ArrayList<ControllablePhysicalSubscription>();

		if (!cExecutor.isPresent()) {

			LOG.error("No executor bound!");
			return subscriptions;

		}

		Iterator<IPhysicalQuery> queryIterator = cExecutor.get().getExecutionPlan().getQueries().iterator();
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

	/**
	 * 
	 * @param pipeId
	 * @return The buffer which is before the sender with the given pipeId. Null, if there is no such buffer.
	 */
	public static RecoveryBufferPO<?> getRecoveryBuffer(PipeID pipeId) {
		for (RecoveryBufferPO<?> buffer : getRecoveryBuffers()) {
			if (buffer.getPipeId().equals(pipeId))
				return buffer;
		}
		return null;
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

	/**
	 * Suspends or resumes the subscription to a sink.
	 * 
	 * @param sink
	 *            The sink where the subscription goes to which has to be suspended
	 * @param suspend
	 *            true, if you want to suspend, false, if you want to resume
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void suspendOrResumeSink(ISink sink, boolean suspend) throws Exception {
		Collection<AbstractPhysicalSubscription> subscriptions = sink.getSubscribedToSource();
		for (AbstractPhysicalSubscription subscription : subscriptions) {
			ISource test = ((ISource) subscription.getTarget());
			Collection<AbstractPhysicalSubscription> col = test.getSubscriptions();
			for (AbstractPhysicalSubscription sub : col) {
				if (sub instanceof ControllablePhysicalSubscription) {
					ControllablePhysicalSubscription controlSub = (ControllablePhysicalSubscription) sub;
					if (suspend && !controlSub.isSuspended()) {
						controlSub.suspend();
					} else if (!suspend && controlSub.isSuspended()) {
						controlSub.resume();
					}
				} else {
					// TODO Errorhandling
					throw new Exception("Error on suspendOrResumeSink"); // XXX
																			// Workaround
																			// till
																			// real
																			// error
																			// handling
				}
			}
		}
	}

	/**
	 * Starts to buffer before the JxtaSenderPO with the given pipeID
	 * 
	 * @param pipeID
	 *            The pipeID to search for
	 */
	public static void startBuffering(String pipeID) throws Exception {
		IPhysicalOperator operator = getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			String errorMessage = "No Sender with PipeID " + pipeID + " found.";
			LOG.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		JxtaSenderPO<?> sender = (JxtaSenderPO<?>) operator;
		suspendOrResumeSink(sender, true);
	}

	/**
	 * Resumes the buffer and goes on to send tuples over the JxtaSender with the given pipeID
	 * 
	 * @param pipeID
	 *            The pipeID to search for
	 */
	public static void resumeFromBuffering(String pipeID) throws Exception {
		IPhysicalOperator operator = getPhysicalJxtaOperator(true, pipeID);
		if (operator == null) {
			String errorMessage = "No Sender with PipeID " + pipeID + " found.";
			LOG.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		JxtaSenderPO<?> sender = (JxtaSenderPO<?>) operator;
		suspendOrResumeSink(sender, false);
	}

	/**
	 * Converts a string to a pipeID
	 * 
	 * @param pipeId
	 *            The pipeID as a String
	 * @return The pipeID, if conversion is possible, null, if not
	 */
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
	 * Converts a string to a peerID
	 * 
	 * @param peerId
	 *            The peerID as a string
	 * @return The peerID, if conversion is possible, null, if not
	 */
	public static PeerID convertToPeerId(String peerId) {
		PeerID peer = null;
		try {
			URI peerUri = new URI(peerId);
			peer = PeerID.create(peerUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return peer;
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
		List<IPhysicalQuery> physicalQueries = cExecutor
				.get()
				.getCompiler()
				.translateAndTransformQuery(pql, "PQL", RecoveryCommunicator.getActiveSession(),
						DataDictionaryProvider.getDataDictionary(RecoveryCommunicator.getActiveSession().getTenant()),
						trafoConfig, Context.empty());
		return physicalQueries;
	}

	/**
	 * Finds an installed JxtaReceiverPO by the given PipeID (searches for a JxtaReceiverPO with this pipeID)
	 * 
	 * @param pipeId
	 *            The pipeId which identifies the JxtareceiverPO
	 * @return The JxtaReceiverPO with the given pipeId or null, if such a receiver can't be found
	 */
	public static JxtaReceiverPO<?> findInstalledJxtaReceiverPO(PipeID pipeId) {
		Collection<IPhysicalQuery> queries = cExecutor.get().getExecutionPlan().getQueries();
		for (IPhysicalQuery query : queries) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (op instanceof JxtaReceiverPO) {
					JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) op;
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
		Collection<IPhysicalQuery> queries = cExecutor.get().getExecutionPlan().getQueries();
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
