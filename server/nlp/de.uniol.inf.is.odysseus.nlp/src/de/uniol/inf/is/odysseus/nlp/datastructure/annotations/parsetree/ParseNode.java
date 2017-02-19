package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class ParseNode {
	
	protected String tag;
	private ParseNode[] children;

	public ParseNode(String tag){
		this.tag = tag;
	}
	
	public ParseNode(String tag, ParseNode... children){
		this(tag);
		this.children = children;
	}
	
	public Object toObject(){
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Object[] childrenObjects = new Object[children.length];
    	for(int i = 0; i < children.length; i++){
    		childrenObjects[i] = children[i].toObject();
    	}
    	keyvalue.setAttribute(tag, children);
		return keyvalue;
	}
}
