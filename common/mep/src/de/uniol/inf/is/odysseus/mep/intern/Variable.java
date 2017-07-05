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
package de.uniol.inf.is.odysseus.mep.intern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractExpression;

/**
 * This class represents a variable with an identifier, a data type, a set of
 * data types is can process and optional a value
 * 
 * @author Marco Grawunder
 *
 */
public class Variable extends AbstractExpression<Object> implements IMepVariable {

	Logger LOG = LoggerFactory.getLogger(Variable.class);

	private static final long serialVersionUID = 6977413577984429091L;
	/**
	 * The bounded value, could be null
	 */
	private Object value;

	/** The position in the schema. */
	private int position;
	/**
	 * The name of the variable
	 */
	final private String identifier;
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

	public Variable(Variable variable) {
		if (variable.value instanceof IClone){
			this.value = ((IClone)variable.value).clone();
		}else{
			this.value = variable.value;
		}
		this.position = variable.position;
		this.identifier = variable.identifier;
		this.type = variable.type;
		if (variable.acceptedTypes != null){
			this.acceptedTypes = new SDFDatatype[variable.acceptedTypes.length];
			System.arraycopy(variable.getAcceptedTypes(), 0, this.acceptedTypes, 0, variable.acceptedTypes.length);
		}	
	}

	/**
	 * Get the name of the variable
	 * 
	 * @return the name of the variable
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}
	

	/**
	 * Bind the value to the variable
	 * 
	 * @param value
	 *            The value to bind
	 */
	@Override
	public void bind(Object value, int position) {
		this.value = value;
		this.position = position;
	}

	
	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public Object acceptVisitor(IMepExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return identifier;
	}

	@Override
	public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
		return ((Variable) obj).identifier.equals(this.identifier);
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@Override
	public Set<IMepVariable> getVariables() {
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

	@Override
	public Variable toVariable() {
		return this;
	}

	@Override
	public IMepFunction<Object> toFunction() {
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
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		throw new RuntimeException("cannot determine type");
	}

	@Override
	public boolean determineTypeFromInput() {
		return false;
	}

	@Override
	public List<IMepExpression<Object>> conjunctiveSplit() {
		List<IMepExpression<Object>> ret = new ArrayList<>();
		ret.add(this);
		return ret;
	}
	
	@Override
	public IMepExpression<Object> clone(Map<IMepVariable, IMepVariable> vars) {
		// Variables are unique, so if the variable is cloned onces, it must
		// not be cloned again in the same expression
		IMepVariable ret = vars.get(this);
		if (ret == null){
			ret = new Variable(this);
			vars.put(this,ret);
		}
		return ret;
	}
	
	
	
	
}
