/* Generated By:JJTree: Do not edit this line. ASTConstant.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mep.impl;

public @SuppressWarnings("all")
class ASTConstant extends SimpleNode {
	public ASTConstant(int id) {
		super(id);
	}

	public ASTConstant(MEPImpl p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(MEPImplVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
/*
 * JavaCC - OriginalChecksum=4307b9643a35fae9d404b841509af446 (do not edit this
 * line)
 */
