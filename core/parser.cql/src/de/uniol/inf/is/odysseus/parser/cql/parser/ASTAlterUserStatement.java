/* Generated By:JJTree: Do not edit this line. ASTAlterUserStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTAlterUserStatement extends SimpleNode {

	String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		// Anfuehrungszeichen entfernen
		this.password = password.substring(1,password.length()-2);		
	}

	public ASTAlterUserStatement(int id) {
		super(id);
	}

	public ASTAlterUserStatement(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=2ec15774209f220774e385c0113745b2 (do not edit this
 * line)
 */
