package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

import de.uniol.inf.is.odysseus.core.IClone;

public class ParseLeaf extends ParseNode {
	public ParseLeaf(String tag, String text) {
		super(tag, text);
	}


	@Override
	public Object toObject() {
		return tag;
	}
	

	@Override
	public IClone clone(){
		return new ParseLeaf(tag, text);
	}
}
