package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IAggregateFunction;

public class SymbolTableOperationFactory {

	static ISymbolTableOperationFactory fac = null;
	
	static public void setSymbolTableOperationFactory(ISymbolTableOperationFactory factory) {
		if (fac == null){
			fac = factory;
		}else{
			throw new RuntimeException("Multiple Symbole Table Operation Factories set");
		}
	}
	
	static public void unsetSymbolTableOperationFactory() {
		fac = null;
	}

	
	static public IAggregateFunction getOperation(String name) {
		IAggregateFunction op = fac.getOperation(name);
		return op;
	}

}
