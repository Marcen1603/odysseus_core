/* Generated By:JJTree: Do not edit this line. ASTDatabaseTableOptions.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
@SuppressWarnings("all")
class ASTDatabaseTableOptions extends SimpleNode {
  public ASTDatabaseTableOptions(int id) {
    super(id);
  }

  public ASTDatabaseTableOptions(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=bcabe83b36bd0b0535584b97f86a4668 (do not edit this line) */
