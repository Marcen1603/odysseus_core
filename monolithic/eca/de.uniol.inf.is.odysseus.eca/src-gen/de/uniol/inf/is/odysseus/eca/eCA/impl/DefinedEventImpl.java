/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.Source;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Defined Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl#getDefinedSource <em>Defined Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl#getDefinedAttribute <em>Defined Attribute</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl#getDefinedOperator <em>Defined Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl#getDefinedValue <em>Defined Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DefinedEventImpl extends MinimalEObjectImpl.Container implements DefinedEvent
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getDefinedSource() <em>Defined Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedSource()
   * @generated
   * @ordered
   */
  protected Source definedSource;

  /**
   * The default value of the '{@link #getDefinedAttribute() <em>Defined Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedAttribute()
   * @generated
   * @ordered
   */
  protected static final String DEFINED_ATTRIBUTE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefinedAttribute() <em>Defined Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedAttribute()
   * @generated
   * @ordered
   */
  protected String definedAttribute = DEFINED_ATTRIBUTE_EDEFAULT;

  /**
   * The default value of the '{@link #getDefinedOperator() <em>Defined Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedOperator()
   * @generated
   * @ordered
   */
  protected static final String DEFINED_OPERATOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefinedOperator() <em>Defined Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedOperator()
   * @generated
   * @ordered
   */
  protected String definedOperator = DEFINED_OPERATOR_EDEFAULT;

  /**
   * The cached value of the '{@link #getDefinedValue() <em>Defined Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefinedValue()
   * @generated
   * @ordered
   */
  protected EcaValue definedValue;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DefinedEventImpl()
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
    return ECAPackage.Literals.DEFINED_EVENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Source getDefinedSource()
  {
    return definedSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDefinedSource(Source newDefinedSource, NotificationChain msgs)
  {
    Source oldDefinedSource = definedSource;
    definedSource = newDefinedSource;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_SOURCE, oldDefinedSource, newDefinedSource);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefinedSource(Source newDefinedSource)
  {
    if (newDefinedSource != definedSource)
    {
      NotificationChain msgs = null;
      if (definedSource != null)
        msgs = ((InternalEObject)definedSource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.DEFINED_EVENT__DEFINED_SOURCE, null, msgs);
      if (newDefinedSource != null)
        msgs = ((InternalEObject)newDefinedSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.DEFINED_EVENT__DEFINED_SOURCE, null, msgs);
      msgs = basicSetDefinedSource(newDefinedSource, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_SOURCE, newDefinedSource, newDefinedSource));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefinedAttribute()
  {
    return definedAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefinedAttribute(String newDefinedAttribute)
  {
    String oldDefinedAttribute = definedAttribute;
    definedAttribute = newDefinedAttribute;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_ATTRIBUTE, oldDefinedAttribute, definedAttribute));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefinedOperator()
  {
    return definedOperator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefinedOperator(String newDefinedOperator)
  {
    String oldDefinedOperator = definedOperator;
    definedOperator = newDefinedOperator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_OPERATOR, oldDefinedOperator, definedOperator));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EcaValue getDefinedValue()
  {
    return definedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDefinedValue(EcaValue newDefinedValue, NotificationChain msgs)
  {
    EcaValue oldDefinedValue = definedValue;
    definedValue = newDefinedValue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_VALUE, oldDefinedValue, newDefinedValue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefinedValue(EcaValue newDefinedValue)
  {
    if (newDefinedValue != definedValue)
    {
      NotificationChain msgs = null;
      if (definedValue != null)
        msgs = ((InternalEObject)definedValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.DEFINED_EVENT__DEFINED_VALUE, null, msgs);
      if (newDefinedValue != null)
        msgs = ((InternalEObject)newDefinedValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.DEFINED_EVENT__DEFINED_VALUE, null, msgs);
      msgs = basicSetDefinedValue(newDefinedValue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.DEFINED_EVENT__DEFINED_VALUE, newDefinedValue, newDefinedValue));
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
      case ECAPackage.DEFINED_EVENT__DEFINED_SOURCE:
        return basicSetDefinedSource(null, msgs);
      case ECAPackage.DEFINED_EVENT__DEFINED_VALUE:
        return basicSetDefinedValue(null, msgs);
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
      case ECAPackage.DEFINED_EVENT__NAME:
        return getName();
      case ECAPackage.DEFINED_EVENT__DEFINED_SOURCE:
        return getDefinedSource();
      case ECAPackage.DEFINED_EVENT__DEFINED_ATTRIBUTE:
        return getDefinedAttribute();
      case ECAPackage.DEFINED_EVENT__DEFINED_OPERATOR:
        return getDefinedOperator();
      case ECAPackage.DEFINED_EVENT__DEFINED_VALUE:
        return getDefinedValue();
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
      case ECAPackage.DEFINED_EVENT__NAME:
        setName((String)newValue);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_SOURCE:
        setDefinedSource((Source)newValue);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_ATTRIBUTE:
        setDefinedAttribute((String)newValue);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_OPERATOR:
        setDefinedOperator((String)newValue);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_VALUE:
        setDefinedValue((EcaValue)newValue);
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
      case ECAPackage.DEFINED_EVENT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_SOURCE:
        setDefinedSource((Source)null);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_ATTRIBUTE:
        setDefinedAttribute(DEFINED_ATTRIBUTE_EDEFAULT);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_OPERATOR:
        setDefinedOperator(DEFINED_OPERATOR_EDEFAULT);
        return;
      case ECAPackage.DEFINED_EVENT__DEFINED_VALUE:
        setDefinedValue((EcaValue)null);
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
      case ECAPackage.DEFINED_EVENT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ECAPackage.DEFINED_EVENT__DEFINED_SOURCE:
        return definedSource != null;
      case ECAPackage.DEFINED_EVENT__DEFINED_ATTRIBUTE:
        return DEFINED_ATTRIBUTE_EDEFAULT == null ? definedAttribute != null : !DEFINED_ATTRIBUTE_EDEFAULT.equals(definedAttribute);
      case ECAPackage.DEFINED_EVENT__DEFINED_OPERATOR:
        return DEFINED_OPERATOR_EDEFAULT == null ? definedOperator != null : !DEFINED_OPERATOR_EDEFAULT.equals(definedOperator);
      case ECAPackage.DEFINED_EVENT__DEFINED_VALUE:
        return definedValue != null;
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
    result.append(" (name: ");
    result.append(name);
    result.append(", definedAttribute: ");
    result.append(definedAttribute);
    result.append(", definedOperator: ");
    result.append(definedOperator);
    result.append(')');
    return result.toString();
  }

} //DefinedEventImpl
