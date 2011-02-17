/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFComplexPredicateFactory {

	private static HashMap<String, SDFComplexPredicate> predicateCache = new HashMap<String, SDFComplexPredicate>();

	protected SDFComplexPredicateFactory() {
	}

	public static SDFComplexPredicate createComplexPredicate(String URI,
			String typeURI) {
		SDFComplexPredicate cPred = (SDFComplexPredicate) predicateCache
				.get(URI);
		if (cPred == null) {
			cPred = createPredicate(URI, typeURI);
            if (cPred != null) {
                predicateCache.put(URI, cPred);
            }
        }
		return cPred;
	}

    /**
     * @param URI
     * @param typeURI
     * @param cPred
     * @return
     */
    private static SDFComplexPredicate createPredicate(String URI, String typeURI) {
        SDFComplexPredicate cPred = null;
        while (true) {
        	if (typeURI.equals(SDFPredicates.AndPredicate)) {
        		cPred = new SDFComplexPredicate(URI);
        		// Das gefaellt mir aber nicht so gut ...
        		cPred.setOp(SDFLogicalOperatorFactory.getOperator(SDFPredicates.And));
        		break;
        	}
        	if (typeURI.equals(SDFPredicates.OrPredicate)) {
        		cPred = new SDFComplexPredicate(URI);
        		// Das gefaellt mir aber nicht so gut ...
        		cPred.setOp(SDFLogicalOperatorFactory.getOperator(SDFPredicates.Or));
        		break;
        	}
        	System.err.println("Falscher Praedikattyp " + typeURI);
        	break;
        }
        return cPred;
    }
    
    public static SDFComplexPredicate combinePredicates(String operatorURI, SDFPredicate leftPredicate, SDFPredicate rightPredicate){
        SDFComplexPredicate pred = createPredicate("tmp"+System.currentTimeMillis(), operatorURI);
        pred.setLeft(leftPredicate);
        pred.setRight(rightPredicate);
        return pred;
        
    }
    
}