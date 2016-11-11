package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KVGetElementFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -7563778697648088501L;
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFKeyValueDatatype.KEYVALUEOBJECT}, {SDFDatatype.STRING}};

	public KVGetElementFunction(){
		super("getElement", 2, acceptedTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		KeyValueObject<?> kv = getInputValue(0);
		String key = getInputValue(1);
		return kv.getAttribute(key);
	}



}
