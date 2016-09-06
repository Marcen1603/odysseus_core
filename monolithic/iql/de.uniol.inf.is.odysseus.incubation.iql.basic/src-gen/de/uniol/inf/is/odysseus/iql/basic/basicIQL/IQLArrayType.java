/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.xtext.common.types.JvmType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Array Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getDimensions <em>Dimensions</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLArrayType()
 * @model
 * @generated
 */
public interface IQLArrayType extends JvmType
{
  /**
   * Returns the value of the '<em><b>Component Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Component Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Component Type</em>' reference.
   * @see #setComponentType(JvmType)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLArrayType_ComponentType()
   * @model
   * @generated
   */
  JvmType getComponentType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getComponentType <em>Component Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Component Type</em>' reference.
   * @see #getComponentType()
   * @generated
   */
  void setComponentType(JvmType value);

  /**
   * Returns the value of the '<em><b>Dimensions</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Dimensions</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dimensions</em>' attribute list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLArrayType_Dimensions()
   * @model unique="false"
   * @generated
   */
  EList<String> getDimensions();

} // IQLArrayType
