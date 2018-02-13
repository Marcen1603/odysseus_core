package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.ArrayList
import java.util.Collection
import java.util.List

class RenameParser implements IRenameParser {

//	var Logger log = LoggerFactory.getLogger(RenameParser)

	var IUtilityService utilityService
	var AbstractPQLOperatorBuilder builder
	var IJoinParser joinParser
	var ICacheService cacheService

	var Collection<String> renameAliases
	var Collection<String> processedSources
	var Collection<QuerySource> sourcesDuringRename
	
	@Inject
	new(AbstractPQLOperatorBuilder builder, IUtilityService utilityService, IJoinParser joinParser, ICacheService cacheService) {
		
		this.utilityService = utilityService;
		this.builder = builder;
		this.joinParser = joinParser;
		this.cacheService = cacheService;
		
		renameAliases = newArrayList
		processedSources = newArrayList
		sourcesDuringRename = newArrayList
		
	}

	override CharSequence buildRename(CharSequence input, SimpleSource simpleSource, SimpleSelect select, int selfJoin) {
		
		val listOfLists= newArrayList()
		
		// get SourceStruct to the given source
		val SystemSource source = utilityService.getSystemSource(simpleSource)	
		// get alias from source if available
		val String sourcealias = if (simpleSource.alias !== null) simpleSource.alias.name else null;
		
		// TODO add description
//		if (sourcealias !== null) {
			
			for (var j = 0; j < source.getAttributeList().size; j++) {
				
				var attr = source.getAttributeList().get(j);
				var k = 0;
				var l = 0;
				
				while(l < attr.aliases.size) {
					
					var attrAlias = attr.aliases.get(l);
					var sourceAlias = source.getAssociatedSource(attrAlias);
					
//					if (sourceAlias.equals(sourcealias) || sourceAlias.equals(simpleSource.name)) {
				if (sourceAlias !== null && (sourceAlias.equals(sourcealias) || sourceAlias.equals(simpleSource.name))) {
						
						var List<String> col
						
						if (listOfLists.size <= k) {
							col = newArrayList
						} else {
							col = listOfLists.get(k)
						}
						
						col.add(attr.attributename)
						col.add(attrAlias)
						
						if(listOfLists.size <= k) {
							listOfLists.add(col)
						}
						k++
					}
					l++
				}
			}
			
		// add rename operators for self-joins			
		if (listOfLists.size > 1 || selfJoin > 0 || sourcealias !== null) {
			for (var j = 0; j < listOfLists.size; j++) {
				var list = listOfLists.get(j)
				for (var k = 0; k < source.getAttributeList().size; k++) {
					if (!list.contains(source.getAttributeList().get(k).attributename)) {
						
						var String alias = null
						var name = source.getAttributeList().get(k).attributename
						if (sourcealias !== null) {
							if (j > 0 && listOfLists.size > 1) {
								alias = generateAlias(name, source.getName, j)
							} else {
								alias = sourcealias + '.' + name
							}						
						}
						renameAliases.add(name)
						renameAliases.add(source.getName)
						renameAliases.add(alias)
						list.add(name)
						list.add(alias)
						
					}
				}
			}
		}

		var renames = newArrayList
		processedSources.add(source.getName)
		for (var j = 0; j < listOfLists.size; j++) {
			renames.add(parse(listOfLists.get(j), input.toString, select))
		}
		
		if (renames.size > 1) {
			return joinParser.buildJoin(renames)
		}
		
		if (renames.size == 1) {
			return renames.get(0)
		}
		
		return input
	}
	
	override parse(Collection<String> groupAttributes, String input, SimpleSelect select) {
		return cacheService.getOperatorCache().add(select, builder.build(typeof(RenameAO), newHashMap('pairs' -> 'true', 'aliases' -> utilityService.generateListString(groupAttributes), 'input' -> input)))
	}

	def private String generateAlias(String attributename, String sourcename, int number) {
		var alias = sourcename + '.' + attributename + '#' + (number)
		if (renameAliases.contains(alias)) {
			return alias = generateAlias(attributename, sourcename, number + 1)
		}
		return alias
	}
	
	override getAliases() {
		return renameAliases;
	}
	
	override getSources() {
		return sourcesDuringRename
	}
	
	override clear() {
		renameAliases.clear();
		processedSources.clear();
		sourcesDuringRename.clear();
	}
	
	override setSources(Collection<QuerySource> sources) {
		if(sources !== null) {
			sourcesDuringRename = new ArrayList(sources)
		}
	}
	
}