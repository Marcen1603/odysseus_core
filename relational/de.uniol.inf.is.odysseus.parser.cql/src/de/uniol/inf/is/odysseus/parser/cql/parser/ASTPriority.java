/* Generated By:JJTree: Do not edit this line. ASTPriority.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTPriority extends SimpleNode {
	int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ASTPriority(int id) {
		super(id);
	}

	public ASTPriority(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
