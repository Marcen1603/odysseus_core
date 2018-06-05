package de.uniol.inf.is.odysseus.mep.functions.time;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractUnaryDateFunction<T> extends AbstractFunction<T> {

	private static final long serialVersionUID = -1164225253502762180L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.DATE} };

	public AbstractUnaryDateFunction(String symbol, SDFDatatype returnType) {
		super(symbol,1,accTypes,returnType);
	}	
}
