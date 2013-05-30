package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;

// TODO javaDoc
public class Subcluster {
	
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
	
	private Collection<PeerID> subclusterPeerIDs;
	
	public final ImmutableCollection<PeerID> getSubclusterPeerIDs() {
		
		return ImmutableList.copyOf(subclusterPeerIDs);
		
	}
	
	public void setSubclusterPeerIDs(Collection<PeerID> peerIDs) {
		
		Preconditions.checkArgument(!peerIDs.isEmpty(), "List of peer IDs must not be empty!");
		
		this.subclusterPeerIDs = peerIDs;
		
	}
	
	private Subcluster next;
	
	public Optional<Subcluster> getNext() {
		
		return Optional.fromNullable(next);
		
	}
	
	public void setNext(Subcluster next) {
		
		Preconditions.checkArgument(next.getSubclusterPeerIDs().size() != subclusterPeerIDs.size(), 
				"Following subcluster must have the same size!");
		
		this.next = next;
		
	}
	
	public Subcluster(QueryPart part) {
		
		Preconditions.checkNotNull(part, "Query part must not be null!");
		
		this.subclusterID = SUBCLUSTER_ID_COUNTER++;
		List<ILogicalOperator> operators = Lists.newArrayList();
		RestructHelper.collectOperators(part.getOperators().iterator().next(), operators);
		RestructHelper.removeTopAOs(operators);
		if(part.getDestinationName().isPresent())
			this.queryPart = new QueryPart(operators, part.getDestinationName().get());
		else this.queryPart = new QueryPart(operators);
		this.subclusterPeerIDs = Lists.newArrayList();
		this.next = null;
		
	}

}