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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFDatatype extends SDFElement implements Serializable{
	
	public static enum KindOfDatatype{
		BASE, TUPLE, SET, LIST, BEAN;
	}

	/**
	 * abstract type describing that each value is allowed
	 */
	public static final SDFDatatype OBJECT = new SDFDatatype("Object");
	
	/**
	 * predefined datatypes
	 */
	public static final SDFDatatype STRING = new SDFDatatype("String");
	public static final SDFDatatype LONG = new SDFDatatype("Long");
	public static final SDFDatatype INTEGER = new SDFDatatype("Integer");
	public static final SDFDatatype FLOAT = new SDFDatatype("Float");
	public static final SDFDatatype DOUBLE = new SDFDatatype("Double");
	public static final SDFDatatype DATE = new SDFDatatype("Date");
	public static final SDFDatatype BOOLEAN = new SDFDatatype("Boolean");
	
	public static final SDFDatatype SPATIAL_POINT = new SDFDatatype("SpatialPoint", SDFDatatype.KindOfDatatype.BEAN, 
			new SDFAttributeList(
					new SDFAttribute("x", SDFDatatype.DOUBLE),
					new SDFAttribute("y", SDFDatatype.DOUBLE),
					new SDFAttribute("z", SDFDatatype.DOUBLE)));
	
	public static final SDFDatatype SPATIAL_MULTI_POINT = new SDFDatatype("SpatialMultiPoint", SDFDatatype.KindOfDatatype.SET, SDFDatatype.SPATIAL_POINT);
	
	
	public static final SDFDatatype SPATIAL_LINE = new SDFDatatype("SpatialLine", SDFDatatype.KindOfDatatype.BEAN,
			new SDFAttributeList(
					new SDFAttribute("start", SDFDatatype.SPATIAL_POINT),
					new SDFAttribute("end", SDFDatatype.SPATIAL_POINT)));
	
	public static final SDFDatatype SPATIAL_MULTI_LINE = new SDFDatatype("SpatialMultiLine", SDFDatatype.KindOfDatatype.SET, SDFDatatype.SPATIAL_LINE);
	
	
	public static final SDFDatatype SPATIAL_POLYGON = new SDFDatatype("SpatialPolygon", SDFDatatype.KindOfDatatype.BEAN,
			new SDFAttributeList(
					new SDFAttribute("points", SDFDatatype.SPATIAL_MULTI_POINT)));
	
	public static final SDFDatatype SPATIAL_MULTI_POLYGON = new SDFDatatype("SpatialMultiPolygon", SDFDatatype.KindOfDatatype.SET, SDFDatatype.SPATIAL_POLYGON);
	
	/**
	 * abstract type for spatial objects. Access to subschema is not
	 * possible so we treat this type as base type.
	 */
	public static final SDFDatatype SPATIAL = new SDFDatatype("Spatial");
	
	public static final SDFDatatype START_TIMESTAMP = new SDFDatatype("StartTimestamp");
	public static final SDFDatatype END_TIMESTAMP = new SDFDatatype("EndTimestamp");
	public static final SDFDatatype TIMESTAMP = new SDFDatatype("Timestamp");
	
	public static final SDFDatatype POINT_IN_TIME = new SDFDatatype("PointInTime");
	
	public static final SDFDatatype MV = new SDFDatatype("MV");
	
	public static final SDFDatatype MATRIX_DOUBLE = new SDFDatatype("Matrix", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	
	public static final SDFDatatype VECTOR_DOUBLE = new SDFDatatype("Vector", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	
	private static final long serialVersionUID = 8585322290347489841L;
	
	
	private KindOfDatatype type;
	
	/**
	 * A user defined datatype must have
	 * a schema or a subtype that describes the structure
	 * of the datatype. A schema is used if the type is 
	 * a record or a set of complex types that are no beans.
	 */
	private SDFAttributeList schema;
	
	/**
	 * A user defined set datatype must have a subdatatype
	 * or a schema that defines the type of the set elements.
	 * A subtype is used if the subtype is a bean.
	 */
	private SDFDatatype subType;
	
	public SDFDatatype(String URI) {
		super(URI);
		this.type = SDFDatatype.KindOfDatatype.BASE;
	}
	
	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type, SDFAttributeList schema){
		super(datatypeName);
		if(type == SDFDatatype.KindOfDatatype.BASE){
			throw new IllegalArgumentException("Base types must not have a schema.");
		}
		this.type = type;
		
		if(schema == null){
			throw new IllegalArgumentException("Complex types must have a schema.");
		}
		this.schema = schema;
	}
	
	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type, SDFDatatype subType){
		super(datatypeName);
		
		// do not check for set. A matrix can also have a subtype
//		if(type != SDFDatatype.KindOfDatatype.SET){
//			throw new IllegalArgumentException("This constructor is only for SET datatypes.");
//		}
		this.type = type;
		this.subType = subType;
		
		// the schema can be null, if it is a set of
		// base type elements
		if(this.subType.type != SDFDatatype.KindOfDatatype.BASE){
			this.schema = this.subType.getSubSchema();
		}
	}
	
	public SDFDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
		if(sdfDatatype.schema != null){
			this.schema = sdfDatatype.schema.clone();
		}
		this.type = sdfDatatype.type;
		if(sdfDatatype.subType != null){
			this.subType = sdfDatatype.subType.clone();
		}
	}

	@Override
	// TODO: sp�ter wieder entfernen!!
	public String getQualName() {
		if (super.getQualName() != null && super.getQualName().length() > 0){
			return super.getQualName();
		}else{
			return getURI();
		}
	}
	
	@Override
	public SDFDatatype clone() {
		return new SDFDatatype(this);
	}
	
	public boolean hasSchema(){
		return this.schema != null;
	}
	
	public SDFAttributeList getSubSchema(){
		return this.schema;
	}
	
	public boolean hasSubType(){
		return this.subType != null;
	}
	
	public SDFDatatype getSubType(){
		return this.subType;
	}
	
	public void setSubType(SDFDatatype sType){
		this.subType = sType;
	}
	
	public boolean isComplex(){
		return this.type == SDFDatatype.KindOfDatatype.SET ||
		        this.type == SDFDatatype.KindOfDatatype.LIST ||
				this.type == SDFDatatype.KindOfDatatype.TUPLE ||
				this.type == SDFDatatype.KindOfDatatype.BEAN;
	}
	
	public boolean isSet(){
		return this.type == SDFDatatype.KindOfDatatype.SET;
	}
	
	public boolean isList(){
	    return this.type == SDFDatatype.KindOfDatatype.LIST;
	}
	   
	public boolean isTuple(){
		return this.type == SDFDatatype.KindOfDatatype.TUPLE;
	}
	
	public boolean isBean(){
		return this.type == SDFDatatype.KindOfDatatype.BEAN;
	}
	
	public boolean isBase(){
		return this.type == SDFDatatype.KindOfDatatype.BASE;
	}
	
	public boolean isNumeric(){
		return this.getURI().equals(LONG.getURI()) ||
				this.getURI().equals(INTEGER.getURI()) ||
				this.getURI().equals(DOUBLE.getURI()) ||
				this.getURI().equals(FLOAT.getURI());
	}
	
	public boolean isDouble(){
		return this.getURI().equals(DOUBLE.getURI());
	}
	
	public boolean isInteger(){
		return this.getURI().equals(INTEGER.getURI());
	}
	
	public boolean isLong(){
		return this.getURI().equals(LONG.getURI());
	}
	
	public boolean isFloat(){
		return this.getURI().equals(FLOAT.getURI());
	}
	
	public boolean isString(){
		return this.getURI().equals(STRING.getURI());
	}
	
	public boolean isDate(){
		return this.getURI().equals(DATE.getURI());
	}
	
	public boolean isMeasurementValue(){
		return this.getURI().equals(MV.getURI());
	}
	
	public boolean isStartTimestamp(){
		return this.getURI().equals(START_TIMESTAMP.getURI());
	}
	
	public boolean isEndTimestamp(){
		return this.getURI().equals(END_TIMESTAMP.getURI());
	}
	
	public int getSubattributeCount(){
		if(this.schema == null){
			return 0;
		}
		else{
			return this.schema.size();
		}
	}
}