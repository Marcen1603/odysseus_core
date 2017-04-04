/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source;

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
 * An implementation of the model object '<em><b>Select</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getDistinct <em>Distinct</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getSources <em>Sources</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getPredicates <em>Predicates</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getOrder <em>Order</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SelectImpl#getHaving <em>Having</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SelectImpl extends MinimalEObjectImpl.Container implements Select
{
  /**
   * The default value of the '{@link #getDistinct() <em>Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistinct()
   * @generated
   * @ordered
   */
  protected static final String DISTINCT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDistinct() <em>Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistinct()
   * @generated
   * @ordered
   */
  protected String distinct = DISTINCT_EDEFAULT;

  /**
   * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArguments()
   * @generated
   * @ordered
   */
  protected EList<SelectArgument> arguments;

  /**
   * The cached value of the '{@link #getSources() <em>Sources</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSources()
   * @generated
   * @ordered
   */
  protected EList<Source> sources;

  /**
   * The cached value of the '{@link #getPredicates() <em>Predicates</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredicates()
   * @generated
   * @ordered
   */
  protected ExpressionsModel predicates;

  /**
   * The cached value of the '{@link #getOrder() <em>Order</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrder()
   * @generated
   * @ordered
   */
  protected EList<Attribute> order;

  /**
   * The cached value of the '{@link #getHaving() <em>Having</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHaving()
   * @generated
   * @ordered
   */
  protected ExpressionsModel having;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SelectImpl()
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
    return CQLPackage.Literals.SELECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDistinct()
  {
    return distinct;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDistinct(String newDistinct)
  {
    String oldDistinct = distinct;
    distinct = newDistinct;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.SELECT__DISTINCT, oldDistinct, distinct));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<SelectArgument> getArguments()
  {
    if (arguments == null)
    {
      arguments = new EObjectContainmentEList<SelectArgument>(SelectArgument.class, this, CQLPackage.SELECT__ARGUMENTS);
    }
    return arguments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Source> getSources()
  {
    if (sources == null)
    {
      sources = new EObjectContainmentEList<Source>(Source.class, this, CQLPackage.SELECT__SOURCES);
    }
    return sources;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionsModel getPredicates()
  {
    return predicates;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPredicates(ExpressionsModel newPredicates, NotificationChain msgs)
  {
    ExpressionsModel oldPredicates = predicates;
    predicates = newPredicates;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.SELECT__PREDICATES, oldPredicates, newPredicates);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPredicates(ExpressionsModel newPredicates)
  {
    if (newPredicates != predicates)
    {
      NotificationChain msgs = null;
      if (predicates != null)
        msgs = ((InternalEObject)predicates).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SELECT__PREDICATES, null, msgs);
      if (newPredicates != null)
        msgs = ((InternalEObject)newPredicates).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SELECT__PREDICATES, null, msgs);
      msgs = basicSetPredicates(newPredicates, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.SELECT__PREDICATES, newPredicates, newPredicates));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Attribute> getOrder()
  {
    if (order == null)
    {
      order = new EObjectContainmentEList<Attribute>(Attribute.class, this, CQLPackage.SELECT__ORDER);
    }
    return order;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionsModel getHaving()
  {
    return having;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetHaving(ExpressionsModel newHaving, NotificationChain msgs)
  {
    ExpressionsModel oldHaving = having;
    having = newHaving;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.SELECT__HAVING, oldHaving, newHaving);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHaving(ExpressionsModel newHaving)
  {
    if (newHaving != having)
    {
      NotificationChain msgs = null;
      if (having != null)
        msgs = ((InternalEObject)having).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SELECT__HAVING, null, msgs);
      if (newHaving != null)
        msgs = ((InternalEObject)newHaving).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SELECT__HAVING, null, msgs);
      msgs = basicSetHaving(newHaving, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.SELECT__HAVING, newHaving, newHaving));
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
      case CQLPackage.SELECT__ARGUMENTS:
        return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
      case CQLPackage.SELECT__SOURCES:
        return ((InternalEList<?>)getSources()).basicRemove(otherEnd, msgs);
      case CQLPackage.SELECT__PREDICATES:
        return basicSetPredicates(null, msgs);
      case CQLPackage.SELECT__ORDER:
        return ((InternalEList<?>)getOrder()).basicRemove(otherEnd, msgs);
      case CQLPackage.SELECT__HAVING:
        return basicSetHaving(null, msgs);
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
      case CQLPackage.SELECT__DISTINCT:
        return getDistinct();
      case CQLPackage.SELECT__ARGUMENTS:
        return getArguments();
      case CQLPackage.SELECT__SOURCES:
        return getSources();
      case CQLPackage.SELECT__PREDICATES:
        return getPredicates();
      case CQLPackage.SELECT__ORDER:
        return getOrder();
      case CQLPackage.SELECT__HAVING:
        return getHaving();
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
      case CQLPackage.SELECT__DISTINCT:
        setDistinct((String)newValue);
        return;
      case CQLPackage.SELECT__ARGUMENTS:
        getArguments().clear();
        getArguments().addAll((Collection<? extends SelectArgument>)newValue);
        return;
      case CQLPackage.SELECT__SOURCES:
        getSources().clear();
        getSources().addAll((Collection<? extends Source>)newValue);
        return;
      case CQLPackage.SELECT__PREDICATES:
        setPredicates((ExpressionsModel)newValue);
        return;
      case CQLPackage.SELECT__ORDER:
        getOrder().clear();
        getOrder().addAll((Collection<? extends Attribute>)newValue);
        return;
      case CQLPackage.SELECT__HAVING:
        setHaving((ExpressionsModel)newValue);
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
      case CQLPackage.SELECT__DISTINCT:
        setDistinct(DISTINCT_EDEFAULT);
        return;
      case CQLPackage.SELECT__ARGUMENTS:
        getArguments().clear();
        return;
      case CQLPackage.SELECT__SOURCES:
        getSources().clear();
        return;
      case CQLPackage.SELECT__PREDICATES:
        setPredicates((ExpressionsModel)null);
        return;
      case CQLPackage.SELECT__ORDER:
        getOrder().clear();
        return;
      case CQLPackage.SELECT__HAVING:
        setHaving((ExpressionsModel)null);
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
      case CQLPackage.SELECT__DISTINCT:
        return DISTINCT_EDEFAULT == null ? distinct != null : !DISTINCT_EDEFAULT.equals(distinct);
      case CQLPackage.SELECT__ARGUMENTS:
        return arguments != null && !arguments.isEmpty();
      case CQLPackage.SELECT__SOURCES:
        return sources != null && !sources.isEmpty();
      case CQLPackage.SELECT__PREDICATES:
        return predicates != null;
      case CQLPackage.SELECT__ORDER:
        return order != null && !order.isEmpty();
      case CQLPackage.SELECT__HAVING:
        return having != null;
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
    result.append(" (distinct: ");
    result.append(distinct);
    result.append(')');
    return result.toString();
  }

} //SelectImpl
