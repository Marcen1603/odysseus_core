/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL.impl;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inner Select</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.InnerSelectImpl#getSelect <em>Select</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InnerSelectImpl extends MinimalEObjectImpl.Container implements InnerSelect
{
  /**
   * The cached value of the '{@link #getSelect() <em>Select</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSelect()
   * @generated
   * @ordered
   */
  protected SimpleSelect select;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InnerSelectImpl()
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
    return CQLPackage.Literals.INNER_SELECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SimpleSelect getSelect()
  {
    return select;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSelect(SimpleSelect newSelect, NotificationChain msgs)
  {
    SimpleSelect oldSelect = select;
    select = newSelect;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.INNER_SELECT__SELECT, oldSelect, newSelect);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSelect(SimpleSelect newSelect)
  {
    if (newSelect != select)
    {
      NotificationChain msgs = null;
      if (select != null)
        msgs = ((InternalEObject)select).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.INNER_SELECT__SELECT, null, msgs);
      if (newSelect != null)
        msgs = ((InternalEObject)newSelect).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.INNER_SELECT__SELECT, null, msgs);
      msgs = basicSetSelect(newSelect, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.INNER_SELECT__SELECT, newSelect, newSelect));
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
      case CQLPackage.INNER_SELECT__SELECT:
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
      case CQLPackage.INNER_SELECT__SELECT:
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
      case CQLPackage.INNER_SELECT__SELECT:
        setSelect((SimpleSelect)newValue);
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
      case CQLPackage.INNER_SELECT__SELECT:
        setSelect((SimpleSelect)null);
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
      case CQLPackage.INNER_SELECT__SELECT:
        return select != null;
    }
    return super.eIsSet(featureID);
  }

} //InnerSelectImpl
