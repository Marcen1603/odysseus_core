package de.uniol.inf.is.odysseus.rewrite.relational;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This class provides functions to support Restructuring Aspects
 * 
 * @author Marco Grawunder
 * 
 */

public class RestructHelper {

	/**
	 * Sets newSon at father on fatherPort and son to newSon n newSonPort Does
	 * not change Schemata, because it depends on newSon operator type (e.g.
	 * join)
	 * 
	 * @param fatherPort
	 *            Port on which son is registered
	 * @param newSonPort
	 *            newSon input port to which son should be moved
	 * @param father
	 *            father node
	 * @param son
	 *            node to move behind newSon
	 * @param newSon
	 *            node to move between father and son
	 */
	private static void switchOp(int fatherPort, int sonPort, int newSonPort,
			AbstractLogicalOperator father, AbstractLogicalOperator son, AbstractLogicalOperator newSon) {
		// Set new son at fathers fatherPort
		father.setInputAO(fatherPort, newSon);
		// Set old Input from newSon on newSonPort to son on sonPort
		son.setInputAO(sonPort, newSon.getInputAO(newSonPort));
		// Set son as Input on new Son
		newSon.setInputAO(newSonPort, son);
	}

//	public static void pushDownSelection(int newSonPort, AbstractLogicalOperator father,
//			SelectAO selectPO, JoinAO joinPO) {
//		int i = -1;
//		// TODO: While oder if ?? 5.11.08 While, falls der Operator mehrfach am
//		// Vater hängt muss
//		// er auch hier jetzt direkt mehrfach getauscht werden. Hinterher geht
//		// das nicht mehr (?)
//		while ((i = father.getInputPort(selectPO)) != -1) {
//			pushDownSelection(i, newSonPort, father, selectPO, joinPO);
//		}
//	}

//	public static void pushDownSelection(int fatherPort, int newSonPort,
//			AbstractLogicalOperator father, SelectAO selectPO, JoinAO joinPO) {
//		// Change Operators
//		// 0 because selectPO is a unaryOp
//		switchOp(fatherPort, 0, newSonPort, father, selectPO, joinPO);
//		// Change schemas
//		SDFAttributeList jInSchema = joinPO.getInputSchema(newSonPort);
//		selectPO.setInputSchema(jInSchema);
//		selectPO.setOutputSchema(jInSchema);
//		// Whats this for?
//		CQLParser.initPredicates(selectPO);
//		joinPO.setOutputSchema(SDFAttributeList.union(joinPO.getInputAO(0)
//				.getOutputSchema(), joinPO.getInputAO(1).getOutputSchema()));
//
//	}

	public static boolean subsetPredicate(
			IPredicate<RelationalTuple<?>> predicate,
			SDFAttributeList attributes) {
		
		final List<String> uris = new ArrayList<String>(attributes.getAttributeCount());
		for(SDFAttribute curAttr : attributes) {
			uris.add(curAttr.getURI());
		}
		final boolean[] retValue = new boolean[]{true};
		RestructHelper.visitPredicates(predicate, new RestructHelper.IUnaryFunctor<IPredicate<?>>() {
			public void call(IPredicate<?> predicate) {
				if (predicate instanceof IRelationalPredicate) {
					IRelationalPredicate relPred = (IRelationalPredicate)predicate;
					List<SDFAttribute> tmpAttrs = relPred.getAttributes();
					List<String> tmpUris = new ArrayList<String>(tmpAttrs.size());
					for(SDFAttribute curAttr : tmpAttrs) {
						tmpUris.add(curAttr.getURI());
					}
					if (!uris.containsAll(tmpUris)) {
						retValue[0] = false;
					}
				}
			}
		});
		return retValue[0];
	}
//	FIXME WTF das kann nie funktioniert haben ...
//	public static SDFAttributeList unionPredicate(IPredicate attributes1,
//			SDFAttributeList attributes2) {
//		if (attributes1 instanceof RelationalTuple) {
//			RelationalTuple att = (RelationalTuple) attributes1;
//			return SDFAttributeList.union(att.getSchema(), attributes2);
//		} else if (attributes1 instanceof RelationalPredicate) {
//			RelationalPredicate att = (RelationalPredicate) attributes1;
//			SDFAttributeList list = new SDFAttributeList(att.getAttributes());
//			return SDFAttributeList.union(list, attributes2);
//		} else {
//			return attributes2;
//		}
//	}

	public static interface IUnaryFunctor<T> {
		public void call(T parameter);
	}

	public static void visitPredicates(IPredicate<?> p,
			IUnaryFunctor<IPredicate<?>> functor) {
		Stack<IPredicate<?>> predicates = new Stack<IPredicate<?>>();
		predicates.push(p);
		while (!predicates.isEmpty()) {
			IPredicate<?> curPred = predicates.pop();
			if (curPred instanceof ComplexPredicate) {
				predicates.push(((ComplexPredicate<?>) curPred).getLeft());
				predicates.push(((ComplexPredicate<?>) curPred).getRight());
			} else {
				functor.call(curPred);
			}
		}
	}
}
