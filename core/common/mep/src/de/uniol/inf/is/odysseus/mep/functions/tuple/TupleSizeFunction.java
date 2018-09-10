package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class TupleSizeFunction extends AbstractFunction<Integer> {

   
	private static final long serialVersionUID = -4057090808971844503L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.TUPLE } };

    public TupleSizeFunction() {
        super("size", 1, ACC_TYPES, SDFDatatype.INTEGER, false);
    }
	
	@Override
	public Integer getValue() {
		Tuple<?> t = getInputValue(0);
		return t.size();
	}

}
