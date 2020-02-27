package de.uniol.inf.is.odysseus.mep.commons.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class DBSCANFunction extends AbstractDBSCANFunction {

	private static final long serialVersionUID = -2637140703772179352L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFDatatype.LIST_TUPLE},
			{ SDFDatatype.DOUBLE }, SDFDatatype.DISCRETE_NUMBERS };

	public DBSCANFunction() {
		super(3, accTypes);
	}

	@Override
	public List<List<Tuple<?>>> getValue() {
		List<Tuple<?>> input = getInputValue(0);
		double eps = getInputValue(1);
		Long minPts = getInputValue(2);
		return calcClustering(input, eps, minPts, null, false, false);
	}
}

