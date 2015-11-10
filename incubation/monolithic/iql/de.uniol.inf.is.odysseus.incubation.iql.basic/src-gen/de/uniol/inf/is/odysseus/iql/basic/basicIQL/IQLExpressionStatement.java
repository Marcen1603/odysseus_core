/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Expression Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLExpressionStatement()
 * @model
 * @generated
 */
public interface IQLExpressionStatement extends IQLStatement
{
  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(IQLExpression)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLExpressionStatement_Expression()
   * @model containment="true"
   * @generated
   */
  IQLExpression getExpression();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(IQLExpression value);

} // IQLExpressionStatement
