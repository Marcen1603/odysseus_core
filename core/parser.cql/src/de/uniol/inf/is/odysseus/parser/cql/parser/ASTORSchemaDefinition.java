/* Generated By:JJTree: Do not edit this line. ASTORSchemaDefinition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
@SuppressWarnings("all")
class ASTORSchemaDefinition extends SimpleNode {
  public ASTORSchemaDefinition(int id) {
    super(id);
  }

  public ASTORSchemaDefinition(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8fae4cc0ba1c01dda5c6e3825484048d (do not edit this line) */
