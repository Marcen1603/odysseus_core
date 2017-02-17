/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.ide.contentassist.antlr;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.ide.contentassist.antlr.internal.InternalCQLParser;
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

public class CQLParser extends AbstractContentAssistParser {

	@Inject
	private CQLGrammarAccess grammarAccess;

	private Map<AbstractElement, String> nameMappings;

	@Override
	protected InternalCQLParser createParser() {
		InternalCQLParser result = new InternalCQLParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getIDOrINTAccess().getAlternatives(), "rule__IDOrINT__Alternatives");
					put(grammarAccess.getStatementAccess().getAlternatives_0(), "rule__Statement__Alternatives_0");
					put(grammarAccess.getSelectAccess().getAlternatives_2(), "rule__Select__Alternatives_2");
					put(grammarAccess.getSelectAccess().getAlternatives_2_1_0(), "rule__Select__Alternatives_2_1_0");
					put(grammarAccess.getSelectAccess().getAlternatives_2_1_1(), "rule__Select__Alternatives_2_1_1");
					put(grammarAccess.getSourceAccess().getAlternatives(), "rule__Source__Alternatives");
					put(grammarAccess.getSourceAccess().getAlternatives_0_1_1(), "rule__Source__Alternatives_0_1_1");
					put(grammarAccess.getAttributeNameAccess().getAlternatives(), "rule__AttributeName__Alternatives");
					put(grammarAccess.getAggregationAccess().getNameAlternatives_0_0(), "rule__Aggregation__NameAlternatives_0_0");
					put(grammarAccess.getAggregationAccess().getAlternatives_2(), "rule__Aggregation__Alternatives_2");
					put(grammarAccess.getAggregationWithoutAliasDefinitionAccess().getNameAlternatives_0_0(), "rule__AggregationWithoutAliasDefinition__NameAlternatives_0_0");
					put(grammarAccess.getExpressionComponentAccess().getAlternatives(), "rule__ExpressionComponent__Alternatives");
					put(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getAlternatives(), "rule__ExpressionComponentConstantOrAttribute__Alternatives");
					put(grammarAccess.getExpressionComponentMapperOrConstantAccess().getAlternatives(), "rule__ExpressionComponentMapperOrConstant__Alternatives");
					put(grammarAccess.getSelectExpressionAccess().getAlternatives_0(), "rule__SelectExpression__Alternatives_0");
					put(grammarAccess.getSelectExpressionAccess().getOperatorsAlternatives_0_0_1_0_0(), "rule__SelectExpression__OperatorsAlternatives_0_0_1_0_0");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAlternatives_0_0_1_1_0(), "rule__SelectExpression__ExpressionsAlternatives_0_0_1_1_0");
					put(grammarAccess.getSelectExpressionAccess().getOperatorsAlternatives_0_1_1_0_0(), "rule__SelectExpression__OperatorsAlternatives_0_1_1_0_0");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAlternatives_0_1_1_1_0(), "rule__SelectExpression__ExpressionsAlternatives_0_1_1_1_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getAlternatives_1(), "rule__SelectExpressionWithoutAliasDefinition__Alternatives_1");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAlternatives_1_0_1_0_0(), "rule__SelectExpressionWithoutAliasDefinition__OperatorsAlternatives_1_0_1_0_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAlternatives_1_0_1_1_0(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAlternatives_1_0_1_1_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAlternatives_1_1_1_0_0(), "rule__SelectExpressionWithoutAliasDefinition__OperatorsAlternatives_1_1_1_0_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAlternatives_1_1_1_1_0(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAlternatives_1_1_1_1_0");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsAlternatives_1_0_0(), "rule__SelectExpressionWithOnlyAttributeOrConstant__OperatorsAlternatives_1_0_0");
					put(grammarAccess.getStreamToAccess().getAlternatives_3(), "rule__StreamTo__Alternatives_3");
					put(grammarAccess.getDropAccess().getKeyword2Alternatives_1_0(), "rule__Drop__Keyword2Alternatives_1_0");
					put(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0(), "rule__Equalitiy__OpAlternatives_1_1_0");
					put(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0(), "rule__Comparison__OpAlternatives_1_1_0");
					put(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0(), "rule__PlusOrMinus__Alternatives_1_0");
					put(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0(), "rule__MulOrDiv__OpAlternatives_1_1_0");
					put(grammarAccess.getPrimaryAccess().getAlternatives(), "rule__Primary__Alternatives");
					put(grammarAccess.getAtomicAccess().getAlternatives(), "rule__Atomic__Alternatives");
					put(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0(), "rule__Atomic__ValueAlternatives_3_1_0");
					put(grammarAccess.getAtomicAccess().getAlternatives_4_1(), "rule__Atomic__Alternatives_4_1");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getAlternatives(), "rule__AtomicWithoutAttributeRef__Alternatives");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueAlternatives_3_1_0(), "rule__AtomicWithoutAttributeRef__ValueAlternatives_3_1_0");
					put(grammarAccess.getDataTypeAccess().getValueAlternatives_0(), "rule__DataType__ValueAlternatives_0");
					put(grammarAccess.getCreateKeywordAccess().getAlternatives(), "rule__CreateKeyword__Alternatives");
					put(grammarAccess.getStatementAccess().getGroup(), "rule__Statement__Group__0");
					put(grammarAccess.getSelectAccess().getGroup(), "rule__Select__Group__0");
					put(grammarAccess.getSelectAccess().getGroup_2_1(), "rule__Select__Group_2_1__0");
					put(grammarAccess.getSelectAccess().getGroup_2_1_1_0(), "rule__Select__Group_2_1_1_0__0");
					put(grammarAccess.getSelectAccess().getGroup_2_1_1_1(), "rule__Select__Group_2_1_1_1__0");
					put(grammarAccess.getSelectAccess().getGroup_2_1_1_2(), "rule__Select__Group_2_1_1_2__0");
					put(grammarAccess.getSelectAccess().getGroup_3(), "rule__Select__Group_3__0");
					put(grammarAccess.getSelectAccess().getGroup_3_2(), "rule__Select__Group_3_2__0");
					put(grammarAccess.getSelectAccess().getGroup_4(), "rule__Select__Group_4__0");
					put(grammarAccess.getSelectAccess().getGroup_5(), "rule__Select__Group_5__0");
					put(grammarAccess.getSelectAccess().getGroup_5_3(), "rule__Select__Group_5_3__0");
					put(grammarAccess.getSelectAccess().getGroup_6(), "rule__Select__Group_6__0");
					put(grammarAccess.getNestedStatementAccess().getGroup(), "rule__NestedStatement__Group__0");
					put(grammarAccess.getSourceAccess().getGroup_0(), "rule__Source__Group_0__0");
					put(grammarAccess.getSourceAccess().getGroup_0_1(), "rule__Source__Group_0_1__0");
					put(grammarAccess.getSourceAccess().getGroup_0_2(), "rule__Source__Group_0_2__0");
					put(grammarAccess.getSourceAccess().getGroup_1(), "rule__Source__Group_1__0");
					put(grammarAccess.getAttributeAccess().getGroup(), "rule__Attribute__Group__0");
					put(grammarAccess.getAttributeAccess().getGroup_1(), "rule__Attribute__Group_1__0");
					put(grammarAccess.getAttributeNameAccess().getGroup_1(), "rule__AttributeName__Group_1__0");
					put(grammarAccess.getAttributeWithNestedStatementAccess().getGroup(), "rule__AttributeWithNestedStatement__Group__0");
					put(grammarAccess.getAggregationAccess().getGroup(), "rule__Aggregation__Group__0");
					put(grammarAccess.getAggregationAccess().getGroup_4(), "rule__Aggregation__Group_4__0");
					put(grammarAccess.getAggregationWithoutAliasDefinitionAccess().getGroup(), "rule__AggregationWithoutAliasDefinition__Group__0");
					put(grammarAccess.getExpressionComponentAccess().getGroup_0(), "rule__ExpressionComponent__Group_0__0");
					put(grammarAccess.getExpressionComponentAccess().getGroup_1(), "rule__ExpressionComponent__Group_1__0");
					put(grammarAccess.getExpressionComponentMapperOrConstantAccess().getGroup_0(), "rule__ExpressionComponentMapperOrConstant__Group_0__0");
					put(grammarAccess.getExpressionComponentOnlymapperAccess().getGroup(), "rule__ExpressionComponentOnlymapper__Group__0");
					put(grammarAccess.getMapperAccess().getGroup(), "rule__Mapper__Group__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup(), "rule__SelectExpression__Group__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup_0_0(), "rule__SelectExpression__Group_0_0__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup_0_0_1(), "rule__SelectExpression__Group_0_0_1__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup_0_1(), "rule__SelectExpression__Group_0_1__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup_0_1_1(), "rule__SelectExpression__Group_0_1_1__0");
					put(grammarAccess.getSelectExpressionAccess().getGroup_1(), "rule__SelectExpression__Group_1__0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getGroup(), "rule__SelectExpressionWithoutAliasDefinition__Group__0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getGroup_1_0(), "rule__SelectExpressionWithoutAliasDefinition__Group_1_0__0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getGroup_1_0_1(), "rule__SelectExpressionWithoutAliasDefinition__Group_1_0_1__0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getGroup_1_1(), "rule__SelectExpressionWithoutAliasDefinition__Group_1_1__0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getGroup_1_1_1(), "rule__SelectExpressionWithoutAliasDefinition__Group_1_1_1__0");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getGroup(), "rule__SelectExpressionWithOnlyAttributeOrConstant__Group__0");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getGroup_1(), "rule__SelectExpressionWithOnlyAttributeOrConstant__Group_1__0");
					put(grammarAccess.getCreateParametersAccess().getGroup(), "rule__CreateParameters__Group__0");
					put(grammarAccess.getCreateParametersAccess().getGroup_10(), "rule__CreateParameters__Group_10__0");
					put(grammarAccess.getCreateParametersAccess().getGroup_11(), "rule__CreateParameters__Group_11__0");
					put(grammarAccess.getAttributeDefinitionAccess().getGroup(), "rule__AttributeDefinition__Group__0");
					put(grammarAccess.getAttributeDefinitionAccess().getGroup_4(), "rule__AttributeDefinition__Group_4__0");
					put(grammarAccess.getCreateStream1Access().getGroup(), "rule__CreateStream1__Group__0");
					put(grammarAccess.getCreateSink1Access().getGroup(), "rule__CreateSink1__Group__0");
					put(grammarAccess.getCreateStreamChannelAccess().getGroup(), "rule__CreateStreamChannel__Group__0");
					put(grammarAccess.getCreateStreamFileAccess().getGroup(), "rule__CreateStreamFile__Group__0");
					put(grammarAccess.getCreateViewAccess().getGroup(), "rule__CreateView__Group__0");
					put(grammarAccess.getStreamToAccess().getGroup(), "rule__StreamTo__Group__0");
					put(grammarAccess.getCommandAccess().getGroup(), "rule__Command__Group__0");
					put(grammarAccess.getDropAccess().getGroup(), "rule__Drop__Group__0");
					put(grammarAccess.getWindow_TimebasedAccess().getGroup(), "rule__Window_Timebased__Group__0");
					put(grammarAccess.getWindow_TimebasedAccess().getGroup_3(), "rule__Window_Timebased__Group_3__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup(), "rule__Window_Tuplebased__Group__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup_2(), "rule__Window_Tuplebased__Group_2__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup_4(), "rule__Window_Tuplebased__Group_4__0");
					put(grammarAccess.getExpressionsModelAccess().getGroup(), "rule__ExpressionsModel__Group__0");
					put(grammarAccess.getOrAccess().getGroup(), "rule__Or__Group__0");
					put(grammarAccess.getOrAccess().getGroup_1(), "rule__Or__Group_1__0");
					put(grammarAccess.getAndAccess().getGroup(), "rule__And__Group__0");
					put(grammarAccess.getAndAccess().getGroup_1(), "rule__And__Group_1__0");
					put(grammarAccess.getEqualitiyAccess().getGroup(), "rule__Equalitiy__Group__0");
					put(grammarAccess.getEqualitiyAccess().getGroup_1(), "rule__Equalitiy__Group_1__0");
					put(grammarAccess.getComparisonAccess().getGroup(), "rule__Comparison__Group__0");
					put(grammarAccess.getComparisonAccess().getGroup_1(), "rule__Comparison__Group_1__0");
					put(grammarAccess.getPlusOrMinusAccess().getGroup(), "rule__PlusOrMinus__Group__0");
					put(grammarAccess.getPlusOrMinusAccess().getGroup_1(), "rule__PlusOrMinus__Group_1__0");
					put(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_0(), "rule__PlusOrMinus__Group_1_0_0__0");
					put(grammarAccess.getPlusOrMinusAccess().getGroup_1_0_1(), "rule__PlusOrMinus__Group_1_0_1__0");
					put(grammarAccess.getMulOrDivAccess().getGroup(), "rule__MulOrDiv__Group__0");
					put(grammarAccess.getMulOrDivAccess().getGroup_1(), "rule__MulOrDiv__Group_1__0");
					put(grammarAccess.getPrimaryAccess().getGroup_0(), "rule__Primary__Group_0__0");
					put(grammarAccess.getPrimaryAccess().getGroup_1(), "rule__Primary__Group_1__0");
					put(grammarAccess.getAtomicAccess().getGroup_0(), "rule__Atomic__Group_0__0");
					put(grammarAccess.getAtomicAccess().getGroup_1(), "rule__Atomic__Group_1__0");
					put(grammarAccess.getAtomicAccess().getGroup_2(), "rule__Atomic__Group_2__0");
					put(grammarAccess.getAtomicAccess().getGroup_3(), "rule__Atomic__Group_3__0");
					put(grammarAccess.getAtomicAccess().getGroup_4(), "rule__Atomic__Group_4__0");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getGroup_0(), "rule__AtomicWithoutAttributeRef__Group_0__0");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getGroup_1(), "rule__AtomicWithoutAttributeRef__Group_1__0");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getGroup_2(), "rule__AtomicWithoutAttributeRef__Group_2__0");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getGroup_3(), "rule__AtomicWithoutAttributeRef__Group_3__0");
					put(grammarAccess.getAtomicWithOnlyStringConstantAccess().getGroup(), "rule__AtomicWithOnlyStringConstant__Group__0");
					put(grammarAccess.getModelAccess().getStatementsAssignment(), "rule__Model__StatementsAssignment");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_0(), "rule__Statement__TypeAssignment_0_0");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_1(), "rule__Statement__TypeAssignment_0_1");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_2(), "rule__Statement__TypeAssignment_0_2");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_3(), "rule__Statement__TypeAssignment_0_3");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_4(), "rule__Statement__TypeAssignment_0_4");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_5(), "rule__Statement__TypeAssignment_0_5");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_6(), "rule__Statement__TypeAssignment_0_6");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_7(), "rule__Statement__TypeAssignment_0_7");
					put(grammarAccess.getSelectAccess().getNameAssignment_0(), "rule__Select__NameAssignment_0");
					put(grammarAccess.getSelectAccess().getDistinctAssignment_1(), "rule__Select__DistinctAssignment_1");
					put(grammarAccess.getSelectAccess().getAttributesAssignment_2_1_0_0(), "rule__Select__AttributesAssignment_2_1_0_0");
					put(grammarAccess.getSelectAccess().getAggregationsAssignment_2_1_0_1(), "rule__Select__AggregationsAssignment_2_1_0_1");
					put(grammarAccess.getSelectAccess().getExpressionsAssignment_2_1_0_2(), "rule__Select__ExpressionsAssignment_2_1_0_2");
					put(grammarAccess.getSelectAccess().getAttributesAssignment_2_1_1_0_1(), "rule__Select__AttributesAssignment_2_1_1_0_1");
					put(grammarAccess.getSelectAccess().getAggregationsAssignment_2_1_1_1_1(), "rule__Select__AggregationsAssignment_2_1_1_1_1");
					put(grammarAccess.getSelectAccess().getExpressionsAssignment_2_1_1_2_1(), "rule__Select__ExpressionsAssignment_2_1_1_2_1");
					put(grammarAccess.getSelectAccess().getSourcesAssignment_3_1(), "rule__Select__SourcesAssignment_3_1");
					put(grammarAccess.getSelectAccess().getSourcesAssignment_3_2_1(), "rule__Select__SourcesAssignment_3_2_1");
					put(grammarAccess.getSelectAccess().getPredicatesAssignment_4_1(), "rule__Select__PredicatesAssignment_4_1");
					put(grammarAccess.getSelectAccess().getOrderAssignment_5_2(), "rule__Select__OrderAssignment_5_2");
					put(grammarAccess.getSelectAccess().getOrderAssignment_5_3_1(), "rule__Select__OrderAssignment_5_3_1");
					put(grammarAccess.getSelectAccess().getHavingAssignment_6_1(), "rule__Select__HavingAssignment_6_1");
					put(grammarAccess.getSourceAccess().getNameAssignment_0_0(), "rule__Source__NameAssignment_0_0");
					put(grammarAccess.getSourceAccess().getUnboundedAssignment_0_1_1_0(), "rule__Source__UnboundedAssignment_0_1_1_0");
					put(grammarAccess.getSourceAccess().getTimeAssignment_0_1_1_1(), "rule__Source__TimeAssignment_0_1_1_1");
					put(grammarAccess.getSourceAccess().getTupleAssignment_0_1_1_2(), "rule__Source__TupleAssignment_0_1_1_2");
					put(grammarAccess.getSourceAccess().getAliasAssignment_0_2_1(), "rule__Source__AliasAssignment_0_2_1");
					put(grammarAccess.getSourceAccess().getNestedAssignment_1_0(), "rule__Source__NestedAssignment_1_0");
					put(grammarAccess.getSourceAccess().getAliasAssignment_1_2(), "rule__Source__AliasAssignment_1_2");
					put(grammarAccess.getAttributeAccess().getNameAssignment_0(), "rule__Attribute__NameAssignment_0");
					put(grammarAccess.getAttributeAccess().getAliasAssignment_1_1(), "rule__Attribute__AliasAssignment_1_1");
					put(grammarAccess.getAttributeWithoutAliasDefinitionAccess().getNameAssignment(), "rule__AttributeWithoutAliasDefinition__NameAssignment");
					put(grammarAccess.getAttributeWithNestedStatementAccess().getValueAssignment_0(), "rule__AttributeWithNestedStatement__ValueAssignment_0");
					put(grammarAccess.getAttributeWithNestedStatementAccess().getNestedAssignment_2(), "rule__AttributeWithNestedStatement__NestedAssignment_2");
					put(grammarAccess.getAggregationAccess().getNameAssignment_0(), "rule__Aggregation__NameAssignment_0");
					put(grammarAccess.getAggregationAccess().getAttributeAssignment_2_0(), "rule__Aggregation__AttributeAssignment_2_0");
					put(grammarAccess.getAggregationAccess().getExpressionAssignment_2_1(), "rule__Aggregation__ExpressionAssignment_2_1");
					put(grammarAccess.getAggregationAccess().getAliasAssignment_4_1(), "rule__Aggregation__AliasAssignment_4_1");
					put(grammarAccess.getAggregationWithoutAliasDefinitionAccess().getNameAssignment_0(), "rule__AggregationWithoutAliasDefinition__NameAssignment_0");
					put(grammarAccess.getAggregationWithoutAliasDefinitionAccess().getAttributeAssignment_2(), "rule__AggregationWithoutAliasDefinition__AttributeAssignment_2");
					put(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAssignment_0(), "rule__ExpressionComponentConstantOrAttribute__ValueAssignment_0");
					put(grammarAccess.getExpressionComponentConstantOrAttributeAccess().getValueAssignment_1(), "rule__ExpressionComponentConstantOrAttribute__ValueAssignment_1");
					put(grammarAccess.getExpressionComponentMapperOrConstantAccess().getValueAssignment_1(), "rule__ExpressionComponentMapperOrConstant__ValueAssignment_1");
					put(grammarAccess.getExpressionComponentOnlyAttributeAccess().getValueAssignment(), "rule__ExpressionComponentOnlyAttribute__ValueAssignment");
					put(grammarAccess.getExpressionComponentOnlyConstantAccess().getValueAssignment(), "rule__ExpressionComponentOnlyConstant__ValueAssignment");
					put(grammarAccess.getMapperAccess().getNameAssignment_1(), "rule__Mapper__NameAssignment_1");
					put(grammarAccess.getMapperAccess().getValueAssignment_3(), "rule__Mapper__ValueAssignment_3");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAssignment_0_0_0(), "rule__SelectExpression__ExpressionsAssignment_0_0_0");
					put(grammarAccess.getSelectExpressionAccess().getOperatorsAssignment_0_0_1_0(), "rule__SelectExpression__OperatorsAssignment_0_0_1_0");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAssignment_0_0_1_1(), "rule__SelectExpression__ExpressionsAssignment_0_0_1_1");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAssignment_0_1_0(), "rule__SelectExpression__ExpressionsAssignment_0_1_0");
					put(grammarAccess.getSelectExpressionAccess().getOperatorsAssignment_0_1_1_0(), "rule__SelectExpression__OperatorsAssignment_0_1_1_0");
					put(grammarAccess.getSelectExpressionAccess().getExpressionsAssignment_0_1_1_1(), "rule__SelectExpression__ExpressionsAssignment_0_1_1_1");
					put(grammarAccess.getSelectExpressionAccess().getAliasAssignment_1_1(), "rule__SelectExpression__AliasAssignment_1_1");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAssignment_1_0_0(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAssignment_1_0_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAssignment_1_0_1_0(), "rule__SelectExpressionWithoutAliasDefinition__OperatorsAssignment_1_0_1_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAssignment_1_0_1_1(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAssignment_1_0_1_1");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAssignment_1_1_0(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAssignment_1_1_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getOperatorsAssignment_1_1_1_0(), "rule__SelectExpressionWithoutAliasDefinition__OperatorsAssignment_1_1_1_0");
					put(grammarAccess.getSelectExpressionWithoutAliasDefinitionAccess().getExpressionsAssignment_1_1_1_1(), "rule__SelectExpressionWithoutAliasDefinition__ExpressionsAssignment_1_1_1_1");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsAssignment_0(), "rule__SelectExpressionWithOnlyAttributeOrConstant__ExpressionsAssignment_0");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getOperatorsAssignment_1_0(), "rule__SelectExpressionWithOnlyAttributeOrConstant__OperatorsAssignment_1_0");
					put(grammarAccess.getSelectExpressionWithOnlyAttributeOrConstantAccess().getExpressionsAssignment_1_1(), "rule__SelectExpressionWithOnlyAttributeOrConstant__ExpressionsAssignment_1_1");
					put(grammarAccess.getAliasAccess().getNameAssignment(), "rule__Alias__NameAssignment");
					put(grammarAccess.getCreateParametersAccess().getWrapperAssignment_1(), "rule__CreateParameters__WrapperAssignment_1");
					put(grammarAccess.getCreateParametersAccess().getProtocolAssignment_3(), "rule__CreateParameters__ProtocolAssignment_3");
					put(grammarAccess.getCreateParametersAccess().getTransportAssignment_5(), "rule__CreateParameters__TransportAssignment_5");
					put(grammarAccess.getCreateParametersAccess().getDatahandlerAssignment_7(), "rule__CreateParameters__DatahandlerAssignment_7");
					put(grammarAccess.getCreateParametersAccess().getKeysAssignment_10_0(), "rule__CreateParameters__KeysAssignment_10_0");
					put(grammarAccess.getCreateParametersAccess().getValuesAssignment_10_1(), "rule__CreateParameters__ValuesAssignment_10_1");
					put(grammarAccess.getCreateParametersAccess().getKeysAssignment_11_1(), "rule__CreateParameters__KeysAssignment_11_1");
					put(grammarAccess.getCreateParametersAccess().getValuesAssignment_11_2(), "rule__CreateParameters__ValuesAssignment_11_2");
					put(grammarAccess.getAttributeDefinitionAccess().getNameAssignment_0(), "rule__AttributeDefinition__NameAssignment_0");
					put(grammarAccess.getAttributeDefinitionAccess().getAttributesAssignment_2(), "rule__AttributeDefinition__AttributesAssignment_2");
					put(grammarAccess.getAttributeDefinitionAccess().getDatatypesAssignment_3(), "rule__AttributeDefinition__DatatypesAssignment_3");
					put(grammarAccess.getAttributeDefinitionAccess().getAttributesAssignment_4_1(), "rule__AttributeDefinition__AttributesAssignment_4_1");
					put(grammarAccess.getAttributeDefinitionAccess().getDatatypesAssignment_4_2(), "rule__AttributeDefinition__DatatypesAssignment_4_2");
					put(grammarAccess.getCreateStream1Access().getKeywordAssignment_0(), "rule__CreateStream1__KeywordAssignment_0");
					put(grammarAccess.getCreateStream1Access().getAttributesAssignment_2(), "rule__CreateStream1__AttributesAssignment_2");
					put(grammarAccess.getCreateStream1Access().getParsAssignment_3(), "rule__CreateStream1__ParsAssignment_3");
					put(grammarAccess.getCreateSink1Access().getKeywordAssignment_0(), "rule__CreateSink1__KeywordAssignment_0");
					put(grammarAccess.getCreateSink1Access().getAttributesAssignment_2(), "rule__CreateSink1__AttributesAssignment_2");
					put(grammarAccess.getCreateSink1Access().getParsAssignment_3(), "rule__CreateSink1__ParsAssignment_3");
					put(grammarAccess.getCreateStreamChannelAccess().getKeywordAssignment_0(), "rule__CreateStreamChannel__KeywordAssignment_0");
					put(grammarAccess.getCreateStreamChannelAccess().getAttributesAssignment_2(), "rule__CreateStreamChannel__AttributesAssignment_2");
					put(grammarAccess.getCreateStreamChannelAccess().getHostAssignment_4(), "rule__CreateStreamChannel__HostAssignment_4");
					put(grammarAccess.getCreateStreamChannelAccess().getPortAssignment_6(), "rule__CreateStreamChannel__PortAssignment_6");
					put(grammarAccess.getCreateStreamFileAccess().getKeywordAssignment_0(), "rule__CreateStreamFile__KeywordAssignment_0");
					put(grammarAccess.getCreateStreamFileAccess().getAttributesAssignment_2(), "rule__CreateStreamFile__AttributesAssignment_2");
					put(grammarAccess.getCreateStreamFileAccess().getFilenameAssignment_4(), "rule__CreateStreamFile__FilenameAssignment_4");
					put(grammarAccess.getCreateStreamFileAccess().getTypeAssignment_6(), "rule__CreateStreamFile__TypeAssignment_6");
					put(grammarAccess.getCreateViewAccess().getNameAssignment_1(), "rule__CreateView__NameAssignment_1");
					put(grammarAccess.getCreateViewAccess().getSelectAssignment_3(), "rule__CreateView__SelectAssignment_3");
					put(grammarAccess.getStreamToAccess().getNameAssignment_2(), "rule__StreamTo__NameAssignment_2");
					put(grammarAccess.getStreamToAccess().getStatementAssignment_3_0(), "rule__StreamTo__StatementAssignment_3_0");
					put(grammarAccess.getStreamToAccess().getInputnameAssignment_3_1(), "rule__StreamTo__InputnameAssignment_3_1");
					put(grammarAccess.getCommandAccess().getKeyword1Assignment_0(), "rule__Command__Keyword1Assignment_0");
					put(grammarAccess.getCommandAccess().getKeyword2Assignment_1(), "rule__Command__Keyword2Assignment_1");
					put(grammarAccess.getCommandAccess().getValue1Assignment_2(), "rule__Command__Value1Assignment_2");
					put(grammarAccess.getCommandAccess().getKeyword3Assignment_3(), "rule__Command__Keyword3Assignment_3");
					put(grammarAccess.getCommandAccess().getValue2Assignment_4(), "rule__Command__Value2Assignment_4");
					put(grammarAccess.getDropAccess().getKeyword1Assignment_0(), "rule__Drop__Keyword1Assignment_0");
					put(grammarAccess.getDropAccess().getKeyword2Assignment_1(), "rule__Drop__Keyword2Assignment_1");
					put(grammarAccess.getDropAccess().getValue1Assignment_2(), "rule__Drop__Value1Assignment_2");
					put(grammarAccess.getDropAccess().getKeyword3Assignment_3(), "rule__Drop__Keyword3Assignment_3");
					put(grammarAccess.getDropAccess().getValue2Assignment_4(), "rule__Drop__Value2Assignment_4");
					put(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_1(), "rule__Window_Timebased__SizeAssignment_1");
					put(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_2(), "rule__Window_Timebased__UnitAssignment_2");
					put(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_3_1(), "rule__Window_Timebased__Advance_sizeAssignment_3_1");
					put(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_3_2(), "rule__Window_Timebased__Advance_unitAssignment_3_2");
					put(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_1(), "rule__Window_Tuplebased__SizeAssignment_1");
					put(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_2_1(), "rule__Window_Tuplebased__Advance_sizeAssignment_2_1");
					put(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_4_2(), "rule__Window_Tuplebased__Partition_attributeAssignment_4_2");
					put(grammarAccess.getExpressionsModelAccess().getElementsAssignment_1(), "rule__ExpressionsModel__ElementsAssignment_1");
					put(grammarAccess.getOrAccess().getRightAssignment_1_2(), "rule__Or__RightAssignment_1_2");
					put(grammarAccess.getAndAccess().getRightAssignment_1_2(), "rule__And__RightAssignment_1_2");
					put(grammarAccess.getEqualitiyAccess().getOpAssignment_1_1(), "rule__Equalitiy__OpAssignment_1_1");
					put(grammarAccess.getEqualitiyAccess().getRightAssignment_1_2(), "rule__Equalitiy__RightAssignment_1_2");
					put(grammarAccess.getComparisonAccess().getOpAssignment_1_1(), "rule__Comparison__OpAssignment_1_1");
					put(grammarAccess.getComparisonAccess().getRightAssignment_1_2(), "rule__Comparison__RightAssignment_1_2");
					put(grammarAccess.getPlusOrMinusAccess().getRightAssignment_1_1(), "rule__PlusOrMinus__RightAssignment_1_1");
					put(grammarAccess.getMulOrDivAccess().getOpAssignment_1_1(), "rule__MulOrDiv__OpAssignment_1_1");
					put(grammarAccess.getMulOrDivAccess().getRightAssignment_1_2(), "rule__MulOrDiv__RightAssignment_1_2");
					put(grammarAccess.getPrimaryAccess().getInnerAssignment_0_2(), "rule__Primary__InnerAssignment_0_2");
					put(grammarAccess.getPrimaryAccess().getExpressionAssignment_1_2(), "rule__Primary__ExpressionAssignment_1_2");
					put(grammarAccess.getAtomicAccess().getValueAssignment_0_1(), "rule__Atomic__ValueAssignment_0_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_1_1(), "rule__Atomic__ValueAssignment_1_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_2_1(), "rule__Atomic__ValueAssignment_2_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_3_1(), "rule__Atomic__ValueAssignment_3_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_4_1_0(), "rule__Atomic__ValueAssignment_4_1_0");
					put(grammarAccess.getAtomicAccess().getValueAssignment_4_1_1(), "rule__Atomic__ValueAssignment_4_1_1");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueAssignment_0_1(), "rule__AtomicWithoutAttributeRef__ValueAssignment_0_1");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueAssignment_1_1(), "rule__AtomicWithoutAttributeRef__ValueAssignment_1_1");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueAssignment_2_1(), "rule__AtomicWithoutAttributeRef__ValueAssignment_2_1");
					put(grammarAccess.getAtomicWithoutAttributeRefAccess().getValueAssignment_3_1(), "rule__AtomicWithoutAttributeRef__ValueAssignment_3_1");
					put(grammarAccess.getAtomicWithOnlyStringConstantAccess().getValueAssignment_1(), "rule__AtomicWithOnlyStringConstant__ValueAssignment_1");
					put(grammarAccess.getDataTypeAccess().getValueAssignment(), "rule__DataType__ValueAssignment");
				}
			};
		}
		return nameMappings.get(element);
	}

	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			InternalCQLParser typedParser = (InternalCQLParser) parser;
			typedParser.entryRuleModel();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public CQLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(CQLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
