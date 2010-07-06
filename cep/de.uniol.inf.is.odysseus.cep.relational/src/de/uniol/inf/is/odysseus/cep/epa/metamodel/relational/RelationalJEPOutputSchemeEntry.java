package de.uniol.inf.is.odysseus.cep.epa.metamodel.relational;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPOutputSchemeEntry;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalJEPOutputSchemeEntry extends JEPOutputSchemeEntry {

	public RelationalJEPOutputSchemeEntry(String expression) {
		super(expression);
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		setValue_internal(varName, ((RelationalTuple)newValue).getAttribute(0));
	}

}
