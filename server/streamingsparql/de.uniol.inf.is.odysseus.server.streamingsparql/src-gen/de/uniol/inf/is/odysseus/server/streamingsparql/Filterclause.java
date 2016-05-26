/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Filterclause</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getLeft <em>Left</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getOperator <em>Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getRight <em>Right</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getFilterclause()
 * @model
 * @generated
 */
public interface Filterclause extends EObject
{
  /**
   * Returns the value of the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left</em>' containment reference.
   * @see #setLeft(Variable)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getFilterclause_Left()
   * @model containment="true"
   * @generated
   */
  Variable getLeft();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getLeft <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left</em>' containment reference.
   * @see #getLeft()
   * @generated
   */
  void setLeft(Variable value);

  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * The literals are from the enumeration {@link de.uniol.inf.is.odysseus.server.streamingsparql.Operator}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Operator
   * @see #setOperator(Operator)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getFilterclause_Operator()
   * @model
   * @generated
   */
  Operator getOperator();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Operator
   * @see #getOperator()
   * @generated
   */
  void setOperator(Operator value);

  /**
   * Returns the value of the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right</em>' containment reference.
   * @see #setRight(Variable)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getFilterclause_Right()
   * @model containment="true"
   * @generated
   */
  Variable getRight();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getRight <em>Right</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right</em>' containment reference.
   * @see #getRight()
   * @generated
   */
  void setRight(Variable value);

} // Filterclause
