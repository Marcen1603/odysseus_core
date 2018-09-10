package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExistPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
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
	
	
	@SuppressWarnings("unused")
	@Override
	public List<String> parse(ComplexPredicate predicate, SimpleSelect parent, List<String> predicateString, boolean negated) {
		
		final ExistPredicate qp = predicate.getExists();
		final InPredicate ip = predicate.getIn();
		final SimpleSelect select = predicate.getSelect().getSelect();
		final String symbol = qp.getPredicate();
		final boolean isExist = qp != null ? true : false;
		
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
		Optional<QueryPredicate> o = cacheService.getQueryCache().getPredicate(select);
		QueryPredicate queryPredicate = null;
		String predString = null;
		if (o.isPresent()) {
			
			queryPredicate = o.get();
			predString = queryPredicate.stringPredicate;
			
			for(QueryAttribute queryAttribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
				String name = queryAttribute.getName();
				if (name.contains(".")) {
					if (cacheService.getOperatorCache().getOperator(cacheService.getOperatorCache().getLastOperator(select)).get().contains("MAP(")) {
						predString = predString.replaceAll(name, name.replaceAll("\\.", "_"));
					}
				}
			}
			
			final Collection<QueryAttribute> attributes = new ArrayList<>(); 
			
			queryPredicate.predicate.stream().forEach(e -> {
				EcoreUtil2.getAllContentsOfType(e, Attribute.class).forEach(k -> {
					Optional<QueryAttribute> queryAttribute = utilityService.getQueryAttribute(k);
					if (queryAttribute.isPresent()) {
						attributes.add(queryAttribute.get());
					}
				});
			});
			
			for(QueryAttribute e : attributes) {
				String name = e.getName();
				if (!name.contains(".")) {
					if (e.sources.size() == 1) {
						name = e.sources.stream().findFirst().get().name + "." + e.getName();
					}
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
		
		try {
			cacheService.getOperatorCache().add(select, builder.build(ExistenceAO.class, arguments));
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
			
//			var type = 'EXISTS'
//					var operator = '=='
//					// save the current predicate
//					var predicateStringListBackup = new ArrayList(predicateStringList)
//					predicateStringList = newArrayList
//					var predicateBackup = predicateString
//					predicateString = ''
//
//					var select = complexPredicate.select.select
//					selectParser.prepare(select, null)
//					var predicate = ''
//					if (select.predicates === null) {
//						selectParser.parse(select)
//						for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//							predicate += in.attribute.name + operator + attribute.name + '&&'	
//						}
//						predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//					} else {
//						selectParser.parseWithPredicate(select)
//						for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//							predicate += in.attribute.name + operator + attribute.name + '&&'
//						}
//						predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//					}
////FIXME 31.12.17
////					for (Entry<String, Collection<String>> l : cacheService.getQueryCache().getQueryAttributes(select).entrySet)
////						for (String s : l.value) {
////							var attributename = s
////							if (!attributename.contains('.')) {
////								attributename = l.key + '.' + attributename
////							}
//////						println(attributename)
////							predicate = predicate.replace(attributename, attributename.replace('.', '_'))
////						}
//
////				predicate = predicate.replace('\\.', '_')
//					var Map<String, String> args = newHashMap
//					args.put('type', type)
//					args.put('input', cacheService.getOperatorCache().lastOperatorId())
//					args.put('predicate', predicate)
//					existenceParser.getOperators().add(args)
//
//					// restore predicate
//					predicateString = predicateBackup
//					predicateStringList = new ArrayList(predicateStringListBackup)
			
		return predicateString;
	}

}
