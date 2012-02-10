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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.Subscription;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.ISerializable;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public interface ILogicalOperator extends IOwnedOperator, 
	ISubscribable<ILogicalOperator, LogicalSubscription>, ISubscriber<ILogicalOperator,LogicalSubscription>, IClone, Serializable, ISerializable{

	@Override
	public ILogicalOperator clone();
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> replaced);
	public SDFSchema getOutputSchema();
	public SDFSchema getOutputSchema(int pos);
	public SDFSchema getInputSchema(int pos);	
		
	public IPredicate<?> getPredicate();	
	public void setPredicate(IPredicate<?> predicate);

	public String getName();
	public void setName(String name);
	
	boolean isAllPhysicalInputSet();
	public void setPhysSubscriptionTo(Subscription<ISource<?>> sub);
	public void setPhysSubscriptionTo(ISource<?> op, int sinkInPort, int sourceOutPort, SDFSchema schema);
	public void clearPhysicalSubscriptions();
	public Subscription<ISource<?>> getPhysSubscriptionTo(int port);
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo();
	// Currently needed for Transformation --> we should get rid of this!
	public Collection<ISource<?>> getPhysInputPOs();
	public int getNumberOfInputs();

//	public Collection<LogicalSubscription> getSubscriptions(ILogicalOperator a);
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator a);
	public boolean isValid();
	
	
}
