/* Generated By:JavaCC: Do not edit this line. NewSQLParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.parser.cql.parser;

@SuppressWarnings("all")
public interface NewSQLParserVisitor
{
  public Object visit(SimpleNode node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTPriorizedStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTStreamToStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTComplexSelectStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSelectStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateType node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateContextStore node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTContextStoreType node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTLoginPassword node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateSinkStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSocketSink node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDatabaseSink node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDatabaseSinkOptions node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTFileSink node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateDatabaseConnection node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateFromDatabase node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDatabaseTimeSensitiv node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateViewStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateSensor node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTORSchemaDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTRecordDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTRecordEntryDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTListDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAttrDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAttributeDefinitions node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAttributeDefinition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTimedTuples node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTimedTuple node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTimeInterval node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSocket node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSilab node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTChannel node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAutoReconnect node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTHost node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTFileSource node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSimpleTuple node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAttributeType node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTMVCovarianceRow node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCovarianceRow node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDateFormat node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSetOperator node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSelectClause node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTProjectionMatrix node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTProjectionVector node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTFromClause node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTWhereClause node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTGroupByClause node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTHavingClause node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSelectAll node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTRenamedExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAS node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTMatrixExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSimplePredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBasicPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTProbabilityPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTPriority node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTOrPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAndPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTNotPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTQuantificationPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAnyPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAllPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTInPredicate node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTExists node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTElementPriorities node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTElementPriority node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDefaultPriority node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTuple node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTime node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTTupleSet node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTQuantificationOperator node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSimpleToken node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTFunctionExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTFunctionName node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAggregateExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAggregateFunction node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDistinctExpression node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSimpleSource node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSubselect node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTStreamSQLWindow node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTPartition node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAdvance node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlide node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTIdentifier node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTInteger node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTNumber node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTString node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCompareOperator node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSpatialCompareOperator node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateBroker node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBrokerSource node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBrokerAsSource node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBrokerSelectInto node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBrokerSimpleSource node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTBrokerQueue node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTMetric node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateUserStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTAlterUserStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDropUserStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateSLAStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlaMetricDef node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlaScopeDef node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlaWindowDef node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlaServiceLevelDef node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTSlaPenaltyDef node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDropStreamStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDropViewStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTCreateRoleStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTIfExists node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTDropRoleStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTGrantRoleStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTRevokeRoleStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTGrantStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTIdentifierList node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
  public Object visit(ASTRevokeStatement node, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
}
/* JavaCC - OriginalChecksum=acf2b3d30fe1f5d2ca90336dac8dc149 (do not edit this line) */
