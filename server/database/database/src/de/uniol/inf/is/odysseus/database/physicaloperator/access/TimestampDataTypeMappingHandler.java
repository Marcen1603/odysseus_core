package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class TimestampDataTypeMappingHandler extends
		AbstractDatatypeMappingHandler<Long> {

	public TimestampDataTypeMappingHandler() {
		super(SDFDatatype.TIMESTAMP, Types.TIMESTAMP);	
		
		addAdditionalSDFDatatype(SDFDatatype.POINT_IN_TIME);
		addAdditionalSDFDatatype(SDFDatatype.START_TIMESTAMP);
		addAdditionalSDFDatatype(SDFDatatype.END_TIMESTAMP);
		addAdditionalSDFDatatype(SDFDatatype.DATE);
	}

	@Override
	public void setValue(PreparedStatement preparedStatement, int position,
			Object value) throws SQLException {
		preparedStatement.setTimestamp(position, (Timestamp) new Date((long) value));		
	}

	@Override
	public Long getValue(ResultSet result, int position)
			throws SQLException {
		return result.getTimestamp(position).getTime();
	}

}
