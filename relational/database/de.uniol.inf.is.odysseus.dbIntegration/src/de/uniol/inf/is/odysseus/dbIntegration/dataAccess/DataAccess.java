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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.dbIntegration.Activator;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * Die Klasse DataAccess implementiert das Interface IDataAccess und stellt damit 
 * die Datenbankverbindung des Datenbankframeworks dar.
 * 
 * @author crolfes
 *
 */
public class DataAccess implements IDataAccess {

	private DBQuery dbQuery;
	private SDFAttributeList inputSchema;
	private SDFAttributeList prefetchSchema;
	private SDFAttributeList cacheSchema;
	private IConnectionData connectionData;
	private Connection connection;
	private LinkedList<String> refStreamAttributes;
	private PreparedStatement prepBaseQuery;
	private PreparedStatement prepCacheQuery;
	private PreparedStatement prepPrefetchQuery;
	private ConnectionDataServiceTracker connectionDataTracker;
	protected Logger logger = LoggerFactory.getLogger(DataAccess.class);
	private List<String> outputSchema;
	
	public DataAccess(DBQuery dbQuery, SDFAttributeList inputSchema) {
		
		this(dbQuery);
		this.inputSchema = inputSchema;
		refStreamAttributes = findDataStreamAttr(dbQuery.getQuery());
		System.out.println("db constructor");
	}
	
	public DataAccess(DBQuery query) {
		this.dbQuery = query;
		connectionDataTracker = new ConnectionDataServiceTracker(Activator.getContext());
		connectionDataTracker.open();
		prepBaseQuery = prepareStatement(dbQuery);
	}


	@Override
	public DBResult executeBaseQuery (RelationalTuple<?>tuple) {
		return executeQuery(prepBaseQuery, inputSchema, tuple);
	}
	
	@Override
	public DBResult executeCacheQuery(RelationalTuple<?> tuple) {
		return executeQuery(prepCacheQuery, cacheSchema, tuple);
	}

	@Override
	public DBResult executePrefetchQuery(RelationalTuple<?> tuple) {
		return executeQuery(prepPrefetchQuery, prefetchSchema, tuple);
	}

	@Override
	public void setCacheQuery(DBQuery dbQuery, SDFAttributeList schema) {
		prepCacheQuery = prepareStatement(dbQuery);
		if (schema == null) {
			this.cacheSchema = inputSchema;
		} else {
			this.cacheSchema = schema;
		}
	}

	@Override
	public void setPrefetchQuery(DBQuery dbQuery, SDFAttributeList schema) {
		prepCacheQuery = prepareStatement(dbQuery);
		if (schema == null) {
			this.prefetchSchema = inputSchema;
		} else {
			this.prefetchSchema = schema;
		}
		
		
	}
	
