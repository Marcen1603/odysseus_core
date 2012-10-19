package de.uniol.inf.is.odysseus.dbenrich.physicaloperator;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;
import de.uniol.inf.is.odysseus.dbenrich.cache.IReadOnlyCache;

public class DBEnrichPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	// Initialized in the constructor
	private final String connectionName;
	private final String query;
	private final List<String> variables;
	private final boolean multiTupleOutput;
	private final boolean noCache;
	private final int cacheSize;
	private final long expirationTime;
	private final String removalStrategy;
	// Fully initialized after process_open()
	private final IDataMergeFunction<Tuple<T>> dataMergeFunction;
	private final IReadOnlyCache<ComplexParameterKey, Tuple<?>[]> cacheManager;
	/** The positions of the db query parameters in the inputTuple attributes,
	 * ordered as in the db query */
	private final int[] parameterPositions;

	public DBEnrichPO(String connectionName, String query,
			List<String> variables, boolean noCache, boolean multiTupleOutput, 
			int cacheSize, long expirationTime, String removalStrategy,
			IDataMergeFunction<Tuple<T>> dataMergeFunction,
			IReadOnlyCache<ComplexParameterKey, Tuple<?>[]> cacheManager) {
		super();
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.multiTupleOutput = multiTupleOutput;
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
		this.multiTupleOutput = dBEnrichPO.multiTupleOutput;
		this.noCache = dBEnrichPO.noCache;
		this.cacheSize = dBEnrichPO.cacheSize;
		this.expirationTime = dBEnrichPO.expirationTime;
		this.removalStrategy = dBEnrichPO.removalStrategy;
		this.dataMergeFunction = dBEnrichPO.dataMergeFunction.clone();
		this.cacheManager = dBEnrichPO.cacheManager; // better provide clone();
		this.parameterPositions = Arrays.copyOf(
				dBEnrichPO.parameterPositions,
				dBEnrichPO.parameterPositions.length);
		System.err.println("The use of a copy constructor is only parially " +
				"supported in DBEnrichPO. The cacheManager will not be copied.");
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	@SuppressWarnings("unchecked") // Suppress metadata-cast warnings
	protected synchronized void process_next(Tuple<T> inputTuple, int port) {
		// System.out.println(String.format("(%28s-------", getName()).replace(' ', '-'));
		// System.out.println("Tuple(before):  "+inputTuple);

		// get the parameters used for the db query from the tuple attributes
		Object[] queryParameters = getQueryParameters(inputTuple);
		ComplexParameterKey complexKey = new ComplexParameterKey(queryParameters);

		Tuple<?>[] dbTupels = cacheManager.get(complexKey);
		
		/*
		 * If multiTupleOutput is set, there will be as many output-tuples 
		 * generated as there are tuples in the db resultset.
		 * Otherwise the array dbTupels has the length '1' and only the first 
		 * result tuple from the db will used for the single output tuple.
		 * If the db resultset was empty, no tuple will be transfered.
		 */
		for(int i=0; i<dbTupels.length; i++) {
			// System.out.println("Tuple(dbcache): " + dbTupels[i]);

			Tuple<T> outputTuple = dataMergeFunction.merge(
					inputTuple, (Tuple<T>)dbTupels[i]);
			// The the metadata of the inputtuple
			outputTuple.setMetadata((T)inputTuple.getMetadata().clone());

			// System.out.println("Tuple(after,"+i+1+"): "+outputTuple);

			transfer(outputTuple, port);
		}
		
		// System.out.println("-----------------------------------)");
	}

	/**
	 * Returns the attributes, that correspond to the positions defined in
	 * parameterPositions. E.g. if the tuple contains the attributes
	 * [SomeText|123|456.7] and the positions are [2|1|3|2], then the
	 * result is [123|SomeText|456.7|123].
	 * @param inputTuple the current tuple from the input stream
	 * @return the corresponding parameters for a query
	 */
	private Object[] getQueryParameters(Tuple<T> inputTuple) {
		Object[] queryParameters = new Object[parameterPositions.length];

		for(int i=0; i<queryParameters.length; i++) {
			queryParameters[i] =
					inputTuple.getAttribute(parameterPositions[i]);
		}

		return queryParameters;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
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
	protected synchronized void process_close() {
		cacheManager.close();
	}

	@Override
	public AbstractPipe<Tuple<T>, Tuple<T>> clone() {
		return new DBEnrichPO<T>(this);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		// Compare all class attributes, that were already present in the AO.
		if (this == ipo)
			return true;
		if (!super.equals(ipo))
			return false;
		if (getClass() != ipo.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		DBEnrichPO other = (DBEnrichPO) ipo;
		if (cacheSize != other.cacheSize)
			return false;
		if (connectionName == null) {
			if (other.connectionName != null)
				return false;
		} else if (!connectionName.equals(other.connectionName))
			return false;
		if (expirationTime != other.expirationTime)
			return false;
		if (multiTupleOutput != other.multiTupleOutput)
			return false;
		if (noCache != other.noCache)
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (removalStrategy == null) {
			if (other.removalStrategy != null)
				return false;
		} else if (!removalStrategy.equals(other.removalStrategy))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}
}
