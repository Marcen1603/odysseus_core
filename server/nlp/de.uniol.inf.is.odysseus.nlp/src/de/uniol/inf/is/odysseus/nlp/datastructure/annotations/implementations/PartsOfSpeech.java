package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;

public class PartsOfSpeech extends Annotation {
	private Map<Integer, String[]> tags;

	public PartsOfSpeech(Map<Integer, String[]> tags){
		this.tags = tags;
	}
	
	@Override
	public KeyValueObject<IMetaAttribute> toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	for(Entry<Integer, String[]> entry : tags.entrySet()){
    		keyvalue.setAttribute(entry.getKey().toString(), entry.getValue());
    	}
		return keyvalue;
	}

}
