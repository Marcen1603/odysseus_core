/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operator Or Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOutputPort <em>Output Port</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getQuery <em>Query</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperatorOrQuery()
 * @model
 * @generated
 */
public interface OperatorOrQuery extends EObject
{
  /**
   * Returns the value of the '<em><b>Output Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Output Port</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output Port</em>' attribute.
   * @see #setOutputPort(int)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperatorOrQuery_OutputPort()
   * @model
   * @generated
   */
  int getOutputPort();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOutputPort <em>Output Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Output Port</em>' attribute.
   * @see #getOutputPort()
   * @generated
   */
  void setOutputPort(int value);

  /**
   * Returns the value of the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op</em>' containment reference.
   * @see #setOp(Operator)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperatorOrQuery_Op()
   * @model containment="true"
   * @generated
   */
  Operator getOp();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOp <em>Op</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op</em>' containment reference.
   * @see #getOp()
   * @generated
   */
  void setOp(Operator value);

  /**
   * Returns the value of the '<em><b>Query</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Query</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Query</em>' reference.
   * @see #setQuery(Query)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperatorOrQuery_Query()
   * @model
   * @generated
   */
  Query getQuery();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getQuery <em>Query</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Query</em>' reference.
   * @see #getQuery()
   * @generated
   */
  void setQuery(Query value);

} // OperatorOrQuery
