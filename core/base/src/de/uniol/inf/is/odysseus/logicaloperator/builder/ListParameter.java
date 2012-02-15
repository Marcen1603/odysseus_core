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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class ListParameter<T> extends AbstractParameter<List<T>> {

	private static final long serialVersionUID = -369501131293705600L;
	private IParameter<T> singleParameter;

	public ListParameter(String name, REQUIREMENT requirement,
			IParameter<T> singleParameter) {
		super(name, requirement);
		this.singleParameter = singleParameter;
	}

	public ListParameter(IParameter<T> singleParameter) {
		this.singleParameter = singleParameter;
	}

	@Override
	protected void internalAssignment() {
		// TODO allgemein input parametertyp ueberpruefen
		try {
			ArrayList<T> list = new ArrayList<T>();
			for (Object o : (List<?>) inputValue) {
				singleParameter.setInputValue(o);
				singleParameter.setAttributeResolver(getAttributeResolver());
				singleParameter.setDataDictionary(getDataDictionary());
				if (!singleParameter.validate()) {
					throw new RuntimeException(singleParameter.getErrors().get(
							0));
				}
				list.add(singleParameter.getValue());
			}
			setValue(list);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("wrong input for parameter "
					+ getName() + ", List expected, got "
					+ inputValue.getClass().getSimpleName());
		}

	}

	@Override
	public void setAttributeResolver(IAttributeResolver resolver) {
		super.setAttributeResolver(resolver);
		singleParameter.setAttributeResolver(resolver);
	}
	

	public IParameter<T> getSingleParameter() {
		return singleParameter;
	}

}
