package de.uniol.inf.is.odysseus.parser.cql.parser;

import java.io.StringReader;

public class NewSQLParserDumpVisitor implements NewSQLParserVisitor {

	private int indent = 0;

	private String indentString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; ++i) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private Object pvisit(SimpleNode node) {
		System.out.println(indentString() + node);
		++indent;
		node.childrenAccept(this, null);
		--indent;
		return null;
	}

	public Object visit(SimpleNode node, Object data) {
		// return pvisit(node);
		return null;
	}

	public Object visit(ASTSQLStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTSelectClause node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTFromClause node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTWhereClause node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTGroupByClause node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTHavingClause node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTSimplePredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTExpression node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTSimpleToken node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAggregateExpression node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAggregateFunction node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTDistinctExpression node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAS node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTWindow node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTRange node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTRows node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTIdentifier node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTInteger node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTNumber node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTCompareOperator node, Object data) {
		return pvisit(node);
	}

	public static void main(String[] args) {
		String q = "SELECT * FROM A";
		// String q = "SELECT A.b, C.d FROM Q RANGE 10000 ADVANCE 10, C AS Q
		// WHERE c.a > 2 AND c.b < 1 GROUP BY C.d, A.b";
		@SuppressWarnings("unused")
		NewSQLParser p = new NewSQLParser(new StringReader(q));
		try {
			NewSQLParser.Statement().childrenAccept(
					new NewSQLParserDumpVisitor(), null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object visit(ASTSetOperator node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTRenamedExpression node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTFunctionExpression node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTString node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAdvance node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTSlide node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTQuantificationPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAnyPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAllPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTInPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTExists node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTTuple node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTTupleSet node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTQuantificationOperator node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTPartition node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTOldWindow node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTStreamSQLWindow node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTOrPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAndPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTNotPredicate node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTPriorizedStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTComplexSelectStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTSelectStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTCreateStatement node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAttributeDefinitions node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTAttributeDefinition node, Object data) {
		return pvisit(node);
	}

	public Object visit(ASTTimedTuples node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	public Object visit(ASTTimedTuple node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	public Object visit(ASTTimeInterval node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	public Object visit(ASTSimpleTuple node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	public Object visit(ASTAttributeType node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	public Object visit(ASTPriority node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTElementPriority node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDefaultPriority node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSocket node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSpatialCompareOperator node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCovarianceRow node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTMatrixExpression node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProbabilityPredicate node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTDateFormat node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSpatialPredicate node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTOSGI node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTCSVSource node, Object data) {
		return pvisit(node);
	}

	@Override
	public Object visit(ASTSilab node, Object data) {
		// TODO Auto-generated method stub
		return pvisit(node);
	}

}
