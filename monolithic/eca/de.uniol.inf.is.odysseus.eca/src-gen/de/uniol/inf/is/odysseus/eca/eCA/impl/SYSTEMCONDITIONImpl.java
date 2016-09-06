/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SYSTEMCONDITION</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl#getSystemAttribute <em>System Attribute</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SYSTEMCONDITIONImpl extends MinimalEObjectImpl.Container implements SYSTEMCONDITION
{
  /**
   * The default value of the '{@link #getSystemAttribute() <em>System Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemAttribute()
   * @generated
   * @ordered
   */
  protected static final String SYSTEM_ATTRIBUTE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSystemAttribute() <em>System Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemAttribute()
   * @generated
   * @ordered
   */
  protected String systemAttribute = SYSTEM_ATTRIBUTE_EDEFAULT;

  /**
   * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperator()
   * @generated
   * @ordered
   */
  protected static final String OPERATOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperator()
   * @generated
   * @ordered
   */
  protected String operator = OPERATOR_EDEFAULT;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected EcaValue value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SYSTEMCONDITIONImpl()
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
    return ECAPackage.Literals.SYSTEMCONDITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSystemAttribute()
  {
    return systemAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSystemAttribute(String newSystemAttribute)
  {
    String oldSystemAttribute = systemAttribute;
    systemAttribute = newSystemAttribute;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SYSTEMCONDITION__SYSTEM_ATTRIBUTE, oldSystemAttribute, systemAttribute));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOperator()
  {
    return operator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperator(String newOperator)
  {
    String oldOperator = operator;
    operator = newOperator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SYSTEMCONDITION__OPERATOR, oldOperator, operator));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EcaValue getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetValue(EcaValue newValue, NotificationChain msgs)
  {
    EcaValue oldValue = value;
    value = newValue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.SYSTEMCONDITION__VALUE, oldValue, newValue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValue(EcaValue newValue)
  {
    if (newValue != value)
    {
      NotificationChain msgs = null;
      if (value != null)
        msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SYSTEMCONDITION__VALUE, null, msgs);
      if (newValue != null)
        msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SYSTEMCONDITION__VALUE, null, msgs);
      msgs = basicSetValue(newValue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SYSTEMCONDITION__VALUE, newValue, newValue));
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
      case ECAPackage.SYSTEMCONDITION__VALUE:
        return basicSetValue(null, msgs);
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
      case ECAPackage.SYSTEMCONDITION__SYSTEM_ATTRIBUTE:
        return getSystemAttribute();
      case ECAPackage.SYSTEMCONDITION__OPERATOR:
        return getOperator();
      case ECAPackage.SYSTEMCONDITION__VALUE:
        return getValue();
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
      case ECAPackage.SYSTEMCONDITION__SYSTEM_ATTRIBUTE:
        setSystemAttribute((String)newValue);
        return;
      case ECAPackage.SYSTEMCONDITION__OPERATOR:
        setOperator((String)newValue);
        return;
      case ECAPackage.SYSTEMCONDITION__VALUE:
        setValue((EcaValue)newValue);
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
      case ECAPackage.SYSTEMCONDITION__SYSTEM_ATTRIBUTE:
        setSystemAttribute(SYSTEM_ATTRIBUTE_EDEFAULT);
        return;
      case ECAPackage.SYSTEMCONDITION__OPERATOR:
        setOperator(OPERATOR_EDEFAULT);
        return;
      case ECAPackage.SYSTEMCONDITION__VALUE:
        setValue((EcaValue)null);
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
      case ECAPackage.SYSTEMCONDITION__SYSTEM_ATTRIBUTE:
        return SYSTEM_ATTRIBUTE_EDEFAULT == null ? systemAttribute != null : !SYSTEM_ATTRIBUTE_EDEFAULT.equals(systemAttribute);
      case ECAPackage.SYSTEMCONDITION__OPERATOR:
        return OPERATOR_EDEFAULT == null ? operator != null : !OPERATOR_EDEFAULT.equals(operator);
      case ECAPackage.SYSTEMCONDITION__VALUE:
        return value != null;
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
    result.append(" (systemAttribute: ");
    result.append(systemAttribute);
    result.append(", operator: ");
    result.append(operator);
    result.append(')');
    return result.toString();
  }

} //SYSTEMCONDITIONImpl
