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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * This Interface represents all expressions in MEP. Could be a function, a
 * variable or a constant
 *
 * @author Jonas Jacobi
 *
 * @param <T>
 */
public interface IMepExpression<T> extends Serializable {

	/**
	 * Returns the value of this expression
	 * 
	 * @return
	 */
	public T getValue();

	/**
	 * Method for optimization purposes (walk through the expression)
	 * 
	 * @param visitor
	 *            TODO
	 * @param data
	 *            TODO
	 * @return TODO
	 */
	public Object acceptVisitor(IMepExpressionVisitor visitor, Object data);

	/**
	 * Returns the set of Variables of this expression
	 * 
	 * @return
	 */
	public Set<IMepVariable> getVariables();

	/**
	 * Return variable with name
	 * 
	 * @param name
	 *            The name of the variable
	 * @return The Variable
	 */
	public IMepVariable getVariable(String name);

	/**
	 * Delivers the return type of this expression
	 * 
	 * @return The SDFExpression
	 */
	public SDFDatatype getReturnType();

	/**
	 * For some return types (e.g. TUPLE) the size of the result
	 * 
	 * @return
	 */
	public int getReturnTypeCard();

	/**
	 * For some types (e.g. TUPLE) there may be sub types
	 * 
	 * @param pos
	 *            the position of the type in the return element
	 * @return the type at position pos
	 */
	public SDFDatatype getReturnType(int pos);

	/**
	 * To avoid instanceof
	 * 
	 * @return true if this expression is a variable
	 */
	public boolean isVariable();

	/**
	 * To avoid instanceof
	 * 
	 * @return true if this expression is a function
	 */
	public boolean isFunction();

	/**
	 * To avoid instanceof
	 * 
	 * @return true if this expression is a constant
	 */
	public boolean isConstant();

	/**
	 * Conversion function
	 * 
	 * @return the variable if this expression is a variable, runtime exception else
	 */
	public IMepVariable toVariable();

	/**
	 * Conversion function
	 * 
	 * @return the function if this expression is a function, runtime exception else
	 */
	public IMepFunction<T> toFunction();

	/**
	 * Conversion function
	 * 
	 * @return the constant if this expression is a constant, runtime exception else
	 */
	public IMepConstant<T> toConstant();

	public boolean determineTypeFromInput();

	public SDFDatatype determineType(IMepExpression<?>[] args);

	public IMepExpression<T> clone(Map<IMepVariable, IMepVariable> vars);

	public List<IMepExpression<T>> conjunctiveSplit();

	IMepFunction<?> and(IMepExpression<?> expression);

	IMepFunction<?> or(IMepExpression<?> expression);

	IMepFunction<?> not();

	/**
	 * The constraints returned by this method are added to the attributes if the
	 * operator supports adding constraints.
	 * 
	 * @return The collection of constraints to be added or an empty list, if no
	 *         constraints have to be added
	 */
	public Collection<SDFConstraint> getConstraintsToAdd();

}
