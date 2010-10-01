/* Generated By:JJTree: Do not edit this line. ASTWindow.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.logicaloperator.WindowType;

public abstract class ASTWindow extends SimpleNode {
	public static enum Type {
		TIME, TUPLE
	}

	protected Type type;
	private Long size;
	private Long advance;

	public ASTWindow(int id) {
		super(id);
	}

	public ASTWindow(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public abstract boolean isUnbounded();

	public abstract boolean isPartitioned();

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	

	public void setAdvance(Long advance) {
		if (advance == 1) {
			this.advance = null;
		} else {
			this.advance = advance;
		}
	}
	
	public Long getAdvance() {
		return this.advance;
	}
	
	public Long getSlide() {
		return null;
	}

	public abstract WindowType getType();

	public boolean hasOffset() {
		return false;
	}

	public boolean hasTimeout() {
		return false;
	}

	public long getOffset() {
		return 0;
	}

	public long getTimeout() {
		return 0;
	}
	
	public abstract ASTPartition getPartition();

}
