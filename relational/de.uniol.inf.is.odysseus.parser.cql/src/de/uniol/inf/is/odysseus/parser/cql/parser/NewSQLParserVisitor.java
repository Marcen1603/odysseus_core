/* Generated By:JavaCC: Do not edit this line. NewSQLParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public interface NewSQLParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTStatement node, Object data);
  public Object visit(ASTDBExecuteStatement node, Object data);
  public Object visit(ASTDatabase node, Object data);
  public Object visit(ASTDBSelectStatement node, Object data);
  public Object visit(ASTDatabaseOptions node, Object data);
  public Object visit(ASTAS node, Object data);
  public Object visit(ASTSQL node, Object data);
  public Object visit(ASTPriorizedStatement node, Object data);
  public Object visit(ASTComplexSelectStatement node, Object data);
  public Object visit(ASTSelectStatement node, Object data);
  public Object visit(ASTCreateStatement node, Object data);
  public Object visit(ASTCreateViewStatement node, Object data);
  public Object visit(ASTCreateSensor node, Object data);
  public Object visit(ASTORSchemaDefinition node, Object data);
  public Object visit(ASTRecordDefinition node, Object data);
  public Object visit(ASTRecordEntryDefinition node, Object data);
  public Object visit(ASTListDefinition node, Object data);
  public Object visit(ASTAttrDefinition node, Object data);
  public Object visit(ASTAttributeDefinitions node, Object data);
  public Object visit(ASTAttributeDefinition node, Object data);
  public Object visit(ASTTimedTuples node, Object data);
  public Object visit(ASTTimedTuple node, Object data);
  public Object visit(ASTTimeInterval node, Object data);
  public Object visit(ASTSocket node, Object data);
  public Object visit(ASTSilab node, Object data);
  public Object visit(ASTChannel node, Object data);
  public Object visit(ASTHost node, Object data);
  public Object visit(ASTCSVSource node, Object data);
  public Object visit(ASTSimpleTuple node, Object data);
  public Object visit(ASTAttributeType node, Object data);
  public Object visit(ASTCovarianceRow node, Object data);
  public Object visit(ASTDateFormat node, Object data);
  public Object visit(ASTSetOperator node, Object data);
  public Object visit(ASTSelectClause node, Object data);
  public Object visit(ASTProjectionMatrix node, Object data);
  public Object visit(ASTProjectionVector node, Object data);
  public Object visit(ASTFromClause node, Object data);
  public Object visit(ASTWhereClause node, Object data);
  public Object visit(ASTGroupByClause node, Object data);
  public Object visit(ASTHavingClause node, Object data);
  public Object visit(ASTSelectAll node, Object data);
  public Object visit(ASTRenamedExpression node, Object data);
  public Object visit(ASTMatrixExpression node, Object data);
  public Object visit(ASTPredicate node, Object data);
  public Object visit(ASTSimplePredicate node, Object data);
  public Object visit(ASTBasicPredicate node, Object data);
  public Object visit(ASTSpatialPredicate node, Object data);
  public Object visit(ASTProbabilityPredicate node, Object data);
  public Object visit(ASTPriority node, Object data);
  public Object visit(ASTOrPredicate node, Object data);
  public Object visit(ASTAndPredicate node, Object data);
  public Object visit(ASTNotPredicate node, Object data);
  public Object visit(ASTQuantificationPredicate node, Object data);
  public Object visit(ASTAnyPredicate node, Object data);
  public Object visit(ASTAllPredicate node, Object data);
  public Object visit(ASTInPredicate node, Object data);
  public Object visit(ASTExists node, Object data);
  public Object visit(ASTElementPriorities node, Object data);
  public Object visit(ASTElementPriority node, Object data);
  public Object visit(ASTDefaultPriority node, Object data);
  public Object visit(ASTTuple node, Object data);
  public Object visit(ASTTupleSet node, Object data);
  public Object visit(ASTQuantificationOperator node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTSimpleToken node, Object data);
  public Object visit(ASTFunctionExpression node, Object data);
  public Object visit(ASTFunctionName node, Object data);
  public Object visit(ASTAggregateExpression node, Object data);
  public Object visit(ASTAggregateFunction node, Object data);
  public Object visit(ASTDistinctExpression node, Object data);
  public Object visit(ASTSimpleSource node, Object data);
  public Object visit(ASTSubselect node, Object data);
  public Object visit(ASTOldWindow node, Object data);
  public Object visit(ASTStreamSQLWindow node, Object data);
  public Object visit(ASTPartition node, Object data);
  public Object visit(ASTAdvance node, Object data);
  public Object visit(ASTSlide node, Object data);
  public Object visit(ASTIdentifier node, Object data);
  public Object visit(ASTInteger node, Object data);
  public Object visit(ASTNumber node, Object data);
  public Object visit(ASTString node, Object data);
  public Object visit(ASTCompareOperator node, Object data);
  public Object visit(ASTSpatialCompareOperator node, Object data);
  public Object visit(ASTOSGI node, Object data);
  public Object visit(ASTCreateBroker node, Object data);
  public Object visit(ASTBrokerSource node, Object data);
  public Object visit(ASTBrokerAsSource node, Object data);
  public Object visit(ASTBrokerSelectInto node, Object data);
  public Object visit(ASTBrokerSimpleSource node, Object data);
  public Object visit(ASTBrokerQueue node, Object data);
  public Object visit(ASTMetric node, Object data);
}
/* JavaCC - OriginalChecksum=33c2dfba531d854d307a1d404debf9d2 (do not edit this line) */
