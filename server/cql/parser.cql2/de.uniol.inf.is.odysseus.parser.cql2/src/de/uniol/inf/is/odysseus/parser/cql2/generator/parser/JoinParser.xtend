package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import java.util.ArrayList
import java.util.Arrays
import java.util.Collection
import java.util.Map.Entry
import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import java.util.List
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService

class JoinParser implements IJoinParser {

	val Logger log = LoggerFactory.getLogger(typeof(JoinParser))

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

//		var simpleSources = sources.stream.filter(e|e instanceof SimpleSource).map(e|e as SimpleSource).collect(
//			Collectors.toList)
//		var subQueries = sources.stream.filter(e|e instanceof NestedSource).map(e|e as NestedSource).collect(
//			Collectors.toList)
		for (var i = 0; i < sources.size; i++) {
			var source = sources.get(i)
			if (source instanceof NestedSource) {
				var query = cacheService.getSelectCache().last()
				var queryAttributess = cacheService.getQueryCache().getQueryAttributes(query)
//				var queryAggregations = utilityServicetil.getQueryAggregations(query)
				var subQuery = source.statement.select as SimpleSelect
				var subQueryAttributes = cacheService.getQueryCache().getQueryAttributes(subQuery)
//				var subQueryAggregations = utilityServicetil.getQueryAggregations(subQuery)
				var lastOperator = cacheService.getOperatorCache().getSubQueries().get(subQuery)
				var inputs = newArrayList
				var attributeAliases = utilityService.getAttributeAliasesAsList()
//				var allQuerAttributes = utilityServicetil.getAllQueryAttributes(query)
//				var allSubQuerAttributes = utilityServicetil.getAllQueryAttributes(subQuery)
				for (Entry<String, Collection<String>> entry : queryAttributess.entrySet) {
					var attributes = subQueryAttributes.get(entry.key)
					if (attributes !== null) {
						var aliasses = newArrayList
						for (String name : attributes)
							for (String name2 : entry.value) {
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
						inputs.add(
							cacheService.getOperatorCache().registerOperator(
								builder.build(typeof(RenameAO),
									newHashMap('aliases' -> utilityService.generateListString(aliasses),
										'pairs' -> 'true', 'input' -> lastOperator))))
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
				var op = builder.build(typeof(RenameAO),
					newHashMap('aliases' -> utilityService.generateListString(aliasses), 'pairs' -> 'true',
						'input' -> lastOperator))
				inputs.add(cacheService.getOperatorCache().registerOperator(op))

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
