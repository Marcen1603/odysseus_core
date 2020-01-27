package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListToStringFunction2 extends AbstractFunction<String> {

	private static final long serialVersionUID = 8703882849717127378L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists(), {SDFDatatype.BOOLEAN}};

    public ListToStringFunction2() {
        super("toString", 2, ACC_TYPES, SDFDatatype.STRING, false);
    }
	
	@Override
	public String getValue() {
        List<?> list = (List<?>) getInputValue(0);
        boolean flatten = getInputValue(1);
        if (flatten) {
        	StringBuilder res = new StringBuilder();
        	for (Object o:list) {
        		res.append(o);
        	}
        	return res.toString();
        }
        
		return list.toString();
	}

}
