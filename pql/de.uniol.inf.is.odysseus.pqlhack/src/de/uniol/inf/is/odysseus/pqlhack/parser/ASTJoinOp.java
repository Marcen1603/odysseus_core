/* Generated By:JJTree: Do not edit this line. ASTJoinOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTJoinOp extends SimpleNode {
  public ASTJoinOp(int id) {
    super(id);
  }

  public ASTJoinOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
	private int windowSize;
	  
	  public void setWindowSize(String windowSizeString){
		  this.windowSize = Integer.parseInt(windowSizeString);
	  }
	  
	  public int getWindowSize(){
		  return this.windowSize;
	  }
}
/* JavaCC - OriginalChecksum=3c72479b78c733f01ba5247fd326d72a (do not edit this line) */
