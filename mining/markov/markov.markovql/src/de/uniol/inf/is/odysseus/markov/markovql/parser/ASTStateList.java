/* Generated By:JJTree: Do not edit this line. ASTStateList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.markov.markovql.parser;

public
class ASTStateList extends SimpleNode {
  public ASTStateList(int id) {
    super(id);
  }

  public ASTStateList(MarkovQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MarkovQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d1b78faeaf41029b6e5336320ae039ba (do not edit this line) */
