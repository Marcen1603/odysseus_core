package de.uniol.inf.is.odysseus.cep.epa.metamodel.relational;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.MEPCondition;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMEPCondition extends MEPCondition {

	public RelationalMEPCondition(String jepExpression){
		super(jepExpression);
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		super.setValue(varName,((RelationalTuple)newValue).getAttribute(0));
	}

	
}
