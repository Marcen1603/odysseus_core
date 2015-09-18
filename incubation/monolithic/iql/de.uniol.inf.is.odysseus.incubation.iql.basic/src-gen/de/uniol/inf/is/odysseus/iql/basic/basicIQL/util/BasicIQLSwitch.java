/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.util;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.xtext.common.types.JvmAnnotationTarget;
import org.eclipse.xtext.common.types.JvmComponentType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeParameterDeclarator;
import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage
 * @generated
 */
public class BasicIQLSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static BasicIQLPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasicIQLSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = BasicIQLPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case BasicIQLPackage.IQL_MODEL:
      {
        IQLModel iqlModel = (IQLModel)theEObject;
        T result = caseIQLModel(iqlModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_MODEL_ELEMENT:
      {
        IQLModelElement iqlModelElement = (IQLModelElement)theEObject;
        T result = caseIQLModelElement(iqlModelElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_NAMESPACE:
      {
        IQLNamespace iqlNamespace = (IQLNamespace)theEObject;
        T result = caseIQLNamespace(iqlNamespace);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_JAVA_METADATA:
      {
        IQLJavaMetadata iqlJavaMetadata = (IQLJavaMetadata)theEObject;
        T result = caseIQLJavaMetadata(iqlJavaMetadata);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_LIST:
      {
        IQLMetadataList iqlMetadataList = (IQLMetadataList)theEObject;
        T result = caseIQLMetadataList(iqlMetadataList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA:
      {
        IQLMetadata iqlMetadata = (IQLMetadata)theEObject;
        T result = caseIQLMetadata(iqlMetadata);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE:
      {
        IQLMetadataValue iqlMetadataValue = (IQLMetadataValue)theEObject;
        T result = caseIQLMetadataValue(iqlMetadataValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_MAP_ELEMENT:
      {
        IQLMetadataValueMapElement iqlMetadataValueMapElement = (IQLMetadataValueMapElement)theEObject;
        T result = caseIQLMetadataValueMapElement(iqlMetadataValueMapElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_VARIABLE_INITIALIZATION:
      {
        IQLVariableInitialization iqlVariableInitialization = (IQLVariableInitialization)theEObject;
        T result = caseIQLVariableInitialization(iqlVariableInitialization);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARGUMENTS_LIST:
      {
        IQLArgumentsList iqlArgumentsList = (IQLArgumentsList)theEObject;
        T result = caseIQLArgumentsList(iqlArgumentsList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARGUMENTS_MAP:
      {
        IQLArgumentsMap iqlArgumentsMap = (IQLArgumentsMap)theEObject;
        T result = caseIQLArgumentsMap(iqlArgumentsMap);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARGUMENTS_MAP_KEY_VALUE:
      {
        IQLArgumentsMapKeyValue iqlArgumentsMapKeyValue = (IQLArgumentsMapKeyValue)theEObject;
        T result = caseIQLArgumentsMapKeyValue(iqlArgumentsMapKeyValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_STATEMENT:
      {
        IQLStatement iqlStatement = (IQLStatement)theEObject;
        T result = caseIQLStatement(iqlStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_CASE_PART:
      {
        IQLCasePart iqlCasePart = (IQLCasePart)theEObject;
        T result = caseIQLCasePart(iqlCasePart);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_EXPRESSION:
      {
        IQLExpression iqlExpression = (IQLExpression)theEObject;
        T result = caseIQLExpression(iqlExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_MEMBER_SELECTION:
      {
        IQLMemberSelection iqlMemberSelection = (IQLMemberSelection)theEObject;
        T result = caseIQLMemberSelection(iqlMemberSelection);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE:
      {
        IQLLiteralExpressionMapKeyValue iqlLiteralExpressionMapKeyValue = (IQLLiteralExpressionMapKeyValue)theEObject;
        T result = caseIQLLiteralExpressionMapKeyValue(iqlLiteralExpressionMapKeyValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_JAVA:
      {
        IQLJava iqlJava = (IQLJava)theEObject;
        T result = caseIQLJava(iqlJava);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_CLASS:
      {
        IQLClass iqlClass = (IQLClass)theEObject;
        T result = caseIQLClass(iqlClass);
        if (result == null) result = caseJvmGenericType(iqlClass);
        if (result == null) result = caseJvmDeclaredType(iqlClass);
        if (result == null) result = caseJvmTypeParameterDeclarator(iqlClass);
        if (result == null) result = caseJvmMember(iqlClass);
        if (result == null) result = caseJvmComponentType(iqlClass);
        if (result == null) result = caseJvmAnnotationTarget(iqlClass);
        if (result == null) result = caseJvmType(iqlClass);
        if (result == null) result = caseJvmIdentifiableElement(iqlClass);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_INTERFACE:
      {
        IQLInterface iqlInterface = (IQLInterface)theEObject;
        T result = caseIQLInterface(iqlInterface);
        if (result == null) result = caseJvmGenericType(iqlInterface);
        if (result == null) result = caseJvmDeclaredType(iqlInterface);
        if (result == null) result = caseJvmTypeParameterDeclarator(iqlInterface);
        if (result == null) result = caseJvmMember(iqlInterface);
        if (result == null) result = caseJvmComponentType(iqlInterface);
        if (result == null) result = caseJvmAnnotationTarget(iqlInterface);
        if (result == null) result = caseJvmType(iqlInterface);
        if (result == null) result = caseJvmIdentifiableElement(iqlInterface);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ATTRIBUTE:
      {
        IQLAttribute iqlAttribute = (IQLAttribute)theEObject;
        T result = caseIQLAttribute(iqlAttribute);
        if (result == null) result = caseJvmField(iqlAttribute);
        if (result == null) result = caseJvmFeature(iqlAttribute);
        if (result == null) result = caseJvmMember(iqlAttribute);
        if (result == null) result = caseJvmAnnotationTarget(iqlAttribute);
        if (result == null) result = caseJvmIdentifiableElement(iqlAttribute);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_SIMPLE_TYPE_REF:
      {
        IQLSimpleTypeRef iqlSimpleTypeRef = (IQLSimpleTypeRef)theEObject;
        T result = caseIQLSimpleTypeRef(iqlSimpleTypeRef);
        if (result == null) result = caseJvmTypeReference(iqlSimpleTypeRef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARRAY_TYPE_REF:
      {
        IQLArrayTypeRef iqlArrayTypeRef = (IQLArrayTypeRef)theEObject;
        T result = caseIQLArrayTypeRef(iqlArrayTypeRef);
        if (result == null) result = caseJvmTypeReference(iqlArrayTypeRef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARRAY_TYPE:
      {
        IQLArrayType iqlArrayType = (IQLArrayType)theEObject;
        T result = caseIQLArrayType(iqlArrayType);
        if (result == null) result = caseJvmType(iqlArrayType);
        if (result == null) result = caseJvmIdentifiableElement(iqlArrayType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METHOD:
      {
        IQLMethod iqlMethod = (IQLMethod)theEObject;
        T result = caseIQLMethod(iqlMethod);
        if (result == null) result = caseJvmOperation(iqlMethod);
        if (result == null) result = caseJvmExecutable(iqlMethod);
        if (result == null) result = caseJvmFeature(iqlMethod);
        if (result == null) result = caseJvmTypeParameterDeclarator(iqlMethod);
        if (result == null) result = caseJvmMember(iqlMethod);
        if (result == null) result = caseJvmAnnotationTarget(iqlMethod);
        if (result == null) result = caseJvmIdentifiableElement(iqlMethod);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METHOD_DECLARATION:
      {
        IQLMethodDeclaration iqlMethodDeclaration = (IQLMethodDeclaration)theEObject;
        T result = caseIQLMethodDeclaration(iqlMethodDeclaration);
        if (result == null) result = caseJvmOperation(iqlMethodDeclaration);
        if (result == null) result = caseJvmExecutable(iqlMethodDeclaration);
        if (result == null) result = caseJvmFeature(iqlMethodDeclaration);
        if (result == null) result = caseJvmTypeParameterDeclarator(iqlMethodDeclaration);
        if (result == null) result = caseJvmMember(iqlMethodDeclaration);
        if (result == null) result = caseJvmAnnotationTarget(iqlMethodDeclaration);
        if (result == null) result = caseJvmIdentifiableElement(iqlMethodDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_JAVA_MEMBER:
      {
        IQLJavaMember iqlJavaMember = (IQLJavaMember)theEObject;
        T result = caseIQLJavaMember(iqlJavaMember);
        if (result == null) result = caseJvmMember(iqlJavaMember);
        if (result == null) result = caseJvmAnnotationTarget(iqlJavaMember);
        if (result == null) result = caseJvmIdentifiableElement(iqlJavaMember);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_INT:
      {
        IQLMetadataValueSingleInt iqlMetadataValueSingleInt = (IQLMetadataValueSingleInt)theEObject;
        T result = caseIQLMetadataValueSingleInt(iqlMetadataValueSingleInt);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleInt);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_DOUBLE:
      {
        IQLMetadataValueSingleDouble iqlMetadataValueSingleDouble = (IQLMetadataValueSingleDouble)theEObject;
        T result = caseIQLMetadataValueSingleDouble(iqlMetadataValueSingleDouble);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleDouble);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_STRING:
      {
        IQLMetadataValueSingleString iqlMetadataValueSingleString = (IQLMetadataValueSingleString)theEObject;
        T result = caseIQLMetadataValueSingleString(iqlMetadataValueSingleString);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleString);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_BOOLEAN:
      {
        IQLMetadataValueSingleBoolean iqlMetadataValueSingleBoolean = (IQLMetadataValueSingleBoolean)theEObject;
        T result = caseIQLMetadataValueSingleBoolean(iqlMetadataValueSingleBoolean);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleBoolean);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_TYPE_REF:
      {
        IQLMetadataValueSingleTypeRef iqlMetadataValueSingleTypeRef = (IQLMetadataValueSingleTypeRef)theEObject;
        T result = caseIQLMetadataValueSingleTypeRef(iqlMetadataValueSingleTypeRef);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleTypeRef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_NULL:
      {
        IQLMetadataValueSingleNull iqlMetadataValueSingleNull = (IQLMetadataValueSingleNull)theEObject;
        T result = caseIQLMetadataValueSingleNull(iqlMetadataValueSingleNull);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueSingleNull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_LIST:
      {
        IQLMetadataValueList iqlMetadataValueList = (IQLMetadataValueList)theEObject;
        T result = caseIQLMetadataValueList(iqlMetadataValueList);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_METADATA_VALUE_MAP:
      {
        IQLMetadataValueMap iqlMetadataValueMap = (IQLMetadataValueMap)theEObject;
        T result = caseIQLMetadataValueMap(iqlMetadataValueMap);
        if (result == null) result = caseIQLMetadataValue(iqlMetadataValueMap);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_VARIABLE_DECLARATION:
      {
        IQLVariableDeclaration iqlVariableDeclaration = (IQLVariableDeclaration)theEObject;
        T result = caseIQLVariableDeclaration(iqlVariableDeclaration);
        if (result == null) result = caseJvmIdentifiableElement(iqlVariableDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_STATEMENT_BLOCK:
      {
        IQLStatementBlock iqlStatementBlock = (IQLStatementBlock)theEObject;
        T result = caseIQLStatementBlock(iqlStatementBlock);
        if (result == null) result = caseIQLStatement(iqlStatementBlock);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_JAVA_STATEMENT:
      {
        IQLJavaStatement iqlJavaStatement = (IQLJavaStatement)theEObject;
        T result = caseIQLJavaStatement(iqlJavaStatement);
        if (result == null) result = caseIQLStatement(iqlJavaStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_IF_STATEMENT:
      {
        IQLIfStatement iqlIfStatement = (IQLIfStatement)theEObject;
        T result = caseIQLIfStatement(iqlIfStatement);
        if (result == null) result = caseIQLStatement(iqlIfStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_WHILE_STATEMENT:
      {
        IQLWhileStatement iqlWhileStatement = (IQLWhileStatement)theEObject;
        T result = caseIQLWhileStatement(iqlWhileStatement);
        if (result == null) result = caseIQLStatement(iqlWhileStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_DO_WHILE_STATEMENT:
      {
        IQLDoWhileStatement iqlDoWhileStatement = (IQLDoWhileStatement)theEObject;
        T result = caseIQLDoWhileStatement(iqlDoWhileStatement);
        if (result == null) result = caseIQLStatement(iqlDoWhileStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_FOR_STATEMENT:
      {
        IQLForStatement iqlForStatement = (IQLForStatement)theEObject;
        T result = caseIQLForStatement(iqlForStatement);
        if (result == null) result = caseIQLStatement(iqlForStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT:
      {
        IQLForEachStatement iqlForEachStatement = (IQLForEachStatement)theEObject;
        T result = caseIQLForEachStatement(iqlForEachStatement);
        if (result == null) result = caseIQLStatement(iqlForEachStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_SWITCH_STATEMENT:
      {
        IQLSwitchStatement iqlSwitchStatement = (IQLSwitchStatement)theEObject;
        T result = caseIQLSwitchStatement(iqlSwitchStatement);
        if (result == null) result = caseIQLStatement(iqlSwitchStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_EXPRESSION_STATEMENT:
      {
        IQLExpressionStatement iqlExpressionStatement = (IQLExpressionStatement)theEObject;
        T result = caseIQLExpressionStatement(iqlExpressionStatement);
        if (result == null) result = caseIQLStatement(iqlExpressionStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_VARIABLE_STATEMENT:
      {
        IQLVariableStatement iqlVariableStatement = (IQLVariableStatement)theEObject;
        T result = caseIQLVariableStatement(iqlVariableStatement);
        if (result == null) result = caseIQLStatement(iqlVariableStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT:
      {
        IQLConstructorCallStatement iqlConstructorCallStatement = (IQLConstructorCallStatement)theEObject;
        T result = caseIQLConstructorCallStatement(iqlConstructorCallStatement);
        if (result == null) result = caseIQLStatement(iqlConstructorCallStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_BREAK_STATEMENT:
      {
        IQLBreakStatement iqlBreakStatement = (IQLBreakStatement)theEObject;
        T result = caseIQLBreakStatement(iqlBreakStatement);
        if (result == null) result = caseIQLStatement(iqlBreakStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_CONTINUE_STATEMENT:
      {
        IQLContinueStatement iqlContinueStatement = (IQLContinueStatement)theEObject;
        T result = caseIQLContinueStatement(iqlContinueStatement);
        if (result == null) result = caseIQLStatement(iqlContinueStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_RETURN_STATEMENT:
      {
        IQLReturnStatement iqlReturnStatement = (IQLReturnStatement)theEObject;
        T result = caseIQLReturnStatement(iqlReturnStatement);
        if (result == null) result = caseIQLStatement(iqlReturnStatement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ASSIGNMENT_EXPRESSION:
      {
        IQLAssignmentExpression iqlAssignmentExpression = (IQLAssignmentExpression)theEObject;
        T result = caseIQLAssignmentExpression(iqlAssignmentExpression);
        if (result == null) result = caseIQLExpression(iqlAssignmentExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LOGICAL_OR_EXPRESSION:
      {
        IQLLogicalOrExpression iqlLogicalOrExpression = (IQLLogicalOrExpression)theEObject;
        T result = caseIQLLogicalOrExpression(iqlLogicalOrExpression);
        if (result == null) result = caseIQLExpression(iqlLogicalOrExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LOGICAL_AND_EXPRESSION:
      {
        IQLLogicalAndExpression iqlLogicalAndExpression = (IQLLogicalAndExpression)theEObject;
        T result = caseIQLLogicalAndExpression(iqlLogicalAndExpression);
        if (result == null) result = caseIQLExpression(iqlLogicalAndExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_EQUALITY_EXPRESSION:
      {
        IQLEqualityExpression iqlEqualityExpression = (IQLEqualityExpression)theEObject;
        T result = caseIQLEqualityExpression(iqlEqualityExpression);
        if (result == null) result = caseIQLExpression(iqlEqualityExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_INSTANCE_OF_EXPRESSION:
      {
        IQLInstanceOfExpression iqlInstanceOfExpression = (IQLInstanceOfExpression)theEObject;
        T result = caseIQLInstanceOfExpression(iqlInstanceOfExpression);
        if (result == null) result = caseIQLExpression(iqlInstanceOfExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_RELATIONAL_EXPRESSION:
      {
        IQLRelationalExpression iqlRelationalExpression = (IQLRelationalExpression)theEObject;
        T result = caseIQLRelationalExpression(iqlRelationalExpression);
        if (result == null) result = caseIQLExpression(iqlRelationalExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ADDITIVE_EXPRESSION:
      {
        IQLAdditiveExpression iqlAdditiveExpression = (IQLAdditiveExpression)theEObject;
        T result = caseIQLAdditiveExpression(iqlAdditiveExpression);
        if (result == null) result = caseIQLExpression(iqlAdditiveExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_MULTIPLICATIVE_EXPRESSION:
      {
        IQLMultiplicativeExpression iqlMultiplicativeExpression = (IQLMultiplicativeExpression)theEObject;
        T result = caseIQLMultiplicativeExpression(iqlMultiplicativeExpression);
        if (result == null) result = caseIQLExpression(iqlMultiplicativeExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_PLUS_MINUS_EXPRESSION:
      {
        IQLPlusMinusExpression iqlPlusMinusExpression = (IQLPlusMinusExpression)theEObject;
        T result = caseIQLPlusMinusExpression(iqlPlusMinusExpression);
        if (result == null) result = caseIQLExpression(iqlPlusMinusExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_BOOLEAN_NOT_EXPRESSION:
      {
        IQLBooleanNotExpression iqlBooleanNotExpression = (IQLBooleanNotExpression)theEObject;
        T result = caseIQLBooleanNotExpression(iqlBooleanNotExpression);
        if (result == null) result = caseIQLExpression(iqlBooleanNotExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_PREFIX_EXPRESSION:
      {
        IQLPrefixExpression iqlPrefixExpression = (IQLPrefixExpression)theEObject;
        T result = caseIQLPrefixExpression(iqlPrefixExpression);
        if (result == null) result = caseIQLExpression(iqlPrefixExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION:
      {
        IQLTypeCastExpression iqlTypeCastExpression = (IQLTypeCastExpression)theEObject;
        T result = caseIQLTypeCastExpression(iqlTypeCastExpression);
        if (result == null) result = caseIQLExpression(iqlTypeCastExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_POSTFIX_EXPRESSION:
      {
        IQLPostfixExpression iqlPostfixExpression = (IQLPostfixExpression)theEObject;
        T result = caseIQLPostfixExpression(iqlPostfixExpression);
        if (result == null) result = caseIQLExpression(iqlPostfixExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_ARRAY_EXPRESSION:
      {
        IQLArrayExpression iqlArrayExpression = (IQLArrayExpression)theEObject;
        T result = caseIQLArrayExpression(iqlArrayExpression);
        if (result == null) result = caseIQLExpression(iqlArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION:
      {
        IQLMemberSelectionExpression iqlMemberSelectionExpression = (IQLMemberSelectionExpression)theEObject;
        T result = caseIQLMemberSelectionExpression(iqlMemberSelectionExpression);
        if (result == null) result = caseIQLExpression(iqlMemberSelectionExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_JVM_ELEMENT_CALL_EXPRESSION:
      {
        IQLJvmElementCallExpression iqlJvmElementCallExpression = (IQLJvmElementCallExpression)theEObject;
        T result = caseIQLJvmElementCallExpression(iqlJvmElementCallExpression);
        if (result == null) result = caseIQLExpression(iqlJvmElementCallExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_THIS_EXPRESSION:
      {
        IQLThisExpression iqlThisExpression = (IQLThisExpression)theEObject;
        T result = caseIQLThisExpression(iqlThisExpression);
        if (result == null) result = caseIQLExpression(iqlThisExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_SUPER_EXPRESSION:
      {
        IQLSuperExpression iqlSuperExpression = (IQLSuperExpression)theEObject;
        T result = caseIQLSuperExpression(iqlSuperExpression);
        if (result == null) result = caseIQLExpression(iqlSuperExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_PARENTHESIS_EXPRESSION:
      {
        IQLParenthesisExpression iqlParenthesisExpression = (IQLParenthesisExpression)theEObject;
        T result = caseIQLParenthesisExpression(iqlParenthesisExpression);
        if (result == null) result = caseIQLExpression(iqlParenthesisExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_NEW_EXPRESSION:
      {
        IQLNewExpression iqlNewExpression = (IQLNewExpression)theEObject;
        T result = caseIQLNewExpression(iqlNewExpression);
        if (result == null) result = caseIQLExpression(iqlNewExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_INT:
      {
        IQLLiteralExpressionInt iqlLiteralExpressionInt = (IQLLiteralExpressionInt)theEObject;
        T result = caseIQLLiteralExpressionInt(iqlLiteralExpressionInt);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionInt);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_DOUBLE:
      {
        IQLLiteralExpressionDouble iqlLiteralExpressionDouble = (IQLLiteralExpressionDouble)theEObject;
        T result = caseIQLLiteralExpressionDouble(iqlLiteralExpressionDouble);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionDouble);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_STRING:
      {
        IQLLiteralExpressionString iqlLiteralExpressionString = (IQLLiteralExpressionString)theEObject;
        T result = caseIQLLiteralExpressionString(iqlLiteralExpressionString);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionString);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_BOOLEAN:
      {
        IQLLiteralExpressionBoolean iqlLiteralExpressionBoolean = (IQLLiteralExpressionBoolean)theEObject;
        T result = caseIQLLiteralExpressionBoolean(iqlLiteralExpressionBoolean);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionBoolean);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_RANGE:
      {
        IQLLiteralExpressionRange iqlLiteralExpressionRange = (IQLLiteralExpressionRange)theEObject;
        T result = caseIQLLiteralExpressionRange(iqlLiteralExpressionRange);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionRange);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_TYPE:
      {
        IQLLiteralExpressionType iqlLiteralExpressionType = (IQLLiteralExpressionType)theEObject;
        T result = caseIQLLiteralExpressionType(iqlLiteralExpressionType);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_NULL:
      {
        IQLLiteralExpressionNull iqlLiteralExpressionNull = (IQLLiteralExpressionNull)theEObject;
        T result = caseIQLLiteralExpressionNull(iqlLiteralExpressionNull);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionNull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_LIST:
      {
        IQLLiteralExpressionList iqlLiteralExpressionList = (IQLLiteralExpressionList)theEObject;
        T result = caseIQLLiteralExpressionList(iqlLiteralExpressionList);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP:
      {
        IQLLiteralExpressionMap iqlLiteralExpressionMap = (IQLLiteralExpressionMap)theEObject;
        T result = caseIQLLiteralExpressionMap(iqlLiteralExpressionMap);
        if (result == null) result = caseIQLExpression(iqlLiteralExpressionMap);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLModel(IQLModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLModelElement(IQLModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Namespace</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Namespace</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLNamespace(IQLNamespace object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Java Metadata</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Java Metadata</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLJavaMetadata(IQLJavaMetadata object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataList(IQLMetadataList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadata(IQLMetadata object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValue(IQLMetadataValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Map Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Map Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueMapElement(IQLMetadataValueMapElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Variable Initialization</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Variable Initialization</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLVariableInitialization(IQLVariableInitialization object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Arguments List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Arguments List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArgumentsList(IQLArgumentsList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Arguments Map</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Arguments Map</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArgumentsMap(IQLArgumentsMap object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Arguments Map Key Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Arguments Map Key Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLStatement(IQLStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Case Part</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Case Part</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLCasePart(IQLCasePart object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLExpression(IQLExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Member Selection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Member Selection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMemberSelection(IQLMemberSelection object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Map Key Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Map Key Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionMapKeyValue(IQLLiteralExpressionMapKeyValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Java</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Java</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLJava(IQLJava object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Class</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Class</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLClass(IQLClass object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Interface</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Interface</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLInterface(IQLInterface object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Attribute</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Attribute</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLAttribute(IQLAttribute object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Simple Type Ref</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Simple Type Ref</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLSimpleTypeRef(IQLSimpleTypeRef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Array Type Ref</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Array Type Ref</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArrayTypeRef(IQLArrayTypeRef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Array Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Array Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArrayType(IQLArrayType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Method</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Method</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMethod(IQLMethod object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Method Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Method Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMethodDeclaration(IQLMethodDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Java Member</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Java Member</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLJavaMember(IQLJavaMember object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Int</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Int</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleInt(IQLMetadataValueSingleInt object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Double</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Double</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleDouble(IQLMetadataValueSingleDouble object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single String</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single String</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleString(IQLMetadataValueSingleString object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Boolean</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Boolean</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleBoolean(IQLMetadataValueSingleBoolean object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Type Ref</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Type Ref</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleTypeRef(IQLMetadataValueSingleTypeRef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Null</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Single Null</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueSingleNull(IQLMetadataValueSingleNull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueList(IQLMetadataValueList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Metadata Value Map</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Metadata Value Map</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMetadataValueMap(IQLMetadataValueMap object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Variable Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Variable Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLVariableDeclaration(IQLVariableDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Statement Block</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Statement Block</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLStatementBlock(IQLStatementBlock object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Java Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Java Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLJavaStatement(IQLJavaStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL If Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL If Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLIfStatement(IQLIfStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL While Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL While Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLWhileStatement(IQLWhileStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Do While Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Do While Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLDoWhileStatement(IQLDoWhileStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL For Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL For Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLForStatement(IQLForStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL For Each Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL For Each Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLForEachStatement(IQLForEachStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Switch Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Switch Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLSwitchStatement(IQLSwitchStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Expression Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Expression Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLExpressionStatement(IQLExpressionStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Variable Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Variable Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLVariableStatement(IQLVariableStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Constructor Call Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Constructor Call Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLConstructorCallStatement(IQLConstructorCallStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Break Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Break Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLBreakStatement(IQLBreakStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Continue Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Continue Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLContinueStatement(IQLContinueStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Return Statement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Return Statement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLReturnStatement(IQLReturnStatement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Assignment Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Assignment Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLAssignmentExpression(IQLAssignmentExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Logical Or Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Logical Or Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLogicalOrExpression(IQLLogicalOrExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Logical And Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Logical And Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLogicalAndExpression(IQLLogicalAndExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Equality Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Equality Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLEqualityExpression(IQLEqualityExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Instance Of Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Instance Of Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLInstanceOfExpression(IQLInstanceOfExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Relational Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Relational Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLRelationalExpression(IQLRelationalExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Additive Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Additive Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLAdditiveExpression(IQLAdditiveExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Multiplicative Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Multiplicative Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMultiplicativeExpression(IQLMultiplicativeExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Plus Minus Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Plus Minus Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLPlusMinusExpression(IQLPlusMinusExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Boolean Not Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Boolean Not Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLBooleanNotExpression(IQLBooleanNotExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Prefix Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Prefix Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLPrefixExpression(IQLPrefixExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Type Cast Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Type Cast Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLTypeCastExpression(IQLTypeCastExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Postfix Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Postfix Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLPostfixExpression(IQLPostfixExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLArrayExpression(IQLArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Member Selection Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Member Selection Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLMemberSelectionExpression(IQLMemberSelectionExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Jvm Element Call Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Jvm Element Call Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLJvmElementCallExpression(IQLJvmElementCallExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL This Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL This Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLThisExpression(IQLThisExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Super Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Super Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLSuperExpression(IQLSuperExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Parenthesis Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Parenthesis Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLParenthesisExpression(IQLParenthesisExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL New Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL New Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLNewExpression(IQLNewExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Int</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Int</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionInt(IQLLiteralExpressionInt object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Double</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Double</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionDouble(IQLLiteralExpressionDouble object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression String</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression String</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionString(IQLLiteralExpressionString object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Boolean</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Boolean</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionBoolean(IQLLiteralExpressionBoolean object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Range</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Range</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionRange(IQLLiteralExpressionRange object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionType(IQLLiteralExpressionType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Null</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Null</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionNull(IQLLiteralExpressionNull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionList(IQLLiteralExpressionList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Literal Expression Map</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Literal Expression Map</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLLiteralExpressionMap(IQLLiteralExpressionMap object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Identifiable Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Identifiable Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmIdentifiableElement(JvmIdentifiableElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Annotation Target</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Annotation Target</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmAnnotationTarget(JvmAnnotationTarget object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Member</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Member</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmMember(JvmMember object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmType(JvmType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Component Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Component Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmComponentType(JvmComponentType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Declared Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Declared Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmDeclaredType(JvmDeclaredType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Type Parameter Declarator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Type Parameter Declarator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmTypeParameterDeclarator(JvmTypeParameterDeclarator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Generic Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Generic Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmGenericType(JvmGenericType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Feature</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Feature</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmFeature(JvmFeature object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Field</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Field</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmField(JvmField object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmTypeReference(JvmTypeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Executable</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Executable</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmExecutable(JvmExecutable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmOperation(JvmOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //BasicIQLSwitch
