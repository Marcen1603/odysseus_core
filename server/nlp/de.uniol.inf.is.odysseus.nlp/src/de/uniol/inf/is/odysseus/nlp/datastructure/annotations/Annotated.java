package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;
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
	private List<String> models;
	
	public Annotated(String text, List<String> models){
		this.text = text;
		this.models = models;
	}

	@Override
	public KeyValueObject<IMetaAttribute> toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Map<String, IAnnotation> annotations = getAnnotations();
    	for(Entry<String, IAnnotation> entry : annotations.entrySet()){
    		if(models.contains(entry.getValue().identifier()))
    			keyvalue.setAttribute(entry.getKey(), entry.getValue().toObject());
    	}
    	keyvalue.setAttribute("text", text);
		return keyvalue;
	}

	public String getText() {
		return text;
	}
	

	@Override
	public IClone clone(){
		Map<String, IAnnotation> annotations = cloneAnnotations();
		Annotated annotated = new Annotated(text, new ArrayList<String>(models));
		annotated.annotations = annotations;
		return annotated;
	}
}
