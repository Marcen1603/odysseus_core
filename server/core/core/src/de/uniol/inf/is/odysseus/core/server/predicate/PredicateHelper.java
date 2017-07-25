package de.uniol.inf.is.odysseus.core.server.predicate;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.ComparablePair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.IEqualsOperator;

@SuppressWarnings("rawtypes")
public class PredicateHelper {

	/**
	 * Checks if predicate is of the form (a == b AND b == c AND d == f AND ...)
	 * 
	 * @param predicate
	 * @param neededAttrs
	 *            In this set the attributes used in predicate are stored
	 * @return
	 */
	public static boolean checkEqualsPredicate(IPredicate predicate, Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs,
			SDFSchema ownSchema, SDFSchema otherSchema) {
		// TODO nach Umstellung auf MEP auskommenieren
		// ===========================================

		String mepString = predicate.toString();
		SDFSchema outputSchema = SDFSchema.union(ownSchema, otherSchema);
		IAttributeResolver attrRes = new DirectAttributeResolver(outputSchema);
		SDFExpression expr = new SDFExpression(null, mepString, attrRes, MEP.getInstance(),
				AggregateFunctionBuilderRegistry.getAggregatePattern());
		// ===========================================

		IMepExpression<?> mepExpr = expr.getMEPExpression();
		return checkEqualsPredicate(mepExpr, neededAttrs, ownSchema, otherSchema);

		// if(predicate instanceof AndPredicate){
		// return checkPredicate(((AndPredicate)predicate).getLeft(),
		// neededAttrs) &&
		// checkPredicate(((AndPredicate)predicate).getRight(), neededAttrs);
		// }
		// else if(predicate instanceof RelationalPredicate){
		// RelationalPredicate relPred = (RelationalPredicate)predicate;
		// IExpression mepExpr = relPred.getExpression().getMEPExpression();
		// if(checkMEPExpression(mepExpr)){
		// List<SDFAttribute> attrRelPred = relPred.getAttributes();
		// neededAttrs.addAll(attrRelPred);
		// return true;
		// }
		// }
		// // a TruePredicate does not restrict anything a == b AND true is the
		// // same as a==b
		// else if(predicate instanceof TruePredicate){
		// return true;
		// }
		// return false;
	}

	public static boolean checkEqualsPredicate(IMepExpression mepExpr, Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs, SDFSchema ownSchema, SDFSchema otherSchema){
		if(mepExpr instanceof AndOperator){
			return checkEqualsPredicate(((AndOperator)mepExpr).getArgument(0), neededAttrs, ownSchema, otherSchema) &&
					checkEqualsPredicate(((AndOperator)mepExpr).getArgument(1), neededAttrs, ownSchema, otherSchema);
		}
		if(mepExpr instanceof IEqualsOperator){
			
			// get left and right argument
			// if these are variables
			// return true
			IEqualsOperator eq = (IEqualsOperator)mepExpr;
			IMepExpression arg1 = eq.getArgument(0);
			IMepExpression arg2 = eq.getArgument(1);
			
			if(arg1 instanceof IMepVariable && arg2 instanceof IMepVariable){
				String id1 = ((IMepVariable)arg1).getIdentifier();
				String id2 = ((IMepVariable)arg2).getIdentifier();
				
				SDFAttribute ownAttr = ownSchema.findAttribute(id1);
				if (ownAttr == null){
					ownAttr = otherSchema.findAttribute(id1);
				}
				
				SDFAttribute otherAttr = otherSchema.findAttribute(id2);
				if (otherAttr == null){
					otherAttr = ownSchema.findAttribute(id2);
				}
				
				if (ownAttr != null && otherAttr != null){
						neededAttrs.add(new ComparablePair<SDFAttribute, SDFAttribute>(ownAttr, otherAttr));
				}
				return true;
			}
			return false;
		}
		return false;
	}
}
