/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getMetadataList <em>Metadata List</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getStatements <em>Statements</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getQDLQuery()
 * @model
 * @generated
 */
public interface QDLQuery extends IQLTypeDef
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
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getQDLQuery_MetadataList()
   * @model containment="true"
   * @generated
   */
  IQLMetadataList getMetadataList();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getMetadataList <em>Metadata List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Metadata List</em>' containment reference.
   * @see #getMetadataList()
   * @generated
   */
  void setMetadataList(IQLMetadataList value);

  /**
   * Returns the value of the '<em><b>Statements</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Statements</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Statements</em>' containment reference.
   * @see #setStatements(IQLStatement)
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#getQDLQuery_Statements()
   * @model containment="true"
   * @generated
   */
  IQLStatement getStatements();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getStatements <em>Statements</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Statements</em>' containment reference.
   * @see #getStatements()
   * @generated
   */
  void setStatements(IQLStatement value);

} // QDLQuery
