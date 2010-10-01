package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO.Type;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class ExistenceAOBuilder extends AbstractOperatorBuilder {

	PredicateParameter predicate = new PredicateParameter("PREDICATE", REQUIREMENT.MANDATORY);
	DirectParameter<String> type = new DirectParameter<String>("TYPE", REQUIREMENT.MANDATORY);
	
	public ExistenceAOBuilder() {
		super(2,2);
		setParameters(predicate);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		ExistenceAO ao = new ExistenceAO();
		ao.setPredicate(predicate.getValue());
		if (type.getValue().equalsIgnoreCase("EXISTS")) {
			ao.setType(Type.EXISTS);
		} else {
			ao.setType(Type.NOT_EXISTS);
		}
		
		return ao;
	}

	@Override
	protected boolean internalValidation() {
		String typeStr = type.getValue();
		if (typeStr.equalsIgnoreCase("EXISTS") || typeStr.equalsIgnoreCase("NOT EXISTS")) {
			return true;
		} else {
			addError(new IllegalParameterException("Illegal value for type of existence AO, EXISTS or NOT EXISTS expected"));
			return false;
		}
	}

}
