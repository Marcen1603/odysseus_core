package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Objects of this class contain the annotations for a specific text.
 */
public class Annotated extends Annotation{
	/**
	 * Text used for natural language processing.
	 */
	private String text;
	private Set<String> information;
	
	public Annotated(String text, Set<String> information){
		this.text = text;
		this.information = information;
	}

	@Override
	public KeyValueObject<IMetaAttribute> toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Map<String, IAnnotation> annotations = getAnnotations();
    	for(Entry<String, IAnnotation> entry : annotations.entrySet()){
    		if(information.contains(entry.getValue().identifier()))
    			keyvalue.setAttribute(entry.getKey(), entry.getValue().toObject());
    	}
		return keyvalue;
	}
}
