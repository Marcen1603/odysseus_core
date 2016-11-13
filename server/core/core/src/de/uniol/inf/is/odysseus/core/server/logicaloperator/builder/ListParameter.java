/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ValidationException;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;

public class ListParameter<T> extends AbstractParameter<List<T>> {

	private static final long serialVersionUID = -369501131293705600L;
	private IParameter<T> singleParameter;

	public ListParameter(String name, REQUIREMENT requirement, IParameter<T> singleParameter) {
		super(name, requirement, USAGE.RECENT);
		this.singleParameter = singleParameter;
	}

	public ListParameter(String name, REQUIREMENT requirement, IParameter<T> singleParameter, USAGE usage) {
		super(name, requirement, usage);
		this.singleParameter = singleParameter;
	}

	public ListParameter(IParameter<T> singleParameter) {
		this.singleParameter = singleParameter;
	}

	@Override
	public String getAliasName() {
		return this.singleParameter.getAliasName();
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
				singleParameter.setServerExecutor(getServerExecutor());
				if (!singleParameter.validate()) {
					throw new ValidationException("", singleParameter.getErrors());
				}
				list.add(singleParameter.getValue());
			}
			setValue(list);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("wrong input for parameter " + getName() + ", List expected, got " + inputValue.getClass().getSimpleName());
		}

	}

	@Override
	public void setAttributeResolver(DirectAttributeResolver resolver) {
		super.setAttributeResolver(resolver);
		singleParameter.setAttributeResolver(resolver);
	}

	public IParameter<T> getSingleParameter() {
		return singleParameter;
	}

	@Override
	protected String getPQLStringInternal() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Object[] inputList = ((List<?>)inputValue).toArray(new Object[0]);
		for (int i = 0; i < inputList.length; i++) {
			Object o = inputList[i];

			singleParameter.setInputValue(o);
			singleParameter.setAttributeResolver(getAttributeResolver());
			singleParameter.setDataDictionary(getDataDictionary());
			if (!singleParameter.validate()) {
				throw new RuntimeException(singleParameter.getErrors().get(0));
			}
			sb.append(singleParameter.getPQLString());
			if( i < inputList.length - 1 ) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
