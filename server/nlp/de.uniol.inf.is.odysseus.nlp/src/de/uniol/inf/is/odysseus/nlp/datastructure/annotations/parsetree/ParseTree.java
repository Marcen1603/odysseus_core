package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;

public class ParseTree extends ParseNode{
	public ParseTree(String tag, String text, ParseNode... children){
		super(tag, text, children);
	}

	@Override
	public Object toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	keyvalue.setAttribute(this.tag, super.toObject());
    	return keyvalue;
	}

	public String getTag() {
		return tag;
	}

	public String getText() {
		return text;
	}
	
	
}
