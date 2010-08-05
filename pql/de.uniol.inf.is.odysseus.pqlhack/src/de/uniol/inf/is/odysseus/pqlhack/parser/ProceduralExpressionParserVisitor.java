/* Generated By:JavaCC: Do not edit this line. ProceduralExpressionParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public interface ProceduralExpressionParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTLogicalPlan node, Object data);
  public Object visit(ASTAlgebraOp node, Object data);
  public Object visit(ASTTestOp node, Object data);
  public Object visit(ASTProjectionOp node, Object data);
  public Object visit(ASTProjectionIdentifier node, Object data);
  public Object visit(ASTRelationalProjectionOp node, Object data);
  public Object visit(ASTSelectionOp node, Object data);
  public Object visit(ASTRelationalSelectionOp node, Object data);
  public Object visit(ASTRelationalJoinOp node, Object data);
  public Object visit(ASTRelationalNestOp node, Object data);
  public Object visit(ASTRelationalUnnestOp node, Object data);
  public Object visit(ASTJoinOp node, Object data);
  public Object visit(ASTWindowOp node, Object data);
  public Object visit(ASTSlidingTimeWindow node, Object data);
  public Object visit(ASTAccessOp node, Object data);
  public Object visit(ASTBrokerOp node, Object data);
  public Object visit(ASTPredictionAssignOp node, Object data);
  public Object visit(ASTPredictionAssignOrOp node, Object data);
  public Object visit(ASTPredictionOp node, Object data);
  public Object visit(ASTPredicate node, Object data);
  public Object visit(ASTSimplePredicate node, Object data);
  public Object visit(ASTBasicPredicate node, Object data);
  public Object visit(ASTOrPredicate node, Object data);
  public Object visit(ASTAndPredicate node, Object data);
  public Object visit(ASTNotPredicate node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTSimpleToken node, Object data);
  public Object visit(ASTFunctionExpression node, Object data);
  public Object visit(ASTFunctionName node, Object data);
  public Object visit(ASTNumber node, Object data);
  public Object visit(ASTString node, Object data);
  public Object visit(ASTIdentifier node, Object data);
  public Object visit(ASTCompareOperator node, Object data);
  public Object visit(ASTPredictionDefinition node, Object data);
  public Object visit(ASTDefaultPredictionDefinition node, Object data);
  public Object visit(ASTPredictionFunctionDefinition node, Object data);
  public Object visit(ASTKeyValueList node, Object data);
  public Object visit(ASTKeyValuePair node, Object data);
  public Object visit(ASTAssociationGenOp node, Object data);
  public Object visit(ASTAssociationEvalOp node, Object data);
  public Object visit(ASTAssociationSelOp node, Object data);
  public Object visit(ASTAssociationSrcOp node, Object data);
}
/* JavaCC - OriginalChecksum=db453350117533bb08d41eb5faa53865 (do not edit this line) */
