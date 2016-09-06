/**
 */
package de.uniol.inf.is.odysseus.pql2.impl;

import de.uniol.inf.is.odysseus.pql2.Operator;
import de.uniol.inf.is.odysseus.pql2.OperatorList;
import de.uniol.inf.is.odysseus.pql2.ParameterList;
import de.uniol.inf.is.odysseus.pql2.Pql2Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl#getOperatorType <em>Operator Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl#getOperators <em>Operators</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperatorImpl extends MinimalEObjectImpl.Container implements Operator
{
  /**
   * The default value of the '{@link #getOperatorType() <em>Operator Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperatorType()
   * @generated
   * @ordered
   */
  protected static final String OPERATOR_TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOperatorType() <em>Operator Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperatorType()
   * @generated
   * @ordered
   */
  protected String operatorType = OPERATOR_TYPE_EDEFAULT;

  /**
   * The cached value of the '{@link #getOperators() <em>Operators</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperators()
   * @generated
   * @ordered
   */
  protected OperatorList operators;

  /**
   * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameters()
   * @generated
   * @ordered
   */
  protected ParameterList parameters;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperatorImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return Pql2Package.Literals.OPERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOperatorType()
  {
    return operatorType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperatorType(String newOperatorType)
  {
    String oldOperatorType = operatorType;
    operatorType = newOperatorType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR__OPERATOR_TYPE, oldOperatorType, operatorType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OperatorList getOperators()
  {
    return operators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOperators(OperatorList newOperators, NotificationChain msgs)
  {
    OperatorList oldOperators = operators;
    operators = newOperators;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR__OPERATORS, oldOperators, newOperators);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperators(OperatorList newOperators)
  {
    if (newOperators != operators)
    {
      NotificationChain msgs = null;
      if (operators != null)
        msgs = ((InternalEObject)operators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR__OPERATORS, null, msgs);
      if (newOperators != null)
        msgs = ((InternalEObject)newOperators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR__OPERATORS, null, msgs);
      msgs = basicSetOperators(newOperators, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR__OPERATORS, newOperators, newOperators));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParameterList getParameters()
  {
    return parameters;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParameters(ParameterList newParameters, NotificationChain msgs)
  {
    ParameterList oldParameters = parameters;
    parameters = newParameters;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR__PARAMETERS, oldParameters, newParameters);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParameters(ParameterList newParameters)
  {
    if (newParameters != parameters)
    {
      NotificationChain msgs = null;
      if (parameters != null)
        msgs = ((InternalEObject)parameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR__PARAMETERS, null, msgs);
      if (newParameters != null)
        msgs = ((InternalEObject)newParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR__PARAMETERS, null, msgs);
      msgs = basicSetParameters(newParameters, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR__PARAMETERS, newParameters, newParameters));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case Pql2Package.OPERATOR__OPERATORS:
        return basicSetOperators(null, msgs);
      case Pql2Package.OPERATOR__PARAMETERS:
        return basicSetParameters(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case Pql2Package.OPERATOR__OPERATOR_TYPE:
        return getOperatorType();
      case Pql2Package.OPERATOR__OPERATORS:
        return getOperators();
      case Pql2Package.OPERATOR__PARAMETERS:
        return getParameters();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case Pql2Package.OPERATOR__OPERATOR_TYPE:
        setOperatorType((String)newValue);
        return;
      case Pql2Package.OPERATOR__OPERATORS:
        setOperators((OperatorList)newValue);
        return;
      case Pql2Package.OPERATOR__PARAMETERS:
        setParameters((ParameterList)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case Pql2Package.OPERATOR__OPERATOR_TYPE:
        setOperatorType(OPERATOR_TYPE_EDEFAULT);
        return;
      case Pql2Package.OPERATOR__OPERATORS:
        setOperators((OperatorList)null);
        return;
      case Pql2Package.OPERATOR__PARAMETERS:
        setParameters((ParameterList)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case Pql2Package.OPERATOR__OPERATOR_TYPE:
        return OPERATOR_TYPE_EDEFAULT == null ? operatorType != null : !OPERATOR_TYPE_EDEFAULT.equals(operatorType);
      case Pql2Package.OPERATOR__OPERATORS:
        return operators != null;
      case Pql2Package.OPERATOR__PARAMETERS:
        return parameters != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (operatorType: ");
    result.append(operatorType);
    result.append(')');
    return result.toString();
  }

} //OperatorImpl
