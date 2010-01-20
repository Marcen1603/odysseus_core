package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This class is the default implemention of the ISolution interface
 * and represents a solutions for a system of equations or inequalities.
 * 
 * @author André Bolles
 */
public class Solution implements ISolution, IClone{

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
	
	public SDFExpression getVariable() {
		return variable;
	}
	public void setVariable(SDFExpression variable) {
		this.variable = variable;
	}
	public String getCompareOperator() {
		return compareOperator;
	}
	public void setCompareOperator(String compareOperator) {
		this.compareOperator = compareOperator;
	}
	public SDFExpression getSolution() {
		return solution;
	}
	public void setSolution(SDFExpression solution) {
		this.solution = solution;
	}
	
	public Solution clone(){
		return new Solution(this);
	}
	
	public boolean isFull(){
		return this.isFull;
	}
	
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
}
