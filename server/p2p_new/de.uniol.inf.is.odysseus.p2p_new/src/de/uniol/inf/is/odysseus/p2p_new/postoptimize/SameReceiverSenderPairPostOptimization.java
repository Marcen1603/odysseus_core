package de.uniol.inf.is.odysseus.p2p_new.postoptimize;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class SameReceiverSenderPairPostOptimization implements IPostOptimizationAction {

	@Override
	public void run(IPhysicalQuery query, OptimizationConfiguration parameter, IExecutionPlan execPlan) {
		
//		Set<IPhysicalOperator> physicalOperators = query.getAllOperators();
//		for( IPhysicalOperator physicalOperator : physicalOperators.toArray(new IPhysicalOperator[0]) ) {
//			if( physicalOperator instanceof JxtaReceiverPO ) {
//				JxtaReceiverPO<?> receiverPO = (JxtaReceiverPO<?>)physicalOperator;
//				PipeID pipeID = receiverPO.getPipeID();
//				
//				Optional<JxtaSenderPO> optSenderPO = findJxtaSenderPO(pipeID, execPlan);
//				if( optSenderPO.isPresent() ) {
//					connectJxtaOperators( optSenderPO.get(), receiverPO );
//				}
//			} else if( physicalOperator instanceof JxtaSenderPO ) {
//				JxtaSenderPO<?> senderPO = (JxtaSenderPO<?>)physicalOperator;
//				PipeID pipeID = senderPO.getPipeID();
//				
//				Optional<JxtaReceiverPO> optReceiverPO = findJxtaReceiverPO(pipeID, execPlan);
//				if( optReceiverPO.isPresent() ) {
//					connectJxtaOperators( senderPO, optReceiverPO.get());
//				}
//			}
//		}
	}
//
//	private static Optional<JxtaSenderPO> findJxtaSenderPO(PipeID pipeID, IExecutionPlan execPlan) {
//		Collection<IPhysicalQuery> physicalQueries = execPlan.getQueries();
//		for( IPhysicalQuery physicalQuery : physicalQueries ) {
//			for( IPhysicalOperator physicalOperator : physicalQuery.getAllOperators() ) {
//				if( physicalOperator instanceof JxtaSenderPO) {
//					JxtaSenderPO senderPO = (JxtaSenderPO)physicalOperator;
//					if( senderPO.getPipeID().equals(pipeID)) {
//						return Optional.of(senderPO);
//					}
//				}
//			}
//		}
//		return Optional.absent();
//	}
//	
//	private static Optional<JxtaReceiverPO> findJxtaReceiverPO(PipeID pipeID, IExecutionPlan execPlan) {
//		Collection<IPhysicalQuery> physicalQueries = execPlan.getQueries();
//		for( IPhysicalQuery physicalQuery : physicalQueries ) {
//			for( IPhysicalOperator physicalOperator : physicalQuery.getAllOperators() ) {
//				if( physicalOperator instanceof JxtaReceiverPO) {
//					JxtaReceiverPO receiverPO = (JxtaReceiverPO)physicalOperator;
//					if( receiverPO.getPipeID().equals(pipeID)) {
//						return Optional.of(receiverPO);
//					}
//				}
//			}
//		}
//		return Optional.absent();
//	}
//
//	@SuppressWarnings("unchecked")
//	private static void connectJxtaOperators(JxtaSenderPO jxtaSenderPO, JxtaReceiverPO receiverPO) {
//		List<PhysicalSubscription> receiverSubs = receiverPO.getSubscriptions();
//		List<PhysicalSubscription> senderSubs = jxtaSenderPO.getSubscribedToSource();
//		
//		receiverPO.unsubscribeFromAllSinks();
//		
//		for( PhysicalSubscription receiverSub : receiverSubs ) {
//			ISink sinkTarget = (ISink) receiverSub.getTarget();
//			
//			for( PhysicalSubscription senderSub : senderSubs ) {
//				ISource sourceTarget = (ISource)senderSub.getTarget();
//				
//				sinkTarget.subscribeToSource(sourceTarget,receiverSub.getSinkInPort(), senderSub.getSourceOutPort(), receiverSub.getSchema()); 
//			}
//		}
//		
//		List<IOperatorOwner> owners = receiverPO.getOwner();
//		
//		traverseToSourcesAndSetOwner( jxtaSenderPO, owners );
//	}
//
//	@SuppressWarnings("unchecked")
//	private static void traverseToSourcesAndSetOwner(ISink operator, List<IOperatorOwner> owners) {
//		List<PhysicalSubscription> subs = (List<PhysicalSubscription>) operator.getSubscribedToSource();
//		for( PhysicalSubscription sub : subs ) {
//			IPhysicalOperator physOp = (IPhysicalOperator) sub.getTarget();
//			physOp.addOwner(owners);
//			
//			if( physOp instanceof ISink ) {
//				traverseToSourcesAndSetOwner((ISink)physOp, owners);
//			}
//		}
//	}
}
