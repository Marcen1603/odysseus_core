/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmIdentifiableElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL For Each Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl#getVar <em>Var</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl#getForExpression <em>For Expression</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLForEachStatementImpl extends IQLStatementImpl implements IQLForEachStatement
{
  /**
   * The cached value of the '{@link #getVar() <em>Var</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVar()
   * @generated
   * @ordered
   */
  protected JvmIdentifiableElement var;

  /**
   * The cached value of the '{@link #getForExpression() <em>For Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getForExpression()
   * @generated
   * @ordered
   */
  protected IQLExpression forExpression;

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
  protected IQLForEachStatementImpl()
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
    return BasicIQLPackage.Literals.IQL_FOR_EACH_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmIdentifiableElement getVar()
  {
    return var;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVar(JvmIdentifiableElement newVar, NotificationChain msgs)
  {
    JvmIdentifiableElement oldVar = var;
    var = newVar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR, oldVar, newVar);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVar(JvmIdentifiableElement newVar)
  {
    if (newVar != var)
    {
      NotificationChain msgs = null;
      if (var != null)
        msgs = ((InternalEObject)var).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR, null, msgs);
      if (newVar != null)
        msgs = ((InternalEObject)newVar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR, null, msgs);
      msgs = basicSetVar(newVar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR, newVar, newVar));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression getForExpression()
  {
    return forExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetForExpression(IQLExpression newForExpression, NotificationChain msgs)
  {
    IQLExpression oldForExpression = forExpression;
    forExpression = newForExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION, oldForExpression, newForExpression);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setForExpression(IQLExpression newForExpression)
  {
    if (newForExpression != forExpression)
    {
      NotificationChain msgs = null;
      if (forExpression != null)
        msgs = ((InternalEObject)forExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION, null, msgs);
      if (newForExpression != null)
        msgs = ((InternalEObject)newForExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION, null, msgs);
      msgs = basicSetForExpression(newForExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION, newForExpression, newForExpression));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY, oldBody, newBody);
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
        msgs = ((InternalEObject)body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY, null, msgs);
      if (newBody != null)
        msgs = ((InternalEObject)newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY, null, msgs);
      msgs = basicSetBody(newBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY, newBody, newBody));
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
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR:
        return basicSetVar(null, msgs);
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION:
        return basicSetForExpression(null, msgs);
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY:
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
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR:
        return getVar();
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION:
        return getForExpression();
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY:
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
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR:
        setVar((JvmIdentifiableElement)newValue);
        return;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION:
        setForExpression((IQLExpression)newValue);
        return;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY:
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
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR:
        setVar((JvmIdentifiableElement)null);
        return;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION:
        setForExpression((IQLExpression)null);
        return;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY:
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
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__VAR:
        return var != null;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION:
        return forExpression != null;
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT__BODY:
        return body != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLForEachStatementImpl