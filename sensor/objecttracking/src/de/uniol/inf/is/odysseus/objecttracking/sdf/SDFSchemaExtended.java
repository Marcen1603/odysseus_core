/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.sdf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;

/**
 *  
 * In this SDFSchema metadata about the schema can be carried.
 * This can be for example PredictionFunctions or something else.
 * 
 * @author André Bolles
 *
 */
public class SDFSchemaExtended extends SDFSchema implements Serializable{

	private static final long serialVersionUID = -6831412045682783890L;

	private Map<SDFSchemaMetadataTypes, Object> metadata;

	private SDFDatatype datatypeHACK;

	private SDFUnit unitHACK;
	
//	public SDFSchemaExtended(String URI) {
//		super(URI);
//		this.metadata = new HashMap<SDFSchemaMetadataTypes, Object>();
//	}
//
//	public SDFSchemaExtended() {
//		super("");
//		this.metadata = new HashMap<SDFSchemaMetadataTypes, Object>();
//	}

	/**
	 * @param attributes1 The old schema that is to be copied
	 */
	public SDFSchemaExtended(SDFSchemaExtended attributes1) {
		super(attributes1.getURI());
		this.setDatatype(attributes1.getDatatype());
		this.setUnit(attributes1.getUnit());
		// deep copy for SDFAttributes and the subattributes 
		for(int i=0; i<attributes1.size(); i++) {
			this.elements.add(copyDeep(attributes1.get(i)));
		}
		// copy of schmea metadaten
		if(attributes1.metadata != null){
			this.metadata = new HashMap<SDFSchemaMetadataTypes, Object>();
			for(Entry<SDFSchemaMetadataTypes, Object> entry : attributes1.metadata.entrySet()){
				this.metadata.put(entry.getKey(), entry.getValue());
			}
		}
	}


	private void setUnit(SDFUnit unit) {
		this.unitHACK = unit;
	}

	public SDFUnit getUnit() {
		return unitHACK;
	}
	
	private void setDatatype(SDFDatatype datatype) {
		this.datatypeHACK = datatype;
	}
	
	public SDFDatatype getDatatype() {
		return datatypeHACK;
	}

	
	
	private static SDFAttribute copyDeep(SDFAttribute attribute) {
		SDFAttribute copy = new SDFAttribute(attribute.getSourceName(), attribute.getAttributeName(), attribute.getDatatype(), attribute.getUnit(), null, attribute.getAddInfo());
		return copy;
	}
	
//	/**
//	 * recursively sets the source name for each attribute to
//	 * newSourceName
//	 * @param newSourceName
//	 */
//	public SDFSchemaExtended redefineSourceName(String newSourceName){
//		SDFSchemaExtended redefinedSet = new SDFSchemaExtended();
//		
//		for(int i = 0; i<this.size(); i++){
//			redefinedSet.add(redAttrSourceName(this.getAttribute(i), newSourceName));
//		}
//		
//		return redefinedSet;
//	}
//	
//	private SDFAttribute redAttrSourceName(SDFAttribute attr, String newSourceName){
//		SDFAttribute newAttribute = new SDFAttribute(newSourceName, attr.getAttributeName(), attr);
//		if(attr.getDatatype().hasSchema()){
//			
//			for(SDFAttribute subAttr: attr.getDatatype().getSubSchema()){
//				   this.redAttrSourceName(subAttr, newSourceName);
//			}
//		}
//		return newAttribute;
//	}
	
//	public SDFSchemaExtended(SDFAttribute[] attributes1) {
//		super("",attributes1);
//		this.metadata = new HashMap<SDFSchemaMetadataTypes, Object>();
//	}
	
	
	public SDFSchemaExtended(Collection<SDFAttribute> attributes1) {
		super("",attributes1);
		this.metadata = new HashMap<SDFSchemaMetadataTypes, Object>();
	}
	
	@Override
	public SDFSchemaExtended clone(){
		return new SDFSchemaExtended(this);
	}
	
	@SuppressWarnings("unchecked")
	public <M> M getMetadata(SDFSchemaMetadataTypes metadataName){
		return (M)this.metadata.get(metadataName);
	}
	
	
	public void setMetadata(SDFSchemaMetadataTypes metadataName, Object metadata){
		this.metadata.put(metadataName, metadata);
	}
	
	@SuppressWarnings("unchecked")
	public <M> M removeMetadata(SDFSchemaMetadataTypes metadataName){
		return (M)this.metadata.remove(metadataName);
	}
	
	public int[] getMeasurementAttributePositions(){
		List<Integer> pos = new ArrayList<Integer>();
		for(int i = 0; i<this.elements.size(); i++){
			SDFAttribute curAttr = this.elements.get(i);
			if(curAttr.getDatatype().isMeasurementValue()){
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
