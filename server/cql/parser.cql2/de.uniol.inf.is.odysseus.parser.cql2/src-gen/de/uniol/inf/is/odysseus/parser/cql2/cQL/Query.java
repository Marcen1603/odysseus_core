/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.Query#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getQuery()
 * @model
 * @generated
 */
public interface Query extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(EObject)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getQuery_Type()
   * @model containment="true"
   * @generated
   */
  EObject getType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.Query#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(EObject value);

} // Query
