/* Generated By:JJTree: Do not edit this line. ASTExpression.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTExpression extends SimpleNode {

	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public ASTExpression(int id) {
		super(id);
	}

	public ASTExpression(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		if (jjtGetNumChildren() == 1) {
			return jjtGetChild(0).toString();
		} else {
			return jjtGetChild(0).toString() + " " + getOperator() + " "
					+ jjtGetChild(1).toString();
		}
	}
}
