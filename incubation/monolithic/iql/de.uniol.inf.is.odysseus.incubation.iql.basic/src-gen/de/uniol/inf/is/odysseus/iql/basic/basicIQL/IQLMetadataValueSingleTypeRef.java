/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Metadata Value Single Type Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLMetadataValueSingleTypeRef()
 * @model
 * @generated
 */
public interface IQLMetadataValueSingleTypeRef extends IQLMetadataValue
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(JvmTypeReference)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLMetadataValueSingleTypeRef_Value()
   * @model containment="true"
   * @generated
   */
  JvmTypeReference getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(JvmTypeReference value);

} // IQLMetadataValueSingleTypeRef
