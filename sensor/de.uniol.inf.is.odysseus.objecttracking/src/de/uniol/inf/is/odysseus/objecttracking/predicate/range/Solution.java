package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * This class is the default implemention of the ISolution interface
 * and represents a solutions for a system of equations or inequalities.
 * 
 * @author André Bolles
 */
public class Solution implements ISolution, IClone{

	private String variable;
	private String compareOperator;
	private String solution;
	
	// we need both variables, since it is possible
	// that the solution is neither full nor empty
	private boolean isFull;
	private boolean isEmpty;
	
	public Solution(String variable, String operator, String solution){
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
	
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getCompareOperator() {
		return compareOperator;
	}
	public void setCompareOperator(String compareOperator) {
		this.compareOperator = compareOperator;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
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
