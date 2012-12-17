/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import java.io.StringReader;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class NewSQLParserDumpVisitor implements NewSQLParserVisitor {

	private int indent = 0;

	private String indentString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; ++i) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private Object pvisit(SimpleNode node) throws QueryParseException {
		System.out.println(indentString() + node);
		++indent;
		node.childrenAccept(this, null);
		--indent;
		return null;
	}

	@Override
	public Object visit(SimpleNode node, Object data) throws QueryParseException {
		return null;
	}	

	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFromClause node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAggregateFunction node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDistinctExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAS node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	public Object visit(ASTWindow node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	public Object visit(ASTRange node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	public Object visit(ASTRows node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTInteger node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTNumber node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	public static void main(String[] args) {
		String q = "SELECT ContextStore(bla) AS bla, moin FROM A";
		// String q = "SELECT A.b, C.d FROM Q RANGE 10000 ADVANCE 10, C AS Q
		// WHERE c.a > 2 AND c.b < 1 GROUP BY C.d, A.b";
	    new NewSQLParser(new StringReader(q));
		try {
			NewSQLParser.Statement().childrenAccept(
					new NewSQLParserDumpVisitor(), null);			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (QueryParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object visit(ASTSetOperator node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTRenamedExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTString node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAdvance node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlide node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTQuantificationPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAnyPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAllPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTInPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTExists node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTTuple node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTTupleSet node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTQuantificationOperator node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTPartition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTStreamSQLWindow node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTPriorizedStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTComplexSelectStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSelectStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAttributeDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTTimedTuples node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTTimedTuple node, Object data) throws QueryParseException {

		return pvisit(node);
	}

	@Override
	public Object visit(ASTTimeInterval node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSimpleTuple node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAttributeType node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTPriority node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTElementPriority node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDefaultPriority node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSocket node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTHost node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTChannel node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSpatialCompareOperator node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCovarianceRow node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTMatrixExpression node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProbabilityPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

//	@Override
//	public Object visit(ASTDateFormat node, Object data) throws QueryParseException {
//		return pvisit(node);
//	}

//	@Override
//	public Object visit(ASTSpatialPredicate node, Object data) throws QueryParseException {
//		return pvisit(node);
//	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSilab node, Object data) throws QueryParseException {
		return pvisit(node);
	}	

	@Override
	public Object visit(ASTCreateBroker node, Object data) throws QueryParseException {		
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBrokerSelectInto node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDateFormat node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBrokerAsSource node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBrokerSimpleSource node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBrokerQueue node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTMetric node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateViewStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateUserStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAlterUserStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropStreamStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropViewStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateSLAStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) throws QueryParseException {
		return pvisit(node);
	}	

	@Override
	public Object visit(ASTDatabaseTimeSensitiv node, Object data) throws QueryParseException {	
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropUserStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateRoleStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropRoleStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTGrantStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTRevokeStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTIdentifierList node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTGrantRoleStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTRevokeRoleStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTMVCovarianceRow node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserVisitor#visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateType, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCreateType node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaMetricDef node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaScopeDef node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaWindowDef node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaServiceLevelDef node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaPenaltyDef node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateSinkStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTStreamToStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTLoginPassword node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAutoReconnect node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDatabaseSink node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDatabaseSinkOptions node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateDatabaseConnection node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTTime node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFileSink node, Object data)
			throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSocketSink node, Object data)
			throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFileSource node, Object data)
			throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTIfExists node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCreateContextStore node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTContextStoreType node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropContextStore node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTIfNotExists node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaMaxAdmissionCostFactor node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSlaKillPenalty node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTAssignSLAStatement node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTJDBCConnection node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDatabaseConnectionCheck node, Object data) throws QueryParseException {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDropDatabaseConnection node, Object data) throws QueryParseException {
		return pvisit(node);
	}	
}
