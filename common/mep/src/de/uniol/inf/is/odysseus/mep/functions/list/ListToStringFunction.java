package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListToStringFunction extends AbstractFunction<String> {

	private static final long serialVersionUID = 8703882849717127378L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.getLists()};

    public ListToStringFunction() {
        super("toString", 1, ACC_TYPES, SDFDatatype.STRING, false);
    }
	
	@Override
	public String getValue() {
        List<?> list = (List<?>) getInputValue(0);        
		return list.toString();
	}

}
