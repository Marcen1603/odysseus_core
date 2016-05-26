/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Triples Same Subject</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getSubject <em>Subject</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getPropertyList <em>Property List</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getTriplesSameSubject()
 * @model
 * @generated
 */
public interface TriplesSameSubject extends EObject
{
  /**
   * Returns the value of the '<em><b>Subject</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subject</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subject</em>' containment reference.
   * @see #setSubject(GraphNode)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getTriplesSameSubject_Subject()
   * @model containment="true"
   * @generated
   */
  GraphNode getSubject();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getSubject <em>Subject</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subject</em>' containment reference.
   * @see #getSubject()
   * @generated
   */
  void setSubject(GraphNode value);

  /**
   * Returns the value of the '<em><b>Property List</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Property List</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Property List</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getTriplesSameSubject_PropertyList()
   * @model containment="true"
   * @generated
   */
  EList<PropertyList> getPropertyList();

} // TriplesSameSubject
