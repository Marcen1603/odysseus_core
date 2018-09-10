package de.uniol.inf.is.odysseus.mep.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
@Deprecated
public class SMinFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 8052397285858330468L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.OBJECT },
			{ SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
					SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public SMinFunction() {
		super("SMIN", 2, accTypes, SDFDatatype.DOUBLE);
	}
	

	@Override
	public Double getValue() {
		@SuppressWarnings("unchecked")
		List<Tuple<?>> tuples = (List<Tuple<?>>) getInputValue(0);
		Integer pos = getNumericalInputValue(1).intValue();
		Tuple<?> minTuple = tuples.get(0).restrict(pos, true);
		Double result = minTuple.getAttribute(0);
		for (Tuple<?> tuple : tuples) {
			Tuple<?> other = tuple.restrict(pos, true);
			if (minTuple.compareTo(other) > 0) {
				minTuple = other;
				result = other.getAttribute(0);
			}
		}
		return result;
	}

}
