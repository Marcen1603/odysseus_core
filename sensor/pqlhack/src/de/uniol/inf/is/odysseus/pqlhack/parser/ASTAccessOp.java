/* Generated By:JJTree: Do not edit this line. ASTAccessOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTAccessOp extends SimpleNode {
  public ASTAccessOp(int id) {
    super(id);
  }

  public ASTAccessOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String getAlias() {
		switch (jjtGetNumChildren()) {
		case 1:
			return null;
		case 2:
			return ((ASTIdentifier)jjtGetChild(1)).getName();
		default:
			return null;
		}
	}

	public boolean hasAlias() {
		return getAlias() != null;
	}
}
/* JavaCC - OriginalChecksum=30c4b16532c44b8c4ea26f31adc7b9d8 (do not edit this line) */
