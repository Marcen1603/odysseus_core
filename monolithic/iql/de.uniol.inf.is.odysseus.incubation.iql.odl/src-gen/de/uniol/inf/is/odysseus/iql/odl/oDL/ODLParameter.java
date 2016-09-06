/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isOptional <em>Optional</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isParameter <em>Parameter</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#getMetadataList <em>Metadata List</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLParameter()
 * @model
 * @generated
 */
public interface ODLParameter extends IQLAttribute
{
  /**
   * Returns the value of the '<em><b>Optional</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Optional</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Optional</em>' attribute.
   * @see #setOptional(boolean)
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLParameter_Optional()
   * @model
   * @generated
   */
  boolean isOptional();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isOptional <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Optional</em>' attribute.
   * @see #isOptional()
   * @generated
   */
  void setOptional(boolean value);

  /**
   * Returns the value of the '<em><b>Parameter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameter</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameter</em>' attribute.
   * @see #setParameter(boolean)
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLParameter_Parameter()
   * @model
   * @generated
   */
  boolean isParameter();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isParameter <em>Parameter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameter</em>' attribute.
   * @see #isParameter()
   * @generated
   */
  void setParameter(boolean value);

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
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLParameter_MetadataList()
   * @model containment="true"
   * @generated
   */
  IQLMetadataList getMetadataList();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#getMetadataList <em>Metadata List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Metadata List</em>' containment reference.
   * @see #getMetadataList()
   * @generated
   */
  void setMetadataList(IQLMetadataList value);

} // ODLParameter
