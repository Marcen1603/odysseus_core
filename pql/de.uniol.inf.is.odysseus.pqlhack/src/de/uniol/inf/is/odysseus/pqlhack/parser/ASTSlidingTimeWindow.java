/* Generated By:JJTree: Do not edit this line. ASTSlidingTimeWindow.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

@SuppressWarnings("all")
public class ASTSlidingTimeWindow extends SimpleNode {
  public ASTSlidingTimeWindow(int id) {
    super(id);
  }

  public ASTSlidingTimeWindow(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9396852912ea07d44daa086d4edf43f3 (do not edit this line) */
