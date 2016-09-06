/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Timer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Timer#getTimerIntervallValue <em>Timer Intervall Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getTimer()
 * @model
 * @generated
 */
public interface Timer extends EObject
{
  /**
   * Returns the value of the '<em><b>Timer Intervall Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Timer Intervall Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Timer Intervall Value</em>' attribute.
   * @see #setTimerIntervallValue(int)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getTimer_TimerIntervallValue()
   * @model
   * @generated
   */
  int getTimerIntervallValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Timer#getTimerIntervallValue <em>Timer Intervall Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Timer Intervall Value</em>' attribute.
   * @see #getTimerIntervallValue()
   * @generated
   */
  void setTimerIntervallValue(int value);

} // Timer
