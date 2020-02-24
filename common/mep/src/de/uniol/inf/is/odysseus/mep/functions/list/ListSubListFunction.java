package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListSubListFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -3114684384693799438L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists(),
			SDFDatatype.DISCRETE_NUMBERS, SDFDatatype.DISCRETE_NUMBERS };

	public ListSubListFunction() {
		super("sublist", 3, accTypes, SDFDatatype.LIST);
	}

	@Override
	public List<?> getValue() {
		List<?> in = getInputValue(0);
		if(in == null) {
			return null;
		}
		Number from = getInputValue(1);
		Number to = getInputValue(2);
		return in.subList(from.intValue(), to.intValue());
		/*List<Object> out = new ArrayList<Object>();
		for (int i=from.intValue(); i<=to.intValue();i++){
			out.add(in.get(i));
		}
		return out;*/
	}
	
	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		return args[0].getReturnType();
	}
	
	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

}
