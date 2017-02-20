package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class ParseLeaf extends ParseNode {
	public ParseLeaf(String tag, String text) {
		super(tag, text);
	}


	@Override
	public Object toObject() {
		return tag;
	}
}
