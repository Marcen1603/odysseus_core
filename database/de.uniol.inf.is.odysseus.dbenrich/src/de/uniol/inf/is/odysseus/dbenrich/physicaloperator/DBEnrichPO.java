package de.uniol.inf.is.odysseus.dbenrich.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.DBRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.ICache;
import de.uniol.inf.is.odysseus.dbenrich.cache.IRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.SomeCache;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

public class DBEnrichPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	// Initialized in the constructor
	private final String connectionName;
	private final String query;
	private final List<String> variables;
	private final int cacheSize;
	private final String replacementStrategy;

	// Available after process_open()
	private ICache<ComplexParameterKey, Tuple> cacheManager; // TODO key, value types

	public DBEnrichPO(String connectionName, String query,
			List<String> variables, int cacheSize, String replacementStrategy) {
		super();
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.cacheSize = cacheSize;
		this.replacementStrategy = replacementStrategy;
	}

	public DBEnrichPO(DBEnrichPO<T> dBEnrichPO) {
		super(dBEnrichPO);
		this.connectionName = dBEnrichPO.connectionName;
		this.query = dBEnrichPO.query;
		this.variables = dBEnrichPO.variables;
		this.cacheSize = dBEnrichPO.cacheSize;
		this.replacementStrategy = dBEnrichPO.replacementStrategy;
		//TODO deepCopy
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT; // TODO eventuell modified
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {

		
		System.out.println(String.format("(%28s-------", this.getName()).replace(' ', '-'));
		
		System.out.println("Tuple(before):  "+inputTuple);


		Object[] queryParameters = getQueryParameters(inputTuple);
		ComplexParameterKey complexKey = new ComplexParameterKey(queryParameters);
		
		Tuple dbTupel = cacheManager.get(complexKey);
		System.out.println("Tuple(dbcache): " + dbTupel); // TODO REMOVE
		
		
		// Temporary: Just append, keep Metadata
		// Later: Maybe use Strategy, (clone metadata, tuple?)
		for(Object attribute : dbTupel.getAttributes()) {
			inputTuple.append(attribute, false);
		}

		System.out.println("Tuple(after):   "+inputTuple);

		transfer(inputTuple, port);

		System.out.println("-----------------------------------)");
	}

	private Object[] getQueryParameters(Tuple<T> inputTuple) {
		Object[] queryParameters = new Object[variables.size()];
		
		for(int i=0; i<queryParameters.length; i++) {
			String variable  = variables.get(i);
			
			// Get desired parameter from input tuple
			// TODO remember the positions
			SDFAttribute attribute = getOutputSchema().findAttribute(variable);
			if(attribute==null) {
				throw new RuntimeException("Could not find attribute '" + variable +"' in input tuple.");
			}
			int parameterPosition = getOutputSchema().indexOf(attribute);
			// String parameter = inputTuple.getAttribute(parameterPosition).toString();
			queryParameters[i] = inputTuple.getAttribute(parameterPosition);
		}
		
		return queryParameters;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		
		DBRetrievalStrategy dbRetrievalStrategy = new DBRetrievalStrategy(connectionName, query, variables, getOutputSchema());
		
		System.out.println("ReplacementStrategy: " + replacementStrategy);
		
		cacheManager = new SomeCache<ComplexParameterKey, Tuple>(dbRetrievalStrategy);
		
		// Opens the database connection
		cacheManager.open();
		
		System.out.println("executed process_open()");
	}

	@Override
	protected void process_close() {
		// TODO Close all (future) ressources
		cacheManager.close();

		System.out.println("executed process_close()");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp); // TODO ggf. aendern
	}

	@Override
	public AbstractPipe<Tuple<T>, Tuple<T>> clone() {
		return new DBEnrichPO<T>(this);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		// TODO Auto-generated method stub
		return false;
	}
}
