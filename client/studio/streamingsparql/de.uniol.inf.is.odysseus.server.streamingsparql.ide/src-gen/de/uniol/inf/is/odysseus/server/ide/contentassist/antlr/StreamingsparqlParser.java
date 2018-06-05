/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.server.ide.contentassist.antlr;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.server.ide.contentassist.antlr.internal.InternalStreamingsparqlParser;
import de.uniol.inf.is.odysseus.server.services.StreamingsparqlGrammarAccess;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;

public class StreamingsparqlParser extends AbstractContentAssistParser {

	@Inject
	private StreamingsparqlGrammarAccess grammarAccess;

	private Map<AbstractElement, String> nameMappings;

	@Override
	protected InternalStreamingsparqlParser createParser() {
		InternalStreamingsparqlParser result = new InternalStreamingsparqlParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getPrefixAccess().getAlternatives(), "rule__Prefix__Alternatives");
					put(grammarAccess.getSelectQueryAccess().getAlternatives_0(), "rule__SelectQuery__Alternatives_0");
					put(grammarAccess.getGraphNodeAccess().getAlternatives(), "rule__GraphNode__Alternatives");
					put(grammarAccess.getVariableAccess().getAlternatives(), "rule__Variable__Alternatives");
					put(grammarAccess.getOperatorAccess().getAlternatives(), "rule__Operator__Alternatives");
					put(grammarAccess.getPrefixAccess().getGroup_0(), "rule__Prefix__Group_0__0");
					put(grammarAccess.getUnNamedPrefixAccess().getGroup(), "rule__UnNamedPrefix__Group__0");
					put(grammarAccess.getBaseAccess().getGroup(), "rule__Base__Group__0");
					put(grammarAccess.getSelectQueryAccess().getGroup(), "rule__SelectQuery__Group__0");
					put(grammarAccess.getAggregateAccess().getGroup(), "rule__Aggregate__Group__0");
					put(grammarAccess.getAggregateAccess().getGroup_2(), "rule__Aggregate__Group_2__0");
					put(grammarAccess.getAggregateAccess().getGroup_3(), "rule__Aggregate__Group_3__0");
					put(grammarAccess.getGroupByAccess().getGroup(), "rule__GroupBy__Group__0");
					put(grammarAccess.getGroupByAccess().getGroup_2(), "rule__GroupBy__Group_2__0");
					put(grammarAccess.getAggregationAccess().getGroup(), "rule__Aggregation__Group__0");
					put(grammarAccess.getAggregationAccess().getGroup_6(), "rule__Aggregation__Group_6__0");
					put(grammarAccess.getFilesinkclauseAccess().getGroup(), "rule__Filesinkclause__Group__0");
					put(grammarAccess.getFilterclauseAccess().getGroup(), "rule__Filterclause__Group__0");
					put(grammarAccess.getGroupClauseAccess().getGroup(), "rule__GroupClause__Group__0");
					put(grammarAccess.getDatasetClauseAccess().getGroup(), "rule__DatasetClause__Group__0");
					put(grammarAccess.getDatasetClauseAccess().getGroup_4(), "rule__DatasetClause__Group_4__0");
					put(grammarAccess.getDatasetClauseAccess().getGroup_4_5(), "rule__DatasetClause__Group_4_5__0");
					put(grammarAccess.getDatasetClauseAccess().getGroup_4_6(), "rule__DatasetClause__Group_4_6__0");
					put(grammarAccess.getWhereClauseAccess().getGroup(), "rule__WhereClause__Group__0");
					put(grammarAccess.getInnerWhereClauseAccess().getGroup(), "rule__InnerWhereClause__Group__0");
					put(grammarAccess.getGroupGraphPatternSubAccess().getGroup(), "rule__GroupGraphPatternSub__Group__0");
					put(grammarAccess.getGroupGraphPatternSubAccess().getGroup_2(), "rule__GroupGraphPatternSub__Group_2__0");
					put(grammarAccess.getTriplesSameSubjectAccess().getGroup(), "rule__TriplesSameSubject__Group__0");
					put(grammarAccess.getTriplesSameSubjectAccess().getGroup_2(), "rule__TriplesSameSubject__Group_2__0");
					put(grammarAccess.getPropertyListAccess().getGroup(), "rule__PropertyList__Group__0");
					put(grammarAccess.getUnNamedVariableAccess().getGroup(), "rule__UnNamedVariable__Group__0");
					put(grammarAccess.getPropertyAccess().getGroup(), "rule__Property__Group__0");
					put(grammarAccess.getIRIAccess().getGroup(), "rule__IRI__Group__0");
					put(grammarAccess.getTypeTagAccess().getGroup(), "rule__TypeTag__Group__0");
					put(grammarAccess.getLangTagAccess().getGroup(), "rule__LangTag__Group__0");
					put(grammarAccess.getPrefixAccess().getNameAssignment_0_1(), "rule__Prefix__NameAssignment_0_1");
					put(grammarAccess.getPrefixAccess().getIrefAssignment_0_3(), "rule__Prefix__IrefAssignment_0_3");
					put(grammarAccess.getUnNamedPrefixAccess().getIrefAssignment_2(), "rule__UnNamedPrefix__IrefAssignment_2");
					put(grammarAccess.getBaseAccess().getIrefAssignment_1(), "rule__Base__IrefAssignment_1");
					put(grammarAccess.getSelectQueryAccess().getMethodAssignment_0_0(), "rule__SelectQuery__MethodAssignment_0_0");
					put(grammarAccess.getSelectQueryAccess().getBaseAssignment_1(), "rule__SelectQuery__BaseAssignment_1");
					put(grammarAccess.getSelectQueryAccess().getPrefixesAssignment_2(), "rule__SelectQuery__PrefixesAssignment_2");
					put(grammarAccess.getSelectQueryAccess().getDatasetClausesAssignment_3(), "rule__SelectQuery__DatasetClausesAssignment_3");
					put(grammarAccess.getSelectQueryAccess().getVariablesAssignment_5(), "rule__SelectQuery__VariablesAssignment_5");
					put(grammarAccess.getSelectQueryAccess().getVariablesAssignment_6(), "rule__SelectQuery__VariablesAssignment_6");
					put(grammarAccess.getSelectQueryAccess().getWhereClauseAssignment_7(), "rule__SelectQuery__WhereClauseAssignment_7");
					put(grammarAccess.getSelectQueryAccess().getFilterclauseAssignment_8(), "rule__SelectQuery__FilterclauseAssignment_8");
					put(grammarAccess.getSelectQueryAccess().getAggregateClauseAssignment_9(), "rule__SelectQuery__AggregateClauseAssignment_9");
					put(grammarAccess.getSelectQueryAccess().getFilesinkclauseAssignment_10(), "rule__SelectQuery__FilesinkclauseAssignment_10");
					put(grammarAccess.getAggregateAccess().getAggregationsAssignment_2_3(), "rule__Aggregate__AggregationsAssignment_2_3");
					put(grammarAccess.getAggregateAccess().getGroupbyAssignment_3_1(), "rule__Aggregate__GroupbyAssignment_3_1");
					put(grammarAccess.getGroupByAccess().getVariablesAssignment_1(), "rule__GroupBy__VariablesAssignment_1");
					put(grammarAccess.getGroupByAccess().getVariablesAssignment_2_1(), "rule__GroupBy__VariablesAssignment_2_1");
					put(grammarAccess.getAggregationAccess().getFunctionAssignment_1(), "rule__Aggregation__FunctionAssignment_1");
					put(grammarAccess.getAggregationAccess().getVarToAggAssignment_3(), "rule__Aggregation__VarToAggAssignment_3");
					put(grammarAccess.getAggregationAccess().getAggNameAssignment_5(), "rule__Aggregation__AggNameAssignment_5");
					put(grammarAccess.getAggregationAccess().getDatatypeAssignment_6_1(), "rule__Aggregation__DatatypeAssignment_6_1");
					put(grammarAccess.getFilesinkclauseAccess().getPathAssignment_1(), "rule__Filesinkclause__PathAssignment_1");
					put(grammarAccess.getFilterclauseAccess().getLeftAssignment_1(), "rule__Filterclause__LeftAssignment_1");
					put(grammarAccess.getFilterclauseAccess().getOperatorAssignment_2(), "rule__Filterclause__OperatorAssignment_2");
					put(grammarAccess.getFilterclauseAccess().getRightAssignment_3(), "rule__Filterclause__RightAssignment_3");
					put(grammarAccess.getGroupClauseAccess().getConditionsAssignment_2(), "rule__GroupClause__ConditionsAssignment_2");
					put(grammarAccess.getGroupClauseAccess().getConditionsAssignment_3(), "rule__GroupClause__ConditionsAssignment_3");
					put(grammarAccess.getDatasetClauseAccess().getDataSetAssignment_1(), "rule__DatasetClause__DataSetAssignment_1");
					put(grammarAccess.getDatasetClauseAccess().getNameAssignment_3(), "rule__DatasetClause__NameAssignment_3");
					put(grammarAccess.getDatasetClauseAccess().getTypeAssignment_4_2(), "rule__DatasetClause__TypeAssignment_4_2");
					put(grammarAccess.getDatasetClauseAccess().getSizeAssignment_4_4(), "rule__DatasetClause__SizeAssignment_4_4");
					put(grammarAccess.getDatasetClauseAccess().getAdvanceAssignment_4_5_1(), "rule__DatasetClause__AdvanceAssignment_4_5_1");
					put(grammarAccess.getDatasetClauseAccess().getUnitAssignment_4_6_1(), "rule__DatasetClause__UnitAssignment_4_6_1");
					put(grammarAccess.getWhereClauseAccess().getWhereclausesAssignment_2(), "rule__WhereClause__WhereclausesAssignment_2");
					put(grammarAccess.getInnerWhereClauseAccess().getNameAssignment_0(), "rule__InnerWhereClause__NameAssignment_0");
					put(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternAssignment_1(), "rule__InnerWhereClause__GroupGraphPatternAssignment_1");
					put(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_1(), "rule__GroupGraphPatternSub__GraphPatternsAssignment_1");
					put(grammarAccess.getGroupGraphPatternSubAccess().getGraphPatternsAssignment_2_1(), "rule__GroupGraphPatternSub__GraphPatternsAssignment_2_1");
					put(grammarAccess.getTriplesSameSubjectAccess().getSubjectAssignment_0(), "rule__TriplesSameSubject__SubjectAssignment_0");
					put(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_1(), "rule__TriplesSameSubject__PropertyListAssignment_1");
					put(grammarAccess.getTriplesSameSubjectAccess().getPropertyListAssignment_2_1(), "rule__TriplesSameSubject__PropertyListAssignment_2_1");
					put(grammarAccess.getPropertyListAccess().getPropertyAssignment_0(), "rule__PropertyList__PropertyAssignment_0");
					put(grammarAccess.getPropertyListAccess().getObjectAssignment_1(), "rule__PropertyList__ObjectAssignment_1");
					put(grammarAccess.getGraphNodeAccess().getVariableAssignment_0(), "rule__GraphNode__VariableAssignment_0");
					put(grammarAccess.getGraphNodeAccess().getLiteralAssignment_1(), "rule__GraphNode__LiteralAssignment_1");
					put(grammarAccess.getGraphNodeAccess().getIriAssignment_2(), "rule__GraphNode__IriAssignment_2");
					put(grammarAccess.getVariableAccess().getUnnamedAssignment_0(), "rule__Variable__UnnamedAssignment_0");
					put(grammarAccess.getVariableAccess().getPropertyAssignment_1(), "rule__Variable__PropertyAssignment_1");
					put(grammarAccess.getUnNamedVariableAccess().getNameAssignment_1(), "rule__UnNamedVariable__NameAssignment_1");
					put(grammarAccess.getPropertyAccess().getPrefixAssignment_0(), "rule__Property__PrefixAssignment_0");
					put(grammarAccess.getPropertyAccess().getNameAssignment_2(), "rule__Property__NameAssignment_2");
					put(grammarAccess.getIRIAccess().getValueAssignment_1(), "rule__IRI__ValueAssignment_1");
					put(grammarAccess.getTypeTagAccess().getTypeAssignment_1(), "rule__TypeTag__TypeAssignment_1");
					put(grammarAccess.getLangTagAccess().getLangAssignment_1(), "rule__LangTag__LangAssignment_1");
				}
			};
		}
		return nameMappings.get(element);
	}
			
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public StreamingsparqlGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(StreamingsparqlGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
