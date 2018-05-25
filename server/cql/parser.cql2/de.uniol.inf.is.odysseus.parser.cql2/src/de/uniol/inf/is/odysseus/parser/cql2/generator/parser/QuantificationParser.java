package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.QuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IQuantificationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;

public class QuantificationParser implements IQuantificationParser {

	private ISelectParser selectParser;
	private ICacheService cacheService;
	private AbstractPQLOperatorBuilder builder;
	
	@Inject
	public QuantificationParser(ICacheService cacheService, ISelectParser selectParser, AbstractPQLOperatorBuilder builder) {
		this.cacheService = cacheService;
		this.selectParser = selectParser;
		this.builder = builder;
	}
	
	@Override
	public String parse(ComplexPredicate predicate, SimpleSelect parent) throws PQLOperatorBuilderException {

		final QuantificationPredicate qp = predicate.getQuantification();
		final SimpleSelect select = predicate.getSelect().getSelect();
		final String quantification = qp.getPredicate();
		final Attribute attribute = qp.getAttribute();
		
		String operator = qp.getOperator();
		String type = "EXISTS";
		if (quantification.equalsIgnoreCase("ALL")) {
			type = "NOT_EXISTS";
			
			switch (operator) {
			case ">=":
				operator = "<";
				break;
			case ">":
				operator = "<=";
				break;
			case "<=":
				operator = ">";
				break;
			case "<":
				operator = ">=";
				break;
			}
			
		}
		
		// parse select and remove it from the processing queue after
		selectParser.prepare(select, null);
		selectParser.parseSingleSelect(select);
		cacheService.getSelectCache().remove(select);
		
		final String predicateString = createPredicateString(attribute, operator, select);
		final String input = cacheService.getOperatorCache().getLastOperator(parent) + "," + cacheService.getOperatorCache().last();
		
		Map<String, String> arguments = new HashMap<>();
		arguments.put("input", input);
		arguments.put("predicate", predicateString);
		arguments.put("type", type);
		
		return cacheService.getOperatorCache().add(select, builder.build(ExistenceAO.class, arguments));
	}

	private String createPredicateString(Attribute attribute, String operator, SimpleSelect parent) {

		StringBuilder predicateBuilder = new StringBuilder();
		
		int i = 0;
		final Collection<QueryAttribute> queryAttributes = cacheService.getQueryCache().getProjectionAttributes(parent);
		for(QueryAttribute queryAttribute : queryAttributes) {
			
			String name1 = attribute.getName();
			if (name1.contains(".")) {
				name1 = name1.replace(".", "_");
			}
			
			String name2 = queryAttribute.getName();
			if (name2.contains(".")) {
				name2 = name2.replace(".", "_");
			}
			
			predicateBuilder.append(name1 + " " + operator + " " + name2);
			
			if (i != queryAttributes.size() - 1) {
				predicateBuilder.append(" && ");
			}
			
			i++;
		}
		
		return predicateBuilder.toString();
	}

}
