package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IAggregateFunction;

public interface ISymbolTableOperationFactory {

	public IAggregateFunction getOperation(String name);

}
