/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Drop Context Store</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore#getExists <em>Exists</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getDropContextStore()
 * @model
 * @generated
 */
public interface DropContextStore extends Command
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getDropContextStore_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Exists</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exists</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exists</em>' attribute.
   * @see #setExists(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getDropContextStore_Exists()
   * @model
   * @generated
   */
  String getExists();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore#getExists <em>Exists</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exists</em>' attribute.
   * @see #getExists()
   * @generated
   */
  void setExists(String value);

} // DropContextStore
