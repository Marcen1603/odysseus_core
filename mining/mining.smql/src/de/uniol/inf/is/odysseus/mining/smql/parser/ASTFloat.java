/* Generated By:JJTree: Do not edit this line. ASTFloat.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
@SuppressWarnings("all")
class ASTFloat extends SimpleNode {
  public ASTFloat(int id) {
    super(id);
  }

  public ASTFloat(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d48931503dd94205e8b047ad6fd3b6a3 (do not edit this line) */
