/* Generated By:JJTree: Do not edit this line. ASTDistanceObjectSelectorOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTDistanceObjectSelectorOp extends SimpleNode {
  public ASTDistanceObjectSelectorOp(int id) {
    super(id);
  }

  public ASTDistanceObjectSelectorOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9ec4853bf16808fc0db69d82e89b613f (do not edit this line) */
