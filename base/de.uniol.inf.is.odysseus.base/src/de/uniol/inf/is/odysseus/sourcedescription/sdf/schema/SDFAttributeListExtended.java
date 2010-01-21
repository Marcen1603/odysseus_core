package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * In this SDFAttributeList metadata about the schema can be carried.
 * This can be for example PredictionFunctions or something else.
 * 
 * @author André Bolles
 *
 */
public class SDFAttributeListExtended extends SDFAttributeList {

	private Map<SDFAttributeListMetadataTypes, Object> metadata;
	
	public SDFAttributeListExtended(String URI) {
		super(URI);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}

	public SDFAttributeListExtended() {
		super();
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}

	/**
	 * @param attributes1 The old schema that is to be copied
	 */
	public SDFAttributeListExtended(SDFAttributeListExtended attributes1) {
		super(attributes1);
		if(attributes1.metadata != null){
			this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
			for(Entry<SDFAttributeListMetadataTypes, Object> entry : attributes1.metadata.entrySet()){
				this.metadata.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public SDFAttributeListExtended(SDFAttribute[] attributes1) {
		super(attributes1);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}
	
	
	public SDFAttributeListExtended(Collection<SDFAttribute> attributes1) {
		super(attributes1);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}
	
	public SDFAttributeList clone(){
		return new SDFAttributeListExtended(this);
	}
	
	public Object getMetadata(SDFAttributeListMetadataTypes metadataName){
		return this.metadata.get(metadataName);
	}
	
	public void setMetadata(SDFAttributeListMetadataTypes metadataName, Object metadata){
		this.metadata.put(metadataName, metadata);
	}
	
	public Object removeMetadata(SDFAttributeListMetadataTypes metadataName){
		return this.metadata.remove(metadataName);
	}
	
	public void clearMetadata(){
		this.metadata.clear();
	}
}
