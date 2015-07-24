/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Type Cast Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl#getTargetRef <em>Target Ref</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl#getOperand <em>Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLTypeCastExpressionImpl extends IQLExpressionImpl implements IQLTypeCastExpression
{
  /**
   * The cached value of the '{@link #getTargetRef() <em>Target Ref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetRef()
   * @generated
   * @ordered
   */
  protected JvmTypeReference targetRef;

  /**
   * The cached value of the '{@link #getOperand() <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperand()
   * @generated
   * @ordered
   */
  protected IQLExpression operand;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLTypeCastExpressionImpl()
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
    return BasicIQLPackage.Literals.IQL_TYPE_CAST_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmTypeReference getTargetRef()
  {
    return targetRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTargetRef(JvmTypeReference newTargetRef, NotificationChain msgs)
  {
    JvmTypeReference oldTargetRef = targetRef;
    targetRef = newTargetRef;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF, oldTargetRef, newTargetRef);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetRef(JvmTypeReference newTargetRef)
  {
    if (newTargetRef != targetRef)
    {
      NotificationChain msgs = null;
      if (targetRef != null)
        msgs = ((InternalEObject)targetRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF, null, msgs);
      if (newTargetRef != null)
        msgs = ((InternalEObject)newTargetRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF, null, msgs);
      msgs = basicSetTargetRef(newTargetRef, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF, newTargetRef, newTargetRef));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getOperand()
  {
    return operand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOperand(IQLExpression newOperand, NotificationChain msgs)
  {
    IQLExpression oldOperand = operand;
    operand = newOperand;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND, oldOperand, newOperand);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperand(IQLExpression newOperand)
  {
    if (newOperand != operand)
    {
      NotificationChain msgs = null;
      if (operand != null)
        msgs = ((InternalEObject)operand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND, null, msgs);
      if (newOperand != null)
        msgs = ((InternalEObject)newOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND, null, msgs);
      msgs = basicSetOperand(newOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND, newOperand, newOperand));
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
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF:
        return basicSetTargetRef(null, msgs);
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND:
        return basicSetOperand(null, msgs);
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
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF:
        return getTargetRef();
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND:
        return getOperand();
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
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF:
        setTargetRef((JvmTypeReference)newValue);
        return;
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND:
        setOperand((IQLExpression)newValue);
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
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF:
        setTargetRef((JvmTypeReference)null);
        return;
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND:
        setOperand((IQLExpression)null);
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
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__TARGET_REF:
        return targetRef != null;
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION__OPERAND:
        return operand != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLTypeCastExpressionImpl
