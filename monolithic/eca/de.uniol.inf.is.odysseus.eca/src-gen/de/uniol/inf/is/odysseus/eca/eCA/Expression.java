/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsource <em>Subsource</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsys <em>Subsys</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getComAction <em>Com Action</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getExpression()
 * @model
 * @generated
 */
public interface Expression extends EObject
{
  /**
   * Returns the value of the '<em><b>Subsource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subsource</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subsource</em>' containment reference.
   * @see #setSubsource(SOURCECONDITION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getExpression_Subsource()
   * @model containment="true"
   * @generated
   */
  SOURCECONDITION getSubsource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsource <em>Subsource</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subsource</em>' containment reference.
   * @see #getSubsource()
   * @generated
   */
  void setSubsource(SOURCECONDITION value);

  /**
   * Returns the value of the '<em><b>Subsys</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subsys</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subsys</em>' containment reference.
   * @see #setSubsys(SYSTEMCONDITION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getExpression_Subsys()
   * @model containment="true"
   * @generated
   */
  SYSTEMCONDITION getSubsys();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsys <em>Subsys</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subsys</em>' containment reference.
   * @see #getSubsys()
   * @generated
   */
  void setSubsys(SYSTEMCONDITION value);

  /**
   * Returns the value of the '<em><b>Com Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Com Action</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Com Action</em>' containment reference.
   * @see #setComAction(COMMANDACTION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getExpression_ComAction()
   * @model containment="true"
   * @generated
   */
  COMMANDACTION getComAction();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getComAction <em>Com Action</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Com Action</em>' containment reference.
   * @see #getComAction()
   * @generated
   */
  void setComAction(COMMANDACTION value);

} // Expression
