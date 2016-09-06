/**
 */
package de.uniol.inf.is.odysseus.pql2.impl;

import de.uniol.inf.is.odysseus.pql2.Operator;
import de.uniol.inf.is.odysseus.pql2.OperatorOrQuery;
import de.uniol.inf.is.odysseus.pql2.Pql2Package;
import de.uniol.inf.is.odysseus.pql2.Query;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operator Or Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl#getOutputPort <em>Output Port</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl#getQuery <em>Query</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperatorOrQueryImpl extends MinimalEObjectImpl.Container implements OperatorOrQuery
{
  /**
   * The default value of the '{@link #getOutputPort() <em>Output Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputPort()
   * @generated
   * @ordered
   */
  protected static final int OUTPUT_PORT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getOutputPort() <em>Output Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputPort()
   * @generated
   * @ordered
   */
  protected int outputPort = OUTPUT_PORT_EDEFAULT;

  /**
   * The cached value of the '{@link #getOp() <em>Op</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected Operator op;

  /**
   * The cached value of the '{@link #getQuery() <em>Query</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQuery()
   * @generated
   * @ordered
   */
  protected Query query;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperatorOrQueryImpl()
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
    return Pql2Package.Literals.OPERATOR_OR_QUERY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getOutputPort()
  {
    return outputPort;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOutputPort(int newOutputPort)
  {
    int oldOutputPort = outputPort;
    outputPort = newOutputPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR_OR_QUERY__OUTPUT_PORT, oldOutputPort, outputPort));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operator getOp()
  {
    return op;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOp(Operator newOp, NotificationChain msgs)
  {
    Operator oldOp = op;
    op = newOp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR_OR_QUERY__OP, oldOp, newOp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp(Operator newOp)
  {
    if (newOp != op)
    {
      NotificationChain msgs = null;
      if (op != null)
        msgs = ((InternalEObject)op).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR_OR_QUERY__OP, null, msgs);
      if (newOp != null)
        msgs = ((InternalEObject)newOp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Pql2Package.OPERATOR_OR_QUERY__OP, null, msgs);
      msgs = basicSetOp(newOp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR_OR_QUERY__OP, newOp, newOp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Query getQuery()
  {
    if (query != null && query.eIsProxy())
    {
      InternalEObject oldQuery = (InternalEObject)query;
      query = (Query)eResolveProxy(oldQuery);
      if (query != oldQuery)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, Pql2Package.OPERATOR_OR_QUERY__QUERY, oldQuery, query));
      }
    }
    return query;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Query basicGetQuery()
  {
    return query;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQuery(Query newQuery)
  {
    Query oldQuery = query;
    query = newQuery;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, Pql2Package.OPERATOR_OR_QUERY__QUERY, oldQuery, query));
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
      case Pql2Package.OPERATOR_OR_QUERY__OP:
        return basicSetOp(null, msgs);
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
      case Pql2Package.OPERATOR_OR_QUERY__OUTPUT_PORT:
        return getOutputPort();
      case Pql2Package.OPERATOR_OR_QUERY__OP:
        return getOp();
      case Pql2Package.OPERATOR_OR_QUERY__QUERY:
        if (resolve) return getQuery();
        return basicGetQuery();
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
      case Pql2Package.OPERATOR_OR_QUERY__OUTPUT_PORT:
        setOutputPort((Integer)newValue);
        return;
      case Pql2Package.OPERATOR_OR_QUERY__OP:
        setOp((Operator)newValue);
        return;
      case Pql2Package.OPERATOR_OR_QUERY__QUERY:
        setQuery((Query)newValue);
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
      case Pql2Package.OPERATOR_OR_QUERY__OUTPUT_PORT:
        setOutputPort(OUTPUT_PORT_EDEFAULT);
        return;
      case Pql2Package.OPERATOR_OR_QUERY__OP:
        setOp((Operator)null);
        return;
      case Pql2Package.OPERATOR_OR_QUERY__QUERY:
        setQuery((Query)null);
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
      case Pql2Package.OPERATOR_OR_QUERY__OUTPUT_PORT:
        return outputPort != OUTPUT_PORT_EDEFAULT;
      case Pql2Package.OPERATOR_OR_QUERY__OP:
        return op != null;
      case Pql2Package.OPERATOR_OR_QUERY__QUERY:
        return query != null;
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
    result.append(" (outputPort: ");
    result.append(outputPort);
    result.append(')');
    return result.toString();
  }

} //OperatorOrQueryImpl
