package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroupID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

public class SubPlanManipulator {
	private static int connectionNo=0;
	private static final Logger log = LoggerFactory.getLogger(SubPlanManipulator.class);
	
	
	public void insertDummyAOs(List<SubPlan> queryParts) {
		
		for(SubPlan part : queryParts) {
			Collection<ILogicalOperator> relativeSinks = part.getSinks();
			for(ILogicalOperator sinkOfSender : relativeSinks) {
				for(LogicalSubscription sub : sinkOfSender.getSubscriptions()) {	
					if(part.contains(sub.getTarget()))
							continue;
					
					ILogicalOperator srcOfAcceptor = sub.getTarget();
					
					LogicalSubscription removingSubscription = 
							RestructHelper.determineSubscription(sinkOfSender, srcOfAcceptor);

					sinkOfSender.unsubscribeSink(removingSubscription);
					
					DummyAO sender = new DummyAO();
					sender.setOutputSchema(sinkOfSender.getOutputSchema());
					
					DummyAO receiver = new DummyAO();
					// just for the junit-test - normally it should be always set
					if(sinkOfSender.getOutputSchema()!=null) {
						receiver.setOutputSchema(sinkOfSender.getOutputSchema());
						receiver.setSchema(sinkOfSender.getOutputSchema().getAttributes());
					}
					
					sinkOfSender.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), sinkOfSender.getOutputSchema());
					sender.subscribeToSource(sinkOfSender, 0, removingSubscription.getSourceOutPort(), sinkOfSender.getOutputSchema());
					receiver.subscribeSink(srcOfAcceptor, removingSubscription.getSinkInPort(), 0, receiver.getOutputSchema());
					srcOfAcceptor.subscribeToSource(receiver, removingSubscription.getSinkInPort(), 0, receiver.getOutputSchema());
					
					if(sender.getSubscribedToSource().isEmpty() ||
							sinkOfSender.getSubscriptions().isEmpty()) {
						log.warn("DAFUQ?!");
					}
					if(receiver.getSubscriptions().isEmpty() ||
							srcOfAcceptor.getSubscribedToSource().isEmpty()) {
						log.warn("WTF...");
					}
					
					sender.connectWithDummySink(receiver);
					receiver.connectWithDummySource(sender);

					part.addOperators(sender);
					
					SubPlan receiverPart = getQueryPartToOperator(queryParts, srcOfAcceptor);					
					receiverPart.addOperators(receiver);
				}
			}
		}
	}
	
	// sfl4j / log4j funktioniert nicht korrekt im unit test (zu viele bindings)
	@SuppressWarnings("unused")
	private void log(String str, Object ...arg) {
		System.out.println(String.format(str, arg));
		// slf4j
//		str = str.replace("%s", "{}");
//		log.debug(str, arg);
	}
	
	public List<SubPlan> mergeSubPlans(List<SubPlan> queryParts) {
		Map<String, List<SubPlan>> partsOnPeer = getPartsOnPeers(queryParts);
		return _mergeSubPlans(partsOnPeer);
	}
	
	public List<SubPlan> mergeSubPlans(Map<String, List<SubPlan>> partsOnPeer) {
		return _mergeSubPlans(partsOnPeer);
	}	

	private List<SubPlan> _mergeSubPlans(Map<String, List<SubPlan>> partsOnPeer) {
		for(String peer : partsOnPeer.keySet()) {
			List<SubPlan> otherParts = Lists.newArrayList(partsOnPeer.get(peer));
			Iterator<SubPlan> i =  Lists.newArrayList(partsOnPeer.get(peer)).iterator();
			if(i.hasNext()) {
				SubPlan a = i.next();
				for(SubPlan b : otherParts) {
					if(a==b)
						continue;
					
					SubPlan c = mergeIfPossible(a,b);
					if(c==null)
						c = mergeIfPossible(b,a);
						
					if(c!=null) {
						partsOnPeer.get(peer).remove(a);
						partsOnPeer.get(peer).remove(b);
						partsOnPeer.get(peer).add(c);
						_mergeSubPlans(partsOnPeer);
						break;
					}
				}	
			}	
		}
		
		List<SubPlan> result = Lists.newArrayList();
		for(Collection<SubPlan> parts : partsOnPeer.values()) {
			for(SubPlan part : parts) {
				result.add(part);
			}
		}
		return result;
	}
	
	private SubPlan mergeIfPossible(SubPlan a, SubPlan b) {
		SubPlan subPlan = null;
		Set<ILogicalOperator> sumOperators = Sets.newHashSet();
		for(ILogicalOperator sinkOfSender : a.getDummySinks()) {
			DummyAO dummySender = ((DummyAO)sinkOfSender);
			
			DummyAO dummyReceiver = dummySender.getDummySink();
			
			if(b.getDummySources().contains(dummyReceiver)) {
				// remove dummy operators
				ILogicalOperator realSender = dummySender.getSubscribedToSource().iterator().next().getTarget();
//				realSender.unsubscribeSink(RestructHelper.determineSubscription(realSender, dummySender));
				dummySender.unsubscribeFromAllSources();
				
				ILogicalOperator realReceiver = dummyReceiver.getSubscriptions().iterator().next().getTarget();
				LogicalSubscription removeSubscription = RestructHelper.determineSubscription(dummyReceiver, realReceiver);
				realReceiver.unsubscribeFromSource(removeSubscription);
				
				realSender.subscribeSink(realReceiver, removeSubscription.getSinkInPort(),
						removeSubscription.getSourceOutPort(), removeSubscription.getSchema());				
				
				if(sumOperators.isEmpty()) {
					sumOperators.addAll(a.getAllOperators());
					sumOperators.addAll(b.getAllOperators());
				}
				sumOperators.remove(dummySender);
				sumOperators.remove(dummyReceiver);
			}					
		}
		if(!sumOperators.isEmpty())
			subPlan = new SubPlan(a.getDestinationName(), sumOperators.toArray(new ILogicalOperator[0]));
		return subPlan;
	}

	private Map<String,  List<SubPlan>> getPartsOnPeers(List<SubPlan> queryParts) {
		Map<String, List<SubPlan>> partsOnPeer = Maps.newHashMap();
		for(SubPlan part : queryParts) {
			List<SubPlan> parts = partsOnPeer.get(part.getDestinationName());
			if(parts==null) {
				parts = Lists.newArrayList();
				partsOnPeer.put(part.getDestinationName(), parts);
			}
			parts.add(part);
		}
		return partsOnPeer;
	}

	@SuppressWarnings("unchecked")
	public void insertJxtaOperators(List<SubPlan> queryParts, String sharedQueryId, PeerGroupID peerGroupId) {
//		int connectionNo = 0;
		for(SubPlan part : queryParts) {
			// alle relativen senken im teilplan
			List<DummyAO> dummys = (List<DummyAO>)(List<?>) part.getDummySinks();
			if(!dummys.isEmpty()) {
				// alle relativen senken im teilplan sind
				// theoretisch quellen für andere operatoren in anderen plänen
				for(DummyAO sinkOfSender : dummys) {
					DummyAO srcOfAcceptor = sinkOfSender.getDummySink();				
					ILogicalOperator realSinkOfSender = sinkOfSender.getSubscribedToSource().iterator().next().getTarget();
					ILogicalOperator realSrcOfAcceptor = srcOfAcceptor.getSubscriptions().iterator().next().getTarget();
					
					// dummy subscriptions löschen
					LogicalSubscription removingSubscription1 = RestructHelper.determineSubscription(realSinkOfSender, sinkOfSender);
					LogicalSubscription removingSubscription2 = RestructHelper.determineSubscription(srcOfAcceptor, realSrcOfAcceptor);
					realSinkOfSender.unsubscribeSink(removingSubscription1);
					realSrcOfAcceptor.unsubscribeFromSource(removingSubscription2);
					realSrcOfAcceptor.unsubscribeFromSource(RestructHelper.determineSubscription(srcOfAcceptor, realSrcOfAcceptor));
					
					String pipeId = "default";
					if(peerGroupId!=null) // nur fuer vereinfachte unit-tests
						pipeId = IDFactory.newPipeID(peerGroupId).toString();
					
					final JxtaReceiverAO access = new JxtaReceiverAO();
					access.setPipeID(pipeId);
					access.setOutputSchema(realSinkOfSender.getOutputSchema());
					if(realSinkOfSender.getOutputSchema()!=null) // bloß für unit-test, normalerweise immer gesetzt
						access.setSchema(realSinkOfSender.getOutputSchema().getAttributes());
//					access.setName("RCV_"+sharedQueryId+"_"+connectionNo);
					access.setName("RCV_"+connectionNo);

					final JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPipeID(pipeId);
					sender.setOutputSchema(realSinkOfSender.getOutputSchema());
//					sender.setName("SND_"+sharedQueryId+"_"+connectionNo);	
					sender.setName("SND_"+connectionNo);	
					
					realSinkOfSender.subscribeSink(sender, 0, removingSubscription1.getSourceOutPort(), sinkOfSender.getOutputSchema());
					realSrcOfAcceptor.subscribeToSource(access, removingSubscription2.getSinkInPort(), 0, access.getOutputSchema());					
					
					SubPlan receiverPart = getQueryPartToOperator(queryParts, srcOfAcceptor);				
					part.removeOperators(sinkOfSender);
					part.addOperators(sender);
					
					receiverPart.removeOperators(srcOfAcceptor);
					receiverPart.addOperators(access);		
					
					connectionNo++;
				}
			}
		}
	}
	
	private SubPlan getQueryPartToOperator(List<SubPlan> queryParts, ILogicalOperator operator) {
		for(SubPlan part : queryParts) {
			if(part.getAllOperators().contains(operator)) {
				return part;
			}			
		}
		return null;
	}
}
