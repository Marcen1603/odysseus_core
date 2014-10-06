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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * This generic class represents a constant with a data type and a value
 * 
 * @author Jonas Jacobi
 * 
 * @param <T>
 *            Java type of this value, should be compatible to the SDFDatatype
 *            used
 */

public class Constant<T> implements IExpression<T> {

	private static final long serialVersionUID = 504053838249636471L;
	/**
	 * The value of this
	 */
	private final T value;
	private SDFDatatype type;

	/**
	 * Create a new constant
	 * 
	 * @param value
	 *            The value
	 * @param type
	 *            The SDFDatatype
	 */
	public Constant(T value, SDFDatatype type) {
		this.value = value;
		this.type = type;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		// if( type.isNumeric() ){
		// NumberFormat f = new DecimalFormat("#");
		// return f.format(value);
		// }
		if (type == SDFDatatype.STRING || type == SDFDatatype.START_TIMESTAMP_STRING) {
			return "\""+value.toString()+"\"";
		} else {
			return value.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Variable> getVariables() {
		return Collections.EMPTY_SET;
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
	public SDFDatatype getReturnType(SDFDatatype inputType) {
		return inputType.getSubType();
	}

	@Override
	public Variable getVariable(String name) {
		return null;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert Constant to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		throw new RuntimeException("cannot convert Constant to IFunction");
	}

	@Override
	public Constant<T> toConstant() {
		return this;
	}
	
	@Override
	public boolean determineTypeFromFirstInput() {
		return false;
	}
}
