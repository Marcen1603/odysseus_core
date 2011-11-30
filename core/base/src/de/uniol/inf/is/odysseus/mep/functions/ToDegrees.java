package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToDegrees extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137250935924025881L;
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
		} else {
			return accTypes;
		}
	}

	@Override
	public String getSymbol() {
		return "ToDegrees";
	}

	@Override
	public Double getValue() {
		Double val = Double.parseDouble(getInputValue(0).toString());
		return Math.toDegrees(val);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
