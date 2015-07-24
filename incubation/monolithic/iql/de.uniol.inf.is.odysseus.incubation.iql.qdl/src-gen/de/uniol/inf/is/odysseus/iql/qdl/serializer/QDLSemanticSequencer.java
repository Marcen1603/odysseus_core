package de.uniol.inf.is.odysseus.iql.qdl.serializer;

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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleLong;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.serializer.BasicIQLSemanticSequencer;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFile;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.services.QDLGrammarAccess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class QDLSemanticSequencer extends BasicIQLSemanticSequencer {

	@Inject
	private QDLGrammarAccess grammarAccess;
	
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
			case BasicIQLPackage.IQL_ATTRIBUTE_SELECTION:
				if(context == grammarAccess.getIQLAttributeSelectionRule()) {
					sequence_IQLAttributeSelection(context, (IQLAttributeSelection) semanticObject); 
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
				if(context == grammarAccess.getIQLClassRule() ||
				   context == grammarAccess.getIQLTypeDefsRule() ||
				   context == grammarAccess.getQDLTypeDefsRule()) {
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
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
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
			case BasicIQLPackage.IQL_FILE:
				if(context == grammarAccess.getIQLFileRule()) {
					sequence_IQLFile(context, (IQLFile) semanticObject); 
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLRelationalExpression(context, (IQLInstanceOfExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_INTERFACE:
				if(context == grammarAccess.getIQLInterfaceRule() ||
				   context == grammarAccess.getIQLTypeDefsRule() ||
				   context == grammarAccess.getQDLTypeDefsRule()) {
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionBoolean) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LITERAL_EXPRESSION_CHAR:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLLiteralExpression(context, (IQLLiteralExpressionChar) semanticObject); 
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
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
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLLogicalAndExpression(context, (IQLLogicalAndExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_LOGICAL_OR_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionRule() ||
				   context == grammarAccess.getIQLLogicalOrExpressionAccess().getIQLLogicalOrExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLLogicalOrExpression(context, (IQLLogicalOrExpression) semanticObject); 
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_CHAR:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleChar) semanticObject); 
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
			case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_LONG:
				if(context == grammarAccess.getIQLMetadataValueRule() ||
				   context == grammarAccess.getIQLMetadataValueSingleRule()) {
					sequence_IQLMetadataValueSingle(context, (IQLMetadataValueSingleLong) semanticObject); 
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
			case BasicIQLPackage.IQL_METHOD_SELECTION:
				if(context == grammarAccess.getIQLMethodSelectionRule()) {
					sequence_IQLMethodSelection(context, (IQLMethodSelection) semanticObject); 
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
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
			case BasicIQLPackage.IQL_SWITCH_STATEMENT:
				if(context == grammarAccess.getIQLStatementRule() ||
				   context == grammarAccess.getIQLSwitchStatementRule()) {
					sequence_IQLSwitchStatement(context, (IQLSwitchStatement) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_NEW:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLTerminalExpression(context, (IQLTerminalExpressionNew) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_PARENTHESIS:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLTerminalExpression(context, (IQLTerminalExpressionParenthesis) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_SUPER:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLTerminalExpression(context, (IQLTerminalExpressionSuper) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_THIS:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLTerminalExpression(context, (IQLTerminalExpressionThis) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_VARIABLE:
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
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLArrayExpressionLeftOperandAction_1_0_0() ||
				   context == grammarAccess.getIQLMemberCallExpressionAccess().getIQLMemberSelectionExpressionLeftOperandAction_2_0_0_0() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionRule() ||
				   context == grammarAccess.getIQLMultiplicativeExpressionAccess().getIQLMultiplicativeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLTerminalExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionRule() ||
				   context == grammarAccess.getIQLUnaryExpressionAccess().getIQLPostfixExpressionOperandAction_4_1_0_0()) {
					sequence_IQLTerminalExpression(context, (IQLTerminalExpressionVariable) semanticObject); 
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
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionRule() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLInstanceOfExpressionLeftOperandAction_1_0_0_0_0() ||
				   context == grammarAccess.getIQLRelationalExpressionAccess().getIQLRelationalExpressionLeftOperandAction_1_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLUnaryExpressionRule()) {
					sequence_IQLUnaryExpression(context, (IQLTypeCastExpression) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_TYPE_DEF:
				if(context == grammarAccess.getIQLTypeDefRule()) {
					sequence_IQLTypeDef(context, (IQLTypeDef) semanticObject); 
					return; 
				}
				else break;
			case BasicIQLPackage.IQL_VARIABLE_DECLARATION:
				if(context == grammarAccess.getIQLFormalParameterRule() ||
				   context == grammarAccess.getJvmFormalParameterRule()) {
					sequence_IQLFormalParameter(context, (IQLVariableDeclaration) semanticObject); 
					return; 
				}
				else if(context == grammarAccess.getIQLVariableDeclarationRule()) {
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
		else if(semanticObject.eClass().getEPackage() == QDLPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case QDLPackage.IQL_PORT_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionRule() ||
				   context == grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLPortExpression(context, (IQLPortExpression) semanticObject); 
					return; 
				}
				else break;
			case QDLPackage.IQL_SUBSCRIBE_EXPRESSION:
				if(context == grammarAccess.getIQLAssignmentExpressionRule() ||
				   context == grammarAccess.getIQLAssignmentExpressionAccess().getIQLAssignmentExpressionLeftOperandAction_1_0_0_0() ||
				   context == grammarAccess.getIQLExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionRule() ||
				   context == grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0()) {
					sequence_IQLSubscribeExpression(context, (IQLSubscribeExpression) semanticObject); 
					return; 
				}
				else break;
			case QDLPackage.QDL_FILE:
				if(context == grammarAccess.getQDLFileRule()) {
					sequence_QDLFile(context, (QDLFile) semanticObject); 
					return; 
				}
				else break;
			case QDLPackage.QDL_QUERY:
				if(context == grammarAccess.getQDLQueryRule() ||
				   context == grammarAccess.getQDLTypeDefsRule()) {
					sequence_QDLQuery(context, (QDLQuery) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
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
	 *     (leftOperand=IQLPortExpression_IQLPortExpression_1_0_0_0 op=':' rightOperand=IQLLogicalOrExpression)
	 */
	protected void sequence_IQLPortExpression(EObject context, IQLPortExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_PORT_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLPortExpressionAccess().getIQLPortExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLPortExpressionAccess().getOpColonKeyword_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLPortExpressionAccess().getRightOperandIQLLogicalOrExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (leftOperand=IQLSubscribeExpression_IQLSubscribeExpression_1_0_0_0 op=IQLSubscribe rightOperand=IQLPortExpression)
	 */
	protected void sequence_IQLSubscribeExpression(EObject context, IQLSubscribeExpression semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__LEFT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__LEFT_OPERAND));
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__OP) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__OP));
			if(transientValues.isValueTransient(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__RIGHT_OPERAND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, QDLPackage.Literals.IQL_SUBSCRIBE_EXPRESSION__RIGHT_OPERAND));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIQLSubscribeExpressionAccess().getIQLSubscribeExpressionLeftOperandAction_1_0_0_0(), semanticObject.getLeftOperand());
		feeder.accept(grammarAccess.getIQLSubscribeExpressionAccess().getOpIQLSubscribeParserRuleCall_1_0_0_1_0(), semanticObject.getOp());
		feeder.accept(grammarAccess.getIQLSubscribeExpressionAccess().getRightOperandIQLPortExpressionParserRuleCall_1_1_0(), semanticObject.getRightOperand());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (namespaces+=IQLNamespace* elements+=QDLTypeDefs*)
	 */
	protected void sequence_QDLFile(EObject context, QDLFile semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (javametadata+=IQLJavaMetadata* simpleName=ID metadataList=IQLMetadataList? statements=IQLStatementBlock)
	 */
	protected void sequence_QDLQuery(EObject context, QDLQuery semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
