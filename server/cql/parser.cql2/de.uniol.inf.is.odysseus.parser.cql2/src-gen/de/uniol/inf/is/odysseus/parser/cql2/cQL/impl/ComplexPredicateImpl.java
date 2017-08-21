/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL.impl;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExistPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.QuantificationPredicate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.ComplexPredicateImpl#getQuantification <em>Quantification</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.ComplexPredicateImpl#getExists <em>Exists</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.ComplexPredicateImpl#getIn <em>In</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.ComplexPredicateImpl#getSelect <em>Select</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComplexPredicateImpl extends MinimalEObjectImpl.Container implements ComplexPredicate
{
  /**
   * The cached value of the '{@link #getQuantification() <em>Quantification</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQuantification()
   * @generated
   * @ordered
   */
  protected QuantificationPredicate quantification;

  /**
   * The cached value of the '{@link #getExists() <em>Exists</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExists()
   * @generated
   * @ordered
   */
  protected ExistPredicate exists;

  /**
   * The cached value of the '{@link #getIn() <em>In</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIn()
   * @generated
   * @ordered
   */
  protected InPredicate in;

  /**
   * The cached value of the '{@link #getSelect() <em>Select</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSelect()
   * @generated
   * @ordered
   */
  protected InnerSelect select;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComplexPredicateImpl()
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
    return CQLPackage.Literals.COMPLEX_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QuantificationPredicate getQuantification()
  {
    return quantification;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetQuantification(QuantificationPredicate newQuantification, NotificationChain msgs)
  {
    QuantificationPredicate oldQuantification = quantification;
    quantification = newQuantification;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION, oldQuantification, newQuantification);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQuantification(QuantificationPredicate newQuantification)
  {
    if (newQuantification != quantification)
    {
      NotificationChain msgs = null;
      if (quantification != null)
        msgs = ((InternalEObject)quantification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION, null, msgs);
      if (newQuantification != null)
        msgs = ((InternalEObject)newQuantification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION, null, msgs);
      msgs = basicSetQuantification(newQuantification, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION, newQuantification, newQuantification));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExistPredicate getExists()
  {
    return exists;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExists(ExistPredicate newExists, NotificationChain msgs)
  {
    ExistPredicate oldExists = exists;
    exists = newExists;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__EXISTS, oldExists, newExists);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExists(ExistPredicate newExists)
  {
    if (newExists != exists)
    {
      NotificationChain msgs = null;
      if (exists != null)
        msgs = ((InternalEObject)exists).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__EXISTS, null, msgs);
      if (newExists != null)
        msgs = ((InternalEObject)newExists).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__EXISTS, null, msgs);
      msgs = basicSetExists(newExists, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__EXISTS, newExists, newExists));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InPredicate getIn()
  {
    return in;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIn(InPredicate newIn, NotificationChain msgs)
  {
    InPredicate oldIn = in;
    in = newIn;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__IN, oldIn, newIn);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIn(InPredicate newIn)
  {
    if (newIn != in)
    {
      NotificationChain msgs = null;
      if (in != null)
        msgs = ((InternalEObject)in).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__IN, null, msgs);
      if (newIn != null)
        msgs = ((InternalEObject)newIn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__IN, null, msgs);
      msgs = basicSetIn(newIn, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__IN, newIn, newIn));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InnerSelect getSelect()
  {
    return select;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSelect(InnerSelect newSelect, NotificationChain msgs)
  {
    InnerSelect oldSelect = select;
    select = newSelect;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__SELECT, oldSelect, newSelect);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSelect(InnerSelect newSelect)
  {
    if (newSelect != select)
    {
      NotificationChain msgs = null;
      if (select != null)
        msgs = ((InternalEObject)select).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__SELECT, null, msgs);
      if (newSelect != null)
        msgs = ((InternalEObject)newSelect).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.COMPLEX_PREDICATE__SELECT, null, msgs);
      msgs = basicSetSelect(newSelect, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.COMPLEX_PREDICATE__SELECT, newSelect, newSelect));
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
      case CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION:
        return basicSetQuantification(null, msgs);
      case CQLPackage.COMPLEX_PREDICATE__EXISTS:
        return basicSetExists(null, msgs);
      case CQLPackage.COMPLEX_PREDICATE__IN:
        return basicSetIn(null, msgs);
      case CQLPackage.COMPLEX_PREDICATE__SELECT:
        return basicSetSelect(null, msgs);
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
      case CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION:
        return getQuantification();
      case CQLPackage.COMPLEX_PREDICATE__EXISTS:
        return getExists();
      case CQLPackage.COMPLEX_PREDICATE__IN:
        return getIn();
      case CQLPackage.COMPLEX_PREDICATE__SELECT:
        return getSelect();
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
      case CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION:
        setQuantification((QuantificationPredicate)newValue);
        return;
      case CQLPackage.COMPLEX_PREDICATE__EXISTS:
        setExists((ExistPredicate)newValue);
        return;
      case CQLPackage.COMPLEX_PREDICATE__IN:
        setIn((InPredicate)newValue);
        return;
      case CQLPackage.COMPLEX_PREDICATE__SELECT:
        setSelect((InnerSelect)newValue);
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
      case CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION:
        setQuantification((QuantificationPredicate)null);
        return;
      case CQLPackage.COMPLEX_PREDICATE__EXISTS:
        setExists((ExistPredicate)null);
        return;
      case CQLPackage.COMPLEX_PREDICATE__IN:
        setIn((InPredicate)null);
        return;
      case CQLPackage.COMPLEX_PREDICATE__SELECT:
        setSelect((InnerSelect)null);
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
      case CQLPackage.COMPLEX_PREDICATE__QUANTIFICATION:
        return quantification != null;
      case CQLPackage.COMPLEX_PREDICATE__EXISTS:
        return exists != null;
      case CQLPackage.COMPLEX_PREDICATE__IN:
        return in != null;
      case CQLPackage.COMPLEX_PREDICATE__SELECT:
        return select != null;
    }
    return super.eIsSet(featureID);
  }

} //ComplexPredicateImpl
