/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getUnnamed <em>Unnamed</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getProperty <em>Property</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getVariable()
 * @model
 * @generated
 */
public interface Variable extends EObject
{
  /**
   * Returns the value of the '<em><b>Unnamed</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Unnamed</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Unnamed</em>' containment reference.
   * @see #setUnnamed(UnNamedVariable)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getVariable_Unnamed()
   * @model containment="true"
   * @generated
   */
  UnNamedVariable getUnnamed();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getUnnamed <em>Unnamed</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Unnamed</em>' containment reference.
   * @see #getUnnamed()
   * @generated
   */
  void setUnnamed(UnNamedVariable value);

  /**
   * Returns the value of the '<em><b>Property</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Property</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Property</em>' containment reference.
   * @see #setProperty(NamedVariable)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getVariable_Property()
   * @model containment="true"
   * @generated
   */
  NamedVariable getProperty();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getProperty <em>Property</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Property</em>' containment reference.
   * @see #getProperty()
   * @generated
   */
  void setProperty(NamedVariable value);

} // Variable
