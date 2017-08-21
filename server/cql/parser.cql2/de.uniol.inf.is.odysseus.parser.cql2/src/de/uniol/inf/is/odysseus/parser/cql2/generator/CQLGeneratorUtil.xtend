package de.uniol.inf.is.odysseus.parser.cql2.generator

import de.uniol.inf.is.odysseus.core.mep.IMepFunction
import de.uniol.inf.is.odysseus.mep.FunctionStore
import de.uniol.inf.is.odysseus.mep.MEP
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.Map.Entry
import java.util.Set
import java.util.regex.Pattern
import java.util.stream.Collectors
import org.eclipse.xtext.EcoreUtil2

class CQLGeneratorUtil {

	public static var CQLGeneratorUtil instance = null

	public static def CQLGeneratorUtil getInstance(CQLGenerator generator) {
		return if(instance === null) (instance = new CQLGeneratorUtil(generator)) else instance
	}

	private var CQLGenerator generator
	
	private static var List<SourceStruct> registry_Sources = newArrayList
	private static var Map<String, String> registry_Expressions = newHashMap
	private static var List<String> registry_AggregationAttributes = newArrayList
	private static var Map<String, Set<String>> registry_SubQuerySources = newHashMap
	private static var Map<String, String> registry_AttributeAliases = newHashMap
	/** Contains all selected attributes for each registered query */
	private static var Map<SimpleSelect, List<String>> projectionAttributes = newHashMap
	/** Contains the corresponding sources to the attributes in projectionAttributes */
	private static var Map<SimpleSelect, List<String>> projectionSources = newHashMap
	private static var Map<SimpleSelect, List<SelectExpression>> queryExpressions = newHashMap

	private static var aggregationCounter = 0
	private static var expressionCounter = 0

	private new(CQLGenerator generator) {
		this.generator = generator
	}

	public static def Map<String, List<String>> addToMap(Map<String, List<String>> map, String attribute,
		String realSourcename) {
		var attributeList = map.get(realSourcename)
		if (attributeList === null)
			attributeList = newArrayList
		if (!attributeList.contains(attribute))
			attributeList.add(attribute)
		map.put(realSourcename, attributeList)
		return map
	}

	public static def List<String> getAttributeAliasesAsList() {
		var list = newArrayList
		for (List<String> l : getAttributeAliasesAsMap().values)
			for (String alias : l)
				list.add(alias)
		return list
	}

	public static def boolean isAttributeAlias(String attributename) {
		return getAttributeAliasesAsList().contains(attributename)
	}

	public static def isSourceAlias(String sourcename) { return getSourceAliasesAsList().contains(sourcename) }

	public static def List<AttributeStruct> getAttributes() {
		var list = newArrayList
		for (SourceStruct source : registry_Sources)
			list.addAll(source.attributes)
		return list
	}

	private static def String registerAttributeAliases(Attribute attribute, String attributename, String realSourcename,
		String sourcenamealias, boolean isSubQuery) {
//		println(
//			'registerAttributeAliases() -> attribute= ' + attributename + ', realSourcename= ' + realSourcename +
//				', sourcenameAlias= ' + sourcenamealias + ', isFormSubQuery= ' + isSubQuery)
		var simpleAttributename = if(attribute.alias !== null) attribute.name else attributename
		if(simpleAttributename.contains('.')) simpleAttributename = simpleAttributename.split("\\.").get(1)
		var alias = sourcenamealias
		for (AttributeStruct attr1 : getSource(realSourcename).attributes) {
			if (attr1.attributename.equals(simpleAttributename)) {
				if(alias === null) alias = realSourcename
				if (attribute.alias !== null) {
					if (registry_AttributeAliases.entrySet.contains(attribute.alias.name))
						throw new IllegalArgumentException("given alias " + attribute.alias.name + " is ambiguous")
					if (!attr1.aliases.contains(attribute.alias.name)) {
						attr1.aliases.add(attribute.alias.name)
						registry_AttributeAliases.put(attribute.alias.name, alias)
					}
					return attribute.alias.name
				} else if (attribute.alias === null && getSourceAliasesAsList().contains(alias)) {
					if (!attr1.aliases.contains(attributename)) {
						attr1.aliases.add(attributename)
						registry_AttributeAliases.put(attributename, alias)
					}
					return attributename
				}
			}
		}
		return null
	}

