/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;

/**
 * This class represents data types. For some default data types there exists
 * static implementations. Data types can be simple (BASE), Lists (MULTI_VALUE)
 * or complex (TUPLE, BEAN).
 *
 * @author Marco Grawunder
 */
public class SDFDatatype extends SDFElement implements Serializable {

	/**
	 * This enumerations contains the available type constructors.
	 */
	public static enum KindOfDatatype {
		BASE, TUPLE, MULTI_VALUE, BEAN, LIST, GENERIC;
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

	public static final SDFDatatype NTUPLE = new SDFDatatype("NTuple",
			SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.OBJECT);

	public static final SDFDatatype TRIPLE = new SDFDatatype("Triple",
			SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.OBJECT);

	/**
	 * predefined datatypes
	 */
	public static final SDFDatatype STRING = new SDFDatatype("String");
	public static final SDFDatatype DSTRING = new SDFDatatype("DString");
	public static final SDFDatatype DOCUMENT = new SDFDatatype("Document");
	public static final SDFDatatype LONG = new SDFDatatype("Long");
	public static final SDFDatatype INTEGER = new SDFDatatype("Integer");
	public static final SDFDatatype UNSIGNEDINT16 = new SDFDatatype(
			"UnsignedInt16");
	public static final SDFDatatype BYTE = new SDFDatatype("Byte");
	public static final SDFDatatype CHAR = new SDFDatatype("Char");
	public static final SDFDatatype FLOAT = new SDFDatatype("Float");
	public static final SDFDatatype DOUBLE = new SDFDatatype("Double");
	public static final SDFDatatype DATE = new SDFDatatype("Date");
	public static final SDFDatatype PERIOD = new SDFDatatype("Period");
	public static final SDFDatatype BOOLEAN = new SDFDatatype("Boolean");
	public static final SDFDatatype SHORT = new SDFDatatype("Short");
	public static final SDFDatatype BITVECTOR = new SDFDatatype("BitVector");
	public static final SDFDatatype BYTEBUFFER = new SDFDatatype("ByteBuffer");
	public static final SDFDatatype HEXSTRING = new SDFDatatype("HexString");
	public static final SDFDatatype COMMAND = new SDFDatatype("Command");

	public static final SDFDatatype START_TIMESTAMP = new SDFDatatype(
			"StartTimestamp");
	public static final SDFDatatype START_TIMESTAMP_STRING = new SDFDatatype(
			"StartTimestampString");
	public static final SDFDatatype END_TIMESTAMP = new SDFDatatype(
			"EndTimestamp");
	public static final SDFDatatype END_TIMESTAMP_STRING = new SDFDatatype(
			"EndTimestampString");
	public static final SDFDatatype TIMESTAMP = new SDFDatatype("Timestamp");

	public static final SDFDatatype POINT_IN_TIME = new SDFDatatype(
			"PointInTime");

	private static final SDFDatatype MV = new SDFDatatype("MV");
	public static final SDFDatatype LIST = new SDFDatatype("List",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.OBJECT);

