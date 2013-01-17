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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Stephan Jansen
 *
 */
public class BooleanDataTypeMappingHandler extends AbstractDatatypeMappingHandler<Boolean> {
	
	
	public BooleanDataTypeMappingHandler(){
		super(SDFDatatype.BOOLEAN, Types.BOOLEAN);
		
		addAdditionalSQLDatatype(Types.BIT);		
	}		
	
	@Override
	public void setValue(PreparedStatement preparedStatement, int position, Object value) throws SQLException {
		preparedStatement.setBoolean(position, (Boolean) value);
	}

	@Override
	public Boolean getValue(ResultSet result, int position) throws SQLException {
		return result.getBoolean(position);		
	}
	
}
