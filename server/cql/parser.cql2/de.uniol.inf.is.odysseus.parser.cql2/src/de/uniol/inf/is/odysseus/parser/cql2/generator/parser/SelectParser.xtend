package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.SelectCache
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Collection
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.eclipse.xtext.EcoreUtil2
import java.util.Optional

class SelectParser implements ISelectParser {

//	val Logger log = LoggerFactory.getLogger(SelectParser);

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
	var IExpressionParser expressionParser;
	var boolean prepare;

	@Inject
	new(AbstractPQLOperatorBuilder builder, ICacheService cacheService, IUtilityService utilityService,
		IAttributeNameParser nameParser, IPredicateParser predicateParser, IJoinParser joinParser,
		IProjectionParser projectionParser, IRenameParser renameParser, IAggregationParser aggregateParser,
		IExistenceParser existenceParser, IAttributeParser attributeParser, IExpressionParser expressionParser) {

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
		this.expressionParser = expressionParser;
		this.prepare = true;

	}

	override parse(SimpleSelect select) {
		
		//
		if (prepare) {
			prepare(select, null);
			prepare = false;
		}
		
		attributeParser.clear()// is this still necessary?
		
		// process select statements by the given order of the prepare method
		cacheService.getSelectCache().getSelects().stream().forEach(e | {
			
			// determine if current select is the root select or an inner select
			var boolean root = true;
			if (!select.equals(e)) {
				root = false;
			}
			
			if (e.predicates !== null) {
				parseWithPredicate(e);
				
				if (!root) {
					val Optional<SubQuery> o = cacheService.getQueryCache().getSubQuery(e);
					if (o.isPresent()) {
						o.get().operator = cacheService.getOperatorCache().lastOperatorId();
					}
				}
				
				return;
			}
	
			//
			var String projectInput
			// Build additional operators for aggregatations, expressions, ...
			var String operator1 = parseAdditionalOperator(Operator.MAP, e)
			var String operator2 = parseAdditionalOperator(Operator.AGGREGATE, e)
	
			// Query corresponds to a select all	
			if (operator1 === null && operator2 === null && e.arguments.empty) {
				projectInput = joinParser.buildJoin(e.sources).toString
	
				// Return a join over all query sources
				if (e.sources.size > 1) {
					cacheService.getOperatorCache().registerOperator(projectInput)
					
					if (!root) {
						val Optional<SubQuery> o = cacheService.getQueryCache().getSubQuery(e);
						if (o.isPresent()) {
							o.get().operator = cacheService.getOperatorCache().lastOperatorId();
						}
					}
					
					return;
				}
				// Return a projection with one source
				else {
					cacheService.getOperatorCache().registerOperator(projectionParser.parse(e, projectInput))
					
					if (!root) {
						val Optional<SubQuery> o = cacheService.getQueryCache().getSubQuery(e);
						if (o.isPresent()) {
							o.get().operator = cacheService.getOperatorCache().lastOperatorId();
						}
					}
					
					return;
				}
			} // Arbitrary query with aggregations and/or expressions and/or simple attributes
			else {
				projectInput = buildInput2(e, operator1, operator2)
				cacheService.getOperatorCache().registerOperator(projectionParser.parse(e, projectInput));
				
				if (!root) {
					val Optional<SubQuery> o = cacheService.getQueryCache().getSubQuery(e);
					if (o.isPresent()) {
						o.get().operator = cacheService.getOperatorCache().lastOperatorId();
					}
				}
				
				return;
			}
		})
		

	}

	def Collection<NestedSource> registerAllSource(SimpleSelect select) {

		val col = newArrayList
		
		select.sources.stream().forEach(e | {
			if (e instanceof SimpleSource) {

				val name = (e as SimpleSource).getName()
				SystemSource.addQuerySource(name);

				if (e.alias !== null && !utilityService.getSystemSource(e).hasAlias(e.alias)) {
					utilityService.registerSourceAlias(e as SimpleSource);
				}
				
			} else if (e instanceof NestedSource) {
				col.add(e);
				cacheService.getQueryCache().addSubQuerySource(new SubQuery(e));
			}
		})
		
		return col
	}

	/** 
	 * Collects and saves attribute, aggregation and expression information about a select statement before it will be parsed.
	 * This method is called recursively such that the most nested select statement will be processed first. Therefore, the 
	 * processing order of the selects statements is determined by this method and is retained by {@link SelectCache}.
	 */
	override prepare(SimpleSelect select, NestedSource innerSelect) {

		if (!cacheService.getSelectCache().getSelects().contains(select)) {

			// register all sources and call prepare recursively
			registerAllSource(select).stream().forEach(e | {
				prepare(e.statement.select, e);
			});				
			
			// register attributes that are contained by the predicate of the select statement	
			attributeParser.registerAttributesFromPredicate(select);
			
			// retrieve attributes that are selected by the statement
			cacheService.getQueryCache().putQueryAttributes(select, attributeParser.getSelectedAttributes(select));
			// save some information that were retrieved by the attributeParser
			cacheService.getQueryCache().putProjectionSources(select, attributeParser.getSourceOrder());
			cacheService.getQueryCache().putProjectionAttributes(select, attributeParser.getAttributeOrder());
			cacheService.getQueryCache().putQueryAggregations(select, attributeParser.getAggregates());
			cacheService.getQueryCache().putQueryExpressions(select, attributeParser.getExpressions());
			
			// save select object
			cacheService.getSelectCache().add(select);
		}
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
				if (utilityService.containsAllAggregates(select)) {
					// If the aggregate operator outputs the same attributes that
					if (predicateAttributes !== null 
						&& !predicateAttributes.empty 
						&& utilityService.containsAllPredicates(predicateAttributes)) {
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
