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
					put(grammarAccess.getStatementAccess().getAlternatives_0(), "rule__Statement__Alternatives_0");
					put(grammarAccess.getAtomicAccess().getAlternatives(), "rule__Atomic__Alternatives");
					put(grammarAccess.getAtomicAccess().getValueAlternatives_3_1_0(), "rule__Atomic__ValueAlternatives_3_1_0");
					put(grammarAccess.getSourceAccess().getAlternatives_1_1(), "rule__Source__Alternatives_1_1");
					put(grammarAccess.getDataTypeAccess().getAlternatives(), "rule__DataType__Alternatives");
					put(grammarAccess.getEqualitiyAccess().getOpAlternatives_1_1_0(), "rule__Equalitiy__OpAlternatives_1_1_0");
					put(grammarAccess.getComparisonAccess().getOpAlternatives_1_1_0(), "rule__Comparison__OpAlternatives_1_1_0");
					put(grammarAccess.getPlusOrMinusAccess().getAlternatives_1_0(), "rule__PlusOrMinus__Alternatives_1_0");
					put(grammarAccess.getMulOrDivAccess().getOpAlternatives_1_1_0(), "rule__MulOrDiv__OpAlternatives_1_1_0");
					put(grammarAccess.getPrimaryAccess().getAlternatives(), "rule__Primary__Alternatives");
					put(grammarAccess.getSelect_StatementAccess().getAlternatives_2(), "rule__Select_Statement__Alternatives_2");
					put(grammarAccess.getConditionAccess().getAlternatives(), "rule__Condition__Alternatives");
					put(grammarAccess.getCreate_StatementAccess().getAlternatives_1(), "rule__Create_Statement__Alternatives_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getTypeAlternatives_0_0(), "rule__Create_AccessFramework__TypeAlternatives_0_0");
					put(grammarAccess.getCreate_ChannelAccess().getTypeAlternatives_0_0(), "rule__Create_Channel__TypeAlternatives_0_0");
					put(grammarAccess.getFunctionTypeAccess().getAlternatives(), "rule__FunctionType__Alternatives");
					put(grammarAccess.getStatementAccess().getGroup(), "rule__Statement__Group__0");
					put(grammarAccess.getAtomicAccess().getGroup_0(), "rule__Atomic__Group_0__0");
					put(grammarAccess.getAtomicAccess().getGroup_1(), "rule__Atomic__Group_1__0");
					put(grammarAccess.getAtomicAccess().getGroup_2(), "rule__Atomic__Group_2__0");
					put(grammarAccess.getAtomicAccess().getGroup_3(), "rule__Atomic__Group_3__0");
					put(grammarAccess.getAtomicAccess().getGroup_4(), "rule__Atomic__Group_4__0");
					put(grammarAccess.getSourceAccess().getGroup(), "rule__Source__Group__0");
					put(grammarAccess.getSourceAccess().getGroup_1(), "rule__Source__Group_1__0");
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
					put(grammarAccess.getSelect_StatementAccess().getGroup(), "rule__Select_Statement__Group__0");
					put(grammarAccess.getSelect_StatementAccess().getGroup_2_1(), "rule__Select_Statement__Group_2_1__0");
					put(grammarAccess.getSelect_StatementAccess().getGroup_2_1_1(), "rule__Select_Statement__Group_2_1_1__0");
					put(grammarAccess.getSelect_StatementAccess().getGroup_4(), "rule__Select_Statement__Group_4__0");
					put(grammarAccess.getSelect_StatementAccess().getGroup_4_1(), "rule__Select_Statement__Group_4_1__0");
					put(grammarAccess.getSelect_StatementAccess().getGroup_5(), "rule__Select_Statement__Group_5__0");
					put(grammarAccess.getWindow_TimebasedAccess().getGroup(), "rule__Window_Timebased__Group__0");
					put(grammarAccess.getWindow_TimebasedAccess().getGroup_3(), "rule__Window_Timebased__Group_3__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup(), "rule__Window_Tuplebased__Group__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup_2(), "rule__Window_Tuplebased__Group_2__0");
					put(grammarAccess.getWindow_TuplebasedAccess().getGroup_4(), "rule__Window_Tuplebased__Group_4__0");
					put(grammarAccess.getConditionAccess().getGroup_0(), "rule__Condition__Group_0__0");
					put(grammarAccess.getConditionAccess().getGroup_1(), "rule__Condition__Group_1__0");
					put(grammarAccess.getCreate_StatementAccess().getGroup(), "rule__Create_Statement__Group__0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getGroup(), "rule__Create_AccessFramework__Group__0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getGroup_5(), "rule__Create_AccessFramework__Group_5__0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getGroup_17(), "rule__Create_AccessFramework__Group_17__0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getGroup_18(), "rule__Create_AccessFramework__Group_18__0");
					put(grammarAccess.getCreate_ChannelAccess().getGroup(), "rule__Create_Channel__Group__0");
					put(grammarAccess.getCreate_ChannelAccess().getGroup_5(), "rule__Create_Channel__Group_5__0");
					put(grammarAccess.getModelAccess().getStatementsAssignment(), "rule__Model__StatementsAssignment");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_0(), "rule__Statement__TypeAssignment_0_0");
					put(grammarAccess.getStatementAccess().getTypeAssignment_0_1(), "rule__Statement__TypeAssignment_0_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_0_1(), "rule__Atomic__ValueAssignment_0_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_1_1(), "rule__Atomic__ValueAssignment_1_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_2_1(), "rule__Atomic__ValueAssignment_2_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_3_1(), "rule__Atomic__ValueAssignment_3_1");
					put(grammarAccess.getAtomicAccess().getValueAssignment_4_1(), "rule__Atomic__ValueAssignment_4_1");
					put(grammarAccess.getAliasAccess().getNameAssignment(), "rule__Alias__NameAssignment");
					put(grammarAccess.getSourceAccess().getNameAssignment_0(), "rule__Source__NameAssignment_0");
					put(grammarAccess.getSourceAccess().getUnboundedAssignment_1_1_0(), "rule__Source__UnboundedAssignment_1_1_0");
					put(grammarAccess.getSourceAccess().getTimeAssignment_1_1_1(), "rule__Source__TimeAssignment_1_1_1");
					put(grammarAccess.getSourceAccess().getTupleAssignment_1_1_2(), "rule__Source__TupleAssignment_1_1_2");
					put(grammarAccess.getAttributeAccess().getNameAssignment(), "rule__Attribute__NameAssignment");
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
					put(grammarAccess.getSelect_StatementAccess().getNameAssignment_0(), "rule__Select_Statement__NameAssignment_0");
					put(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_0(), "rule__Select_Statement__AttributesAssignment_2_1_0");
					put(grammarAccess.getSelect_StatementAccess().getAttributesAssignment_2_1_1_1(), "rule__Select_Statement__AttributesAssignment_2_1_1_1");
					put(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_0(), "rule__Select_Statement__SourcesAssignment_4_0");
					put(grammarAccess.getSelect_StatementAccess().getSourcesAssignment_4_1_1(), "rule__Select_Statement__SourcesAssignment_4_1_1");
					put(grammarAccess.getSelect_StatementAccess().getPredicatesAssignment_5_1(), "rule__Select_Statement__PredicatesAssignment_5_1");
					put(grammarAccess.getWindow_TimebasedAccess().getSizeAssignment_1(), "rule__Window_Timebased__SizeAssignment_1");
					put(grammarAccess.getWindow_TimebasedAccess().getUnitAssignment_2(), "rule__Window_Timebased__UnitAssignment_2");
					put(grammarAccess.getWindow_TimebasedAccess().getAdvance_sizeAssignment_3_1(), "rule__Window_Timebased__Advance_sizeAssignment_3_1");
					put(grammarAccess.getWindow_TimebasedAccess().getAdvance_unitAssignment_3_2(), "rule__Window_Timebased__Advance_unitAssignment_3_2");
					put(grammarAccess.getWindow_TuplebasedAccess().getSizeAssignment_1(), "rule__Window_Tuplebased__SizeAssignment_1");
					put(grammarAccess.getWindow_TuplebasedAccess().getAdvance_sizeAssignment_2_1(), "rule__Window_Tuplebased__Advance_sizeAssignment_2_1");
					put(grammarAccess.getWindow_TuplebasedAccess().getPartition_attributeAssignment_4_2(), "rule__Window_Tuplebased__Partition_attributeAssignment_4_2");
					put(grammarAccess.getOperatorAccess().getNameAssignment(), "rule__Operator__NameAssignment");
					put(grammarAccess.getConditionAccess().getLeftAssignment_0_0(), "rule__Condition__LeftAssignment_0_0");
					put(grammarAccess.getConditionAccess().getRightAssignment_0_1(), "rule__Condition__RightAssignment_0_1");
					put(grammarAccess.getConditionAccess().getRightAssignment_1_0(), "rule__Condition__RightAssignment_1_0");
					put(grammarAccess.getConditionAccess().getLeftAssignment_1_1(), "rule__Condition__LeftAssignment_1_1");
					put(grammarAccess.getValueAccess().getNameAssignment(), "rule__Value__NameAssignment");
					put(grammarAccess.getScalar_FunctionAccess().getNameAssignment(), "rule__Scalar_Function__NameAssignment");
					put(grammarAccess.getCreate_StatementAccess().getChannelAssignment_1_0(), "rule__Create_Statement__ChannelAssignment_1_0");
					put(grammarAccess.getCreate_StatementAccess().getAccessframeworkAssignment_1_1(), "rule__Create_Statement__AccessframeworkAssignment_1_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getTypeAssignment_0(), "rule__Create_AccessFramework__TypeAssignment_0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getNameAssignment_1(), "rule__Create_AccessFramework__NameAssignment_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getAttributesAssignment_3(), "rule__Create_AccessFramework__AttributesAssignment_3");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getDatatypesAssignment_4(), "rule__Create_AccessFramework__DatatypesAssignment_4");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getAttributesAssignment_5_1(), "rule__Create_AccessFramework__AttributesAssignment_5_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getDatatypesAssignment_5_2(), "rule__Create_AccessFramework__DatatypesAssignment_5_2");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getWrapperAssignment_8(), "rule__Create_AccessFramework__WrapperAssignment_8");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getProtocolAssignment_10(), "rule__Create_AccessFramework__ProtocolAssignment_10");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getTransportAssignment_12(), "rule__Create_AccessFramework__TransportAssignment_12");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getDatahandlerAssignment_14(), "rule__Create_AccessFramework__DatahandlerAssignment_14");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getKeysAssignment_17_0(), "rule__Create_AccessFramework__KeysAssignment_17_0");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getValuesAssignment_17_1(), "rule__Create_AccessFramework__ValuesAssignment_17_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getKeysAssignment_18_1(), "rule__Create_AccessFramework__KeysAssignment_18_1");
					put(grammarAccess.getCreate_AccessFrameworkAccess().getValuesAssignment_18_2(), "rule__Create_AccessFramework__ValuesAssignment_18_2");
					put(grammarAccess.getCreate_ChannelAccess().getTypeAssignment_0(), "rule__Create_Channel__TypeAssignment_0");
					put(grammarAccess.getCreate_ChannelAccess().getNameAssignment_1(), "rule__Create_Channel__NameAssignment_1");
					put(grammarAccess.getCreate_ChannelAccess().getAttributesAssignment_3(), "rule__Create_Channel__AttributesAssignment_3");
					put(grammarAccess.getCreate_ChannelAccess().getDatatypesAssignment_4(), "rule__Create_Channel__DatatypesAssignment_4");
					put(grammarAccess.getCreate_ChannelAccess().getAttributesAssignment_5_1(), "rule__Create_Channel__AttributesAssignment_5_1");
					put(grammarAccess.getCreate_ChannelAccess().getDatatypesAssignment_5_2(), "rule__Create_Channel__DatatypesAssignment_5_2");
					put(grammarAccess.getCreate_ChannelAccess().getHostAssignment_8(), "rule__Create_Channel__HostAssignment_8");
					put(grammarAccess.getCreate_ChannelAccess().getPortAssignment_10(), "rule__Create_Channel__PortAssignment_10");
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
