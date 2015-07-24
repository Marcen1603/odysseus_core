/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Plus Minus Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOperand <em>Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLPlusMinusExpression()
 * @model
 * @generated
 */
public interface IQLPlusMinusExpression extends IQLExpression
{
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
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLPlusMinusExpression_Op()
   * @model
   * @generated
   */
  String getOp();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOp <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op</em>' attribute.
   * @see #getOp()
   * @generated
   */
  void setOp(String value);

  /**
   * Returns the value of the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operand</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operand</em>' containment reference.
   * @see #setOperand(IQLExpression)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLPlusMinusExpression_Operand()
   * @model containment="true"
   * @generated
   */
  IQLExpression getOperand();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOperand <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operand</em>' containment reference.
   * @see #getOperand()
   * @generated
   */
  void setOperand(IQLExpression value);

} // IQLPlusMinusExpression
