/* Generated By:JJTree: Do not edit this line. ASTSimpleToken.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;


@SuppressWarnings("all")
public class ASTSimpleToken extends SimpleNode {
  public ASTSimpleToken(int id) {
    super(id);
  }

  public ASTSimpleToken(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
	@Override
	public String toString() {
		Node node = jjtGetChild(0);
		if (node instanceof ASTExpression) {
			return "(" + node.toString() + ")";
		}
		
		return node.toString();
	}
}
/* JavaCC - OriginalChecksum=1c67faa4e769c6fd4fc0ff62dd587bf8 (do not edit this line) */
