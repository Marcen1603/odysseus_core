/* Generated By:JJTree: Do not edit this line. ASTSlaServiceLevelDef.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTSlaServiceLevelDef extends SimpleNode {
  public ASTSlaServiceLevelDef(int id) {
    super(id);
  }

  public ASTSlaServiceLevelDef(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=abef9f5d89ebce92e52acda217bf2a48 (do not edit this line) */