	public static final SDFDatatype LIST_STRING = new SDFDatatype(
			"List_String", SDFDatatype.KindOfDatatype.LIST, SDFDatatype.STRING);
	public static final SDFDatatype LIST_LONG = new SDFDatatype("List_Long",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.LONG);
	public static final SDFDatatype LIST_INTEGER = new SDFDatatype(
			"List_Integer", SDFDatatype.KindOfDatatype.LIST,
			SDFDatatype.INTEGER);
	public static final SDFDatatype LIST_BYTE = new SDFDatatype("List_Byte",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.BYTE);
	public static final SDFDatatype LIST_CHAR = new SDFDatatype("List_Char",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.CHAR);
	public static final SDFDatatype LIST_FLOAT = new SDFDatatype("List_Float",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.FLOAT);
	public static final SDFDatatype LIST_DOUBLE = new SDFDatatype(
			"List_Double", SDFDatatype.KindOfDatatype.LIST, SDFDatatype.DOUBLE);
	public static final SDFDatatype LIST_DATE = new SDFDatatype("List_Date",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.DATE);
	public static final SDFDatatype LIST_BOOLEAN = new SDFDatatype(
			"List_Boolean", SDFDatatype.KindOfDatatype.LIST,
			SDFDatatype.BOOLEAN);
	public static final SDFDatatype LIST_SHORT = new SDFDatatype("List_Short",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.SHORT);
	public static final SDFDatatype LIST_TUPLE = new SDFDatatype("List_Tuple",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.TUPLE);
	public static final SDFDatatype LIST_LIST = new SDFDatatype("List_List",
			SDFDatatype.KindOfDatatype.LIST, SDFDatatype.LIST);
	private static final SDFDatatype[] LISTS = new SDFDatatype[] { LIST,
			LIST_STRING, LIST_LONG, LIST_INTEGER, LIST_BYTE, LIST_CHAR,
			LIST_FLOAT, LIST_DOUBLE, LIST_DATE, LIST_BOOLEAN, LIST_SHORT, LIST_TUPLE, LIST_LIST };

