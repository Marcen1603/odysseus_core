/* Generated By:JJTree: Do not edit this line. ASTRows.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTRows extends SimpleNode {
  public ASTRows(int id) {
    super(id);
  }

  public ASTRows(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
