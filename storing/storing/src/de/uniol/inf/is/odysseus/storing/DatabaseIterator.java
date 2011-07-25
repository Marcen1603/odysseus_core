/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseIterator implements Iterator<List<RelationalTuple<?>>> {
	
	protected volatile static Logger LOGGER = LoggerFactory.getLogger(DatabaseIterator.class);
	
	private int SELECT_BASH_SIZE = 30;

	private String tableName;
	private SDFAttributeList schema;
	private int selectPointer = 0;
	private int attributeSize = 0;
	private String preparedSelect = "";
	
	private int totalCount = 0;

	private Connection connection;

	private Statement statement;
	private ResultSet resultSet;
	
	private int startTimeIndex = -1;
	private int endTimeIndex = -1;

	public DatabaseIterator(String name, SDFAttributeList schema, Connection connection) {
		this.schema = schema;
		this.tableName = name;
		this.connection = connection;
		this.preparedSelect = createSelectStatement();
		
		
		//First preSelect for init without a pointer move and any order for a fast installation 
		//String query = "SELECT serial, scancount, stimestamp, data FROM laser_join_9c193a OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY";
		
		totalCount = getCount();
		
		String query = this.preparedSelect + " offset "+selectPointer+" rows fetch next "+totalCount+" rows only";
		try {
			long startStatement = System.currentTimeMillis();
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(query);
			LOGGER.debug("Init Statement: " + (System.currentTimeMillis() - startStatement) + "ms");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public String createSelectStatement() {				
		String query = "SELECT ";
		String delimiter = "";
		String suffix ="";
		int index = 0;
		for (SDFAttribute attribute : this.schema) {
			query = query + delimiter + attribute.getAttributeName();
			delimiter = ", ";
			this.attributeSize++;
			if(attribute.getDatatype().getQualName().toUpperCase().equals("STARTTIMESTAMP")){
				suffix = " ORDER BY "+attribute.getAttributeName()+ " ASC";
				this.setStartTimeIndex(index);
			}
			if(attribute.getDatatype().getQualName().toUpperCase().equals("ENDTIMESTAMP")){
				this.setEndTimeIndex(index);
			}
			index++;
		}
		query = query + " FROM " + this.tableName + suffix;
		return query;
	}

	public void resetSelectPointer() {
		this.selectPointer = 0;
	}

	public List<RelationalTuple<?>> selectGetNext() throws SQLException{
		String query = this.preparedSelect + " offset "+selectPointer+" rows fetch next "+SELECT_BASH_SIZE+" rows only";
		
		long startStatement = System.currentTimeMillis();
		
		//this.selectPointer = this.selectPointer+SELECT_BASH_SIZE;
		//this.resultSet = this.statement.executeQuery(query);
		
		LOGGER.debug("selectGetNext() Statement: " + (System.currentTimeMillis() - startStatement) + "ms");
		
		List<RelationalTuple<?>> liste = new ArrayList<RelationalTuple<?>>();
		
		startStatement = System.currentTimeMillis();
		while(this.resultSet.next()) {
			Object[] values = new Object[this.attributeSize];
			for(int i=0;i<this.attributeSize;i++){
				values[i] = this.resultSet.getObject(i+1);            			
			}
			
			RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(values);
			liste.add(tuple);
		}
		LOGGER.debug("Fetch Result: " + (System.currentTimeMillis() - startStatement) + "ms");
		return liste;		
	}

	@Override
	public boolean hasNext() {		
			try {
				if(this.resultSet.isLast()){
					return false;
				}
				else{
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	}

	@Override
	public List<RelationalTuple<?>> next() {		
		try {
			return selectGetNext();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return new ArrayList<RelationalTuple<?>>();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
		
	}
	
	

	private int getCount(){
		try {
			Statement s = this.connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT count(*) FROM " + this.tableName);
			rs.next();
			int count = rs.getInt(1);
			LOGGER.debug("Database Count: " + count);
			return count;
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return -1;
	}

	public void setStartTimeIndex(int startTimeIndex) {
		this.startTimeIndex = startTimeIndex;
	}

	public int getStartTimeIndex() {
		return startTimeIndex;
	}

	public void setEndTimeIndex(int endTimeIndex) {
		this.endTimeIndex = endTimeIndex;
	}

	public int getEndTimeIndex() {
		return endTimeIndex;
	}

}