	/** Returns all attributes with its corresponding sources from a select statement. */
	public static def Map<String, List<String>> getSelectedAttributes(SimpleSelect select,
		Map<String, List<String>> var2) {
		var map = var2
		var attributes = newArrayList
		var String[] attributeOrder = newArrayOfSize(select.arguments.size)
		var String[] sourceOrder = newArrayOfSize(select.arguments.size)
		// Get all attributes from select arguments		
		for (SelectArgument argument : select.arguments) {
			if (argument.attribute !== null) {
				attributes.add(argument.attribute)
			}
		}
		// Check if it's a select * query and add for each source its attributes.
		// Return the computed attribute set
		if (attributes.empty && EcoreUtil2.getAllContentsOfType(select, SelectExpression).empty) {
			var List<String> attributeOrderList = newArrayList
			var List<String> sourceOrderList = newArrayList
			for (Source source : select.sources)
				if (source instanceof SimpleSource)
					for (String attribute : getAttributeNamesFrom(source.name)) {
						if (source.alias !== null) {
							var attributealias = source.alias.name + '.' + attribute
							getSource(source.name).findbyName(attribute).aliases.add(attributealias)
							registry_AttributeAliases.put(attributealias, source.alias.name)
//							registerAttributeAliases(attribute, attributename, sourcename, sourcealias, isFromSubQuery)    
//							registerAttributeAliases(..) //TODO insert method here
							attributeOrderList.add(attributealias)
							map = addToMap(map, attributealias, source.name)
						} else
							attributeOrderList.add(source.name + '.' + attribute)
						map = addToMap(map, attribute, source.name)
						sourceOrderList.add(source.name)
					}
			attributeOrder = attributeOrderList
			sourceOrder = sourceOrderList
			projectionAttributes.put(select, attributeOrder)
			projectionSources.put(select, sourceOrder)
			return map
		}
		// Get all attributes from predicates
		if (select.predicates !== null) {
			var list = EcoreUtil2.getAllContentsOfType(select.predicates, Attribute)
			for (Attribute attribute : list)
				if (attribute.name.contains('.')) {
					var split = attribute.name.split('\\.')
					var sourcename = split.get(0)
					var attributename = split.get(1)
					if (sourcename.isSourceAlias && !attributename.isAttributeAlias)
						registerAttributeAliases(attribute, attribute.name, getSourcenameFromAlias(sourcename),
							sourcename, false)
//					else
//						registerAttributeAliases(attribute, attributename, sourcename, null, false)
				}
//				if(!contains(attributes, attribute))
//				{
//							
//					attributes.add(attribute)
//				}
		}

		var i = 0
		// Iterate over all found attributes
		for (Attribute attribute : attributes) {
			// Compute source candidates for the current attribute
			var sourceCandidates = getSourceCandidates(attribute, select.sources)
			// Parse the current attribute and get its informations			
			var result = parseAttribute(attribute)
			var attributename = result.get(0) as String
			var sourcename = result.get(1) as String
			var sourcealias = result.get(2) as String
			var String attributealias
			var list = result.get(3) as List<String>
			var isFromSubQuery = result.get(4) as Boolean
			if (attribute.alias !== null)
				attributename = attribute.alias.name

			if (sourceCandidates.size > 0) {
				if (sourceCandidates.size > 1 && sourcename === null)
					throw new IllegalArgumentException(
						"attribute " + attributename + " is ambiguous: possible sources are " +
							sourceCandidates.toString)

						if (sourceCandidates.size == 1) {
							sourcename = sourceCandidates.get(0).sourcename
							if (list !== null)
								for (String name : list)
									map = addToMap(map, name, sourcename)
						}
						map = addToMap(map, attributename, sourcename)
						if (isFromSubQuery)
							registerSourceAlias(sourcename, sourcealias)
						attributealias = registerAttributeAliases(attribute, attributename, sourcename, sourcealias,
							isFromSubQuery)
					} else {
						if (list !== null)
							for (String name : list) {
								map = addToMap(map, name, sourcename)
								registerAttributeAliases(attribute, sourcealias + '.' + name, sourcename, sourcealias,
									isFromSubQuery)
							}
					}
					// Place the current attribute in the right order
					attributeOrder = computeProjectionAttributes(attributeOrder, select, attribute, attributename,
						attributealias, sourcename)
					sourceOrder.set(i, sourcename)
					i++
				}
				attributeOrder = computeProjectionAttributes(attributeOrder, select, null, null, null, null)
				projectionAttributes.put(select, attributeOrder)
				projectionSources.put(select, sourceOrder)

//				println("getSelectedAttributes() -> map= " + map.toString + ', order= ' + attributeOrder.toString)
				return map
			}

