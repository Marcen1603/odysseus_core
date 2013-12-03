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
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * This Interface represents all expressions in MEP. Could be 
 * a function, a variable or a constant
 * 
 * @author Jonas Jacobi
 *
 * @param <T>
 */
public interface IExpression<T> extends Serializable{
	
	/**
	 * Returns the value of this expression
	 * @return
	 */
	public T getValue();
	
	/**
	 * Method for optimization purposes (walk through the expression)
	 * @param visitor TODO
	 * @param data TODO
	 * @return TODO
	 */
	public Object acceptVisitor(IExpressionVisitor visitor, Object data);
	
	/**
	 * Returns the set of Variables of this expression
	 * @return
	 */
	public Set<Variable> getVariables();

	/**
	 * Return variable with name 
	 * @param name The name of the variable
	 * @return The Variable
	 */
	public Variable getVariable(String name);
	
	/**
	 * Delivers the return type of this expression
	 * @return The SDFExpression
	 */
	public SDFDatatype getReturnType();
	
	/**
	 * To avoid instanceof
	 * @return true if this expression is a variable
	 */
	public boolean isVariable();

	/**
	 * To avoid instanceof
	 * @return true if this expression is a function
	 */
	public boolean isFunction();

	/**
	 * To avoid instanceof
	 * @return true if this expression is a constant
	 */
	public boolean isConstant();
	
	/**
	 * Conversion function
	 * @return the variable if this expression is a variable, runtime exception else
	 */
	public Variable toVariable();
	
	/**
	 * Conversion function
	 * @return the function if this expression is a function, runtime exception else
	 */	
	public IFunction<T> toFunction();
	
	/**
	 * Conversion function
	 * @return the constant if this expression is a constant, runtime exception else
	 */
	public Constant<T> toConstant();
}
