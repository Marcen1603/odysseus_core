/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Relational Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl#getLeftOperand <em>Left Operand</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl#getRightOperand <em>Right Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLRelationalExpressionImpl extends IQLExpressionImpl implements IQLRelationalExpression
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
   * The default value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected static final String OP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected String op = OP_EDEFAULT;

  /**
   * The cached value of the '{@link #getRightOperand() <em>Right Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRightOperand()
   * @generated
   * @ordered
   */
  protected IQLExpression rightOperand;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLRelationalExpressionImpl()
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
    return QDLPackage.Literals.IQL_RELATIONAL_EXPRESSION;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND, oldLeftOperand, newLeftOperand);
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
        msgs = ((InternalEObject)leftOperand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND, null, msgs);
      if (newLeftOperand != null)
        msgs = ((InternalEObject)newLeftOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND, null, msgs);
      msgs = basicSetLeftOperand(newLeftOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND, newLeftOperand, newLeftOperand));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOp()
  {
    return op;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp(String newOp)
  {
    String oldOp = op;
    op = newOp;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_RELATIONAL_EXPRESSION__OP, oldOp, op));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getRightOperand()
  {
    return rightOperand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRightOperand(IQLExpression newRightOperand, NotificationChain msgs)
  {
    IQLExpression oldRightOperand = rightOperand;
    rightOperand = newRightOperand;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND, oldRightOperand, newRightOperand);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRightOperand(IQLExpression newRightOperand)
  {
    if (newRightOperand != rightOperand)
    {
      NotificationChain msgs = null;
      if (rightOperand != null)
        msgs = ((InternalEObject)rightOperand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND, null, msgs);
      if (newRightOperand != null)
        msgs = ((InternalEObject)newRightOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND, null, msgs);
      msgs = basicSetRightOperand(newRightOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND, newRightOperand, newRightOperand));
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
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND:
        return basicSetLeftOperand(null, msgs);
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND:
        return basicSetRightOperand(null, msgs);
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
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND:
        return getLeftOperand();
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__OP:
        return getOp();
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND:
        return getRightOperand();
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
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)newValue);
        return;
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__OP:
        setOp((String)newValue);
        return;
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND:
        setRightOperand((IQLExpression)newValue);
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
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)null);
        return;
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__OP:
        setOp(OP_EDEFAULT);
        return;
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND:
        setRightOperand((IQLExpression)null);
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
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND:
        return leftOperand != null;
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__OP:
        return OP_EDEFAULT == null ? op != null : !OP_EDEFAULT.equals(op);
      case QDLPackage.IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND:
        return rightOperand != null;
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
    result.append(" (op: ");
    result.append(op);
    result.append(')');
    return result.toString();
  }

} //IQLRelationalExpressionImpl