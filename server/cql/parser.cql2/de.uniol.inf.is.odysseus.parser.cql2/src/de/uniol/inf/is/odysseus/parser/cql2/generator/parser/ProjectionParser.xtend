package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import java.util.Collection
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import java.util.List
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService

class ProjectionParser implements IProjectionParser {

	private AbstractPQLOperatorBuilder builder;
	private ISelectParser selectParser;
	private IRenameParser renameParser;
	private IUtilityService utilityService;
	private ICacheService cacheService;

	@Inject
	new(AbstractPQLOperatorBuilder builder, ISelectParser selectParser, IRenameParser renameParser,
		IUtilityService utilityService, ICacheService cacheService) {

		this.builder = builder;
		this.selectParser = selectParser;
		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.renameParser = renameParser;

	}

	override parse(Collection<SelectExpression> expressions, String input) {
		buildMapOperator(expressions)
	}

	override parse(SimpleSelect select, String operator) {
		return buildProjection(select, operator);
	}

	def private Object[] buildMapOperator(Collection<SelectExpression> expressions) {
		return buildMapOperator(expressions, null)
	}

	def private Object[] buildMapOperator(Collection<SelectExpression> expressions, String input) {
		var expressionArgument = ''
		var List<String> expressionStrings = newArrayList()
		var List<String> attributeNames = newArrayList()
		for (var i = 0; i < expressions.size; i++) {
			var expressionName = ''
			var expressionString = selectParser.parseExpression(expressions.get(i)).toString
//			var expressionType = MEP.instance.parse(expressionString).returnType.toString //parseSelectExpressionType(expressionComponents)
//			println("expressiontype:: " + expressionType)
			if (expressions.get(i).alias === null)
				expressionName = utilityService.getExpressionName()
			else
				expressionName = expressions.get(i).alias.name
			expressionStrings.add(expressionString)
			expressionStrings.add(expressionName)
			expressionStrings.add(',')
			var t = utilityService.generateKeyValueString(expressionStrings)
			expressionArgument += t
			cacheService.getExpressionCache().put(expressionName, t)
			if(i != expressions.size - 1) expressionArgument += ','
			expressionStrings.clear
			attributeNames.add(expressionName)
		}
//		Collections.sort(attributeNames)
		return #[attributeNames,
			builder.build(typeof(MapAO), newLinkedHashMap('expressions' -> expressionArgument, 'input' -> input))]
	}

	def private String buildProjection(SimpleSelect select, CharSequence operator) {
		var attributes = cacheService.getQueryCache().getProjectionAttributes(select)
		// Add new aliases from the rename operation		
		for (var i = 0; i < renameParser.getAliases().size - 2; i = i + 3) {
			var attributename = renameParser.getAliases().get(i)
			var sourcename = renameParser.getAliases().get(i + 1)
			var alias = renameParser.getAliases().get(i + 2)
			utilityService.getSource(sourcename).addAliasTo(attributename, alias);
		}

		var list = newArrayList
		for (var i = 0; i < attributes.size; i++) {
			var attribute1 = utilityService.getProjectAttribute(attributes.get(i))
			list.add(attribute1)
		}

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
