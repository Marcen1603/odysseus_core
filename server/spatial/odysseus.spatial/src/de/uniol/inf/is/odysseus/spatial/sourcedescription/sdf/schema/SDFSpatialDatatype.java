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
package de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class SDFSpatialDatatype extends SDFDatatype {

	private static final long serialVersionUID = 6644927317015629981L;

	public SDFSpatialDatatype(String URI) {
		super(URI, true);
	}

	public SDFSpatialDatatype(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	public SDFSpatialDatatype(String datatypeName, KindOfDatatype type, SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public SDFSpatialDatatype(String datatypeName, KindOfDatatype type, SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	public static final SDFDatatype SPATIAL_COORDINATE = new SDFSpatialDatatype("SpatialCoordinate",
			SDFDatatype.KindOfDatatype.BEAN,
			SDFSchemaFactory.createNewTupleSchema("", new SDFAttribute(null, "x", SDFDatatype.DOUBLE, null, null, null),
					new SDFAttribute(null, "y", SDFDatatype.DOUBLE, null, null, null),
					new SDFAttribute(null, "z", SDFDatatype.DOUBLE, null, null, null)));

	public static final SDFDatatype SPATIAL_POLAR_COORDINATE = new SDFSpatialDatatype("SpatialPolarCoordinate",
			SDFDatatype.KindOfDatatype.BEAN,
			SDFSchemaFactory.createNewTupleSchema("", new SDFAttribute(null, "r", SDFDatatype.DOUBLE, null, null, null),
					new SDFAttribute(null, "a", SDFDatatype.DOUBLE, null, null, null)));

	public static final SDFDatatype SPATIAL_COORDINATE_SEQUENCE = new SDFSpatialDatatype("SpatialCoordinateSequence",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_COORDINATE);

	/**
	 * abstract type for spatial objects. Access to subschema is not possible so
	 * we treat this type as base type.
	 */
	public static final SDFDatatype SPATIAL_GEOMETRY = new SDFSpatialDatatype("SpatialGeometry");

	public static final SDFDatatype SPATIAL_GEOMETRY_COLLECTION = new SDFSpatialDatatype("SpatialGeometryCollection",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_GEOMETRY);

	public static final SDFDatatype SPATIAL_POINT = new SDFSpatialDatatype("SpatialPoint",
			SDFDatatype.KindOfDatatype.BEAN,
			SDFSchemaFactory.createNewTupleSchema("",
					new SDFAttribute(null, "coordinate", SDFSpatialDatatype.SPATIAL_COORDINATE, null, null, null),
					new SDFAttribute(null, "srid", SDFDatatype.INTEGER, null, null, null)));

	public static final SDFDatatype SPATIAL_LINE_STRING = new SDFSpatialDatatype("SpatialLineString",
			SDFDatatype.KindOfDatatype.BEAN,
			SDFSchemaFactory.createNewTupleSchema("",
					new SDFAttribute(null, "points", SDFSpatialDatatype.SPATIAL_COORDINATE_SEQUENCE, null, null, null),
					new SDFAttribute(null, "srid", SDFDatatype.INTEGER, null, null, null)));

	public static final SDFDatatype SPATIAL_LINEAR_RING = new SDFSpatialDatatype("SpatialLinearRing",
			SDFDatatype.KindOfDatatype.BEAN, SDFSpatialDatatype.SPATIAL_LINE_STRING);
	public static final SDFDatatype SPATIAL_LINEAR_RING_ARRAY = new SDFSpatialDatatype("SpatialLinearRingArray",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_LINE_STRING);

	public static final SDFDatatype SPATIAL_POLYGON = new SDFSpatialDatatype("SpatialPolygon",
			SDFDatatype.KindOfDatatype.BEAN,
			SDFSchemaFactory.createNewTupleSchema("",
					new SDFAttribute(null, "shell", SDFSpatialDatatype.SPATIAL_LINEAR_RING, null, null, null),
					new SDFAttribute(null, "holes", SDFSpatialDatatype.SPATIAL_LINEAR_RING_ARRAY, null, null, null),
					new SDFAttribute(null, "srid", SDFDatatype.INTEGER, null, null, null)));

	public static final SDFDatatype SPATIAL_MULTI_POINT = new SDFSpatialDatatype("SpatialMultiPoint",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_POINT);

	public static final SDFDatatype SPATIAL_MULTI_LINE_STRING = new SDFSpatialDatatype("SpatialMultiLineString",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_LINE_STRING);

	public static final SDFDatatype SPATIAL_MULTI_POLYGON = new SDFSpatialDatatype("SpatialMultiPolygon",
			SDFDatatype.KindOfDatatype.MULTI_VALUE, SDFSpatialDatatype.SPATIAL_POLYGON);

	// List of spatial points
	public static final SDFDatatype LIST_SPATIAL_POINT = new SDFDatatype("List_SPATIAL_POINT", SDFDatatype.KindOfDatatype.LIST,
			SDFSpatialDatatype.SPATIAL_POINT);

	public boolean isSpatial() {
		return this.getURI().equals(SPATIAL_GEOMETRY.getURI()) || this.isPoint() || this.isLineString()
				|| this.isPolygon() || this.isMultiPoint() || this.isMultiLineString() || this.isMultiPolygon();
	}

	public boolean isGeometryCollection() {
		return this.getURI().equals(SPATIAL_GEOMETRY_COLLECTION.getURI());
	}

	public boolean isPoint() {
		return this.getURI().equals(SPATIAL_POINT.getURI());
	}

	public boolean isLineString() {
		return this.getURI().equals(SPATIAL_LINE_STRING.getURI());
	}

	public boolean isPolygon() {
		return this.getURI().equals(SPATIAL_POLYGON.getURI());
	}

	public boolean isMultiPoint() {
		return this.getURI().equals(SPATIAL_MULTI_POINT.getURI());
	}

	public boolean isMultiLineString() {
		return this.getURI().equals(SPATIAL_MULTI_LINE_STRING.getURI());
	}

	public boolean isMultiPolygon() {
		return this.getURI().equals(SPATIAL_MULTI_POLYGON.getURI());
	}

	/**
	 * This method checks whether this type can be casted into
	 * <code>other</code>.
	 * 
	 * @param other
	 * @return True, if this type can be casted into <code>other</code>
	 */
	@Override
	public boolean compatibleTo(SDFDatatype other) {
		if (other instanceof SDFSpatialDatatype) {
			SDFSpatialDatatype otherSpatial = (SDFSpatialDatatype) other;
			if (this.isSpatial() && (otherSpatial.isSpatial())
					|| this.isGeometryCollection() && (otherSpatial.isGeometryCollection())
					|| this.isPoint() && (otherSpatial.isPoint())
					|| this.isLineString() && (otherSpatial.isLineString())
					|| this.isPolygon() && otherSpatial.isPolygon()
					|| this.isMultiPoint() && otherSpatial.isMultiPoint()
					|| this.isMultiLineString() && otherSpatial.isMultiLineString()
					|| this.isMultiPolygon() && otherSpatial.isMultiPolygon()) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}

}