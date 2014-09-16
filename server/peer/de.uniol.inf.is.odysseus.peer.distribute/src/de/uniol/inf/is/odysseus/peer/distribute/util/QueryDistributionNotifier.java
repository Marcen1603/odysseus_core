package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionListener;

/**
 * The query distribution notifier is a helper class to notify
 * {@link IQueryDistributionListener}s about the current state of a dstribution
 * process. <br />
 * The helper class includes exception handling and logging.
 * 
 * @author Michael Brand
 *
 */
public class QueryDistributionNotifier {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(QueryDistributionNotifier.class);

	/**
	 * The bound listeners.
	 */
	private static Collection<IQueryDistributionListener> cListeners = Lists
			.newArrayList();

	/**
	 * Binds a new listener. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param listener
	 *            The listener to bind. <br />
	 *            Must be not null.
	 */
	public static void bindListener(IQueryDistributionListener listener) {

		Preconditions.checkNotNull(listener,
				"The listener to bind must be not null!");
		cListeners.add(listener);
		LOG.debug("Bound {} as a query distribution listener.", listener
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a listener. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param listener
	 *            The listener to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindListener(IQueryDistributionListener listener) {

		Preconditions.checkNotNull(listener,
				"The listener to bind must be not null!");
		if (cListeners.contains(listener)) {

			cListeners.remove(listener);
			LOG.debug("Unbound {} as a query distribution listener.", listener
					.getClass().getSimpleName());

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#beforeDistribution(ILogicalQuery)}. <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The query to distribute.
	 */
	public static void tryNotifyBeforeDistribution(ILogicalQuery query) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.beforeDistribution(query);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'beforeDistribution' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterPreProcessing(ILogicalQuery)}. <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 */
	public static void tryNotifyAfterPreProcessing(ILogicalQuery query) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterPreProcessing(query);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterPreProcessing' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterPartitioning(ILogicalQuery, Collection)}
	 * . <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 * @param queryParts
	 *            The resulting query parts.
	 */
	public static void tryNotifyAfterPartitioning(ILogicalQuery query,
			Collection<ILogicalQueryPart> queryParts) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterPartitioning(query, queryParts);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterPartitioning' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterModification(ILogicalQuery, Collection, Collection)}
	 * . <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 * @param originalParts
	 *            The original query parts (before modification).
	 * @param modifiedParts
	 *            The resulting query parts (after modification).
	 */
	public static void tryNotifyAfterModification(ILogicalQuery query,
			Collection<ILogicalQueryPart> originalParts,
			Collection<ILogicalQueryPart> modifiedParts) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterModification(query, originalParts, modifiedParts);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterModification' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterAllocation(ILogicalQuery, Map)}. <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 */
	public static void tryNotifyAfterAllocation(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterAllocation(query, allocationMap);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterAllocation' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterPostProcessing(ILogicalQuery, Map)}
	 * . <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 */
	public static void tryNotifyAfterPostProcessing(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterPostProcessing(query, allocationMap);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterPostProcessing' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

	/**
	 * This method tries to call
	 * {@link IQueryDistributionListener#afterTransmission(ILogicalQuery, Map, PeerID)}
	 * . <br />
	 * Errors will be caught and logged.
	 * 
	 * @param query
	 *            The processed query.
	 * @param allocationMap
	 *            The resulting allocation map.
	 * @param sharedQueryId
	 *            The shared query id of the distributed query.
	 */
	public static void tryNotifyAfterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {

		for (IQueryDistributionListener listener : cListeners) {

			try {

				listener.afterTransmission(query, allocationMap, sharedQueryId);

			} catch (Throwable e) {

				LOG.error(
						"Error while calling 'afterTransmission' of an 'IQueryDistributionListener'!",
						e);

			}

		}

	}

}