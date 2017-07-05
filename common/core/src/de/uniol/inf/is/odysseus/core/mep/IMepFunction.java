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

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public interface IMepFunction<T> extends IMepExpression<T> {
	/**
	 * Get the number of parameter that can be given to this function 
	 * @return the numer of parameters
	 */
	public int getArity();
	
	/**
	 * Set all arguments in order of the function at once.
	 * @param arguments
	 */
	public void setArguments(IMepExpression<?>... arguments);
	
	/**
	 * Set arguments at distinct positions of this function
	 * @param argumentPosition The position to set
	 * @param argument The argument to set
	 */
	public void setArgument(int argumentPosition, IMepExpression<?> argument);

	/**
	 * Get the array of arguments this function has set
	 * @return the arguments of this functions that are set, can contain null values if 
	 * some positions are not set
	 */
	public IMepExpression<?>[] getArguments();
	
	/**
	 * Get the argument at a specific position
	 * @param argumentPosition the position of the argument to deliver
	 * @return the argument, could be null
	 */
	public IMepExpression<?> getArgument(int argumentPosition);
	
	/**
	 * Get the sessions assigned to this function
	 * @return
	 */
	public List<ISession> getSessions();
	/**
	 * Set sessions
	 */
	public void setSessions(List<ISession> session);
	
	/**
	 * Gets the types that are accepted at the position
	 * @param argPos the argument position
	 * @return the array of types that are accepted
	 */
	public SDFDatatype[] getAcceptedTypes(int argPos);	
	
	/**
	 * Return the symblic representation of this function (e.g. the name)
	 * @return
	 */
	public String getSymbol();

    /**
     * Get the time complexity value of this function. The complexity value is a
     * numeric value between 0-9 describing the average expected time complexity
     * of this function. 0 means no processing time (e.g., a constant value) and
     * 9 means a high time complexity (e.g., estimating an integral).
     * 
     * @return The time complexity value
     */
    public int getTimeComplexity();

    /**
     * Get the space complexity value of this function. The complexity value is
     * a
     * numeric value between 0-9 describing the average expected space
     * complexity
     * of this function. 0 means no space requirements (e.g., a constant value)
     * and
     * 9 means a high space complexity (e.g., n^n^n).
     * 
     * @return The time complexity value
     */
    public int getSpaceComplexity();

	/**
	 * The could be cases where the parser detects a constant (e.g. a string) 
	 * as a parameter. In most cases this could be optimized by replacing
	 * this function with a Constant with the value of the function
	 * Sometime this constant could e.g. represent a storage place where
	 * each call of getValue() can deliver another value. In this case
	 * the value must not be evaluated once but at each call.
	 * 
	 * Remark: If at least one parameter is a variable the function will never be called!
	 * 
	 * @return true, if the function in case of only constant parameters can be optimized, false
	 * if the call to the function with a constant parameter can deliver different results 
	 */
	public boolean optimizeConstantParameter();

	public void setBasetimeUnit(TimeUnit baseTimeUnit);

	void propagateSessionReference(List<ISession> sessions);

	boolean cnfEquals(IMepExpression<?> otherExpression);

	boolean isContainedIn(IMepExpression<?> otherExpression);

	boolean isAndPredicate();

	boolean isOrPredicate();

	boolean isNotPredicate();
	
}
