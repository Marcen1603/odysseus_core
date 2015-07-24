/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl#getExtendedClass <em>Extended Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLClassImpl extends IQLTypeDefImpl implements IQLClass
{
  /**
   * The cached value of the '{@link #getExtendedClass() <em>Extended Class</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExtendedClass()
   * @generated
   * @ordered
   */
  protected JvmTypeReference extendedClass;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLClassImpl()
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
    return BasicIQLPackage.Literals.IQL_CLASS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmTypeReference getExtendedClass()
  {
    return extendedClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExtendedClass(JvmTypeReference newExtendedClass, NotificationChain msgs)
  {
    JvmTypeReference oldExtendedClass = extendedClass;
    extendedClass = newExtendedClass;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS, oldExtendedClass, newExtendedClass);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExtendedClass(JvmTypeReference newExtendedClass)
  {
    if (newExtendedClass != extendedClass)
    {
      NotificationChain msgs = null;
      if (extendedClass != null)
        msgs = ((InternalEObject)extendedClass).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS, null, msgs);
      if (newExtendedClass != null)
        msgs = ((InternalEObject)newExtendedClass).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS, null, msgs);
      msgs = basicSetExtendedClass(newExtendedClass, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS, newExtendedClass, newExtendedClass));
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        return basicSetExtendedClass(null, msgs);
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        return getExtendedClass();
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        setExtendedClass((JvmTypeReference)newValue);
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        setExtendedClass((JvmTypeReference)null);
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        return extendedClass != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLClassImpl
