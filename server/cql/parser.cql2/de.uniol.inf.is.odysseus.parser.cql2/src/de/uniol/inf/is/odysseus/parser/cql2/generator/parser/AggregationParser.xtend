package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Starthing
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Collection
import java.util.stream.Collectors

class AggregationParser implements IAggregationParser {

	var AbstractPQLOperatorBuilder builder
	var IUtilityService utilityService;
	var IJoinParser joinParser;
	var IAttributeNameParser nameParser;
	var IAttributeParser attributeParser;
	var String argsstr = ''

	val String regex = ",$";

	@Inject
	new (AbstractPQLOperatorBuilder builder, IUtilityService utilityService, IJoinParser joinParser, IAttributeNameParser nameParser, IAttributeParser attributeParser) {

		this.builder = builder;
		this.utilityService = utilityService;
		this.joinParser = joinParser;
		this.nameParser = nameParser;
		this.attributeParser = attributeParser;
		
	}
	
	override public Object[] parse(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<Source> srcs) {
		return buildAggregateOP(list, list2, srcs);
	}

	def private Object[] buildAggregateOP(Collection<QueryAggregate> aggAttr, Collection<Attribute> orderAttr, CharSequence input) {

		argsstr = ''
		var mapName = ''
		val Collection<String> args = newArrayList
		val Collection<String> aliases = newArrayList

		aggAttr.stream.forEach(e | {
			
			argsstr = ''
			val aggregate = e.expression.expressions.get(0).value as Function
			val components = (aggregate.value as SelectExpression).expressions
			var attributename = ''
			var datatype = ''
			
			if (components.size == 1) {
				
				val comp = components.get(0).value
				
				switch (comp) {
					Attribute: {
						
						// check if attribute is already known
						var QueryAttribute queryAttribute = utilityService.getQueryAttribute(comp);
						if (queryAttribute != null) {
							attributename = queryAttribute.name;
							datatype = queryAttribute.datatype;
						// otherwise
						} else {
							attributename = nameParser.parse(comp.name)
							datatype = utilityService.getDataTypeFrom(attributename)
						}
						
					}
					Starthing: {
						attributename = '*'
					}
				}
				
			} else {
				
								// TODO HERE!
//				var mapOperator = buildMapOperator(#[aggregation.value as SelectExpression], input.toString)
//				mapName = cacheService.getOperatorCache().registerOperator(mapOperator.get(1) as CharSequence)
//				attributename = (mapOperator.get(0) as List<String>).get(0)
//				datatype = 'DOUBLE'
				
			}
			
			
			args.add(aggregate.name)
			args.add(attributename)
			var alias = ''
			
			if (e.expression.alias !== null) {
				alias = e.expression.alias.name
			} else {
				alias = attributeParser.getAggregationName(aggregate.name)
			}
			
			args.add(alias)
			aliases.add(alias)

			if(datatype != '') { 
				args.add(datatype)
			}
			
			// utilityServicetil.getRegisteredAggregationAttributes().add(alias)
			
//			cacheService.getAggregationAttributeCache().add(new Pair(aggregation, alias));
			
			args.add(',')
			argsstr += utilityService.generateKeyValueString(args)
			
			args.clear
			
		})
		
		// remove last comma from string
		argsstr.replaceAll(regex, "");
		
		// Generates the group by argument that is formed like ['attr1', attr2', ...]
		var groupby = ''
		if (!orderAttr.empty) {
			groupby += utilityService.generateListString(orderAttr.stream.map(e|nameParser.parse(e.name, null)).collect(Collectors.toList))
		}
		
		return #[aliases,
			builder.build(typeof(AggregateAO),
				newHashMap('aggregations' -> argsstr, 'group_by' -> if(groupby != '') groupby else null,
					'input' -> if(mapName != '') mapName else input.toString))] // '''AGGREGATE({AGGREGATIONS=[«argsstr»]«groupby»}, «IF mapName != ''»«mapName»«ELSE»«»«input»«ENDIF»)''']
	}

	def private Object[] buildAggregateOP(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<Source> srcs) {
		return buildAggregateOP(list, list2, joinParser.buildJoin(srcs))
	}

}
