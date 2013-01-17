/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.database.spatial.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

import de.uniol.inf.is.odysseus.database.physicaloperator.access.AbstractDatatypeMappingHandler;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class SpatialDataTypeMappingHandler extends AbstractDatatypeMappingHandler<Geometry>{
	

	public SpatialDataTypeMappingHandler(){
		
		super(SDFSpatialDatatype.SPATIAL_GEOMETRY, Types.VARBINARY);
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);

		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_POINT);
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_LINE_STRING);
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_POLYGON);
		
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_MULTI_POINT);
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING);
		addAdditionalSDFDatatype(SDFSpatialDatatype.SPATIAL_MULTI_POLYGON);
	}
	private WKBWriter wkbwriter = new WKBWriter();
	private WKBReader wkbreader = new WKBReader();
	
	@Override
	public void setValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setBytes(position, wkbwriter.write((Geometry) value));		
	}

	@Override
	public Geometry getValue(ResultSet result, int position) throws SQLException {
		try {
			return wkbreader.read(result.getBytes(position));
		} catch (ParseException e) {
			throw new SQLException(e);			
		}		
	}

}
