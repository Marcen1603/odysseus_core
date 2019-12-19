package de.uniol.inf.is.odysseus.mep.distance;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class EuclideanDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = 4317327586037864094L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS_OBJECT,
			SDFDatatype.NUMBERS_OBJECT, SDFDatatype.NUMBERS_OBJECT, SDFDatatype.NUMBERS_OBJECT };

	public EuclideanDistance() {
		super("eDistance", 4, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		Number x1 = getInputValue(0);
		Number y1 = getInputValue(1);
		Number x2 = getInputValue(2);
		Number y2 = getInputValue(3);
		return Math.sqrt(
				Math.pow(x1.doubleValue() - x2.doubleValue(), 2) + Math.pow(y1.doubleValue() - y2.doubleValue(), 2));
	}

}
