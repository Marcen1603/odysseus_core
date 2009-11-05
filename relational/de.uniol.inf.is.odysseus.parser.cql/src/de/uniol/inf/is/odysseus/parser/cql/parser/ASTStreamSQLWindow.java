/* Generated By:JJTree: Do not edit this line. ASTStreamSQLWindow.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;

public class ASTStreamSQLWindow extends ASTWindow {
	private Long offset;
	private Long timeout;

	public ASTStreamSQLWindow(int id) {
		super(id);
	}

	public ASTStreamSQLWindow(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public ASTPartition getPartition() {
		return (ASTPartition) jjtGetChild(this.type == Type.ON ? 3 : 2);

	}

	@Override
	public WindowType getType() {
		if (this.type == Type.TUPLE) {
			if (getAdvance() == null || getAdvance() == 1) {
				return WindowType.SLIDING_TUPLE_WINDOW;
			} else {
				return WindowType.JUMPING_TUPLE_WINDOW;
			}
		} else {
			if (getAdvance() == null || getAdvance() == 1) {
				return WindowType.SLIDING_TIME_WINDOW;
			} else {
				return WindowType.JUMPING_TIME_WINDOW;
			}
		}
	}

	@Override
	public long getOffset() {
		return this.offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	@Override
	public boolean hasOffset() {
		return this.offset != null;
	}

	@Override
	public ASTIdentifier getOn() {
		if (this.type == Type.ON) {
			return (ASTIdentifier) jjtGetChild(2);
		}
		return null;
	}

	@Override
	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean hasTimeout() {
		return this.timeout != null;
	}

	@Override
	public boolean isPartitioned() {
		int pos = this.type == Type.ON ? 3 : 2;
		if (jjtGetNumChildren() > pos) {
			return jjtGetChild(pos) instanceof ASTPartition;
		}
		return false;
	}

	@Override
	public boolean isUnbounded() {
		return false;
	}
}
