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

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class CreateSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> list = (List<String>) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException(
					"Wrong number of inputs for SDFAttribute. Expecting id and datatype.");
		}
		SDFAttribute attribute = new SDFAttribute(list.get(0));
		attribute.setDatatype(GlobalState.getActiveDatadictionary().getDatatype(list.get(1)));

		setValue(attribute);
	}
}
