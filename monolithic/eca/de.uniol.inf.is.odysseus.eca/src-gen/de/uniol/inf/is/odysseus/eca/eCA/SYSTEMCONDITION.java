/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SYSTEMCONDITION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getSystemAttribute <em>System Attribute</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getOperator <em>Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSYSTEMCONDITION()
 * @model
 * @generated
 */
public interface SYSTEMCONDITION extends EObject
{
  /**
   * Returns the value of the '<em><b>System Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>System Attribute</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>System Attribute</em>' attribute.
   * @see #setSystemAttribute(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSYSTEMCONDITION_SystemAttribute()
   * @model
   * @generated
   */
  String getSystemAttribute();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getSystemAttribute <em>System Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>System Attribute</em>' attribute.
   * @see #getSystemAttribute()
   * @generated
   */
  void setSystemAttribute(String value);

  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see #setOperator(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSYSTEMCONDITION_Operator()
   * @model
   * @generated
   */
  String getOperator();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see #getOperator()
   * @generated
   */
  void setOperator(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(EcaValue)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSYSTEMCONDITION_Value()
   * @model containment="true"
   * @generated
   */
  EcaValue getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(EcaValue value);

} // SYSTEMCONDITION
