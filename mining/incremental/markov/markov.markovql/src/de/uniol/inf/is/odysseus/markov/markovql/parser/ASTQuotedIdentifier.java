/* Generated By:JJTree: Do not edit this line. ASTQuotedIdentifier.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.markov.markovql.parser;

public
@SuppressWarnings("all")
class ASTQuotedIdentifier extends SimpleNode {
  public ASTQuotedIdentifier(int id) {
    super(id);
  }

  public ASTQuotedIdentifier(MarkovQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MarkovQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=43680cd4a31dca79d1eab618c4995c86 (do not edit this line) */
