/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;

import org.eclipse.xtext.common.types.JvmGenericType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator#getMetadataList <em>Metadata List</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLOperator()
 * @model
 * @generated
 */
public interface ODLOperator extends JvmGenericType
{
  /**
   * Returns the value of the '<em><b>Metadata List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Metadata List</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Metadata List</em>' containment reference.
   * @see #setMetadataList(IQLMetadataList)
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLOperator_MetadataList()
   * @model containment="true"
   * @generated
   */
  IQLMetadataList getMetadataList();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator#getMetadataList <em>Metadata List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Metadata List</em>' containment reference.
   * @see #getMetadataList()
   * @generated
   */
  void setMetadataList(IQLMetadataList value);

} // ODLOperator