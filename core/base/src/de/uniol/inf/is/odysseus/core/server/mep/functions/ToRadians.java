package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToRadians extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -616286834459140847L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.DOUBLE, SDFDatatype.BYTE, SDFDatatype.FLOAT,
			SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.STRING };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
        return accTypes;
	}

	@Override
	public String getSymbol() {
		return "ToRadians";
	}

	@Override
	public Double getValue() {
		Double val = Double.parseDouble(getInputValue(0).toString());
		return Math.toRadians(val);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
