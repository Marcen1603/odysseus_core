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
/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;

/**
 * @author dtwumasi
 *
 */
public abstract class AbstractMetaDataUpdateFunction<M extends IProbability> {
	
	public AbstractMetaDataUpdateFunction() {
		parameters = new HashMap<Enum<Parameters>, Object>();
	}
	
	public AbstractMetaDataUpdateFunction(HashMap<Enum<Parameters>,Object> parameters ) {
		this.setParameters(parameters);
	}
	
	public AbstractMetaDataUpdateFunction(AbstractMetaDataUpdateFunction<M> copy ) {
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));	
	}
	
	@Override
	public abstract AbstractMetaDataUpdateFunction<M> clone();
		
	
	private HashMap<Enum<Parameters>, Object> parameters;
	
	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(IConnection connected, MVRelationalTuple<M> tuple, HashMap<Enum<Parameters>, Object> parameters);

	/**
	 * @param parameters the parameters needed for computation
	 */
	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) { 
	this.parameters = parameters;
	}
	
	/**
	 * @return the parameters
	 */
	public HashMap<Enum<Parameters>, Object> getParameters() {
		
		return parameters;
	} 

	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Enum<Parameters> key, Object value) {
		this.parameters.put(key, value);
	}

	

}
