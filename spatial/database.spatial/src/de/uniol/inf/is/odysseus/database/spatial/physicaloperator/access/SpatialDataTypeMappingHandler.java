package de.uniol.inf.is.odysseus.database.spatial.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;

import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;

public class SpatialDataTypeMappingHandler implements IDataTypeMappingHandler{
	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("SpatialGeometry");
		types.add("SpatialGeometryCollection");

		types.add("SpatialPoint");
		types.add("SpatialLineString");
		types.add("SpatialPolygon");
		
		types.add("SpatialMultiPoint");
		types.add("SpatialMultiLineString");
		types.add("SpatialMutliPolygon");
	}
	private static WKBWriter wkbwriter = new WKBWriter();
	
	@Override
	public void mapValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setBytes(position, wkbwriter.write((Geometry)value));
		
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return types;
	}

}
