package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import java.util.Collection
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import java.util.List
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Starthing
import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import java.util.stream.Collectors
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source

class AggregationParser implements IAggregationParser {

	var AbstractPQLOperatorBuilder builder
	var IUtilityService utilityService;
	var IJoinParser joinParser;
	var IAttributeNameParser nameParser;
	var IAttributeParser attributeParser;

	@Inject
	new (AbstractPQLOperatorBuilder builder, IUtilityService utilityService, IJoinParser joinParser, IAttributeNameParser nameParser, IAttributeParser attributeParser) {

		this.builder = builder;
		this.utilityService = utilityService;
		this.joinParser = joinParser;
		this.nameParser = nameParser;
		this.attributeParser = attributeParser;
		
	}
	
	override public Object[] parse(Collection<SelectExpression> list, List<Attribute> list2, List<Source> srcs) {
		return buildAggregateOP(list, list2, srcs);
	}

	def private Object[] buildAggregateOP(Collection<SelectExpression> aggAttr, List<Attribute> orderAttr,
		CharSequence input) {
		var argsstr = ''
		var List<String> args = newArrayList
		var List<String> aliases = newArrayList
		var mapName = ''
		for (var i = 0; i < aggAttr.length; i++) {
			var aggregation = aggAttr.get(i).expressions.get(0).value as Function
			var attributename = ''
			var datatype = ''
			var components = (aggregation.value as SelectExpression).expressions
			if (components.size == 1) {
				var comp = components.get(0).value
				switch (comp) {
					Attribute: {
						attributename = nameParser.parse(comp.name)
						datatype = utilityService.getDataTypeFrom(attributename)

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

			args.add(aggregation.name)
			args.add(attributename)
			var alias = ''
			if (aggAttr.get(i).alias !== null)
				alias = aggAttr.get(i).alias.name
			else
				alias = attributeParser.getAggregationName(aggregation.name)
			args.add(alias)
			aliases.add(alias)

			if(datatype != '') args.add(datatype)
			// utilityServicetil.getRegisteredAggregationAttributes().add(alias)
			utilityService.addAggregationAttribute(aggAttr.get(i), alias)
			args.add(',')
			argsstr += utilityService.generateKeyValueString(args)
			if(i != aggAttr.length - 1) argsstr += ','
			args.clear
		}
		// Generates the group by argument that is formed like ['attr1', attr2', ...]
		var groupby = ''
		if (!orderAttr.empty)
			groupby +=
				utilityService.generateListString(orderAttr.stream.map(e|nameParser.parse(e.name, null)).collect(Collectors.toList))
		return #[aliases,
			builder.build(typeof(AggregateAO),
				newHashMap('aggregations' -> argsstr, 'group_by' -> if(groupby != '') groupby else null,
					'input' -> if(mapName != '') mapName else input.toString))] // '''AGGREGATE({AGGREGATIONS=[«argsstr»]«groupby»}, «IF mapName != ''»«mapName»«ELSE»«»«input»«ENDIF»)''']
	}

	def private Object[] buildAggregateOP(Collection<SelectExpression> list, List<Attribute> list2, List<Source> srcs) {
		return buildAggregateOP(list, list2, joinParser.buildJoin(srcs))
	}

}
