package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;


/**
 * A range predicate does not say, if an expression is true or not. It returns
 * the time interval, when the expression is true. If the interval is empty.
 * the expression is never true.
 * 
 * @author Andr� Bolles
 *
 * @param <T>
 */
public interface IRangePredicate<T> extends IClone, Serializable{

	public List<ITimeInterval> evaluate(T input);
	public List<ITimeInterval> evaluate(T left, T right);
	public IRangePredicate<T> clone();
	
}
