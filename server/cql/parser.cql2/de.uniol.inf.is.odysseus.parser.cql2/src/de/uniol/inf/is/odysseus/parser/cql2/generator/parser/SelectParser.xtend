package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Matrix
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Vector
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Collection
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.eclipse.xtext.EcoreUtil2
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute

class SelectParser implements ISelectParser {

	val Logger log = LoggerFactory.getLogger(SelectParser);

	var AbstractPQLOperatorBuilder builder;
	var ICacheService cacheService;
	var IUtilityService utilityService;
	var IAttributeNameParser nameParser;
	var IPredicateParser predicateParser;
	var IJoinParser joinParser;
	var IRenameParser renameParser;
	var IProjectionParser projectionParser;
	var IAggregationParser aggregateParser;
	var IExistenceParser existenceParser;
	var IAttributeParser attributeParser;
	
	var boolean prepare;

	@Inject
	new(AbstractPQLOperatorBuilder builder, ICacheService cacheService, IUtilityService utilityService,
		IAttributeNameParser nameParser, IPredicateParser predicateParser, IJoinParser joinParser,
		IProjectionParser projectionParser, IRenameParser renameParser, IAggregationParser aggregateParser, 
		IExistenceParser existenceParser, IAttributeParser attributeParser) {

		this.builder = builder;
		this.cacheService = cacheService;
		this.utilityService = utilityService;
		this.nameParser = nameParser;
		this.attributeParser = attributeParser;
		this.predicateParser = predicateParser;
		this.joinParser = joinParser;
		this.projectionParser = projectionParser;
		this.aggregateParser = aggregateParser;
		this.existenceParser = existenceParser;
		this.renameParser = renameParser;
		this.prepare = true;

	}

	override parse(SimpleSelect select) {
		
		//
		if (prepare) {
			prepare(select);
			prepare = false;
		}
		
		if (select.predicates !== null) {
			return parseWithPredicate(select);
		}

		//
		var String projectInput
		// Build additional operators for aggregatations, expressions, ...
		var String operator1 = parseAdditionalOperator(Operator.MAP, select)
		var String operator2 = parseAdditionalOperator(Operator.AGGREGATE, select)

		// Query corresponds to a select all	
		if (operator1 === null && operator2 === null && select.arguments.empty) {
			projectInput = joinParser.buildJoin(select.sources).toString

			// Return a join over all query sources
			if (select.sources.size > 1)
				return cacheService.getOperatorCache().registerOperator(projectInput)
			// Return a projection with one source
			else {

				return cacheService.getOperatorCache().registerOperator(projectionParser.parse(select, projectInput))
			}
		} // Arbitrary query with aggregations and/or expressions and/or simple attributes
		else {
			projectInput = buildInput2(select, operator1, operator2)
			return cacheService.getOperatorCache().registerOperator(projectionParser.parse(select, projectInput));
		}

	}

	def Collection<NestedSource> registerAllSource(SimpleSelect select) {
		var list = newArrayList
		for (Source source : select.sources) {
			var name = ''
			if (source instanceof SimpleSource) {

//				if(!SourceStruct.existQuerySource(name = (source as SimpleSource).getName())) {
				SourceStruct.addQuerySource(name = (source as SimpleSource).getName());
//				}
				if (source.alias !== null && !utilityService.getSource(source).hasAlias(source.alias)) {
					utilityService.registerSourceAlias(source as SimpleSource);
				}
			} else if (source instanceof NestedSource)
				list.add(source)
		}
		return list
	}

	def List<SelectExpression> extractAggregationsFromArgument(List<SelectArgument> args) {
		var List<SelectExpression> list = newArrayList
		for (SelectArgument a : args)
			if (a.expression !== null)
				if (a.expression.expressions.size == 1) {
					var aggregation = a.expression.expressions.get(0)
					var function = aggregation.value
					if (function instanceof Function) {
						if (utilityService.isAggregateFunctionName(function.name))
							list.add(a.expression)
					}
				}
		return list
	}

//
	def Collection<SelectExpression> extractSelectExpressionsFromArgument(List<SelectArgument> args) {
		var Collection<SelectExpression> list = newArrayList
		for (SelectArgument a : args)
			if (a.expression !== null) {
				if (a.expression.expressions.size == 1) {
					var aggregation = a.expression.expressions.get(0)
					var function = aggregation.value
					if (function instanceof Function) {
						if (utilityService.isMEPFunctionMame(function.name,
							parseExpression(a.expression as SelectExpression).toString))
							list.add(a.expression)
					} else
						list.add(a.expression)
				} else
					list.add(a.expression)
			}
		return list
	}

