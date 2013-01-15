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

import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.event.IEventHandler;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IOwnedOperator,
		IMonitoringDataProvider, IEventHandler, IClone {

	boolean isSource();

	boolean isSink();

	boolean isPipe();
	
	boolean isSemanticallyEqual(IPhysicalOperator ipo);
	
	/**
	 * Name for Operator (Visual Identification) 
	 */
	public String getName();
	public void setName(String name);
	
	/**
	 * gets a Map of simple information that may be used by the GUI
	 * @return a map of key-value pairs
	 */
	public Map<String, String> getInfos();
	/**
	 * sets a map of key-value pairs for additional information
	 * @param infos a map with key-value pairs
	 */
	public void setInfos(Map<String, String> infos);
	
	/**
	 * adds a key value pair to the information map
	 * @param key the name of the information
	 * @param value the value of the information
	 */
	public void addInfo(String key, Object value);
	
	
	/**
	 * Schemarelated methods
	 * @return
	 */
	public SDFSchema getOutputSchema();
	public SDFSchema getOutputSchema(int port);
	public Map<Integer, SDFSchema> getOutputSchemas();
	public void setOutputSchema(SDFSchema outputSchema);
	public void setOutputSchema(SDFSchema outputSchema, int port);
	
	@Override
	public IPhysicalOperator clone();
	
	public boolean isOpen();

	void addUniqueId(IOperatorOwner owner, String id);
	void removeUniqueId(IOperatorOwner key);
	Map<IOperatorOwner,String> getUniqueIds();

}