	public static final SDFDatatype MATRIX_DOUBLE = new SDFDatatype("Matrix",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	public static final SDFDatatype MATRIX_INTEGER = new SDFDatatype("Matrix",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.INTEGER);
	public static final SDFDatatype MATRIX_FLOAT = new SDFDatatype(
			"MatrixFloat", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.FLOAT);
	public static final SDFDatatype MATRIX_BYTE = new SDFDatatype("MatrixByte",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.BYTE);
	public static final SDFDatatype MATRIX_SHORT = new SDFDatatype("MatrixShort",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.SHORT);
	public static final SDFDatatype MATRIX_LONG = new SDFDatatype("MatrixLong",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.LONG);
	public static final SDFDatatype MATRIX_BOOLEAN = new SDFDatatype(
			"MatrixBoolean", SDFDatatype.KindOfDatatype.BASE,
			SDFDatatype.BOOLEAN);
	public static final SDFDatatype[] MATRIXS = new SDFDatatype[] {
			SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE,
			SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE };

	public static final SDFDatatype VECTOR_DOUBLE = new SDFDatatype("Vector",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.DOUBLE);
	public static final SDFDatatype VECTOR_FLOAT = new SDFDatatype(
			"VectorFloat", SDFDatatype.KindOfDatatype.BASE, SDFDatatype.FLOAT);
	public static final SDFDatatype VECTOR_BYTE = new SDFDatatype("VectorByte",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.BYTE);
	public static final SDFDatatype VECTOR_SHORT = new SDFDatatype("VectorShort",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.SHORT);
	public static final SDFDatatype VECTOR_INTEGER = new SDFDatatype("VectorInteger",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.INTEGER);
	public static final SDFDatatype VECTOR_LONG = new SDFDatatype("VectorLong",
			SDFDatatype.KindOfDatatype.BASE, SDFDatatype.LONG);
	public static final SDFDatatype VECTOR_BOOLEAN = new SDFDatatype(
			"VectorBoolean", SDFDatatype.KindOfDatatype.BASE,
			SDFDatatype.BOOLEAN);
	public static final SDFDatatype[] VECTORS = new SDFDatatype[] {
			SDFDatatype.VECTOR_BOOLEAN, SDFDatatype.VECTOR_BYTE,
			SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE };
	
	public static final SDFDatatype OBJECT_MAP = new SDFDatatype("ObjectMap");

	/**
	 * Datatypes for aggregations (partial aggregates
	 */
	public static final SDFDatatype PARTIAL_AGGREGATE = new SDFDatatype(
			"PartialAggregate");
	public static final SDFDatatype AVG_SUM_PARTIAL_AGGREGATE = new SDFDatatype(
			"AvgSumPartialAggregate");
	public static final SDFDatatype COUNT_PARTIAL_AGGREGATE = new SDFDatatype(
			"CountPartialAggregate");
	public static final SDFDatatype RELATIONAL_ELEMENT_PARTIAL_AGGREGATE = new SDFDatatype(
			"RelationalElementPartialAggregate");
	public static final SDFDatatype LIST_PARTIAL_AGGREGATE = new SDFDatatype(
			"ListPartialAggregate");
	public static final SDFDatatype MEDIAN_PARTIAL_AGGREGATE = new SDFDatatype(
			"MedianPartialAggregate");
	public static final SDFDatatype STDDEV_PARTIAL_AGGREGATE = new SDFDatatype(
			"StdDevPartialAggregate");
	public static final SDFDatatype CORR_PARTIAL_AGGREGATE = new SDFDatatype(
			"CorrPartialAggregate");
	public static final SDFDatatype COV_PARTIAL_AGGREGATE = new SDFDatatype(
			"CovPartialAggregate");
	public static final SDFDatatype VAR_PARTIAL_AGGREGATE = new SDFDatatype(
			"VarPartialAggregate");

	public static final SDFDatatype[] DISCRETE_NUMBERS = new SDFDatatype[] {
			BYTE, SHORT, INTEGER, LONG, START_TIMESTAMP, END_TIMESTAMP,
			TIMESTAMP, UNSIGNEDINT16 };

	public static final SDFDatatype[] FLOATING_NUMBERS = new SDFDatatype[] {
			FLOAT, DOUBLE };

	public static final SDFDatatype[] NUMBERS = new SDFDatatype[DISCRETE_NUMBERS.length+FLOATING_NUMBERS.length];
	static{
		int i = 0;
		for (SDFDatatype s:DISCRETE_NUMBERS){
			NUMBERS[i++] = s;
		}
		for (SDFDatatype s:FLOATING_NUMBERS){
			NUMBERS[i++] = s;
		}
	}

	public static final SDFDatatype[] NUMBERS_OBJECT = new SDFDatatype[NUMBERS.length+1] ;
	static{
		int i = 0;
		for (SDFDatatype s:NUMBERS){
			NUMBERS_OBJECT[i++] = s;
		}
		NUMBERS_OBJECT[i++] = OBJECT;
	}

	public static final SDFDatatype[] LONG_NUMBERS = new SDFDatatype[] { LONG,
			START_TIMESTAMP, END_TIMESTAMP, TIMESTAMP };

	public static final SDFDatatype[] SIMPLE_TYPES = new SDFDatatype[] {
			OBJECT, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, START_TIMESTAMP,
			END_TIMESTAMP, TIMESTAMP, STRING, DSTRING };

	static final List<SDFDatatype> types = new ArrayList<>();
	static final Map<String, SDFDatatype> nameMap = new HashMap<>();
	static{

		types.add(SDFDatatype.OBJECT);

		types.add(SDFDatatype.TUPLE);
		types.add(SDFDatatype.NTUPLE);

		types.add(SDFDatatype.DATE);
		types.add(SDFDatatype.DOUBLE);
		types.add(SDFDatatype.END_TIMESTAMP);
		types.add(SDFDatatype.END_TIMESTAMP_STRING);
		types.add(SDFDatatype.FLOAT);
		types.add(SDFDatatype.INTEGER);
		types.add(SDFDatatype.UNSIGNEDINT16);
		types.add(SDFDatatype.LONG);
		types.add(SDFDatatype.START_TIMESTAMP);
		types.add(SDFDatatype.START_TIMESTAMP_STRING);
		types.add(SDFDatatype.SHORT);
		types.add(SDFDatatype.CHAR);
		types.add(SDFDatatype.BYTE);
		types.add(SDFDatatype.BITVECTOR);
		types.add(SDFDatatype.BYTEBUFFER);
		types.add(SDFDatatype.HEXSTRING);

		for (SDFDatatype t: LISTS){
			types.add(t);
		}

		types.add(SDFDatatype.STRING);
		types.add(SDFDatatype.DSTRING);
		types.add(SDFDatatype.DOCUMENT);
		types.add(SDFDatatype.MV);
		types.add(SDFDatatype.TIMESTAMP);
		types.add(SDFDatatype.BOOLEAN);

		types.add(SDFDatatype.VECTOR_DOUBLE);
		types.add(SDFDatatype.MATRIX_DOUBLE);

		types.add(SDFDatatype.PARTIAL_AGGREGATE);
		types.add(SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE);
		types.add(SDFDatatype.COUNT_PARTIAL_AGGREGATE);
		types.add(SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE);
		types.add(SDFDatatype.LIST_PARTIAL_AGGREGATE);
		
		types.add(SDFDatatype.OBJECT_MAP);

		for (SDFDatatype t:types){
			nameMap.put(t.getURI().toLowerCase(), t);
		}
	}

	private static final long serialVersionUID = 8585322290347489841L;

	private final KindOfDatatype type;

	/**
	 * A user defined datatype must have a schema or a subtype that describes
	 * the structure of the datatype. A schema is used if the type is a record
	 * or a set of complex types that are no beans.
	 */
	private SDFSchema schema;

	/**
	 * A user defined set datatype must have a subdatatype or a schema that
	 * defines the type of the set elements. A subtype is used if the subtype is
	 * a bean.
	 */
	private SDFDatatype subType;
	/**
	 * State whether this {@link SDFDatatype} requires a deep clone during
	 * transfer
	 */
	private final boolean requiresDeepClone;

	public SDFDatatype(String URI, boolean requiresDeepClone) {
		super(URI);
		this.type = SDFDatatype.KindOfDatatype.BASE;
		this.requiresDeepClone = requiresDeepClone;
	}

	public SDFDatatype(String URI) {
		this(URI, false);
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFSchema schema, boolean requiresDeepClone) {
		super(datatypeName);
//		if (schema != null && type == SDFDatatype.KindOfDatatype.BASE) {
//			throw new IllegalArgumentException(
//					"Base types must not have a schema. "+datatypeName+" "+schema);
//		}
		this.type = type;

//		if (schema == null && type != SDFDatatype.KindOfDatatype.BASE) {
//			throw new IllegalArgumentException(
//					"Complex types must have a schema.");
//		}
		this.schema = schema;
		this.requiresDeepClone = requiresDeepClone;
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFSchema schema) {
		this(datatypeName, type, schema, false);
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFDatatype subType, boolean requiresDeepClone) {
		super(datatypeName);

		this.type = type;
		this.subType = subType;

		// the schema can be null, if it is a set of
		// base type elements
		if (this.subType.type != SDFDatatype.KindOfDatatype.BASE) {
			this.schema = this.subType.getSchema();
		}
		this.requiresDeepClone = requiresDeepClone;
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			SDFDatatype subType) {
		this(datatypeName, type, subType, false);
	}

	public SDFDatatype(String datatypeName, SDFDatatype.KindOfDatatype type,
			Collection<SDFAttribute> attributes) {
		this(datatypeName, type, SDFSchemaFactory.createNewSchema("", Tuple.class, attributes), false);
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
		this.requiresDeepClone = sdfDatatype.requiresDeepClone;
	}

	public SDFDatatype(String uri, KindOfDatatype type, SDFDatatype subType, SDFSchema subSchema) {
		this(uri,type,subSchema);
		this.subType = subType;
	}

	public static List<SDFDatatype> getTypes() {
		return types;
	}

	public static SDFDatatype getType(String name) {
		return nameMap.get(name.toLowerCase());
	}

	@Override
	// TODO: sp�ter wieder entfernen!!
	public String getQualName() {
		if (super.getQualName() != null && super.getQualName().length() > 0) {
			return super.getQualName();
		}
		return getURI();
	}

	@Override
	public SDFDatatype clone() {
		return new SDFDatatype(this);
	}

	public boolean hasSchema() {
		return this.schema != null;
	}

	public SDFSchema getSchema() {
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

	public SDFDatatype.KindOfDatatype getType(){
		return type;
	}

	public boolean isComplex() {
		return this.type == SDFDatatype.KindOfDatatype.MULTI_VALUE
				|| this.type == SDFDatatype.KindOfDatatype.TUPLE
				|| this.type == SDFDatatype.KindOfDatatype.BEAN;
	}

	public boolean isListValue() {
		return this.type == SDFDatatype.KindOfDatatype.LIST;
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
				|| this.getURI().equals(FLOAT.getURI())
				|| this.getURI().equals(SHORT.getURI())
				|| this.getURI().equals(TIMESTAMP.getURI())
				|| this.getURI().equals(POINT_IN_TIME.getURI())
				|| this.getURI().equals(START_TIMESTAMP.getURI())
				|| this.getURI().equals(END_TIMESTAMP.getURI());
	}

	public boolean isPartialAggregate() {
		return this.getURI().equals(PARTIAL_AGGREGATE.getURI())
				|| this.getURI().equals(AVG_SUM_PARTIAL_AGGREGATE.getURI())
				|| this.getURI().equals(COUNT_PARTIAL_AGGREGATE.getURI())
				|| this.getURI().equals(
						RELATIONAL_ELEMENT_PARTIAL_AGGREGATE.getURI())
				|| this.getURI().equals(LIST_PARTIAL_AGGREGATE.getURI());
	}

    public boolean isBoolean() {
        return this.getURI().equals(BOOLEAN.getURI());
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

	public boolean isShort() {
		return this.getURI().equals(SHORT.getURI());
	}

	public boolean isByte() {
		return this.getURI().equals(BYTE.getURI());
	}

	public boolean isByteBuffer() {
		return this.getURI().equals(BYTEBUFFER.getURI());
	}

    public boolean isChar() {
        return this.getURI().equals(CHAR.getURI());
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

	public int getSubattributeCount() {
		if (this.schema == null) {
			return 0;
		}
		return this.schema.size();
	}

	/**
	 * State if the function returns a complex object that requires a deep clone
	 * during transfer. Default should be false.
	 *
	 * @return true, if the output requires deep clone
	 */
	public boolean requiresDeepClone() {
		return this.requiresDeepClone;
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
		if (other == null) {
			return false;
		}
		if (this.equals(other)) {
			return true;
		} else if (this.isInteger() && other.isNumeric()) {
			return true;
		} else if (this.isShort() && other.isNumeric()) {
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
		}
		return false;
	}

	@Override
	public String toString() {
		if (type == KindOfDatatype.GENERIC) {
			return super.toString() + "<" + getSubType() + ">";
		} else if (type == KindOfDatatype.LIST){
			return KindOfDatatype.LIST+ "<" + getSubType() + ">";
		} else {
			return super.toString();
		}
	}

	public static SDFDatatype createTypeWithSubSchema(SDFDatatype type, SDFSchema subSchema) {

		if (type.isListValue()){
			if (type.subType.isTuple()){
				return new SDFDatatype(type.getURI(), KindOfDatatype.LIST, type.subType, subSchema);
			}
		}
		if (type.isTuple()){
			return new SDFDatatype(type.getURI(), KindOfDatatype.TUPLE, subSchema);
		}
		return new SDFDatatype(type.getURI(), type.type, subSchema);
	}

	public static SDFDatatype createTypeWithSubSchema(SDFDatatype type, SDFDatatype subType, SDFSchema subSchema) {
		if (type.isListValue()){
			if (subType.isTuple()){
				return new SDFDatatype(type.getURI(), KindOfDatatype.LIST, subType, subSchema);
			}else{
				return new SDFDatatype(type.getURI(), KindOfDatatype.LIST, subType);
			}
		}
		if (type.isTuple()){
			return new SDFDatatype(type.getURI(), KindOfDatatype.TUPLE, subSchema);
		}
		return new SDFDatatype(type.getURI(), type.type, subSchema);

	}

	public static final SDFDatatype[] getLists(){
		///return Arrays.copyOf(LISTS, LISTS.length);
		// Copy should not be necessary anymore
		return LISTS;
	}
	

}