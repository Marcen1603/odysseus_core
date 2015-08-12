/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Metadata Value Single Double</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleDouble#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleDouble()
 * @model
 * @generated
 */
public interface IQLMetadataValueSingleDouble extends IQLMetadataValue
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
   * @see #setValue(double)
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getIQLMetadataValueSingleDouble_Value()
   * @model
   * @generated
   */
  double getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleDouble#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(double value);

} // IQLMetadataValueSingleDouble