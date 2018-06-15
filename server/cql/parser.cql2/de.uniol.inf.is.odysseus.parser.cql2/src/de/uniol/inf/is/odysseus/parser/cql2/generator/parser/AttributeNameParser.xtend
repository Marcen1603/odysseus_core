package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Optional

class AttributeNameParser implements IAttributeNameParser {

//	val Logger log = LoggerFactory.getLogger(AttributeNameParser)

	var IUtilityService utilityService
	var ICacheService cacheService
	var IRenameParser renameParser;

	@Inject
	new(IUtilityService utilityService, ICacheService cacheService, IRenameParser renameParser) {

		this.utilityService = utilityService
		this.cacheService = cacheService
		this.renameParser = renameParser;

	}

	override String parse(String attributename, String sourcename) {

		var String attribute
		var String source
		if (sourcename !== null && !sourcename.equals("")) {
			var SystemSource tmp
			attribute = attributename
			source = if((tmp = utilityService.getSystemSource(sourcename)) !== null) tmp.getName else null
		} else if (attributename.contains(".")) {
			var String[] split = attributename.split('\\.')
			attribute = split.get(1)
			source = split.get(0)
		}
		// //
		if (source !== null) {
			if (utilityService.isAggregationAttribute(attribute)) {
				return attribute
			}
			var isAlias = utilityService.isAttributeAlias(attribute)
			if (utilityService.isSourceAlias(source)) {
				if (isAlias) {
					return attribute
				} else {
					var r = source + '.' + attribute
					if (utilityService.getAttributeAliasesAsList().contains(r)) {
						return r
					} else {
//						var sourcenameFromalias = utilityService.getSourceNameFromAlias(source)
						var attributeAliases = utilityService.getSystemSource(source).findByName(attributename).getAliases
						if (!attributeAliases.empty) {
							return attributeAliases.get(0)
						} else {
							return source + '.' + attribute
						}
					}
				}
			} else if (isAlias) {
				return attribute
			} else {
				return source + '.' + attribute
			}
		} else {
			attribute = attributename
			
			if (utilityService.isAggregationAttribute(attribute)) {
				return attributename
			}
			// TODO could be also containsKey
			
			var Optional<String> expressionString = utilityService.getQueryExpressionString(attribute);
			
			if (expressionString.isPresent()) {
				return expressionString.get();
			}
			
//			if (cacheService.getExpressionCache().containsValue(attribute)) {
//				return cacheService.getExpressionCache().get(attribute) as String
//			}
			
			if (utilityService.isAttributeAlias(attribute)) {
				return attribute
			}
			var containedBySources = newArrayList
//			var usedNames = newArrayList
			// TODO clear all data structures!!!!
			for (String name : SystemSource.getQuerySources()) {
//				if (!usedNames.contains(name)) {
//					usedNames.add(name)
				var source2 = utilityService.getSystemSource(name)
				if (source2.hasAttribute(attribute)) {
					containedBySources.add(source2)
				}
//				}
			}


			if (containedBySources.size == 1) {
				var aliases = utilityService.getSystemSource(containedBySources.get(0).getName()).findByName(attribute).
					getAliases // getAliasFromAttributename(attribute, containedBySources.get(0).getName)
				if (!aliases.empty) {
//					var renames = //registry_RenamedAttributes.get(attribute)
//					if (renames === null || renames.empty) {
					if (utilityService.isAggregationAttribute(attribute)) {
						return attribute
					}
					return aliases.get(0)
				}
				var sourceStruct = utilityService.getSystemSource(containedBySources.get(0).getName)
				if (!sourceStruct.aliasList.empty) {
//					var renames = registry_RenamedAttributes.get(attribute)
//					if (renames === null || renames.empty) {
//						return attribute
//					}
					return sourceStruct.aliasList.get(0) + '.' + attributename
				}
				return containedBySources.get(0).getName + '.' + attribute
			}
			if (attributename.contains("()")) {
				return "${" + attributename.replace("(", "").replace(")", "") + "}"
			}
			if (attributename.contains("$(")) {
				return attributename
			}
		}

		throw new IllegalArgumentException("attribute " + attribute + " could not be resolved")
	}

	override parse(String attributename) {
		parse(attributename, null)
	}

	override parse(Attribute attribute) {
		if (attribute !== null) {
			parse(attribute.getName())
		}

	}

}