			public static def String[] computeProjectionAttributes(String[] list, SimpleSelect select,
				Attribute attribute, String attributename, String attributealias, String sourcename) {
				expressionCounter = 0
				aggregationCounter = 0
				var i = 0
				var attributeOrder = list
				var Object candidate
				if (attribute !== null) {
					for (SelectArgument argument : select.arguments) {
						if ((candidate = argument.attribute) !== null)
							if ((candidate as Attribute).name.equals(attribute.name))
								if ((candidate as Attribute).alias !== null)
									attributeOrder.set(i, (candidate as Attribute).alias.name)
								else if (attributealias !== null)
									attributeOrder.set(i, attributealias)
								else {
									if (attributename.contains('.')) {
										var split = attributename.split('\\.')
										var name = split.get(1)
										var source = split.get(0)
										var salias = source
										if (isSourceAlias(source))
											source = getSourcenameFromAlias(salias)
										if (name.equals('*')) {
											// TODO Query with stream1.*, stream.* would be overriden!
											var attributeOrderList = new ArrayList(attributeOrder.size)
											for (String str : getAttributeNamesFrom(source)) {
												attributeOrderList.add(salias + '.' + str)
												i++
											}
											attributeOrder = attributeOrderList
										} else
											attributeOrder.set(i, attributename)
									} else
										attributeOrder.set(i, sourcename + '.' + attributename)
								}

						if ((candidate = argument.expression) !== null) {
							if ((candidate as SelectExpression).alias !== null)
								attributeOrder.set(i, (candidate as SelectExpression).alias.name)
							else {
								if ((candidate as SelectExpression).expressions.size == 1) {
									var function = (candidate as SelectExpression).expressions.get(0).value
									if (function instanceof Function)
										if (isAggregateFunction(function.name))
											attributeOrder.set(i, getAggregationName(function.name))
										else
											attributeOrder.set(i, getExpressionName())
								} else
									attributeOrder.set(i, getExpressionName())
							}
						}
						i++
					}
				} else {
					for (SelectArgument argument : select.arguments) {
						if ((candidate = argument.expression) !== null) {
							if ((candidate as SelectExpression).alias !== null)
								attributeOrder.set(i, (candidate as SelectExpression).alias.name)
							else {
								if ((candidate as SelectExpression).expressions.size == 1) {
									var function = (candidate as SelectExpression).expressions.get(0).value
									if (function instanceof Function)
										if (isAggregateFunction(function.name))
											attributeOrder.set(i, getAggregationName(function.name))
										else
											attributeOrder.set(i, getExpressionName())
								} else
									attributeOrder.set(i, getExpressionName())
							}
						}
						i++
					}
				}
				expressionCounter = 0
				aggregationCounter = 0
				return attributeOrder
			}

