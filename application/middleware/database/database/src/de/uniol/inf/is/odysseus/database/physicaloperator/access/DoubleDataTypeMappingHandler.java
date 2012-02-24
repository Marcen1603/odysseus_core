package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Stephan Jansen
 *
 */
public class DoubleDataTypeMappingHandler implements IDataTypeMappingHandler{
	static protected List<String> types = new ArrayList<String>(1);
	static{
		types.add(SDFDatatype.DOUBLE.getURI());

	}
	
	@Override
	public void mapValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setDouble(position, (Double) value);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return types;
	}

}
