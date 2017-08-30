package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class AsKeyValueFunction extends AbstractFunction<KeyValueObject<? extends IMetaAttribute>> {

	private static final long serialVersionUID = -3642222892242871110L;

	public AsKeyValueFunction() {
		super("asKeyValue", 1, null, SDFKeyValueDatatype.KEYVALUEOBJECT);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getValue() {
		return getInputValue(0);
	}

}
