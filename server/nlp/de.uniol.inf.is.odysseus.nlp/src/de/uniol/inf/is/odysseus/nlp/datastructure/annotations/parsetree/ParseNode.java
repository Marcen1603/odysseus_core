package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IKeyValueObject;

public class ParseNode implements IKeyValueObject, IClone{
	
	protected String tag;
	protected ParseNode[] children;
	protected String text;

	public ParseNode(String tag, String text){
		this.tag = tag;
		this.text = text;
	}
	
	public ParseNode(String tag, String text, ParseNode... children){
		this(tag, text);
		this.children = children;
	}
	
	@Override
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
	
	public String getTag() {
		return tag;
	}

	public String getText() {
		return text;
	}

	@Override
	public IClone clone(){
		ParseNode[] newNodes = new ParseNode[children.length];
		for(int i = 0; i < children.length; i++)
			newNodes[i] = (ParseNode) children[i].clone();
		return new ParseNode(tag, text, newNodes);
	}
}
