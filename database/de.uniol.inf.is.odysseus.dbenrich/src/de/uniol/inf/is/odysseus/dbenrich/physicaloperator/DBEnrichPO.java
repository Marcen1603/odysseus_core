package de.uniol.inf.is.odysseus.dbenrich.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.IReadOnlyCache;

public class DBEnrichPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	// Initialized in the constructor
	private final String connectionName;
	private final String query;
	private final List<String> variables;
	private final boolean noCache;
	private final int cacheSize;
	private final long expirationTime;
	private final String removalStrategy;
	// Fully initialized after process_open()
	private final IDataMergeFunction<Tuple<T>> dataMergeFunction;
	private final IReadOnlyCache<ComplexParameterKey, Tuple> cacheManager;
	/** The positions of the db query parameters in the inputTuple attributes,
	 * ordered as in the db query */
	private final int[] parameterPositions;

	public DBEnrichPO(String connectionName, String query,
			List<String> variables, boolean noCache, int cacheSize,
			long expirationTime, String removalStrategy,
			IDataMergeFunction<Tuple<T>> dataMergeFunction,
			IReadOnlyCache<ComplexParameterKey, Tuple> cacheManager) {
		super();
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.noCache = noCache;
		this.cacheSize = cacheSize;
		this.expirationTime = expirationTime;
		this.removalStrategy = removalStrategy;
		this.dataMergeFunction = dataMergeFunction;
		this.cacheManager = cacheManager;
		this.parameterPositions = new int[variables.size()];
	}

	public DBEnrichPO(DBEnrichPO<T> dBEnrichPO) {
		super(dBEnrichPO);
		this.connectionName = dBEnrichPO.connectionName;
		this.query = dBEnrichPO.query;
		this.variables = dBEnrichPO.variables;
		this.noCache = dBEnrichPO.noCache;
		this.cacheSize = dBEnrichPO.cacheSize;
		this.expirationTime = dBEnrichPO.expirationTime;
		this.removalStrategy = dBEnrichPO.removalStrategy;
		this.dataMergeFunction = dBEnrichPO.dataMergeFunction;
		this.cacheManager = dBEnrichPO.cacheManager;
		this.parameterPositions = dBEnrichPO.parameterPositions;
		//TODO ggf. deepCopies von arrays, cacheManager, mergeFunction
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {

		System.out.println(String.format("(%28s-------", getName()).replace(' ', '-'));

		System.out.println("Tuple(before):  "+inputTuple);

		// get the parameters used for the db query from the tuple attributes
		Object[] queryParameters = getQueryParameters(inputTuple);
		ComplexParameterKey complexKey = new ComplexParameterKey(queryParameters);

		Tuple<?> dbTupel = cacheManager.get(complexKey);

		if(dbTupel != null) {
			System.out.println("Tuple(dbcache): " + dbTupel);

			@SuppressWarnings("unchecked") // The Metadata is irrelevant here
			Tuple<T> outputTuple = dataMergeFunction.merge(inputTuple, (Tuple<T>)dbTupel);
			outputTuple.setMetadata(inputTuple.getMetadata());

			System.out.println("Tuple(after):   "+outputTuple);

			transfer(outputTuple, port);

		} else {
			System.out.println("No enrichement data found.");
		}

		System.out.println("-----------------------------------)");
	}

	private Object[] getQueryParameters(Tuple<T> inputTuple) {
		Object[] queryParameters = new Object[parameterPositions.length];

		for(int i=0; i<queryParameters.length; i++) {
			queryParameters[i] =
					inputTuple.getAttribute(parameterPositions[i]);
		}

		return queryParameters;
	}

	//	@Deprecated // the new version uses preset paramPositions, no search
	//	private Object[] getQueryParameters(Tuple<T> inputTuple) {
	//		Object[] queryParameters = new Object[variables.size()];
	//
	//		for(int i=0; i<queryParameters.length; i++) {
	//			String variable  = variables.get(i);
	//
	//			// Get desired parameter from input tuple
	//			// tbd remember the positions
	//			SDFAttribute attribute = getOutputSchema().findAttribute(variable);
	//			if(attribute==null) {
	//				throw new RuntimeException("Could not find attribute '" + variable +"' in input tuple.");
	//			}
	//			int parameterPosition = getOutputSchema().indexOf(attribute);
	//			// String parameter = inputTuple.getAttribute(parameterPosition).toString();
	//			queryParameters[i] = inputTuple.getAttribute(parameterPosition);
	//		}
	//
	//		return queryParameters;
	//	}

	@Override
	protected void process_open() throws OpenFailedException {

		//		/* It is ensured, that the store size will never exceed the
		//		 * maximum size, therefore the loadCapacity may be set to 1
		//		 * to prevent rehashing. */
		//		Map<ComplexParameterKey, Tuple> cacheStore =
		//				new HashMap<ComplexParameterKey, Tuple>(cacheSize+1, 1.0f);
		//
		//		DBRetrievalStrategy dbRetrievalStrategy =
		//				new DBRetrievalStrategy(
		//						connectionName, query, variables, getOutputSchema());
		//
		//		System.out.println("RemovalStrategy: " + removalStrategy);
		//
		//		/* Instantiate removal strategy. A Switch case (Java 7), or a
		//		 * factory class could be used here if needed in the future. */
		//		String removalStrategyLCStr = removalStrategy.toLowerCase();
		//		IRemovalStrategy<ComplexParameterKey, Tuple> removalStrategy;
		//		if (removalStrategyLCStr.equals("random")) {
		//			removalStrategy = new Random<ComplexParameterKey, Tuple>(cacheStore);
		//		} else if (removalStrategyLCStr.equals("fifo")) {
		//			removalStrategy = new FIFO<ComplexParameterKey, Tuple>(cacheStore);
		//		} else if (removalStrategyLCStr.equals("lru")) {
		//			throw new RuntimeException("TBD");
		//		} else if (removalStrategyLCStr.equals("lfu")) {
		//			throw new RuntimeException("TBD");
		//		} else { // default
		//			removalStrategy = new FIFO<ComplexParameterKey, Tuple>(cacheStore);
		//		}
		//
		//		cacheManager = new ReadOnlyCache<ComplexParameterKey, Tuple>(
		//				cacheStore, dbRetrievalStrategy, removalStrategy, cacheSize);
		initParameterPositions();

		cacheManager.open();

		dataMergeFunction.init();
	}

	/**
	 * Goes through the variables (a set of inputTuples attribute
	 * names) and saves each position in parameterPositions[]. This has to be
	 * done only once and prevents therefore a search of the positions for
	 * each input tuple/ process_next-call.
	 */
	private void initParameterPositions() {
		for(int i=0; i<variables.size(); i++) {
			String variableName  = variables.get(i);

			// Get desired parameter from input tuple
			SDFAttribute attribute = getOutputSchema().findAttribute(variableName);
			if(attribute==null) {
				throw new RuntimeException("Could not find attribute '" + variableName +"' in input tuple.");
			}

			// save it's position
			parameterPositions[i] = getOutputSchema().indexOf(attribute);
		}
	}

	@Override
	protected void process_close() {
		cacheManager.close();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
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
