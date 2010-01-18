package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

/**
 * This interface represents a solution for a system of equations or
 * inequalities.
 * 
 * @author André Bolles
 *
 */
public interface ISolution {

	public String getVariable();
	public String getCompareOperator();
	public String getSolution();
}
