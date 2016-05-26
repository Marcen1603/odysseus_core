/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Aggregate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl#getAggregations <em>Aggregations</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl#getGroupby <em>Groupby</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AggregateImpl extends MinimalEObjectImpl.Container implements Aggregate
{
  /**
   * The cached value of the '{@link #getAggregations() <em>Aggregations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAggregations()
   * @generated
   * @ordered
   */
  protected EList<Aggregation> aggregations;

  /**
   * The cached value of the '{@link #getGroupby() <em>Groupby</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGroupby()
   * @generated
   * @ordered
   */
  protected GroupBy groupby;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AggregateImpl()
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
    return StreamingsparqlPackage.Literals.AGGREGATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Aggregation> getAggregations()
  {
    if (aggregations == null)
    {
      aggregations = new EObjectContainmentEList<Aggregation>(Aggregation.class, this, StreamingsparqlPackage.AGGREGATE__AGGREGATIONS);
    }
    return aggregations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupBy getGroupby()
  {
    return groupby;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGroupby(GroupBy newGroupby, NotificationChain msgs)
  {
    GroupBy oldGroupby = groupby;
    groupby = newGroupby;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATE__GROUPBY, oldGroupby, newGroupby);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGroupby(GroupBy newGroupby)
  {
    if (newGroupby != groupby)
    {
      NotificationChain msgs = null;
      if (groupby != null)
        msgs = ((InternalEObject)groupby).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.AGGREGATE__GROUPBY, null, msgs);
      if (newGroupby != null)
        msgs = ((InternalEObject)newGroupby).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.AGGREGATE__GROUPBY, null, msgs);
      msgs = basicSetGroupby(newGroupby, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.AGGREGATE__GROUPBY, newGroupby, newGroupby));
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
      case StreamingsparqlPackage.AGGREGATE__AGGREGATIONS:
        return ((InternalEList<?>)getAggregations()).basicRemove(otherEnd, msgs);
      case StreamingsparqlPackage.AGGREGATE__GROUPBY:
        return basicSetGroupby(null, msgs);
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
      case StreamingsparqlPackage.AGGREGATE__AGGREGATIONS:
        return getAggregations();
      case StreamingsparqlPackage.AGGREGATE__GROUPBY:
        return getGroupby();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case StreamingsparqlPackage.AGGREGATE__AGGREGATIONS:
        getAggregations().clear();
        getAggregations().addAll((Collection<? extends Aggregation>)newValue);
        return;
      case StreamingsparqlPackage.AGGREGATE__GROUPBY:
        setGroupby((GroupBy)newValue);
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
      case StreamingsparqlPackage.AGGREGATE__AGGREGATIONS:
        getAggregations().clear();
        return;
      case StreamingsparqlPackage.AGGREGATE__GROUPBY:
        setGroupby((GroupBy)null);
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
      case StreamingsparqlPackage.AGGREGATE__AGGREGATIONS:
        return aggregations != null && !aggregations.isEmpty();
      case StreamingsparqlPackage.AGGREGATE__GROUPBY:
        return groupby != null;
    }
    return super.eIsSet(featureID);
  }

} //AggregateImpl
