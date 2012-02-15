package de.offis.sensegood.odysseus.database.postgresql.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.database.connection.DatatypeMapping;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
public class DatabaseConnection implements IDatabaseConnection {

	private static Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

	protected Connection connection;
	// protected HashMap<SDFDatatype, String> datatypeMappings = new
	// HashMap<SDFDatatype, String>();
	protected List<DatatypeMapping> ds2dbMapping = new ArrayList<DatatypeMapping>();

	public DatabaseConnection(Connection connection) {
		this.connection = connection;
		try {
			((org.postgresql.PGConnection) connection).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) connection).addDataType("box2d", Class.forName("org.postgis.PGbox2d"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public String getDBMSSpecificType(int jdbcType) {
		try {
			DatabaseMetaData dmd = this.connection.getMetaData();
			ResultSet r = dmd.getTypeInfo();
			while (r.next()) {
				int type = r.getInt("DATA_TYPE");
				String dbmsSpecificName = r.getString("TYPE_NAME");
				
				String params = r.getString("CREATE_PARAMS");
				String fullName = dbmsSpecificName;
				if (params != null){
					params = params.trim();
					if(!params.isEmpty()){
						if(params.startsWith("(M)")){
							fullName = fullName+"(128)";
						}
					}
				}
			//	System.out.println(r.getString("TYPE_NAME") + " | " + r.getString("CREATE_PARAMS")+" --> "+fullName);
				if(type==jdbcType){
					return fullName;
				}				
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public void createTable(String tablename, SDFSchema schema) {
		try {
			Statement st = connection.createStatement();
			String table = "CREATE TABLE " + tablename + "(";
			String sep = "";
			for (SDFAttribute attribute : schema) {
				table = table + sep + attribute.getAttributeName() + " ";
				SDFDatatype d = attribute.getDatatype();
				if (d.getClass().getSimpleName().equals("SDFSpatialDatatype"))
					table = table + "GEOMETRY";
				else
					table = table + getDBMSSpecificType(DatatypeRegistry.getInstance().getJDBCDatatype(attribute.getDatatype()));
				sep = ", ";
			}
			table = table + ")";	
			st.executeUpdate(table);			
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	public boolean tableExists(String tablename) {
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (res.next()) {
				if (res.getString("TABLE_NAME").equals(tablename)) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

	public SDFSchema getSchema(String tablename) {
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = meta.getColumns(null, null, tablename, null);
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME");
				SDFDatatype dt = DatatypeRegistry.getInstance().getSDFDatatype(rs.getInt("DATA_TYPE"));
				SDFAttribute a = new SDFAttribute(null,name, dt);
				attrs.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SDFSchema schema = new SDFSchema(tablename, attrs);
		return schema;
	}	

	public boolean equalSchemas(String tablename, SDFSchema schema) {
		try {
			DatabaseMetaData meta = connection.getMetaData();
			int i = 0;
			ResultSet rsColumns = meta.getColumns(null, null, tablename, null);
			while (rsColumns.next()) {
				if (i >= schema.size()) {
					// unterschiedliche anzahl
					return false;
				}
				int dbType = rsColumns.getInt("DATA_TYPE");
				SDFDatatype dt = schema.get(i).getDatatype();

				if (!DatatypeRegistry.getInstance().isValidStreamToDatabaseMapping(dt, dbType)) {
					// stream to db mapping does not exist, but maybe other
					// direction?
					if (!DatatypeRegistry.getInstance().isValidDatabaseToStreamMapping(dbType, dt)) {
						// both combinations were not found
						logger.error("Expected types for stream and database are not equal for");
						logger.error("- database: " + dbType + " <--> local: " + dt);
						logger.error("- see also in java.sql.Types!");
						logger.error("- database: " + rsColumns.getShort("COLUMN_NAME") + " <--> local: " + schema.get(i).getAttributeName());
						return false;
					}
				}
				i++;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public void dropTable(String tablename) {
		Statement stmt;
		try {
			stmt = this.connection.createStatement();
			stmt.executeUpdate("DROP TABLE " + tablename);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void truncateTable(String tablename) {
		Statement stmt;
		try {
			stmt = this.connection.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE " + tablename);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> getInformation() {
		Map<String, String> infos = new TreeMap<String, String>();
		try {
			DatabaseMetaData dmd = this.connection.getMetaData();
			infos.put("Database Product Name", dmd.getDatabaseProductName());
			infos.put("Database Product Version", dmd.getDatabaseProductVersion());
			infos.put("Driver Name", dmd.getDriverName());
			infos.put("Driver Version", dmd.getDriverVersion());
			infos.put("URL", dmd.getURL());
			infos.put("Username", dmd.getUserName());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return infos;
	}

	public List<String> getTables() {
		List<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (res.next()) {
				tables.add(res.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	public List<String> getSchemas() {
		List<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getSchemas();
			while (res.next()) {
				tables.add(res.getString("TABLE_SCHEM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
}
