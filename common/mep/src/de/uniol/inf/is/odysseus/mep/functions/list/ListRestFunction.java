package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListRestFunction extends AbstractFunction<List<?>> {

	private static final long serialVersionUID = -3114684384693799438L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists()};
	
	public ListRestFunction(){
		super("rest",1,accTypes, SDFDatatype.LIST);
	}

	@Override
	public List<?> getValue() {
		List<?> in = getInputValue(0);
		List<Object> out = new ArrayList<Object>();
		for (int i=1; i<in.size();i++){
			out.add(in.get(i));
		}
		return out;
	}
	
}
