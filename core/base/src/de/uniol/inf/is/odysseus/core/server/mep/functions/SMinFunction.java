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
public class SMinFunction extends AbstractFunction<Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8052397285858330468L;
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
		return "SMIN";
	}

	@Override
	public Tuple<?> getValue() {
		@SuppressWarnings("unchecked")
		List<Tuple<?>> tuples = (List<Tuple<?>>) getInputValue(0);
		Integer pos = getNumericalInputValue(1).intValue();
		Tuple<?> minTuple = tuples.get(0).restrict(pos, true);
		Tuple<?> result = null;
		for (Tuple<?> tuple : tuples) {
			Tuple<?> other = tuple.restrict(pos, true);
			if (minTuple.compareTo(other) > 0) {
				minTuple = other;
				result = tuple;
			}
		}
		return result;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.TUPLE;
	}
}
