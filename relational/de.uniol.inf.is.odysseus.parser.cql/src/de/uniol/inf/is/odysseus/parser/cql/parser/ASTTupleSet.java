/* Generated By:JJTree: Do not edit this line. ASTTupleSet.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTTupleSet extends SimpleNode {
  public ASTTupleSet(int id) {
    super(id);
  }

  public ASTTupleSet(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
