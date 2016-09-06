/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Logical Or Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getLeftOperand <em>Left Operand</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getOp <em>Op</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getRightOperand <em>Right Operand</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLLogicalOrExpression()
 * @model
 * @generated
 */
public interface IQLLogicalOrExpression extends IQLExpression
{
  /**
   * Returns the value of the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left Operand</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left Operand</em>' containment reference.
   * @see #setLeftOperand(IQLExpression)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLLogicalOrExpression_LeftOperand()
   * @model containment="true"
   * @generated
   */
  IQLExpression getLeftOperand();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getLeftOperand <em>Left Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left Operand</em>' containment reference.
   * @see #getLeftOperand()
   * @generated
   */
  void setLeftOperand(IQLExpression value);

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
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLLogicalOrExpression_Op()
   * @model
   * @generated
   */
  String getOp();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getOp <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op</em>' attribute.
   * @see #getOp()
   * @generated
   */
  void setOp(String value);

  /**
   * Returns the value of the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right Operand</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right Operand</em>' containment reference.
   * @see #setRightOperand(IQLExpression)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLLogicalOrExpression_RightOperand()
   * @model containment="true"
   * @generated
   */
  IQLExpression getRightOperand();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getRightOperand <em>Right Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right Operand</em>' containment reference.
   * @see #getRightOperand()
   * @generated
   */
  void setRightOperand(IQLExpression value);

} // IQLLogicalOrExpression
