/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SOURCECONDITION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getCondAttribute <em>Cond Attribute</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getOperator <em>Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSOURCECONDITION()
 * @model
 * @generated
 */
public interface SOURCECONDITION extends EObject
{
  /**
   * Returns the value of the '<em><b>Cond Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Cond Attribute</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Cond Attribute</em>' attribute.
   * @see #setCondAttribute(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSOURCECONDITION_CondAttribute()
   * @model
   * @generated
   */
  String getCondAttribute();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getCondAttribute <em>Cond Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Cond Attribute</em>' attribute.
   * @see #getCondAttribute()
   * @generated
   */
  void setCondAttribute(String value);

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
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSOURCECONDITION_Operator()
   * @model
   * @generated
   */
  String getOperator();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getOperator <em>Operator</em>}' attribute.
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
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSOURCECONDITION_Value()
   * @model containment="true"
   * @generated
   */
  EcaValue getValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
  void setValue(EcaValue value);

} // SOURCECONDITION