	override prepare(SimpleSelect select) {
		try {
			if (!cacheService.getSelectCache().getSelects().contains(select)) {
				var subQueries = registerAllSource(select)
				for (NestedSource subQuery : subQueries) {
					prepare(subQuery.statement.select)
					// key=simpleSelect, value=subQuery++ 
//					utilityServicetil.getSubQuerySources().put(subQuery.alias.name,utilityServicetil.getQueryCacheAttributes(subQuery.statement.select).keySet)
					cacheService.getQueryCache().putSubQuerySources(subQuery);
				}

				var Collection<QueryAttribute> attributes2 = attributeParser.getSelectedAttributes(select)
				
				var aggregations = extractAggregationsFromArgument(select.arguments)
				var expressions = extractSelectExpressionsFromArgument(select.arguments)
				if (aggregations !== null) {
					cacheService.getQueryCache().putQueryAggregations(select, aggregations);
				}
//					utilityServicetil.addQueryAggregations(select, aggregations)
				if (expressions !== null) {
					cacheService.getQueryCache().putQueryExpressions(select, expressions);
				}
//					utilityServicetil.getQueryExpressions().put(select, expressions)
				if (attributes2 !== null) {
					cacheService.getQueryCache().putQueryAttributes(select, attributes2);
				}

				cacheService.getSelectCache().add(select);
			}
		} catch (Exception e) {
			// TODO remove this after debugging
			log.error("error occurred while parsing select: " + e.message)
			throw e
		}
	}

	override parseExpression(SelectExpression e) {
		var str = ''
		for (var i = 0; i < e.expressions.size; i++) {
			var component = (e.expressions.get(i) as ExpressionComponent).value
			switch (component) {
				Function:
					str += component.name + '(' + parseExpression((component.value as SelectExpression)) + ')'
				Attribute:
					str += nameParser.parse(component.name)
				IntConstant:
					str += component.value + ''
				FloatConstant:
					str += component.value + ''
				BoolConstant:
					str += component.value + '' // TODO Is a bool value feasible?
				StringConstant:
					str += '\"' + component.value + '\"'
				Vector:
					str += component.value
				Matrix:
					str += component.value
			}
			if (i != e.expressions.size - 1)
				str += e.operators.get(i)
		}
		return str
	}

	override parseComplex(SimpleSelect left, SimpleSelect right, String operator) {
		parse(left)
		var rightSelectOperatorName = cacheService.getOperatorCache().lastOperatorId();
		cacheService.getSelectCache().flush()
		parse(right)
		var leftSelectOperatorName = cacheService.getOperatorCache().lastOperatorId();
		return cacheService.getOperatorCache().registerOperator(
			operator + '(' + rightSelectOperatorName + ',' + leftSelectOperatorName + ')')
	}

	override parseWithPredicate(SimpleSelect stmt) {

		// Check if query contains ComplexPredicates like EXISTS, ALL, ANY
		var List<Expression> predicates = newArrayList
		var List<Source> sources = newArrayList
		if (stmt.predicates !== null) {
			predicates.add(0, stmt.predicates.elements.get(0))
			var complexPredicates = EcoreUtil2.getAllContentsOfType(stmt.predicates, ComplexPredicate)
			if (complexPredicates !== null && !complexPredicates.empty && complexPredicates.size > 1)
				throw new IllegalArgumentException('queries with more then one complex predicates are not supported')
		}

		if (stmt.having !== null) // extract predicates from having clause
			predicates.add(0, stmt.having.elements.get(0))
		sources.addAll(stmt.sources)

		var String operator1 = parseAdditionalOperator(Operator.MAP, stmt)
		var String operator2 = parseAdditionalOperator(Operator.AGGREGATE, stmt)

		predicateParser.clear() // predicateStringList.clear()
		predicateParser.parse(predicates)
		var selectInput = buildInput2(stmt, operator1, operator2).toString
		var predicate = predicateParser.parsePredicateString(predicateParser.getPredicateStringList())
		var select = ''
		if (!predicate.equals(''))
			select = cacheService.getOperatorCache().registerOperator(
				builder.build(typeof(SelectAO),
					newLinkedHashMap('predicate' -> predicate.toString, 'input' -> selectInput)))
		else {
			var Map<String, String> newArgs = existenceParser.getOperator(0);
			newArgs.put('input', newArgs.get('input') + ',' + selectInput)
			cacheService.getOperatorCache().registerOperator(builder.build(typeof(ExistenceAO), newArgs))
			return cacheService.getOperatorCache().registerOperator(
				projectionParser.parse(stmt,
					'JOIN(' + cacheService.getOperatorCache().lastOperatorId() + ',' + selectInput + ')'))
		}

		// TODO are parameters correct?
		existenceParser.register(stmt, select)
		var attributes = newArrayList
		for (SelectArgument arg : stmt.arguments)
			if (arg.attribute !== null)
				attributes.add(arg.attribute)

		if (!checkIfSelectAll(attributes) || !cacheService.getQueryCache().getQueryAggregations(stmt).empty ||
			!cacheService.getQueryCache().getQueryExpressions(stmt).empty)
			return cacheService.getOperatorCache().registerOperator(projectionParser.parse(stmt, select))
			
		return select
	}

