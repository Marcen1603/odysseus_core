package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryNameFunction extends
		AbstractQueryInformationFunction<String> {

	private static final long serialVersionUID = -948427057065250120L;

	public QueryNameFunction() {
		super("QueryName", SDFDatatype.STRING);
	}

	@Override
	public String getValue() {
		int queryId = getNumericalInputValue(0).intValue();
		IPhysicalQuery query = getQuery(queryId);
		return query.getName().toString();
	}
}
