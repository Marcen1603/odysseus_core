package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree;

public class ParseTree extends ParseNode{
	public ParseTree(String tag, ParseNode... children){
		super(tag, children);
	}
}
