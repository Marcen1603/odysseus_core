package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseIterator implements Iterator<List<RelationalTuple<?>>> {
	
	private static final int SELECT_BASH_SIZE = 10;

	private String tableName;
	private SDFAttributeList schema; 

	private int selectPointer = 0;

	private int attributeSize = 0;
	private String preparedSelect = "";
	
	private int totalSize = 0;

	private Connection connection;

	private int startTimeIndex = -1;
	private int endTimeIndex = -1;


	public DatabaseIterator(String name, SDFAttributeList schema, Connection connection) {
		this.schema = schema;
		this.tableName = name;
		this.connection = connection;
		init();
	}

	public void init() {
		this.preparedSelect = createSelectStatement();
		// this produces an unsynchronized view, because the size could be changed during further execution...
		this.totalSize = getCount();
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
				this.startTimeIndex  = index;
			}
			if(attribute.getDatatype().getQualName().toUpperCase().equals("ENDTIMESTAMP")){
				this.endTimeIndex = index;
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
		this.selectPointer=this.selectPointer+SELECT_BASH_SIZE;
		Statement s = this.connection.createStatement();
		ResultSet rs = s.executeQuery(query);
		List<RelationalTuple<?>> liste = new ArrayList<RelationalTuple<?>>();
		while(rs.next()) {
			Object[] values = new Object[this.attributeSize];
			for(int i=0;i<this.attributeSize;i++){
				values[i] = rs.getObject(i+1);            			
			}
			RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(values);
			PointInTime startTimePT = PointInTime.getZeroTime();
			PointInTime endTimePT = PointInTime.getInfinityTime();
			if(this.startTimeIndex>-1){
				long time = rs.getLong(this.startTimeIndex+1);
				startTimePT = new PointInTime(time);
			}
			if(this.endTimeIndex>-1){
				long time = rs.getLong(this.endTimeIndex+1);
				endTimePT = new PointInTime(time);
			}
			tuple.setMetadata(new TimeInterval(startTimePT, endTimePT));
			liste.add(tuple);
		}
		return liste;		
	}

	@Override
	public boolean hasNext() {		
		return selectPointer < totalSize;
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
			Statement s = DatabaseServiceLoader.getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT count(*) FROM "+this.tableName);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return -1;
	}

}
