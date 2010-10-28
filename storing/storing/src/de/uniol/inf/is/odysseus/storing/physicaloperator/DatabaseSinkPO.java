package de.uniol.inf.is.odysseus.storing.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class DatabaseSinkPO<T extends RelationalTuple<?>> extends AbstractSink<T> {

	private static final int SELECT_BASH_SIZE = 10;
	private Queue<RelationalTuple<?>> values = new LinkedList<RelationalTuple<?>>();
	private PreparedStatement preparedStatement;
	private Connection connection;
	private String table;
	private boolean savemetadata = false;
	private boolean create = false;
	private boolean ifnotexists = false;
	private boolean truncate = false;
	private boolean opened = false;

	
	public DatabaseSinkPO(Connection connection, String table){
		this(connection, table, true, true, false, true);
	}
	
	
	public DatabaseSinkPO(Connection connection, String table, boolean savemetadata, boolean create, boolean truncate, boolean ifnotexists) {
		this.connection = connection;
		this.table = table;
		this.create = create;
		this.ifnotexists = ifnotexists;
		this.truncate = truncate;
		this.savemetadata = savemetadata;
	}

	public DatabaseSinkPO(DatabaseSinkPO<T> o) {
		this.connection = o.connection;
		this.table = o.table;
		this.create = o.create;
		this.ifnotexists = o.ifnotexists;
		this.truncate = o.truncate;
		this.savemetadata = o.savemetadata;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.opened = true;
		// CREATE TABLE
		if (create) {
			// IF NOT EXISTS
			if (ifnotexists) {
				// also, not exists
				if (!tableExists()) {
					createTable();
				} else {
					// nothing, because only create if NOT exists!
//					dropTable();
//					createTable();
				}
			}else{
				dropTable();
				createTable();
			}
		} else {
			if (truncate) {
				truncate();
			}
		}

		if (checkDatabaseValidity()) {
			try {
				String ps = "INSERT INTO " + table + " (";

				int count = 0;
				String del = "";
				for (SDFAttribute a : this.getOutputSchema()) {
					ps = ps + del + a.getAttributeName();
					count++;
					del = ", ";
				}
				ps = ps + ") VALUES (";
				del = "";
				for (int i = 0; i < count; i++) {
					ps = ps + del + "?";
					del = ", ";
				}
				ps = ps + ")";
				System.out.println("insert statement created: " + ps);
				preparedStatement = this.connection.prepareStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException(
					"Table already exists, but its schema does not match the query schema! Use CREATE option for dropping the old table and recreating a new table.");
		}

	}

	private boolean tableExists() {		
		try {
			this.connection.createStatement().execute("SELECT count(*) FROM "+table);
			return true;
		} catch (SQLException e) {
			return false;
		}		
	}

	private boolean checkDatabaseValidity() {
		return true;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		if(!opened){
			System.out.println("Not opened!!");
			try {
				process_open();
			} catch (OpenFailedException e) {				
				e.printStackTrace();
			}
		}
		synchronized (values) {
			values.offer(object);
			if (values.size() >= SELECT_BASH_SIZE) {
				writeToDatabase(values);
			}
		}
	}

	private void writeToDatabase(Queue<RelationalTuple<?>> queue) {
		try {
			while (!queue.isEmpty()) {
				RelationalTuple<?> tuple = queue.poll();
				for (int i = 1; i <= tuple.getAttributeCount(); i++) {
					preparedStatement.setObject(i, tuple.getAttribute(i - 1));
				}
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DatabaseSinkPO<T> clone() {
		return new DatabaseSinkPO<T>(this);
	}

	private void createTable() {
		Statement s;
		try {
			s = this.connection.createStatement();
		} catch (SQLException e) {
			// exists...
			e.printStackTrace();
			return;
		}

		String query = "CREATE TABLE " + this.table + "(";
		String delimiter = "";
		for (SDFAttribute attribute : getOutputSchema()) {
			String name = attribute.getAttributeName();
			SDFDatatype type = attribute.getDatatype();
			String typeString = getSQLType(type);
			query = query + delimiter + name + " " + typeString;
			delimiter = ", ";
		}
		query = query + ")";
		System.out.println("CREATING TABLE: " + query);
		try {
			s.execute(query);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	private void dropTable() {
		if(tableExists()){			
			try {
				String query = "DROP TABLE " + table;
				System.out.println("Dropping table: " + query);
				Statement s = this.connection.createStatement();
				s.execute(query);
			} catch (SQLException e1) {
				System.err.println("Error while dropping table "+table);
			}
		}
	}

	private void truncate() {
		dropTable();
		createTable();
	}

	private String getSQLType(SDFDatatype type) {
		if (SDFDatatypes.isDouble(type)) {
			return "DOUBLE";
		}
		if (SDFDatatypes.isDate(type)) {
			return "DATE";
		}
		if (SDFDatatypes.isInteger(type)) {
			return "INTEGER";
		}
		if (SDFDatatypes.isString(type)) {
			return "VARCHAR(255)";
		}
		if (SDFDatatypes.isLong(type)) {
			return "BIGINT";
		}
		if (type.getURI(false).toUpperCase().endsWith("TIMESTAMP")){
			return "BIGINT";
		}
		return "VARCHAR(255)";
	}

}
