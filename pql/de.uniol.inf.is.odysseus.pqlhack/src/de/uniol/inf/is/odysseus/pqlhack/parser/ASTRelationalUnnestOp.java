/* Generated By:JJTree: Do not edit this line. ASTRelationalUnnestOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTRelationalUnnestOp extends SimpleNode {
  public ASTRelationalUnnestOp(int id) {
    super(id);
  }

  public ASTRelationalUnnestOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0d1e9bf7bb948a173bf9fc99a89c795b (do not edit this line) */
