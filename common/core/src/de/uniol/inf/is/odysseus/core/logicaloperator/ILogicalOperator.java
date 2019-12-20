/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.AnyTypeAdapter;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This interface represent all logical operators. A logical operator is
 * describes an operation that should be executed over input data. A logical
 * operator is translated to a physical operator that that does the execution
 * 
 * @author Marco Grawunder
 * 
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface ILogicalOperator extends IOwnedOperator, ISubscribable<ILogicalOperator, LogicalSubscription>, ISubscriber<ILogicalOperator, LogicalSubscription>, IClone, Serializable, IOperatorState {

	/**
	 * For debugging purposes and for visualization, each operator can have a
	 * name
	 * 
	 * @return the Name of this operator
	 */
	public String getName();

	/**
	 * For debugging purposes and for visualization, each operator can have a
	 * name
	 * 
	 * @param name
	 *            The name of the operator to set
	 */
	public void setName(String name);
	
	/**
	 * getName will return a value in every case, isNameSet can be used
	 * to determine if the name was set or if a default value is returned
	 * 
	 * @return true if the name of the logical operator is set by the user/query
	 */
	boolean isNameSet();
	
	public void setDebug(boolean debug);

	/**
	 * Name of the place to execute the logical operator (e.g. peer name). The
	 * interpretation of the destination name is implementation-dependent.
	 * 
	 * @return Name of the destination to execute the logical operator
	 */
	public String getDestinationName();

	/**
	 * Sets the place to execute the logical operator (e.g. peer name,
	 * ip-address). The interpretation of the destination name is
	 * implementation-dependent.
	 * 
	 * @param destinationName
	 *            New name of the destination for execution. Null specifies that
	 *            the operator should be executed locally.
	 */
	public void setDestinationName(String destinationName);

	/**
	 * Gets a Map of simple information that may be used by the GUI
	 * 
	 * @return a map of key-value pairs
	 */
	public Map<String, String> getParameterInfos();

	/**
	 * sets a map of key-value pairs for additional information
	 * 
	 * @param infos
	 *            a map with key-value pairs
	 */
	public void setParameterInfos(Map<String, String> infos);

	/**
	 * adds a key value pair to the information map
	 * 
	 * @param key
	 *            the name of the information
	 * @param value
	 *            the value of the information
	 */
	public void addParameterInfo(String key, Object value);
	
	/**
	 * 
	 * @param key
	 */
	public void removeParameterInfo(String key);

	/**
	 * How many inputs has this logical operator at call time
	 * 
	 * @return the count of currently attached sources
	 */
	public int getNumberOfInputs();

	/**
	 * Get the output schema of this operator, could be null if not set or not
	 * determinable (because no input operators are set)
	 * 
	 * @return The Schema
	 */
	public SDFSchema getOutputSchema();

	/**
	 * Get the output schema for a specific port. Most logical operators provide
	 * only a single output port
	 * 
	 * @param pos
	 *            Which output port (default is 0)
	 * @return The schema
	 */
	public SDFSchema getOutputSchema(int pos);
	
	public Map<Integer, SDFSchema> getOutputSchemaMap();

	void setOutputSchema(int pos, SDFSchema outputSchema);

	void setMetadata(IMetaAttribute metaattribute);
	
	void setOutputSchema(SDFSchema outputSchema);

	/**
	 * Deliever the input schema of this operator at a spefific input port.
	 * Typically this is the output schema of the input operator
	 * 
	 * @param pos
	 *            Which input port
	 * @return The Schema
	 */
	public SDFSchema getInputSchema(int pos);

	/**
	 * Find the subscription where the logical operator is the source
	 * 
	 * @param logicalOperator
	 *            The operator that should be found
	 * @return a collection of subscriptions
	 */
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator logicalOperator);

	/**
	 * This method can be called in scenarios where multiple input values that
	 * are set independently together form one parameter of this operator.
	 * Needed in GenericOperatorBuilder and will be call before validation and
	 * after all input sources are set
	 */
	public void initialize();

	/**
	 * This Method can be called to check if all needed parameters are set or
	 * the combination of parameters is valid. Needed in GenericOperatorBuilder.
	 * 
	 * @return
	 */
	public boolean isValid();

	/**
	 * This method is used in the transformation process.
	 * 
	 * @return true if for any input port with a logical operator a physical
	 *         operator (ISource) is set else false
	 */
	boolean isAllPhysicalInputSet();

	/**
	 * Add a new physical subscription to an ISource
	 * 
	 * @param sub
	 *            the subscription to add
	 */
	public void setPhysSubscriptionTo(Subscription<IPhysicalOperator, ILogicalOperator> sub);

	/**
	 * Creates a new physical subscription
	 * 
	 * @param op
	 *            The operator that should be registered as source
	 * @param sinkInPort
	 *            The input port of this logical operator that should be used
	 * @param sourceOutPort
	 *            The output port of the op to connect
	 * @param schema
	 *            The data schema of the data that is transfered over this
	 *            subscription
	 */
	public void setPhysSubscriptionTo(IPhysicalOperator op, int sinkInPort, int sourceOutPort, SDFSchema schema);

	/**
	 * Removes all subscriptions
	 */
	public void clearPhysicalSubscriptions();

	/**
	 * Gets the physical Subscription
	 * 
	 * @param inputPort
	 *            the input port
	 * @return the physical subscription of the inputPort
	 */
	public Subscription<IPhysicalOperator, ILogicalOperator> getPhysSubscriptionTo(int inputPort);

	/**
	 * Get the collection of all physical subscriptions
	 * 
	 * @return
	 */
	public Collection<Subscription<IPhysicalOperator, ILogicalOperator>> getPhysSubscriptionsTo();

	// Currently needed for Transformation --> we should get rid of this!
	public Collection<IPhysicalOperator> getPhysInputPOs();

	/**
	 * Create a copy of this logical operator.
	 */
	@Override
	public ILogicalOperator clone();

	/**
	 * Each operator can have a unique id
	 * 
	 * @param id
	 */
	void setUniqueIdentifier(String id);

	/**
	 * Each operator can have a unique id
	 * 
	 * @param id
	 */
	String getUniqueIdentifier();
	
	public boolean isSinkOperator();
	public boolean isSourceOperator();
	public boolean isPipeOperator();

	public boolean isDebug();

	boolean isSuppressPunctuations();
	
	/**
	 * Out of order processing
	 */
	
	/**
	 * Define which way the input of the source should be treated
	 * @param inputPort The port for which the input should be delivered
	 * @return the input type
	 */
	InputOrderRequirement getInputOrderRequirement(int inputPort);

	public void recalcOutputSchema();


	
}
