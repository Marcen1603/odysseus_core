/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate;
import de.uniol.inf.is.odysseus.server.streamingsparql.Base;
import de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Prefix;
import de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;
import de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Select Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getBase <em>Base</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getPrefixes <em>Prefixes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getDatasetClauses <em>Dataset Clauses</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#isIsDistinct <em>Is Distinct</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#isIsReduced <em>Is Reduced</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getWhereClause <em>Where Clause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getFilterclause <em>Filterclause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getAggregateClause <em>Aggregate Clause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl#getFilesinkclause <em>Filesinkclause</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SelectQueryImpl extends SPARQLQueryImpl implements SelectQuery
{
  /**
   * The default value of the '{@link #getMethod() <em>Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethod()
   * @generated
   * @ordered
   */
  protected static final String METHOD_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMethod() <em>Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethod()
   * @generated
   * @ordered
   */
  protected String method = METHOD_EDEFAULT;

  /**
   * The cached value of the '{@link #getBase() <em>Base</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBase()
   * @generated
   * @ordered
   */
  protected Base base;

  /**
   * The cached value of the '{@link #getPrefixes() <em>Prefixes</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrefixes()
   * @generated
   * @ordered
   */
  protected EList<Prefix> prefixes;

  /**
   * The cached value of the '{@link #getDatasetClauses() <em>Dataset Clauses</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatasetClauses()
   * @generated
   * @ordered
   */
  protected EList<DatasetClause> datasetClauses;

  /**
   * The default value of the '{@link #isIsDistinct() <em>Is Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsDistinct()
   * @generated
   * @ordered
   */
  protected static final boolean IS_DISTINCT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsDistinct() <em>Is Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsDistinct()
   * @generated
   * @ordered
   */
  protected boolean isDistinct = IS_DISTINCT_EDEFAULT;

  /**
   * The default value of the '{@link #isIsReduced() <em>Is Reduced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsReduced()
   * @generated
   * @ordered
   */
  protected static final boolean IS_REDUCED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsReduced() <em>Is Reduced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsReduced()
   * @generated
   * @ordered
   */
  protected boolean isReduced = IS_REDUCED_EDEFAULT;

  /**
   * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariables()
   * @generated
   * @ordered
   */
  protected EList<Variable> variables;

  /**
   * The cached value of the '{@link #getWhereClause() <em>Where Clause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWhereClause()
   * @generated
   * @ordered
   */
  protected WhereClause whereClause;

  /**
   * The cached value of the '{@link #getFilterclause() <em>Filterclause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilterclause()
   * @generated
   * @ordered
   */
  protected Filterclause filterclause;

  /**
   * The cached value of the '{@link #getAggregateClause() <em>Aggregate Clause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAggregateClause()
   * @generated
   * @ordered
   */
  protected Aggregate aggregateClause;

  /**
   * The cached value of the '{@link #getFilesinkclause() <em>Filesinkclause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilesinkclause()
   * @generated
   * @ordered
   */
  protected Filesinkclause filesinkclause;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SelectQueryImpl()
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
    return StreamingsparqlPackage.Literals.SELECT_QUERY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMethod(String newMethod)
  {
    String oldMethod = method;
    method = newMethod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__METHOD, oldMethod, method));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Base getBase()
  {
    return base;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBase(Base newBase, NotificationChain msgs)
  {
    Base oldBase = base;
    base = newBase;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__BASE, oldBase, newBase);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBase(Base newBase)
  {
    if (newBase != base)
    {
      NotificationChain msgs = null;
      if (base != null)
        msgs = ((InternalEObject)base).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__BASE, null, msgs);
      if (newBase != null)
        msgs = ((InternalEObject)newBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__BASE, null, msgs);
      msgs = basicSetBase(newBase, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__BASE, newBase, newBase));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Prefix> getPrefixes()
  {
    if (prefixes == null)
    {
      prefixes = new EObjectContainmentEList<Prefix>(Prefix.class, this, StreamingsparqlPackage.SELECT_QUERY__PREFIXES);
    }
    return prefixes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<DatasetClause> getDatasetClauses()
  {
    if (datasetClauses == null)
    {
      datasetClauses = new EObjectContainmentEList<DatasetClause>(DatasetClause.class, this, StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES);
    }
    return datasetClauses;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsDistinct()
  {
    return isDistinct;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsDistinct(boolean newIsDistinct)
  {
    boolean oldIsDistinct = isDistinct;
    isDistinct = newIsDistinct;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__IS_DISTINCT, oldIsDistinct, isDistinct));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsReduced()
  {
    return isReduced;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsReduced(boolean newIsReduced)
  {
    boolean oldIsReduced = isReduced;
    isReduced = newIsReduced;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__IS_REDUCED, oldIsReduced, isReduced));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Variable> getVariables()
  {
    if (variables == null)
    {
      variables = new EObjectContainmentEList<Variable>(Variable.class, this, StreamingsparqlPackage.SELECT_QUERY__VARIABLES);
    }
    return variables;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WhereClause getWhereClause()
  {
    return whereClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWhereClause(WhereClause newWhereClause, NotificationChain msgs)
  {
    WhereClause oldWhereClause = whereClause;
    whereClause = newWhereClause;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE, oldWhereClause, newWhereClause);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWhereClause(WhereClause newWhereClause)
  {
    if (newWhereClause != whereClause)
    {
      NotificationChain msgs = null;
      if (whereClause != null)
        msgs = ((InternalEObject)whereClause).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE, null, msgs);
      if (newWhereClause != null)
        msgs = ((InternalEObject)newWhereClause).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE, null, msgs);
      msgs = basicSetWhereClause(newWhereClause, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE, newWhereClause, newWhereClause));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Filterclause getFilterclause()
  {
    return filterclause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFilterclause(Filterclause newFilterclause, NotificationChain msgs)
  {
    Filterclause oldFilterclause = filterclause;
    filterclause = newFilterclause;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE, oldFilterclause, newFilterclause);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilterclause(Filterclause newFilterclause)
  {
    if (newFilterclause != filterclause)
    {
      NotificationChain msgs = null;
      if (filterclause != null)
        msgs = ((InternalEObject)filterclause).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE, null, msgs);
      if (newFilterclause != null)
        msgs = ((InternalEObject)newFilterclause).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE, null, msgs);
      msgs = basicSetFilterclause(newFilterclause, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE, newFilterclause, newFilterclause));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Aggregate getAggregateClause()
  {
    return aggregateClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAggregateClause(Aggregate newAggregateClause, NotificationChain msgs)
  {
    Aggregate oldAggregateClause = aggregateClause;
    aggregateClause = newAggregateClause;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE, oldAggregateClause, newAggregateClause);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAggregateClause(Aggregate newAggregateClause)
  {
    if (newAggregateClause != aggregateClause)
    {
      NotificationChain msgs = null;
      if (aggregateClause != null)
        msgs = ((InternalEObject)aggregateClause).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE, null, msgs);
      if (newAggregateClause != null)
        msgs = ((InternalEObject)newAggregateClause).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE, null, msgs);
      msgs = basicSetAggregateClause(newAggregateClause, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE, newAggregateClause, newAggregateClause));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Filesinkclause getFilesinkclause()
  {
    return filesinkclause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFilesinkclause(Filesinkclause newFilesinkclause, NotificationChain msgs)
  {
    Filesinkclause oldFilesinkclause = filesinkclause;
    filesinkclause = newFilesinkclause;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE, oldFilesinkclause, newFilesinkclause);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilesinkclause(Filesinkclause newFilesinkclause)
  {
    if (newFilesinkclause != filesinkclause)
    {
      NotificationChain msgs = null;
      if (filesinkclause != null)
        msgs = ((InternalEObject)filesinkclause).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE, null, msgs);
      if (newFilesinkclause != null)
        msgs = ((InternalEObject)newFilesinkclause).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE, null, msgs);
      msgs = basicSetFilesinkclause(newFilesinkclause, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE, newFilesinkclause, newFilesinkclause));
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
      case StreamingsparqlPackage.SELECT_QUERY__BASE:
        return basicSetBase(null, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__PREFIXES:
        return ((InternalEList<?>)getPrefixes()).basicRemove(otherEnd, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES:
        return ((InternalEList<?>)getDatasetClauses()).basicRemove(otherEnd, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__VARIABLES:
        return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE:
        return basicSetWhereClause(null, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE:
        return basicSetFilterclause(null, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE:
        return basicSetAggregateClause(null, msgs);
      case StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE:
        return basicSetFilesinkclause(null, msgs);
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
      case StreamingsparqlPackage.SELECT_QUERY__METHOD:
        return getMethod();
      case StreamingsparqlPackage.SELECT_QUERY__BASE:
        return getBase();
      case StreamingsparqlPackage.SELECT_QUERY__PREFIXES:
        return getPrefixes();
      case StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES:
        return getDatasetClauses();
      case StreamingsparqlPackage.SELECT_QUERY__IS_DISTINCT:
        return isIsDistinct();
      case StreamingsparqlPackage.SELECT_QUERY__IS_REDUCED:
        return isIsReduced();
      case StreamingsparqlPackage.SELECT_QUERY__VARIABLES:
        return getVariables();
      case StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE:
        return getWhereClause();
      case StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE:
        return getFilterclause();
      case StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE:
        return getAggregateClause();
      case StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE:
        return getFilesinkclause();
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
      case StreamingsparqlPackage.SELECT_QUERY__METHOD:
        setMethod((String)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__BASE:
        setBase((Base)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__PREFIXES:
        getPrefixes().clear();
        getPrefixes().addAll((Collection<? extends Prefix>)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES:
        getDatasetClauses().clear();
        getDatasetClauses().addAll((Collection<? extends DatasetClause>)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__IS_DISTINCT:
        setIsDistinct((Boolean)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__IS_REDUCED:
        setIsReduced((Boolean)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__VARIABLES:
        getVariables().clear();
        getVariables().addAll((Collection<? extends Variable>)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE:
        setWhereClause((WhereClause)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE:
        setFilterclause((Filterclause)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE:
        setAggregateClause((Aggregate)newValue);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE:
        setFilesinkclause((Filesinkclause)newValue);
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
      case StreamingsparqlPackage.SELECT_QUERY__METHOD:
        setMethod(METHOD_EDEFAULT);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__BASE:
        setBase((Base)null);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__PREFIXES:
        getPrefixes().clear();
        return;
      case StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES:
        getDatasetClauses().clear();
        return;
      case StreamingsparqlPackage.SELECT_QUERY__IS_DISTINCT:
        setIsDistinct(IS_DISTINCT_EDEFAULT);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__IS_REDUCED:
        setIsReduced(IS_REDUCED_EDEFAULT);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__VARIABLES:
        getVariables().clear();
        return;
      case StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE:
        setWhereClause((WhereClause)null);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE:
        setFilterclause((Filterclause)null);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE:
        setAggregateClause((Aggregate)null);
        return;
      case StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE:
        setFilesinkclause((Filesinkclause)null);
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
      case StreamingsparqlPackage.SELECT_QUERY__METHOD:
        return METHOD_EDEFAULT == null ? method != null : !METHOD_EDEFAULT.equals(method);
      case StreamingsparqlPackage.SELECT_QUERY__BASE:
        return base != null;
      case StreamingsparqlPackage.SELECT_QUERY__PREFIXES:
        return prefixes != null && !prefixes.isEmpty();
      case StreamingsparqlPackage.SELECT_QUERY__DATASET_CLAUSES:
        return datasetClauses != null && !datasetClauses.isEmpty();
      case StreamingsparqlPackage.SELECT_QUERY__IS_DISTINCT:
        return isDistinct != IS_DISTINCT_EDEFAULT;
      case StreamingsparqlPackage.SELECT_QUERY__IS_REDUCED:
        return isReduced != IS_REDUCED_EDEFAULT;
      case StreamingsparqlPackage.SELECT_QUERY__VARIABLES:
        return variables != null && !variables.isEmpty();
      case StreamingsparqlPackage.SELECT_QUERY__WHERE_CLAUSE:
        return whereClause != null;
      case StreamingsparqlPackage.SELECT_QUERY__FILTERCLAUSE:
        return filterclause != null;
      case StreamingsparqlPackage.SELECT_QUERY__AGGREGATE_CLAUSE:
        return aggregateClause != null;
      case StreamingsparqlPackage.SELECT_QUERY__FILESINKCLAUSE:
        return filesinkclause != null;
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
    result.append(" (method: ");
    result.append(method);
    result.append(", isDistinct: ");
    result.append(isDistinct);
    result.append(", isReduced: ");
    result.append(isReduced);
    result.append(')');
    return result.toString();
  }

} //SelectQueryImpl
