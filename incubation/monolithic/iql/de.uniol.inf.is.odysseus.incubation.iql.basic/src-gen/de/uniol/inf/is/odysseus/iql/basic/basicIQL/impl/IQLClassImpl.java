/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.xtext.common.types.JvmTypeReference;

import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl#getExtendedClass <em>Extended Class</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl#getExtendedInterfaces <em>Extended Interfaces</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IQLClassImpl extends JvmGenericTypeImpl implements IQLClass
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
   * The cached value of the '{@link #getExtendedInterfaces() <em>Extended Interfaces</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExtendedInterfaces()
   * @generated
   * @ordered
   */
  protected EList<JvmTypeReference> extendedInterfaces;

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
  public EList<JvmTypeReference> getExtendedInterfaces()
  {
    if (extendedInterfaces == null)
    {
      extendedInterfaces = new EObjectContainmentEList<JvmTypeReference>(JvmTypeReference.class, this, BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES);
    }
    return extendedInterfaces;
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES:
        return ((InternalEList<?>)getExtendedInterfaces()).basicRemove(otherEnd, msgs);
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES:
        return getExtendedInterfaces();
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_CLASS:
        setExtendedClass((JvmTypeReference)newValue);
        return;
      case BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES:
        getExtendedInterfaces().clear();
        getExtendedInterfaces().addAll((Collection<? extends JvmTypeReference>)newValue);
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES:
        getExtendedInterfaces().clear();
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
      case BasicIQLPackage.IQL_CLASS__EXTENDED_INTERFACES:
        return extendedInterfaces != null && !extendedInterfaces.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //IQLClassImpl
