package de.uniol.inf.is.odysseus.cep.epa.metamodel.relational;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalJEPCondition extends JEPCondition {

	public RelationalJEPCondition(String jepExpression) {
		super(jepExpression);
	}

	public void setValue(CepVariable varName, Object newValue) {
		setValue_internal(varName, ((RelationalTuple)newValue).getAttribute(0));
	}
	
}
