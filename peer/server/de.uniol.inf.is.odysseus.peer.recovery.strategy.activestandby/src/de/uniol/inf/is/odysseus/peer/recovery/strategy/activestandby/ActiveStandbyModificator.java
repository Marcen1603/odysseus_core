package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPreprocessorListener;

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

	/**
	 * The listeners.
	 */
	private static final Collection<IRecoveryPreprocessorListener> cListeners = Sets
			.newHashSet();

	/**
	 * Binds a listener. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param serv
	 *            The listener to bind. <br />
	 *            Must be not null.
	 */
	public static void bindListener(IRecoveryPreprocessorListener serv) {
		Preconditions.checkNotNull(serv);
		cListeners.add(serv);
		LOG.debug("Bound {} as a listener.", serv.getClass().getSimpleName());
	}

	/**
	 * Unbinds a listener. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param serv
	 *            The listener to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindListener(IRecoveryPreprocessorListener serv) {
		Preconditions.checkNotNull(serv);
		cListeners.remove(serv);
		LOG.debug("Unbound {} as a listener.", serv.getClass().getSimpleName());
	}

	/**
	 * Notifies all listeners.
	 * 
	 * @param parts
	 *            The query parts, which should be recovered by the active
	 *            standby strategy.
	 */
	private static void notifyListeners(Collection<ILogicalQueryPart> parts) {
		for (IRecoveryPreprocessorListener listener : cListeners) {
			try {
				listener.setRecoveryStrategy(
						ActiveStandbyStrategy.getStrategyName(), parts);
			} catch (Throwable t) {
				LOG.error("Error while notifying listener!", t);
			}
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

		// Notify, which parts should be recovered by active-standby
		notifyListeners(replicatedParts);

		return replicatedParts;
	}

}