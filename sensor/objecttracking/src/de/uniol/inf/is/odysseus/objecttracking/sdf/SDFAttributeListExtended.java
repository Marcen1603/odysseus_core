package de.uniol.inf.is.odysseus.objecttracking.sdf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * In this SDFAttributeList metadata about the schema can be carried.
 * This can be for example PredictionFunctions or something else.
 * 
 * @author André Bolles
 *
 */
public class SDFAttributeListExtended extends SDFAttributeList implements Serializable{

	private static final long serialVersionUID = -6831412045682783890L;

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
		super(attributes1.getURI());
		this.setDatatype(attributes1.getDatatype());
		this.setUnit(attributes1.getUnit());
		// deep copy for SDFAttributes and the subattributes 
		for(int i=0; i<attributes1.getAttributeCount(); i++) {
			this.add(copyDeep(attributes1.get(i)));
		}
		// copy of schmea metadaten
		if(attributes1.metadata != null){
			this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
			for(Entry<SDFAttributeListMetadataTypes, Object> entry : attributes1.metadata.entrySet()){
				this.metadata.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private SDFAttribute copyDeep(SDFAttribute attribute) {
		SDFAttribute copy = new SDFAttribute(attribute.getSourceName(), attribute.getAttributeName());
		copy.setDatatype(attribute.getDatatype());
		copy.setCovariance(attribute.getCovariance());
		copy.setUnit(attribute.getUnit());
		for(int i=0; i<attribute.getAmountOfSubattributes(); i++) {
			copy.addSubattribute(copyDeep(attribute.getSubattribute(i)));
		}
		return copy;
	}
	
	/**
	 * recursively sets the source name for each attribute to
	 * newSourceName
	 * @param newSourceName
	 */
	public void redefineSourceName(String newSourceName){
		for(int i = 0; i<this.getAttributeCount(); i++){
			this.redAttrSourceName(this.getAttribute(i), newSourceName);
		}
	}
	
	private void redAttrSourceName(SDFAttribute attr, String newSourceName){
		attr.setSourceName(newSourceName);
		for(SDFAttribute subAttr: attr.getSubattributes()){
			this.redAttrSourceName(subAttr, newSourceName);
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
	
	public SDFAttributeListExtended clone(){
		return new SDFAttributeListExtended(this);
	}
	
	public <M> M getMetadata(SDFAttributeListMetadataTypes metadataName){
		return (M)this.metadata.get(metadataName);
	}
	
	
	public void setMetadata(SDFAttributeListMetadataTypes metadataName, Object metadata){
		this.metadata.put(metadataName, metadata);
	}
	
	public <M> M removeMetadata(SDFAttributeListMetadataTypes metadataName){
		return (M)this.metadata.remove(metadataName);
	}
	
	public int[] getMeasurementAttributePositions(){
		List<Integer> pos = new ArrayList<Integer>();
		for(int i = 0; i<this.elements.size(); i++){
			SDFAttribute curAttr = this.elements.get(i);
			if(SDFDatatypes.isMeasurementValue(curAttr.getDatatype())){
				pos.add(i);
			}
		}
		
		int[] mvPos = new int[pos.size()];
		for(int i = 0; i<mvPos.length; i++){
			mvPos[i] = pos.get(i);
		}
		
		return mvPos;
	}
	
	public void clearMetadata(){
		this.metadata.clear();
	}
}
