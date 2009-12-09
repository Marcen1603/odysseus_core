package de.uniol.inf.is.odysseus.cep.metamodel.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.AbstractSymbolTableOperation;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableOperationFactory;

public class SymbolTableOperationAdapter extends XmlAdapter<String, AbstractSymbolTableOperation<?,?>> {

	@Override
	public String marshal(AbstractSymbolTableOperation<?,?> arg0) throws Exception {
		return arg0.getClass().getSimpleName();
	}

	@Override
	public AbstractSymbolTableOperation<?,?> unmarshal(String arg0) throws Exception {		
		return SymbolTableOperationFactory.getOperation(arg0);
	}

}
