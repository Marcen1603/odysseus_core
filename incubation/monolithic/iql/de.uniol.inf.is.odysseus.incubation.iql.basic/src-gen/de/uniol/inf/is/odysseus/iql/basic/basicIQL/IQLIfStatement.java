/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL If Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getThenBody <em>Then Body</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getElseBody <em>Else Body</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLIfStatement()
 * @model
 * @generated
 */
public interface IQLIfStatement extends IQLStatement
{
  /**
   * Returns the value of the '<em><b>Predicate</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Predicate</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predicate</em>' containment reference.
   * @see #setPredicate(IQLExpression)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLIfStatement_Predicate()
   * @model containment="true"
   * @generated
   */
  IQLExpression getPredicate();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getPredicate <em>Predicate</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Predicate</em>' containment reference.
   * @see #getPredicate()
   * @generated
   */
  void setPredicate(IQLExpression value);

  /**
   * Returns the value of the '<em><b>Then Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Then Body</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Then Body</em>' containment reference.
   * @see #setThenBody(IQLStatement)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLIfStatement_ThenBody()
   * @model containment="true"
   * @generated
   */
  IQLStatement getThenBody();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getThenBody <em>Then Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Then Body</em>' containment reference.
   * @see #getThenBody()
   * @generated
   */
  void setThenBody(IQLStatement value);

  /**
   * Returns the value of the '<em><b>Else Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Else Body</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else Body</em>' containment reference.
   * @see #setElseBody(IQLStatement)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLIfStatement_ElseBody()
   * @model containment="true"
   * @generated
   */
  IQLStatement getElseBody();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getElseBody <em>Else Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Else Body</em>' containment reference.
   * @see #getElseBody()
   * @generated
   */
  void setElseBody(IQLStatement value);

} // IQLIfStatement
