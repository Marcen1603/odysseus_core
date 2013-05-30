package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.SessionManagementService;

import net.jxta.discovery.DiscoveryService;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

// TODO javaDoc
public class Subcluster {
	
	private static final Logger LOG = LoggerFactory.getLogger(Subcluster.class);
	
	private static int SUBCLUSTER_ID_COUNTER = 0;
	
	private final int subclusterID;
	
	public final int getSubClusterID() {
		
		return subclusterID;
		
	}
	
	/**
	 * Returns the base name for acceptor operators.
	 */
	public String getAccessName() {
		
		return "JxtaReceiver_" + subclusterID + "_";
		
	}

	/**
	 * Returns the base name for sender operators.
	 */
	public String getSenderName() {
		
		return "JxtaSender_" + subclusterID + "_";
		
	}
	
	private final QueryPart queryPart;
	
	public final QueryPart getQueryPart() {
		
		return this.queryPart;
		
	}
	
	private final Collection<PeerID> subclusterPeerIDs;
	
	public final ImmutableCollection<PeerID> getSubclusterPeerIDs() {
		
		return ImmutableList.copyOf(subclusterPeerIDs);
		
	}
	
	private Subcluster next;
	
	public Optional<Subcluster> getNext() {
		
		return Optional.fromNullable(next);
		
	}
	
	public void setNext(Subcluster next) {
		
		this.next = next;
		
	}
	
	public Subcluster(QueryPart part, Collection<PeerID> peerIDs) {
		
		Preconditions.checkNotNull(part, "Query part must not be null!");
		Preconditions.checkArgument(!peerIDs.isEmpty(), "List of peer IDs must not be empty!");
		
		this.subclusterID = SUBCLUSTER_ID_COUNTER++;
		List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(part.getOperators().iterator().next(), operators);
		RestructHelper.removeTopAOs(operators);
		if(part.getDestinationName().isPresent())
			this.queryPart = new QueryPart(operators, part.getDestinationName().get());
		else this.queryPart = new QueryPart(operators);
		this.subclusterPeerIDs = peerIDs;
		
	}
	
	public void publishQueryPart(ID sharedQueryID, String transCfgName) {
		
		Preconditions.checkNotNull(sharedQueryID, "Shared query ID must not be null!");
		Preconditions.checkNotNull(transCfgName, "Name of the transfer configuration must not be null!");
		
		final Map<QueryPart, PeerID> queryPartDistributionMap = Maps.newHashMap();
		for(PeerID peerID : subclusterPeerIDs)
			queryPartDistributionMap.put(queryPart, peerID);
		PeerGroupID localPeerGroupID = P2PDictionaryService.get().getLocalPeerGroupID();
		
		int connectionNo = 0;
		for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
			
			final ILogicalOperator nextOperator = this.determineNextOperator(relativeSink);
			if(nextOperator != null) {
					
				DistributionHelper.generatePeerConnection(queryPart, next.getQueryPart(), relativeSink, nextOperator, 
						this.getSenderName() + connectionNo, this.getAccessName() + connectionNo, localPeerGroupID);
				connectionNo++;


			}
		}
		
		QueryPart partToPublish = DistributionHelper.replaceStreamAOs(queryPart, SessionManagementService.getActiveSession(), 
				DataDictionaryService.get());
		String pqlStatement = PQLGeneratorService.get().generatePQLStatement(partToPublish.getOperators().iterator().next());
		DiscoveryService disService = JxtaServicesProviderService.get().getDiscoveryService();
		for(PeerID peerID : subclusterPeerIDs) {
			
			DistributionHelper.publish(pqlStatement, peerID, localPeerGroupID, sharedQueryID, transCfgName, disService);
			LOG.debug("Shared part {} on peer {}", queryPart, P2PDictionaryService.get().getPeerRemoteName(peerID).get());
			
		}
		
	}
	
	private ILogicalOperator determineNextOperator(ILogicalOperator relativeSink) {
		
		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				
				final ILogicalOperator target = subscription.getTarget();
				if(next.getQueryPart().getOperators().contains(target))
					return target;
				
			}
			
		}
		
		return null;
		
	}

}