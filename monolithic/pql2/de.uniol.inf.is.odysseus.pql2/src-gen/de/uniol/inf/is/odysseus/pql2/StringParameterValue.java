/**
 */
package de.uniol.inf.is.odysseus.pql2;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>String Parameter Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.StringParameterValue#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getStringParameterValue()
 * @model
 * @generated
 */
public interface StringParameterValue extends ParameterValue
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getStringParameterValue_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.pql2.StringParameterValue#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // StringParameterValue
