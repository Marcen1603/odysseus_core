package de.uniol.inf.is.odysseus.cep.epa.symboltable.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalAvgSum;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalCount;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMinMax;

public class RelationalSymbolTableOperationFactory implements
		ISymbolTableOperationFactory {
	Map<String, IAggregateFunction> cache = new HashMap<String, IAggregateFunction>();

	@Override
	public IAggregateFunction getOperation(String name) {
		IAggregateFunction func = cache.get(name);
		if (func == null){
			func = createNewFunction(name);;
			cache.put(name, func);
		}
		return func;
	}

	private IAggregateFunction createNewFunction(String name) {
		IAggregateFunction func = null;
		if ("COUNT".equalsIgnoreCase(name)){
			func = RelationalCount.getInstance();
		}else if ("SUM".equalsIgnoreCase(name)){
			func = RelationalAvgSum.getInstance(0, false);
		}else if ("AVG".equalsIgnoreCase(name)){
			func = RelationalAvgSum.getInstance(0, true);
		}else if ("MAX".equalsIgnoreCase(name)){
			func = RelationalMinMax.getInstance(0, true);
		}else if ("MIN".equalsIgnoreCase(name)){
			func = RelationalMinMax.getInstance(0, false);
		}else if ("WRITE".equalsIgnoreCase(name)){
			func = Write.getInstance();
		}
		return func;
	}

}
