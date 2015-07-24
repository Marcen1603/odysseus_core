/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL If Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl#getThenBody <em>Then Body</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl#getElseBody <em>Else Body</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLIfStatementImpl extends IQLStatementImpl implements IQLIfStatement
{
  /**
   * The cached value of the '{@link #getPredicate() <em>Predicate</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredicate()
   * @generated
   * @ordered
   */
  protected IQLExpression predicate;

  /**
   * The cached value of the '{@link #getThenBody() <em>Then Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThenBody()
   * @generated
   * @ordered
   */
  protected IQLStatement thenBody;

  /**
   * The cached value of the '{@link #getElseBody() <em>Else Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElseBody()
   * @generated
   * @ordered
   */
  protected IQLStatement elseBody;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLIfStatementImpl()
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
    return BasicIQLPackage.Literals.IQL_IF_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getPredicate()
  {
    return predicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPredicate(IQLExpression newPredicate, NotificationChain msgs)
  {
    IQLExpression oldPredicate = predicate;
    predicate = newPredicate;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE, oldPredicate, newPredicate);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPredicate(IQLExpression newPredicate)
  {
    if (newPredicate != predicate)
    {
      NotificationChain msgs = null;
      if (predicate != null)
        msgs = ((InternalEObject)predicate).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE, null, msgs);
      if (newPredicate != null)
        msgs = ((InternalEObject)newPredicate).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE, null, msgs);
      msgs = basicSetPredicate(newPredicate, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE, newPredicate, newPredicate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getThenBody()
  {
    return thenBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetThenBody(IQLStatement newThenBody, NotificationChain msgs)
  {
    IQLStatement oldThenBody = thenBody;
    thenBody = newThenBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY, oldThenBody, newThenBody);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setThenBody(IQLStatement newThenBody)
  {
    if (newThenBody != thenBody)
    {
      NotificationChain msgs = null;
      if (thenBody != null)
        msgs = ((InternalEObject)thenBody).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY, null, msgs);
      if (newThenBody != null)
        msgs = ((InternalEObject)newThenBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY, null, msgs);
      msgs = basicSetThenBody(newThenBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY, newThenBody, newThenBody));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getElseBody()
  {
    return elseBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetElseBody(IQLStatement newElseBody, NotificationChain msgs)
  {
    IQLStatement oldElseBody = elseBody;
    elseBody = newElseBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY, oldElseBody, newElseBody);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setElseBody(IQLStatement newElseBody)
  {
    if (newElseBody != elseBody)
    {
      NotificationChain msgs = null;
      if (elseBody != null)
        msgs = ((InternalEObject)elseBody).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY, null, msgs);
      if (newElseBody != null)
        msgs = ((InternalEObject)newElseBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY, null, msgs);
      msgs = basicSetElseBody(newElseBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY, newElseBody, newElseBody));
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
      case BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE:
        return basicSetPredicate(null, msgs);
      case BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY:
        return basicSetThenBody(null, msgs);
      case BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY:
        return basicSetElseBody(null, msgs);
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
      case BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE:
        return getPredicate();
      case BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY:
        return getThenBody();
      case BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY:
        return getElseBody();
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
      case BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE:
        setPredicate((IQLExpression)newValue);
        return;
      case BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY:
        setThenBody((IQLStatement)newValue);
        return;
      case BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY:
        setElseBody((IQLStatement)newValue);
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
      case BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE:
        setPredicate((IQLExpression)null);
        return;
      case BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY:
        setThenBody((IQLStatement)null);
        return;
      case BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY:
        setElseBody((IQLStatement)null);
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
      case BasicIQLPackage.IQL_IF_STATEMENT__PREDICATE:
        return predicate != null;
      case BasicIQLPackage.IQL_IF_STATEMENT__THEN_BODY:
        return thenBody != null;
      case BasicIQLPackage.IQL_IF_STATEMENT__ELSE_BODY:
        return elseBody != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLIfStatementImpl
