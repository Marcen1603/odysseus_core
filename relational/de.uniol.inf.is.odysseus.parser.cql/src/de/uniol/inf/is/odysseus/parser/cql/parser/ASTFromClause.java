/* Generated By:JJTree: Do not edit this line. ASTFromClause.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTFromClause extends SimpleNode {
  public ASTFromClause(int id) {
    super(id);
  }

  public ASTFromClause(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
