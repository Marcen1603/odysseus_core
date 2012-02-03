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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.unit.SDFUnit;

/**
 * 
 * FIXME: THIS CLASS MUST BE UPDATED TO THE NEW SdfAttributeList that can contain arbitrary objects
 * 
 * In this SDFAttributeList metadata about the schema can be carried.
 * This can be for example PredictionFunctions or something else.
 * 
 * @author Andr� Bolles
 *
 */
@Deprecated
public class SDFAttributeListExtended extends SDFAttributeList implements Serializable{

	private static final long serialVersionUID = -6831412045682783890L;

	private Map<SDFAttributeListMetadataTypes, Object> metadata;

	private SDFDatatype datatypeHACK;

	private SDFUnit unitHACK;
	
	public SDFAttributeListExtended(String URI) {
		super(URI);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}

	public SDFAttributeListExtended() {
		super("");
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


	private void setUnit(SDFUnit unit) {
		this.unitHACK = unit;
	}

	@Override
	public SDFUnit getUnit() {
		return unitHACK;
	}
	
	private void setDatatype(SDFDatatype datatype) {
		this.datatypeHACK = datatype;
	}
	
	@Override
	public SDFDatatype getDatatype() {
		return datatypeHACK;
	}

	
	
	private SDFAttribute copyDeep(SDFAttribute attribute) {
		SDFAttribute copy = new SDFAttribute(attribute.getSourceName(), attribute.getAttributeName(), attribute.getDatatype(), attribute.getUnit(), null, attribute.getCovariance());
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
		// FIXME: SET SOURCE NAME IS NOT ALLOWED!!
		//attr.setSourceName(newSourceName);
		if(attr.getDatatype().hasSchema()){
			for(SDFAttribute subAttr: attr.getDatatype().getSubSchema()){
				this.redAttrSourceName(subAttr, newSourceName);
			}
		}
	}
	
	public SDFAttributeListExtended(SDFAttribute[] attributes1) {
		super("",attributes1);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}
	
	
	public SDFAttributeListExtended(Collection<SDFAttribute> attributes1) {
		super("",attributes1);
		this.metadata = new HashMap<SDFAttributeListMetadataTypes, Object>();
	}
	
	@Override
	public SDFAttributeListExtended clone(){
		return new SDFAttributeListExtended(this);
	}
	
	@SuppressWarnings("unchecked")
	public <M> M getMetadata(SDFAttributeListMetadataTypes metadataName){
		return (M)this.metadata.get(metadataName);
	}
	
	
	public void setMetadata(SDFAttributeListMetadataTypes metadataName, Object metadata){
		this.metadata.put(metadataName, metadata);
	}
	
	@SuppressWarnings("unchecked")
	public <M> M removeMetadata(SDFAttributeListMetadataTypes metadataName){
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
