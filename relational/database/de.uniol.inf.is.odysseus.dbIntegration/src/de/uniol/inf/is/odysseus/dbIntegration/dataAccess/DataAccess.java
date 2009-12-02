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
import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.dbIntegration.Activator;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DataAccess implements IDataAccess {

	
	private PreparedStatement baseQuery;
	private PreparedStatement cacheQuery;
	private PreparedStatement prefetchQuery;
	private DBQuery dbQuery;
	private Connection connection;
	private ArrayList<String> dsAttributes;
	private IConnectionData connectionData;
	private SDFAttributeList sdfList;
	
	private String alias;
	
//	public DataAccess(DBQuery dbQuery) {
//		this.dbQuery = dbQuery;
//		baseQuery = prepareStatement(escape(dbQuery.getQuery()));
//		dsAttributes = findDataStreamAttr(dbQuery.getQuery());
//		
//	}
	public DataAccess(DBQuery dbQuery, SDFAttributeList sdfList) {
		this.connectionData = getConnectionData(Activator.getContext());
		this.dbQuery = dbQuery;
		this.sdfList = sdfList;
		baseQuery = prepareStatement(escape(dbQuery.getQuery()));
		dsAttributes = findDataStreamAttr(dbQuery.getQuery());
		
		
	}
	
	
//	public void init(DBQuery dbQuery) {
//		this.dbQuery = dbQuery;
//		baseQuery = prepareStatement(escape(dbQuery.getQuery()));
//		dsAttributes = findDataStreamAttr(dbQuery.getQuery());
//	}

	public DataAccess(DBQuery query) {
		this.dbQuery = query;
		connectionData = getConnectionData(Activator.getContext());
		baseQuery = prepareStatement(escape(dbQuery.getQuery()));
	}


	public DBResult executeBaseQuery (RelationalTuple<?> tuple) {
		return executeQuery(sortInput(tuple, sdfList, dsAttributes), baseQuery);
	}
	
	public DBResult executeCacheQuery(RelationalTuple tuple) {
		return executeQuery(tuple, cacheQuery);
	}

	public DBResult executePrefetchQuery(RelationalTuple tuple) {
		return executeQuery(tuple, prefetchQuery);
	}

	@Override
	public void setCacheQuery(DBQuery dbQuery) {
		cacheQuery = prepareStatement(escape(dbQuery.getQuery()));

	}

	@Override
	public void setPrefetchQuery(DBQuery dbQuery) {
		cacheQuery = prepareStatement(escape(dbQuery.getQuery()));
	}
	
	
	
	private DBResult executeQuery(RelationalTuple<?> tuple, PreparedStatement prst) {
		
		List<RelationalTuple<?>> list = new LinkedList<RelationalTuple<?>>() ;
		ResultSet rs = null;
		
		try {
			if (connection.isClosed()) {
				openConnection();
			}
			
			
			for (int i = 0; i < tuple.getAttributeCount(); i++) {
				String input = tuple.getAttribute(i).toString();
				prst.setString(i+1, input);
			}
			
//			for (int i = 0; i < streamParam.getParams().size(); i++) {
//				prst.setString(i, (String) streamParam.getParams().get(i));
//			}
			
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
//			try {
//				closeConnection();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		}
		
		return new DBResult(list);
	}
	
	
	private void openConnection() throws SQLException {
		DBProperties properties;
		try {
			properties = connectionData.getConnection(dbQuery.getDatabase());
			connection = null;
			try {
				Class.forName(properties.getDriverClass()).newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			connection = DriverManager.getConnection(
					properties.getUrl(), 
					properties.getUser(), 
					properties.getPassword());
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	private void closeConnection() throws SQLException {
		if(connection != null) {
			connection.close();
		}
	}
	
	private PreparedStatement prepareStatement(String sql) {
		PreparedStatement prst = null;
		try {
			openConnection();
			
			
			//Elemente der Form "$source.attribute durch ? ersetzen
			sql = sql.replaceAll("\\$\\w+\\.\\w+", "?");
			prst = connection.prepareStatement(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return prst;
		
		
	}
	
	public List<String> getOutAttributeSchema() {
		
		List<String> out = new ArrayList<String>();
		try {
			ResultSetMetaData meta = baseQuery.getMetaData();
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
	 * @param oldTuple Tupel vom phys. Operator
	 * @param list Liste der Attributsnamen aus dem Datenstrom
	 * @param refAttributes Liste der referenzierten Attribute in 
	 * 			der DB-Anweisung
	 * @return Liste der DS-Werte in DB-korrekter Reihenfolge
	 */
	private RelationalTuple<?> sortInput(RelationalTuple<?> oldTuple, 
			SDFAttributeList list, List<String> refAttributes) {
		
		Object[] objects = new Object[refAttributes.size()];
//		for (String string : refAttributes) {
//			for (int i = 0; i < list.size(); i++) {
//				if (string.equals(((CQLAttribute) list.getAttribute(i)).getURI())) {
//					objects[i] = oldTuple.getAttribute(i).toString();
//				}
//			}
//		}
		
		for (int i = 0; i < refAttributes.size(); i++) {
			String refAttr = refAttributes.get(i);
			for (int j = 0; j < list.size(); j++) {
				
				String inputSchemaAttr = ((CQLAttribute) list.getAttribute(j)).getURI();
				
				if (refAttr.equals(inputSchemaAttr)) {
					objects[i] = oldTuple.getAttribute(j).toString();
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
	private ArrayList<String> findDataStreamAttr(String sql) {
		String [] list = new String [0];
		list = sql.split("[^\\$\\w*\\.]");
		ArrayList<String> result = new ArrayList<String>();
		
		for (String string : list) {
			if(string.startsWith("$")) {
				result.add(string.replaceFirst("\\$", "").trim());
			}
		}
//		for (String string : result) {
//			System.out.println(string);
//		}
		
		return result;
	}
	
	private void filterSQLCommands(String sql) {
		String[] queries = sql.split(";");
		for (String string : queries) {
			if (string.startsWith("drop")) {
				//TODO: Exception werfen
			}
		}
	}
	
	private IConnectionData getConnectionData(BundleContext context) {
		if (context != null) {
			return (IConnectionData) context.getService(context.getServiceReference(IConnectionData.class.getName()));
		}
		return null;
		
	}
	

}
