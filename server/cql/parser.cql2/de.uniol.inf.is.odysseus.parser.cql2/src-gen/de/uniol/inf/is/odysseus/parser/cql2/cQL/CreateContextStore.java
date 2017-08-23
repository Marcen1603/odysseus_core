/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Create Context Store</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore#getContextType <em>Context Type</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateContextStore()
 * @model
 * @generated
 */
public interface CreateContextStore extends Command
{
  /**
   * Returns the value of the '<em><b>Attributes</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attributes</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attributes</em>' containment reference.
   * @see #setAttributes(SchemaDefinition)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateContextStore_Attributes()
   * @model containment="true"
   * @generated
   */
  SchemaDefinition getAttributes();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore#getAttributes <em>Attributes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Attributes</em>' containment reference.
   * @see #getAttributes()
   * @generated
   */
  void setAttributes(SchemaDefinition value);

  /**
   * Returns the value of the '<em><b>Context Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Context Type</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Context Type</em>' containment reference.
   * @see #setContextType(ContextStoreType)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getCreateContextStore_ContextType()
   * @model containment="true"
   * @generated
   */
  ContextStoreType getContextType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore#getContextType <em>Context Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Context Type</em>' containment reference.
   * @see #getContextType()
   * @generated
   */
  void setContextType(ContextStoreType value);

} // CreateContextStore
