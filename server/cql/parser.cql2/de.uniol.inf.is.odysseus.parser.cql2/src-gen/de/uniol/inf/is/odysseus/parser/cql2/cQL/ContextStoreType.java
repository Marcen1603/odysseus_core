/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Context Store Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getType <em>Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getSize <em>Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getPartition <em>Partition</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getContextStoreType()
 * @model
 * @generated
 */
public interface ContextStoreType extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getContextStoreType_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

  /**
   * Returns the value of the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size</em>' attribute.
   * @see #setSize(int)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getContextStoreType_Size()
   * @model
   * @generated
   */
  int getSize();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getSize <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' attribute.
   * @see #getSize()
   * @generated
   */
  void setSize(int value);

  /**
   * Returns the value of the '<em><b>Partition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Partition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Partition</em>' attribute.
   * @see #setPartition(int)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getContextStoreType_Partition()
   * @model
   * @generated
   */
  int getPartition();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.ContextStoreType#getPartition <em>Partition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Partition</em>' attribute.
   * @see #getPartition()
   * @generated
   */
  void setPartition(int value);

} // ContextStoreType
