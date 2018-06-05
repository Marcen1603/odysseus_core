package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SleepFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 2154239054532126650L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] {
			SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
			SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public SleepFunction() {
    	super("sleep",1,accTypes, SDFDatatype.DOUBLE, false);
    }

	@Override
	public Double getValue() {
		int time = getNumericalInputValue(0).intValue();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return 1.0;
	}

}
