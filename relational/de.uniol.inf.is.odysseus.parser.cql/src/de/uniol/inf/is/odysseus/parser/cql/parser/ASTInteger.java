/* Generated By:JJTree: Do not edit this line. ASTInteger.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTInteger extends SimpleNode {
	private Long value;

	public Long getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = Long.parseLong(value);
	}

	public ASTInteger(int id) {
		super(id);
	}

	public ASTInteger(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
