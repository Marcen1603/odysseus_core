/* Generated By:JJTree: Do not edit this line. ASTBrokerSource.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
@SuppressWarnings("all")
class ASTBrokerSource extends SimpleNode {
  public ASTBrokerSource(int id) {
    super(id);
  }

  public ASTBrokerSource(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7b9cc604275713ab87fb23ea47875e1e (do not edit this line) */
