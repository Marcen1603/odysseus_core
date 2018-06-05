package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AndPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Bracket
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicateRef
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Equality
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IPredicateParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IQuantificationParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.Optional

class PredicateParser implements IPredicateParser {

//	private val Logger log = LoggerFactory.getLogger(PredicateParser);

	private var IUtilityService utilityService
	private var ICacheService cacheService
	private var IAttributeNameParser attributeParser
	private var ISelectParser selectParser
	private var IExistenceParser existenceParser
	private var IQuantificationParser quantificationParser
	
	private var String predicateString
	private var List<String> predicateStringList
	private var CharSequence lastPredicateElement

	@Inject
	new(IUtilityService utilityService, ICacheService cacheService, IAttributeNameParser attributeParser, ISelectParser selectParser, IExistenceParser existenceParser, IQuantificationParser quantificationParser) {
		
		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.attributeParser = attributeParser;
		this.selectParser = selectParser;
		this.existenceParser = existenceParser;
		this.quantificationParser = quantificationParser;
		
		this.predicateString = ''
		this.predicateStringList = newArrayList
		this.lastPredicateElement = ''
		
	}

	def CharSequence parse(Expression e, SimpleSelect select) throws PQLOperatorBuilderException {
		if (!e.eContents.empty) {
			switch e {
				OrPredicate: {
					parse(e.left, select)
					buildPredicateString('||')
					parse(e.right, select)

				}
				AndPredicate: {
					parse(e.left, select)
					buildPredicateString('&&')
					parse(e.right, select)
				}
				Equality: {
					parse(e.left, select)
					if (e.op.equals("="))
						buildPredicateString('==')
					else
						buildPredicateString(e.op)
					parse(e.right, select)
				}
				Comparision: {
					parse(e.left, select)
					buildPredicateString(e.op)
					parse(e.right, select)
				}
				Plus: {
					parse(e.left, select)
					buildPredicateString('+')
					parse(e.right, select)
				}
				Minus: {
					parse(e.left, select)
					buildPredicateString('-')
					parse(e.right, select)
				}
				MulOrDiv: {
					parse(e.left, select)
					buildPredicateString(e.op)
					parse(e.right, select)
				}
				NOT: {
					buildPredicateString('!')
					parse(e.expression, select)
				}
				Bracket: {
					buildPredicateString('(')
					parse(e.inner, select)
					buildPredicateString(')')
				}
				AttributeRef: {
//					buildPredicateString(attributeParser.parse(e.value as Attribute))
					val Optional<QueryAttribute> queryAttribute = utilityService.getQueryAttribute(e.value as Attribute);
					if (queryAttribute.isPresent()) {
						if (queryAttribute.get().getAlias() != null) {
							buildPredicateString(queryAttribute.get().getAlias())
						} else {
							buildPredicateString(queryAttribute.get().getName())
						}
					} else {
						throw new IllegalArgumentException("could not find attribute " + e.value.name);
					}
				}
				ComplexPredicateRef: {
					
					if (e.value.quantification != null) {
						quantificationParser.parse(e.value, select);
						return "";
					}
					
					if (e.value.exists != null) {
						
						predicateStringList = existenceParser.parse(e.value, select, predicateStringList, false);
						predicateString = "";
						predicateStringList.stream().forEach(k | {
							buildPredicateString(k) 
						})
						
						return predicateString;
					}
					
//						var complexPredicate = e.value as ComplexPredicate
//						var QuantificationPredicate quantification = null
//						var ExistPredicate exists = null
//						var InPredicate in = null
//						
//						if ((quantification = complexPredicate.quantification) !== null) {
//							var type = 'EXISTS'
//							var operator = quantification.operator
//							if (quantification.predicate.equalsIgnoreCase('ALL')) {
//								type = 'NOT_EXISTS'
//								if (operator.equals('>='))
//									operator = '<'
//								else if (operator.equals('>'))
//									operator = '<='
//								else if (operator.equals('<='))
//									operator = '>'
//								else if (operator.equals('<'))
//									operator = '>='
//							}
//							// save the current predicate
//							var predicateStringListBackup = new ArrayList(predicateStringList)
//							predicateStringList = newArrayList
//							var predicateBackup = predicateString
//							predicateString = ''
////						var attributeAliasesBackup = registry_AttributeAliases
////						registry_AttributeAliases = newHashMap
//							var select = complexPredicate.select.select
//							selectParser.prepare(select, null)
//							var predicate = ''
//							if (select.predicates === null) {
//								selectParser.parse(select)
//								for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//									predicate += quantification.attribute.name + operator + attribute.name + '&&'
//								}
//								predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//							} else {
//								selectParser.parseWithPredicate(select)
//								for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//									predicate += quantification.attribute.name + operator + attribute.name + '&&'
//								}
//								predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//							}
//
//							var Map<String, String> args = newHashMap
//							args.put('type', type)
//							args.put('input', cacheService.getOperatorCache().lastOperatorId())
////FIXME 31.12.17
////							for (Entry<String, Collection<String>> l : cacheService.getQueryCache().getQueryAttributes(select).entrySet)
////								for (String s : l.value) {
////									var attributename = s
////									if (!attributename.contains('.')) {
////										attributename = l.key + '.' + attributename
////									}
//////								println(attributename)
////									predicate = predicate.replace(attributename, attributename.replace('.', '_'))
////								}
//
//							args.put('predicate', predicate)
//							existenceParser.getOperators().add(args)
//
//							// restore predicate
//							predicateString = predicateBackup
//							predicateStringList = new ArrayList(predicateStringListBackup)
////						registry_AttributeAliases = attributeAliasesBackup
//
//						} else if ((exists = complexPredicate.exists) !== null) {
//							var type = 'EXISTS'
//							if (lastPredicateElement.equals('!')) {
//								type = 'NOT_EXISTS'
//								predicateStringList.remove(predicateStringList.size() - 1)
//								if (predicateStringList.size() - 1 > 0) {
//									var index = predicateStringList.size() - 2
//									var element = predicateStringList.get(index)
//									if (element.equals('&&') || element.equals('||'))
//										predicateStringList.remove(index)
//								}
//							} else if (lastPredicateElement.equals('&&') || lastPredicateElement.equals('||')) {
//								if (predicateStringList.size > 0)
//									predicateStringList.remove(predicateStringList.size() - 1)
//							}
//							parseComplexPredicate(complexPredicate, type)
//							
//						} else if ((in = complexPredicate.in) !== null) {
//							var type = 'EXISTS'
//							var operator = '=='
//							// save the current predicate
//							var predicateStringListBackup = new ArrayList(predicateStringList)
//							predicateStringList = newArrayList
//							var predicateBackup = predicateString
//							predicateString = ''
//
//							var select = complexPredicate.select.select
//							selectParser.prepare(select, null)
//							var predicate = ''
//							if (select.predicates === null) {
//								selectParser.parse(select)
//								for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//									predicate += in.attribute.name + operator + attribute.name + '&&'	
//								}
//								predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//							} else {
//								selectParser.parseWithPredicate(select)
//								for (QueryAttribute attribute : cacheService.getQueryCache().getProjectionAttributes(select)) {
//									predicate += in.attribute.name + operator + attribute.name + '&&'
//								}
//								predicate = predicate.substring(0, predicate.lastIndexOf('&') - 1)
//							}
////FIXME 31.12.17
////							for (Entry<String, Collection<String>> l : cacheService.getQueryCache().getQueryAttributes(select).entrySet)
////								for (String s : l.value) {
////									var attributename = s
////									if (!attributename.contains('.')) {
////										attributename = l.key + '.' + attributename
////									}
//////								println(attributename)
////									predicate = predicate.replace(attributename, attributename.replace('.', '_'))
////								}
//
////						predicate = predicate.replace('\\.', '_')
//							var Map<String, String> args = newHashMap
//							args.put('type', type)
//							args.put('input', cacheService.getOperatorCache().lastOperatorId())
//							args.put('predicate', predicate)
//							existenceParser.getOperators().add(args)
//
//							// restore predicate
//							predicateString = predicateBackup
//							predicateStringList = new ArrayList(predicateStringListBackup)
//						}
				}
				
			}
		} else {
			var str = ''
			switch e {
				IntConstant: str = e.value + ''
				FloatConstant: str = e.value + ''
				StringConstant: str = '"' + e.value + '"'
				BoolConstant: str = e.value + ''
			}
			buildPredicateString(str)
		}

		return predicateString
	}

