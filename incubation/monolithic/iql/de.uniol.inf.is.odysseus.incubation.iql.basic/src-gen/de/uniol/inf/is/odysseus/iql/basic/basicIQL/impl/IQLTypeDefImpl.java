/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.xtext.common.types.JvmTypeReference;

import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Type Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeDefImpl#getJavametadata <em>Javametadata</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeDefImpl#getExtendedInterfaces <em>Extended Interfaces</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQLTypeDefImpl extends JvmGenericTypeImpl implements IQLTypeDef
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
  protected IQLTypeDefImpl()
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
    return BasicIQLPackage.Literals.IQL_TYPE_DEF;
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
      javametadata = new EObjectContainmentEList<IQLJavaMetadata>(IQLJavaMetadata.class, this, BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA);
    }
    return javametadata;
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
      extendedInterfaces = new EObjectContainmentEList<JvmTypeReference>(JvmTypeReference.class, this, BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES);
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
      case BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA:
        return ((InternalEList<?>)getJavametadata()).basicRemove(otherEnd, msgs);
      case BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES:
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
      case BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA:
        return getJavametadata();
      case BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES:
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
      case BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA:
        getJavametadata().clear();
        getJavametadata().addAll((Collection<? extends IQLJavaMetadata>)newValue);
        return;
      case BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES:
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
      case BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA:
        getJavametadata().clear();
        return;
      case BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES:
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
      case BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA:
        return javametadata != null && !javametadata.isEmpty();
      case BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES:
        return extendedInterfaces != null && !extendedInterfaces.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //IQLTypeDefImpl
