/* Generated By:JavaCC: Do not edit this line. MEPImplVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.mep.impl;

public interface MEPImplVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTFunction node, Object data);
  public Object visit(ASTConstant node, Object data);
  public Object visit(ASTVariable node, Object data);
  public Object visit(ASTMatrix node, Object data);
  public Object visit(ASTMatrixLine node, Object data);
}
/* JavaCC - OriginalChecksum=709d1cd9110aa741326a57c25afcb72c (do not edit this line) */
