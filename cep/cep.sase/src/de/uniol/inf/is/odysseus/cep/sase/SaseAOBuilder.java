package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;

import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

public class SaseAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -4928010950616650135L;

	private final StringParameter query = new StringParameter("QUERY",
			REQUIREMENT.MANDATORY);
	
	private final BooleanParameter oneMatchPerInstance = new BooleanParameter("OneMatchPerInstance",
			REQUIREMENT.OPTIONAL);

	public SaseAOBuilder() {
		super(1, Integer.MAX_VALUE);
		addParameters(query, oneMatchPerInstance);
	}

	@Override
	public IOperatorBuilder cleanCopy() {
		return new SaseAOBuilder();
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SaseBuilder parser = new SaseBuilder();
		ILogicalOperator ret = null;
		List<ILogicalQuery> op = parser.parse(query.getValue(), getCaller(),
				getDataDictionary(), false);
		// I know there is only one operator
		ret = op.get(0).getLogicalPlan();
		if (oneMatchPerInstance.hasValue()){
			((CepAO<?>)ret).setOneMatchPerInstance(oneMatchPerInstance.getValue());
		}

		return ret;
	}

}
