package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class ParseLeaf extends ParseNode {
	public ParseLeaf(String tag) {
		super(tag);
		// TODO Auto-generated constructor stub
	}

	private int token;

	@Override
	public Object toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	keyvalue.setAttribute(tag, token);
		return keyvalue;
	}
}
