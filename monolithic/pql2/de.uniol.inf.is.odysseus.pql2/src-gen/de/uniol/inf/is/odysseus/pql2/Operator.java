/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperatorType <em>Operator Type</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperators <em>Operators</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.Operator#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperator()
 * @model
 * @generated
 */
public interface Operator extends EObject
{
  /**
   * Returns the value of the '<em><b>Operator Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operator Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator Type</em>' attribute.
   * @see #setOperatorType(String)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperator_OperatorType()
   * @model
   * @generated
   */
  String getOperatorType();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperatorType <em>Operator Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator Type</em>' attribute.
   * @see #getOperatorType()
   * @generated
   */
  void setOperatorType(String value);

  /**
   * Returns the value of the '<em><b>Operators</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operators</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operators</em>' containment reference.
   * @see #setOperators(OperatorList)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperator_Operators()
   * @model containment="true"
   * @generated
   */
  OperatorList getOperators();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperators <em>Operators</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operators</em>' containment reference.
   * @see #getOperators()
   * @generated
   */
  void setOperators(OperatorList value);

  /**
   * Returns the value of the '<em><b>Parameters</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameters</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameters</em>' containment reference.
   * @see #setParameters(ParameterList)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getOperator_Parameters()
   * @model containment="true"
   * @generated
   */
  ParameterList getParameters();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.Operator#getParameters <em>Parameters</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameters</em>' containment reference.
   * @see #getParameters()
   * @generated
   */
  void setParameters(ParameterList value);

} // Operator
