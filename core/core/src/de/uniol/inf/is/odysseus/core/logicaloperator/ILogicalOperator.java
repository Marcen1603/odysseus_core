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
package de.uniol.inf.is.odysseus.core.logicaloperator;

import java.io.Serializable;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.ISerializable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This interface represent all logical operators. A logical operator is describes an operation
 * that should be executed over input data. A logical operator is translated to a physical operator that
 * that does the execution
 * @author Marco Grawunder
 *
 */

public interface ILogicalOperator extends IOwnedOperator, 
	ISubscribable<ILogicalOperator, LogicalSubscription>, ISubscriber<ILogicalOperator,LogicalSubscription>, IClone, Serializable, ISerializable{

	/**
	 * For debugging purposes and for visualization,
	 * each operator can have a name
	 * @return the Name of this operator
	 */
	public String getName();

	/**
	 * For debugging purposes and for visualization,
	 * each operator can have a name
	 * @param name The name of the operator to set
	 */
	public void setName(String name);

	/**
	 * How many inputs has this logical operator at call time 
	 * @return the count of currently attached sources
	 */
	public int getNumberOfInputs();
	
	/**
	 * Get the output schema of this operator, could be null if not
	 * set or not determinable (because no input operators are set) 
	 * @return The Schema
	 */
	public SDFSchema getOutputSchema();
	
	/**
	 * Get the output schema for a specific port. Most logical operators
	 * provide only a single output port
	 * @param pos Which output port (default is 0) 
	 * @return The schema
	 */
	public SDFSchema getOutputSchema(int pos);
	
	
	
	void setOutputSchema(int pos, SDFSchema outputSchema);

	void setOutputSchema(SDFSchema outputSchema);

	
	/**
	 * Deliever the input schema of this operator at a spefific input 
	 * port. Typically this is the output schema of the input operator 
	 * @param pos Which input port 
	 * @return The Schema
	 */
	public SDFSchema getInputSchema(int pos);	
		
	/**
	 * If this logical operator provides a predicate (e.g. a join or 
	 * a selection) this is delivered with this method
	 * @return the predicate, could be null
	 */
	public IPredicate<?> getPredicate();	
	
	/**
	 * Set the predicate for this logical operator
	 * @param predicate
	 */
	public void setPredicate(IPredicate<?> predicate);

	/**
	 * Find the subscription where the logical operator is the source
	 * @param logicalOperator The operator that should be found
	 * @return a collection of subscriptions
	 */
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator logicalOperator);


	/**
	 * This method can be called in scenarios where multiple input values that are set independently
	 * together form one parameter of this operator. Needed in GenericOperatorBuilder and will be call
	 * before validation and after all input sources are set
	 */
	public void initialize();
	
	/**
	 * This Method can be called to check if all needed parameters are set or
	 * the combination of parameters is valid. Needed in GenericOperatorBuilder.
	 * @return
	 */
	public boolean isValid();
		
	/**
	 * This method is used in the transformation process.
	 * @return true if for any input port with a logical operator a 
	 * physical operator (ISource) is set else false 
	 */
	boolean isAllPhysicalInputSet();
	
	/**
	 * Add a new physical subscription to an ISource
	 * @param sub the subscription to add
	 */
	public void setPhysSubscriptionTo(Subscription<IPhysicalOperator> sub);
	/**
	 * Creates a new physical subscription
	 * @param op The operator that should be registered as source
	 * @param sinkInPort The input port of this logical operator that should be used
	 * @param sourceOutPort The output port of the op to connect
	 * @param schema The data schema of the data that is transfered over this subscription
	 */
	public void setPhysSubscriptionTo(IPhysicalOperator op, int sinkInPort, int sourceOutPort, SDFSchema schema);
	
	/**
	 * Removes all subscriptions
	 */
	public void clearPhysicalSubscriptions();
	
	/**
	 * Gets the physical Subscription 
	 * @param inputPort the input port  
	 * @return the physical subscription of the inputPort
	 */
	public Subscription<IPhysicalOperator> getPhysSubscriptionTo(int inputPort);
	
	/**
	 * Get the collection of all physical subscriptions
	 * @return
	 */
	public Collection<Subscription<IPhysicalOperator>> getPhysSubscriptionsTo();
	
	// Currently needed for Transformation --> we should get rid of this!
	public Collection<IPhysicalOperator> getPhysInputPOs();
		
	/**
	 * Create a copy of this logical operator.
	 */
	@Override
	public ILogicalOperator clone();
	
	/**
	 * Human readable description of the logical operator.
	 * @return The description of the logical operator
	 */
	public String getDoc();

}
