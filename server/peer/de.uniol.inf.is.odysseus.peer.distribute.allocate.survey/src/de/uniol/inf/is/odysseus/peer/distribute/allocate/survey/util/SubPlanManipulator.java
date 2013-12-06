package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DummyAO;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;

public class SubPlanManipulator {
	
	private static final Logger log = LoggerFactory.getLogger(SubPlanManipulator.class);
	
	public static void insertDummyAOs(Collection<SubPlan> queryParts) {
		
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

	private static SubPlan getQueryPartToOperator(Collection<SubPlan> queryParts, ILogicalOperator operator) {
		for(SubPlan part : queryParts) {
			if(part.getAllOperators().contains(operator)) {
				return part;
			}			
		}
		return null;
	}
}