	/**
	 * In dieser Methode werden die Datenbankanfragen ausgefuehrt.
	 * 
	 * @param prst - das auszufuehrende PreparedStatement
	 * @param schema - das genutzte Eingabeschema
	 * @param tuple - das Eingabetupel, das in die Anfrage eingefuegt werden soll
	 * @return ein DBResult-Objekt. Falls es sich bei der Anfrage jedoch um eine manipulierende
	 * Anfrage handelt, wird null zurueckgegeben.
	 */
	private DBResult executeQuery(PreparedStatement prst, SDFAttributeList schema, RelationalTuple<?> tuple) {
		DBResult result;
		try {
			
			List<RelationalTuple<?>> resultTuples; 
			RelationalTuple<?> resultTuple = sortInput(tuple, schema, refStreamAttributes);
			
			for (int j = 0; j < resultTuple.getAttributeCount(); j++) {
				String input = resultTuple.getAttribute(j).toString();
				prst.setString(j+1, input);
			}
			
			if (dbQuery.isUpdate()) {
				prst.execute();
				result = null;
			} else {
				resultTuples = new LinkedList<RelationalTuple<?>>();
				ResultSet rs = prst.executeQuery();
				ResultSetMetaData meta = rs.getMetaData();
				while (rs.next()) {
					Object [] tempTuple = new Object[meta.getColumnCount()];
					for(int j = 0; j < meta.getColumnCount(); j++) {
						tempTuple[j] = rs.getObject(j+1);
					}
					resultTuples.add(new RelationalTuple(tempTuple));
				}
				result = new DBResult(resultTuples, tuple);
			}	
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
		
	}
	
	
	
	
	
	
	/**
	 * Hiermit werden zum uebergebenen Anfrage-Objekt die Verbindungsdaten 
	 * zur Datenbank ausgelesen und die Verbindung zur Datenbank hergestellt.
	 * 
	 * @param dbQuery die Datenbankanfrage und die Datenbankbezeichnung
	 * @throws SQLException falls keine Verbindung hergestellt werden kann.
	 */
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
				} catch (NullPointerException e) {
					logger.error("No settings for database? Before creating a query, you have to edit your databasesettings!");
					throw new NullPointerException();
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
	

	/**
	 * Erzeugt fuer eine uebergebene Datenbankanfrage ein PreparedStatement-Objekt.
	 * 
	 * @param dbQuery - die umzuwandelnde Datenbankanfrage
	 * @return das PreparedStatement-Objekt
	 */
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
				if (string.startsWith("#")) {
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
	public List<String> getSqlOutputSchema(DBQuery dbQuery) {
		if (outputSchema == null) {
			outputSchema = new ArrayList<String>();
			try {
				ResultSetMetaData meta = prepareStatement(dbQuery).getMetaData();
				for (int i = 0; i < meta.getColumnCount(); i++) {
					meta.getColumnType(1);
					outputSchema.add(meta.getColumnName(i+1));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return outputSchema;
	}
	
	
	
	
	/**
	 * Escaped die SQL-Anweisung.
	 * 
	 * @param sql - die SQL-Anweisung
	 * @return die bereinigte Anweisung
	 */
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
	 * fuer eine Datenbankanweisung benoetigt werden.
	 * @param streamInputTuple Tupel vom phys. Operator
	 * @param inputSchemaList Liste der Attributsnamen aus dem Datenstrom
	 * @param refDBAttributes Liste der referenzierten Attribute in 
	 * 			der DB-Anweisung
	 * @return Liste der DS-Werte in DB-korrekter Reihenfolge
	 */
	private RelationalTuple<?> sortInput(RelationalTuple<?> streamInputTuple, 
			SDFAttributeList inputSchemaList, List<String> refDBAttributes) {
		
		Object[] objects = new Object[refDBAttributes.size()];
		
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(inputSchemaList);
		
		
		try {
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
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new RelationalTuple(objects);
	}
	
	
	@Override
	public LinkedList<String> findDataStreamAttr(String sql) {
		
		sql = sql.replace("(", " ");
		sql = sql.replace(")", " ");
		sql = sql.replace(",", " ");
		sql = sql.replace("=", " ");
		String [] list = sql.split(" ");
		LinkedList<String> prefList = new LinkedList<String>();
		for (String string : list) {
			if (string.startsWith("#")) {
				prefList.add(string.replace("#", ""));
			}
		}
		return prefList;
	}
	
	
	
	@Override
	public RelationalTuple<?> getRelevantSQLObjects(RelationalTuple<?> tuple, SDFAttributeList schema) {
		if (schema == null) {
			schema = inputSchema;
		}
		RelationalTuple<?> param = sortInput(tuple, schema, refStreamAttributes);
		return param;
	}
	
	/**
	 * Die Klasse ConnectionDataServiceTracker dient der Einbindung des ConnectionDataServices.
	 * Meldet sich der ConnectionData-Service an der OSGi-Service-Registry an, wird dieser 
	 * dem jeweiligen der Datenbankanbindung uebergeben.
	 * 
	 * @author crolfes
	 *
	 */
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
