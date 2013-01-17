/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Dennis Geesen
 *
 */
public abstract class AbstractDatatypeMappingHandler<T> implements IDataTypeMappingHandler<T> {

	private List<SDFDatatype> dsmstypes = new ArrayList<SDFDatatype>(1);
	private List<Integer> dbmstypes = new ArrayList<Integer>(1);
	private int defaultDBMSType = Types.OTHER;
	private SDFDatatype defaultSDFDatatype = SDFDatatype.OBJECT;
	
	public AbstractDatatypeMappingHandler(SDFDatatype defaultSDFDatatype, int defaultSQLDatatype){
		this.defaultDBMSType = defaultSQLDatatype;
		this.defaultSDFDatatype = defaultSDFDatatype;
		addAdditionalSDFDatatype(defaultSDFDatatype);
		addAdditionalSQLDatatype(defaultSQLDatatype);
	}
	
	
	protected void addAdditionalSDFDatatype(SDFDatatype type){
		dsmstypes.add(type);
	}
	

	protected void addAdditionalSQLDatatype(int type){
		dbmstypes.add(type);
	}
		
	
	public SDFDatatype getDefaultSDFDatatype(){
		return defaultSDFDatatype;
	}
	public int getDefaultSQLDatatype(){
		return defaultDBMSType;
	}
	
	
	@Override
	public List<SDFDatatype> getSupportedSDFDataTypes() {	
		return dsmstypes;
	}

	@Override
	public List<Integer> getSupportedSQLDataTypes() {
		return dbmstypes;
	}
	
	@Override
	public String toString() {	
		return getClass().getCanonicalName();			
	}

}