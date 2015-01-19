package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.logicaloperator.RecoveryMergeAO;

/**
 * Replicates the given query parts once.
 * 
 * @author Michael Brand
 *
 */
public class ActiveStandbyModificator implements IQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveStandbyModificator.class);

	/**
	 * The replication modifier, if there is one bound.
	 */
	private static Optional<IQueryPartModificator> cReplicator = Optional
			.absent();

	/**
	 * Binds a query part modifier. <br />
	 * Stores <code>serv</code> only, if it's the replication modifier. Called
	 * by OSGI-DS.
	 * 
	 * @param serv
	 *            The query part modifier to bind. <br />
	 *            Must be not null.
	 */
	public static void bindModifier(IQueryPartModificator serv) {
		Preconditions.checkNotNull(serv);
		if (serv.getName().equals("replication")) {
			cReplicator = Optional.of(serv);
			LOG.debug("Bound {} as a replication modifier.", serv.getClass()
					.getSimpleName());
		}
	}

	/**
	 * Unbinds a query part modifier. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param serv
	 *            The query part modifier to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindModifier(IQueryPartModificator serv) {
		Preconditions.checkNotNull(serv);
		if (cReplicator.isPresent() && cReplicator.get() == serv) {
			cReplicator = Optional.absent();
			LOG.debug("Unbound {} as a replication modifier.", serv.getClass()
					.getSimpleName());
		}
	}

	@Override
	public String getName() {
		return "recovery_activestandby";
	}

	@Override
	public Collection<ILogicalQueryPart> modify(
			Collection<ILogicalQueryPart> queryParts, ILogicalQuery query,
			QueryBuildConfiguration config, List<String> modificatorParameters)
			throws QueryPartModificationException {
		if (!cReplicator.isPresent()) {
			LOG.error("No replication modifier bound!");
			return queryParts;
		}

		// Make one replicate each
		List<String> modParamsForReplication = Lists.newArrayList("2");
		Collection<ILogicalQueryPart> replicatedParts = cReplicator.get()
				.modify(queryParts, query, config, modParamsForReplication);

		// Replace replication merge operators with recovery merge operators
		return replaceMergers(replicatedParts);
	}

	private static Collection<ILogicalQueryPart> replaceMergers(
			Collection<ILogicalQueryPart> parts) {
		Map<ILogicalQueryPart, ILogicalQueryPart> copyToOriginal = copyQueryPartsDeep(parts);
		Collection<ILogicalQueryPart> modifiedParts = Lists.newArrayList();
		for (ILogicalQueryPart original : copyToOriginal.keySet()) {
			modifiedParts.add(replaceMergers(original, copyToOriginal));
		}
		return modifiedParts;
	}

	private static ILogicalQueryPart replaceMergers(
			ILogicalQueryPart originalQP,
			Map<ILogicalQueryPart, ILogicalQueryPart> copyToOriginal) {
		ILogicalQueryPart copyQP = copyToOriginal.get(originalQP);
		for (ILogicalOperator originalOp : originalQP.getOperators()) {
			if (ReplicationMergeAO.class.isInstance(originalOp)) {
				ReplicationMergeAO originalMerge = (ReplicationMergeAO) originalOp;
				ReplicationMergeAO copyMerge = (ReplicationMergeAO) LogicalQueryHelper
						.collectCopies(originalQP, Lists.newArrayList(copyQP),
								originalMerge).iterator().next();
				RecoveryMergeAO mergerToInsert = new RecoveryMergeAO();

				copyMerge.unsubscribeFromAllSources();
				copyMerge.unsubscribeFromAllSinks();
				copyQP.removeOperator(copyMerge);

				copySubscriptions(mergerToInsert,
						originalMerge.getSubscribedToSource(), copyToOriginal,
						true);
				copySubscriptions(mergerToInsert,
						originalMerge.getSubscriptions(), copyToOriginal, false);
				copyQP.addOperator(mergerToInsert);
			}
		}

		return copyQP;
	}

	private static void copySubscriptions(ILogicalOperator operator,
			Collection<LogicalSubscription> subscriptions,
			Map<ILogicalQueryPart, ILogicalQueryPart> copyToOriginal,
			boolean toSource) {
		for (LogicalSubscription sub : subscriptions) {
			Optional<ILogicalQueryPart> partOfOriginalTarget = LogicalQueryHelper
					.determineQueryPart(copyToOriginal.keySet(),
							sub.getTarget());
			Preconditions.checkArgument(partOfOriginalTarget.isPresent());
			
			ILogicalOperator copyTarget = LogicalQueryHelper
					.collectCopies(
							partOfOriginalTarget.get(),
							Lists.newArrayList(copyToOriginal
									.get(partOfOriginalTarget.get())),
							sub.getTarget()).iterator().next();
			int sinkInPort = sub.getSinkInPort();
			int sourceOutPort = sub.getSourceOutPort();
			SDFSchema schema = sub.getSchema();
			if (toSource) {
				operator.subscribeToSource(copyTarget, sinkInPort,
						sourceOutPort, schema);
			} else {
				operator.subscribeSink(copyTarget, sinkInPort, sourceOutPort,
						schema);
			}
		}
	}

	private static Map<ILogicalQueryPart, ILogicalQueryPart> copyQueryPartsDeep(
			Collection<ILogicalQueryPart> queryParts) {
		Map<ILogicalQueryPart, ILogicalQueryPart> originalToCopy = LogicalQueryHelper
				.copyQueryPartsDeep(queryParts);
		Map<ILogicalQueryPart, ILogicalQueryPart> copyToOriginal = Maps
				.newHashMap();
		for (ILogicalQueryPart copy : originalToCopy.keySet()) {
			copyToOriginal.put(originalToCopy.get(copy), copy);
		}
		return copyToOriginal;
	}

}