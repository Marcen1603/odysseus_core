/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Metadata Value Single Boolean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleBoolean#isValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleBoolean()
 * @model
 * @generated
 */
public interface IQLMetadataValueSingleBoolean extends IQLMetadataValue
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(boolean)
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleBoolean_Value()
   * @model
   * @generated
   */
  boolean isValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleBoolean#isValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #isValue()
   * @generated
   */
  void setValue(boolean value);

} // IQLMetadataValueSingleBoolean
