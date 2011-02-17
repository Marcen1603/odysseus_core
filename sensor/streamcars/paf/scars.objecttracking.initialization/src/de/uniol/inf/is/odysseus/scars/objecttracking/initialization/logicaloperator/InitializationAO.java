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
package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.logicaloperator;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author dtwumasi
 *
 */
public class InitializationAO<M extends IProbability> extends UnaryLogicalOp {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3037302552522493103L;

	private HashMap<Enum, Object> parameters;
	
	// path to new  objects
	private String newObjListPath;
	// path to old  objects
	private String oldObjListPath;
	
	/**
	 * @param AO
	 */
	public InitializationAO(InitializationAO<M> copy) {
		super(copy);
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
	}

	/**
	 * 
	 */
	public InitializationAO() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new InitializationAO<M>(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ILogicalOperator#getOutputSchema()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getOutputSchema();
		}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public String getNewObjListPath() {
		return newObjListPath;
	}

	public void setParameters(HashMap<Enum, Object> parameters) {
		this.parameters = parameters;
	}

	public HashMap<Enum, Object> getParameters() {
		return parameters;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public String getOldObjListPath() {
		return oldObjListPath;
	}

}
