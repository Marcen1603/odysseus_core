/* Generated By:JJTree: Do not edit this line. ASTBrokerOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTBrokerOp extends SimpleNode {
	
	private boolean hasQueue;
	
	private int noOfChildOps;
	
	public ASTBrokerOp(int id) {
    super(id);
  }

  public ASTBrokerOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
//  public String getAlias() {
//	  
//	  // first child = name of broker
//	  // second child = alias or following algebra operator
//		if(jjtGetNumChildren() > 1 && this.jjtGetChild(1) instanceof ASTIdentifier){
//			return ((ASTIdentifier)this.jjtGetChild(1)).getName();
//		}
//		return null;
//	}

//	public boolean hasAlias() {
//		return getAlias() != null;
//	}
	
	public String getName(){
		return ((ASTIdentifier)this.jjtGetChild(0)).getName();
	}
	
	public void setQueue(boolean b){
		this.hasQueue = b;
	}
	
	public boolean hasQueue(){
		return this.hasQueue;
	}
	
	public int getNoOfChildOps(){
		return this.noOfChildOps;
	}
	
	public void increaseNoOfChildOps(){
		this.noOfChildOps++;
	}
}
/* JavaCC - OriginalChecksum=968a071d3599b11d992deb4976887d5d (do not edit this line) */
