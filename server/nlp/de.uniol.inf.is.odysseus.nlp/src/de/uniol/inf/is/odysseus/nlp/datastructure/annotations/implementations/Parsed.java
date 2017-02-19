package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseTree;

public class Parsed extends Annotation {
	public static final String NAME = "parsed";
	
	private ParseTree parseTree;

	public Parsed(ParseTree parseTree){
		this.parseTree = parseTree;
	}
	
	@Override
	public Object toObject() {
		return parseTree.toObject();
	}

}
