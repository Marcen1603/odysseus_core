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
 * 
 */
package de.uniol.inf.is.odysseus.logicaloperator;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

/**
 * @author Jonas Jacobi
 */
public interface IParameter<T> extends Serializable {
	public static enum REQUIREMENT { MANDATORY, OPTIONAL };
	public void setName(String name);
	public String getName();
	public IParameter.REQUIREMENT getRequirement();
	public void setRequirement(REQUIREMENT requirement);
	public void setInputValue(Object object);
	public boolean validate();
	public List<Exception> getErrors();
	public T getValue();
	public boolean hasValue();
	public boolean isMandatory();
	public void setAttributeResolver(IAttributeResolver resolver);
	public IAttributeResolver getAttributeResolver();
	public void clear();
}