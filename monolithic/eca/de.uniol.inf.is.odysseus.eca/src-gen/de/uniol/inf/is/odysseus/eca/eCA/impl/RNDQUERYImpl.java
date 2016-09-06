/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RNDQUERY</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl#getPriOperator <em>Pri Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl#getPriVal <em>Pri Val</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl#getSel <em>Sel</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl#getStateName <em>State Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RNDQUERYImpl extends MinimalEObjectImpl.Container implements RNDQUERY
{
  /**
   * The default value of the '{@link #getPriOperator() <em>Pri Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriOperator()
   * @generated
   * @ordered
   */
  protected static final String PRI_OPERATOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPriOperator() <em>Pri Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriOperator()
   * @generated
   * @ordered
   */
  protected String priOperator = PRI_OPERATOR_EDEFAULT;

  /**
   * The default value of the '{@link #getPriVal() <em>Pri Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriVal()
   * @generated
   * @ordered
   */
  protected static final int PRI_VAL_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPriVal() <em>Pri Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPriVal()
   * @generated
   * @ordered
   */
  protected int priVal = PRI_VAL_EDEFAULT;

  /**
   * The default value of the '{@link #getSel() <em>Sel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSel()
   * @generated
   * @ordered
   */
  protected static final String SEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSel() <em>Sel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSel()
   * @generated
   * @ordered
   */
  protected String sel = SEL_EDEFAULT;

  /**
   * The default value of the '{@link #getStateName() <em>State Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStateName()
   * @generated
   * @ordered
   */
  protected static final String STATE_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStateName() <em>State Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStateName()
   * @generated
   * @ordered
   */
  protected String stateName = STATE_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RNDQUERYImpl()
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
    return ECAPackage.Literals.RNDQUERY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPriOperator()
  {
    return priOperator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPriOperator(String newPriOperator)
  {
    String oldPriOperator = priOperator;
    priOperator = newPriOperator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RNDQUERY__PRI_OPERATOR, oldPriOperator, priOperator));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPriVal()
  {
    return priVal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPriVal(int newPriVal)
  {
    int oldPriVal = priVal;
    priVal = newPriVal;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RNDQUERY__PRI_VAL, oldPriVal, priVal));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSel()
  {
    return sel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSel(String newSel)
  {
    String oldSel = sel;
    sel = newSel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RNDQUERY__SEL, oldSel, sel));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getStateName()
  {
    return stateName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStateName(String newStateName)
  {
    String oldStateName = stateName;
    stateName = newStateName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RNDQUERY__STATE_NAME, oldStateName, stateName));
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
      case ECAPackage.RNDQUERY__PRI_OPERATOR:
        return getPriOperator();
      case ECAPackage.RNDQUERY__PRI_VAL:
        return getPriVal();
      case ECAPackage.RNDQUERY__SEL:
        return getSel();
      case ECAPackage.RNDQUERY__STATE_NAME:
        return getStateName();
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
      case ECAPackage.RNDQUERY__PRI_OPERATOR:
        setPriOperator((String)newValue);
        return;
      case ECAPackage.RNDQUERY__PRI_VAL:
        setPriVal((Integer)newValue);
        return;
      case ECAPackage.RNDQUERY__SEL:
        setSel((String)newValue);
        return;
      case ECAPackage.RNDQUERY__STATE_NAME:
        setStateName((String)newValue);
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
      case ECAPackage.RNDQUERY__PRI_OPERATOR:
        setPriOperator(PRI_OPERATOR_EDEFAULT);
        return;
      case ECAPackage.RNDQUERY__PRI_VAL:
        setPriVal(PRI_VAL_EDEFAULT);
        return;
      case ECAPackage.RNDQUERY__SEL:
        setSel(SEL_EDEFAULT);
        return;
      case ECAPackage.RNDQUERY__STATE_NAME:
        setStateName(STATE_NAME_EDEFAULT);
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
      case ECAPackage.RNDQUERY__PRI_OPERATOR:
        return PRI_OPERATOR_EDEFAULT == null ? priOperator != null : !PRI_OPERATOR_EDEFAULT.equals(priOperator);
      case ECAPackage.RNDQUERY__PRI_VAL:
        return priVal != PRI_VAL_EDEFAULT;
      case ECAPackage.RNDQUERY__SEL:
        return SEL_EDEFAULT == null ? sel != null : !SEL_EDEFAULT.equals(sel);
      case ECAPackage.RNDQUERY__STATE_NAME:
        return STATE_NAME_EDEFAULT == null ? stateName != null : !STATE_NAME_EDEFAULT.equals(stateName);
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
    result.append(" (priOperator: ");
    result.append(priOperator);
    result.append(", priVal: ");
    result.append(priVal);
    result.append(", sel: ");
    result.append(sel);
    result.append(", stateName: ");
    result.append(stateName);
    result.append(')');
    return result.toString();
  }

} //RNDQUERYImpl
