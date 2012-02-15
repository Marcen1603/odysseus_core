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
public class BooleanDataTypeMappingHandler implements IDataTypeMappingHandler{
	static protected List<String> types = new ArrayList<String>(1);
	static{
		types.add(SDFDatatype.BOOLEAN.getURI());
	}
	
	@Override
	public void mapValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setBoolean(position, (Boolean) value);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return types;
	}

}
