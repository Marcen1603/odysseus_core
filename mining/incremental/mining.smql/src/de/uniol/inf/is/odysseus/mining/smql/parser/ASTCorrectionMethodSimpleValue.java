/* Generated By:JJTree: Do not edit this line. ASTCorrectionMethodSimpleValue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
@SuppressWarnings("all")
class ASTCorrectionMethodSimpleValue extends SimpleNode {
  public ASTCorrectionMethodSimpleValue(int id) {
    super(id);
  }

  public ASTCorrectionMethodSimpleValue(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=59bcad211df64b09af2a86f3360eefb9 (do not edit this line) */
