package de.uniol.inf.is.odysseus.storing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseIterator implements Iterator<List<RelationalTuple<?>>> {

	private static final String START_TIME_ATTRIBUTE = "MetaStartTimeValue";
	private static final int SELECT_BASH_SIZE = 10;

	private String tableName;
	private SDFAttributeList schema;

	private int selectPointer = 0;

	private int attributeSize = 0;
	private String preparedSelect = "";
	
	private int totalSize = 0;

	/*	
	 * // insert PreparedStatement psInsert =
	 * conn.prepareStatement("insert into location values (?, ?)");
	 * psInsert.setInt(1, 1956); psInsert.setString(2, "Webster St.");
	 * psInsert.executeUpdate(); System.out.println("Inserted 1956 Webster");	
	 */

	public DatabaseIterator(String name, SDFAttributeList schema) {
		this.schema = schema;
		this.tableName = name;
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
		for (SDFAttribute attribute : this.schema) {
			query = query + delimiter + attribute.getAttributeName();
			delimiter = ", ";
			this.attributeSize++;
		}
		query = query + " FROM " + this.tableName + " ORDER BY " + START_TIME_ATTRIBUTE + " ASC";
		return query;
	}

	public void resetSelectPointer() {
		this.selectPointer = 0;
	}

	public List<RelationalTuple<?>> selectGetNext() throws SQLException{
		String query = this.preparedSelect + " offset "+selectPointer+" rows fetch next "+SELECT_BASH_SIZE+" rows only;";
		this.selectPointer=this.selectPointer+SELECT_BASH_SIZE;
		Statement s = DatabaseServiceLoader.getConnection().createStatement();
		ResultSet rs = s.executeQuery(query);
		List<RelationalTuple<?>> liste = new ArrayList<RelationalTuple<?>>();
		while(rs.next()) {
			Object[] values = new Object[this.attributeSize];
			for(int i=0;i<this.attributeSize;i++){
				values[i] = rs.getObject(i);            			
			}
			RelationalTuple<? extends ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(values);
			tuple.getMetadata().setStart(new PointInTime(rs.getLong(this.attributeSize)));
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
			ResultSet rs = s.executeQuery("SELECT count(*) FROM "+this.tableName+";");
			rs.next();
			return rs.getInt(0);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return -1;
	}

}
