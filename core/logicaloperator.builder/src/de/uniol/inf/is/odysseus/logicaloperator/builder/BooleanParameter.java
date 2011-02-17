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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class BooleanParameter extends AbstractParameter<Boolean> {
	
	private static final long serialVersionUID = -7491596371995854348L;

	public BooleanParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);		
	}

	@Override
	protected void internalAssignment() {
		boolean value = Boolean.parseBoolean(inputValue.toString());		
		setValue(value);
		
	}

}
