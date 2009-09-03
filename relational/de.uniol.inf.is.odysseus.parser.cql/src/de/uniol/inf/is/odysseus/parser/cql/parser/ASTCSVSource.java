/* Generated By:JJTree: Do not edit this line. ASTCSVSource.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import java.net.MalformedURLException;
import java.net.URL;

public class ASTCSVSource extends SimpleNode {

	private URL url;

	public ASTCSVSource(int id) {
		super(id);
	}

	public ASTCSVSource(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void setURL(String url) throws ParseException {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			throw new ParseException(e.getMessage());
		}
	}
	
	public URL getUrl() {
		return url;
	}
}
/*
 * JavaCC - OriginalChecksum=d642b8fc157bb5a2b5c1c41cadbd054e (do not edit this
 * line)
 */
