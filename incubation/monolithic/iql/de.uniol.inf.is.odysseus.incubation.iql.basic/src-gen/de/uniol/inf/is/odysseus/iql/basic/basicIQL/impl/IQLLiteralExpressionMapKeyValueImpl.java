/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Literal Expression Map Key Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl#getKey <em>Key</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLLiteralExpressionMapKeyValueImpl extends MinimalEObjectImpl.Container implements IQLLiteralExpressionMapKeyValue
{
  /**
   * The cached value of the '{@link #getKey() <em>Key</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected IQLExpression key;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected IQLExpression value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLLiteralExpressionMapKeyValueImpl()
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
    return BasicIQLPackage.Literals.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getKey()
  {
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetKey(IQLExpression newKey, NotificationChain msgs)
  {
    IQLExpression oldKey = key;
    key = newKey;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY, oldKey, newKey);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKey(IQLExpression newKey)
  {
    if (newKey != key)
    {
      NotificationChain msgs = null;
      if (key != null)
        msgs = ((InternalEObject)key).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY, null, msgs);
      if (newKey != null)
        msgs = ((InternalEObject)newKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY, null, msgs);
      msgs = basicSetKey(newKey, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY, newKey, newKey));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetValue(IQLExpression newValue, NotificationChain msgs)
  {
    IQLExpression oldValue = value;
    value = newValue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE, oldValue, newValue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValue(IQLExpression newValue)
  {
    if (newValue != value)
    {
      NotificationChain msgs = null;
      if (value != null)
        msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE, null, msgs);
      if (newValue != null)
        msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE, null, msgs);
      msgs = basicSetValue(newValue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE, newValue, newValue));
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
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY:
        return basicSetKey(null, msgs);
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE:
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
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY:
        return getKey();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE:
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
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY:
        setKey((IQLExpression)newValue);
        return;
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE:
        setValue((IQLExpression)newValue);
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
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY:
        setKey((IQLExpression)null);
        return;
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE:
        setValue((IQLExpression)null);
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
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY:
        return key != null;
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE:
        return value != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLLiteralExpressionMapKeyValueImpl