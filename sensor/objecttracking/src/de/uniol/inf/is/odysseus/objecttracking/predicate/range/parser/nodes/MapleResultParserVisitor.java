/* Generated By:JavaCC: Do not edit this line. MapleResultParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes;

public interface MapleResultParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTMaple node, Object data);
  public Object visit(ASTMaplePiecewise node, Object data);
  public Object visit(ASTConditionSolution node, Object data);
  public Object visit(ASTCondition node, Object data);
  public Object visit(ASTComplexCondition node, Object data);
  public Object visit(ASTSimpleCondition node, Object data);
  public Object visit(ASTCompareOperator node, Object data);
  public Object visit(ASTSolution node, Object data);
  public Object visit(ASTSimpleSolution node, Object data);
  public Object visit(ASTSimpleSolutionContent node, Object data);
  public Object visit(ASTFullSolution node, Object data);
  public Object visit(ASTEmptySolution node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTSimpleToken node, Object data);
  public Object visit(ASTFunctionExpression node, Object data);
  public Object visit(ASTFunctionName node, Object data);
  public Object visit(ASTNumber node, Object data);
  public Object visit(ASTString node, Object data);
  public Object visit(ASTIdentifier node, Object data);
}
/* JavaCC - OriginalChecksum=30363d4168b6d56d845f7ea60e5c8371 (do not edit this line) */
