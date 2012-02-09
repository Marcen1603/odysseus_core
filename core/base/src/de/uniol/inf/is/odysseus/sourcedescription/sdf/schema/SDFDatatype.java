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

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFDatatype extends SDFElement implements Serializable {

	public static enum KindOfDatatype {
		BASE, TUPLE, MULTI_VALUE, BEAN;
	}

	/**
	 * abstract type describing that each value is allowed
	 */
	public static final SDFDatatype OBJECT = new SDFDatatype("Object");

	/**
	 * Datatype for nested tuple (ckuka) 07/27/2011
	 */
	public static final SDFDatatype TUPLE = new SDFDatatype("Tuple",
			SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.OBJECT);

	/**
	 * predefined datatypes
	 */
	public static final SDFDatatype STRING = new SDFDatatype("String");
	public static final SDFDatatype LONG = new SDFDatatype("Long");
	public static final SDFDatatype INTEGER = new SDFDatatype("Integer");
	public static final SDFDatatype BYTE = new SDFDatatype("Byte");
	public static final SDFDatatype FLOAT = new SDFDatatype("Float");
	public static final SDFDatatype DOUBLE = new SDFDatatype("Double");
	public static final SDFDatatype DATE = new SDFDatatype("Date");
	public static final SDFDatatype BOOLEAN = new SDFDatatype("Boolean");

//	public static final SDFDatatype SPATIAL_POINT = new SDFDatatype(
//			"SpatialPoint", SDFDatatype.KindOfDatatype.BEAN,
//			new SDFAttributeList("Coordinate", new SDFAttribute(null, "x",
//					SDFDatatype.DOUBLE), new SDFAttribute(null, "y",
//					SDFDatatype.DOUBLE), new SDFAttribute(null, "z",
//					SDFDatatype.DOUBLE)));
//
//	public static final SDFDatatype SPATIAL_MULTI_POINT = new SDFDatatype(
//			"SpatialMultiPoint", SDFDatatype.KindOfDatatype.MULTI_VALUE,
//			SDFDatatype.SPATIAL_POINT);
//
//	public static final SDFDatatype SPATIAL_LINE = new SDFDatatype(
//			"SpatialLine", SDFDatatype.KindOfDatatype.BEAN,
//			new SDFAttributeList("Line", new SDFAttribute(null, "start",
//					SDFDatatype.SPATIAL_POINT), new SDFAttribute(null, "end",
//					SDFDatatype.SPATIAL_POINT)));
//
//	public static final SDFDatatype SPATIAL_MULTI_LINE = new SDFDatatype(
//			"SpatialMultiLine", SDFDatatype.KindOfDatatype.MULTI_VALUE,
//			SDFDatatype.SPATIAL_LINE);
//
//	public static final SDFDatatype SPATIAL_POLYGON = new SDFDatatype(
//			"SpatialPolygon", SDFDatatype.KindOfDatatype.BEAN,
//			new SDFAttributeList("Points", new SDFAttribute(null, "points",
//					SDFDatatype.SPATIAL_MULTI_POINT)));
//
//	public static final SDFDatatype SPATIAL_MULTI_POLYGON = new SDFDatatype(
//			"SpatialMultiPolygon", SDFDatatype.KindOfDatatype.MULTI_VALUE,
//			SDFDatatype.SPATIAL_POLYGON);
//
//	/**
//	 * abstract type for spatial objects. Access to subschema is not possible so
//	 * we treat this type as base type.
//	 */
//	public static final SDFDatatype SPATIAL = new SDFDatatype("Spatial");

	public static final SDFDatatype START_TIMESTAMP = new SDFDatatype(
			"StartTimestamp");
	public static final SDFDatatype END_TIMESTAMP = new SDFDatatype(
			"EndTimestamp");
	public static final SDFDatatype TIMESTAMP = new SDFDatatype("Timestamp");

	public static final SDFDatatype POINT_IN_TIME = new SDFDatatype(
			"PointInTime");

	private static final SDFDatatype MV = new SDFDatatype("MV");

	public static final SDFDatatype MATRIX_DOUBLE = new SDFDatatype("Matrix",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	public static final SDFDatatype MATRIX_FLOAT = new SDFDatatype(
			"MatrixFloat", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.FLOAT);
	public static final SDFDatatype MATRIX_BYTE = new SDFDatatype("MatrixByte",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.BYTE);
	public static final SDFDatatype MATRIX_BOOLEAN = new SDFDatatype(
			"MatrixBoolean", SDFDatatype.KindOfDatatype.BASE,
			SDFDatatype.BOOLEAN);

	public static final SDFDatatype VECTOR_DOUBLE = new SDFDatatype("Vector",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	public static final SDFDatatype VECTOR_FLOAT = new SDFDatatype(
			"VectorFloat", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.FLOAT);
	public static final SDFDatatype VECTOR_BYTE = new SDFDatatype("VectorByte",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.BYTE);
	public static final SDFDatatype VECTOR_BOOLEAN = new SDFDatatype(
			"VectorBoolean", SDFDatatype.KindOfDatatype.BASE,
			SDFDatatype.BOOLEAN);

//	public static final SDFDatatype GRID = new SDFDatatype("Grid",
//			SDFDatatype.KindOfDatatype.BEAN,
//			new SDFAttributeList("", new SDFAttribute(null, "origin",
//					SDFDatatype.SPATIAL_POINT), new SDFAttribute(null,
//					"cellsize", SDFDatatype.DOUBLE), new SDFAttribute(null,
//					"grid", SDFDatatype.MATRIX_BYTE)));

	private static final long serialVersionUID = 8585322290347489841L;

	private KindOfDatatype type;

	/**
	 * A user defined datatype must have a schema or a subtype that describes
	 * the structure of the datatype. A schema is used if the type is a record
	 * or a set of complex types that are no beans.
	 */
	private SDFAttributeList schema;

	/**
	 * A user defined set datatype must have a subdatatype or a schema that
	 * defines the type of the set elements. A subtype is used if the subtype is
	 * a bean.
	 */
	private SDFDatatype subType;

	public SDFDatatype(String URI) {
		super(URI);
		this.type = SDFDatatype.KindOfDatatype.BASE;
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFAttributeList schema) {
		super(datatypeName);
		if (type == SDFDatatype.KindOfDatatype.BASE) {
			throw new IllegalArgumentException(
					"Base types must not have a schema.");
		}
		this.type = type;

		if (schema == null) {
			throw new IllegalArgumentException(
					"Complex types must have a schema.");
		}
		this.schema = schema;
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFDatatype subType) {
		super(datatypeName);

		// do not check for set. A matrix can also have a subtype
		// if(type != SDFDatatype.KindOfDatatype.MULTI_VALUE){
		// throw new
		// IllegalArgumentException("This constructor is only for MULTI_VALUE datatypes.");
		// }
		this.type = type;
		this.subType = subType;

		// the schema can be null, if it is a set of
		// base type elements
		if (this.subType.type != SDFDatatype.KindOfDatatype.BASE) {
			this.schema = this.subType.getSubSchema();
		}
	}

	public SDFDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
		if (sdfDatatype.schema != null) {
			this.schema = sdfDatatype.schema.clone();
		}
		this.type = sdfDatatype.type;
		if (sdfDatatype.subType != null) {
			this.subType = sdfDatatype.subType.clone();
		}
	}

	public static void registerDefaultTypes(IDataDictionary dd) {

		dd.addDatatype(SDFDatatype.OBJECT.getURI(), SDFDatatype.OBJECT);
		dd.addDatatype(SDFDatatype.DATE.getURI(), SDFDatatype.DATE);
		dd.addDatatype(SDFDatatype.DOUBLE.getURI(), SDFDatatype.DOUBLE);
		dd.addDatatype(SDFDatatype.END_TIMESTAMP.getURI(),
				SDFDatatype.END_TIMESTAMP);
		dd.addDatatype(SDFDatatype.FLOAT.getURI(), SDFDatatype.FLOAT);
		dd.addDatatype(SDFDatatype.INTEGER.getURI(), SDFDatatype.INTEGER);
		dd.addDatatype(SDFDatatype.LONG.getURI(), SDFDatatype.LONG);
//		dd.addDatatype(SDFDatatype.SPATIAL_LINE.getURI(), SDFDatatype.SPATIAL_LINE);
//		dd.addDatatype(SDFDatatype.SPATIAL_MULTI_LINE.getURI(),
//				SDFDatatype.SPATIAL_MULTI_LINE);
//		dd.addDatatype(SDFDatatype.SPATIAL_MULTI_POINT.getURI(),
//				SDFDatatype.SPATIAL_MULTI_POINT);
//		dd.addDatatype(SDFDatatype.SPATIAL_MULTI_POLYGON.getURI(),
//				SDFDatatype.SPATIAL_MULTI_POLYGON);
//		dd.addDatatype(SDFDatatype.SPATIAL_POINT.getURI(),
//				SDFDatatype.SPATIAL_POINT);
//		dd.addDatatype(SDFDatatype.SPATIAL_POLYGON.getURI(),
//				SDFDatatype.SPATIAL_POLYGON);
//		dd.addDatatype(SDFDatatype.SPATIAL.getURI(), SDFDatatype.SPATIAL);
		dd.addDatatype(SDFDatatype.START_TIMESTAMP.getURI(),
				SDFDatatype.START_TIMESTAMP);
		dd.addDatatype(SDFDatatype.STRING.getURI(), SDFDatatype.STRING);
		dd.addDatatype(SDFDatatype.MV.getURI(), SDFDatatype.MV);
		dd.addDatatype(SDFDatatype.TIMESTAMP.getURI(), SDFDatatype.TIMESTAMP);
		dd.addDatatype(SDFDatatype.BOOLEAN.getURI(), SDFDatatype.BOOLEAN);
//		dd.addDatatype(SDFDatatype.GRID.getURI(), SDFDatatype.GRID);
	}
	
	@Override
	// TODO: sp�ter wieder entfernen!!
	public String getQualName() {
		if (super.getQualName() != null && super.getQualName().length() > 0) {
			return super.getQualName();
		} else {
			return getURI();
		}
	}

	@Override
	public SDFDatatype clone() {
		return new SDFDatatype(this);
	}

	public boolean hasSchema() {
		return this.schema != null;
	}

	public SDFAttributeList getSubSchema() {
		return this.schema;
	}

	public boolean hasSubType() {
		return this.subType != null;
	}

	public SDFDatatype getSubType() {
		return this.subType;
	}

	public void setSubType(SDFDatatype sType) {
		this.subType = sType;
	}

	public boolean isComplex() {
		return this.type == SDFDatatype.KindOfDatatype.MULTI_VALUE
				|| this.type == SDFDatatype.KindOfDatatype.TUPLE
				|| this.type == SDFDatatype.KindOfDatatype.BEAN;
	}

	public boolean isMultiValue() {
		return this.type == SDFDatatype.KindOfDatatype.MULTI_VALUE;
	}

	public boolean isTuple() {
		return this.type == SDFDatatype.KindOfDatatype.TUPLE;
	}

	public boolean isBean() {
		return this.type == SDFDatatype.KindOfDatatype.BEAN;
	}

	public boolean isBase() {
		return this.type == SDFDatatype.KindOfDatatype.BASE;
	}

	public boolean isNumeric() {
		return this.getURI().equals(LONG.getURI())
				|| this.getURI().equals(INTEGER.getURI())
				|| this.getURI().equals(DOUBLE.getURI())
				|| this.getURI().equals(FLOAT.getURI());
	}

	public boolean isDouble() {
		return this.getURI().equals(DOUBLE.getURI());
	}

	public boolean isInteger() {
		return this.getURI().equals(INTEGER.getURI());
	}

	public boolean isLong() {
		return this.getURI().equals(LONG.getURI());
	}

	public boolean isFloat() {
		return this.getURI().equals(FLOAT.getURI());
	}

	public boolean isString() {
		return this.getURI().equals(STRING.getURI());
	}

	public boolean isDate() {
		return this.getURI().equals(DATE.getURI());
	}

	public boolean isMeasurementValue() {
		return this.getURI().equals(MV.getURI());
	}

	public boolean isStartTimestamp() {
		return this.getURI().equals(START_TIMESTAMP.getURI());
	}

	public boolean isEndTimestamp() {
		return this.getURI().equals(END_TIMESTAMP.getURI());
	}

	public boolean isTimestamp() {
		return this.getURI().equals(TIMESTAMP.getURI())
				|| this.isStartTimestamp() || this.isEndTimestamp();
	}

//	public boolean isSpatial() {
//		return this.getURI().equals(SPATIAL.getURI()) || this.isPoint()
//				|| this.isLine() || this.isPolygon() || this.isMultiPoint()
//				|| this.isMultiLine() || this.isMultiPolygon();
//	}
//
//	public boolean isPoint() {
//		return this.getURI().equals(SPATIAL_POINT.getURI());
//	}
//
//	public boolean isLine() {
//		return this.getURI().equals(SPATIAL_LINE.getURI());
//	}
//
//	public boolean isPolygon() {
//		return this.getURI().equals(SPATIAL_POLYGON.getURI());
//	}
//
//	public boolean isMultiPoint() {
//		return this.getURI().equals(SPATIAL_MULTI_POINT.getURI());
//	}
//
//	public boolean isMultiLine() {
//		return this.getURI().equals(SPATIAL_MULTI_LINE.getURI());
//	}
//
//	public boolean isMultiPolygon() {
//		return this.getURI().equals(SPATIAL_MULTI_POLYGON.getURI());
//	}
//
//	public boolean isGrid() {
//		return this.getURI().equals(GRID.getURI());
//	}

	public int getSubattributeCount() {
		if (this.schema == null) {
			return 0;
		} else {
			return this.schema.size();
		}
	}

	public static SDFDatatype min(SDFDatatype left, SDFDatatype right) {

		if (left.compatibleTo(right) && !right.compatibleTo(left)) {
			return right;
		}

		if (!left.compatibleTo(right) && !right.compatibleTo(left)) {
			throw new IllegalArgumentException(
					"left and right are not compatible.");
		}

		return left; // left->right ok -> left ||| right->left ok -> left
	}

	/**
	 * Checks whether this datatype is semantically equal to <code>other</code>
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(SDFDatatype other) {
		if (other == null) {
			return false;
		}
		if (this.getURI() == null && other.getURI() == null) {
			if (this.type == other.type) {
				if (this.subType != null && this.subType.equals(other.subType)) {
					return true;
				}
				if (this.schema != null && other.schema != null
						&& this.schema.equals(other.schema)) {
					return true;
				}
			}
		}

		else if (this.getURI() != null && other.getURI() != null
				&& this.getURI().equals(other.getURI())) {
			return true;
		}

		return false;
	}

	/**
	 * This method checks whether this type can be casted into
	 * <code>other</code>.
	 * 
	 * @param other
	 * @return True, if this type can be casted into <code>other</code>
	 */
	public boolean compatibleTo(SDFDatatype other) {
		if (this.equals(other)) {
			return true;
		}
		// String and Object are only compatible to themselfes
		// else if(left.equals(SDFDatatype.STRING) ||
		// left.equals(SDFDatatype.OBJECT)){
		// return false;
		// }
		else if (this.isInteger() && other.isNumeric()) {
			return true;
		} else if (this.isLong()
				&& (other.isLong() || other.isFloat() || other.isDouble())) {
			return true;
		} else if (this.isFloat() && (other.isFloat() || other.isDouble())) {
			return true;
		} else if (this.isDouble() && other.isDouble()) {
			return true;
		} else if (this.isEndTimestamp()
				&& (other.isEndTimestamp() || other.isLong())) {
			return true;
		} else if (this.isStartTimestamp()
				&& (other.isStartTimestamp() || other.isLong())) {
			return true;
		} else if (this.isTimestamp()
				&& (other.isTimestamp() || other.isLong())) {
			return true;
//		} else if (this.isSpatial() && (other.isSpatial()) || this.isPoint()
//				&& (other.isPoint()) || this.isLine() && (other.isLine())
//				|| this.isPolygon() && other.isPolygon() || this.isMultiPoint()
//				&& other.isMultiPoint() || this.isMultiLine()
//				&& other.isMultiLine() || this.isMultiPolygon()
//				&& other.isMultiPolygon()) {
//			return true;
//		} else if (this.isGrid() && other.isGrid()) {
//			return true;
		}
		return false;
	}


}