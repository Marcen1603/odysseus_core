package de.uniol.inf.is.odysseus.transformation.helper.broker;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.ITransformationHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class BrokerTransformationHelper implements ITransformationHelper{
	
	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, IPipe physical) {

		Collection<ILogicalOperator> ret = replace(logical, (ISink) physical);
		ret.addAll(replace(logical, (ISource) physical));
		return ret;
	}

	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISink physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeToSource((ISource) psub.getTarget(),
					psub.getSinkInPort(), psub.getSourceOutPort(),psub.getSchema());
		}
		for (LogicalSubscription l : logical.getSubscriptions()) {
			ILogicalOperator target = l.getTarget();
			if (target instanceof TopAO) {
				((TopAO) target).setPhysicalInputPO(physical);
			}
		}
		ret.add(logical);
		return ret;
	}

	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISource physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscriptions()) {
			
			// if the following operator is a broker, then do not
			// subscribe to the logical one, but to the physical one
			if(l.getTarget() instanceof BrokerAO){
				BrokerPO brokerPO = BrokerWrapperPlanFactory.getPlan(((BrokerAO)l.getTarget()).getIdentifier());
				physical.subscribeSink(brokerPO, l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
			}
			else{
				l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(),
						l.getSourceOutPort(), l.getSchema());
				
				// the target of the current subscription has only to be returned
				// if it is a logical operator. Then this logical operator has
				// to be updated by the drools engine, to be checked in the
				// next turn of rule checking.
				// If the target is a broker, then we have subscribed the current phyiscal
				// to the corresponding physical broker. However, this means that
				// there is no logical operator to be updated by the drools engine
				ret.add(l.getTarget());
			}
			
		}
		return ret;
	}
	
	/**
	 * Inserts a new operator between a physical and a logical operator.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ILogicalOperator> insertNewFather(ISource oldFather, Collection<ILogicalOperator> children, IPipe newFather){
		Collection<ILogicalOperator> modifiedChildren = new ArrayList<ILogicalOperator>();
		
		// for every child, remove the connection between
		// its old father and add it to its new father.
		for(ILogicalOperator child : children){
			for(Subscription<ISource<?>> subscription : child.getPhysSubscriptionsTo()){
				// if the following is true, we found the correct subscription
				if(subscription.getTarget() == oldFather){
					child.setPhysSubscriptionTo(newFather, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
					modifiedChildren.add(child);
				}
			}
		}
		
		// then add the new father as child to the old father
		oldFather.subscribeSink(newFather, 0, 0, oldFather.getOutputSchema());
		
		// return the modified children to update the drools working memory
		return modifiedChildren;
	}
	
	/**
	 * Inserts a new operator into a completely transformed physical query plan.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ISink> insertNewFatherPhysical(ISource oldFather, Collection<ISubscription<ISink>> children, IPipe newFather){
		Collection<ISink> modifiedChildren = new ArrayList<ISink>();
		
		// for every child, remove the connection between
		// its old father and add it to its new father.
		for(ISubscription<ISink> subscription: children){
			ISink child = subscription.getTarget();
			int sinkInPort = subscription.getSinkInPort();
			int sourceOutPort = subscription.getSourceOutPort();
			SDFAttributeList schema = subscription.getSchema();
			
			oldFather.unsubscribeSink(subscription);
			newFather.subscribeSink(child, sinkInPort, sourceOutPort, schema);
			modifiedChildren.add(child);
		}
		
		// then add the new father as child to the old father
		oldFather.subscribeSink(newFather, 0, 0, oldFather.getOutputSchema());
		
		// return the modified children to update the drools working memory
		return modifiedChildren;
	}

	public boolean containsWindow(ILogicalOperator inputOp) {
		if (inputOp instanceof WindowAO) {
			return true;
		}
		int numberOfInputs = inputOp.getSubscribedToSource().size();
		if (inputOp instanceof ExistenceAO) {
			numberOfInputs = 1;// don't check subselects in existenceaos
		}
		for (int i = 0; i < numberOfInputs; ++i) {
			if (containsWindow(inputOp.getSubscribedToSource(i).getTarget())) {
				return true;
			}
		}
		return false;
	}

}
