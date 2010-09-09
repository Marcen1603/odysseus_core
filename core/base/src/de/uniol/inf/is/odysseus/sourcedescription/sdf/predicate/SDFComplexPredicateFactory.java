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