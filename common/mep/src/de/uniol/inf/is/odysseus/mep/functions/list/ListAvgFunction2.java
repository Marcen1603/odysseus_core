package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListAvgFunction2 extends AbstractFunction<Double> {

	private static final long serialVersionUID = -3282877303737235603L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { {SDFDatatype.LIST_TUPLE}, SDFDatatype.DISCRETE_NUMBERS};

	public ListAvgFunction2() {
		super("avg", 2, accTypes, SDFDatatype.DOUBLE, false);
	}

	@Override
	public Double getValue() {
		List<Tuple<?>> l = getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
 
		if(l == null || l.size()==0) {
			return null;
		}
		double result = 0;
		for (Tuple<?> v:l){
			if(v != null) {
				result += ((Number)v.getAttribute(pos)).doubleValue();
			}
		}
		return result/l.size();
	}


}
