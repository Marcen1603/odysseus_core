package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDADimFunction extends AbstractFunction<List<Double>> {

	private static final long serialVersionUID = 6353529315932458688L;

	/*
	 * 1: lower bound 2: upper bound 3: number of borders (incl. upper and
	 * lower)
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.INTEGER } };

	public MDADimFunction() {
		super("MDADim", 3, acceptedTypes, SDFDatatype.LIST_DOUBLE);
	}

	@Override
	public List<Double> getValue() {
		double lower = getInputValue(0);
		double upper = getInputValue(1);
		int count = getInputValue(2);

		return createDimension(lower, upper, count);
	}

	public static List<Double> createDimension(double lower, double upper, int count) {
		List<Double> values = Lists.newArrayList();
		double increasingValue = (upper - lower) / (count - 1); // first and
																// last value
																// included
		if (lower == upper) {
			values.add(lower);
		} else if (lower < upper) {
			for (double value = lower; value <= upper; value += increasingValue) {
				values.add(value);
			}
		} else {
			for (double value = lower; value >= upper; value += increasingValue) {
				values.add(value);
			}
		}
		return values;
	}

}