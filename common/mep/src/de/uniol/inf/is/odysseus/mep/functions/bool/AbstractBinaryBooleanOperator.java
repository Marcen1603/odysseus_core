package de.uniol.inf.is.odysseus.mep.functions.bool;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;

abstract public class AbstractBinaryBooleanOperator extends
		AbstractBinaryOperator<Boolean> {

	private static final long serialVersionUID = 5174424163393401545L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{new SDFDatatype[]{ SDFDatatype.BOOLEAN, SDFDatatype.OBJECT },new SDFDatatype[]{ SDFDatatype.BOOLEAN }};
	
	public AbstractBinaryBooleanOperator(String symbol) {
		super(symbol, accTypes, SDFDatatype.BOOLEAN);
	}
}
