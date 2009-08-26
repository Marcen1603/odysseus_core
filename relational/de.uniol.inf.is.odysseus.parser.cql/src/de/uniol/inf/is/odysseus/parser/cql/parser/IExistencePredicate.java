package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperator;

public interface IExistencePredicate {

	public ASTTuple getTuple();

	public SDFCompareOperator getCompareOperator();

	public ASTComplexSelectStatement getQuery();

}