			private static def List<SourceStruct> getSourceCandidates(Attribute attribute, List<Source> sources) {
				var containedBySources = newArrayList
				for (Source source1 : sources) {
					if (source1 instanceof SimpleSource) {
						for (SourceStruct source2 : registry_Sources) {
							if (source1.name.equals(source2.sourcename) && source2.containsAttribute(attribute.name) &&
								sources.stream.map(e|if(e instanceof SimpleSource) e.name).collect(Collectors.toList).
									contains(source2.sourcename)) {
								if (!containedBySources.contains(source2))
									containedBySources.add(source2)
								else if (!attribute.name.contains('.'))
									if (!isAttributeAlias(attribute.name))
										throw new IllegalArgumentException(attribute.name) // TODO Add exception message
							}
						}
					} else {
						var subQueryAlias = (source1 as NestedSource).alias.name
						for (String source : registry_SubQuerySources.get(subQueryAlias)) {
							for (SourceStruct source2 : registry_Sources) {
								var realName = attribute.name
								if (realName.contains('.'))
									realName = realName.split('\\.').get(1)
								if (source.equals(source2.sourcename) && source2.containsAttribute(realName) // /!!!!
//							&& sources.stream.map(e|if(e instanceof SimpleSource) e.name).collect(Collectors.toList).contains(source2.sourcename)
								) {
									if (!containedBySources.contains(source2))
										containedBySources.add(source2)
									else { // TODO comment
										if (!attribute.name.contains('.')) {
											if (!isAttributeAlias(attribute.name))
												throw new IllegalArgumentException(attribute.name)
										}
									}
								}
							}
						}
					}
				}
				return containedBySources
			}

			/** Returns all {@link Attribute} elements from the corresponding source. */
			public static def List<String> getAttributeNamesFrom(String srcname) {
				for (SourceStruct source : registry_Sources)
					if (source.sourcename.equals(srcname) || source.aliases.contains(srcname))
						return source.attributes.stream.map(e|e.attributename).collect(Collectors.toList);
			}

			private static def Object[] parseAttribute(Attribute attribute) {
				var String sourcename
				var String sourcealias
				var List<String> list
				var boolean subQuery
				if (attribute.name.contains('.')) {
					var split = attribute.name.split("\\.")
					sourcename = split.get(0)
					if (!getSourceNames().contains(sourcename)) {
						sourcealias = sourcename
						if ((sourcename = getSourcenameFromAlias(sourcename)) === null &&
							registry_SubQuerySources.keySet.contains(split.get(0)))
							subQuery = true
					}
					if (split.get(1).contains('*')) {
						list = newArrayList
						sourcename = split.get(0)
						if (sourcename.isSourceAlias) {
							sourcealias = sourcename
							sourcename = getSourcenameFromAlias(sourcename)
						}
						for (String str : getAttributeNamesFrom(sourcename))
							list.add(str)
					}
				}
				return #[attribute.name, sourcename, sourcealias, list, subQuery]
			}

			public static def String getProjectAttribute(String attribute) {
				if (attribute.contains(CQLGeneratorUtil.getExpressionPrefix()))
					return CQLGeneratorUtil.getRegisteredExpressions().get(attribute)
				if (CQLGeneratorUtil.getRegisteredExpressions().keySet.contains(attribute))
					return CQLGeneratorUtil.getRegisteredExpressions().get(attribute)
				if (attribute.contains('.')) {
					if (CQLGeneratorUtil.isAttributeAlias(attribute))
						return attribute

					var split = attribute.split('\\.')
					var realAttributename = split.get(1)
					var sourcename = split.get(0)
					var sourcealias = sourcename
					if (CQLGeneratorUtil.isSourceAlias(sourcename))
						sourcename = getSourcenameFromAlias(sourcealias)
					var aliases = CQLGeneratorUtil.getSource(sourcename).findbyName(realAttributename).aliases
					if (!aliases.empty)
						return aliases.get(aliases.size - 1)

					return attribute
				}
				return attribute
			}

			public static def List<String> getSourceNames() {
				return registry_Sources.stream.map(e|e.sourcename).collect(Collectors.toList)
			}

			public static def String getSourcenameFromAlias(String sourcealias) {
				for (Entry<SourceStruct, List<String>> source : getSourceAliases().entrySet)
					if (source.value.contains(sourcealias))
						return source.key.sourcename
				return null
			}

			def public AttributeStruct getAttributeFromAlias(String alias) {
				for (Entry<AttributeStruct, List<String>> entry : getAttributeAliasesAsMap().entrySet)
					if (entry.value.contains(alias))
						return entry.key
				return null
			}

