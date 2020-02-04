package de.uniol.inf.is.odysseus.mep.commons.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class DBSCANFunction2 extends AbstractDBSCANFunction {

	private static final long serialVersionUID = -2637140703772179352L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFDatatype.LIST_TUPLE},
			{ SDFDatatype.DOUBLE }, SDFDatatype.DISCRETE_NUMBERS, {SDFDatatype.STRING} };

	public DBSCANFunction2() {
		super("DBSCAN", 4, accTypes, SDFDatatype.LIST);
	}

	@Override
	public List<List<Tuple<?>>> getValue() {
		List<Tuple<?>> input = getInputValue(0);
		double eps = getInputValue(1);
		Long minPts = getInputValue(2);
		String restrictTo = getInputValue(3);
		return calcClustering(input, eps, minPts, restrictTo);
	}
}

