package de.uniol.inf.is.odysseus.server.keyvalue.function;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringInputFunction;

public class ToKeyValueFuntion extends AbstractUnaryStringInputFunction<KeyValueObject<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -3642222892242871110L;

	public ToKeyValueFuntion() {
		super("toKeyValue", SDFDatatype.KEYVALUEOBJECT);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getValue() {
		String json = getInputValue(0);
		return KeyValueObject.createInstance(json);
	}

}
