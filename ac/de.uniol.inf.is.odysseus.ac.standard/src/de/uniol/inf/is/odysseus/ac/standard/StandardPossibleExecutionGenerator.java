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
package de.uniol.inf.is.odysseus.ac.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecutionGenerator;
import de.uniol.inf.is.odysseus.core.server.ac.StandardPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Standardimplementierung von {@link IPossibleExecutionGenerator}.
 * Erstellt aus der Menge aller Anfragen eine Liste möglicher Anfragekombinationen
 * nach Brute-Force. 
 * 
 * @author Timo Michelsen
 *
 */
public class StandardPossibleExecutionGenerator implements IPossibleExecutionGenerator {

	private static Logger LOGGER = LoggerFactory.getLogger(StandardPossibleExecutionGenerator.class);

	@Override
	public List<IPossibleExecution> getPossibleExecutions(IAdmissionControl ac, Map<IPhysicalQuery, ICost> queryCosts, ICost maxCost) {

		List<IPossibleExecution> poss = new ArrayList<IPossibleExecution>();

		// first solution: all queries running!
		ICost costSum = null;
		for (ICost cost : queryCosts.values()) {
			if (costSum == null) {
				costSum = cost;
			} else {
				costSum = costSum.merge(cost);
			}
		}

		int cmp = costSum.compareTo(maxCost);
		if (cmp == -1 || cmp == 0) {
			poss.add(new StandardPossibleExecution(queryCosts.keySet(), new ArrayList<IPhysicalQuery>(), costSum));
			LOGGER.debug("Possible Execution: execute all queries");
		}
		
		// only one query?
		StandardAC stdAC = (StandardAC)ac;
		ICostModel cm = stdAC.getSelectedCostModelInstance();
		if( queryCosts.size() == 1 ) {
			// nur eine Anfrage, die gestoppt werden sollte
			poss.add(new StandardPossibleExecution(new ArrayList<IPhysicalQuery>(), queryCosts.keySet(), cm.getZeroCost()));
		}

		if( poss.isEmpty() ) {
    		// generate exactly one possible execution
    		// brute-force too load-heavy
    		List<IPhysicalQuery> runningQueries = new ArrayList<IPhysicalQuery>();
    		List<IPhysicalQuery> stoppingQueries = new ArrayList<IPhysicalQuery>();
    		ICost actSum = cm.getZeroCost();
    		for( IPhysicalQuery query : queryCosts.keySet() ) {
    			ICost cost = queryCosts.get(query);
    			ICost newSum = actSum.merge(cost);
    			if( newSum.compareTo(maxCost) < 0 ) {
    				runningQueries.add(query);
    				actSum = newSum;
    			} else {
    				stoppingQueries.add(query);
    			}
    		}
    		
    		poss.add(new StandardPossibleExecution(runningQueries, stoppingQueries, actSum));
		}
		
        return poss;
				
		// TOOO HEAVY:
//		// generate all combinations
//		List<IQuery> queries = new ArrayList<IQuery>(queryCosts.keySet());
//		List<List<IQuery>> solutions = powerset(queries);
//
//		// test combinations
//		// one combination contains
//		// only the quries which must be started
//		for (List<IQuery> solution : solutions) {
//
//			// this solution was calculated above and failed..
//			// do not try it again
//			if (solution.size() == 0 || solution.size() == queryCosts.size())
//				continue;
//
//			// get summed cost of solution
//			ICost solutionCost = null;
//			for (IQuery solutionQuery : solution) {
//				if (solutionCost == null)
//					solutionCost = queryCosts.get(solutionQuery);
//				else
//					solutionCost = solutionCost.merge(queryCosts.get(solutionQuery));
//			}
//
//			cmp = solutionCost.compareTo(maxCost);
//			if (cmp == -1 || cmp == 0) {
//
//				// solution is ok...
//				// get queries who must be stopped
//				List<IQuery> stoppedQueries = new ArrayList<IQuery>();
//				for (IQuery query : queryCosts.keySet())
//					if (!solution.contains(query))
//						stoppedQueries.add(query);
//
//				// generate solution
//				poss.add(new StandardPossibleExecution(solution, stoppedQueries, solutionCost));
//				getLogger().debug("Possible Execution: " + solution);
//				return poss;
//			}
//
//		}
	}

//	private static List<List<IQuery>> powerset(List<IQuery> queries) {
//
//		List<List<IQuery>> power = new ArrayList<List<IQuery>>();
//
//		int elements = queries.size();
//		int powerElements = (int) Math.pow(2, elements);
//
//		for (int i = 0; i < powerElements; i++) {
//			String binary = intToBinary(i, elements);
//
//			// create a new set
//			List<IQuery> queryList = new ArrayList<IQuery>();
//
//			// convert each digit in the current binary number to the
//			// corresponding element
//			// in the given set
//			for (int j = 0; j < binary.length(); j++) {
//				if (binary.charAt(j) == '1')
//					queryList.add(queries.get(j));
//			}
//
//			// add the new set to the power set
//			power.add(queryList);
//
//		}
//
//		return power;
//	}

//	private static String intToBinary(int binary, int digits) {
//
//		String temp = Integer.toBinaryString(binary);
//		int foundDigits = temp.length();
//		String returner = temp;
//		for (int i = foundDigits; i < digits; i++) {
//			returner = "0" + returner;
//		}
//
//		return returner;
//	}

}
