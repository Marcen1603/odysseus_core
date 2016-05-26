/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getProperty <em>Property</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getObject <em>Object</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPropertyList()
 * @model
 * @generated
 */
public interface PropertyList extends EObject
{
  /**
   * Returns the value of the '<em><b>Property</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Property</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Property</em>' containment reference.
   * @see #setProperty(GraphNode)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPropertyList_Property()
   * @model containment="true"
   * @generated
   */
  GraphNode getProperty();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getProperty <em>Property</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Property</em>' containment reference.
   * @see #getProperty()
   * @generated
   */
  void setProperty(GraphNode value);

  /**
   * Returns the value of the '<em><b>Object</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Object</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Object</em>' containment reference.
   * @see #setObject(GraphNode)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPropertyList_Object()
   * @model containment="true"
   * @generated
   */
  GraphNode getObject();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getObject <em>Object</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Object</em>' containment reference.
   * @see #getObject()
   * @generated
   */
  void setObject(GraphNode value);

} // PropertyList
