/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Window</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.Window#getWindowValue <em>Window Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getWindow()
 * @model
 * @generated
 */
public interface Window extends EObject
{
  /**
   * Returns the value of the '<em><b>Window Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Window Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Window Value</em>' attribute.
   * @see #setWindowValue(int)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getWindow_WindowValue()
   * @model
   * @generated
   */
  int getWindowValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.Window#getWindowValue <em>Window Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Window Value</em>' attribute.
   * @see #getWindowValue()
   * @generated
   */
  void setWindowValue(int value);

} // Window
