package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

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


	@Override
	public IClone clone(){
		ParseNode[] newNodes = new ParseNode[children.length];
		for(int i = 0; i < children.length; i++)
			newNodes[i] = (ParseNode) children[i].clone();
		return new ParseTree(tag, text, newNodes);
	}
	
	
}
