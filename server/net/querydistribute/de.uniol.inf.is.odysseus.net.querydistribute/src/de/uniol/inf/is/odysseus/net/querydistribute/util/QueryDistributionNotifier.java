package de.uniol.inf.is.odysseus.net.querydistribute.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryDistributionListener;

public class QueryDistributionNotifier {

	private static final Logger LOG = LoggerFactory.getLogger(QueryDistributionNotifier.class);
	private static final Collection<IQueryDistributionListener> LISTENERS = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindListener(IQueryDistributionListener listener) {
		LISTENERS.add(listener);

		LOG.debug("Bound {} as a query distribution listener.", listener.getClass().getSimpleName());
	}

	// called by OSGi-DS
	public static void unbindListener(IQueryDistributionListener listener) {
		if (LISTENERS.contains(listener)) {
			LISTENERS.remove(listener);
			LOG.debug("Unbound {} as a query distribution listener.", listener.getClass().getSimpleName());
		}
	}

	public static void tryNotifyBeforeDistribution(ILogicalQuery query) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.beforeDistribution(query);
			} catch (Throwable e) {
				LOG.error("Error while calling 'beforeDistribution' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterPreProcessing(ILogicalQuery query) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterPreProcessing(query);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterPreProcessing' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterPartitioning(ILogicalQuery query, Collection<ILogicalQueryPart> queryParts) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterPartitioning(query, queryParts);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterPartitioning' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterModification(ILogicalQuery query, Collection<ILogicalQueryPart> originalParts, Collection<ILogicalQueryPart> modifiedParts) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterModification(query, originalParts, modifiedParts);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterModification' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterAllocation(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterAllocation(query, allocationMap);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterAllocation' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterPostProcessing(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterPostProcessing(query, allocationMap);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterPostProcessing' of an 'IQueryDistributionListener'!", e);
			}
		}
	}

	public static void tryNotifyAfterTransmission(ILogicalQuery query, Map<ILogicalQueryPart, IOdysseusNode> allocationMap, UUID sharedQueryId) {
		for (IQueryDistributionListener listener : LISTENERS) {
			try {
				listener.afterTransmission(query, allocationMap, sharedQueryId);
			} catch (Throwable e) {
				LOG.error("Error while calling 'afterTransmission' of an 'IQueryDistributionListener'!", e);
			}
		}
	}
}