/* Generated By:JJTree: Do not edit this line. ASTExists.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperator;

public class ASTExists extends AbstractQuantificationPredicate {

	public ASTExists(int id) {
		super(id);
	}

	public ASTExists(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public ASTComplexSelectStatement getQuery() {
		return (ASTComplexSelectStatement) jjtGetChild(2);
	}

	public SDFCompareOperator getCompareOperator() {
		return null;
	}

	public ASTTuple getTuple() {
		return null;
	}

}
