/* Generated By:JJTree: Do not edit this line. ASTIRIrefOrFunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTIRIrefOrFunction extends SimpleNode {
  private String str;

  
  public void setString(String s){
	  this.str = s;
  }
  
  @Override
public String toString(){
	  return this.str;
  }
  
	public ASTIRIrefOrFunction(int id) {
    super(id);
  }

  public ASTIRIrefOrFunction(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ea12cef9242808eea11c4832e582a847 (do not edit this line) */
