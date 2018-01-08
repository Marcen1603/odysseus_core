package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.ArrayList
import java.util.Arrays
import java.util.Collection
import java.util.List

class JoinParser implements IJoinParser {

//	val Logger log = LoggerFactory.getLogger(typeof(JoinParser))

	var IUtilityService utilityService
	var ICacheService cacheService
	var AbstractPQLOperatorBuilder builder
	var IRenameParser renameParser
	var IWindowParser windowParser
	var boolean firstJoinInQuery

	@Inject
	new(IUtilityService utilityService, ICacheService cacheService, AbstractPQLOperatorBuilder builder,
		IRenameParser renameParser, IWindowParser windowParser) {

		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.builder = builder;
		this.renameParser = renameParser;
		this.windowParser = windowParser;

	}

	override public String buildJoin(Collection<Source> sources) {
		var String[] sourceStrings = newArrayOfSize(sources.size)
		var Collection<String> sourcenames = newArrayList

		for (var i = 0; i < sources.size; i++) {
			var source = sources.get(i)
			if (source instanceof NestedSource) {
				var query = cacheService.getSelectCache().last()
				
				var Collection<QueryAttribute> queryAttributess = cacheService.getQueryCache().getQueryAttributes(query)
				

				var subQuery = source.statement.select as SimpleSelect
				var Collection<QueryAttribute> subQueryAttributes = cacheService.getQueryCache().getQueryAttributes(subQuery)

				var lastOperator = cacheService.getOperatorCache().getSubQueries().get(subQuery)
				var inputs = newArrayList
				
				var attributeAliases = utilityService.getAttributeAliasesAsList()
				
				for (QueryAttribute entry : queryAttributess) {
					for (QueryAttribute entry2 : subQueryAttributes) {
						if (entry2 !== null) {
							var aliasses = newArrayList
							for (String name : entry2.sources) {
								for (String name2 : entry.sources) {
									var realName = name
									var realName2 = name2
									if (attributeAliases.contains(realName))
										realName = utilityService.getAttributenameFromAlias(realName)
									if (attributeAliases.contains(realName2))
										realName2 = utilityService.getAttributenameFromAlias(realName2)
	
									if (realName.contains('.'))
										realName = name.split('\\.').get(1)
									if (realName2.contains('.'))
										realName2 = realName2.split('\\.').get(1)
	
									if (realName.equals(realName2)) {
										aliasses.add(name.replace('.', '_'))
										aliasses.add(name2)
									}
								}	
							}
							inputs.add(renameParser.parse(aliasses, lastOperator))
							/*TODO remove
							 * builder.build(typeof(RenameAO),
										newHashMap('aliases' -> utilityService.generateListString(aliasses),
											'pairs' -> 'true', 'input' -> lastOperator))
							 */
						}
					}
				}
				// build rename operator for sub query alias
				var aliasses = newArrayList
				var subQueryAlias = source.alias.name
				for (String name : utilityService.getAllQueryAttributes(subQuery)) {
					var realName = name
					if (realName.contains(".")) {
						realName = realName.substring(realName.indexOf(".") + 1, realName.length)
						aliasses.add(name.replace(".", "_"))
					} else {
						aliasses.add(name)
					}
					if (utilityService.isAggregationAttribute(name)) {
						aliasses.add(subQueryAlias + "." + realName)
					} else {
						aliasses.add(subQueryAlias + "." + realName)
					}
				}
				var op = renameParser.parse(aliasses, lastOperator)
						/*
						 * builder.build(typeof(RenameAO),
					newHashMap('aliases' -> utilityService.generateListString(aliasses), 'pairs' -> 'true',
						'input' -> lastOperator))
						 */
				inputs.add(op)

//				if (inputs.size == 0) {
//					inputs.add(lastOperator)
//				}
				sourceStrings.set(i, buildJoin(inputs).toString)
			} else if (source instanceof SimpleSource) {
				// Reset sorucesDuringrename?
//				println("buildJoin() --> SimpleSource")
				// Check for self join
				val sourcename = source.name
				val count = sourcenames.stream.filter(e|e.equals(sourcename)).count
				sourcenames.add(sourcename)
				renameParser.setSources(sources)
				sourceStrings.set(
					i,
					renameParser.buildRename(
						windowParser.parse(source),
						source,
						count as int
					).toString
				)
			}
		}
		return buildJoin(sourceStrings)
	}

	override public String buildJoin(String[] srcs) {
		var sourcenames = srcs
		if (sourcenames.size < 1) {
			throw new IllegalArgumentException("Invalid number of source elements: There has to be at least one source")
		}
		
		// Will only be considered if the first call of this method provides a single source
		if (sourcenames.size == 1) {
			firstJoinInQuery = true
			return sourcenames.get(0)
		}
		
		var List<String> list = new ArrayList(Arrays.asList(sourcenames))
		if (list.size == 2) {
			firstJoinInQuery = true
			return '''JOIN(«sourcenames.get(0)»,«sourcenames.get(1)»)'''
		}
		list.remove(0)
		return '''JOIN(«sourcenames.get(0)»,«buildJoin(list)»)'''
	}

	override public boolean isJoinInQuery() {
		return firstJoinInQuery
	}

	override clear() {
		firstJoinInQuery = true

	}

}
