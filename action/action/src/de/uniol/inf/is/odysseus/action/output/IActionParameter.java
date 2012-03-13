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
package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.operator.EventTriggerPO;

/**
 * Interface describing a parameter for the method of an {@link Action}
 * @see Action
 * @see EventTriggerPO
 * @author Simon Flandergan
 *
 */
public interface IActionParameter {
	public enum ParameterType{Attribute, Value}
		
	public Class<?> getParamClass();

	public ParameterType getType();

	public Object getValue();
}
