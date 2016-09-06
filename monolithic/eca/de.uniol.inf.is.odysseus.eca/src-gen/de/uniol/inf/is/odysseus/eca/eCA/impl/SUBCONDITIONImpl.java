/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SUBCONDITION</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl#getSubfree <em>Subfree</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl#getSubmap <em>Submap</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl#getQueryCond <em>Query Cond</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SUBCONDITIONImpl extends ExpressionImpl implements SUBCONDITION
{
  /**
   * The cached value of the '{@link #getSubfree() <em>Subfree</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubfree()
   * @generated
   * @ordered
   */
  protected FREECONDITION subfree;

  /**
   * The cached value of the '{@link #getSubmap() <em>Submap</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubmap()
   * @generated
   * @ordered
   */
  protected MAPCONDITION submap;

  /**
   * The cached value of the '{@link #getQueryCond() <em>Query Cond</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQueryCond()
   * @generated
   * @ordered
   */
  protected QUERYCONDITION queryCond;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SUBCONDITIONImpl()
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
    return ECAPackage.Literals.SUBCONDITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FREECONDITION getSubfree()
  {
    return subfree;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubfree(FREECONDITION newSubfree, NotificationChain msgs)
  {
    FREECONDITION oldSubfree = subfree;
    subfree = newSubfree;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__SUBFREE, oldSubfree, newSubfree);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubfree(FREECONDITION newSubfree)
  {
    if (newSubfree != subfree)
    {
      NotificationChain msgs = null;
      if (subfree != null)
        msgs = ((InternalEObject)subfree).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__SUBFREE, null, msgs);
      if (newSubfree != null)
        msgs = ((InternalEObject)newSubfree).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__SUBFREE, null, msgs);
      msgs = basicSetSubfree(newSubfree, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__SUBFREE, newSubfree, newSubfree));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MAPCONDITION getSubmap()
  {
    return submap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubmap(MAPCONDITION newSubmap, NotificationChain msgs)
  {
    MAPCONDITION oldSubmap = submap;
    submap = newSubmap;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__SUBMAP, oldSubmap, newSubmap);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubmap(MAPCONDITION newSubmap)
  {
    if (newSubmap != submap)
    {
      NotificationChain msgs = null;
      if (submap != null)
        msgs = ((InternalEObject)submap).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__SUBMAP, null, msgs);
      if (newSubmap != null)
        msgs = ((InternalEObject)newSubmap).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__SUBMAP, null, msgs);
      msgs = basicSetSubmap(newSubmap, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__SUBMAP, newSubmap, newSubmap));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QUERYCONDITION getQueryCond()
  {
    return queryCond;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetQueryCond(QUERYCONDITION newQueryCond, NotificationChain msgs)
  {
    QUERYCONDITION oldQueryCond = queryCond;
    queryCond = newQueryCond;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__QUERY_COND, oldQueryCond, newQueryCond);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQueryCond(QUERYCONDITION newQueryCond)
  {
    if (newQueryCond != queryCond)
    {
      NotificationChain msgs = null;
      if (queryCond != null)
        msgs = ((InternalEObject)queryCond).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__QUERY_COND, null, msgs);
      if (newQueryCond != null)
        msgs = ((InternalEObject)newQueryCond).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.SUBCONDITION__QUERY_COND, null, msgs);
      msgs = basicSetQueryCond(newQueryCond, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.SUBCONDITION__QUERY_COND, newQueryCond, newQueryCond));
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
      case ECAPackage.SUBCONDITION__SUBFREE:
        return basicSetSubfree(null, msgs);
      case ECAPackage.SUBCONDITION__SUBMAP:
        return basicSetSubmap(null, msgs);
      case ECAPackage.SUBCONDITION__QUERY_COND:
        return basicSetQueryCond(null, msgs);
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
      case ECAPackage.SUBCONDITION__SUBFREE:
        return getSubfree();
      case ECAPackage.SUBCONDITION__SUBMAP:
        return getSubmap();
      case ECAPackage.SUBCONDITION__QUERY_COND:
        return getQueryCond();
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
      case ECAPackage.SUBCONDITION__SUBFREE:
        setSubfree((FREECONDITION)newValue);
        return;
      case ECAPackage.SUBCONDITION__SUBMAP:
        setSubmap((MAPCONDITION)newValue);
        return;
      case ECAPackage.SUBCONDITION__QUERY_COND:
        setQueryCond((QUERYCONDITION)newValue);
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
      case ECAPackage.SUBCONDITION__SUBFREE:
        setSubfree((FREECONDITION)null);
        return;
      case ECAPackage.SUBCONDITION__SUBMAP:
        setSubmap((MAPCONDITION)null);
        return;
      case ECAPackage.SUBCONDITION__QUERY_COND:
        setQueryCond((QUERYCONDITION)null);
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
      case ECAPackage.SUBCONDITION__SUBFREE:
        return subfree != null;
      case ECAPackage.SUBCONDITION__SUBMAP:
        return submap != null;
      case ECAPackage.SUBCONDITION__QUERY_COND:
        return queryCond != null;
    }
    return super.eIsSet(featureID);
  }

} //SUBCONDITIONImpl
