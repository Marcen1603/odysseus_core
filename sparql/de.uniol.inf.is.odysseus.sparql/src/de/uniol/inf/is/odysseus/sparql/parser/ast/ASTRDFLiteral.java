/* Generated By:JJTree: Do not edit this line. ASTRDFLiteral.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTRDFLiteral extends SimpleNode {
  public ASTRDFLiteral(int id) {
    super(id);
  }

  public ASTRDFLiteral(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=889635195399065cbba4cdfa9d75b737 (do not edit this line) */
