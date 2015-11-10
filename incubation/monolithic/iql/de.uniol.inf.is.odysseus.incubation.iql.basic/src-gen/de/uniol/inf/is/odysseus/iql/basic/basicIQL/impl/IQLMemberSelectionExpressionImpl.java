/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Member Selection Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl#getLeftOperand <em>Left Operand</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl#getSel <em>Sel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IQLMemberSelectionExpressionImpl extends IQLExpressionImpl implements IQLMemberSelectionExpression
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
   * The cached value of the '{@link #getSel() <em>Sel</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSel()
   * @generated
   * @ordered
   */
  protected IQLMemberSelection sel;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLMemberSelectionExpressionImpl()
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
    return BasicIQLPackage.Literals.IQL_MEMBER_SELECTION_EXPRESSION;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND, oldLeftOperand, newLeftOperand);
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
        msgs = ((InternalEObject)leftOperand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND, null, msgs);
      if (newLeftOperand != null)
        msgs = ((InternalEObject)newLeftOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND, null, msgs);
      msgs = basicSetLeftOperand(newLeftOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND, newLeftOperand, newLeftOperand));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMemberSelection getSel()
  {
    return sel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSel(IQLMemberSelection newSel, NotificationChain msgs)
  {
    IQLMemberSelection oldSel = sel;
    sel = newSel;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL, oldSel, newSel);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSel(IQLMemberSelection newSel)
  {
    if (newSel != sel)
    {
      NotificationChain msgs = null;
      if (sel != null)
        msgs = ((InternalEObject)sel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL, null, msgs);
      if (newSel != null)
        msgs = ((InternalEObject)newSel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL, null, msgs);
      msgs = basicSetSel(newSel, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL, newSel, newSel));
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
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND:
        return basicSetLeftOperand(null, msgs);
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL:
        return basicSetSel(null, msgs);
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
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND:
        return getLeftOperand();
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL:
        return getSel();
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
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)newValue);
        return;
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL:
        setSel((IQLMemberSelection)newValue);
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
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND:
        setLeftOperand((IQLExpression)null);
        return;
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL:
        setSel((IQLMemberSelection)null);
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
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND:
        return leftOperand != null;
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION__SEL:
        return sel != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLMemberSelectionExpressionImpl
