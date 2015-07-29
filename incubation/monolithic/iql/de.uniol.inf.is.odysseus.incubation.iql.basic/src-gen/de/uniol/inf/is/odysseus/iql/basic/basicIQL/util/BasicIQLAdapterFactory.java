/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.util;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

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
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage
 * @generated
 */
public class BasicIQLAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static BasicIQLPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasicIQLAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = BasicIQLPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BasicIQLSwitch<Adapter> modelSwitch =
    new BasicIQLSwitch<Adapter>()
    {
      @Override
      public Adapter caseIQLFile(IQLFile object)
      {
        return createIQLFileAdapter();
      }
      @Override
      public Adapter caseIQLTypeDef(IQLTypeDef object)
      {
        return createIQLTypeDefAdapter();
      }
      @Override
      public Adapter caseIQLNamespace(IQLNamespace object)
      {
        return createIQLNamespaceAdapter();
      }
      @Override
      public Adapter caseIQLJavaMetadata(IQLJavaMetadata object)
      {
        return createIQLJavaMetadataAdapter();
      }
      @Override
      public Adapter caseIQLSimpleTypeRef(IQLSimpleTypeRef object)
      {
        return createIQLSimpleTypeRefAdapter();
      }
      @Override
      public Adapter caseIQLArrayTypeRef(IQLArrayTypeRef object)
      {
        return createIQLArrayTypeRefAdapter();
      }
      @Override
      public Adapter caseIQLSimpleType(IQLSimpleType object)
      {
        return createIQLSimpleTypeAdapter();
      }
      @Override
      public Adapter caseIQLArrayType(IQLArrayType object)
      {
        return createIQLArrayTypeAdapter();
      }
      @Override
      public Adapter caseIQLMetadataList(IQLMetadataList object)
      {
        return createIQLMetadataListAdapter();
      }
      @Override
      public Adapter caseIQLMetadata(IQLMetadata object)
      {
        return createIQLMetadataAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValue(IQLMetadataValue object)
      {
        return createIQLMetadataValueAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueMapElement(IQLMetadataValueMapElement object)
      {
        return createIQLMetadataValueMapElementAdapter();
      }
      @Override
      public Adapter caseIQLVariableInitialization(IQLVariableInitialization object)
      {
        return createIQLVariableInitializationAdapter();
      }
      @Override
      public Adapter caseIQLArgumentsList(IQLArgumentsList object)
      {
        return createIQLArgumentsListAdapter();
      }
      @Override
      public Adapter caseIQLArgumentsMap(IQLArgumentsMap object)
      {
        return createIQLArgumentsMapAdapter();
      }
      @Override
      public Adapter caseIQLArgumentsMapKeyValue(IQLArgumentsMapKeyValue object)
      {
        return createIQLArgumentsMapKeyValueAdapter();
      }
      @Override
      public Adapter caseIQLStatement(IQLStatement object)
      {
        return createIQLStatementAdapter();
      }
      @Override
      public Adapter caseIQLCasePart(IQLCasePart object)
      {
        return createIQLCasePartAdapter();
      }
      @Override
      public Adapter caseIQLExpression(IQLExpression object)
      {
        return createIQLExpressionAdapter();
      }
      @Override
      public Adapter caseIQLMemberSelection(IQLMemberSelection object)
      {
        return createIQLMemberSelectionAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionMapKeyValue(IQLLiteralExpressionMapKeyValue object)
      {
        return createIQLLiteralExpressionMapKeyValueAdapter();
      }
      @Override
      public Adapter caseIQLJava(IQLJava object)
      {
        return createIQLJavaAdapter();
      }
      @Override
      public Adapter caseIQLJavaKeywords(IQLJavaKeywords object)
      {
        return createIQLJavaKeywordsAdapter();
      }
      @Override
      public Adapter caseIQLClass(IQLClass object)
      {
        return createIQLClassAdapter();
      }
      @Override
      public Adapter caseIQLInterface(IQLInterface object)
      {
        return createIQLInterfaceAdapter();
      }
      @Override
      public Adapter caseIQLAttribute(IQLAttribute object)
      {
        return createIQLAttributeAdapter();
      }
      @Override
      public Adapter caseIQLMethod(IQLMethod object)
      {
        return createIQLMethodAdapter();
      }
      @Override
      public Adapter caseIQLMethodDeclarationMember(IQLMethodDeclarationMember object)
      {
        return createIQLMethodDeclarationMemberAdapter();
      }
      @Override
      public Adapter caseIQLJavaMember(IQLJavaMember object)
      {
        return createIQLJavaMemberAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleInt(IQLMetadataValueSingleInt object)
      {
        return createIQLMetadataValueSingleIntAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleDouble(IQLMetadataValueSingleDouble object)
      {
        return createIQLMetadataValueSingleDoubleAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleString(IQLMetadataValueSingleString object)
      {
        return createIQLMetadataValueSingleStringAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleBoolean(IQLMetadataValueSingleBoolean object)
      {
        return createIQLMetadataValueSingleBooleanAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleChar(IQLMetadataValueSingleChar object)
      {
        return createIQLMetadataValueSingleCharAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleID(IQLMetadataValueSingleID object)
      {
        return createIQLMetadataValueSingleIDAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleTypeRef(IQLMetadataValueSingleTypeRef object)
      {
        return createIQLMetadataValueSingleTypeRefAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleNull(IQLMetadataValueSingleNull object)
      {
        return createIQLMetadataValueSingleNullAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueList(IQLMetadataValueList object)
      {
        return createIQLMetadataValueListAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueMap(IQLMetadataValueMap object)
      {
        return createIQLMetadataValueMapAdapter();
      }
      @Override
      public Adapter caseIQLVariableDeclaration(IQLVariableDeclaration object)
      {
        return createIQLVariableDeclarationAdapter();
      }
      @Override
      public Adapter caseIQLStatementBlock(IQLStatementBlock object)
      {
        return createIQLStatementBlockAdapter();
      }
      @Override
      public Adapter caseIQLJavaStatement(IQLJavaStatement object)
      {
        return createIQLJavaStatementAdapter();
      }
      @Override
      public Adapter caseIQLIfStatement(IQLIfStatement object)
      {
        return createIQLIfStatementAdapter();
      }
      @Override
      public Adapter caseIQLWhileStatement(IQLWhileStatement object)
      {
        return createIQLWhileStatementAdapter();
      }
      @Override
      public Adapter caseIQLDoWhileStatement(IQLDoWhileStatement object)
      {
        return createIQLDoWhileStatementAdapter();
      }
      @Override
      public Adapter caseIQLForStatement(IQLForStatement object)
      {
        return createIQLForStatementAdapter();
      }
      @Override
      public Adapter caseIQLForEachStatement(IQLForEachStatement object)
      {
        return createIQLForEachStatementAdapter();
      }
      @Override
      public Adapter caseIQLSwitchStatement(IQLSwitchStatement object)
      {
        return createIQLSwitchStatementAdapter();
      }
      @Override
      public Adapter caseIQLExpressionStatement(IQLExpressionStatement object)
      {
        return createIQLExpressionStatementAdapter();
      }
      @Override
      public Adapter caseIQLVariableStatement(IQLVariableStatement object)
      {
        return createIQLVariableStatementAdapter();
      }
      @Override
      public Adapter caseIQLConstructorCallStatement(IQLConstructorCallStatement object)
      {
        return createIQLConstructorCallStatementAdapter();
      }
      @Override
      public Adapter caseIQLBreakStatement(IQLBreakStatement object)
      {
        return createIQLBreakStatementAdapter();
      }
      @Override
      public Adapter caseIQLContinueStatement(IQLContinueStatement object)
      {
        return createIQLContinueStatementAdapter();
      }
      @Override
      public Adapter caseIQLReturnStatement(IQLReturnStatement object)
      {
        return createIQLReturnStatementAdapter();
      }
      @Override
      public Adapter caseIQLAssignmentExpression(IQLAssignmentExpression object)
      {
        return createIQLAssignmentExpressionAdapter();
      }
      @Override
      public Adapter caseIQLLogicalOrExpression(IQLLogicalOrExpression object)
      {
        return createIQLLogicalOrExpressionAdapter();
      }
      @Override
      public Adapter caseIQLLogicalAndExpression(IQLLogicalAndExpression object)
      {
        return createIQLLogicalAndExpressionAdapter();
      }
      @Override
      public Adapter caseIQLEqualityExpression(IQLEqualityExpression object)
      {
        return createIQLEqualityExpressionAdapter();
      }
      @Override
      public Adapter caseIQLInstanceOfExpression(IQLInstanceOfExpression object)
      {
        return createIQLInstanceOfExpressionAdapter();
      }
      @Override
      public Adapter caseIQLRelationalExpression(IQLRelationalExpression object)
      {
        return createIQLRelationalExpressionAdapter();
      }
      @Override
      public Adapter caseIQLAdditiveExpression(IQLAdditiveExpression object)
      {
        return createIQLAdditiveExpressionAdapter();
      }
      @Override
      public Adapter caseIQLMultiplicativeExpression(IQLMultiplicativeExpression object)
      {
        return createIQLMultiplicativeExpressionAdapter();
      }
      @Override
      public Adapter caseIQLPlusMinusExpression(IQLPlusMinusExpression object)
      {
        return createIQLPlusMinusExpressionAdapter();
      }
      @Override
      public Adapter caseIQLBooleanNotExpression(IQLBooleanNotExpression object)
      {
        return createIQLBooleanNotExpressionAdapter();
      }
      @Override
      public Adapter caseIQLPrefixExpression(IQLPrefixExpression object)
      {
        return createIQLPrefixExpressionAdapter();
      }
      @Override
      public Adapter caseIQLTypeCastExpression(IQLTypeCastExpression object)
      {
        return createIQLTypeCastExpressionAdapter();
      }
      @Override
      public Adapter caseIQLPostfixExpression(IQLPostfixExpression object)
      {
        return createIQLPostfixExpressionAdapter();
      }
      @Override
      public Adapter caseIQLArrayExpression(IQLArrayExpression object)
      {
        return createIQLArrayExpressionAdapter();
      }
      @Override
      public Adapter caseIQLMemberSelectionExpression(IQLMemberSelectionExpression object)
      {
        return createIQLMemberSelectionExpressionAdapter();
      }
      @Override
      public Adapter caseIQLAttributeSelection(IQLAttributeSelection object)
      {
        return createIQLAttributeSelectionAdapter();
      }
      @Override
      public Adapter caseIQLMethodSelection(IQLMethodSelection object)
      {
        return createIQLMethodSelectionAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionVariable(IQLTerminalExpressionVariable object)
      {
        return createIQLTerminalExpressionVariableAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionMethod(IQLTerminalExpressionMethod object)
      {
        return createIQLTerminalExpressionMethodAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionThis(IQLTerminalExpressionThis object)
      {
        return createIQLTerminalExpressionThisAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionSuper(IQLTerminalExpressionSuper object)
      {
        return createIQLTerminalExpressionSuperAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionParenthesis(IQLTerminalExpressionParenthesis object)
      {
        return createIQLTerminalExpressionParenthesisAdapter();
      }
      @Override
      public Adapter caseIQLTerminalExpressionNew(IQLTerminalExpressionNew object)
      {
        return createIQLTerminalExpressionNewAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionInt(IQLLiteralExpressionInt object)
      {
        return createIQLLiteralExpressionIntAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionDouble(IQLLiteralExpressionDouble object)
      {
        return createIQLLiteralExpressionDoubleAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionString(IQLLiteralExpressionString object)
      {
        return createIQLLiteralExpressionStringAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionBoolean(IQLLiteralExpressionBoolean object)
      {
        return createIQLLiteralExpressionBooleanAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionChar(IQLLiteralExpressionChar object)
      {
        return createIQLLiteralExpressionCharAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionRange(IQLLiteralExpressionRange object)
      {
        return createIQLLiteralExpressionRangeAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionNull(IQLLiteralExpressionNull object)
      {
        return createIQLLiteralExpressionNullAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionList(IQLLiteralExpressionList object)
      {
        return createIQLLiteralExpressionListAdapter();
      }
      @Override
      public Adapter caseIQLLiteralExpressionMap(IQLLiteralExpressionMap object)
      {
        return createIQLLiteralExpressionMapAdapter();
      }
      @Override
      public Adapter caseJvmIdentifiableElement(JvmIdentifiableElement object)
      {
        return createJvmIdentifiableElementAdapter();
      }
      @Override
      public Adapter caseJvmAnnotationTarget(JvmAnnotationTarget object)
      {
        return createJvmAnnotationTargetAdapter();
      }
      @Override
      public Adapter caseJvmMember(JvmMember object)
      {
        return createJvmMemberAdapter();
      }
      @Override
      public Adapter caseJvmType(JvmType object)
      {
        return createJvmTypeAdapter();
      }
      @Override
      public Adapter caseJvmComponentType(JvmComponentType object)
      {
        return createJvmComponentTypeAdapter();
      }
      @Override
      public Adapter caseJvmDeclaredType(JvmDeclaredType object)
      {
        return createJvmDeclaredTypeAdapter();
      }
      @Override
      public Adapter caseJvmTypeParameterDeclarator(JvmTypeParameterDeclarator object)
      {
        return createJvmTypeParameterDeclaratorAdapter();
      }
      @Override
      public Adapter caseJvmGenericType(JvmGenericType object)
      {
        return createJvmGenericTypeAdapter();
      }
      @Override
      public Adapter caseJvmTypeReference(JvmTypeReference object)
      {
        return createJvmTypeReferenceAdapter();
      }
      @Override
      public Adapter caseJvmFeature(JvmFeature object)
      {
        return createJvmFeatureAdapter();
      }
      @Override
      public Adapter caseJvmField(JvmField object)
      {
        return createJvmFieldAdapter();
      }
      @Override
      public Adapter caseJvmExecutable(JvmExecutable object)
      {
        return createJvmExecutableAdapter();
      }
      @Override
      public Adapter caseJvmOperation(JvmOperation object)
      {
        return createJvmOperationAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile <em>IQL File</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile
   * @generated
   */
  public Adapter createIQLFileAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef <em>IQL Type Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef
   * @generated
   */
  public Adapter createIQLTypeDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace <em>IQL Namespace</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace
   * @generated
   */
  public Adapter createIQLNamespaceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata <em>IQL Java Metadata</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata
   * @generated
   */
  public Adapter createIQLJavaMetadataAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef <em>IQL Simple Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef
   * @generated
   */
  public Adapter createIQLSimpleTypeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef <em>IQL Array Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
   * @generated
   */
  public Adapter createIQLArrayTypeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType <em>IQL Simple Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType
   * @generated
   */
  public Adapter createIQLSimpleTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType <em>IQL Array Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType
   * @generated
   */
  public Adapter createIQLArrayTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList <em>IQL Metadata List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList
   * @generated
   */
  public Adapter createIQLMetadataListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata <em>IQL Metadata</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata
   * @generated
   */
  public Adapter createIQLMetadataAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue <em>IQL Metadata Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
   * @generated
   */
  public Adapter createIQLMetadataValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement <em>IQL Metadata Value Map Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement
   * @generated
   */
  public Adapter createIQLMetadataValueMapElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization <em>IQL Variable Initialization</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
   * @generated
   */
  public Adapter createIQLVariableInitializationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList <em>IQL Arguments List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList
   * @generated
   */
  public Adapter createIQLArgumentsListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap <em>IQL Arguments Map</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
   * @generated
   */
  public Adapter createIQLArgumentsMapAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue <em>IQL Arguments Map Key Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue
   * @generated
   */
  public Adapter createIQLArgumentsMapKeyValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement <em>IQL Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement
   * @generated
   */
  public Adapter createIQLStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart <em>IQL Case Part</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart
   * @generated
   */
  public Adapter createIQLCasePartAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression <em>IQL Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
   * @generated
   */
  public Adapter createIQLExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection <em>IQL Member Selection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection
   * @generated
   */
  public Adapter createIQLMemberSelectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue <em>IQL Literal Expression Map Key Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue
   * @generated
   */
  public Adapter createIQLLiteralExpressionMapKeyValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava <em>IQL Java</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava
   * @generated
   */
  public Adapter createIQLJavaAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords <em>IQL Java Keywords</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords
   * @generated
   */
  public Adapter createIQLJavaKeywordsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass <em>IQL Class</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass
   * @generated
   */
  public Adapter createIQLClassAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface <em>IQL Interface</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface
   * @generated
   */
  public Adapter createIQLInterfaceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute <em>IQL Attribute</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute
   * @generated
   */
  public Adapter createIQLAttributeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod <em>IQL Method</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod
   * @generated
   */
  public Adapter createIQLMethodAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember <em>IQL Method Declaration Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember
   * @generated
   */
  public Adapter createIQLMethodDeclarationMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember <em>IQL Java Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember
   * @generated
   */
  public Adapter createIQLJavaMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt <em>IQL Metadata Value Single Int</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt
   * @generated
   */
  public Adapter createIQLMetadataValueSingleIntAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble <em>IQL Metadata Value Single Double</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble
   * @generated
   */
  public Adapter createIQLMetadataValueSingleDoubleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString <em>IQL Metadata Value Single String</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString
   * @generated
   */
  public Adapter createIQLMetadataValueSingleStringAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean <em>IQL Metadata Value Single Boolean</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean
   * @generated
   */
  public Adapter createIQLMetadataValueSingleBooleanAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleChar <em>IQL Metadata Value Single Char</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleChar
   * @generated
   */
  public Adapter createIQLMetadataValueSingleCharAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleID <em>IQL Metadata Value Single ID</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleID
   * @generated
   */
  public Adapter createIQLMetadataValueSingleIDAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef <em>IQL Metadata Value Single Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef
   * @generated
   */
  public Adapter createIQLMetadataValueSingleTypeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull <em>IQL Metadata Value Single Null</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull
   * @generated
   */
  public Adapter createIQLMetadataValueSingleNullAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList <em>IQL Metadata Value List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList
   * @generated
   */
  public Adapter createIQLMetadataValueListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap <em>IQL Metadata Value Map</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap
   * @generated
   */
  public Adapter createIQLMetadataValueMapAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration <em>IQL Variable Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
   * @generated
   */
  public Adapter createIQLVariableDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock <em>IQL Statement Block</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock
   * @generated
   */
  public Adapter createIQLStatementBlockAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement <em>IQL Java Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement
   * @generated
   */
  public Adapter createIQLJavaStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement <em>IQL If Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement
   * @generated
   */
  public Adapter createIQLIfStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement <em>IQL While Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement
   * @generated
   */
  public Adapter createIQLWhileStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement <em>IQL Do While Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement
   * @generated
   */
  public Adapter createIQLDoWhileStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement <em>IQL For Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement
   * @generated
   */
  public Adapter createIQLForStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement <em>IQL For Each Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement
   * @generated
   */
  public Adapter createIQLForEachStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement <em>IQL Switch Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement
   * @generated
   */
  public Adapter createIQLSwitchStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement <em>IQL Expression Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement
   * @generated
   */
  public Adapter createIQLExpressionStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement <em>IQL Variable Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement
   * @generated
   */
  public Adapter createIQLVariableStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement <em>IQL Constructor Call Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement
   * @generated
   */
  public Adapter createIQLConstructorCallStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement <em>IQL Break Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement
   * @generated
   */
  public Adapter createIQLBreakStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement <em>IQL Continue Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement
   * @generated
   */
  public Adapter createIQLContinueStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement <em>IQL Return Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement
   * @generated
   */
  public Adapter createIQLReturnStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression <em>IQL Assignment Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression
   * @generated
   */
  public Adapter createIQLAssignmentExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression <em>IQL Logical Or Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression
   * @generated
   */
  public Adapter createIQLLogicalOrExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression <em>IQL Logical And Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression
   * @generated
   */
  public Adapter createIQLLogicalAndExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression <em>IQL Equality Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression
   * @generated
   */
  public Adapter createIQLEqualityExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression <em>IQL Instance Of Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression
   * @generated
   */
  public Adapter createIQLInstanceOfExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression <em>IQL Relational Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression
   * @generated
   */
  public Adapter createIQLRelationalExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression <em>IQL Additive Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression
   * @generated
   */
  public Adapter createIQLAdditiveExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression <em>IQL Multiplicative Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression
   * @generated
   */
  public Adapter createIQLMultiplicativeExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression <em>IQL Plus Minus Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression
   * @generated
   */
  public Adapter createIQLPlusMinusExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression <em>IQL Boolean Not Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression
   * @generated
   */
  public Adapter createIQLBooleanNotExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression <em>IQL Prefix Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression
   * @generated
   */
  public Adapter createIQLPrefixExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression <em>IQL Type Cast Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression
   * @generated
   */
  public Adapter createIQLTypeCastExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression <em>IQL Postfix Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression
   * @generated
   */
  public Adapter createIQLPostfixExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression <em>IQL Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression
   * @generated
   */
  public Adapter createIQLArrayExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression <em>IQL Member Selection Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression
   * @generated
   */
  public Adapter createIQLMemberSelectionExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection <em>IQL Attribute Selection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection
   * @generated
   */
  public Adapter createIQLAttributeSelectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection <em>IQL Method Selection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection
   * @generated
   */
  public Adapter createIQLMethodSelectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable <em>IQL Terminal Expression Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable
   * @generated
   */
  public Adapter createIQLTerminalExpressionVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionMethod <em>IQL Terminal Expression Method</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionMethod
   * @generated
   */
  public Adapter createIQLTerminalExpressionMethodAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis <em>IQL Terminal Expression This</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis
   * @generated
   */
  public Adapter createIQLTerminalExpressionThisAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper <em>IQL Terminal Expression Super</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper
   * @generated
   */
  public Adapter createIQLTerminalExpressionSuperAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis <em>IQL Terminal Expression Parenthesis</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis
   * @generated
   */
  public Adapter createIQLTerminalExpressionParenthesisAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew <em>IQL Terminal Expression New</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew
   * @generated
   */
  public Adapter createIQLTerminalExpressionNewAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt <em>IQL Literal Expression Int</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt
   * @generated
   */
  public Adapter createIQLLiteralExpressionIntAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble <em>IQL Literal Expression Double</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble
   * @generated
   */
  public Adapter createIQLLiteralExpressionDoubleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString <em>IQL Literal Expression String</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString
   * @generated
   */
  public Adapter createIQLLiteralExpressionStringAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean <em>IQL Literal Expression Boolean</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean
   * @generated
   */
  public Adapter createIQLLiteralExpressionBooleanAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar <em>IQL Literal Expression Char</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar
   * @generated
   */
  public Adapter createIQLLiteralExpressionCharAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange <em>IQL Literal Expression Range</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange
   * @generated
   */
  public Adapter createIQLLiteralExpressionRangeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull <em>IQL Literal Expression Null</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull
   * @generated
   */
  public Adapter createIQLLiteralExpressionNullAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList <em>IQL Literal Expression List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList
   * @generated
   */
  public Adapter createIQLLiteralExpressionListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap <em>IQL Literal Expression Map</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap
   * @generated
   */
  public Adapter createIQLLiteralExpressionMapAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmIdentifiableElement <em>Jvm Identifiable Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmIdentifiableElement
   * @generated
   */
  public Adapter createJvmIdentifiableElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmAnnotationTarget <em>Jvm Annotation Target</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmAnnotationTarget
   * @generated
   */
  public Adapter createJvmAnnotationTargetAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmMember <em>Jvm Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmMember
   * @generated
   */
  public Adapter createJvmMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmType <em>Jvm Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmType
   * @generated
   */
  public Adapter createJvmTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmComponentType <em>Jvm Component Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmComponentType
   * @generated
   */
  public Adapter createJvmComponentTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmDeclaredType <em>Jvm Declared Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmDeclaredType
   * @generated
   */
  public Adapter createJvmDeclaredTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmTypeParameterDeclarator <em>Jvm Type Parameter Declarator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmTypeParameterDeclarator
   * @generated
   */
  public Adapter createJvmTypeParameterDeclaratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmGenericType <em>Jvm Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmGenericType
   * @generated
   */
  public Adapter createJvmGenericTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmTypeReference <em>Jvm Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmTypeReference
   * @generated
   */
  public Adapter createJvmTypeReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmFeature <em>Jvm Feature</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmFeature
   * @generated
   */
  public Adapter createJvmFeatureAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmField <em>Jvm Field</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmField
   * @generated
   */
  public Adapter createJvmFieldAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmExecutable <em>Jvm Executable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmExecutable
   * @generated
   */
  public Adapter createJvmExecutableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmOperation <em>Jvm Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmOperation
   * @generated
   */
  public Adapter createJvmOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //BasicIQLAdapterFactory
