/* Generated By:JJTree: Do not edit this line. ASTPredictionOp.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

@SuppressWarnings("all")
public class ASTPredictionOp extends SimpleNode {
  public ASTPredictionOp(int id) {
    super(id);
  }

  public ASTPredictionOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3e89f897e500ea9945d63dacbb93ace3 (do not edit this line) */
