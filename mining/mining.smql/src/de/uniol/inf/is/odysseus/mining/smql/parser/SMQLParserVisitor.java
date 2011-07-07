/* Generated By:JavaCC: Do not edit this line. SMQLParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public interface SMQLParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTStart node, Object data);
  public Object visit(ASTIdentifier node, Object data);
  public Object visit(ASTInteger node, Object data);
  public Object visit(ASTFloat node, Object data);
  public Object visit(ASTNumber node, Object data);
  public Object visit(ASTCreateKnowledgeDiscoveryProcess node, Object data);
  public Object visit(ASTProcessPhases node, Object data);
  public Object visit(ASTCleanPhase node, Object data);
  public Object visit(ASTOutlierDetections node, Object data);
  public Object visit(ASTOutlierDetection node, Object data);
  public Object visit(ASTDetectionMethod node, Object data);
  public Object visit(ASTDetectionMethodStateless node, Object data);
  public Object visit(ASTDetectionMethodStateful node, Object data);
  public Object visit(ASTDetectionMethodOutOfRange node, Object data);
  public Object visit(ASTDetectionMethodSimpleValue node, Object data);
  public Object visit(ASTDetectionMethodSigmaRule node, Object data);
  public Object visit(ASTDetectionMethodSimplePredicate node, Object data);
  public Object visit(ASTDetectionMethodFunction node, Object data);
  public Object visit(ASTDetectionMethodOutOfDomain node, Object data);
  public Object visit(ASTParameterList node, Object data);
  public Object visit(ASTParameter node, Object data);
  public Object visit(ASTCorrectionMethod node, Object data);
  public Object visit(ASTCorrectionMethodDiscard node, Object data);
  public Object visit(ASTCorrectionMethodFunction node, Object data);
  public Object visit(ASTStreamSQLWindow node, Object data);
  public Object visit(ASTPercent node, Object data);
}
/* JavaCC - OriginalChecksum=9bbdbeb1fe9889648de5ff31b3359d23 (do not edit this line) */
