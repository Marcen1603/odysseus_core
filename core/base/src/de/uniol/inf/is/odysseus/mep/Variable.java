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
package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class Variable implements IExpression<Object> {
	private Object value;
	private final String identifier;
	private final SDFDatatype type;
	private Class<?>[] acceptedTypes;

	public Variable(String id) {
		this.identifier = id;
		this.type = SDFDatatype.OBJECT;
	}

	public Variable(String id, SDFDatatype type) {
		this.identifier = id;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void bind(Object value) {
		if (value instanceof Number) {
			value = ((Number) value).doubleValue();
		}
		this.value = value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return identifier + "[ " + value + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		return ((Variable) obj).identifier.equals(this.identifier);
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@Override
	public Set<Variable> getVariables() {
		return Collections.singleton(this);
	}

	@Override
	public Variable getVariable(String varName) {
		return this.identifier.equals(varName) ? this : null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return type;
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		return this;
	}

	@Override
	public IFunction<Object> toFunction() {
		throw new RuntimeException("cannot convert Variable to IFunction");
	}

	@Override
	public Constant<Object> toConstant() {
		throw new RuntimeException("cannot convert Variable to Constant");
	}
	
	public Class<?>[] getAcceptedTypes() {
		return acceptedTypes;
	}

	public void setAcceptedTypes(Class<?>[] acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}

	public void restrictAcceptedTypes(Class<?>[] restrictTypes){
		int countOfRemovedTypes = 0;
		for(int i = 0; i<this.acceptedTypes.length; i++){			
			boolean foundCompatible = false;
			for(int u = 0; u<restrictTypes.length; u++){
				if(DataTypeUtils.compatible(this.acceptedTypes[i], restrictTypes[u])){
					this.acceptedTypes[i] = DataTypeUtils.min(this.acceptedTypes[i], restrictTypes[u]);
					foundCompatible = true;
				}
			}
			if(!foundCompatible){
				this.acceptedTypes[i] = null;
				countOfRemovedTypes++;
			}
		}
		
		// remove all null values from the array;
		Class<?>[] acceptedTypesNew = new Class<?>[this.acceptedTypes.length-countOfRemovedTypes];
		int newIndex = 0;
		for(int i = 0; i<this.acceptedTypes.length; i++){
			if(this.acceptedTypes[i]!= null){
				acceptedTypesNew[newIndex++] = this.acceptedTypes[i];
			}
		}
		
		this.acceptedTypes = acceptedTypesNew;
	}
}
