package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class SaseAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -4928010950616650135L;
	
	private final StringParameter query = new StringParameter(
			"QUERY", REQUIREMENT.MANDATORY);
	
	
	public SaseAOBuilder(int minPortCount, int maxPortCount) {
		super(1, Integer.MAX_VALUE);
		addParameters(query);
	}

	@Override
	public IOperatorBuilder cleanCopy() {
		return null;
	}

	@Override
	protected boolean internalValidation() {
		return false;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SaseBuilder parser = new SaseBuilder();
		ILogicalOperator ret = null;
		try {
			List<IQuery> op = parser.parse(query.getValue(), getCaller(), getDataDictionary(), false);
			// I know there is only one operator
			ret = op.get(0).getLogicalPlan();
		} catch (QueryParseException e) {
			throw new RuntimeException(e);
		}
		
		return ret;
	}

}
