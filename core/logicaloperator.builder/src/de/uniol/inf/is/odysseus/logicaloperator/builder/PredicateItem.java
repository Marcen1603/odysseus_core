package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.io.Serializable;

public class PredicateItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private String predicateType;
	
	private String predicate;
	
	public PredicateItem(String predicateType, String predicate) {
		this.predicateType = predicateType.toUpperCase();
		this.predicate = predicate;
	}
	
	public String getPredicate() {
		return predicate;
	}
	
	public String getPredicateType() {
		return predicateType;
	}
}
