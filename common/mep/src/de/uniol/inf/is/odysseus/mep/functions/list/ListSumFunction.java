package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListSumFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = -3282877303737235603L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.LISTS};

	public ListSumFunction() {
		super("sum", 1, accTypes, SDFDatatype.DOUBLE, false);
	}

	@Override
	public Double getValue() {
		Object in = getInputValue(0);
		List<Object> l = (List<Object>)in;
		double result = 0;
		for (Object v:l){
			result += ((Number)v).doubleValue();
		}
		return result;
	}
		

}
