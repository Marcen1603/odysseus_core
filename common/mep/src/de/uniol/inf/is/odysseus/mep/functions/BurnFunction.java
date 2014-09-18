package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class BurnFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 2154239054532126650L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] {
			SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
			SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public BurnFunction() {
    	super("burn",1,accTypes, SDFDatatype.DOUBLE, false);
    }

	@Override
	public Double getValue() {
		int time = getNumericalInputValue(0).intValue();
		long startTS = System.currentTimeMillis();
		
		// BURN CPU
		while( System.currentTimeMillis() - startTS < time );
		
		return 1.0;
	}

}
