/* Generated By:JJTree: Do not edit this line. ASTCovarianceRow.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTCovarianceRow extends SimpleNode {
  public ASTCovarianceRow(int id) {
    super(id);
  }

  public ASTCovarianceRow(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c8ad8402b69b71b9fa8c2737fad22153 (do not edit this line) */
