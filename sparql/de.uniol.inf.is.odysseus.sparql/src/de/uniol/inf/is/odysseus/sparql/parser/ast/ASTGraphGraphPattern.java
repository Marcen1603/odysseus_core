/* Generated By:JJTree: Do not edit this line. ASTGraphGraphPattern.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTGraphGraphPattern extends SimpleNode {
  public ASTGraphGraphPattern(int id) {
    super(id);
  }

  public ASTGraphGraphPattern(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=64d5aac1f5166c505f49b95b14e746d9 (do not edit this line) */
