/* Generated By:JJTree: Do not edit this line. ASTRange.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTRange extends SimpleNode {
  public ASTRange(int id) {
    super(id);
  }

  public ASTRange(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
