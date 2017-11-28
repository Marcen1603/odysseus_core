package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import java.util.List
import java.util.Map
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO
import com.google.inject.Inject

class ExistenceParser implements IExistenceParser {
	
	var List<Map<String, String>> registry_existenceOperators = newArrayList
	
	var AbstractPQLOperatorBuilder builder;
	var ICacheService cacheService;
	
	@Inject
	new (AbstractPQLOperatorBuilder builder, ICacheService cacheService) {
		
		this.builder = builder;
		this.cacheService = cacheService;
		
	}
	
	override parse() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override register(SimpleSelect selectInput, String select) {
		if (!registry_existenceOperators.empty) {
			if (!select.equals('')) {
				for (Map<String, String> args : registry_existenceOperators) {
					var Map<String, String> newArgs = args
					newArgs.put('input', args.get('input') + ',' + selectInput)
					cacheService.getOperatorCache().registerOperator(builder.build(typeof(ExistenceAO), newArgs))
				}
				var t = cacheService.getOperatorCache().getOperator(select)
				cacheService.getOperatorCache().addOperator(select,
					t.substring(0, t.lastIndexOf('}')) + '},' + 'JOIN(' + cacheService.getOperatorCache().lastOperatorId() + ',' + selectInput + '))')
					
				//TODO this does not nothing, probably the reason for a bug	
//				var lastOperator = registry_OperatorNames.get(registry_OperatorNames.size - 1)
//				registry_OperatorNames.remove(lastOperator)
//				registry_OperatorNames.add(registry_OperatorNames.size - 1, lastOperator)
				// new method to swap operators
				cacheService.getOperatorCache().swapLastOperators();
			} else {
				for (Map<String, String> args : registry_existenceOperators) {
					var Map<String, String> newArgs = args
					newArgs.put('input', args.get('input') + ',' + selectInput)
					cacheService.getOperatorCache().registerOperator(builder.build(typeof(ExistenceAO), newArgs))
				}
			}
		}
	}
	
	override getOperator(int i) {
		registry_existenceOperators.get(i);
	}
	
	override getOperators() {
		return registry_existenceOperators;
	}
	
	
	
}