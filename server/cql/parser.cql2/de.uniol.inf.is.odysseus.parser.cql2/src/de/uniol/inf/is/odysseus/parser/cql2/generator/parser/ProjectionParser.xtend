package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryExpression
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Collection

class ProjectionParser implements IProjectionParser {

	private AbstractPQLOperatorBuilder builder;
	private ISelectParser selectParser;
	private IRenameParser renameParser;
	private IUtilityService utilityService;
	private ICacheService cacheService;
	private IAttributeParser attributeParser;
	private IExpressionParser expressionParser;
	private var curStrRep = ''

	@Inject
	new(AbstractPQLOperatorBuilder builder, ISelectParser selectParser, IRenameParser renameParser,
		IUtilityService utilityService, ICacheService cacheService, IAttributeParser attributeParser,
		IExpressionParser expressionParser) {

		this.builder = builder;
		this.selectParser = selectParser;
		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.renameParser = renameParser;
		this.attributeParser = attributeParser;
		this.expressionParser = expressionParser;

	}

	override parse(Collection<QueryExpression> expressions, String input) {
		buildMapOperator(expressions)
	}

	override parse(SimpleSelect select, String operator) {
		return buildProjection(select, operator);
	}

	def private Object[] buildMapOperator(Collection<QueryExpression> expressions) {
		return buildMapOperator(expressions, null)
	}
	
	def private Object[] buildMapOperator(Collection<QueryExpression> queryExpressions, String input) {
		
		curStrRep = ''
		val Collection<String> stringList = newArrayList()
		val Collection<String> attributes = newArrayList()
		
		queryExpressions.stream().forEach(e | {
			
			var expressionName = ''
			var expressionString = e.alias
			
			if (e.name === null) {
				expressionName = attributeParser.getExpressionName()
			} else {
				expressionName = e.name
			}
			
			stringList.add(expressionString)
			stringList.add(expressionName)
			stringList.add(',')
			
			var t = utilityService.generateKeyValueString(stringList)
			
			curStrRep += t + ','
			
			stringList.clear()
			attributes.add(expressionName)
			
		})
		
		curStrRep = curStrRep.replaceAll(",$", "");
		
		var String build = builder.build(typeof(MapAO), newLinkedHashMap('expressions' -> curStrRep, 'input' -> input));
		
		return #[attributes, build]
	}

	def private String buildProjection(SimpleSelect select, CharSequence operator) {
		
		var Collection<QueryAttribute> attributes = cacheService.getQueryCache().getProjectionAttributes(select)
		
		// Add new aliases from the rename operation		
		for (var i = 0; i < renameParser.getAliases().size - 2; i = i + 3) {
			var attributename = renameParser.getAliases().get(i)
			var sourcename = renameParser.getAliases().get(i + 1)
			var alias = renameParser.getAliases().get(i + 2)
			utilityService.getSource(sourcename).addAliasTo(attributename, alias);
		}

		val list = newArrayList
		
		attributes.stream().forEach(e | {			list.add(utilityService.getProjectAttribute(e.name));
		})
		
		// Add new aliases from the rename operation		
		for (var i = 0; i < renameParser.getAliases().size - 2; i = i + 3) {
			var attributename = renameParser.getAliases().get(i)
			var sourcename = renameParser.getAliases().get(i + 1)
			var alias = renameParser.getAliases().get(i + 2)
			utilityService.getSource(sourcename).removeAliasFrom(attributename, alias)
		}
		var argument = utilityService.generateListString(list).replace("'['", "['").replace("']'", "']")
		return builder.build(typeof(MapAO), newLinkedHashMap('expressions' -> argument, 'input' -> operator.toString))
	}

}
