/* Generated By:JJTree: Do not edit this line. ASTTime.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
@SuppressWarnings("all")
class ASTTime extends SimpleNode {
  public ASTTime(int id) {
    super(id);
  }

  public ASTTime(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3d2c74c580a6db4ee4c3a4a812d65ba7 (do not edit this line) */
