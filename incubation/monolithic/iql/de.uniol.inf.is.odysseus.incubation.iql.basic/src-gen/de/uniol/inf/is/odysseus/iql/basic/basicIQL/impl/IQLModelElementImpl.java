/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;

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

import org.eclipse.xtext.common.types.JvmGenericType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Model Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl#getJavametadata <em>Javametadata</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl#getInner <em>Inner</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLModelElementImpl extends MinimalEObjectImpl.Container implements IQLModelElement
{
  /**
   * The cached value of the '{@link #getJavametadata() <em>Javametadata</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getJavametadata()
   * @generated
   * @ordered
   */
  protected EList<IQLJavaMetadata> javametadata;

  /**
   * The cached value of the '{@link #getInner() <em>Inner</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInner()
   * @generated
   * @ordered
   */
  protected JvmGenericType inner;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLModelElementImpl()
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
    return BasicIQLPackage.Literals.IQL_MODEL_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<IQLJavaMetadata> getJavametadata()
  {
    if (javametadata == null)
    {
      javametadata = new EObjectContainmentEList<IQLJavaMetadata>(IQLJavaMetadata.class, this, BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA);
    }
    return javametadata;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmGenericType getInner()
  {
    return inner;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInner(JvmGenericType newInner, NotificationChain msgs)
  {
    JvmGenericType oldInner = inner;
    inner = newInner;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MODEL_ELEMENT__INNER, oldInner, newInner);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setInner(JvmGenericType newInner)
  {
    if (newInner != inner)
    {
      NotificationChain msgs = null;
      if (inner != null)
        msgs = ((InternalEObject)inner).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MODEL_ELEMENT__INNER, null, msgs);
      if (newInner != null)
        msgs = ((InternalEObject)newInner).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_MODEL_ELEMENT__INNER, null, msgs);
      msgs = basicSetInner(newInner, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_MODEL_ELEMENT__INNER, newInner, newInner));
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
      case BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA:
        return ((InternalEList<?>)getJavametadata()).basicRemove(otherEnd, msgs);
      case BasicIQLPackage.IQL_MODEL_ELEMENT__INNER:
        return basicSetInner(null, msgs);
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
      case BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA:
        return getJavametadata();
      case BasicIQLPackage.IQL_MODEL_ELEMENT__INNER:
        return getInner();
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
      case BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA:
        getJavametadata().clear();
        getJavametadata().addAll((Collection<? extends IQLJavaMetadata>)newValue);
        return;
      case BasicIQLPackage.IQL_MODEL_ELEMENT__INNER:
        setInner((JvmGenericType)newValue);
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
      case BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA:
        getJavametadata().clear();
        return;
      case BasicIQLPackage.IQL_MODEL_ELEMENT__INNER:
        setInner((JvmGenericType)null);
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
      case BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA:
        return javametadata != null && !javametadata.isEmpty();
      case BasicIQLPackage.IQL_MODEL_ELEMENT__INNER:
        return inner != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLModelElementImpl