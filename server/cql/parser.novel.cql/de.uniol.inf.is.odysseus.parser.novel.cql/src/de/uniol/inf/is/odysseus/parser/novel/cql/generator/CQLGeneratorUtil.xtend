package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SimpleSource
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator.Operator
import java.util.List
import java.util.Map
import java.util.Map.Entry
import java.util.Set
import java.util.stream.Collectors
import org.eclipse.xtext.EcoreUtil2

class CQLGeneratorUtil {

	public static var CQLGeneratorUtil instance = null

	public static def CQLGeneratorUtil getInstance(CQLGenerator generator) {
		return if(instance === null) (instance = new CQLGeneratorUtil(generator)) else instance
	}

	private var CQLGenerator generator
	private var List<SourceStruct> registry_Sources = newArrayList
	private var Map<String, String> registry_Expressions = newHashMap
	private var List<String> registry_AggregationAttributes = newArrayList
	private var Map<String, Set<String>> registry_SubQuerySources = newHashMap

	private new(CQLGenerator generator) {
		this.generator = generator
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
			if (argument.attribute !== null)
				attributes.add(argument.attribute)
			else if (argument.expression !== null) // Attributes that are contained by functions or aggregations
			{
				var expressionAttributes = EcoreUtil2.getAllContentsOfType(argument.expression, Attribute)
				for (Attribute attribute : expressionAttributes)
					if (!attributes.stream.map(e|e.name).collect(Collectors.toList).contains(attribute.name))
						attributes.add(attribute)
			// TODO insert if(!contains(attributes, attribute)) attributes.add(attribute) here
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
			println('getAttibutename() -> map = ' + map.toString)
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

				println("getSelectedAttributes() -> map= " + map.toString + ', order= ' + attributeOrder.toString)
				return map
			}

			/** Returns all {@link Attribute} elements from the corresponding source. */
			def public List<String> getAttributeNamesFrom(String srcname) {
				for (SourceStruct source : registry_Sources)
					if (source.sourcename.equals(srcname) || source.aliases.contains(srcname))
						return source.attributes.stream.map(e|e.attributename).collect(Collectors.toList);
			}

			def private String parseAdditionalOperator(Operator operator, SimpleSelect select) {
				var Object[] result = null
				var String operatorName = null
				switch (operator) {
					case MAP: {
						var expressions = queryExpressions.get(select)
						if (expressions !== null && !expressions.empty) {
							result = buildMapOperator(expressions)
							operatorName = result.get(1).toString
						}
					}
					case AGGREGATE: {
						var aggregations = queryAggregations.get(select)
						if (aggregations !== null && !aggregations.empty) {
							result = buildAggregateOP(aggregations, select.order, select.sources)
							operatorName = registerOperator(result.get(1).toString)
						}
					}
				}
				return operatorName
			}

			def private Object[] parseAttribute(Attribute attribute) {
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

			def public List<String> getSourceNames() {
				return registry_Sources.stream.map(e|e.sourcename).collect(Collectors.toList)
			}

			def public String getSourcenameFromAlias(String sourcealias) {
				for (Entry<SourceStruct, List<String>> source : getSourceAliases().entrySet)
					if (source.value.contains(sourcealias))
						return source.key.sourcename
				return null
			}

			def public AttributeStruct getAttributeFromAlias(String alias) {
				println(getAttributeAliases().toString)
				for (Entry<AttributeStruct, List<String>> entry : getAttributeAliases().entrySet)
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

			def public List<String> getSourceAliasesAsList() {
				var list = newArrayList
				for (List<String> l : getSourceAliases().values)
					list.addAll(l)
				return list
			}

			def public Map<SourceStruct, List<String>> getSourceAliases() {
				var map = newHashMap
				for (SourceStruct source : registry_Sources)
					map.put(source, source.aliases)
				return map
			}
			
			def public Map<AttributeStruct, List<String>> getAttributeAliases() {
				var map = newHashMap
				for (SourceStruct source : registry_Sources)
					for (AttributeStruct attribute : source.attributes)
						if (!attribute.aliases.empty)
							map.put(attribute, attribute.aliases)
				return map
			}
			
			

		}
		