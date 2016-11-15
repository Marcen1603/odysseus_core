package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvSelectFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -4407674363442360209L;

	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
		{ SDFKeyValueDatatype.KEYVALUEOBJECT}, { SDFDatatype.STRING} };

	public KvSelectFunction(){
		super("get", 2, acceptedTypes, SDFDatatype.OBJECT);
	}

	@Override
	public Object getValue() {
		KeyValueObject<?> kv = getInputValue(0);
		String path = getInputValue(1);

		return kv.getAttribute(path);
	}


}
