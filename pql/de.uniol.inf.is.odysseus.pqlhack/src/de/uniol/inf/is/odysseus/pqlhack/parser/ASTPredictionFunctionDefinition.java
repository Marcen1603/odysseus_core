/* Generated By:JJTree: Do not edit this line. ASTPredictionFunctionDefinition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTPredictionFunctionDefinition extends SimpleNode {
  public ASTPredictionFunctionDefinition(int id) {
    super(id);
  }

  public ASTPredictionFunctionDefinition(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=676cfaf26dab45c352add80cdfac7158 (do not edit this line) */
