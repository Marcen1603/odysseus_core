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
package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.event.IEventHandler;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	 * Schemarelated methods
	 * @return
	 */
	public SDFAttributeList getOutputSchema();
	public SDFAttributeList getOutputSchema(int port);
	public void setOutputSchema(SDFAttributeList outputSchema);
	public void setOutputSchema(SDFAttributeList outputSchema, int port);
	
	@Override
	public IPhysicalOperator clone();
	
	public boolean isOpen();

}
