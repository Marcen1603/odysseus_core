package de.uniol.inf.is.odysseus.dbenrich.physicaloperator;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractEnrichPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.dbenrich.IRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.cache.ComplexParameterKey;

public class DBEnrichPO<T extends IMetaAttribute> extends
		AbstractEnrichPO<Tuple<T>, T> {

	// Initialized in the constructor
	private final String connectionName;
	private final String query;
	private final List<String> variables;
	/**
	 * The positions of the db query parameters in the inputTuple attributes,
	 * ordered as in the db query
	 */
	private final int[] parameterPositions;
	private IRetrievalStrategy<ComplexParameterKey, List<IStreamObject<?>>> retrievalStrategie;

	public DBEnrichPO(
			String connectionName,
			String query,
			List<String> variables,
			IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
			ILeftMergeFunction<Tuple<T>, T> dataLeftMergeFunction,
			IMetadataMergeFunction<T> metaMergeFunction,
			IRetrievalStrategy<ComplexParameterKey, List<IStreamObject<?>>> retrievalStrategie,
			ICache cache, int[] uniqueKeys) {
		super(cache, dataMergeFunction, dataLeftMergeFunction, metaMergeFunction, uniqueKeys);
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.retrievalStrategie = retrievalStrategie;
		this.parameterPositions = new int[variables.size()];
	}

	public DBEnrichPO(DBEnrichPO<T> dBEnrichPO) {
		super(dBEnrichPO);
		this.connectionName = dBEnrichPO.connectionName;
		this.query = dBEnrichPO.query;
		this.variables = dBEnrichPO.variables;
		this.parameterPositions = Arrays.copyOf(dBEnrichPO.parameterPositions,
				dBEnrichPO.parameterPositions.length);
		System.err
				.println("The use of a copy constructor is only parially "
						+ "supported in DBEnrichPO. The cacheManager will not be copied.");
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected List<IStreamObject<?>> internal_process(Tuple<T> input) {
		Object[] queryParameters = getQueryParameters(input);
		ComplexParameterKey complexKey = new ComplexParameterKey(
				queryParameters);

		List<IStreamObject<?>> dbTupels = retrievalStrategie.get(complexKey);

		return dbTupels;
	}

	/**
	 * Returns the attributes, that correspond to the positions defined in
	 * parameterPositions. E.g. if the tuple contains the attributes
	 * [SomeText|123|456.7] and the positions are [2|1|3|2], then the result is
	 * [123|SomeText|456.7|123].
	 * 
	 * @param inputTuple
	 *            the current tuple from the input stream
	 * @return the corresponding parameters for a query
	 */
	private Object[] getQueryParameters(Tuple<T> inputTuple) {
		Object[] queryParameters = new Object[parameterPositions.length];

		for (int i = 0; i < queryParameters.length; i++) {
			queryParameters[i] = inputTuple.getAttribute(parameterPositions[i]);
		}

		return queryParameters;
	}

	@Override
	protected void internal_process_open() throws OpenFailedException {
		initParameterPositions();
		retrievalStrategie.open();
	}

	/**
	 * Goes through the variables (a set of inputTuples attribute names) and
	 * saves each position in parameterPositions[]. This has to be done only
	 * once and prevents therefore a search of the positions for each input
	 * tuple/ process_next-call.
	 */
	private void initParameterPositions() {
		for (int i = 0; i < variables.size(); i++) {
			String variableName = variables.get(i);

			// Get desired parameter from input tuple
			SDFAttribute attribute = getOutputSchema().findAttribute(
					variableName);
			if (attribute == null) {
				throw new RuntimeException("Could not find attribute '"
						+ variableName + "' in input tuple.");
			}

			// save it's position
			parameterPositions[i] = getOutputSchema().indexOf(attribute);
		}
	}

	@Override
	protected void internal_process_close() {
		retrievalStrategie.close();
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
		if (connectionName == null) {
			if (other.connectionName != null)
				return false;
		} else if (!connectionName.equals(other.connectionName))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;

	}
}
