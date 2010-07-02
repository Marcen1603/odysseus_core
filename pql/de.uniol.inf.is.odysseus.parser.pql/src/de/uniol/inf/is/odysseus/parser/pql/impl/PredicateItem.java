package de.uniol.inf.is.odysseus.parser.pql.impl;

public class PredicateItem {
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
