package de.uniol.inf.is.odysseus.cep.metamodel.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableOperation;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableOperationFactory;

public class SymbolTableOperationAdapter extends XmlAdapter<String, SymbolTableOperation<?>> {

	@Override
	public String marshal(SymbolTableOperation<?> arg0) throws Exception {
		return arg0.getClass().getSimpleName();
	}

	@Override
	public SymbolTableOperation<?> unmarshal(String arg0) throws Exception {		
		return SymbolTableOperationFactory.getOperation(arg0);
	}

}
