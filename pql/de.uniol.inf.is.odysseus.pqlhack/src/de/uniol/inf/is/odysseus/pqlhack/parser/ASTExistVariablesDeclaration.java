/* Generated By:JJTree: Do not edit this line. ASTExistVariablesDeclaration.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTExistVariablesDeclaration extends SimpleNode {
  public ASTExistVariablesDeclaration(int id) {
    super(id);
  }

  public ASTExistVariablesDeclaration(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=beb6f1c0d41afab0c2f0840156692e3e (do not edit this line) */
