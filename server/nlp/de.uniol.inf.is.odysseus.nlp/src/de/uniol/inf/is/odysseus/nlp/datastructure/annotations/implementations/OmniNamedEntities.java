package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;

public class OmniNamedEntities extends Annotation {

	public OmniNamedEntities(){
	}
	
	public OmniNamedEntities(List<NamedEntities> list){
		for(NamedEntities annotation : list){
			getAnnotations().put(annotation.getType(), annotation);
		}
	}
	
	public void add(NamedEntities annotation){
		getAnnotations().put(annotation.getType(), annotation);

	}
	
	@Override
	public KeyValueObject<IMetaAttribute> toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Map<String, IAnnotation> annotations = getAnnotations();
    	for(Entry<String, IAnnotation> entry : annotations.entrySet()){
    		NamedEntities annotation = (NamedEntities) entry.getValue();
    		keyvalue.setAttribute(annotation.getType(), annotation.toObject());
    	}
		return keyvalue;
	}

}
