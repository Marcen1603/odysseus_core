/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Instance Of Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl#getLeftOperand <em>Left Operand</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl#getTargetRef <em>Target Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLInstanceOfExpressionImpl extends IQLExpressionImpl implements IQLInstanceOfExpression
{
  /**
   * The cached value of the '{@link #getLeftOperand() <em>Left Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLeftOperand()
   * @generated
   * @ordered
   */
  protected IQLExpression leftOperand;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLInstanceOfExpressionImpl()
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
    return QDLPackage.Literals.IQL_INSTANCE_OF_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getLeftOperand()
  {
    return leftOperand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetLeftOperand(IQLExpression newLeftOperand, NotificationChain msgs)
  {
    IQLExpression oldLeftOperand = leftOperand;
    leftOperand = newLeftOperand;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND, oldLeftOperand, newLeftOperand);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLeftOperand(IQLExpression newLeftOperand)
  {
    if (newLeftOperand != leftOperand)
    {
      NotificationChain msgs = null;
      if (leftOperand != null)
        msgs = ((InternalEObject)leftOperand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND, null, msgs);
      if (newLeftOperand != null)
        msgs = ((InternalEObject)newLeftOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND, null, msgs);
      msgs = basicSetLeftOperand(newLeftOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND, newLeftOperand, newLeftOperand));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF, oldTargetRef, newTargetRef);
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
        msgs = ((InternalEObject)targetRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF, null, msgs);
      if (newTargetRef != null)
        msgs = ((InternalEObject)newTargetRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF, null, msgs);
      msgs = basicSetTargetRef(newTargetRef, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF, newTargetRef, newTargetRef));
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
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND:
        return basicSetLeftOperand(null, msgs);
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF:
        return basicSetTargetRef(null, msgs);
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
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND:
        return getLeftOperand();
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF:
        return getTargetRef();
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
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)newValue);
        return;
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF:
        setTargetRef((JvmTypeReference)newValue);
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
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)null);
        return;
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF:
        setTargetRef((JvmTypeReference)null);
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
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND:
        return leftOperand != null;
      case QDLPackage.IQL_INSTANCE_OF_EXPRESSION__TARGET_REF:
        return targetRef != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLInstanceOfExpressionImpl
