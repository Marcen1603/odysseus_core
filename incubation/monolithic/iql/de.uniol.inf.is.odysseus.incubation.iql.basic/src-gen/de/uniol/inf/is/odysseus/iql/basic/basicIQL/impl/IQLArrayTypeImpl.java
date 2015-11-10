/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

import org.eclipse.xtext.common.types.JvmType;

import org.eclipse.xtext.common.types.impl.JvmTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Array Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl#getDimensions <em>Dimensions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IQLArrayTypeImpl extends JvmTypeImpl implements IQLArrayType
{
  /**
   * The cached value of the '{@link #getComponentType() <em>Component Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComponentType()
   * @generated
   * @ordered
   */
  protected JvmType componentType;

  /**
   * The cached value of the '{@link #getDimensions() <em>Dimensions</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDimensions()
   * @generated
   * @ordered
   */
  protected EList<String> dimensions;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLArrayTypeImpl()
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
    return BasicIQLPackage.Literals.IQL_ARRAY_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmType getComponentType()
  {
    if (componentType != null && componentType.eIsProxy())
    {
      InternalEObject oldComponentType = (InternalEObject)componentType;
      componentType = (JvmType)eResolveProxy(oldComponentType);
      if (componentType != oldComponentType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE, oldComponentType, componentType));
      }
    }
    return componentType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmType basicGetComponentType()
  {
    return componentType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComponentType(JvmType newComponentType)
  {
    JvmType oldComponentType = componentType;
    componentType = newComponentType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE, oldComponentType, componentType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getDimensions()
  {
    if (dimensions == null)
    {
      dimensions = new EDataTypeEList<String>(String.class, this, BasicIQLPackage.IQL_ARRAY_TYPE__DIMENSIONS);
    }
    return dimensions;
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
      case BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE:
        if (resolve) return getComponentType();
        return basicGetComponentType();
      case BasicIQLPackage.IQL_ARRAY_TYPE__DIMENSIONS:
        return getDimensions();
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
      case BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE:
        setComponentType((JvmType)newValue);
        return;
      case BasicIQLPackage.IQL_ARRAY_TYPE__DIMENSIONS:
        getDimensions().clear();
        getDimensions().addAll((Collection<? extends String>)newValue);
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
      case BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE:
        setComponentType((JvmType)null);
        return;
      case BasicIQLPackage.IQL_ARRAY_TYPE__DIMENSIONS:
        getDimensions().clear();
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
      case BasicIQLPackage.IQL_ARRAY_TYPE__COMPONENT_TYPE:
        return componentType != null;
      case BasicIQLPackage.IQL_ARRAY_TYPE__DIMENSIONS:
        return dimensions != null && !dimensions.isEmpty();
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
    result.append(" (dimensions: ");
    result.append(dimensions);
    result.append(')');
    return result.toString();
  }

} //IQLArrayTypeImpl
