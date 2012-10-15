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
package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * This class is the default implemention of the ISolution interface
 * and represents a solutions for a system of equations or inequalities.
 * 
 * @author André Bolles
 */
public class Solution implements ISolution{

	private SDFExpression variable;
	private String compareOperator;
	private SDFExpression solution;
	
	// we need both variables, since it is possible
	// that the solution is neither full nor empty
	private boolean isFull;
	private boolean isEmpty;
	
	public Solution(SDFExpression variable, String operator, SDFExpression solution){
		this.variable = variable;
		this.compareOperator = operator;
		this.solution = solution;
		
		if(variable == null && operator == null && solution == null){
			this.isFull = false;
			this.isEmpty = true;
		}
		else if(variable != null && operator == null && solution == null){
			this.isFull = true;
			this.isEmpty = false;
		}
	}
	
	public Solution(Solution solution){
		this.variable = solution.variable;
		this.compareOperator = solution.compareOperator;
		this.solution = solution.solution;
	}
	
	@Override
	public SDFExpression getVariable() {
		return variable;
	}
	public void setVariable(SDFExpression variable) {
		this.variable = variable;
	}
	@Override
	public String getCompareOperator() {
		return compareOperator;
	}
	public void setCompareOperator(String compareOperator) {
		this.compareOperator = compareOperator;
	}
	@Override
	public SDFExpression getSolution() {
		return solution;
	}
	public void setSolution(SDFExpression solution) {
		this.solution = solution;
	}
	
	@Override
	public Solution clone(){
		return new Solution(this);
	}
	
	@Override
	public boolean isFull(){
		return this.isFull;
	}
	
	@Override
	public boolean isEmpty(){
		return this.isEmpty;
	}
	
	public void setFull(boolean full){
		this.isFull = full;
		if(isFull){
			this.isEmpty = false;
		}
	}
	
	public void setEmpty(boolean empty){
		this.isEmpty = empty;
		if(isEmpty){
			this.isFull = false;
		}
	}
	
	@Override
	public String toString(){
		if(this.isEmpty){
			return "{}";
		}
		if(this.isFull){
			return "{t}";
		}
		return this.variable + " " + this.compareOperator + " " + this.solution.toString();
	}
}
