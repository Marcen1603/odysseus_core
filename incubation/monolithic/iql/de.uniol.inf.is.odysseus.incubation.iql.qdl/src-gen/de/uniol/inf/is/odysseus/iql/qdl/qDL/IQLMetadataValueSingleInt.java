/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Metadata Value Single Int</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleInt#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleInt()
 * @model
 * @generated
 */
public interface IQLMetadataValueSingleInt extends IQLMetadataValue
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
   * @see #setValue(int)
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleInt_Value()
   * @model
   * @generated
   */
  int getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleInt#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(int value);

} // IQLMetadataValueSingleInt
