package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IWindowParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.ArrayList
import java.util.Arrays
import java.util.Collection
import java.util.List
import java.util.Optional
import java.util.stream.Collectors

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

	override public String buildJoin(Collection<QuerySource> sources, SimpleSelect select) {
		var String[] sourceStrings = newArrayOfSize(sources.size)
		var Collection<String> sourcenames = newArrayList

		for (var i = 0; i < sources.size; i++) {
			
			var source = sources.get(i).source
			if (source instanceof NestedSource) {
				
				val Optional<SubQuery> o = cacheService.getQueryCache().getSubQuery(source.alias.name)
				if (o.isPresent()) {
					
					val SubQuery subQuery = o.get();
					val Collection<QueryAttribute> col = cacheService.getQueryCache().getProjectionAttributes(subQuery.select)
						.stream()
						.filter(p | p.referenceOf != null)
						.collect(Collectors.toList);
					
					val Collection<String> renames = newArrayList()
					col.stream().forEach(e | {
						
						renames.add(e.parsedAttribute.name.replace(".", "_"));
						renames.add(if (e.referenceOf.parsedAttribute.alias != null) e.referenceOf.parsedAttribute.alias else e.referenceOf.parsedAttribute.name)
						
					})					
					
					sourceStrings.set(i, renameParser.parse(renames, subQuery.operator, select))
				}
			} else if (source instanceof SimpleSource) {
				
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
						select,
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
