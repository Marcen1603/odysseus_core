/* Generated By:JJTree: Do not edit this line. ASTDropStreamStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
@SuppressWarnings("all")
class ASTDropStreamStatement extends SimpleNode {
  public ASTDropStreamStatement(int id) {
    super(id);
  }

  public ASTDropStreamStatement(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7cef4aea23cd329ce15ec60dc574ffae (do not edit this line) */
