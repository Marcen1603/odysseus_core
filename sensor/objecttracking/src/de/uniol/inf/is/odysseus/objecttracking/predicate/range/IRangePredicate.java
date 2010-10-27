package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * A range predicate does not say, if an expression is true or not. It returns
 * the time interval, when the expression is true. If the interval is empty.
 * the expression is never true.
 * 
 * @author André Bolles
 *
 * @param <T>
 */
public interface IRangePredicate<T> extends IClone, Serializable{

	public static String tokenizerDelimiters = " \t\n\r\f + - * / < > '<=' '>=' ^ ( )";
	
	public List<ITimeInterval> evaluate(T input);
	public List<ITimeInterval> evaluate(T left, T right);
	@Override
	public IRangePredicate<T> clone();
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema);
	public long getAdditionalEvaluationDuration();
	
}
