/* Generated By:JJTree: Do not edit this line. ASTString.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

@SuppressWarnings("all")
public class ASTString extends SimpleNode {
	
	private String value;
	
  public ASTString(int id) {
    super(id);
  }

  public ASTString(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public void setValue(String value){
	  this.value = value;
  }
  
  public String getValue(){
	  return this.value;
  }
  
  public String toString(){
	  return this.value;
  }
}
/* JavaCC - OriginalChecksum=dfa741e39dd734d285a1cad5c4bd9def (do not edit this line) */
