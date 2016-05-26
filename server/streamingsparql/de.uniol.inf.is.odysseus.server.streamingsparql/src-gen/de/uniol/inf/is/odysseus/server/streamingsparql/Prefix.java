/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Prefix</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getIref <em>Iref</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPrefix()
 * @model
 * @generated
 */
public interface Prefix extends EObject
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
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPrefix_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Iref</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Iref</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Iref</em>' attribute.
   * @see #setIref(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getPrefix_Iref()
   * @model
   * @generated
   */
  String getIref();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getIref <em>Iref</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Iref</em>' attribute.
   * @see #getIref()
   * @generated
   */
  void setIref(String value);

} // Prefix
