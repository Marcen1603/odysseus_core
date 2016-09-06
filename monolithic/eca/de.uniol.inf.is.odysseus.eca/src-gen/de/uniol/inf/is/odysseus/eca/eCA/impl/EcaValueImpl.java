/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.Constant;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eca Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl#getIntValue <em>Int Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl#getIdValue <em>Id Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl#getConstValue <em>Const Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl#getStringValue <em>String Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl#getDoubleValue <em>Double Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EcaValueImpl extends MinimalEObjectImpl.Container implements EcaValue
{
  /**
   * The default value of the '{@link #getIntValue() <em>Int Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntValue()
   * @generated
   * @ordered
   */
  protected static final int INT_VALUE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getIntValue() <em>Int Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntValue()
   * @generated
   * @ordered
   */
  protected int intValue = INT_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getIdValue() <em>Id Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIdValue()
   * @generated
   * @ordered
   */
  protected static final String ID_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIdValue() <em>Id Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIdValue()
   * @generated
   * @ordered
   */
  protected String idValue = ID_VALUE_EDEFAULT;

  /**
   * The cached value of the '{@link #getConstValue() <em>Const Value</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConstValue()
   * @generated
   * @ordered
   */
  protected Constant constValue;

  /**
   * The default value of the '{@link #getStringValue() <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStringValue()
   * @generated
   * @ordered
   */
  protected static final String STRING_VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStringValue() <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStringValue()
   * @generated
   * @ordered
   */
  protected String stringValue = STRING_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getDoubleValue() <em>Double Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDoubleValue()
   * @generated
   * @ordered
   */
  protected static final double DOUBLE_VALUE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getDoubleValue() <em>Double Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDoubleValue()
   * @generated
   * @ordered
   */
  protected double doubleValue = DOUBLE_VALUE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EcaValueImpl()
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
    return ECAPackage.Literals.ECA_VALUE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getIntValue()
  {
    return intValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIntValue(int newIntValue)
  {
    int oldIntValue = intValue;
    intValue = newIntValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.ECA_VALUE__INT_VALUE, oldIntValue, intValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getIdValue()
  {
    return idValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIdValue(String newIdValue)
  {
    String oldIdValue = idValue;
    idValue = newIdValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.ECA_VALUE__ID_VALUE, oldIdValue, idValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Constant getConstValue()
  {
    if (constValue != null && constValue.eIsProxy())
    {
      InternalEObject oldConstValue = (InternalEObject)constValue;
      constValue = (Constant)eResolveProxy(oldConstValue);
      if (constValue != oldConstValue)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ECAPackage.ECA_VALUE__CONST_VALUE, oldConstValue, constValue));
      }
    }
    return constValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Constant basicGetConstValue()
  {
    return constValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConstValue(Constant newConstValue)
  {
    Constant oldConstValue = constValue;
    constValue = newConstValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.ECA_VALUE__CONST_VALUE, oldConstValue, constValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getStringValue()
  {
    return stringValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStringValue(String newStringValue)
  {
    String oldStringValue = stringValue;
    stringValue = newStringValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.ECA_VALUE__STRING_VALUE, oldStringValue, stringValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public double getDoubleValue()
  {
    return doubleValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDoubleValue(double newDoubleValue)
  {
    double oldDoubleValue = doubleValue;
    doubleValue = newDoubleValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.ECA_VALUE__DOUBLE_VALUE, oldDoubleValue, doubleValue));
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
      case ECAPackage.ECA_VALUE__INT_VALUE:
        return getIntValue();
      case ECAPackage.ECA_VALUE__ID_VALUE:
        return getIdValue();
      case ECAPackage.ECA_VALUE__CONST_VALUE:
        if (resolve) return getConstValue();
        return basicGetConstValue();
      case ECAPackage.ECA_VALUE__STRING_VALUE:
        return getStringValue();
      case ECAPackage.ECA_VALUE__DOUBLE_VALUE:
        return getDoubleValue();
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
      case ECAPackage.ECA_VALUE__INT_VALUE:
        setIntValue((Integer)newValue);
        return;
      case ECAPackage.ECA_VALUE__ID_VALUE:
        setIdValue((String)newValue);
        return;
      case ECAPackage.ECA_VALUE__CONST_VALUE:
        setConstValue((Constant)newValue);
        return;
      case ECAPackage.ECA_VALUE__STRING_VALUE:
        setStringValue((String)newValue);
        return;
      case ECAPackage.ECA_VALUE__DOUBLE_VALUE:
        setDoubleValue((Double)newValue);
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
      case ECAPackage.ECA_VALUE__INT_VALUE:
        setIntValue(INT_VALUE_EDEFAULT);
        return;
      case ECAPackage.ECA_VALUE__ID_VALUE:
        setIdValue(ID_VALUE_EDEFAULT);
        return;
      case ECAPackage.ECA_VALUE__CONST_VALUE:
        setConstValue((Constant)null);
        return;
      case ECAPackage.ECA_VALUE__STRING_VALUE:
        setStringValue(STRING_VALUE_EDEFAULT);
        return;
      case ECAPackage.ECA_VALUE__DOUBLE_VALUE:
        setDoubleValue(DOUBLE_VALUE_EDEFAULT);
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
      case ECAPackage.ECA_VALUE__INT_VALUE:
        return intValue != INT_VALUE_EDEFAULT;
      case ECAPackage.ECA_VALUE__ID_VALUE:
        return ID_VALUE_EDEFAULT == null ? idValue != null : !ID_VALUE_EDEFAULT.equals(idValue);
      case ECAPackage.ECA_VALUE__CONST_VALUE:
        return constValue != null;
      case ECAPackage.ECA_VALUE__STRING_VALUE:
        return STRING_VALUE_EDEFAULT == null ? stringValue != null : !STRING_VALUE_EDEFAULT.equals(stringValue);
      case ECAPackage.ECA_VALUE__DOUBLE_VALUE:
        return doubleValue != DOUBLE_VALUE_EDEFAULT;
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
    result.append(" (intValue: ");
    result.append(intValue);
    result.append(", idValue: ");
    result.append(idValue);
    result.append(", stringValue: ");
    result.append(stringValue);
    result.append(", doubleValue: ");
    result.append(doubleValue);
    result.append(')');
    return result.toString();
  }

} //EcaValueImpl