	override def CharSequence parse(List<Expression> expressions, SimpleSelect select) {
		predicateString = ''
		for (var i = 0; i < expressions.size - 1; i++) {
			if (expressions.get(i) instanceof AttributeRef) {
			} else {
				predicateString = parse(expressions.get(i), select).toString
				buildPredicateString('&&')
			}
		}
		predicateString = parse(expressions.get(expressions.size - 1), select).toString
		return predicateString
	}

	override CharSequence parsePredicateString(List<String> predicateString) {
		if (predicateString.size > 0) {
			if (predicateString.get(0).equals('&&') || predicateString.get(0).equals('||'))
				predicateString.remove(0)
			if (predicateString.get(predicateString.size - 1).equals('&&') ||
				predicateString.get(predicateString.size - 1).equals('||'))
				predicateString.remove(predicateString.size - 1)
		}
		var predicate = ''
		for (String pred : predicateString)
			predicate += pred
		return predicate
	}

	def public parseComplexPredicate(SimpleSelect select, ComplexPredicate complexPredicate, String type) {
		// save the current predicate
		var predicateStringListBackup = new ArrayList(predicateStringList)
		predicateStringList = newArrayList
		var predicateBackup = predicateString
		predicateString = ''

		var Map<String, String> args = newHashMap
		args.put('type', type)

		var subQuery = complexPredicate.select.select as SimpleSelect
		selectParser.prepare(subQuery, null)
		selectParser.parse(subQuery)
		parse(complexPredicate.select.select.predicates.elements.get(0), select)

//		println("exists() -> " + predicateString)
//FIXME not working anymore 31.12.17
//		for (QueryAttribute l : cacheService.getQueryCache().getQueryAttributes(subQuery))
//			for (String s : l.value) {
//				var attributename = s
//				if (!attributename.contains('.')) {
//					attributename = l.key + '.' + attributename
//				}
////				println(attributename)
//				predicateString = predicateString.replace(attributename, attributename.replace('.', '_'))
//			}

//		println("exists() -> " + predicateString)
		args.put('predicate', predicateString)
		args.put('input', cacheService.getOperatorCache().last())
//		existenceParser.getOperators().add(args)

		// restore predicate
		predicateString = predicateBackup
		predicateStringList = new ArrayList(predicateStringListBackup)
	}

	def CharSequence buildPredicateString(CharSequence sequence) {
		lastPredicateElement = sequence
		predicateStringList.add(sequence.toString())
		return predicateString += sequence
	}
	
	override clear() {
		predicateString = ""
		lastPredicateElement = ""
		predicateStringList = newArrayList;
	}
	
	override getPredicateStringList() {
		return predicateStringList
	}
	
}
