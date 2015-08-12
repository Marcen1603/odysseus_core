/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodImpl;

import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl#isOn <em>On</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl#isValidate <em>Validate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ODLMethodImpl extends IQLMethodImpl implements ODLMethod
{
  /**
   * The default value of the '{@link #isOn() <em>On</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOn()
   * @generated
   * @ordered
   */
  protected static final boolean ON_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOn() <em>On</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOn()
   * @generated
   * @ordered
   */
  protected boolean on = ON_EDEFAULT;

  /**
   * The default value of the '{@link #isValidate() <em>Validate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isValidate()
   * @generated
   * @ordered
   */
  protected static final boolean VALIDATE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isValidate() <em>Validate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isValidate()
   * @generated
   * @ordered
   */
  protected boolean validate = VALIDATE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ODLMethodImpl()
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
    return ODLPackage.Literals.ODL_METHOD;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOn()
  {
    return on;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOn(boolean newOn)
  {
    boolean oldOn = on;
    on = newOn;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_METHOD__ON, oldOn, on));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isValidate()
  {
    return validate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValidate(boolean newValidate)
  {
    boolean oldValidate = validate;
    validate = newValidate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_METHOD__VALIDATE, oldValidate, validate));
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
      case ODLPackage.ODL_METHOD__ON:
        return isOn();
      case ODLPackage.ODL_METHOD__VALIDATE:
        return isValidate();
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
      case ODLPackage.ODL_METHOD__ON:
        setOn((Boolean)newValue);
        return;
      case ODLPackage.ODL_METHOD__VALIDATE:
        setValidate((Boolean)newValue);
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
      case ODLPackage.ODL_METHOD__ON:
        setOn(ON_EDEFAULT);
        return;
      case ODLPackage.ODL_METHOD__VALIDATE:
        setValidate(VALIDATE_EDEFAULT);
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
      case ODLPackage.ODL_METHOD__ON:
        return on != ON_EDEFAULT;
      case ODLPackage.ODL_METHOD__VALIDATE:
        return validate != VALIDATE_EDEFAULT;
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
    result.append(" (on: ");
    result.append(on);
    result.append(", validate: ");
    result.append(validate);
    result.append(')');
    return result.toString();
  }

} //ODLMethodImpl