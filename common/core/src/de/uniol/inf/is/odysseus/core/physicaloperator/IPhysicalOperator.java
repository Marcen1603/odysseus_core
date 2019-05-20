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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.IHasName;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.event.IEventHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This is the interface for all physical operators. A physical operator can be
 * executed and can deliver output from the received input.
 * 
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator, IMonitoringDataProvider, IEventHandler, IHasName {

	/**
	 * Type of operator
	 * 
	 * @return true, if this operator is a source. A pipe is always a source, too.
	 */
	boolean isSource();

	/**
	 * Type of operator
	 * 
	 * @return true, if this operator is a sink. A pipe is always a sink, too.
	 */
	boolean isSink();

	/**
	 * Type of operator
	 * 
	 * @return true, if this operator is a pipe, i.e. a source and a sink
	 */
	boolean isPipe();

	/**
	 * For query sharing this method needs to be implemented. This method will be
	 * called if all sources of this operator and the given input are the same .
	 * 
	 * 
	 * @param ipo
	 *            This is an existing operator in the plan that is compared with
	 *            this new operator
	 * @return must return false, if the current operator and the given operator are
	 *         not semantically equal, i.e. this operator can or should not be
	 *         replaced by the input. Should return true, if the operator is
	 *         exchangeable
	 */
	boolean isSemanticallyEqual(IPhysicalOperator ipo);

	/**
	 * Name for Operator (Visual Identification)
	 */
	@Override
	String getName();

	/**
	 * Assign a name to the operator. This name should not be used for
	 * identification as uniqueness is not checked
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * For each owner (typically a query) this operator can get an id. This id must
	 * be system wide unique.
	 * 
	 * Due to query sharing, an operator can be part of multiple queries, so the
	 * must be an id for every query
	 * 
	 * @param owner
	 *            The owner of the operator, typically a query
	 * @param id
	 *            The name that should be used to identify this operator
	 */
	void addUniqueId(IOperatorOwner owner, Resource id);

	/**
	 * Remove the unique id. Will typically be done, when the query (Owner) is
	 * removed from the system
	 * 
	 * @param owner
	 *            The owner of the query
	 */
	void removeUniqueId(IOperatorOwner owner);

	/**
	 * Returns a mapping from all owners to unique ids
	 * 
	 * Due to query sharing, an operator can be part of multiple queries, so the
	 * must be an id for every query
	 * 
	 * @return
	 */
	Map<IOperatorOwner, Resource> getUniqueIds();

	/**
	 * Each operator get a UUID generated at creation time.
	 * 
	 * @return the unique identifier of this operator. When removing and adding
	 *         query again, the UUID will change! If you want an id that last query
	 *         removal, you should use addUniqueID
	 */
	UUID getUUID();

	/**
	 * gets a Map of simple information that may be used by the GUI
	 * 
	 * @return a map of key-value pairs
	 */
	Map<String, String> getParameterInfos();

	/**
	 * sets a map of key-value pairs for additional information
	 * 
	 * @param infos
	 *            a map with key-value pairs
	 */
	void setParameterInfos(Map<String, String> infos);

	/**
	 * adds a key value pair to the information map
	 * 
	 * @param key
	 *            the name of the information
	 * @param value
	 *            the value of the information
	 */
	void addParameterInfo(String key, Object value);

	/**
	 * Get the output schema of this operator. Short version for getOutputSchema(0)
	 * 
	 * @return the Schema of this operator for output port 0
	 */
	SDFSchema getOutputSchema();

	/**
	 * Get the output schema of the given port of this operator.
	 * 
	 * @param port
	 *            The port for which the schema should be delivered
	 * @return The output schema of the given port
	 */
	SDFSchema getOutputSchema(int port);

	/**
	 * Return a map, with output schemas for each assigned output port
	 * 
	 * @return
	 */
	Map<Integer, SDFSchema> getOutputSchemas();

	/**
	 * Assign the output schema to port 0
	 * 
	 * @param outputSchema
	 *            The schema that should be set
	 */
	void setOutputSchema(SDFSchema outputSchema);

	/**
	 * Assign the output schema to the given port
	 * 
	 * @param outputSchema
	 *            The schema that should be set
	 * @param port
	 *            the output port for which the schema should be set
	 */
	void setOutputSchema(SDFSchema outputSchema, int port);

	/**
	 * Call open for a distinct owner
	 */
	void open(IOperatorOwner id);

	/**
	 * Returns true, if this operator is opened (initialized)
	 * 
	 * @return
	 */
	boolean isOpen();

	/**
	 * Start this operator
	 */
	void start(IOperatorOwner id);

	/**
	 * Returns true, if this operator is started
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * Call close for a distinct owner
	 */
	void close(IOperatorOwner id);

	/**
	 * Returns true, if this operator has terminated, i.e. will no longer deliver
	 * elements. This could only happen, if the source is not a real data stream
	 * (e.g. when reading from a file).
	 * 
	 * @return
	 */
	boolean isDone();

	/**
	 * Returns true, if this operator is connected to any sources
	 * 
	 * @return
	 */
	boolean hasInput();

	/**
	 * Some operator can provide further information when debug is set to true.
	 * Depends on the operator implementation. Typically, this is used to reduce
	 * processing in standard situations. For single operators the debug mode can be
	 * enabled
	 * 
	 * @param debug
	 *            Set to true, to enable debuging of this operator. Not supported by
	 *            all operators.
	 */
	void setDebug(boolean debug);

	/**
	 * If set to true, this operator will not deliver received punctuations
	 * 
	 * @param suppressPunctuations
	 */
	void setSuppressPunctuations(boolean suppressPunctuations);

	/**
	 * Return the sessions of the queries, where the operator is part from
	 * 
	 * @return
	 */
	List<ISession> getSessions();

	/**
	 * Allow to set the logical operator for this physical operator Remark: There is
	 * not always a 1-1 mapping between a logical and a physical operator possible
	 * 
	 * @param op
	 *            the corresponding logical operator
	 */
	void setLogicalOperator(ILogicalOperator op);

	/**
	 * If set, return the logical operator of this physical operator.
	 * 
	 * Remark: There is not always a 1-1 mapping between a logical and a physical
	 * operator possible
	 * 
	 * @return The logical operator if set. Could be null
	 */
	ILogicalOperator getLogicalOperator();
}
