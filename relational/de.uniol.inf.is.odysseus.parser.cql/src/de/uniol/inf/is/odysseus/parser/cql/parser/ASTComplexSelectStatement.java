/* Generated By:JJTree: Do not edit this line. ASTComplexSelectStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTComplexSelectStatement extends SimpleNode {
  public ASTComplexSelectStatement(int id) {
    super(id);
  }

  public ASTComplexSelectStatement(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=788dc5b3bfafa919f0c8ffbe0878bac2 (do not edit this line) */
