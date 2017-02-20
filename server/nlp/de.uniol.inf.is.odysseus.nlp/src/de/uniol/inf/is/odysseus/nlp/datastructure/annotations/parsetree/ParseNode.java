package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public class ParseNode {
	
	protected String tag;
	private ParseNode[] children;
	protected String text;

	public ParseNode(String tag, String text){
		this.tag = tag;
		this.text = text;
	}
	
	public ParseNode(String tag, String text, ParseNode... children){
		this(tag, text);
		this.children = children;
	}
	
	public Object toObject(){
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	keyvalue.setAttribute("tag", tag);
    	Object[] childrenObjects = new Object[children.length];
    	for(int i = 0; i < children.length; i++){
    		keyvalue.setAttribute(children[i].text, children[i].toObject());
    		childrenObjects[i] = children[i].toObject();
    	}
		return keyvalue;
	}
}
