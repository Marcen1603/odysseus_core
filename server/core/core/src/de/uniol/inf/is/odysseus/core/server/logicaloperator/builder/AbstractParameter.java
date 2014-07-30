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

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This is an abstract parameter. In this abstract base class all attributes are defined, that a common for all parameters
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractParameter<T> implements IParameter<T> {

	private static final String DEFAULT_DOC_TEXT = "No description";

	private static final long serialVersionUID = -5259685918656067407L;
	private String name;
	private String doc;
	private REQUIREMENT requirement = REQUIREMENT.OPTIONAL;
	private USAGE usage = USAGE.RECENT;
	private T value;
	private IAttributeResolver resolver;
	private IDataDictionary dd;
	private ISession caller;
	private Context context;
	private final List<Exception> errors;
	private final List<Exception> warnings;
	protected Object inputValue;

	private String possibleValueMethod = "";
	private boolean possibleValuesAreDynamic = false;


	public AbstractParameter(String name, REQUIREMENT requirement, USAGE usage) {
		this(name, requirement, usage, null);
	}

	public AbstractParameter(String name, REQUIREMENT requirement, USAGE usage, String doc) {
		setName(name);
		setRequirement(requirement);
		setDoc(doc);

		this.requirement = requirement;
		this.usage = usage;
		value = null;
		this.errors = new ArrayList<Exception>(1);
		this.warnings = new ArrayList<Exception>(1);
	}

	public AbstractParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}

	public AbstractParameter() {
		this.value = null;
		this.errors = new ArrayList<Exception>(1);
		this.warnings = new ArrayList<Exception>(1);
	}

	@Override
	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	@Override
	public void setDoc(String doc) {
		this.doc = doc;
	}

	@Override
	public void setRequirement(REQUIREMENT requirement) {
		this.requirement = requirement;
	}

	@Override
	public void setUsage(de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.USAGE usage) {
		this.usage = usage;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDoc() {
		if (this.doc == null || this.doc.isEmpty()) {
			return DEFAULT_DOC_TEXT;
		} else {
			return this.doc;
		}
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
	public boolean isDeprecated() {
		return this.usage == USAGE.DEPRECATED;
	}

	@Override
	public List<Exception> getErrors() {
		return this.errors;
	}

	@Override
	public final boolean validate() {
		this.errors.clear();
		this.warnings.clear();
		if (this.requirement == REQUIREMENT.MANDATORY && inputValue == null) {
			this.errors.add(new MissingParameterException(this.getName()));
			return false;
		}
		if (isDeprecated()) {
			this.warnings.add(new DeprecatedParameterWarning(this.getName()));
		}
		try {
			if (inputValue != null) {
				if (internalValidation()) {
					internalAssignment();
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			this.errors.add(new IllegalParameterException("illegal value for parameter " + getName(), e));
			return false;
		}
		return true;
	}

	abstract protected void internalAssignment();

	public boolean internalValidation() {
		return true;
	}

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
	public void setCaller(ISession caller) {
		this.caller = caller;
	}
	
	@Override
	public ISession getCaller() {
		return caller;
	}
	
	@Override
	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public Context getContext() {
		return context;
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

	@Override
	public final String getPQLString() {
		if (hasValue()) {
			return getPQLStringInternal();
		}
		return null;
	}

	protected abstract String getPQLStringInternal();
	
	@Override
	public String getPossibleValueMethod() {	
		return possibleValueMethod;
	}
	
	@Override
	public void setPossibleValueMethod(String possibleValueMethod) {
		this.possibleValueMethod = possibleValueMethod;	
	}
	
	@Override
	public void setPossibleValuesAreDynamic(boolean possibleValuesAreDynamic){
		this.possibleValuesAreDynamic =possibleValuesAreDynamic;
	}
	
	@Override
	public boolean arePossibleValuesDynamic() {
		return possibleValuesAreDynamic;
	}
}
