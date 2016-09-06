/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getConstants <em>Constants</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getDefEvents <em>Def Events</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getTimeIntervall <em>Time Intervall</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getRules <em>Rules</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Constants</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.eca.eCA.Constant}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Constants</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constants</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel_Constants()
   * @model containment="true"
   * @generated
   */
  EList<Constant> getConstants();

  /**
   * Returns the value of the '<em><b>Def Events</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Def Events</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Def Events</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel_DefEvents()
   * @model containment="true"
   * @generated
   */
  EList<DefinedEvent> getDefEvents();

  /**
   * Returns the value of the '<em><b>Window Size</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Window Size</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Window Size</em>' containment reference.
   * @see #setWindowSize(Window)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel_WindowSize()
   * @model containment="true"
   * @generated
   */
  Window getWindowSize();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getWindowSize <em>Window Size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Window Size</em>' containment reference.
   * @see #getWindowSize()
   * @generated
   */
  void setWindowSize(Window value);

  /**
   * Returns the value of the '<em><b>Time Intervall</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Time Intervall</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time Intervall</em>' containment reference.
   * @see #setTimeIntervall(Timer)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel_TimeIntervall()
   * @model containment="true"
   * @generated
   */
  Timer getTimeIntervall();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getTimeIntervall <em>Time Intervall</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time Intervall</em>' containment reference.
   * @see #getTimeIntervall()
   * @generated
   */
  void setTimeIntervall(Timer value);

  /**
   * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.eca.eCA.Rule}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rules</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getModel_Rules()
   * @model containment="true"
   * @generated
   */
  EList<Rule> getRules();

} // Model
