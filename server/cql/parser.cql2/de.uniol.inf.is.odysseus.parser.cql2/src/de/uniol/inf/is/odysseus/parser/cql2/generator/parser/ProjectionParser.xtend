package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryExpression
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IProjectionParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser
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
			var expressionString = e.parsedExpression.toString()
			
			if (e.parsedExpression.name === null) {
				expressionName = attributeParser.getExpressionName()
			} else {
				expressionName = e.parsedExpression.name
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
			utilityService.getSystemSource(sourcename).addAliasTo(attributename, alias);
		}

		val list = newArrayList
		
		attributes.stream().forEach(e | {
			
			if (e.type.equals(IParsedObject.Type.EXPRESSION)) {
				list.add((e as QueryExpression).parsedExpression.toString())			
			} else {
				
				var String name = "";
				
				if (e.type.equals(IParsedObject.Type.AGGREGATION)) {
					name = (e as QueryAggregate).parsedAggregation.getName();
				} else {
					name = e.parsedAttribute.toString();
				}
				
				list.add(name);
			}
			
		})
		
		// Add new aliases from the rename operation		
		for (var i = 0; i < renameParser.getAliases().size - 2; i = i + 3) {
			var attributename = renameParser.getAliases().get(i)
			var sourcename = renameParser.getAliases().get(i + 1)
			var alias = renameParser.getAliases().get(i + 2)
			utilityService.getSystemSource(sourcename).removeAliasFrom(attributename, alias)
		}
		var argument = utilityService.generateListString(list).replace("'['", "['").replace("']'", "']")
		return builder.build(typeof(MapAO), newLinkedHashMap('expressions' -> argument, 'input' -> operator.toString))
	}

}
