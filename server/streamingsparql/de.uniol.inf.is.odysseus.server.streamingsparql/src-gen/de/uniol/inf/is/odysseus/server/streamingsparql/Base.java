/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Base#getIref <em>Iref</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getBase()
 * @model
 * @generated
 */
public interface Base extends EObject
{
  /**
   * Returns the value of the '<em><b>Iref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Iref</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Iref</em>' containment reference.
   * @see #setIref(IRI)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getBase_Iref()
   * @model containment="true"
   * @generated
   */
  IRI getIref();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Base#getIref <em>Iref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Iref</em>' containment reference.
   * @see #getIref()
   * @generated
   */
  void setIref(IRI value);

} // Base
