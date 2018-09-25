package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.Set;
import java.util.function.Predicate;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class GetSharedOpsCountFunction extends
		AbstractQueryInformationFunction<Long> {

	private static final long serialVersionUID = -948427057065250120L;
	
	public GetSharedOpsCountFunction() {
		super("getSharedOpsCount", SDFDatatype.INTEGER);
	}

	final Predicate<IPhysicalOperator> isShared = op -> op.getOwner().size() > 1;
 	
	@Override
	public Long getValue() {
		int queryId = getNumericalInputValue(0).intValue();		
		IPhysicalQuery physicalQuery = getQuery(queryId);
		Set<IPhysicalOperator> ops = physicalQuery.getAllOperators();		
		return ops.stream().filter(isShared).count();
	}
}
