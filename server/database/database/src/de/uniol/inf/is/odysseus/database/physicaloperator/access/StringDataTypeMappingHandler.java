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
package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class StringDataTypeMappingHandler extends AbstractDatatypeMappingHandler<String> {

	@Override
	public void setValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setString(position, String.valueOf(value));
	}

	@Override
	public String getValue(ResultSet result, int position) throws SQLException {
		switch (result.getMetaData().getColumnType(position)) {
		case Types.LONGNVARCHAR:
		case Types.NVARCHAR:
		case Types.NCHAR:
			return result.getNString(position);
		default:
			return result.getString(position);
		}
	}

}
