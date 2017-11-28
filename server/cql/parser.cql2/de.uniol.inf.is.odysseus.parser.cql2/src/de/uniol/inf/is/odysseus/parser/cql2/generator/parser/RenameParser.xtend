package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import java.util.ArrayList
import java.util.Collection
import java.util.List
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct

class RenameParser implements IRenameParser {

	var IUtilityService utilityService
	var AbstractPQLOperatorBuilder builder
	var IJoinParser joinParser
	var ICacheService cacheService

	var Collection<String> renameAliases
	var Collection<String> processedSources
	var Collection<Source> sourcesDuringRename
	
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

	override CharSequence buildRename(CharSequence input, SimpleSource simpleSource, int selfJoin) {
		
		var listOfLists = newArrayList()
		
		// get SourceStruct to the given source
		var SourceStruct source = utilityService.getSource(simpleSource)	
		// get alias from source if available
		var String sourcealias = if (simpleSource.alias !== null) simpleSource.alias.name else null;
		// get list of attributes of SourceStruct
		var attributeList = source.getAttributeList();
		
		for (var j = 0; j < attributeList.size; j++) {
			var k = 0
			for (String attributealias : attributeList.get(j).aliases) {
				var sourceFromAlias = source.getAssociatedSource(attributealias)//utilityService.getAttributeAliases.get(attributealias)
				if (sourceFromAlias !== null && (sourceFromAlias.equals(sourcealias) || sourceFromAlias.equals(simpleSource.name))) {
					var b = listOfLists.size <= k
					var List<String> list
					if (b)
						list = newArrayList
					else
						list = listOfLists.get(k)
					list.add(attributeList.get(j).attributename)
					list.add(attributealias)
					if(b) listOfLists.add(list)
					k++
				}
			}
		}
		
		// self join			
		if (listOfLists.size > 1 || selfJoin > 0 || sourcealias !== null) {
			for (var j = 0; j < listOfLists.size; j++) {
				var list = listOfLists.get(j)
				for (var k = 0; k < attributeList.size; k++)
					if (!list.contains(attributeList.get(k).attributename)) {
						var String alias = null
						var name = attributeList.get(k).attributename
						if (sourcealias !== null)
							if (j > 0 && listOfLists.size > 1)
								alias = generateAlias(name, source.getName, j)
							else
								alias = sourcealias + '.' + name

						renameAliases.add(name)
						renameAliases.add(source.getName)
						renameAliases.add(alias)
						list.add(name)
						list.add(alias)
					}
			}
		}

		var renames = newArrayList
		processedSources.add(source.getName)
//		renames.add(source.sourcename)
		for (var j = 0; j < listOfLists.size; j++)
			renames.add(
				cacheService.getOperatorCache().registerOperator(
					builder.build(typeof(RenameAO),
						newLinkedHashMap('aliases' -> utilityService.generateListString(listOfLists.get(j)), 'pairs' -> 'true',
							'input' -> input.toString))))
		if (renames.size > 1)
			return joinParser.buildJoin(renames)
		if (renames.size == 1)
			return renames.get(0)
		return input
	}

	def private String generateAlias(String attributename, String sourcename, int number) {
		var alias = sourcename + '.' + attributename + '#' + (number)
		if (renameAliases.contains(alias))
			return alias = generateAlias(attributename, sourcename, number + 1)
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
	
	override setSources(Collection<Source> sources) {
		if(sources !== null) {
			sourcesDuringRename = new ArrayList(sources)
		}
		
	}
	
}