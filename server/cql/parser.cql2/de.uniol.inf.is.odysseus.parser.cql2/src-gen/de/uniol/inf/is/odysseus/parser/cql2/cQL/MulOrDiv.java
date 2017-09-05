/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mul Or Div</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getLeft <em>Left</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getRight <em>Right</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getMulOrDiv()
 * @model
 * @generated
 */
public interface MulOrDiv extends Expression
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
   * @see #setLeft(Expression)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getMulOrDiv_Left()
   * @model containment="true"
   * @generated
   */
  Expression getLeft();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getLeft <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left</em>' containment reference.
   * @see #getLeft()
   * @generated
   */
  void setLeft(Expression value);

  /**
   * Returns the value of the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op</em>' attribute.
   * @see #setOp(String)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getMulOrDiv_Op()
   * @model
   * @generated
   */
  String getOp();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getOp <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op</em>' attribute.
   * @see #getOp()
   * @generated
   */
  void setOp(String value);

  /**
   * Returns the value of the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right</em>' containment reference.
   * @see #setRight(Expression)
   * @see de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage#getMulOrDiv_Right()
   * @model containment="true"
   * @generated
   */
  Expression getRight();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv#getRight <em>Right</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right</em>' containment reference.
   * @see #getRight()
   * @generated
   */
  void setRight(Expression value);

} // MulOrDiv
