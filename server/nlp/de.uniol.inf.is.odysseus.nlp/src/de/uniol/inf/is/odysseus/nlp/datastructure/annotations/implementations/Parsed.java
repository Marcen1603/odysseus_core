package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseTree;

public class Parsed extends Annotation {
	public static final String NAME = "parsed";
	
	private ParseTree[] parseTrees;

	public Parsed(ParseTree[] parseTrees){
		this.parseTrees = parseTrees;
	}
	
	@Override
	public Object toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
		for(int i = 0; i < parseTrees.length; i++){
			keyvalue.setAttribute(parseTrees[i].getText(), parseTrees[i].toObject());
		}
		return keyvalue;
	}

}
