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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractParameter<T> implements IParameter<T> {

	private static final long serialVersionUID = -5259685918656067407L;
	private String name;
	private REQUIREMENT requirement;
	private T value;
	private IAttributeResolver resolver;
	private IDataDictionary dd;
	private final List<Exception> errors;
	protected Object inputValue;

	public AbstractParameter(String name, REQUIREMENT requirement) {
		setName(name);
		setRequirement(requirement);
		this.requirement = requirement;
		value = null;
		this.errors = new ArrayList<Exception>(1);
	}
	
	public AbstractParameter(){
		this.value = null;
		this.errors = new ArrayList<Exception>();
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	
	public void setRequirement(REQUIREMENT requirement) {
		this.requirement = requirement;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void clear() {
		inputValue = null;
		value = null;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT getRequirement() {
		return this.requirement;
	}

	@Override
	public boolean isMandatory() {
		return this.requirement == REQUIREMENT.MANDATORY;
	}

	@Override
	public List<Exception> getErrors() {
		return this.errors;
	}

	@Override
	public final boolean validate() {
		this.errors.clear();
		if (this.requirement == REQUIREMENT.MANDATORY && inputValue == null) {
			this.errors.add(new MissingParameterException(this.getName()));
			return false;
		}
		try {
			if (inputValue != null) {
				internalAssignment();
			}
		} catch (Exception e) {
			this.errors.add(new IllegalParameterException("illegal value for "
					+ getName() + ": " + e.getMessage()));
			return false;
		}
		return true;
	}

	abstract protected void internalAssignment();

	@Override
	public final T getValue() {
		if (!validate()) {
			throw new RuntimeException("parameter could not be validated");
		}
		return value;
	}

	@Override
	public final void setInputValue(Object object) {
		this.inputValue = object;
	}
	
	public Object getInputValue() {
		return this.inputValue;
	}

	@Override
	public boolean hasValue() {
		return this.value != null;
	}

	protected void setValue(T value) {
		this.value = value;
	}

	@Override
	public IAttributeResolver getAttributeResolver() {
		return this.resolver;
	}

	@Override
	public void setAttributeResolver(IAttributeResolver resolver) {
		this.resolver = resolver;
	}
	
	@Override
	public IDataDictionary getDataDictionary() {
		return dd;
	}
	
	@Override
	public void setDataDictionary(IDataDictionary dd) {
		this.dd = dd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		AbstractParameter other = (AbstractParameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
