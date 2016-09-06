/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FREECONDITION</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.FREECONDITIONImpl#getFreeCondition <em>Free Condition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FREECONDITIONImpl extends MinimalEObjectImpl.Container implements FREECONDITION
{
  /**
   * The default value of the '{@link #getFreeCondition() <em>Free Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFreeCondition()
   * @generated
   * @ordered
   */
  protected static final String FREE_CONDITION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFreeCondition() <em>Free Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFreeCondition()
   * @generated
   * @ordered
   */
  protected String freeCondition = FREE_CONDITION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FREECONDITIONImpl()
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
    return ECAPackage.Literals.FREECONDITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFreeCondition()
  {
    return freeCondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFreeCondition(String newFreeCondition)
  {
    String oldFreeCondition = freeCondition;
    freeCondition = newFreeCondition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.FREECONDITION__FREE_CONDITION, oldFreeCondition, freeCondition));
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
      case ECAPackage.FREECONDITION__FREE_CONDITION:
        return getFreeCondition();
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
      case ECAPackage.FREECONDITION__FREE_CONDITION:
        setFreeCondition((String)newValue);
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
      case ECAPackage.FREECONDITION__FREE_CONDITION:
        setFreeCondition(FREE_CONDITION_EDEFAULT);
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
      case ECAPackage.FREECONDITION__FREE_CONDITION:
        return FREE_CONDITION_EDEFAULT == null ? freeCondition != null : !FREE_CONDITION_EDEFAULT.equals(freeCondition);
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
    result.append(" (freeCondition: ");
    result.append(freeCondition);
    result.append(')');
    return result.toString();
  }

} //FREECONDITIONImpl
