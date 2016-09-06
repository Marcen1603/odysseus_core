/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.util;

import de.uniol.inf.is.odysseus.eca.eCA.*;

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
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage
 * @generated
 */
public class ECASwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ECAPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ECASwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ECAPackage.eINSTANCE;
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
      case ECAPackage.MODEL:
      {
        Model model = (Model)theEObject;
        T result = caseModel(model);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.CONSTANT:
      {
        Constant constant = (Constant)theEObject;
        T result = caseConstant(constant);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.WINDOW:
      {
        Window window = (Window)theEObject;
        T result = caseWindow(window);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.TIMER:
      {
        Timer timer = (Timer)theEObject;
        T result = caseTimer(timer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.DEFINED_EVENT:
      {
        DefinedEvent definedEvent = (DefinedEvent)theEObject;
        T result = caseDefinedEvent(definedEvent);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.RULE:
      {
        Rule rule = (Rule)theEObject;
        T result = caseRule(rule);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.EXPRESSION:
      {
        Expression expression = (Expression)theEObject;
        T result = caseExpression(expression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.RULE_SOURCE:
      {
        RuleSource ruleSource = (RuleSource)theEObject;
        T result = caseRuleSource(ruleSource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.SOURCECONDITION:
      {
        SOURCECONDITION sourcecondition = (SOURCECONDITION)theEObject;
        T result = caseSOURCECONDITION(sourcecondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.QUERYCONDITION:
      {
        QUERYCONDITION querycondition = (QUERYCONDITION)theEObject;
        T result = caseQUERYCONDITION(querycondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.SYSTEMCONDITION:
      {
        SYSTEMCONDITION systemcondition = (SYSTEMCONDITION)theEObject;
        T result = caseSYSTEMCONDITION(systemcondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.FREECONDITION:
      {
        FREECONDITION freecondition = (FREECONDITION)theEObject;
        T result = caseFREECONDITION(freecondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.MAPCONDITION:
      {
        MAPCONDITION mapcondition = (MAPCONDITION)theEObject;
        T result = caseMAPCONDITION(mapcondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.COMMANDACTION:
      {
        COMMANDACTION commandaction = (COMMANDACTION)theEObject;
        T result = caseCOMMANDACTION(commandaction);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.RNDQUERY:
      {
        RNDQUERY rndquery = (RNDQUERY)theEObject;
        T result = caseRNDQUERY(rndquery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.SOURCE:
      {
        Source source = (Source)theEObject;
        T result = caseSource(source);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.ECA_VALUE:
      {
        EcaValue ecaValue = (EcaValue)theEObject;
        T result = caseEcaValue(ecaValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.CONDITIONS:
      {
        CONDITIONS conditions = (CONDITIONS)theEObject;
        T result = caseCONDITIONS(conditions);
        if (result == null) result = caseExpression(conditions);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.SUBCONDITION:
      {
        SUBCONDITION subcondition = (SUBCONDITION)theEObject;
        T result = caseSUBCONDITION(subcondition);
        if (result == null) result = caseExpression(subcondition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ECAPackage.ACTIONS:
      {
        ACTIONS actions = (ACTIONS)theEObject;
        T result = caseACTIONS(actions);
        if (result == null) result = caseExpression(actions);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModel(Model object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Constant</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Constant</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConstant(Constant object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Window</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Window</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWindow(Window object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Timer</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Timer</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTimer(Timer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Defined Event</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Defined Event</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDefinedEvent(DefinedEvent object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Rule</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Rule</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRule(Rule object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpression(Expression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Rule Source</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Rule Source</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRuleSource(RuleSource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SOURCECONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SOURCECONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSOURCECONDITION(SOURCECONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>QUERYCONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>QUERYCONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseQUERYCONDITION(QUERYCONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SYSTEMCONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SYSTEMCONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSYSTEMCONDITION(SYSTEMCONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>FREECONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>FREECONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFREECONDITION(FREECONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MAPCONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MAPCONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMAPCONDITION(MAPCONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>COMMANDACTION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>COMMANDACTION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCOMMANDACTION(COMMANDACTION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RNDQUERY</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RNDQUERY</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRNDQUERY(RNDQUERY object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Source</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Source</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSource(Source object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Eca Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Eca Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEcaValue(EcaValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>CONDITIONS</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>CONDITIONS</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCONDITIONS(CONDITIONS object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SUBCONDITION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SUBCONDITION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSUBCONDITION(SUBCONDITION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ACTIONS</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ACTIONS</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseACTIONS(ACTIONS object)
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

} //ECASwitch
