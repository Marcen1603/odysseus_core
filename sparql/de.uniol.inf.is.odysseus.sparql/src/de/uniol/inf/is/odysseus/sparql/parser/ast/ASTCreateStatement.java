/* Generated By:JJTree: Do not edit this line. ASTCreateStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTCreateStatement extends SimpleNode {
  
	String streamName;
	boolean isPersistent;
	
	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
	public void setPersistent(boolean b){
		this.isPersistent = b;
	}
	
	public boolean isPersistent(){
		return this.isPersistent;
	}

	public ASTCreateStatement(int id) {
    super(id);
  }

  public ASTCreateStatement(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5ee99815765f1da0a84d906a17cd049f (do not edit this line) */
