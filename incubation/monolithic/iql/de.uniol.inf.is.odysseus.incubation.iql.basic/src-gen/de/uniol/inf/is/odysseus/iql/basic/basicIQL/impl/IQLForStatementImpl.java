/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL For Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl#getVar <em>Var</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl#getUpdateExpr <em>Update Expr</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLForStatementImpl extends IQLStatementImpl implements IQLForStatement
{
  /**
   * The cached value of the '{@link #getVar() <em>Var</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVar()
   * @generated
   * @ordered
   */
  protected IQLStatement var;

  /**
   * The cached value of the '{@link #getPredicate() <em>Predicate</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredicate()
   * @generated
   * @ordered
   */
  protected IQLStatement predicate;

  /**
   * The cached value of the '{@link #getUpdateExpr() <em>Update Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUpdateExpr()
   * @generated
   * @ordered
   */
  protected IQLExpression updateExpr;

  /**
   * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBody()
   * @generated
   * @ordered
   */
  protected IQLStatement body;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLForStatementImpl()
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
    return BasicIQLPackage.Literals.IQL_FOR_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getVar()
  {
    return var;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVar(IQLStatement newVar, NotificationChain msgs)
  {
    IQLStatement oldVar = var;
    var = newVar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__VAR, oldVar, newVar);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVar(IQLStatement newVar)
  {
    if (newVar != var)
    {
      NotificationChain msgs = null;
      if (var != null)
        msgs = ((InternalEObject)var).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__VAR, null, msgs);
      if (newVar != null)
        msgs = ((InternalEObject)newVar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__VAR, null, msgs);
      msgs = basicSetVar(newVar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__VAR, newVar, newVar));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getPredicate()
  {
    return predicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPredicate(IQLStatement newPredicate, NotificationChain msgs)
  {
    IQLStatement oldPredicate = predicate;
    predicate = newPredicate;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE, oldPredicate, newPredicate);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPredicate(IQLStatement newPredicate)
  {
    if (newPredicate != predicate)
    {
      NotificationChain msgs = null;
      if (predicate != null)
        msgs = ((InternalEObject)predicate).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE, null, msgs);
      if (newPredicate != null)
        msgs = ((InternalEObject)newPredicate).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE, null, msgs);
      msgs = basicSetPredicate(newPredicate, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE, newPredicate, newPredicate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getUpdateExpr()
  {
    return updateExpr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetUpdateExpr(IQLExpression newUpdateExpr, NotificationChain msgs)
  {
    IQLExpression oldUpdateExpr = updateExpr;
    updateExpr = newUpdateExpr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR, oldUpdateExpr, newUpdateExpr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUpdateExpr(IQLExpression newUpdateExpr)
  {
    if (newUpdateExpr != updateExpr)
    {
      NotificationChain msgs = null;
      if (updateExpr != null)
        msgs = ((InternalEObject)updateExpr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR, null, msgs);
      if (newUpdateExpr != null)
        msgs = ((InternalEObject)newUpdateExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR, null, msgs);
      msgs = basicSetUpdateExpr(newUpdateExpr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR, newUpdateExpr, newUpdateExpr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getBody()
  {
    return body;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBody(IQLStatement newBody, NotificationChain msgs)
  {
    IQLStatement oldBody = body;
    body = newBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__BODY, oldBody, newBody);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBody(IQLStatement newBody)
  {
    if (newBody != body)
    {
      NotificationChain msgs = null;
      if (body != null)
        msgs = ((InternalEObject)body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__BODY, null, msgs);
      if (newBody != null)
        msgs = ((InternalEObject)newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_STATEMENT__BODY, null, msgs);
      msgs = basicSetBody(newBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_STATEMENT__BODY, newBody, newBody));
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
      case BasicIQLPackage.IQL_FOR_STATEMENT__VAR:
        return basicSetVar(null, msgs);
      case BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE:
        return basicSetPredicate(null, msgs);
      case BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR:
        return basicSetUpdateExpr(null, msgs);
      case BasicIQLPackage.IQL_FOR_STATEMENT__BODY:
        return basicSetBody(null, msgs);
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
      case BasicIQLPackage.IQL_FOR_STATEMENT__VAR:
        return getVar();
      case BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE:
        return getPredicate();
      case BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR:
        return getUpdateExpr();
      case BasicIQLPackage.IQL_FOR_STATEMENT__BODY:
        return getBody();
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
      case BasicIQLPackage.IQL_FOR_STATEMENT__VAR:
        setVar((IQLStatement)newValue);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE:
        setPredicate((IQLStatement)newValue);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR:
        setUpdateExpr((IQLExpression)newValue);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__BODY:
        setBody((IQLStatement)newValue);
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
      case BasicIQLPackage.IQL_FOR_STATEMENT__VAR:
        setVar((IQLStatement)null);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE:
        setPredicate((IQLStatement)null);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR:
        setUpdateExpr((IQLExpression)null);
        return;
      case BasicIQLPackage.IQL_FOR_STATEMENT__BODY:
        setBody((IQLStatement)null);
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
      case BasicIQLPackage.IQL_FOR_STATEMENT__VAR:
        return var != null;
      case BasicIQLPackage.IQL_FOR_STATEMENT__PREDICATE:
        return predicate != null;
      case BasicIQLPackage.IQL_FOR_STATEMENT__UPDATE_EXPR:
        return updateExpr != null;
      case BasicIQLPackage.IQL_FOR_STATEMENT__BODY:
        return body != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLForStatementImpl
