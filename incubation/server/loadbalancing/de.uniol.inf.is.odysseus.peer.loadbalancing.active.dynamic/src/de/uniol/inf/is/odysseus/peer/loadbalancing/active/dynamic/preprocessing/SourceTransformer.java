package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

public class SourceTransformer {
	

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SourceTransformer.class);

	
	@SuppressWarnings({ "rawtypes"})
	public static void replaceSources(int queryID, PeerID localPeerID, ISession session, IP2PNetworkManager networkManager, IServerExecutor executor) {
		LOG.debug("Replacing Sources for Query {}",queryID);
		List<Pair<ISource,ISink>> connections = Lists.newArrayList();
		Collection<IPhysicalOperator> operatorsInQuery = executor.getExecutionPlan().getQueryById(queryID).getAllOperators();
		
		for(IPhysicalOperator op : operatorsInQuery) {
			if( !(op instanceof ISink) && (op instanceof ISource) && !(op instanceof JxtaReceiverPO) ) {
				LOG.debug("Operator {} seems to be a real source.",op.getName());
				ISource source = (ISource)op;
				for(Object obj : source.getSubscriptions()) {
					if(obj instanceof ISubscription) {
						ISubscription subscr = (ISubscription)obj;
						if(operatorsInQuery.contains(subscr.getTarget())) {
							connections.add(new Pair<ISource,ISink>(source,(ISink)subscr.getTarget()));
						}
					}
				}
			}
		}
		
		for(Pair<ISource,ISink> connection : connections) {
			PipeID newPipe = IDFactory.newPipeID(networkManager.getLocalPeerGroupID());
			insertJxtaConnectionAfterSource(connection.getE1(),connection.getE2(),localPeerID,newPipe,queryID,executor);
		}
		
		
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void insertJxtaConnectionAfterSource(ISource sourceOperator, ISink firstOperatorAfterSource,PeerID peerID, PipeID pipeID, int queryID, IServerExecutor executor) {
		
		List<ControllablePhysicalSubscription> subscriptionsToReplace = Lists.newArrayList();
		
		for (Object subscrObj : sourceOperator.getSubscriptions()) {
			if(subscrObj instanceof ControllablePhysicalSubscription) {
				ControllablePhysicalSubscription subscr = (ControllablePhysicalSubscription)subscrObj;
				if(!subscr.getTarget().equals(firstOperatorAfterSource)) {
					continue;
				}
				subscriptionsToReplace.add(subscr);
			}
			else {
				//TODO Find some solution here.
				LOG.warn("Can not process non ControllablePhysicalSubscription");
			}
			
			
		}
		
		for (ControllablePhysicalSubscription subscr: subscriptionsToReplace) {
			replaceSubscriptionWithSenderAndReceiver(sourceOperator, subscr, peerID, pipeID,queryID,executor);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void replaceSubscriptionWithSenderAndReceiver(ISource source,ControllablePhysicalSubscription subscr, PeerID peerID, PipeID pipeID, int queryID, IServerExecutor executor) {
		ISink sink = (ISink) subscr.getTarget();
		ISubscription revSubscr = getReverseSubscription(source,subscr);
		IPhysicalQuery query = executor.getExecutionPlan().getQueryById(queryID);
		
		JxtaSenderAO senderAO = new JxtaSenderAO();
		senderAO.setPeerID(peerID.toString());
		senderAO.setPipeID(pipeID.toString());
		senderAO.setOutputSchema(subscr.getSchema().clone());
		JxtaSenderPO senderPO;
		
		try {
			senderPO = new JxtaSenderPO(senderAO);
			senderPO.setName("JxtaSender "+source.getName());
			senderPO.addOwner(source.getOwner());
		} catch (DataTransmissionException e) {
			LOG.error("Could not create JxtaSenderPO.");
			e.printStackTrace();
			return;
		}
		
		JxtaReceiverAO receiverAO = new JxtaReceiverAO();
		receiverAO.setPeerID(peerID.toString());
		receiverAO.setPipeID(pipeID.toString());
		receiverAO.setSchema(subscr.getSchema().clone().getAttributes());
		
		JxtaReceiverPO receiverPO;
		
		try {
			receiverPO = new JxtaReceiverPO(receiverAO);
			receiverPO.setName("JxtaReceiver "+source.getName());
			receiverPO.addOwner(source.getOwner());
		} catch (DataTransmissionException e) {
			LOG.error("Could not create JxtaSenderPO.");
			e.printStackTrace();
			return;
		}
		
		
		//TODO init Sender and Receiver
		
		subscr.suspend();
		
		//TODO What about ports? -> Perhapts add Port Route-Operator inbetween.
		

		sink.unsubscribeFromSource(revSubscr);
		receiverPO.subscribeSink(sink, subscr.getSinkInPort(), 0, subscr.getSchema().clone());
		sink.subscribeToSource(receiverPO, subscr.getSinkInPort(), 0, subscr.getSchema());
		
		senderPO.subscribeToSource(source, subscr.getSinkInPort(), subscr.getSourceOutPort(), subscr.getSchema());
		subscr.setTarget(senderPO);
		List<IOperatorOwner> ownerList = new ArrayList<IOperatorOwner>();
		ownerList.add(query);
		receiverPO.open(sink, 0, subscr.getSinkInPort(), Lists.newArrayList(),ownerList);
		subscr.resume();
		senderPO.open(query);
		
		
		query.addChild(senderPO);
		query.addChild(receiverPO);
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>(query.getRoots());
		roots.add(senderPO);
		query.setRoots(roots);
	}

	/**
	 * Converts a String to a peer ID.
	 * 
	 * @param peerIDString
	 *            String to convert
	 * @return PeerId (if it exists), null else.
	 */
	public static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static ISubscription getReverseSubscription(ISource source, ISubscription subscr) {
		ISink sink = (ISink)subscr.getTarget();
		for(Object obj : sink.getSubscribedToSource()) {
			if(obj instanceof ISubscription) {
				ISubscription revSubscr = (ISubscription)obj;
				if(revSubscr.getTarget().equals(source) && revSubscr.getSourceOutPort()==subscr.getSourceOutPort() && revSubscr.getSinkInPort()==subscr.getSinkInPort()) {
					return revSubscr;
				}
			}
		}
		return null;
		
	}
	
}
