package de.uniol.inf.is.odysseus.dbIntegration.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.dbIntegration.Activator;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DataAccess implements IDataAccess {

	private DBQuery dbQuery;
	private SDFAttributeList inputSchema;
	private IConnectionData connectionData;
	private Connection connection;
	private LinkedList<String> refStreamAttributes;
	private PreparedStatement prepBaseQuery;
	private PreparedStatement prepCacheQuery;
	private PreparedStatement prepPrefetchQuery;
	private ConnectionDataServiceTracker connectionDataTracker;
	
	
	public DataAccess(DBQuery dbQuery, SDFAttributeList inputSchema) {
		this(dbQuery);
		this.inputSchema = inputSchema;
		refStreamAttributes = findDataStreamAttr(dbQuery.getQuery());
	}
	
	public DataAccess(DBQuery query) {
		this.dbQuery = query;
		connectionDataTracker = new ConnectionDataServiceTracker(Activator.getContext());
		connectionDataTracker.open();
		prepBaseQuery = prepareStatement(dbQuery);
	}


	@Override
	public List<RelationalTuple<?>> executeBaseQuery (RelationalTuple<?> tuple) {
		return executeQuery(sortInput(tuple, inputSchema, refStreamAttributes), prepBaseQuery);
	}
	
	@Override
	public List<RelationalTuple<?>> executeCacheQuery(RelationalTuple<?> tuple) {
		return executeQuery(tuple, prepCacheQuery);
	}

	@Override
	public List<RelationalTuple<?>> executePrefetchQuery(RelationalTuple<?> tuple) {
		return executeQuery(tuple, prepPrefetchQuery);
	}

	@Override
	public void setCacheQuery(DBQuery dbQuery) {
		prepCacheQuery = prepareStatement(dbQuery);

	}

	@Override
	public void setPrefetchQuery(DBQuery dbQuery) {
		prepCacheQuery = prepareStatement(dbQuery);
	}
	
	
	
	private List<RelationalTuple<?>> executeQuery(RelationalTuple<?> tuple, PreparedStatement prst) {
		
		List<RelationalTuple<?>> list = new LinkedList<RelationalTuple<?>>() ;
		ResultSet rs = null;
		
		try {
			if (connection.isClosed()) {
				openConnection(dbQuery);
			}
			
			
			for (int i = 0; i < tuple.getAttributeCount(); i++) {
				String input = tuple.getAttribute(i).toString();
				prst.setString(i+1, input);
			}
			
			if (dbQuery.isUpdate()) {
				prst.execute();
			} else {
				rs = prst.executeQuery();
				ResultSetMetaData meta = rs.getMetaData();
				while (rs.next()) {
					Object [] tempTuple = new Object[meta.getColumnCount()];
					for(int i = 0; i < meta.getColumnCount(); i++) {
						tempTuple[i] = rs.getObject(i+1);
					}
					list.add(new RelationalTuple(tempTuple));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		
		return list;
	}
	
	
	private void openConnection(DBQuery dbQuery) throws SQLException {
		DBProperties properties;
		try {
			properties = connectionData.getConnection(dbQuery.getDatabase());
			
			if (properties != null && !properties.getDriverClass().isEmpty()) {
				connection = null;
				try {
					Class.forName(properties.getDriverClass()).newInstance();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				connection = DriverManager.getConnection(
						properties.getUrl(), 
						properties.getUser(), 
						properties.getPassword());
			}
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
		
	}
	
	
	private void closeConnection() throws SQLException {
		if(connection != null) {
			connection.close();
		}
	}
	
	private PreparedStatement prepareStatement(DBQuery dbQuery) {
		PreparedStatement prst = null;
		try {
			openConnection(dbQuery);
			String sql = dbQuery.getQuery();
			String prepSql = sql;
			sql = sql.replace("(", " ");
			sql = sql.replace(")", " ");
			sql = sql.replace(",", " ");
			sql = sql.replace("=", " ");
			String [] list = sql.split(" ");
			LinkedList<String> prefList = new LinkedList<String>();
			for (String string : list) {
				if (string.startsWith("$")) {
					prefList.add(string);
				}
			}
			for (String string : prefList) {
				prepSql = prepSql.replace(string, "?");
			
			}
			prst = connection.prepareStatement(escape(prepSql));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return prst;
	}
	
	@Override
	public List<String> getOutAttributeSchema(DBQuery dbQuery) {
		List<String> out = new ArrayList<String>();
		try {
			ResultSetMetaData meta = prepareStatement(dbQuery).getMetaData();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				
				out.add(meta.getColumnName(i+1));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	
	
	
	
	private String escape(String sql) {
		if (sql == null) {
			return null;
		}
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("\"", "\\\"");
		sql = sql.replace("'", "''");
		return sql;
	}
	
	/**
	 * Bringt die Elemente eines DS-Tupels in die Reihenfolge, wie sie auch
	 * fuer eine Datenbankanweisung benötigt werden.
	 * @param streamInputTuple Tupel vom phys. Operator
	 * @param inputSchemaList Liste der Attributsnamen aus dem Datenstrom
	 * @param refDBAttributes Liste der referenzierten Attribute in 
	 * 			der DB-Anweisung
	 * @return Liste der DS-Werte in DB-korrekter Reihenfolge
	 */
	@SuppressWarnings("unchecked")
	private RelationalTuple<?> sortInput(RelationalTuple<?> streamInputTuple, 
			SDFAttributeList inputSchemaList, List<String> refDBAttributes) {
		
		Object[] objects = new Object[refDBAttributes.size()];
		
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(inputSchemaList);
		
		
		for (int i = 0; i < refDBAttributes.size(); i++) {
			SDFAttribute refAttr = attributeResolver.getAttribute(refDBAttributes.get(i));
			
			for (int j = 0; j < inputSchemaList.size(); j++) {
				
				SDFAttribute inputSchemaAttr = ((SDFAttribute) inputSchemaList.getAttribute(j));
				
				if (refAttr.equals(inputSchemaAttr)) {
					objects[i] = streamInputTuple.getAttribute(j).toString();
					break;
				}
			}
		}
		return new RelationalTuple(objects);
	}
	
	/**
	 * Sucht aus einem uebergebenen SQL-Statement alle Referenzen
	 * zur Datenstromanfrage heraus. Diese beginnen mit "$".
	 * @param sql
	 * @return eine Liste der Referenzen
	 */
	private LinkedList<String> findDataStreamAttr(String sql) {
		
		sql = sql.replace("(", " ");
		sql = sql.replace(")", " ");
		sql = sql.replace(",", " ");
		sql = sql.replace("=", " ");
		String [] list = sql.split(" ");
		LinkedList<String> prefList = new LinkedList<String>();
		for (String string : list) {
			if (string.startsWith("$")) {
				prefList.add(string.replace("$", ""));
			}
		}
		return prefList;
	}
	
//	private void filterSQLCommands(String sql) {
//		String[] queries = sql.split(";");
//		for (String string : queries) {
//			if (string.startsWith("drop")) {
//			}
//		}
//	}
	
	
	@Override
	public RelationalTuple<?> getRelevantParams(RelationalTuple<?> tuple) {
		RelationalTuple<?> param = sortInput(tuple, inputSchema, refStreamAttributes);
		return param;
	}
	
	
	class ConnectionDataServiceTracker extends ServiceTracker {

		public ConnectionDataServiceTracker(BundleContext bundleContext) {
			super(bundleContext, IConnectionData.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			
			IConnectionData connService = (IConnectionData) context.getService(reference);
			
			if (connService != null) {
				connectionData = connService;
			}
			return connService;
		}

		public void removedService(ServiceReference reference, Object service) {
			context.ungetService(reference);
		}
	}

	

}
