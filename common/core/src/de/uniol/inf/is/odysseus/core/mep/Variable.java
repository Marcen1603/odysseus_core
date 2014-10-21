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
package de.uniol.inf.is.odysseus.core.mep;

import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * This class represents a variable with an identifier, a data type, a set of
 * data types is can process and optional a value
 * 
 * @author Marco Grawunder
 *
 */
public class Variable implements IExpression<Object> {

	Logger LOG = LoggerFactory.getLogger(Variable.class);

	private static final long serialVersionUID = 6977413577984429091L;
	/**
	 * The bounded value, could be null
	 */
	private Object value;
	/**
	 * The metadata for this object, could be null
	 */
	private IMetaAttribute metadata;
	/** The position in the schema. */
	private int position;
	/**
	 * The name of the variable
	 */
	private final String identifier;
	/**
	 * The type of the variable
	 */
	private final SDFDatatype type;
	/**
	 * The array of types that could be used to bind a value
	 */
	private SDFDatatype[] acceptedTypes;

	// private Integer arrayIndex;

	/**
	 * Create a new Variable of type Object
	 * 
	 * @param id
	 *            The name of the variable
	 */
	public Variable(String id) {
		this.identifier = id;
		this.type = SDFDatatype.OBJECT;
	}

	/**
	 * Create a new Variable
	 * 
	 * @param id
	 *            The name of the variable
	 * @param type
	 *            The type of the variable
	 */
	public Variable(String id, SDFDatatype type) {
		this.identifier = id;
		this.type = type;
	}

	/**
	 * Get the name of the variable
	 * 
	 * @return the name of the variable
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Bind the value to the variable
	 * 
	 * @param value
	 *            The value to bind
	 */
	public void bind(Object value, IMetaAttribute metadata, int position) {
		this.value = value;
		this.metadata = metadata;
		this.position = position;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	public IMetaAttribute getMetadata() {
		return metadata;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return identifier;
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
	public int getReturnTypeCard() {
		return 1;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return getReturnType();
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	// public boolean isArray() {
	// if(arrayIndex != null) {
	// return true;
	// } else {
	// return false;
	// }
	// }

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

	/**
	 * Get the array of value types that this variable a bind
	 * 
	 * @return the array of type
	 */
	public SDFDatatype[] getAcceptedTypes() {
		return acceptedTypes;
	}

	/**
	 * Set the array of value types that this variable a bind
	 */
	public void setAcceptedTypes(SDFDatatype[] acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}

	/**
	 * Remove some types from the array of accepted types
	 * 
	 * @param restrictTypes
	 *            The array of types that should be removed
	 */
	public void restrictAcceptedTypes(SDFDatatype[] restrictTypes) {
		int countOfRemovedTypes = 0;
		for (int i = 0; i < this.acceptedTypes.length; i++) {
			boolean foundCompatible = false;
			if (restrictTypes != null) {
				for (int u = 0; u < restrictTypes.length; u++) {
					if ((acceptedTypes[i] != null)
							&& (this.acceptedTypes[i]
									.compatibleTo(restrictTypes[u]))) {
						this.acceptedTypes[i] = SDFDatatype.min(
								this.acceptedTypes[i], restrictTypes[u]);
						foundCompatible = true;
					}
				}
			}
			if (!foundCompatible) {
				this.acceptedTypes[i] = null;
				countOfRemovedTypes++;
			}
		}

		// remove all null values from the array;
		SDFDatatype[] acceptedTypesNew = new SDFDatatype[this.acceptedTypes.length
				- countOfRemovedTypes];
		int newIndex = 0;
		for (int i = 0; i < this.acceptedTypes.length; i++) {
			if (this.acceptedTypes[i] != null) {
				acceptedTypesNew[newIndex++] = this.acceptedTypes[i];
			} else {
				// LOG.warn(identifier+" accepted type was null!!");
				// RuntimeException e = new RuntimeException();
				// e.printStackTrace();
			}
		}

		this.acceptedTypes = acceptedTypesNew;
	}

	@Override
	public SDFDatatype determineType(IExpression<?>[] args) {
		throw new RuntimeException("cannot determine type");
	}

	@Override
	public boolean determineTypeFromInput() {
		return false;
	}

	// public Integer getArrayIndex() {
	// return arrayIndex;
	// }
	//
	// public void setArrayIndex(Integer arrayIndex) {
	// this.arrayIndex = arrayIndex;
	// }
}
