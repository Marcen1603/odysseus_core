package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterMaxSheddingFactor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryMaxSheddingFactorFunction extends
		AbstractQueryInformationFunction<Integer> {

	private static final long serialVersionUID = -948427057065250120L;

	public QueryMaxSheddingFactorFunction() {
		super("MaxSheddingFactor", SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		int queryId = getNumericalInputValue(0).intValue();
		IPhysicalQuery query = getQuery(queryId);
		ILogicalQuery logicalQuery = query.getLogicalQuery();
		
		Object maxSheddingFactor = logicalQuery.getParameter(ParameterMaxSheddingFactor.class.getSimpleName());
		if( maxSheddingFactor != null && maxSheddingFactor instanceof Integer ) {
			return (Integer)maxSheddingFactor;
		}
		
		return 1;
	}
}
