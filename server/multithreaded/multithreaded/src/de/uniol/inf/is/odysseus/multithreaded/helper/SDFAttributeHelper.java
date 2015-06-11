package de.uniol.inf.is.odysseus.multithreaded.helper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class SDFAttributeHelper {

	public static boolean hasSDFAttributesInEqualPredicate(JoinAO joinOperator) {
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof RelationalPredicate) {
			RelationalPredicate relPredicate = (RelationalPredicate) predicate;
			IExpression<?> expression = relPredicate.getExpression()
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate.getExpression()
					.getAttributeResolver();
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			attributes.addAll(getSDFAttributesForEqualPredicates(attributes,
					expression, resolver, joinOperator));
			if (attributes.size() >= 2) {
				return true;
			}
		}
		return false;
	}

	public static List<SDFAttribute> getSDFAttributesForEqualPredicates(
			List<SDFAttribute> attributes, IExpression<?> expression,
			IAttributeResolver resolver, JoinAO joinOperator) {
		String symbol = expression.toFunction().getSymbol();

		if ((expression.isFunction())
				&& (symbol.equalsIgnoreCase("xor")
						|| symbol.equalsIgnoreCase("||") || symbol
							.equalsIgnoreCase("&&"))) {
			// TODO check if all functions are okay

			IBinaryOperator<?> binaryOperator = (IBinaryOperator<?>) expression;
			IExpression<?> argument1 = binaryOperator.getArgument(0);
			if (argument1.isFunction()) {
				// recursive call
				getSDFAttributesForEqualPredicates(attributes, argument1,
						resolver, joinOperator);
			}
			IExpression<?> argument2 = binaryOperator.getArgument(1);
			if (argument2.isFunction()) {
				// recursive call
				getSDFAttributesForEqualPredicates(attributes, argument2,
						resolver, joinOperator);
			}
		} else if ((expression.isFunction())
				&& (expression.toFunction().getSymbol().equalsIgnoreCase("="))) {
			final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
			final IExpression<?> arg1 = eq.getArgument(0);
			final IExpression<?> arg2 = eq.getArgument(1);
			if ((arg1.isVariable()) && (arg2.isVariable())) {
				SDFAttribute attribute1 = resolver
						.getAttribute(((Variable) arg1).getIdentifier());
				SDFAttribute attribute2 = resolver
						.getAttribute(((Variable) arg2).getIdentifier());
				List<String> baseSourceNamesLeft = joinOperator.getInputSchema(
						0).getBaseSourceNames();
				List<String> baseSourceNamesRight = joinOperator
						.getInputSchema(1).getBaseSourceNames();

				if ((baseSourceNamesLeft.contains(attribute1.getSourceName()) || baseSourceNamesLeft
						.contains(attribute2.getSourceName()))
						&& (baseSourceNamesRight.contains(attribute1
								.getSourceName()) || baseSourceNamesRight
								.contains(attribute2.getSourceName()))){
					// we need to check if each input stream has an fragmentation
					// attribute
					attributes.add(attribute1);
					attributes.add(attribute2);					
				}
			}
		}

		return attributes;
	}
}
