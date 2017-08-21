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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractExpression;

/**
 * This generic class represents a constant with a data type and a value
 * 
 * @author Jonas Jacobi
 * 
 * @param <T>
 *            Java type of this value, should be compatible to the SDFDatatype
 *            used
 */

public class Constant<T> extends AbstractExpression<T> implements IMepConstant<T> {

	private static final long serialVersionUID = 504053838249636471L;
	private static final NumberFormat DOUBLE_FORMATTER = NumberFormat.getNumberInstance(Locale.ENGLISH); // '.' instead of ',' 
	
	/**
	 * The value of this
	 */
	private final T value;
	final private SDFDatatype type;

	static {
		DOUBLE_FORMATTER.setGroupingUsed(false);
	}
	
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
	public Object acceptVisitor(IMepExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		if (value == null) {
			return "null";
		}
		if( type.isDouble() ) {
			return DOUBLE_FORMATTER.format(value);
		}
		
		if (type == SDFDatatype.STRING || type == SDFDatatype.START_TIMESTAMP_STRING) {
			return "\"" + value + "\"";
		}
		
		return  ""+value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<IMepVariable> getVariables() {
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
	public Variable getVariable(String name) {
		return null;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public Constant<T> toConstant() {
		return this;
	}
	
	@Override
	public List<IMepExpression<T>> conjunctiveSplit() {
		List<IMepExpression<T>> ret = new ArrayList<>();
		ret.add(this);
		return ret;
	}
	
	@Override
	public IMepExpression<T> clone(Map<IMepVariable, IMepVariable> vars){
		return this;
	}


}
