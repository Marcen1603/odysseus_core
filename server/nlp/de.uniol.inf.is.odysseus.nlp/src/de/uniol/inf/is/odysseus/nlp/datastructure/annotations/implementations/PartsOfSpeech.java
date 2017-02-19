package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class PartsOfSpeech extends Annotation {
	private String[] tags;

	public PartsOfSpeech(String[] tags){
		this.tags = tags;
	}
	
	@Override
	public Object toObject() {
		return tags;
	}

}