			def public String getAttributenameFromAlias(String alias) {
				var attribute = getAttributeFromAlias(alias)
				if (attribute !== null)
					return attribute.attributename
				if (this.registry_AggregationAttributes.contains(alias) ||
					this.registry_Expressions.keySet.contains(alias))
					return alias
			}

			public static def List<String> getSourceAliasesAsList() {
				var list = newArrayList
				for (List<String> l : getSourceAliases().values)
					list.addAll(l)
				return list
			}

			public static def Map<SourceStruct, List<String>> getSourceAliases() {
				var map = newHashMap
				for (SourceStruct source : registry_Sources)
					map.put(source, source.aliases)
				return map
			}

/////rename method1
			public static def Map<AttributeStruct, List<String>> getAttributeAliasesAsMap() {
				var map = newHashMap
				for (SourceStruct source : registry_Sources)
					for (AttributeStruct attribute : source.attributes)
						if (!attribute.aliases.empty)
							map.put(attribute, attribute.aliases)
				return map
			}

			public static def SourceStruct getSource(String name) {
				for (SourceStruct source : registry_Sources)
					if (source.sourcename.equals(name))
						return source
					else if (source.aliases.contains(name))
						return source
				throw new IllegalArgumentException('given source ' + name + ' is not registered')
			}

			public static def SourceStruct getSource(Source source) {
				if (source instanceof SimpleSource)
					return getSource(source.name)
			}

			public static def registerSourceAlias(String sourcename, String sourcealias) {
				var source = getSource(sourcename)
				if (!source.aliases.contains(sourcealias))
					source.aliases.add(sourcealias)
			}

			private static val EXPRESSSION_NAME_PREFIX = 'expression_'

			public static def String getExpressionName() { return EXPRESSSION_NAME_PREFIX + (expressionCounter++) }

			public static def String getAggregationName(String name) { return name + '_' + (aggregationCounter++) }

			public static def boolean isAggregateFunction(String name) {
				return aggregatePattern.matcher(name).toString.contains(name);
			}

			private static var FunctionStore functionStore;
			private static var Pattern aggregatePattern;
			private static var MEP mep;

			public static def setFunctionStore(FunctionStore store) {
				functionStore = store;
			}

			public static def setAggregatePattern(Pattern pattern) {
				aggregatePattern = pattern;
			}

			public static def setMEP(MEP mepp) {
				mep = mepp;
			}

			public static def getExpressionPrefix() {
				return EXPRESSSION_NAME_PREFIX
			}

			public static def getRegisteredSources() {
				return registry_Sources;
			}

			public static def setRegisteredSources(List<SourceStruct> schemata) {
				registry_Sources = schemata
			}

			public static def getRegisteredExpressions() {
				return registry_Expressions
			}

			public static def getRegisteredAggregationAttributes() {
				return registry_AggregationAttributes
			}

			public static def getProjectionAttributes() {
				return projectionAttributes
			}

			public static def getQueryExpressions() {
				return queryExpressions
			}

			public static def getGetProjectSources() {
				return projectionSources
			}

			public static def getSubQuerySources() {
				return registry_SubQuerySources
			}

			public static def Map<String, String> getAttributeAliases() {
				return registry_AttributeAliases
			}

			public static def clear() {
				registry_AggregationAttributes.clear()
				registry_AttributeAliases.clear()
				registry_Expressions.clear()
				registry_Sources.clear()
				registry_SubQuerySources.clear()
				projectionSources.clear()
				projectionAttributes.clear()
				queryExpressions.clear()
				aggregationCounter = 0
				expressionCounter = 0
			}

			public static def boolean isMEPFunction(String name, String function) {
				if (functionStore.containsSymbol(name)) {
					try {
						var datatype = mep.parse(function).returnType
						for (IMepFunction<?> f : FunctionStore.instance.getFunctions(name))
							if (f.returnType.equals(datatype))
								return true
					} catch (Exception e) {
						return false
					}
				}
				return false;
			}

		}
		