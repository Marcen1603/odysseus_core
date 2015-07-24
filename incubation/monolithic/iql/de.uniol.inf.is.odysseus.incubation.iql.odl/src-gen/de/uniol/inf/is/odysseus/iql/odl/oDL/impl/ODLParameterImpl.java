/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAttributeImpl;

import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl#isParameter <em>Parameter</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl#getMetadataList <em>Metadata List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ODLParameterImpl extends IQLAttributeImpl implements ODLParameter
{
  /**
   * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected static final boolean OPTIONAL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected boolean optional = OPTIONAL_EDEFAULT;

  /**
   * The default value of the '{@link #isParameter() <em>Parameter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isParameter()
   * @generated
   * @ordered
   */
  protected static final boolean PARAMETER_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isParameter() <em>Parameter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isParameter()
   * @generated
   * @ordered
   */
  protected boolean parameter = PARAMETER_EDEFAULT;

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
  protected ODLParameterImpl()
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
    return ODLPackage.Literals.ODL_PARAMETER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOptional()
  {
    return optional;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOptional(boolean newOptional)
  {
    boolean oldOptional = optional;
    optional = newOptional;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_PARAMETER__OPTIONAL, oldOptional, optional));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isParameter()
  {
    return parameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParameter(boolean newParameter)
  {
    boolean oldParameter = parameter;
    parameter = newParameter;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_PARAMETER__PARAMETER, oldParameter, parameter));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_PARAMETER__METADATA_LIST, oldMetadataList, newMetadataList);
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
        msgs = ((InternalEObject)metadataList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ODLPackage.ODL_PARAMETER__METADATA_LIST, null, msgs);
      if (newMetadataList != null)
        msgs = ((InternalEObject)newMetadataList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ODLPackage.ODL_PARAMETER__METADATA_LIST, null, msgs);
      msgs = basicSetMetadataList(newMetadataList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ODLPackage.ODL_PARAMETER__METADATA_LIST, newMetadataList, newMetadataList));
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
      case ODLPackage.ODL_PARAMETER__METADATA_LIST:
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
      case ODLPackage.ODL_PARAMETER__OPTIONAL:
        return isOptional();
      case ODLPackage.ODL_PARAMETER__PARAMETER:
        return isParameter();
      case ODLPackage.ODL_PARAMETER__METADATA_LIST:
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
      case ODLPackage.ODL_PARAMETER__OPTIONAL:
        setOptional((Boolean)newValue);
        return;
      case ODLPackage.ODL_PARAMETER__PARAMETER:
        setParameter((Boolean)newValue);
        return;
      case ODLPackage.ODL_PARAMETER__METADATA_LIST:
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
      case ODLPackage.ODL_PARAMETER__OPTIONAL:
        setOptional(OPTIONAL_EDEFAULT);
        return;
      case ODLPackage.ODL_PARAMETER__PARAMETER:
        setParameter(PARAMETER_EDEFAULT);
        return;
      case ODLPackage.ODL_PARAMETER__METADATA_LIST:
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
      case ODLPackage.ODL_PARAMETER__OPTIONAL:
        return optional != OPTIONAL_EDEFAULT;
      case ODLPackage.ODL_PARAMETER__PARAMETER:
        return parameter != PARAMETER_EDEFAULT;
      case ODLPackage.ODL_PARAMETER__METADATA_LIST:
        return metadataList != null;
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
    result.append(" (optional: ");
    result.append(optional);
    result.append(", parameter: ");
    result.append(parameter);
    result.append(')');
    return result.toString();
  }

} //ODLParameterImpl
