/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.util;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.xtext.common.types.JvmAnnotationTarget;
import org.eclipse.xtext.common.types.JvmComponentType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeParameterDeclarator;

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
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage
 * @generated
 */
public class QDLSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static QDLPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = QDLPackage.eINSTANCE;
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
      case QDLPackage.QDL_QUERY:
      {
        QDLQuery qdlQuery = (QDLQuery)theEObject;
        T result = caseQDLQuery(qdlQuery);
        if (result == null) result = caseJvmGenericType(qdlQuery);
        if (result == null) result = caseJvmDeclaredType(qdlQuery);
        if (result == null) result = caseJvmTypeParameterDeclarator(qdlQuery);
        if (result == null) result = caseJvmMember(qdlQuery);
        if (result == null) result = caseJvmComponentType(qdlQuery);
        if (result == null) result = caseJvmAnnotationTarget(qdlQuery);
        if (result == null) result = caseJvmType(qdlQuery);
        if (result == null) result = caseJvmIdentifiableElement(qdlQuery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QDLPackage.IQL_SUBSCRIBE_EXPRESSION:
      {
        IQLSubscribeExpression iqlSubscribeExpression = (IQLSubscribeExpression)theEObject;
        T result = caseIQLSubscribeExpression(iqlSubscribeExpression);
        if (result == null) result = caseIQLExpression(iqlSubscribeExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QDLPackage.IQL_PORT_EXPRESSION:
      {
        IQLPortExpression iqlPortExpression = (IQLPortExpression)theEObject;
        T result = caseIQLPortExpression(iqlPortExpression);
        if (result == null) result = caseIQLExpression(iqlPortExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QDLPackage.QDL_METADATA_VALUE_SINGLE_ID:
      {
        QDLMetadataValueSingleID qdlMetadataValueSingleID = (QDLMetadataValueSingleID)theEObject;
        T result = caseQDLMetadataValueSingleID(qdlMetadataValueSingleID);
        if (result == null) result = caseIQLMetadataValue(qdlMetadataValueSingleID);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Query</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Query</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseQDLQuery(QDLQuery object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Subscribe Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Subscribe Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLSubscribeExpression(IQLSubscribeExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IQL Port Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IQL Port Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIQLPortExpression(IQLPortExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Metadata Value Single ID</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Metadata Value Single ID</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseQDLMetadataValueSingleID(QDLMetadataValueSingleID object)
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

} //QDLSwitch
