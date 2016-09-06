/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>QUERYCONDITION</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl#getQueryNot <em>Query Not</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl#getQueryFunct <em>Query Funct</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QUERYCONDITIONImpl extends MinimalEObjectImpl.Container implements QUERYCONDITION
{
  /**
   * The default value of the '{@link #getQueryNot() <em>Query Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQueryNot()
   * @generated
   * @ordered
   */
  protected static final String QUERY_NOT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getQueryNot() <em>Query Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQueryNot()
   * @generated
   * @ordered
   */
  protected String queryNot = QUERY_NOT_EDEFAULT;

  /**
   * The cached value of the '{@link #getQueryFunct() <em>Query Funct</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQueryFunct()
   * @generated
   * @ordered
   */
  protected RNDQUERY queryFunct;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected QUERYCONDITIONImpl()
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
    return ECAPackage.Literals.QUERYCONDITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getQueryNot()
  {
    return queryNot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQueryNot(String newQueryNot)
  {
    String oldQueryNot = queryNot;
    queryNot = newQueryNot;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.QUERYCONDITION__QUERY_NOT, oldQueryNot, queryNot));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RNDQUERY getQueryFunct()
  {
    return queryFunct;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetQueryFunct(RNDQUERY newQueryFunct, NotificationChain msgs)
  {
    RNDQUERY oldQueryFunct = queryFunct;
    queryFunct = newQueryFunct;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.QUERYCONDITION__QUERY_FUNCT, oldQueryFunct, newQueryFunct);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQueryFunct(RNDQUERY newQueryFunct)
  {
    if (newQueryFunct != queryFunct)
    {
      NotificationChain msgs = null;
      if (queryFunct != null)
        msgs = ((InternalEObject)queryFunct).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.QUERYCONDITION__QUERY_FUNCT, null, msgs);
      if (newQueryFunct != null)
        msgs = ((InternalEObject)newQueryFunct).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.QUERYCONDITION__QUERY_FUNCT, null, msgs);
      msgs = basicSetQueryFunct(newQueryFunct, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.QUERYCONDITION__QUERY_FUNCT, newQueryFunct, newQueryFunct));
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
      case ECAPackage.QUERYCONDITION__QUERY_FUNCT:
        return basicSetQueryFunct(null, msgs);
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
      case ECAPackage.QUERYCONDITION__QUERY_NOT:
        return getQueryNot();
      case ECAPackage.QUERYCONDITION__QUERY_FUNCT:
        return getQueryFunct();
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
      case ECAPackage.QUERYCONDITION__QUERY_NOT:
        setQueryNot((String)newValue);
        return;
      case ECAPackage.QUERYCONDITION__QUERY_FUNCT:
        setQueryFunct((RNDQUERY)newValue);
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
      case ECAPackage.QUERYCONDITION__QUERY_NOT:
        setQueryNot(QUERY_NOT_EDEFAULT);
        return;
      case ECAPackage.QUERYCONDITION__QUERY_FUNCT:
        setQueryFunct((RNDQUERY)null);
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
      case ECAPackage.QUERYCONDITION__QUERY_NOT:
        return QUERY_NOT_EDEFAULT == null ? queryNot != null : !QUERY_NOT_EDEFAULT.equals(queryNot);
      case ECAPackage.QUERYCONDITION__QUERY_FUNCT:
        return queryFunct != null;
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
    result.append(" (queryNot: ");
    result.append(queryNot);
    result.append(')');
    return result.toString();
  }

} //QUERYCONDITIONImpl
