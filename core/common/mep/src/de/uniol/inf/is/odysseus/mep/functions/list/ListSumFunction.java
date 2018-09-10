package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListSumFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = -3282877303737235603L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists()};

	public ListSumFunction() {
		super("sum", 1, accTypes, SDFDatatype.DOUBLE, false);
	}

	@Override
	public Double getValue() { 
		List<Object> l = getInputValue(0);
		if(l == null) {
			return null;
		}
		double result = 0;
		for (Object v:l){
			if(v != null) {
				result += ((Number)v).doubleValue();
			}
		}
		return result;
	}
		

}
