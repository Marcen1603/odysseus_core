package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService

class AttributeNameParser implements IAttributeNameParser {

	val Logger log = LoggerFactory.getLogger(AttributeNameParser)

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
			var SourceStruct tmp
			attribute = attributename
			source = if((tmp = utilityService.getSource(sourcename)) !== null) tmp.getName else null
		} else if (attributename.contains(".")) {
			var String[] split = attributename.split('\\.')
			attribute = split.get(1)
			source = split.get(0)
		}
		// //
		if (source !== null) {
			if (utilityService.isAggregationAttribute(attribute)) {
				log.info("YO-2")
				return attribute
			}
			var isAlias = utilityService.isAttributeAlias(attribute)
			if (utilityService.isSourceAlias(source)) {
				if (isAlias) {
					log.info("YO-2")
					return attribute
				} else {
					var r = source + '.' + attribute
					if (utilityService.getAttributeAliasesAsList().contains(r)) {
						log.info("YO-1")
						return r
					} else {
//						var sourcenameFromalias = utilityService.getSourceNameFromAlias(source)
						var attributeAliases = utilityService.getSource(source).findByName(attributename).getAliases
						if (!attributeAliases.empty) {
							log.info("YO0")
							return attributeAliases.get(0)
						} else {
							log.info("YO1")
							return source + '.' + attribute
						}
					}
				}
			} else if (isAlias) {
				log.info("YO2")
				return attribute
			} else {
				log.info("Y3")
				return source + '.' + attribute
			}
		} else {
			attribute = attributename
			log.info("HERE-1::" + attribute)
			log.info("aggregationAttributes=" + cacheService.getAggregationAttributeCache())
			if (cacheService.getAggregationAttributeCache().stream().map(e | e.e2).filter(p | p.equals(attributename)).findFirst().isPresent()) {
				return attributename
			}
			// TODO could be also containsKey
			if (cacheService.getExpressionCache().containsValue(attribute)) {
				return cacheService.getExpressionCache().get(attribute) as String
			}
			log.info("HERE0")
			if (utilityService.isAttributeAlias(attribute)) {
				return attribute
			}
			var containedBySources = newArrayList
//			var usedNames = newArrayList
			// TODO clear all data structures!!!!
			for (String name : SourceStruct.getQuerySources()) {
//				if (!usedNames.contains(name)) {
//					usedNames.add(name)
				log.info("querySource::" + name)
				var source2 = utilityService.getSource(name)
				if (source2.hasAttribute(attribute)) {
					containedBySources.add(source2)
				}
//				}
			}

			log.info("HERE1::" + containedBySources.toString())

			if (containedBySources.size == 1) {
				var aliases = utilityService.getSource(containedBySources.get(0).getName()).findByName(attribute).
					getAliases // getAliasFromAttributename(attribute, containedBySources.get(0).getName)
				if (!aliases.empty) {
//					var renames = //registry_RenamedAttributes.get(attribute)
//					if (renames === null || renames.empty) {
					if (utilityService.isAggregationAttribute(attribute)) {
						return attribute
					}
					log.info("HERE1 ALIASES NOT EMPTY")
					return aliases.get(0)
				}
				log.info("HERE2")
				var sourceStruct = utilityService.getSource(containedBySources.get(0).getName)
				if (!sourceStruct.aliasList.empty) {
//					var renames = registry_RenamedAttributes.get(attribute)
//					if (renames === null || renames.empty) {
//						return attribute
//					}
					return sourceStruct.aliasList.get(0) + '.' + attributename
				}
				log.info("HERE3")
				return containedBySources.get(0).getName + '.' + attribute
			}
			if (attributename.contains("()")) {
				return "${" + attributename.replace("(", "").replace(")", "") + "}"
			}
			log.info("HERE4")
			if (attributename.contains("$(")) {
				return attributename
			}
			log.info("HERE5")
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
