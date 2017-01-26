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
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider, IEventHandler, IHasName {

	boolean isSource();

	boolean isSink();

	boolean isPipe();

	boolean isSemanticallyEqual(IPhysicalOperator ipo);

	/**
	 * Name for Operator (Visual Identification)
	 */
	@Override
	public String getName();
	public void setName(String name);

	/**
	 * gets a Map of simple information that may be used by the GUI
	 * @return a map of key-value pairs
	 */
	public Map<String, String> getParameterInfos();
	/**
	 * sets a map of key-value pairs for additional information
	 * @param infos a map with key-value pairs
	 */
	public void setParameterInfos(Map<String, String> infos);

	/**
	 * adds a key value pair to the information map
	 * @param key the name of the information
	 * @param value the value of the information
	 */
	public void addParameterInfo(String key, Object value);


	/**
	 * Schemarelated methods
	 * @return
	 */
	public SDFSchema getOutputSchema();
	public SDFSchema getOutputSchema(int port);
	public Map<Integer, SDFSchema> getOutputSchemas();
	public void setOutputSchema(SDFSchema outputSchema);
	public void setOutputSchema(SDFSchema outputSchema, int port);

	/**
	 * Call open for a distinct owner
	 */
	void open(IOperatorOwner id) throws OpenFailedException;
	public boolean isOpen();

	/**
	 * Call start
	 */
	void start(IOperatorOwner id) throws StartFailedException;
	public boolean isStarted();


	/**
	 * Call close for a distinct owner
	 */
	void close(IOperatorOwner id);

	boolean isDone();

	void addUniqueId(IOperatorOwner owner, Resource id);
	void removeUniqueId(IOperatorOwner key);
	Map<IOperatorOwner,Resource> getUniqueIds();

	public boolean hasInput();

	/**
	 * debug
	 */
	void setDebug(boolean debug);

	void setSuppressPunctuations(boolean suppressPunctuations);

	List<ISession> getSessions();

	void setLogicalOperator(ILogicalOperator op);

	ILogicalOperator getLogicalOperator();
}