	def private String buildInput2(SimpleSelect select, String ... operators) {

		if (operators !== null) {
			var String mapOperator = if(operators.size > 0) operators.get(0) else null
			var String aggregateOperator = if(operators.size > 1) operators.get(1) else null

			if (mapOperator !== null && aggregateOperator !== null) {
				return checkForGroupAttributes(aggregateOperator, select,
					joinParser.buildJoin(#[aggregateOperator, joinParser.buildJoin(select.sources)]))
			} else if (mapOperator !== null) {
				return joinParser.buildJoin(select.sources)
			} else if (aggregateOperator !== null) {
				// Get all names of the attributes in the predicate clause
				var List<String> predicateAttributes = if (select.predicates !== null) {
						EcoreUtil2.getAllContentsOfType(select.predicates, Attribute).stream.map(e|e.name).collect(
							Collectors.toList)
					} else {
						null
					}
				// If the aggregate operator outputs the same attributes that are selected in the select clause
				if (cacheService.getAggregationAttributeCache().containsAll(
					cacheService.getQueryCache().getProjectionAttributes(select))) {
					// If the aggregate operator outputs the same attributes that
					if (predicateAttributes !== null && !predicateAttributes.empty &&
						cacheService.getAggregationAttributeCache().containsAll(predicateAttributes)) {
						return aggregateOperator
					} else {
						return checkForGroupAttributes(aggregateOperator, select,
							joinParser.buildJoin(#[aggregateOperator, joinParser.buildJoin(select.sources)]))
					}
				} else {
					return checkForGroupAttributes(aggregateOperator, select,
						joinParser.buildJoin(#[aggregateOperator, joinParser.buildJoin(select.sources)]))
				}
			}
		}
		return joinParser.buildJoin(select.sources)
	}

	def private String checkForGroupAttributes(String aggregateOperator, SimpleSelect select, String output) {
		if (cacheService.getOperatorCache().getOperator(aggregateOperator).contains('group_by')) {
			var String join = joinParser.buildJoin(select.sources)
			var groupAttributes = newArrayList
			// Compute group attributes
			for (var i = 0; i < select.order.size; i++) {
				var groupAttribute = select.order.get(i).name
				groupAttributes.add(groupAttribute)
				groupAttributes.add(groupAttribute + '_groupAttribute#' + i)
			}
			// Return a join operator that aggregate operator and a rename operator that renames the group attributes
			return joinParser.buildJoin(#[renameParser.parse(groupAttributes, aggregateOperator), join])

		}
		return output
	}

	def boolean checkIfSelectAll(List<Attribute> attributes) {
		if (attributes.empty)
			return true
		else
			for (Attribute attribute : attributes)
				if (!attribute.name.contains('.*'))
					return false
		return true
	}

	enum Operator {
		MAP,
		AGGREGATE
	}

	def private String parseAdditionalOperator(Operator operator, SimpleSelect select) {
		var Object[] result = null
		var String operatorName = null
		switch (operator) {
			case MAP: {
				var expressions = cacheService.getQueryCache().getQueryExpressions(select)
				if (expressions !== null && !expressions.empty) {
					result = projectionParser.parse(expressions, null)
					operatorName = result.get(1).toString
				}
			}
			case AGGREGATE: {
				var aggregations = cacheService.getQueryCache().getQueryAggregations(select)
				if (aggregations !== null && !aggregations.empty) {
					//TODO add aggregate parser
					result = aggregateParser.parse(aggregations, select.order, select.sources);
					operatorName = cacheService.getOperatorCache().registerOperator(result.get(1).toString)
				}
			}
		}
		return operatorName
	}
	
	override clear() {
		prepare = true;
	}

}
