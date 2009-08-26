package de.uniol.inf.is.odysseus.sourcedescription.sdf.query;

import java.util.ArrayList;

public class QueryQualityPredicateSet {

    /**
	 * @uml.property  name="predicates"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.query.QueryQualityPredicate"
	 */
    private ArrayList<QueryQualityPredicate> predicates;

	public QueryQualityPredicateSet() {
		predicates = new ArrayList<QueryQualityPredicate>();
	}

	public void addPredicate(QueryQualityPredicate predicate) {
		predicates.add(predicate);
	}

	public QueryQualityPredicate getPredicate(int pos) {
		return (QueryQualityPredicate) predicates.get(pos);
	}

	public int getPredicateCount() {
		return predicates.size();
	}

}