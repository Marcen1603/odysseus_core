/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.connection;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen
 * Created at: 08.11.2011
 */
public class DatatypeRegistry {

	private static DatatypeRegistry instance = null;
	
	private List<DatatypeMapping> streamToBase = new ArrayList<DatatypeMapping>();
	private List<DatatypeMapping> baseToStream = new ArrayList<DatatypeMapping>();
	
	private DatatypeRegistry(){
		
	}
	
	public static synchronized DatatypeRegistry getInstance(){
		if(instance == null){
			instance = new DatatypeRegistry();
		}
		return instance;
	}
	
	
	public void registerStreamToDatabase(SDFDatatype streamType, int databaseType){
		this.streamToBase.add(new DatatypeMapping(streamType, databaseType));
	}
	
	public void registerDatabaseToStream(int databaseType, SDFDatatype streamType){
		this.baseToStream.add(new DatatypeMapping(streamType, databaseType));
	}
	
	public SDFDatatype getSDFDatatype(int databaseType){
		for(DatatypeMapping dm : this.baseToStream){
			if(dm.getJDBCDatatype()==databaseType){
				return dm.getSDFDatatype();
			}
		}
		return SDFDatatype.OBJECT;
	}
	
	public int getJDBCDatatype(SDFDatatype streamType){
		for(DatatypeMapping dm : this.streamToBase){
			if(dm.getSDFDatatype().equals(streamType)){
				return dm.getJDBCDatatype();
			}
		}
		return Types.OTHER;
	}
	
	
	public boolean isValidStreamToDatabaseMapping(SDFDatatype dt, int databaseType){
		for(DatatypeMapping dm : this.streamToBase){
			if(dm.getJDBCDatatype()==databaseType){
				if(dm.getSDFDatatype().equals(dt)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidDatabaseToStreamMapping(int databaseType, SDFDatatype dt){
		for(DatatypeMapping dm : this.baseToStream){
			if(dm.getJDBCDatatype()==databaseType){
				if(dm.getSDFDatatype().equals(dt)){
					return true;
				}
			}
		}
		return false;
	}	
	
}
