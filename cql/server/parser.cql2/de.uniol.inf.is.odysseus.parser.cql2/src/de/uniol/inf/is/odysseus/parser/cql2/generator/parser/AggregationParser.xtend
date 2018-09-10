package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Starthing
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Collection
import java.util.Optional
import java.util.stream.Collectors
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException

class AggregationParser implements IAggregationParser {

	var AbstractPQLOperatorBuilder builder
	var IUtilityService utilityService;
	var IJoinParser joinParser;
	var IAttributeNameParser nameParser;
	var IAttributeParser attributeParser;
	var ICacheService cacheService;
	var String argsstr = ''

	val String regex = ",$";

	@Inject
	new (AbstractPQLOperatorBuilder builder, IUtilityService utilityService, IJoinParser joinParser, IAttributeNameParser nameParser, IAttributeParser attributeParser, ICacheService cacheService) {
		this.builder = builder;
		this.utilityService = utilityService;
		this.joinParser = joinParser;
		this.nameParser = nameParser;
		this.attributeParser = attributeParser;
		this.cacheService = cacheService;
	}
	
	override public Object[] parse(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<QuerySource> srcs, SimpleSelect select) {
		return buildAggregateOP(list, list2, srcs, select);
	}

	def private Object[] buildAggregateOP(Collection<QueryAggregate> aggAttr, Collection<Attribute> orderAttr, CharSequence input) throws PQLOperatorBuilderException {

		argsstr = ''
		var mapName = ''
		val Collection<String> args = newArrayList
		val Collection<String> aliases = newArrayList

		aggAttr.stream.forEach(e | {
			
			argsstr = ''
			val aggregate = e.parsedAggregation.expression.expressions.get(0).value as Function
			val components = (aggregate.value as SelectExpression).expressions
			var attributename = ''
			var datatype = ''
			
			if (components.size == 1) {
				
				val comp = components.get(0).value
				
				switch (comp) {
					Attribute: {
						// Get concrete attribute name
						var Optional<QueryAttribute> queryAttribute = utilityService.getQueryAttribute(comp);
						if (queryAttribute.isPresent()) {
							attributename = queryAttribute.get().getName();
							// Shouldn't the data type specified by the aggregate? 
							datatype = queryAttribute.get().getDataType();
						}						
					}
					// only working with COUNT(*)
					Starthing: {
						attributename = '*'
					}
				}
				
			} else {
				//TODO Add here code for processing aggregates with MEP-expressions as input
			}
			
			args.add(aggregate.name)
			args.add(attributename)
			var alias = ''
			
			if (e.parsedAggregation.expression.alias !== null) {
				alias = e.parsedAggregation.expression.alias.name
			} else {
				alias = attributeParser.getAggregationName(aggregate.name)
			}
			
			args.add(alias)
			aliases.add(alias)

			if(datatype != '') { 
				args.add(datatype)
			}
			
			args.add(',')
			argsstr += utilityService.generateKeyValueString(args)
			
			args.clear
			
		})
		
		// remove last comma from string
		argsstr.replaceAll(regex, "");
		
		// Generates the group by argument that is formed like ['attr1', attr2', ...]
		var groupby = ''
		if (!orderAttr.empty) {
			groupby += utilityService.generateListString(
				orderAttr.stream.map(e| e.name).collect(Collectors.toList)					
			);
		}
		
		return #[aliases,
			builder.build(typeof(AggregateAO),
				newHashMap('aggregations' -> argsstr, 'group_by' -> if(groupby != '') groupby else null,
					'input' -> if(mapName != '') mapName else input.toString))] // '''AGGREGATE({AGGREGATIONS=[«argsstr»]«groupby»}, «IF mapName != ''»«mapName»«ELSE»«»«input»«ENDIF»)''']
	}

	def private Object[] buildAggregateOP(Collection<QueryAggregate> list, Collection<Attribute> list2, Collection<QuerySource> srcs, SimpleSelect select) {
		return buildAggregateOP(list, list2, joinParser.buildJoin(srcs, select))
	}

}
