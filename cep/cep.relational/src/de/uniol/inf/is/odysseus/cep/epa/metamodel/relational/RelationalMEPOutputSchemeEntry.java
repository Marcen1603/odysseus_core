package de.uniol.inf.is.odysseus.cep.epa.metamodel.relational;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.MEPOutputSchemeEntry;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMEPOutputSchemeEntry extends MEPOutputSchemeEntry {

	public RelationalMEPOutputSchemeEntry(String expression){
		super(expression);
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		super.setValue(varName, ((RelationalTuple)newValue).getAttribute(0));
	}

}
