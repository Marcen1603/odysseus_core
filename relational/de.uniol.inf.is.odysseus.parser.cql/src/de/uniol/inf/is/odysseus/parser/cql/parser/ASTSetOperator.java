/* Generated By:JJTree: Do not edit this line. ASTSetOperator.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTSetOperator extends SimpleNode {
	private SetOperation operation;

	public ASTSetOperator(int id) {
		super(id);
	}

	public ASTSetOperator(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public enum SetOperation {
		UNION, MINUS, INTERSECT
	};

	public void setOperation(SetOperation operation) {
		this.operation = operation;
	}
	
	public SetOperation getOperation() {
		return this.operation;
	}
}
