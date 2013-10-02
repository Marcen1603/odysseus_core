/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.interval.transform.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.ComparablePair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
@SuppressWarnings({"unchecked","rawtypes"})
public class JoinTransformationHelper {

	
	/**
	 * Checks if there is a window in any path to a source
	 * of this operator.
	 * 
	 * @param operator
	 * @return false, if there is a window in the path.
	 * 		   true, if there is no window in the path.
	 */
	public static boolean checkPhysicalPath(Object operator){
		if(operator instanceof AbstractWindowTIPO){
			return false;
		}
		if(((ISource)operator).isSink()){
			// in each child path there must
			// be no window. If in one path
			// there is a window, we need
			// temporal sweep area in our
			// join.
			ISink opAsSink = (ISink)operator;
			Collection<PhysicalSubscription> subs = opAsSink.getSubscribedToSource();
			for(PhysicalSubscription s: subs){
				if(!checkPhysicalPath(s.getTarget())){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if there is a window in any path to a source
	 * of this operator.
	 * 
	 * @param operator
	 * @return false, if there is a window in the path.
	 * 		   true, if there is no window in the path.
	 */
	public static boolean checkLogicalPath(ILogicalOperator operator){
		if(operator instanceof AbstractWindowAO){
			return false;
		}
		
		// in each child path there must
		// be no window. If in one path
		// there is a window, we need
		// temporal sweep area in our
		// join.
		Collection<LogicalSubscription> subs = operator.getSubscribedToSource();
		for(LogicalSubscription s: subs){
			if(!checkLogicalPath(s.getTarget())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if predicate is of the form (a == b AND b == c AND d == f AND ...)
	 * @param predicate
	 * @param neededAttrs In this set the attributes used in predicate are stored
	 * @return
	 */
	public static boolean checkPredicate(IPredicate predicate, Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs, SDFSchema ownSchema, SDFSchema otherSchema){
		// TODO nach Umstellung auf MEP auskommenieren
	    // ===========================================
		
		String mepString = predicate.toString();
		SDFSchema outputSchema = SDFSchema.union(ownSchema, otherSchema);
		IAttributeResolver attrRes = new DirectAttributeResolver(outputSchema);
		SDFExpression expr = new SDFExpression(null, mepString, attrRes, MEP.getInstance());		
		// ===========================================
		
		IExpression<?> mepExpr = expr.getMEPExpression();
		return checkPredicate(mepExpr, neededAttrs, ownSchema, otherSchema);
		
//		if(predicate instanceof AndPredicate){
//			return checkPredicate(((AndPredicate)predicate).getLeft(), neededAttrs) && 
//					checkPredicate(((AndPredicate)predicate).getRight(), neededAttrs);
//		}
//		else if(predicate instanceof RelationalPredicate){
//			RelationalPredicate relPred = (RelationalPredicate)predicate;
//			IExpression mepExpr = relPred.getExpression().getMEPExpression();
//			if(checkMEPExpression(mepExpr)){
//				List<SDFAttribute> attrRelPred = relPred.getAttributes();
//				neededAttrs.addAll(attrRelPred);
//				return true;
//			}
//		}
//		// a TruePredicate does not restrict anything a == b AND true is the
//		// same as a==b
//		else if(predicate instanceof TruePredicate){
//			return true;
//		}
//		return false;
	}
	
	public static boolean checkPredicate(IExpression mepExpr, Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs, SDFSchema ownSchema, SDFSchema otherSchema){
		if(mepExpr instanceof AndOperator){
			return checkPredicate(((AndOperator)mepExpr).getArgument(0), neededAttrs, ownSchema, otherSchema) &&
					checkPredicate(((AndOperator)mepExpr).getArgument(1), neededAttrs, ownSchema, otherSchema);
		}
		if(mepExpr instanceof EqualsOperator){
			
			// get left and right argument
			// if these are variables
			// return true
			EqualsOperator eq = (EqualsOperator)mepExpr;
			IExpression arg1 = eq.getArgument(0);
			IExpression arg2 = eq.getArgument(1);
			
			if(arg1 instanceof Variable && arg2 instanceof Variable){
				String id1 = ((Variable)arg1).getIdentifier();
				String id2 = ((Variable)arg2).getIdentifier();
				
				// find the attribute from the own schema in this expression
				// in a == b either a or b can be from the own schema
				// Du to the pointURI a.b we can use
				// the equals(id1) || comparison(id2) in this loop
				SDFAttribute ownAttr = null;
				for(SDFAttribute curOwnAttr : ownSchema){
					if(curOwnAttr.getURI().equalsIgnoreCase(id1) ||
							curOwnAttr.getURI().equalsIgnoreCase(id2)){
						ownAttr = curOwnAttr;
						break;
					}
				}
				
				// find the attribute from the other schema in this expression
				// in a == b either a or b can be from the other schema
				SDFAttribute otherAttr = null;
				for(SDFAttribute curOtherAttr : otherSchema){
					if(curOtherAttr.getURI().equalsIgnoreCase(id1) ||
							curOtherAttr.getURI().equalsIgnoreCase(id2)){
						otherAttr = curOtherAttr; 
						break;
					}
				}
				
				neededAttrs.add(new ComparablePair<SDFAttribute, SDFAttribute>(ownAttr, otherAttr));

				return true;
			}
			return false;
		}
		return false;
	}
	
//	/**
//	 * Checks if expr is of the form (a == b AND b == c AND d == f AND ...)
//	 * @param expr
//	 * @return
//	 */
//	private static boolean checkMEPExpression(IExpression expr){
//		if(expr instanceof AndOperator){
//			return checkMEPExpression(((AndOperator)expr).getArgument(0)) && checkMEPExpression(((AndOperator)expr).getArgument(1));
//		}
//		if((expr instanceof IFunction) && (expr instanceof EqualsOperator)){
//			return true;
//		}
//		return false;
//	}
//	
	/**
	 * This method calculates the restrict lists for HashJoinSweepAreas.
	 * @param joinPO
	 * @param neededAttrs
	 * @param port
	 * @return A Pair, containing the insertRestrictList in E1 and the queryRestrictList in E2
	 */
	public static Pair<int[], int[]> createRestrictLists(AbstractPipe joinPO, List<Pair<SDFAttribute, SDFAttribute>> neededAttrs, int port){
	
		int otherPort = port^1;
		// run through the list of pairs
		// each time, if neither the attribute
		// from the left schema nor the attribute
		// from the right schema has already been
		// used, insert both attributes into
		// their corresponding restrict lists.
		// assume the following predicate:
		// a.x = b.y AND a.x = b.z AND a.y = b.x
		// From this, the following list of pairs
		// is generated:
		// a.x, b.y
		// a.x, b.z
		// a.y, b.x
		// here we don't need both attributes b.y and
		// b.z since, they must be equal due to the fact
		// that they are both compared to a.x
		ArrayList<SDFAttribute> usedOwnAttributes = new ArrayList<SDFAttribute>();
		ArrayList<SDFAttribute> usedOtherAttributes = new ArrayList<SDFAttribute>();
		
		SDFSchema ownSchema = joinPO.getSubscribedToSource(port).getSchema();
		SDFSchema otherSchema = joinPO.getSubscribedToSource(otherPort).getSchema();
		
		ArrayList<Integer> queryRestrictList = new ArrayList<Integer>(); // restrict list for querying (other schema)
		ArrayList<Integer> insertRestrictList = new ArrayList<Integer>(); // restrict list for inserting into a sweep area (own schema)
		
		for(Pair<SDFAttribute, SDFAttribute> pair: neededAttrs){
			SDFAttribute ownAttr = pair.getE1(); // E1 contains Attributes from the own schema, E2 from the other
			SDFAttribute otherAttr = pair.getE2(); // E1 contains Attributes from the own schema, E2 from the other
			
			if(!usedOwnAttributes.contains(ownAttr) && !usedOtherAttributes.contains(otherAttr)){
				int ownIndex = ownSchema.indexOf(ownAttr);
				int otherIndex = otherSchema.indexOf(otherAttr);
				
				insertRestrictList.add(ownIndex);
				queryRestrictList.add(otherIndex);
				
				// don't use and attribute twice for restriction
				usedOwnAttributes.add(ownAttr); 
				usedOtherAttributes.add(otherAttr);
			}
		}
		
		int[] insertRestrictArray = new int[insertRestrictList.size()];
		for(int i = 0; i<insertRestrictList.size(); i++){
			insertRestrictArray[i] = insertRestrictList.get(i);
		}
		
		int[] queryRestrictArray = new int[queryRestrictList.size()];
		for(int i = 0; i<queryRestrictList.size(); i++){
			queryRestrictArray[i] = queryRestrictList.get(i);
		}
		
		return new Pair<int[], int[]>(insertRestrictArray, queryRestrictArray);
	}

}
