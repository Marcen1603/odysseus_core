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
package de.uniol.inf.is.odysseus.mining.classification.model;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
/**
 * This interface defines a classification object used to wrap tuples for classification operators
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public interface IClassificationObject<U extends IMetaAttribute> {

	/**get the attribute values of the tuple
	 * @return the attribute values
	 */
	public Object[] getAttributes();
	
	/** get the number of attributes used for the classification
	 * @return the number of attributes
	 */
	public int getClassificationAttributeCount();
	
	/**get the values of the tuples classification attributes
	 * @return the attribute values
	 */
	public Object[] getClassificationAttributes();
	
	/**get the class label
	 * @return the class the tuple belongs to
	 */
	public Object getClassLabel();
	
	/**set the tuples class lable
	 * @param classLabel the class to set
	 */
	public void setClassLabel(Object classLabel);
	
	}
