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
		if (instance == null) {
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
			Map<Integer, List<SDFAttribute>> attributes = new HashMap<Integer, List<SDFAttribute>>();
			attributes = getSDFAttributesFromEqualPredicates(attributes,
					expression, resolver, joinOperator);
			// if fragmentation is not possible, remove all collected attributes
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (Integer inputPort : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(inputPort);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(
							arrayList);
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

	public Map<Integer, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(
			Map<Integer, List<SDFAttribute>> attributes, JoinAO joinOperator) {
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof RelationalPredicate) {
			RelationalPredicate relPredicate = (RelationalPredicate) predicate;
			IExpression<?> expression = relPredicate.getExpression()
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate.getExpression()
					.getAttributeResolver();
			attributes = getSDFAttributesFromEqualPredicates(attributes,
					expression, resolver, joinOperator);
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (Integer inputPort : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(inputPort);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(
							arrayList);
					arrayList.clear();
					arrayList.addAll(hashSet);
				}
			}
		}
		fragmentationIsPossible = true;
		return attributes;

	}

	private Map<Integer, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(
			Map<Integer, List<SDFAttribute>> attributes,
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
				// Attribute 1
				SDFAttribute attr1 = resolver.getAttribute(((Variable) arg1)
						.getIdentifier());
				
				int attribute1port = -1;
				if (joinOperator.getInputSchema(0)
						.findAttribute(attr1.getURI()) != null) {
					attribute1port = 0;
				} else if (joinOperator.getInputSchema(1)
						.findAttribute(attr1.getURI()) != null){
					attribute1port = 1;
				}

				// Attribute 2
				SDFAttribute attr2 = resolver.getAttribute(((Variable) arg2)
						.getIdentifier());
				
				int attribute2port = -1;
				if (joinOperator.getInputSchema(0)
						.findAttribute(attr2.getURI()) != null) {
					attribute2port = 0;
				} else if (joinOperator.getInputSchema(1)
						.findAttribute(attr2.getURI()) != null){
					attribute2port = 1;
				}

				// save attributes in combination with input port
				if (attribute1port > -1 && attribute2port > -1){
					if (!attributes.containsKey(attribute1port)) {
						attributes.put(attribute1port,
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute1port).add(attr1);
				
					if (!attributes.containsKey(attribute2port)) {
						attributes.put(attribute2port,
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute2port).add(attr2);
				}
			}
		} else {
			fragmentationIsPossible = false;
		}

		return attributes;
	}
}
