/* Generated By:JJTree: Do not edit this line. ASTWindowAdvance.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

@SuppressWarnings("all")
public class ASTWindowAdvance extends SimpleNode {
  public ASTWindowAdvance(int id) {
    super(id);
  }

  public ASTWindowAdvance(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=36ec2399c7203579444717a17378b2e4 (do not edit this line) */
