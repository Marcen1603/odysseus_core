package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * This interface represents a solution for a system of equations or
 * inequalities.
 * 
 * @author André Bolles
 *
 */
public interface ISolution extends IClone{

	public SDFExpression getVariable();
	public String getCompareOperator();
	public SDFExpression getSolution();
	public boolean isEmpty();
	public boolean isFull();
}
