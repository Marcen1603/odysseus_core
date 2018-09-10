package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Matrix
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Vector
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.ArrayList
import java.util.Collection

class ExpressionParser implements IExpressionParser {
	
	var IAttributeNameParser nameParser;
	IUtilityService utilityService;
	
	@Inject
	new (IUtilityService utilityService, IAttributeNameParser nameParser) {
		this.utilityService = utilityService;
		this.nameParser = nameParser;
	}	
	
	override parse(SelectExpression e) {
		var str = ''
		for (var i = 0; i < e.expressions.size; i++) {
			var component = (e.expressions.get(i) as ExpressionComponent).value
			switch (component) {
				Function:
					str += component.name + '(' + parse((component.value as SelectExpression)) + ')'
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
	
	override Collection<SelectExpression> extractSelectExpressionsFromArgument(Collection<SelectArgument> args) {
		
		val ArrayList<SelectExpression> list = newArrayList
		
		args.stream()
			.filter(a | a.expression != null)
			.forEach(a | {
				if (a.expression.expressions.size == 1) {
					
					var aggregation = a.expression.expressions.get(0)
					var function = aggregation.value
					
					if (function instanceof Function) {
						if (utilityService.isMEPFunctionMame(function.name, parse(a.expression as SelectExpression).toString)) {
							list.add(a.expression)
						}
					} else {
						list.add(a.expression)
					}
				} else {
					list.add(a.expression)
				}
			})
		
		return list
	}
	
}