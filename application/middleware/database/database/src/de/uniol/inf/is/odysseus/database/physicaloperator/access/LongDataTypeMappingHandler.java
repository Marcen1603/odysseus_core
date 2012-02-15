package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Stephan Jansen
 *
 */
public class LongDataTypeMappingHandler implements IDataTypeMappingHandler{
	static protected List<String> types = new ArrayList<String>(5);
	static{
		types.add(SDFDatatype.LONG.getURI());
		types.add(SDFDatatype.TIMESTAMP.getURI());
		types.add(SDFDatatype.POINT_IN_TIME.getURI());
		types.add(SDFDatatype.START_TIMESTAMP.getURI());
		types.add(SDFDatatype.END_TIMESTAMP.getURI());
	}
	
	@Override
	public void mapValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setLong(position, (Long) value);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return types;
	}

}
