package de.uniol.inf.is.odysseus.multithreaded.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class SDFAttributeHelper {

	private boolean fragmentationIsPossible = true;
	private static SDFAttributeHelper instance;

	public static SDFAttributeHelper getInstance() {
		if (instance == null){
			instance = new SDFAttributeHelper();
		}
		return instance;
	}

	public boolean validateStructureOfPredicate(JoinAO joinOperator) {
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof RelationalPredicate) {
			RelationalPredicate relPredicate = (RelationalPredicate) predicate;
			IExpression<?> expression = relPredicate.getExpression()
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate.getExpression()
					.getAttributeResolver();
			Map<String, List<SDFAttribute>> attributes = new HashMap<String, List<SDFAttribute>>();
			attributes = getSDFAttributesFromEqualPredicates(attributes,
					expression, resolver, joinOperator);
			// if fragmentation is not possible, remove all collected attributes
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (String sourceName : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(sourceName);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(arrayList);
					arrayList.clear();
					arrayList.addAll(hashSet);
				}
			}

			fragmentationIsPossible = true;
			// check if at least two attributes exists, one for each input
			// stream
			if (attributes.keySet().size() >= 2) {
				return true;
			}
		}
		return false;
	}

	public Map<String, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(Map<String, List<SDFAttribute>> attributes, JoinAO joinOperator){
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof RelationalPredicate) {
			RelationalPredicate relPredicate = (RelationalPredicate) predicate;
			IExpression<?> expression = relPredicate.getExpression()
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate.getExpression()
					.getAttributeResolver();
			attributes = getSDFAttributesFromEqualPredicates(attributes, expression,
							resolver, joinOperator);
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (String sourceName : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(sourceName);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(arrayList);
					arrayList.clear();
					arrayList.addAll(hashSet);
				}
			}
		}
		fragmentationIsPossible = true;
		return attributes;
		
	}
	
	
	
	private Map<String, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(
			Map<String, List<SDFAttribute>> attributes,
			IExpression<?> expression, IAttributeResolver resolver,
			JoinAO joinOperator) {
		String symbol = expression.toFunction().getSymbol();

		if (expression.isFunction() && (symbol.equalsIgnoreCase("&&"))) {

			IBinaryOperator<?> binaryOperator = (IBinaryOperator<?>) expression;
			IExpression<?> argument1 = binaryOperator.getArgument(0);
			if (argument1.isFunction()) {
				// recursive call
				getSDFAttributesFromEqualPredicates(attributes, argument1,
						resolver, joinOperator);
			}
			IExpression<?> argument2 = binaryOperator.getArgument(1);
			if (argument2.isFunction()) {
				// recursive call
				getSDFAttributesFromEqualPredicates(attributes, argument2,
						resolver, joinOperator);
			}
		} else if ((expression.isFunction())
				&& (expression.toFunction().getSymbol().equalsIgnoreCase("="))) {
			final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
			final IExpression<?> arg1 = eq.getArgument(0);
			final IExpression<?> arg2 = eq.getArgument(1);
			if ((arg1.isVariable()) && (arg2.isVariable())) {
				SDFAttribute attr1 = resolver
						.getAttribute(((Variable) arg1).getIdentifier());
				
				SDFAttribute attribute1 = joinOperator.getInputSchema(0).findAttribute(attr1.getURI());
				
				if (attribute1 == null){
					attribute1 = joinOperator.getInputSchema(1).findAttribute(attr1.getURI());
					System.err.println("found in 1");
				}
				
				SDFAttribute attr2 = resolver
						.getAttribute(((Variable) arg2).getIdentifier());

				SDFAttribute attribute2 = joinOperator.getInputSchema(0).findAttribute(attr2.getURI());
				if (attribute2 == null){
					attribute2 = joinOperator.getInputSchema(1).findAttribute(attr2.getURI());
				}
				
				
				List<String> baseSourceNamesLeft = joinOperator.getInputSchema(
						0).getBaseSourceNames();
				List<String> baseSourceNamesRight = joinOperator
						.getInputSchema(1).getBaseSourceNames();

				if ((baseSourceNamesLeft.contains(attribute1.getSourceName()) || baseSourceNamesLeft
						.contains(attribute2.getSourceName()))
						&& (baseSourceNamesRight.contains(attribute1
								.getSourceName()) || baseSourceNamesRight
								.contains(attribute2.getSourceName()))) {
					// we need to check if each input stream has an
					// fragmentation
					// attribute
					if (!attributes.containsKey(attribute1.getSourceName())) {
						attributes.put(attribute1.getSourceName(),
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute1.getSourceName()).add(attribute1);

					if (!attributes.containsKey(attribute2.getSourceName())) {
						attributes.put(attribute2.getSourceName(),
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute2.getSourceName()).add(attribute2);
				} else {
					fragmentationIsPossible = false;
				}
			}
		} else {
			fragmentationIsPossible = false;
		}

		return attributes;
	}
}
