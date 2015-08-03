/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;

import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLOperatorImpl#getMetadataList <em>Metadata List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ODLOperatorImpl extends JvmGenericTypeImpl implements ODLOperator
{
  /**
   * The cached value of the '{@link #getMetadataList() <em>Metadata List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMetadataList()
   * @generated
   * @ordered
   */
  protected IQLMetadataList metadataList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ODLOperatorImpl()
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
    return ODLPackage.Literals.ODL_OPERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataList getMetadataList()
  {
    return metadataList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMetadataList(IQLMetadataList newMetadataList, NotificationChain msgs)
  {
    IQLMetadataList oldMetadataList = metadataList;
    metadataList = newMetadataList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_OPERATOR__METADATA_LIST, oldMetadataList, newMetadataList);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMetadataList(IQLMetadataList newMetadataList)
  {
    if (newMetadataList != metadataList)
    {
      NotificationChain msgs = null;
      if (metadataList != null)
        msgs = ((InternalEObject)metadataList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ODLPackage.ODL_OPERATOR__METADATA_LIST, null, msgs);
      if (newMetadataList != null)
        msgs = ((InternalEObject)newMetadataList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ODLPackage.ODL_OPERATOR__METADATA_LIST, null, msgs);
      msgs = basicSetMetadataList(newMetadataList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_OPERATOR__METADATA_LIST, newMetadataList, newMetadataList));
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
      case ODLPackage.ODL_OPERATOR__METADATA_LIST:
        return basicSetMetadataList(null, msgs);
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
      case ODLPackage.ODL_OPERATOR__METADATA_LIST:
        return getMetadataList();
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
      case ODLPackage.ODL_OPERATOR__METADATA_LIST:
        setMetadataList((IQLMetadataList)newValue);
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
      case ODLPackage.ODL_OPERATOR__METADATA_LIST:
        setMetadataList((IQLMetadataList)null);
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
      case ODLPackage.ODL_OPERATOR__METADATA_LIST:
        return metadataList != null;
    }
    return super.eIsSet(featureID);
  }

} //ODLOperatorImpl
