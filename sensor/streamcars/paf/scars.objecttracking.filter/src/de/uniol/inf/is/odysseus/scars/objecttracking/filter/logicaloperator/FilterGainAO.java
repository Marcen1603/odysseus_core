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
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterGainAO <M extends IProbability & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private String functionID;
	
	private String newListName;
	private String oldListName;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum<Parameters>, Object> parameters;
	
	private AbstractMetaDataCreationFunction<M> metaDataCreationFunction;
	
	public FilterGainAO()
	{
		super();
		
		// Standardwerte (Timo M)
		parameters = new HashMap<Enum<Parameters>, Object>();
		metaDataCreationFunction = new KalmanGainFunction<M>();
		functionID = "KALMAN";
	}
	
	public FilterGainAO(FilterGainAO<M> copy) {
		super(copy);
		this.setFunctionID(copy.getFunctionID());
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));	
		this.setMetaDataCreationFunction(copy.getMetaDataCreationFunction().clone());
		this.setNewListName(copy.getNewListName());
		this.setOldListName(copy.getOldListName());
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new FilterGainAO<M>(this);
	}
		
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	// Getter & Setter
	
	public String getFunctionID() {
		return functionID;
	}
	
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	public HashMap<Enum<Parameters>, Object> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) {
		this.parameters = parameters;
	}

	public void setMetaDataCreationFunction(AbstractMetaDataCreationFunction<M> metaDataCreationFunction) {
		this.metaDataCreationFunction = metaDataCreationFunction;
	}

	public AbstractMetaDataCreationFunction<M> getMetaDataCreationFunction() {
		return metaDataCreationFunction;
	}

	public String getNewListName() {
		return newListName;
	}

	public void setNewListName(String newListName) {
		this.newListName = newListName;
	}

	public String getOldListName() {
		return oldListName;
	}

	public void setOldListName(String oldListName) {
		this.oldListName = oldListName;
	}

	
}

