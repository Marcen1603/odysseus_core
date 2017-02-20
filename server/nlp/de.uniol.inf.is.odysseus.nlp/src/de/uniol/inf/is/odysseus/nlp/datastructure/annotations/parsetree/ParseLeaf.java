package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

public class ParseLeaf extends ParseNode {
	public ParseLeaf(String tag, String text) {
		super(tag, text);
	}


	@Override
	public Object toObject() {
		return tag;
	}
}
