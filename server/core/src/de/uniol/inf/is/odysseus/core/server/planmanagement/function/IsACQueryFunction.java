package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class IsACQueryFunction extends
		AbstractQueryInformationFunction<Boolean> {

	private static final long serialVersionUID = -948427057065250120L;
	
	public IsACQueryFunction() {
		super("IsACQuery", SDFDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		int queryId = getNumericalInputValue(0).intValue();		
		IPhysicalQuery physicalQuery = getQuery(queryId);
		return physicalQuery.isACquery();
	}
}
