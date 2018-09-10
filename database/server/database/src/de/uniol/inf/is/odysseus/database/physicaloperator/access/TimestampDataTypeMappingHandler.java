package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class TimestampDataTypeMappingHandler extends
		AbstractDatatypeMappingHandler<Long> {

	@Override
	public void setValue(PreparedStatement preparedStatement, int position,
			Object value) throws SQLException {
		preparedStatement.setTimestamp(position,  new Timestamp(new Date((long) value).getTime()));		
	}

	@Override
	public Long getValue(ResultSet result, int position)
			throws SQLException {
		return result.getTimestamp(position).getTime();
	}

}
