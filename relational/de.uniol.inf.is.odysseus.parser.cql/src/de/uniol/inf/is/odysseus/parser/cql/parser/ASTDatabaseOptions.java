/* Generated By:JJTree: Do not edit this line. ASTDatabaseOptions.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTDatabaseOptions extends SimpleNode {
  public ASTDatabaseOptions(int id) {
    super(id);
  }

  public ASTDatabaseOptions(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=04a325e5f3b92ceded7ba00fe781a9c3 (do not edit this line) */
