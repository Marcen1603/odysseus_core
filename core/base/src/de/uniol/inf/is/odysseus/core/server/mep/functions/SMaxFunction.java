package de.uniol.inf.is.odysseus.core.server.mep.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class SMaxFunction extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9061250775833636682L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.OBJECT },
			{ SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
					SDFDatatype.INTEGER, SDFDatatype.LONG } };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity()
					+ " argument(s): a tuple list and the position");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "SMAX";
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
			if (minTuple.compareTo(other) < 0) {
				minTuple = other;
				result = other.getAttribute(0);
			}
		}
		return result;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}
}
