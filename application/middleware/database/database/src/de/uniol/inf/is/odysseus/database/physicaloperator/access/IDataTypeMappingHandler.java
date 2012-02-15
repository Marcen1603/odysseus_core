package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Stephan Jansen
 *
 */
public interface IDataTypeMappingHandler {
	public void mapValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException;

	public List<String> getSupportedDataTypes();
}
