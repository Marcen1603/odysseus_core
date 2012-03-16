/* Generated By:JJTree: Do not edit this line. ASTTriplesBlock.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

import java.util.List;

import de.uniol.inf.is.odysseus.sparql.parser.helper.Triple;

public
class ASTTriplesBlock extends SimpleNode {
  
	/**
	 * contains a list of triples. Each list
	 * contains triples with the same subject.
	 */
	private List<List<Triple>> triples;
	
	
	public ASTTriplesBlock(int id) {
    super(id);
  }

  public ASTTriplesBlock(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public void setTriples(List<List<Triple>> triples){
	  this.triples = triples;
  }
  
  public List<List<Triple>> getTriples(){
	  return this.triples;
  }
}
/* JavaCC - OriginalChecksum=f383de7beb77fe058c81ed577523b991 (do not edit this line) */
