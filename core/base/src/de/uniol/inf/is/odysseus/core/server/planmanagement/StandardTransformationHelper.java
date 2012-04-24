/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformationHelper;

@SuppressWarnings({"unchecked","rawtypes"})
public class StandardTransformationHelper implements ITransformationHelper{
	@Override
	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, IPipe physical, boolean ignoreSocketSinkPort) {
		Collection<ILogicalOperator> ret = replace(logical, (ISink) physical, ignoreSocketSinkPort);
		ret.addAll(replace(logical, (ISource) physical));
		return ret;
	}

	@Override
	public Collection<ILogicalOperator> replace(ILogicalOperator logical,
			IPipe physical) {
		return replace(logical, physical, false);
	}
	
	@Override
	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISink physical, boolean ignoreSocketSinkPort) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeToSource(psub.getTarget(),
					(ignoreSocketSinkPort?-1:psub.getSinkInPort()), psub.getSourceOutPort(),psub.getSchema());
		}
//		for (LogicalSubscription l : logical.getSubscriptions()) {
//			ILogicalOperator target = l.getTarget();
//			if (target instanceof TopAO) {
//				((TopAO) target).setPhysicalInputPO(physical);
//			}
//		}
		ret.add(logical);
		return ret;
	}
	
	@Override
	public Collection<ILogicalOperator> replace(ILogicalOperator logical,
			ISink physical) {
		return replace(logical, physical, false);
	}

	@Override
	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISource physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscriptions()) {
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(),
					l.getSourceOutPort(), l.getSchema());
			ret.add(l.getTarget());
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
	@Override
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
	@Override
	public Collection<ISink> insertNewFatherPhysical(ISource oldFather, Collection<ISubscription<ISink>> children, IPipe newFather){
		Collection<ISink> modifiedChildren = new ArrayList<ISink>();
		
		// for every child, remove the connection between
		// its old father and add it to its new father.
		for(ISubscription<ISink> subscription: children){
			ISink child = subscription.getTarget();
			int sinkInPort = subscription.getSinkInPort();
			int sourceOutPort = subscription.getSourceOutPort();
			SDFSchema schema = subscription.getSchema();
			
			oldFather.unsubscribeSink(subscription);
			newFather.subscribeSink(child, sinkInPort, sourceOutPort, schema);
			modifiedChildren.add(child);
		}
		
		// then add the new father as child to the old father
		oldFather.subscribeSink(newFather, 0, 0, oldFather.getOutputSchema());
		
		// return the modified children to update the drools working memory
		return modifiedChildren;
	}

	@Override
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
