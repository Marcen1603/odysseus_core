/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Metadata Value Single Null</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleNull#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleNull()
 * @model
 * @generated
 */
public interface IQLMetadataValueSingleNull extends IQLMetadataValue
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
   * @see #setValue(String)
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleNull_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleNull#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // IQLMetadataValueSingleNull
