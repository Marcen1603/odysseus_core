/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getDefSource <em>Def Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getNewSource <em>New Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getPreSource <em>Pre Source</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRuleSource()
 * @model
 * @generated
 */
public interface RuleSource extends EObject
{
  /**
   * Returns the value of the '<em><b>Def Source</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Def Source</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Def Source</em>' reference.
   * @see #setDefSource(DefinedEvent)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRuleSource_DefSource()
   * @model
   * @generated
   */
  DefinedEvent getDefSource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getDefSource <em>Def Source</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Def Source</em>' reference.
   * @see #getDefSource()
   * @generated
   */
  void setDefSource(DefinedEvent value);

  /**
   * Returns the value of the '<em><b>New Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>New Source</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>New Source</em>' containment reference.
   * @see #setNewSource(Source)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRuleSource_NewSource()
   * @model containment="true"
   * @generated
   */
  Source getNewSource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getNewSource <em>New Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>New Source</em>' containment reference.
   * @see #getNewSource()
   * @generated
   */
  void setNewSource(Source value);

  /**
   * Returns the value of the '<em><b>Pre Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pre Source</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pre Source</em>' attribute.
   * @see #setPreSource(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRuleSource_PreSource()
   * @model
   * @generated
   */
  String getPreSource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getPreSource <em>Pre Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pre Source</em>' attribute.
   * @see #getPreSource()
   * @generated
   */
  void setPreSource(String value);

} // RuleSource
