package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class GetSourceCountFunction extends
		AbstractQueryInformationFunction<Integer> {

	private static final long serialVersionUID = -948427057065250120L;
	
	public GetSourceCountFunction() {
		super("getSourceCount", SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		int queryId = getNumericalInputValue(0).intValue();		
		IPhysicalQuery physicalQuery = getQuery(queryId);
		return physicalQuery.getLeafSources().size();		
	}
}
