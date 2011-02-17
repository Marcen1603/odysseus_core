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

/**
 * A static parameter represents a single atomic value
 * @author Simon Flandergan
 *
 */
public class StaticParameter implements IActionParameter {
	private Object value;
	private Class<?> paramClass;

	public StaticParameter(Object value) {
		this.value = value;
		this.paramClass = value.getClass();
	}

	@Override
	public Class<?> getParamClass() {
		return this.paramClass;
	}

	@Override
	public ParameterType getType() {
		return ParameterType.Value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

}
