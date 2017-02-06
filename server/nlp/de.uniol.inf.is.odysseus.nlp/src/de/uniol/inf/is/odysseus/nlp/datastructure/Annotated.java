package de.uniol.inf.is.odysseus.nlp.datastructure;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Objects of this class contain the annotations for a specific text.
 */
public class Annotated extends Annotation{
	/**
	 * Text used for natural language processing.
	 */
	public String text;
	
	public Annotated(String text){
		this.text = text;
	}

	@Override
	public Object toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Map<String, IAnnotation> annotations = getAnnotations();
    	for(Entry<String, IAnnotation> entry : annotations.entrySet()){
    		keyvalue.setAttribute(entry.getKey(), entry.getValue().toObject());
    	}
		return keyvalue;
	}
}
