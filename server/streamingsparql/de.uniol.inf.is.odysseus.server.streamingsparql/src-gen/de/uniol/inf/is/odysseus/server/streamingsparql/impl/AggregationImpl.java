/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Aggregation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl#getVarToAgg <em>Var To Agg</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl#getAggName <em>Agg Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl#getDatatype <em>Datatype</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AggregationImpl extends MinimalEObjectImpl.Container implements Aggregation
{
  /**
   * The default value of the '{@link #getFunction() <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunction()
   * @generated
   * @ordered
   */
  protected static final String FUNCTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFunction() <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunction()
   * @generated
   * @ordered
   */
  protected String function = FUNCTION_EDEFAULT;

  /**
   * The cached value of the '{@link #getVarToAgg() <em>Var To Agg</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarToAgg()
   * @generated
   * @ordered
   */
  protected Variable varToAgg;

  /**
   * The default value of the '{@link #getAggName() <em>Agg Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAggName()
   * @generated
   * @ordered
   */
  protected static final String AGG_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAggName() <em>Agg Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAggName()
   * @generated
   * @ordered
   */
  protected String aggName = AGG_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getDatatype() <em>Datatype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatatype()
   * @generated
   * @ordered
   */
  protected static final String DATATYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDatatype() <em>Datatype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatatype()
   * @generated
   * @ordered
   */
  protected String datatype = DATATYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AggregationImpl()
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
    return StreamingsparqlPackage.Literals.AGGREGATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFunction()
  {
    return function;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFunction(String newFunction)
  {
    String oldFunction = function;
    function = newFunction;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATION__FUNCTION, oldFunction, function));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable getVarToAgg()
  {
    return varToAgg;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVarToAgg(Variable newVarToAgg, NotificationChain msgs)
  {
    Variable oldVarToAgg = varToAgg;
    varToAgg = newVarToAgg;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG, oldVarToAgg, newVarToAgg);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVarToAgg(Variable newVarToAgg)
  {
    if (newVarToAgg != varToAgg)
    {
      NotificationChain msgs = null;
      if (varToAgg != null)
        msgs = ((InternalEObject)varToAgg).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG, null, msgs);
      if (newVarToAgg != null)
        msgs = ((InternalEObject)newVarToAgg).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG, null, msgs);
      msgs = basicSetVarToAgg(newVarToAgg, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG, newVarToAgg, newVarToAgg));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAggName()
  {
    return aggName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAggName(String newAggName)
  {
    String oldAggName = aggName;
    aggName = newAggName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATION__AGG_NAME, oldAggName, aggName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDatatype()
  {
    return datatype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDatatype(String newDatatype)
  {
    String oldDatatype = datatype;
    datatype = newDatatype;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATION__DATATYPE, oldDatatype, datatype));
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
      case StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG:
        return basicSetVarToAgg(null, msgs);
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
      case StreamingsparqlPackage.AGGREGATION__FUNCTION:
        return getFunction();
      case StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG:
        return getVarToAgg();
      case StreamingsparqlPackage.AGGREGATION__AGG_NAME:
        return getAggName();
      case StreamingsparqlPackage.AGGREGATION__DATATYPE:
        return getDatatype();
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
      case StreamingsparqlPackage.AGGREGATION__FUNCTION:
        setFunction((String)newValue);
        return;
      case StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG:
        setVarToAgg((Variable)newValue);
        return;
      case StreamingsparqlPackage.AGGREGATION__AGG_NAME:
        setAggName((String)newValue);
        return;
      case StreamingsparqlPackage.AGGREGATION__DATATYPE:
        setDatatype((String)newValue);
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
      case StreamingsparqlPackage.AGGREGATION__FUNCTION:
        setFunction(FUNCTION_EDEFAULT);
        return;
      case StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG:
        setVarToAgg((Variable)null);
        return;
      case StreamingsparqlPackage.AGGREGATION__AGG_NAME:
        setAggName(AGG_NAME_EDEFAULT);
        return;
      case StreamingsparqlPackage.AGGREGATION__DATATYPE:
        setDatatype(DATATYPE_EDEFAULT);
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
      case StreamingsparqlPackage.AGGREGATION__FUNCTION:
        return FUNCTION_EDEFAULT == null ? function != null : !FUNCTION_EDEFAULT.equals(function);
      case StreamingsparqlPackage.AGGREGATION__VAR_TO_AGG:
        return varToAgg != null;
      case StreamingsparqlPackage.AGGREGATION__AGG_NAME:
        return AGG_NAME_EDEFAULT == null ? aggName != null : !AGG_NAME_EDEFAULT.equals(aggName);
      case StreamingsparqlPackage.AGGREGATION__DATATYPE:
        return DATATYPE_EDEFAULT == null ? datatype != null : !DATATYPE_EDEFAULT.equals(datatype);
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
    result.append(" (function: ");
    result.append(function);
    result.append(", aggName: ");
    result.append(aggName);
    result.append(", datatype: ");
    result.append(datatype);
    result.append(')');
    return result.toString();
  }

} //AggregationImpl
