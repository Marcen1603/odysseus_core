/* Generated By:JavaCC: Do not edit this line. MEPImplVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.mep.impl;

public interface MEPImplVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTFunction node, Object data);
  public Object visit(ASTConstant node, Object data);
  public Object visit(ASTVariable node, Object data);
  public Object visit(ASTArray node, Object data);
  public Object visit(ASTMatrix node, Object data);
  public Object visit(ASTMatrixLine node, Object data);
}
/* JavaCC - OriginalChecksum=182094a066dc4627d2494ca6d1c9f415 (do not edit this line) */
