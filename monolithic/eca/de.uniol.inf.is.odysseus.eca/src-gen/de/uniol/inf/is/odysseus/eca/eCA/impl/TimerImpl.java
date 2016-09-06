/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.Timer;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Timer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.TimerImpl#getTimerIntervallValue <em>Timer Intervall Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimerImpl extends MinimalEObjectImpl.Container implements Timer
{
  /**
   * The default value of the '{@link #getTimerIntervallValue() <em>Timer Intervall Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimerIntervallValue()
   * @generated
   * @ordered
   */
  protected static final int TIMER_INTERVALL_VALUE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getTimerIntervallValue() <em>Timer Intervall Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimerIntervallValue()
   * @generated
   * @ordered
   */
  protected int timerIntervallValue = TIMER_INTERVALL_VALUE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TimerImpl()
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
    return ECAPackage.Literals.TIMER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getTimerIntervallValue()
  {
    return timerIntervallValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTimerIntervallValue(int newTimerIntervallValue)
  {
    int oldTimerIntervallValue = timerIntervallValue;
    timerIntervallValue = newTimerIntervallValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.TIMER__TIMER_INTERVALL_VALUE, oldTimerIntervallValue, timerIntervallValue));
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
      case ECAPackage.TIMER__TIMER_INTERVALL_VALUE:
        return getTimerIntervallValue();
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
      case ECAPackage.TIMER__TIMER_INTERVALL_VALUE:
        setTimerIntervallValue((Integer)newValue);
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
      case ECAPackage.TIMER__TIMER_INTERVALL_VALUE:
        setTimerIntervallValue(TIMER_INTERVALL_VALUE_EDEFAULT);
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
      case ECAPackage.TIMER__TIMER_INTERVALL_VALUE:
        return timerIntervallValue != TIMER_INTERVALL_VALUE_EDEFAULT;
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
    result.append(" (timerIntervallValue: ");
    result.append(timerIntervallValue);
    result.append(')');
    return result.toString();
  }

} //TimerImpl
