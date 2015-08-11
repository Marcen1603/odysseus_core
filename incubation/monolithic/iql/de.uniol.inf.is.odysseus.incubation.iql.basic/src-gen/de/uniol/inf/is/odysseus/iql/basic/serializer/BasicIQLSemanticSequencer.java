package de.uniol.inf.is.odysseus.iql.basic.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.services.BasicIQLGrammarAccess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class BasicIQLSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private BasicIQLGrammarAccess grammarAccess;
	
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == BasicIQLPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case BasicIQLPackage.IQL_ADDITIVE_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0()) {
					sequence_IQLAdditiveExpression(context, (IQLAdditiveExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARGUMENTS_LIST:
				if(context == grammarAccess.getIQLArgumentsListRule()) {
					sequence_IQLArgumentsList(context, (IQLArgumentsList) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARGUMENTS_MAP:
				if(context == grammarAccess.getIQLArgumentsMapRule()) {
					sequence_IQLArgumentsMap(context, (IQLArgumentsMap) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARGUMENTS_MAP_KEY_VALUE:
				if(context == grammarAccess.getIQLArgumentsMapKeyValueRule()) {
					sequence_IQLArgumentsMapKeyValue(context, (IQLArgumentsMapKeyValue) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARRAY_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLMemberCallExpression(context, (IQLArrayExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARRAY_TYPE:
				if(context == grammarAccess.getIQLArrayTypeRule() ||
				   context == grammarAccess.getJvmTypeRule()) {
					sequence_IQLArrayType(context, (IQLArrayType) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ARRAY_TYPE_REF:
				if(context == grammarAccess.getIQLArrayTypeRefRule() ||
				   context == grammarAccess.getJvmTypeReferenceRule()) {
					sequence_IQLArrayTypeRef(context, (IQLArrayTypeRef) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ASSIGNMENT_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLExpressionRule()) {
					sequence_IQLAssignmentExpression(context, (IQLAssignmentExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_ATTRIBUTE:
				if(context == grammarAccess.getIQLAttributeRule()) {
					sequence_IQLAttribute(context, (IQLAttribute) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_BOOLEAN_NOT_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLBooleanNotExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_BREAK_STATEMENT:
				if(context == grammarAccess.getIQLBreakStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLBreakStatement(context, (IQLBreakStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_CASE_PART:
				if(context == grammarAccess.getIQLCasePartRule()) {
					sequence_IQLCasePart(context, (IQLCasePart) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_CLASS:
				if(context == grammarAccess.getIQLClassRule()) {
					sequence_IQLClass(context, (IQLClass) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT:
				if(context == grammarAccess.getIQLConstructorCallStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLConstructorCallStatement(context, (IQLConstructorCallStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_CONTINUE_STATEMENT:
				if(context == grammarAccess.getIQLContinueStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLContinueStatement(context, (IQLContinueStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_DO_WHILE_STATEMENT:
				if(context == grammarAccess.getIQLDoWhileStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLDoWhileStatement(context, (IQLDoWhileStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_EQUALITY_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLEqualityExpression(context, (IQLEqualityExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_EXPRESSION_STATEMENT:
				if(context == grammarAccess.getIQLExpressionStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLExpressionStatement(context, (IQLExpressionStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_FOR_EACH_STATEMENT:
				if(context == grammarAccess.getIQLForEachStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLForEachStatement(context, (IQLForEachStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_FOR_STATEMENT:
				if(context == grammarAccess.getIQLForStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLForStatement(context, (IQLForStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_IF_STATEMENT:
				if(context == grammarAccess.getIQLIfStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLIfStatement(context, (IQLIfStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_INSTANCE_OF_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0()) {
					sequence_IQLRelationalExpression(context, (IQLInstanceOfExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_INTERFACE:
				if(context == grammarAccess.getIQLInterfaceRule()) {
					sequence_IQLInterface(context, (IQLInterface) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JAVA:
				if(context == grammarAccess.getIQLJavaRule()) {
					sequence_IQLJava(context, (IQLJava) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JAVA_KEYWORDS:
				if(context == grammarAccess.getIQLJavaKeywordsRule()) {
					sequence_IQLJavaKeywords(context, (IQLJavaKeywords) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JAVA_MEMBER:
				if(context == grammarAccess.getIQLJavaMemberRule()) {
					sequence_IQLJavaMember(context, (IQLJavaMember) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JAVA_METADATA:
				if(context == grammarAccess.getIQLJavaMetadataRule()) {
					sequence_IQLJavaMetadata(context, (IQLJavaMetadata) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JAVA_STATEMENT:
				if(context == grammarAccess.getIQLJavaStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLJavaStatement(context, (IQLJavaStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_JVM_ELEMENT_CALL_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLOtherExpressions(context, (IQLJvmElementCallExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_BOOLEAN:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionBoolean) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_DOUBLE:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionDouble) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_INT:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionInt) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_LIST:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionListRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpressionList(context, (IQLLiteralExpressionList) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionMapRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpressionMap(context, (IQLLiteralExpressionMap) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE:
				if(context == grammarAccess.getIQLLiteralExpressionMapKeyValueRule()) {
					sequence_IQLLiteralExpressionMapKeyValue(context, (IQLLiteralExpressionMapKeyValue) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_NULL:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionNull) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_RANGE:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionRange) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_STRING:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLiteralExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionString) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LOGICAL_AND_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLLogicalAndExpression(context, (IQLLogicalAndExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LOGICAL_OR_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLLogicalOrExpression(context, (IQLLogicalOrExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_MEMBER_SELECTION:
				if(context == grammarAccess.getIQLMemberSelectionRule()) {
					sequence_IQLMemberSelection(context, (IQLMemberSelection) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLMemberCallExpression(context, (IQLMemberSelectionExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA:
				if(context == grammarAccess.getIQLMetadataRule()) {
					sequence_IQLMetadata(context, (IQLMetadata) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_LIST:
				if(context == grammarAccess.getIQLMetadataListRule()) {
					sequence_IQLMetadataList(context, (IQLMetadataList) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_LIST:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueListRule()) {
					sequence_IQLMetadataValueList(context, (IQLMetadataValueList) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_MAP:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueMapRule()) {
					sequence_IQLMetadataValueMap(context, (IQLMetadataValueMap) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_MAP_ELEMENT:
				if(context == grammarAccess.getIQLMetadataValueMapElementRule()) {
					sequence_IQLMetadataValueMapElement(context, (IQLMetadataValueMapElement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_BOOLEAN:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleBoolean) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_DOUBLE:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleDouble) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_INT:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleInt) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_NULL:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleNull) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_STRING:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleString) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_TYPE_REF:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleTypeRef) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METHOD:
				if(context == grammarAccess.getIQLMethodRule()) {
					sequence_IQLMethod(context, (IQLMethod) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_METHOD_DECLARATION_MEMBER:
				if(context == grammarAccess.getIQLMethodDeclarationMemberRule()) {
					sequence_IQLMethodDeclarationMember(context, (IQLMethodDeclarationMember) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_MODEL:
				if(context == grammarAccess.getIQLModelRule()) {
					sequence_IQLModel(context, (IQLModel) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_MULTIPLICATIVE_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0()) {
					sequence_IQLMultiplicativeExpression(context, (IQLMultiplicativeExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_NAMESPACE:
				if(context == grammarAccess.getIQLNamespaceRule()) {
					sequence_IQLNamespace(context, (IQLNamespace) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_NEW_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLOtherExpressions(context, (IQLNewExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_PARENTHESIS_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLOtherExpressions(context, (IQLParenthesisExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_PLUS_MINUS_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLPlusMinusExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_POSTFIX_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLPostfixExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_PREFIX_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLPrefixExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_RELATIONAL_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0()) {
					sequence_IQLRelationalExpression(context, (IQLRelationalExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_RETURN_STATEMENT:
				if(context == grammarAccess.getIQLReturnStatementRule() ||
				   context == grammarAccess.getIQLStatementRule()) {
					sequence_IQLReturnStatement(context, (IQLReturnStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_SIMPLE_TYPE:
				if(context == grammarAccess.getIQLSimpleTypeRule() ||
				   context == grammarAccess.getJvmTypeRule()) {
					sequence_IQLSimpleType(context, (IQLSimpleType) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_SIMPLE_TYPE_REF:
				if(context == grammarAccess.getIQLSimpleTypeRefRule() ||
				   context == grammarAccess.getJvmTypeReferenceRule()) {
					sequence_IQLSimpleTypeRef(context, (IQLSimpleTypeRef) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_STATEMENT_BLOCK:
				if(context == grammarAccess.getIQLStatementRule() ||
				   context == grammarAccess.getIQLStatementBlockRule()) {
					sequence_IQLStatementBlock(context, (IQLStatementBlock) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_SUPER_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLOtherExpressions(context, (IQLSuperExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_SWITCH_STATEMENT:
				if(context == grammarAccess.getIQLStatementRule() ||
				   context == grammarAccess.getIQLSwitchStatementRule()) {
					sequence_IQLSwitchStatement(context, (IQLSwitchStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_THIS_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionRule() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLOtherExpressionsRule() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLOtherExpressions(context, (IQLThisExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION:
				if(context == grammarAccess.getIQLAdditiveExpressionRule() ||
				   context == grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLEqualityExpressionRule() ||
				   context == grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionRule() ||
				   context == grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLTypeCastExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TYPE_DEFINITION:
				if(context == grammarAccess.getIQLTypeDefinitionRule()) {
					sequence_IQLTypeDefinition(context, (IQLTypeDefinition) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_VARIABLE_DECLARATION:
				if(context == grammarAccess.getIQLVariableDeclarationRule()) {
					sequence_IQLVariableDeclaration(context, (IQLVariableDeclaration) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_VARIABLE_INITIALIZATION:
				if(context == grammarAccess.getIQLVariableInitializationRule()) {
					sequence_IQLVariableInitialization(context, (IQLVariableInitialization) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_VARIABLE_STATEMENT:
				if(context == grammarAccess.getIQLStatementRule() ||
				   context == grammarAccess.getIQLVariableStatementRule()) {
					sequence_IQLVariableStatement(context, (IQLVariableStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_WHILE_STATEMENT:
				if(context == grammarAccess.getIQLStatementRule() ||
				   context == grammarAccess.getIQLWhileStatementRule()) {
					sequence_IQLWhileStatement(context, (IQLWhileStatement) semanticObject); 
					return; 
				}
				else break;
			}
		else if(semanticObject.eClass().getEPackage() == TypesPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case TypesPackage.JVM_FORMAL_PARAMETER:
				if(context == grammarAccess.getJvmFormalParameterRule()) {
					sequence_JvmFormalParameter(context, (JvmFormalParameter) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLAdditiveExpression_IQLAdditiveExpression_1_0_0_0 op=OpAdd rightOperand=IQLMultiplicativeExpression)
	 */
	protected void sequence_IQLAdditiveExpression(EObject context, IQLAdditiveExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ADDITIVE_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLAdditiveExpressionAccess().getIQLAdditiveExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLAdditiveExpressionAccess().getOpOpAddParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLAdditiveExpressionAccess().getRightOperandIQLMultiplicativeExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLExpression elements+=IQLExpression*)?)
	 */
	protected void sequence_IQLArgumentsList(EObject context, IQLArgumentsList semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (key=ID value=IQLExpression)
	 */
	protected void sequence_IQLArgumentsMapKeyValue(EObject context, IQLArgumentsMapKeyValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ARGUMENTS_MAP_KEY_VALUE__KEY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ARGUMENTS_MAP_KEY_VALUE__KEY));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ARGUMENTS_MAP_KEY_VALUE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ARGUMENTS_MAP_KEY_VALUE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLArgumentsMapKeyValueAccess().getKeyIDTerminalRuleCall_0_0(), semanticObject.getKey());
		feeder.accept(grammarAccess.getIQLArgumentsMapKeyValueAccess().getValueIQLExpressionParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLArgumentsMapKeyValue elements+=IQLArgumentsMapKeyValue*)?)
	 */
	protected void sequence_IQLArgumentsMap(EObject context, IQLArgumentsMap semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     type=IQLArrayType
	 */
	protected void sequence_IQLArrayTypeRef(EObject context, IQLArrayTypeRef semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ARRAY_TYPE_REF__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ARRAY_TYPE_REF__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLArrayTypeRefAccess().getTypeIQLArrayTypeParserRuleCall_0(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (type=[JvmType|QualifiedName] dimensions+=ArrayBrackets+)
	 */
	protected void sequence_IQLArrayType(EObject context, IQLArrayType semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLAssignmentExpression_IQLAssignmentExpression_1_0_0_0 op=OpAssign rightOperand=IQLAssignmentExpression)
	 */
	protected void sequence_IQLAssignmentExpression(EObject context, IQLAssignmentExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_ASSIGNMENT_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLAssignmentExpressionAccess().getOpOpAssignParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLAssignmentExpressionAccess().getRightOperandIQLAssignmentExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (type=JvmTypeReference simpleName=ID init=IQLVariableInitialization?)
	 */
	protected void sequence_IQLAttribute(EObject context, IQLAttribute semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     {IQLBreakStatement}
	 */
	protected void sequence_IQLBreakStatement(EObject context, IQLBreakStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (expr=IQLLiteralExpression statements+=IQLStatement*)
	 */
	protected void sequence_IQLCasePart(EObject context, IQLCasePart semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         simpleName=ID 
	 *         extendedClass=JvmTypeReference? 
	 *         (extendedInterfaces+=JvmTypeReference extendedInterfaces+=JvmTypeReference?)? 
	 *         (members+=IQLAttribute | members+=IQLMethod | members+=IQLJavaMember)*
	 *     )
	 */
	protected void sequence_IQLClass(EObject context, IQLClass semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((keyword='super' | keyword='this') args=IQLArgumentsList)
	 */
	protected void sequence_IQLConstructorCallStatement(EObject context, IQLConstructorCallStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     {IQLContinueStatement}
	 */
	protected void sequence_IQLContinueStatement(EObject context, IQLContinueStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (body=IQLStatement predicate=IQLExpression)
	 */
	protected void sequence_IQLDoWhileStatement(EObject context, IQLDoWhileStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_DO_WHILE_STATEMENT__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_DO_WHILE_STATEMENT__BODY));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_DO_WHILE_STATEMENT__PREDICATE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_DO_WHILE_STATEMENT__PREDICATE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLDoWhileStatementAccess().getBodyIQLStatementParserRuleCall_2_0(), semanticObject.getBody());
		feeder.accept(grammarAccess.getIQLDoWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_5_0(), semanticObject.getPredicate());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLEqualityExpression_IQLEqualityExpression_1_0_0_0 op=OpEquality rightOperand=IQLRelationalExpression)
	 */
	protected void sequence_IQLEqualityExpression(EObject context, IQLEqualityExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_EQUALITY_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLEqualityExpressionAccess().getIQLEqualityExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLEqualityExpressionAccess().getOpOpEqualityParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLEqualityExpressionAccess().getRightOperandIQLRelationalExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     expression=IQLExpression
	 */
	protected void sequence_IQLExpressionStatement(EObject context, IQLExpressionStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_EXPRESSION_STATEMENT__EXPRESSION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_EXPRESSION_STATEMENT__EXPRESSION));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLExpressionStatementAccess().getExpressionIQLExpressionParserRuleCall_1_0(), semanticObject.getExpression());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (var=IQLVariableDeclaration forExpression=IQLExpression body=IQLStatement)
	 */
	protected void sequence_IQLForEachStatement(EObject context, IQLForEachStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__VAR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__VAR));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT__BODY));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLForEachStatementAccess().getVarIQLVariableDeclarationParserRuleCall_3_0(), semanticObject.getVar());
		feeder.accept(grammarAccess.getIQLForEachStatementAccess().getForExpressionIQLExpressionParserRuleCall_5_0(), semanticObject.getForExpression());
		feeder.accept(grammarAccess.getIQLForEachStatementAccess().getBodyIQLStatementParserRuleCall_7_0(), semanticObject.getBody());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (var=IQLVariableStatement predicate=IQLExpressionStatement updateExpr=IQLExpression body=IQLStatement)
	 */
	protected void sequence_IQLForStatement(EObject context, IQLForStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__VAR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__VAR));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__PREDICATE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__PREDICATE));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__UPDATE_EXPR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__UPDATE_EXPR));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_FOR_STATEMENT__BODY));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLForStatementAccess().getVarIQLVariableStatementParserRuleCall_3_0(), semanticObject.getVar());
		feeder.accept(grammarAccess.getIQLForStatementAccess().getPredicateIQLExpressionStatementParserRuleCall_4_0(), semanticObject.getPredicate());
		feeder.accept(grammarAccess.getIQLForStatementAccess().getUpdateExprIQLExpressionParserRuleCall_5_0(), semanticObject.getUpdateExpr());
		feeder.accept(grammarAccess.getIQLForStatementAccess().getBodyIQLStatementParserRuleCall_7_0(), semanticObject.getBody());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (predicate=IQLExpression thenBody=IQLStatement elseBody=IQLStatement?)
	 */
	protected void sequence_IQLIfStatement(EObject context, IQLIfStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         simpleName=ID 
	 *         (extendedInterfaces+=JvmTypeReference extendedInterfaces+=JvmTypeReference?)? 
	 *         (members+=IQLMethodDeclarationMember | members+=IQLJavaMember)*
	 *     )
	 */
	protected void sequence_IQLInterface(EObject context, IQLInterface semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         keyword='break' | 
	 *         keyword='case' | 
	 *         keyword='class' | 
	 *         keyword='continue' | 
	 *         keyword='default' | 
	 *         keyword='do' | 
	 *         keyword='else' | 
	 *         keyword='extends' | 
	 *         keyword='for' | 
	 *         keyword='if' | 
	 *         keyword='implements' | 
	 *         keyword='instanceof' | 
	 *         keyword='interface' | 
	 *         keyword='new' | 
	 *         keyword='package' | 
	 *         keyword='return' | 
	 *         keyword='super' | 
	 *         keyword='switch' | 
	 *         keyword='this' | 
	 *         keyword='while' | 
	 *         keyword='abstract' | 
	 *         keyword='assert' | 
	 *         keyword='catch' | 
	 *         keyword='const' | 
	 *         keyword='enum' | 
	 *         keyword='final' | 
	 *         keyword='finally' | 
	 *         keyword='goto' | 
	 *         keyword='import' | 
	 *         keyword='native' | 
	 *         keyword='private' | 
	 *         keyword='protected' | 
	 *         keyword='public' | 
	 *         keyword='static' | 
	 *         keyword='synchronized' | 
	 *         keyword='throw' | 
	 *         keyword='throws' | 
	 *         keyword='transient' | 
	 *         keyword='try' | 
	 *         keyword='volatile' | 
	 *         keyword='strictfp'
	 *     )
	 */
	protected void sequence_IQLJavaKeywords(EObject context, IQLJavaKeywords semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     text=IQLJava
	 */
	protected void sequence_IQLJavaMember(EObject context, IQLJavaMember semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     text=IQLJava
	 */
	protected void sequence_IQLJavaMetadata(EObject context, IQLJavaMetadata semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_JAVA_METADATA__TEXT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_JAVA_METADATA__TEXT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLJavaMetadataAccess().getTextIQLJavaParserRuleCall_2_0(), semanticObject.getText());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     text=IQLJava
	 */
	protected void sequence_IQLJavaStatement(EObject context, IQLJavaStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_JAVA_STATEMENT__TEXT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_JAVA_STATEMENT__TEXT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLJavaStatementAccess().getTextIQLJavaParserRuleCall_2_0(), semanticObject.getText());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((text+=IQLJavaText | keywords+=IQLJavaKeywords)*)
	 */
	protected void sequence_IQLJava(EObject context, IQLJava semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLExpression elements+=IQLExpression*)?)
	 */
	protected void sequence_IQLLiteralExpressionList(EObject context, IQLLiteralExpressionList semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (key=IQLExpression value=IQLExpression)
	 */
	protected void sequence_IQLLiteralExpressionMapKeyValue(EObject context, IQLLiteralExpressionMapKeyValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getKeyIQLExpressionParserRuleCall_0_0(), semanticObject.getKey());
		feeder.accept(grammarAccess.getIQLLiteralExpressionMapKeyValueAccess().getValueIQLExpressionParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLLiteralExpressionMapKeyValue elements+=IQLLiteralExpressionMapKeyValue*)?)
	 */
	protected void sequence_IQLLiteralExpressionMap(EObject context, IQLLiteralExpressionMap semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=BOOLEAN
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionBoolean semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_BOOLEAN__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_BOOLEAN__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionAccess().getValueBOOLEANTerminalRuleCall_3_1_0(), semanticObject.isValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=DOUBLE
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionDouble semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_DOUBLE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_DOUBLE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionAccess().getValueDOUBLETerminalRuleCall_1_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=INT
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionInt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_INT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_INT__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionAccess().getValueINTTerminalRuleCall_0_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     {IQLLiteralExpressionNull}
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionNull semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=RANGE
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionRange semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_RANGE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_RANGE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionAccess().getValueRANGETerminalRuleCall_4_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=STRING
	 */
	protected void sequence_IQLLiteralExpression(EObject context, IQLLiteralExpressionString semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_STRING__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_STRING__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLiteralExpressionAccess().getValueSTRINGTerminalRuleCall_2_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLLogicalAndExpression_IQLLogicalAndExpression_1_0_0_0 op=OpLogicalAnd rightOperand=IQLEqualityExpression)
	 */
	protected void sequence_IQLLogicalAndExpression(EObject context, IQLLogicalAndExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_AND_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLogicalAndExpressionAccess().getIQLLogicalAndExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLLogicalAndExpressionAccess().getOpOpLogicalAndParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLLogicalAndExpressionAccess().getRightOperandIQLEqualityExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLLogicalOrExpression_IQLLogicalOrExpression_1_0_0_0 op=OpLogicalOr rightOperand=IQLLogicalAndExpression)
	 */
	protected void sequence_IQLLogicalOrExpression(EObject context, IQLLogicalOrExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_LOGICAL_OR_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLLogicalOrExpressionAccess().getOpOpLogicalOrParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLLogicalOrExpressionAccess().getRightOperandIQLLogicalAndExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLMemberCallExpression_IQLArrayExpression_1_0_0_0 expressions+=IQLExpression expressions+=IQLExpression?)
	 */
	protected void sequence_IQLMemberCallExpression(EObject context, IQLArrayExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLMemberCallExpression_IQLMemberSelectionExpression_1_1_0_0_0 sel=IQLMemberSelection)
	 */
	protected void sequence_IQLMemberCallExpression(EObject context, IQLMemberSelectionExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_MEMBER_SELECTION_EXPRESSION__SEL) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_MEMBER_SELECTION_EXPRESSION__SEL));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_1_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLMemberCallExpressionAccess().getSelIQLMemberSelectionParserRuleCall_1_1_1_0(), semanticObject.getSel());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (member=[JvmMember|ID] args=IQLArgumentsList?)
	 */
	protected void sequence_IQLMemberSelection(EObject context, IQLMemberSelection semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (elements+=IQLMetadata elements+=IQLMetadata*)
	 */
	protected void sequence_IQLMetadataList(EObject context, IQLMetadataList semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLMetadataValue elements+=IQLMetadataValue*)?)
	 */
	protected void sequence_IQLMetadataValueList(EObject context, IQLMetadataValueList semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (key=IQLMetadataValue value=IQLMetadataValue)
	 */
	protected void sequence_IQLMetadataValueMapElement(EObject context, IQLMetadataValueMapElement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_MAP_ELEMENT__KEY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_MAP_ELEMENT__KEY));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_MAP_ELEMENT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_MAP_ELEMENT__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueMapElementAccess().getKeyIQLMetadataValueParserRuleCall_0_0(), semanticObject.getKey());
		feeder.accept(grammarAccess.getIQLMetadataValueMapElementAccess().getValueIQLMetadataValueParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     ((elements+=IQLMetadataValueMapElement elements+=IQLMetadataValueMapElement*)?)
	 */
	protected void sequence_IQLMetadataValueMap(EObject context, IQLMetadataValueMap semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=BOOLEAN
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleBoolean semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_BOOLEAN__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_BOOLEAN__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueBOOLEANTerminalRuleCall_3_1_0(), semanticObject.isValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=DOUBLE
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleDouble semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_DOUBLE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_DOUBLE__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueDOUBLETerminalRuleCall_1_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=INT
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleInt semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_INT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_INT__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueINTTerminalRuleCall_0_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value='null'
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleNull semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_NULL__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_NULL__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueNullKeyword_5_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=STRING
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleString semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_STRING__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_STRING__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueSTRINGTerminalRuleCall_2_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     value=JvmTypeReference
	 */
	protected void sequence_IQLMetadataValueSingle(EObject context, IQLMetadataValueSingleTypeRef semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_TYPE_REF__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_METADATA_VALUE_SINGLE_TYPE_REF__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMetadataValueSingleAccess().getValueJvmTypeReferenceParserRuleCall_4_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID value=IQLMetadataValue?)
	 */
	protected void sequence_IQLMetadata(EObject context, IQLMetadata semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (simpleName=ID (parameters+=JvmFormalParameter parameters+=JvmFormalParameter*)? returnType=JvmTypeReference?)
	 */
	protected void sequence_IQLMethodDeclarationMember(EObject context, IQLMethodDeclarationMember semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         override?='override'? 
	 *         simpleName=ID 
	 *         (parameters+=JvmFormalParameter parameters+=JvmFormalParameter*)? 
	 *         returnType=JvmTypeReference? 
	 *         body=IQLStatementBlock
	 *     )
	 */
	protected void sequence_IQLMethod(EObject context, IQLMethod semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=QualifiedName? namespaces+=IQLNamespace* elements+=IQLTypeDefinition*)
	 */
	protected void sequence_IQLModel(EObject context, IQLModel semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLMultiplicativeExpression_IQLMultiplicativeExpression_1_0_0_0 op=OpMulti rightOperand=IQLUnaryExpression)
	 */
	protected void sequence_IQLMultiplicativeExpression(EObject context, IQLMultiplicativeExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_MULTIPLICATIVE_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLMultiplicativeExpressionAccess().getOpOpMultiParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLMultiplicativeExpressionAccess().getRightOperandIQLUnaryExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     importedNamespace=QualifiedNameWithWildcard
	 */
	protected void sequence_IQLNamespace(EObject context, IQLNamespace semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_NAMESPACE__IMPORTED_NAMESPACE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_NAMESPACE__IMPORTED_NAMESPACE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLNamespaceAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_1_0(), semanticObject.getImportedNamespace());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (element=[JvmIdentifiableElement|QualifiedName] args=IQLArgumentsList?)
	 */
	protected void sequence_IQLOtherExpressions(EObject context, IQLJvmElementCallExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (ref=IQLArrayTypeRef | (ref=IQLSimpleTypeRef argsList=IQLArgumentsList argsMap=IQLArgumentsMap?))
	 */
	protected void sequence_IQLOtherExpressions(EObject context, IQLNewExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     expr=IQLExpression
	 */
	protected void sequence_IQLOtherExpressions(EObject context, IQLParenthesisExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_PARENTHESIS_EXPRESSION__EXPR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_PARENTHESIS_EXPRESSION__EXPR));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLOtherExpressionsAccess().getExprIQLExpressionParserRuleCall_3_2_0(), semanticObject.getExpr());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     {IQLSuperExpression}
	 */
	protected void sequence_IQLOtherExpressions(EObject context, IQLSuperExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     {IQLThisExpression}
	 */
	protected void sequence_IQLOtherExpressions(EObject context, IQLThisExpression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLRelationalExpression_IQLInstanceOfExpression_1_0_0_0_0 targetRef=JvmTypeReference)
	 */
	protected void sequence_IQLRelationalExpression(EObject context, IQLInstanceOfExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLRelationalExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_1_0_1_0(), semanticObject.getTargetRef());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLRelationalExpression_IQLRelationalExpression_1_1_0_0_0 op=OpRelational rightOperand=IQLAdditiveExpression)
	 */
	protected void sequence_IQLRelationalExpression(EObject context, IQLRelationalExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLRelationalExpressionAccess().getOpOpRelationalParserRuleCall_1_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLRelationalExpressionAccess().getRightOperandIQLAdditiveExpressionParserRuleCall_1_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     expression=IQLExpression
	 */
	protected void sequence_IQLReturnStatement(EObject context, IQLReturnStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_RETURN_STATEMENT__EXPRESSION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_RETURN_STATEMENT__EXPRESSION));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLReturnStatementAccess().getExpressionIQLExpressionParserRuleCall_2_0(), semanticObject.getExpression());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     type=IQLSimpleType
	 */
	protected void sequence_IQLSimpleTypeRef(EObject context, IQLSimpleTypeRef semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_SIMPLE_TYPE_REF__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_SIMPLE_TYPE_REF__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLSimpleTypeRefAccess().getTypeIQLSimpleTypeParserRuleCall_0(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     type=[JvmType|QualifiedName]
	 */
	protected void sequence_IQLSimpleType(EObject context, IQLSimpleType semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_SIMPLE_TYPE__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_SIMPLE_TYPE__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLSimpleTypeAccess().getTypeJvmTypeQualifiedNameParserRuleCall_0_1(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (statements+=IQLStatement*)
	 */
	protected void sequence_IQLStatementBlock(EObject context, IQLStatementBlock semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (expr=IQLExpression cases+=IQLCasePart* statements+=IQLStatement*)
	 */
	protected void sequence_IQLSwitchStatement(EObject context, IQLSwitchStatement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (javametadata+=IQLJavaMetadata* (inner=IQLClass | inner=IQLInterface))
	 */
	protected void sequence_IQLTypeDefinition(EObject context, IQLTypeDefinition semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (op=OpUnaryBooleanNot operand=IQLMemberCallExpression)
	 */
	protected void sequence_IQLUnaryExpression(EObject context, IQLBooleanNotExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_BOOLEAN_NOT_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_BOOLEAN_NOT_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_BOOLEAN_NOT_EXPRESSION__OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_BOOLEAN_NOT_EXPRESSION__OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryBooleanNotParserRuleCall_1_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_1_1_0(), semanticObject.getOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (op=OpUnaryPlusMinus operand=IQLMemberCallExpression)
	 */
	protected void sequence_IQLUnaryExpression(EObject context, IQLPlusMinusExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_PLUS_MINUS_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_PLUS_MINUS_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_PLUS_MINUS_EXPRESSION__OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_PLUS_MINUS_EXPRESSION__OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOpOpUnaryPlusMinusParserRuleCall_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_0_1_0(), semanticObject.getOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (operand=IQLUnaryExpression_IQLPostfixExpression_4_1_0_0 op=OpPostfix)
	 */
	protected void sequence_IQLUnaryExpression(EObject context, IQLPostfixExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_POSTFIX_EXPRESSION__OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_POSTFIX_EXPRESSION__OPERAND));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_POSTFIX_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_POSTFIX_EXPRESSION__OP));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0(), semanticObject.getOperand());
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPostfixParserRuleCall_4_1_0_1_0(), semanticObject.getOp());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (op=OpPrefix operand=IQLMemberCallExpression)
	 */
	protected void sequence_IQLUnaryExpression(EObject context, IQLPrefixExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_PREFIX_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_PREFIX_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_PREFIX_EXPRESSION__OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_PREFIX_EXPRESSION__OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOpOpPrefixParserRuleCall_2_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_2_1_0(), semanticObject.getOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (targetRef=JvmTypeReference operand=IQLMemberCallExpression)
	 */
	protected void sequence_IQLUnaryExpression(EObject context, IQLTypeCastExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_TYPE_CAST_EXPRESSION__TARGET_REF) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_TYPE_CAST_EXPRESSION__TARGET_REF));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_TYPE_CAST_EXPRESSION__OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_TYPE_CAST_EXPRESSION__OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getTargetRefJvmTypeReferenceParserRuleCall_3_0_0_2_0(), semanticObject.getTargetRef());
		feeder.accept(grammarAccess.getIQLUnaryExpressionAccess().getOperandIQLMemberCallExpressionParserRuleCall_3_1_0(), semanticObject.getOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (ref=JvmTypeReference name=ID)
	 */
	protected void sequence_IQLVariableDeclaration(EObject context, IQLVariableDeclaration semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_DECLARATION__REF) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_DECLARATION__REF));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_DECLARATION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_DECLARATION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLVariableDeclarationAccess().getRefJvmTypeReferenceParserRuleCall_1_0(), semanticObject.getRef());
		feeder.accept(grammarAccess.getIQLVariableDeclarationAccess().getNameIDTerminalRuleCall_2_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (argsList=IQLArgumentsList argsMap=IQLArgumentsMap?)
	 */
	protected void sequence_IQLVariableInitialization(EObject context, IQLVariableInitialization semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (var=IQLVariableDeclaration init=IQLVariableInitialization)
	 */
	protected void sequence_IQLVariableStatement(EObject context, IQLVariableStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_STATEMENT__VAR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_STATEMENT__VAR));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_STATEMENT__INIT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_VARIABLE_STATEMENT__INIT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLVariableStatementAccess().getVarIQLVariableDeclarationParserRuleCall_1_0(), semanticObject.getVar());
		feeder.accept(grammarAccess.getIQLVariableStatementAccess().getInitIQLVariableInitializationParserRuleCall_2_0(), semanticObject.getInit());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (predicate=IQLExpression body=IQLStatement)
	 */
	protected void sequence_IQLWhileStatement(EObject context, IQLWhileStatement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_WHILE_STATEMENT__PREDICATE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_WHILE_STATEMENT__PREDICATE));
			if(transientValues.isValueTransient(semanticObject, BasicIQLPackage.Literals.IQL_WHILE_STATEMENT__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, BasicIQLPackage.Literals.IQL_WHILE_STATEMENT__BODY));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLWhileStatementAccess().getPredicateIQLExpressionParserRuleCall_3_0(), semanticObject.getPredicate());
		feeder.accept(grammarAccess.getIQLWhileStatementAccess().getBodyIQLStatementParserRuleCall_5_0(), semanticObject.getBody());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (parameterType=JvmTypeReference name=ID)
	 */
	protected void sequence_JvmFormalParameter(EObject context, JvmFormalParameter semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
