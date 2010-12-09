/* Generated By:JJTree: Do not edit this line. ASTTimeInterval.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;

public class ASTTimeInterval extends SimpleNode {
	private ITimeInterval interval;

	public ASTTimeInterval(int id) {
		super(id);
	}

	public ASTTimeInterval(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public ITimeInterval getInterval() {
		return interval;
	}

	public void setInterval(ITimeInterval interval) {
		this.interval = interval;
	}
}
