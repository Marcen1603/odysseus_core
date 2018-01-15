package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExistPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class ExistenceParser implements IExistenceParser {

	private ISelectParser selectParser;
	private ICacheService cacheService;
	private IUtilityService utilityService;
	private AbstractPQLOperatorBuilder builder;
	
	@Inject
	public ExistenceParser(ICacheService cacheService, ISelectParser selectParser, IUtilityService utilityService, AbstractPQLOperatorBuilder builder) {
		this.cacheService = cacheService;
		this.selectParser = selectParser;
		this.utilityService = utilityService;
		this.builder = builder;
	}
	
	
	@Override
	public List<String> parse(ComplexPredicate predicate, SimpleSelect parent, List<String> predicateString, boolean negated) {
		
		final ExistPredicate qp = predicate.getExists();
		final SimpleSelect select = predicate.getSelect().getSelect();
		final String symbol = qp.getPredicate();
		
		int max = predicateString.size();
		
		String type = "EXISTS";
		if (negated) {
			type = "NOT_EXISTS";
			predicateString.remove(max - 1);
			if (predicateString.size() > 0) {
				int index = predicateString.size() - 2;
				final String e = predicateString.get(index);
				if (e.equals("&&") || e.equals("||")) {
					predicateString.remove(index);
				}
			} else if (predicateString.get(max - 1).equals("&&") || predicateString.get(max - 1).equals("||")) {
				if ( predicateString.size() > 0) {
					predicateString.remove(max -1);
				}
			}
		}
		
		
		// parse select and remove it from the processing queue after
		selectParser.prepare(select, null);
		selectParser.fooor(select);
		selectParser.parseSingleSelect(select);
//		cacheService.getSelectCache().remove(select);
		Optional<QueryPredicate> o = cacheService.getQueryCache().getPredicate(select);
		QueryPredicate queryPredicate = null;
		String predString = null;
		if (o.isPresent()) {
			
			queryPredicate = o.get();
			predString = queryPredicate.stringPredicate;
			
			for(QueryAttribute queryAttribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
				String name = queryAttribute.name;
				if (name.contains(".")) {
					if (cacheService.getOperatorCache().getOperator(cacheService.getOperatorCache().getLastOperator(select)).get().contains("MAP(")) {
						predString = predString.replaceAll(name, name.replaceAll("\\.", "_"));
					}
				}
			}
			
			for(QueryAttribute e : queryPredicate.attributes) {
				String name = e.name;
				if (!name.contains(".")) {
					name = e.sources.stream().findFirst().get().name + "." + e.name;
				}
				if (cacheService.getOperatorCache().getOperator(cacheService.getOperatorCache().getLastOperator(select)).get().contains("MAP(")) {
					predString = predString.replaceAll(name, name.replaceAll("\\.", "_"));
				}
			};
			
		}
		cacheService.getSelectCache().remove(select);
		
		Map<String, String> arguments = new HashMap<>();
		arguments.put("type", type);
		arguments.put("predicate", predString);
		final String input = cacheService.getOperatorCache().getLastOperator(parent) + "," + cacheService.getOperatorCache().last();
		arguments.put("input", input);
		
		cacheService.getOperatorCache().add(select, builder.build(ExistenceAO.class, arguments));
		
		return predicateString;
	}

	@Override
	public void register(SimpleSelect input, String select) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getOperator(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Map<String, String>> getOperators() {
		// TODO Auto-generated method stub
		return null;
	}

}
