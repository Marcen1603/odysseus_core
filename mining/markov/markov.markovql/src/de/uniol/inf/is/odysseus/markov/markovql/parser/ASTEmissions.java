/* Generated By:JJTree: Do not edit this line. ASTEmissions.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.markov.markovql.parser;

public
class ASTEmissions extends SimpleNode {
  public ASTEmissions(int id) {
    super(id);
  }

  public ASTEmissions(MarkovQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MarkovQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3d03e752480f78761240df92a70d625c (do not edit this line) */
