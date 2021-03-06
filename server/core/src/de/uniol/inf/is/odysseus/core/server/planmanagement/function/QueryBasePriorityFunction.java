package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryBasePriorityFunction extends
		AbstractQueryInformationFunction<Long> {

	private static final long serialVersionUID = -948427057065250120L;

	public QueryBasePriorityFunction() {
		super("QueryBasePriority", SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		int queryId = getNumericalInputValue(0).intValue();
		IPhysicalQuery query = getQuery(queryId);
		return query.getBasePriority();
	}
}
