package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;

public class SubPlanManipulator {
	
	private static final Logger log = LoggerFactory.getLogger(SubPlanManipulator.class);
	
	public static void insertDummyAOs(List<SubPlan> queryParts) {
		
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
	
	public static List<SubPlan> mergeSubPlans(List<SubPlan> queryParts) {
		Map<String, List<SubPlan>> partsOnPeer = getPartsOnPeers(queryParts);
		return _mergeSubPlans(partsOnPeer);
	}
	
	public static List<SubPlan> mergeSubPlans(Map<String, List<SubPlan>> partsOnPeer) {
		return _mergeSubPlans(partsOnPeer);
	}	

	private static List<SubPlan> _mergeSubPlans(Map<String, List<SubPlan>> partsOnPeer) {
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
				if( !result.contains(part)) {
					result.add(part);
				}
			}
		}
		return result;
	}
	
	private static SubPlan mergeIfPossible(SubPlan a, SubPlan b) {
		SubPlan subPlan = null;
		Set<ILogicalOperator> sumOperators = Sets.newHashSet();
		for(ILogicalOperator sinkOfSender : a.getDummySinks()) {
			DummyAO dummySender = ((DummyAO)sinkOfSender);
			
			DummyAO dummyReceiver = dummySender.getDummySink();
			
			if(b.getDummySources().contains(dummyReceiver)) {
				ILogicalOperator realSender = dummySender.getSubscribedToSource().iterator().next().getTarget();
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
		if(!sumOperators.isEmpty()) {
			subPlan = new SubPlan(a.getDestinationNames(), sumOperators.toArray(new ILogicalOperator[0]));
		}
		return subPlan;
	}

	private static Map<String,  List<SubPlan>> getPartsOnPeers(List<SubPlan> queryParts) {
		Map<String, List<SubPlan>> partsOnPeer = Maps.newHashMap();
		for(SubPlan part : queryParts) {
			for( String destinationName : part.getDestinationNames() ) {
				List<SubPlan> parts = partsOnPeer.get(destinationName);
				
				if(parts==null) {
					parts = Lists.newArrayList();
					partsOnPeer.put(destinationName, parts);
				}
				
				parts.add(part);
			}
		}
		return partsOnPeer;
	}

	private static SubPlan getQueryPartToOperator(List<SubPlan> queryParts, ILogicalOperator operator) {
		for(SubPlan part : queryParts) {
			if(part.getAllOperators().contains(operator)) {
				return part;
			}			
		}
		return null;
	}
}
