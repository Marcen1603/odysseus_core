package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.peer.recovery.simplestrategy.SimpleRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.protocol.UpdateMergerSender;

/**
 * The active standby strategy uses already existing replicas of the query parts
 * to recover. <br />
 * It calls {@link SimpleRecoveryStrategy#recover(PeerID, UUID)} or
 * {@link SimpleRecoveryStrategy#recoverSingleQueryPart(PeerID, UUID, UUID)}.
 * 
 * @author Michael Brand
 *
 */
public class ActiveStandbyStrategy implements IRecoveryStrategy {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveStandbyStrategy.class);

	/**
	 * The PQL parser, if there is one bound.
	 */
	private static Optional<IQueryParser> cPQLParser = Optional.absent();

	/**
	 * Binds a PQL parser. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The PQL parser to bind. <br />
	 *            Must be not null.
	 */
	public static void bindPQLParser(IQueryParser serv) {

		Preconditions.checkNotNull(serv);

		if (serv.getLanguage().equals("PQL")) {
			cPQLParser = Optional.of(serv);
			LOG.debug("Bound {} as a PQL parser.", serv.getClass()
					.getSimpleName());
		}

	}

	/**
	 * Unbinds a PQL parser, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The PQL parser to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindPQLParser(IQueryParser serv) {

		Preconditions.checkNotNull(serv);

		if (cPQLParser.isPresent() && cPQLParser.get() == serv) {

			cPQLParser = Optional.absent();
			LOG.debug("Unbound {} as a PQL parser.", serv.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The backup information access, if there is one bound.
	 */
	private static Optional<IBackupInformationAccess> cBackupInfoAccess = Optional
			.absent();

	/**
	 * Binds a backup information access. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The backup information access to bind. <br />
	 *            Must be not null.
	 */
	public static void bindBackupInfoAccess(IBackupInformationAccess serv) {

		Preconditions.checkNotNull(serv);

		cBackupInfoAccess = Optional.of(serv);
		LOG.debug("Bound {} as a backup information access.", serv.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a backup information access, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The backup information access to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindBackupInfoAccess(IBackupInformationAccess serv) {

		Preconditions.checkNotNull(serv);

		if (cBackupInfoAccess.isPresent() && cBackupInfoAccess.get() == serv) {

			cBackupInfoAccess = Optional.absent();
			LOG.debug("Unbound {} as a backup information access.", serv
					.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery allocator, if there is one bound.
	 */
	private static Optional<IRecoveryAllocator> cRecoveryAllocator = Optional
			.absent();

	/**
	 * Binds a recovery allocator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent()
				&& cRecoveryAllocator.get().getName()
						.equals("roundrobinwithlocal")) {
			// use local as default so do nothing here
		} else {
			cRecoveryAllocator = Optional.of(serv);
			LOG.debug("Bound {} as a recovery allocator.", serv.getClass()
					.getSimpleName());
		}

	}

	/**
	 * Unbinds a recovery allocator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery allocator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryAllocator(IRecoveryAllocator serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get() == serv) {

			cRecoveryAllocator = Optional.absent();
			LOG.debug("Unbound {} as a recovery allocator.", serv.getClass()
					.getSimpleName());

		}

	}

	@Override
	public void setAllocator(IRecoveryAllocator allocator) {
		cRecoveryAllocator = Optional.of(allocator);
	}

	/**
	 * @see #getName()
	 */
	public static String getStrategyName() {
		return "activestandby";
	}

	@Override
	public String getName() {
		return getStrategyName();
	}

	private static Collection<JxtaSenderAO> findSender(ILogicalQuery query) {
		Collection<JxtaSenderAO> foundSenders = Lists.newArrayList();
		Collection<ILogicalOperator> operators = LogicalQueryHelper
				.getAllOperators(query);
		for (ILogicalOperator operator : operators) {
			if (JxtaSenderAO.class.isInstance(operator)) {
				foundSenders.add((JxtaSenderAO) operator);
			}
		}
		return foundSenders;
	}

	private static PipeID toPipeID(String str) {
		try {
			return PipeID.create(new URI(str));
		} catch (URISyntaxException e) {
			LOG.error("Invalid pipe id: " + str, e);
			return null;
		}
	}

	private static PeerID toPeerID(String str) {
		try {
			return PeerID.create(new URI(str));
		} catch (URISyntaxException e) {
			LOG.error("Invalid peer id: " + str, e);
			return null;
		}
	}

	@Override
	public void recover(PeerID failedPeer, UUID recoveryStateIdentifier) {
		if (!cPQLParser.isPresent()) {
			LOG.error("No PQL parser bound");
			return;
		} else if (!cBackupInfoAccess.isPresent()) {
			LOG.error("No backup information access bound");
			return;
		}
		ISession session = UserManagementProvider.getSessionmanagement()
				.loginSuperUser(null,
						UserManagementProvider.getDefaultTenant().getName());
		IDataDictionary datadict = DataDictionaryProvider
				.getDataDictionary(UserManagementProvider.getDefaultTenant());

		// Update recovery mergers
		Map<Integer, BackupInfo> infoMap = cBackupInfoAccess.get()
				.getBackupInformation(failedPeer.toString());
		for (int queryId : infoMap.keySet()) {
			String pql = infoMap.get(queryId).pql;
			List<IExecutorCommand> cmds = cPQLParser.get().parse(pql, session,
					datadict, Context.empty());
			for (IExecutorCommand cmd : cmds) {
				if (CreateQueryCommand.class.isInstance(cmd)) {
					Collection<JxtaSenderAO> senders = findSender(((CreateQueryCommand) cmd)
							.getQuery());
					for (JxtaSenderAO sender : senders) {
						String pipeId = sender.getPipeID();
						String peerId = sender.getPeerID();
						
						// One or both strings may be null.
						if(pipeId == null || peerId == null) {
							LOG.warn("Could not update sender {}, since pipeId is null!", sender);
							continue;
						}
						
						UpdateMergerSender.getInstance().sendInstruction(
								toPeerID(sender.getPeerID()),
								toPipeID(sender.getPipeID()));
					}
				}
			}
		}

		new SimpleRecoveryStrategy().recover(failedPeer,
				recoveryStateIdentifier);
	}

	@Override
	public void recoverSingleQueryPart(PeerID failedPeer,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier) {
		new SimpleRecoveryStrategy().recoverSingleQueryPart(failedPeer,
				recoveryStateIdentifier, recoverySubStateIdentifier);
	}

}
