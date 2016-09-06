/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getSource <em>Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleConditions <em>Rule Conditions</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleActions <em>Rule Actions</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRule()
 * @model
 * @generated
 */
public interface Rule extends EObject
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
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRule_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source</em>' containment reference.
   * @see #setSource(RuleSource)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRule_Source()
   * @model containment="true"
   * @generated
   */
  RuleSource getSource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getSource <em>Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Source</em>' containment reference.
   * @see #getSource()
   * @generated
   */
  void setSource(RuleSource value);

  /**
   * Returns the value of the '<em><b>Rule Conditions</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rule Conditions</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rule Conditions</em>' containment reference.
   * @see #setRuleConditions(Expression)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRule_RuleConditions()
   * @model containment="true"
   * @generated
   */
  Expression getRuleConditions();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleConditions <em>Rule Conditions</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rule Conditions</em>' containment reference.
   * @see #getRuleConditions()
   * @generated
   */
  void setRuleConditions(Expression value);

  /**
   * Returns the value of the '<em><b>Rule Actions</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rule Actions</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rule Actions</em>' containment reference.
   * @see #setRuleActions(Expression)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRule_RuleActions()
   * @model containment="true"
   * @generated
   */
  Expression getRuleActions();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleActions <em>Rule Actions</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rule Actions</em>' containment reference.
   * @see #getRuleActions()
   * @generated
   */
  void setRuleActions(Expression value);

} // Rule
