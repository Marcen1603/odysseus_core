package de.uniol.inf.is.odysseus.peer.distribute.listener;

import java.util.Collection;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryDistributionListener;

/**
 * The abstract query distribution listener implements each method of
 * {@link IQueryDistributionListener}. Those implementations are empty. <be />
 * Concrete listeners can extends this class and override the methods, which
 * they want to override.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractQueryDistributionListener implements
		IQueryDistributionListener {

	@Override
	public void beforeDistribution(ILogicalQuery query) {
		
		// Empty implementation.

	}

	@Override
	public void afterPreProcessing(ILogicalQuery query) {
		
		// Empty implementation.

	}

	@Override
	public void afterPartitioning(ILogicalQuery query,
			Collection<ILogicalQueryPart> queryParts) {
		
		// Empty implementation.

	}

	@Override
	public void afterModification(ILogicalQuery query,
			Collection<ILogicalQueryPart> originalParts,
			Collection<ILogicalQueryPart> modifiedParts) {
		
		// Empty implementation.

	}

	@Override
	public void afterAllocation(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {
		
		// Empty implementation.

	}

	@Override
	public void afterPostProcessing(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap) {
		
		// Empty implementation.
		
	}

	@Override
	public void afterTransmission(ILogicalQuery query,
			Map<ILogicalQueryPart, PeerID> allocationMap, ID sharedQueryId) {
		
		// Empty implementation.

	}

}