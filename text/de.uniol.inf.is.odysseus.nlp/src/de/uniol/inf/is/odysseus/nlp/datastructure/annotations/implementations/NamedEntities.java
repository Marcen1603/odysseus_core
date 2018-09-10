package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;

public class NamedEntities extends Annotation {
	public static final String NAME = "namedentities";

	public NamedEntities(){
	}
	
	public NamedEntities(List<NamedEntity> list){
		for(NamedEntity annotation : list){
			getAnnotations().put(annotation.getType(), annotation);
		}
	}
	
	public void add(NamedEntity annotation){
		getAnnotations().put(annotation.getType(), annotation);
	}
	
	@Override
	public KeyValueObject<IMetaAttribute> toObject() {
    	KeyValueObject<IMetaAttribute> keyvalue = KeyValueObject.createInstance();
    	Map<String, IAnnotation> annotations = getAnnotations();
    	for(Entry<String, IAnnotation> entry : annotations.entrySet()){
    		NamedEntity annotation = (NamedEntity) entry.getValue();
    		keyvalue.setAttribute(annotation.getType(), annotation.toObject());
    	}
		return keyvalue;
	}

	@Override
	public IClone clone(){
		Map<String, IAnnotation> annotations = cloneAnnotations();
		NamedEntities namedentities = new NamedEntities();
		namedentities.annotations = annotations;
		return namedentities;
	}

}
