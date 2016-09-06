/**
 */
package de.uniol.inf.is.odysseus.pql2.util;

import de.uniol.inf.is.odysseus.pql2.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package
 * @generated
 */
public class Pql2Switch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static Pql2Package modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pql2Switch()
  {
    if (modelPackage == null)
    {
      modelPackage = Pql2Package.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
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
      case Pql2Package.PQL_MODEL:
      {
        PQLModel pqlModel = (PQLModel)theEObject;
        T result = casePQLModel(pqlModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.QUERY:
      {
        Query query = (Query)theEObject;
        T result = caseQuery(query);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.TEMPORARY_STREAM:
      {
        TemporaryStream temporaryStream = (TemporaryStream)theEObject;
        T result = caseTemporaryStream(temporaryStream);
        if (result == null) result = caseQuery(temporaryStream);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.VIEW:
      {
        View view = (View)theEObject;
        T result = caseView(view);
        if (result == null) result = caseQuery(view);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.SHARED_STREAM:
      {
        SharedStream sharedStream = (SharedStream)theEObject;
        T result = caseSharedStream(sharedStream);
        if (result == null) result = caseQuery(sharedStream);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.OPERATOR:
      {
        Operator operator = (Operator)theEObject;
        T result = caseOperator(operator);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.OPERATOR_LIST:
      {
        OperatorList operatorList = (OperatorList)theEObject;
        T result = caseOperatorList(operatorList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.OPERATOR_OR_QUERY:
      {
        OperatorOrQuery operatorOrQuery = (OperatorOrQuery)theEObject;
        T result = caseOperatorOrQuery(operatorOrQuery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.PARAMETER_LIST:
      {
        ParameterList parameterList = (ParameterList)theEObject;
        T result = caseParameterList(parameterList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.PARAMETER:
      {
        Parameter parameter = (Parameter)theEObject;
        T result = caseParameter(parameter);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.PARAMETER_VALUE:
      {
        ParameterValue parameterValue = (ParameterValue)theEObject;
        T result = caseParameterValue(parameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.LONG_PARAMETER_VALUE:
      {
        LongParameterValue longParameterValue = (LongParameterValue)theEObject;
        T result = caseLongParameterValue(longParameterValue);
        if (result == null) result = caseParameterValue(longParameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.DOUBLE_PARAMETER_VALUE:
      {
        DoubleParameterValue doubleParameterValue = (DoubleParameterValue)theEObject;
        T result = caseDoubleParameterValue(doubleParameterValue);
        if (result == null) result = caseParameterValue(doubleParameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.STRING_PARAMETER_VALUE:
      {
        StringParameterValue stringParameterValue = (StringParameterValue)theEObject;
        T result = caseStringParameterValue(stringParameterValue);
        if (result == null) result = caseParameterValue(stringParameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.LIST_PARAMETER_VALUE:
      {
        ListParameterValue listParameterValue = (ListParameterValue)theEObject;
        T result = caseListParameterValue(listParameterValue);
        if (result == null) result = caseParameterValue(listParameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.MAP_PARAMETER_VALUE:
      {
        MapParameterValue mapParameterValue = (MapParameterValue)theEObject;
        T result = caseMapParameterValue(mapParameterValue);
        if (result == null) result = caseParameterValue(mapParameterValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.MAP_ENTRY:
      {
        MapEntry mapEntry = (MapEntry)theEObject;
        T result = caseMapEntry(mapEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.LIST:
      {
        List list = (List)theEObject;
        T result = caseList(list);
        if (result == null) result = caseListParameterValue(list);
        if (result == null) result = caseParameterValue(list);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case Pql2Package.MAP:
      {
        Map map = (Map)theEObject;
        T result = caseMap(map);
        if (result == null) result = caseMapParameterValue(map);
        if (result == null) result = caseParameterValue(map);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>PQL Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>PQL Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePQLModel(PQLModel object)
  {
    return null;
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
  public T caseQuery(Query object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Temporary Stream</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Temporary Stream</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTemporaryStream(TemporaryStream object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>View</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>View</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseView(View object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Shared Stream</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Shared Stream</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSharedStream(SharedStream object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperator(Operator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operator List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operator List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperatorList(OperatorList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operator Or Query</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operator Or Query</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperatorOrQuery(OperatorOrQuery object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterList(ParameterList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameter(Parameter object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterValue(ParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Long Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Long Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLongParameterValue(LongParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Double Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Double Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDoubleParameterValue(DoubleParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringParameterValue(StringParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>List Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>List Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseListParameterValue(ListParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Map Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Map Parameter Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMapParameterValue(MapParameterValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Map Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Map Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMapEntry(MapEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseList(List object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Map</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Map</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMap(Map object)
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

} //Pql2Switch
