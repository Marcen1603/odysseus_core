package de.uniol.inf.is.odysseus.cep.metamodel.validator.warning;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationWarning;

public class UnreachableStateWarning extends ValidationWarning {

	public UnreachableStateWarning() {
		super();
	}

	@Override
	public String getWarningMessage() {
		return "The State is unreachable.";
	}
	
}
