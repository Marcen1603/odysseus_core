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

package de.uniol.inf.is.odysseus.sink.database.physicaloperator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 */
public class DatabaseSinkPO extends AbstractSink<RelationalTuple<ITimeInterval>> {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(DatabaseSinkPO.class); 
	private Connection connection;
	private PreparedStatement preparedStatement;
	private enum DatabaseType { Integer, String, Float, Long, Double, Boolean};
	private Map<SDFDatatype, DatabaseType> datatypeMappings = new HashMap<SDFDatatype, DatabaseType>();
	private String tablename="\"GEESEN\".\"testtable\"";
	
	
	private int counter = 1;
	private long summe = 0L;
	
	public DatabaseSinkPO(String databasename) {
		initMappings();
	}

	
	public DatabaseSinkPO(DatabaseSinkPO databaseSinkPO) {
		this.initMappings();
	}


	private void initMappings(){
		this.datatypeMappings.put(SDFDatatype.INTEGER, DatabaseType.Integer);
		this.datatypeMappings.put(SDFDatatype.BOOLEAN, DatabaseType.Boolean);		
		this.datatypeMappings.put(SDFDatatype.END_TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.FLOAT, DatabaseType.Float);
		this.datatypeMappings.put(SDFDatatype.LONG, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.POINT_IN_TIME, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.START_TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.STRING, DatabaseType.String);
		this.datatypeMappings.put(SDFDatatype.TIMESTAMP, DatabaseType.Long);
		this.datatypeMappings.put(SDFDatatype.DOUBLE, DatabaseType.Double);
		
	}
	
	
	private void createTable(){
		String statement = "CREATE TABLE "+this.tablename+"(";
		
		
//		CREATE TABLE "GEESEN"."testtable" 
//		   (	"timestamp" NUMBER(*,0), 
//			"name" VARCHAR2(255), 
//			"id" NUMBER(*,0), 
//			"xvalue" FLOAT(126), 
//			"yvalue" FLOAT(126), 
//			"number" NUMBER, 
//			"meta_start" NUMBER, 
//			"meta_end" NUMBER
//		   ) SEGMENT CREATION IMMEDIATE 
//		  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
//		  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
//		  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
//		  TABLESPACE "USERS";
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/odysseustest", "odysseus", "ithaka");
			Class.forName("oracle.jdbc.OracleDriver");
			this.connection = DriverManager.getConnection("jdbc:oracle:thin:@134.106.56.69:1521/p2new.offis.de", "geesen", "odysseus");
			this.preparedStatement = this.connection.prepareStatement(createPreparedStatement());
			this.connection.setAutoCommit(false);
			this.counter = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenFailedException(e);		
		}
	}

	private String createPreparedStatement() {
		String s = "INSERT INTO  \"GEESEN\".\"testtable\" VALUES(";
		int count = super.getOutputSchema().size();
		String sep = "";
		for (int i = 0; i < count; i++) {
			s = s + sep + "?";
			sep = ",";
		}
		s = s + ")";
		return s;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
	}

	private void calcLatency(RelationalTuple<ITimeInterval> tuple){
		long start = tuple.getAttribute(0);
		long diff = System.currentTimeMillis() - start;
		summe = summe+diff;
		if((counter%1000)==0){
			System.out.println("Bei "+counter+" Elementen:");
			System.out.println(" - Total: "+summe);
			System.out.println(" - Avg: "+((double)summe/(double)counter));
		}
		
	}
	
	@Override
	protected void process_next(RelationalTuple<ITimeInterval> tuple, int port, boolean isReadOnly) {
		try {
			int i = 0;
				
			for (SDFAttribute attribute : this.getOutputSchema()) {
				SDFDatatype datatype = attribute.getDatatype();
				Object attributeValue = tuple.getAttribute(i);
				DatabaseType mapping = this.datatypeMappings.get(datatype);
				switch (mapping) {
				case Boolean:
					this.preparedStatement.setBoolean(i+1, (Boolean) attributeValue);
					break;
				case Integer:
					this.preparedStatement.setInt(i+1, (Integer) attributeValue);
					break;
				case Double:
					this.preparedStatement.setDouble(i+1, (Double) attributeValue);
					break;
				case Float:
					this.preparedStatement.setFloat(i+1, (Float) attributeValue);
					break;
				case Long:
					this.preparedStatement.setLong(i+1, (Long) attributeValue);
					break;
				case String:
					this.preparedStatement.setString(i+1, (String) attributeValue);
					break;				
				}				
				i++;
			}
			this.preparedStatement.addBatch();			
			counter++;
			if((counter%10)==0){
				int count = this.preparedStatement.executeBatch().length;
				this.connection.commit();
				//logger.debug("Inserted "+count+" rows in database");
			}
			calcLatency(tuple);			
		//	logger.debug("Inserted "+count+" rows in database");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public DatabaseSinkPO clone() {
		return new DatabaseSinkPO(this);
	}

}
