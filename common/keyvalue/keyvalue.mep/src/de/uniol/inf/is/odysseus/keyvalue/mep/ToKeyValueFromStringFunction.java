package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringInputFunction;

public class ToKeyValueFromStringFunction extends AbstractUnaryStringInputFunction<KeyValueObject<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -3642222892242871110L;

	public ToKeyValueFromStringFunction() {
		super("toKeyValue", SDFKeyValueDatatype.KEYVALUEOBJECT);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getValue() {
		String json = getInputValue(0);
		return KeyValueObject.createInstance(json);
	}

}
