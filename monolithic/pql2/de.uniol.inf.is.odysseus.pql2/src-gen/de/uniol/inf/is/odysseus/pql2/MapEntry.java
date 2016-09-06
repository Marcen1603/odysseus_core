/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getKey <em>Key</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getMapEntry()
 * @model
 * @generated
 */
public interface MapEntry extends EObject
{
  /**
   * Returns the value of the '<em><b>Key</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Key</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Key</em>' containment reference.
   * @see #setKey(ParameterValue)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getMapEntry_Key()
   * @model containment="true"
   * @generated
   */
  ParameterValue getKey();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getKey <em>Key</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Key</em>' containment reference.
   * @see #getKey()
   * @generated
   */
  void setKey(ParameterValue value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(ParameterValue)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getMapEntry_Value()
   * @model containment="true"
   * @generated
   */
  ParameterValue getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(ParameterValue value);

} // MapEntry
