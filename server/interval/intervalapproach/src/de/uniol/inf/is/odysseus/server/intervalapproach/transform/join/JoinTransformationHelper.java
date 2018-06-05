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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.PredicateHelper;
import de.uniol.inf.is.odysseus.persistentqueries.HashJoinSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

@SuppressWarnings({ "rawtypes" })
public class JoinTransformationHelper {
	
	private static Logger LOG = LoggerFactory.getLogger(JoinTransformationHelper.class);




	// /**
	// * Checks if expr is of the form (a == b AND b == c AND d == f AND ...)
	// * @param expr
	// * @return
	// */
	// private static boolean checkMEPExpression(IExpression expr){
	// if(expr instanceof AndOperator){
	// return checkMEPExpression(((AndOperator)expr).getArgument(0)) &&
	// checkMEPExpression(((AndOperator)expr).getArgument(1));
	// }
	// if((expr instanceof IFunction) && (expr instanceof EqualsOperator)){
	// return true;
	// }
	// return false;
	// }
	//
	/**
	 * This method calculates the restrict lists for HashJoinSweepAreas.
	 * 
	 * @param joinPO
	 * @param neededAttrs
	 * @param port
	 * @return A Pair, containing the insertRestrictList in E1 and the
	 *         queryRestrictList in E2
	 */
	public static Pair<int[], int[]> createRestrictLists(SDFSchema ownSchema,SDFSchema otherSchema,
			List<Pair<SDFAttribute, SDFAttribute>> neededAttrs) {

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

		ArrayList<Integer> queryRestrictList = new ArrayList<Integer>(); // restrict
																			// list
																			// for
																			// querying
																			// (other
																			// schema)
		ArrayList<Integer> insertRestrictList = new ArrayList<Integer>(); // restrict
																			// list
																			// for
																			// inserting
																			// into
																			// a
																			// sweep
																			// area
																			// (own
																			// schema)

		for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
			SDFAttribute ownAttr = pair.getE1(); // E1 contains Attributes from
													// the own schema, E2 from
													// the other
			SDFAttribute otherAttr = pair.getE2(); // E1 contains Attributes
													// from the own schema, E2
													// from the other

			if (!usedOwnAttributes.contains(ownAttr) && !usedOtherAttributes.contains(otherAttr)) {
				int ownIndex = ownSchema.indexOf(ownAttr);
				int otherIndex = otherSchema.indexOf(otherAttr);
				
				// TMP-HACK
				if (ownIndex == -1 && otherSchema.indexOf(ownAttr) > -1){
					otherIndex = otherSchema.indexOf(ownAttr);
					ownIndex = ownSchema.indexOf(otherAttr);
				}

				insertRestrictList.add(ownIndex);
				queryRestrictList.add(otherIndex);

				// don't use and attribute twice for restriction
				usedOwnAttributes.add(ownAttr);
				usedOtherAttributes.add(otherAttr);
			}
		}

		int[] insertRestrictArray = new int[insertRestrictList.size()];
		for (int i = 0; i < insertRestrictList.size(); i++) {
			insertRestrictArray[i] = insertRestrictList.get(i);
		}

		int[] queryRestrictArray = new int[queryRestrictList.size()];
		for (int i = 0; i < queryRestrictList.size(); i++) {
			queryRestrictArray[i] = queryRestrictList.get(i);
		}

		if(insertRestrictList.contains(-1)) {
			LOG.warn("Insert restriction list '{}' contains -1. needed attributes: {}", insertRestrictList, neededAttrs);
		}
		if(queryRestrictList.contains(-1)) {
			LOG.warn("Query restriction list '{}' contains -1. needed attributes: {}", queryRestrictList, neededAttrs);
		}
		
		return new Pair<int[], int[]>(insertRestrictArray, queryRestrictArray);
	}
	
	public static boolean canBeUsedWithHashJoin(IPredicate predicate, SDFSchema leftSchema, SDFSchema rightSchema, ITimeIntervalSweepArea[] areas) {
		boolean check = false;
		try {
			// check the paths
			for (int port = 0; port < 2; port++) {
				SDFSchema ownSchema = port==0?leftSchema:rightSchema;
				SDFSchema otherSchema = port!=0?leftSchema:rightSchema;
				// check the predicate and calculate
				// the restrictList
				Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (check = PredicateHelper.checkEqualsPredicate(predicate, neededAttrs,
						ownSchema,
						otherSchema)) {

					// transform the set into a list to guarantee the
					// same order of attributes for both restrict lists
					List<Pair<SDFAttribute, SDFAttribute>> neededAttrsList = new ArrayList<Pair<SDFAttribute, SDFAttribute>>();
					for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						neededAttrsList.add(pair);
					}

					Pair<int[], int[]> restrictLists = JoinTransformationHelper.createRestrictLists(ownSchema, otherSchema,
							neededAttrsList);
					// TODO: Flag to set if input is sorted
					// currently, default is false --> to be sure
					areas[port] = new HashJoinSweepArea(restrictLists.getE1(), restrictLists.getE2(), false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return check;
	}

}
