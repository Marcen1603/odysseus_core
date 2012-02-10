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
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * Die IStreamCarsExpression dient zur Kapsellung eines mathematischen Ausdrucks.
 * @author Benjamin G
 */
public interface IStreamCarsExpression {
	
	/**
	 * 
	 * @return
	 */
	public String getExpression();
	
	/**
	 * 
	 * @return
	 */
	public List<IStreamCarsExpressionVariable> getVariables();
	
	/**
	 * 
	 * @return
	 */
	public Object getValue();
	
	/**
	 * 
	 * @return
	 */
	public double getDoubleValue();
	
	/**
	 * 
	 * @return
	 */
	public IStreamCarsExpressionVariable getTarget();
	
	/**
	 * 
	 */
	public void evaluate();
	
	/**
	 * 
	 * @param schemata
	 */
	public void init(SDFSchema...schemata);
	
	/**
	 * 
	 * @param schema
	 * @param index
	 */
	public void replaceVaryingIndex(SDFSchema schema, int index);
	
	/**
	 * 
	 * @param schema
	 * @param index
	 * @param copy
	 */
	public void replaceVaryingIndex(SDFSchema schema, int index, boolean copy);
	
	/**
	 * 
	 * @param variable
	 * @param value
	 */
	public void bind(String variable, Object value);
	
	/**
	 * 
	 * @param variable
	 * @param value
	 */
	public void bind(IStreamCarsExpressionVariable variable, Object value);
	
	/**
	 * 
	 * @param schema
	 * @param tuple
	 */
	public void bindTupleValues(SDFSchema schema, MVRelationalTuple<?> tuple);
	

	public void reset();
	
}
