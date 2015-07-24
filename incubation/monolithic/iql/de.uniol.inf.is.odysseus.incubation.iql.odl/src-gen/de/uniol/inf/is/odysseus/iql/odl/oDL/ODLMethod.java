/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isOn <em>On</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isValidate <em>Validate</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLMethod()
 * @model
 * @generated
 */
public interface ODLMethod extends IQLMethod
{
  /**
   * Returns the value of the '<em><b>On</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On</em>' attribute.
   * @see #setOn(boolean)
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLMethod_On()
   * @model
   * @generated
   */
  boolean isOn();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isOn <em>On</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On</em>' attribute.
   * @see #isOn()
   * @generated
   */
  void setOn(boolean value);

  /**
   * Returns the value of the '<em><b>Validate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Validate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Validate</em>' attribute.
   * @see #setValidate(boolean)
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#getODLMethod_Validate()
   * @model
   * @generated
   */
  boolean isValidate();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isValidate <em>Validate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Validate</em>' attribute.
   * @see #isValidate()
   * @generated
   */
  void setValidate(boolean value);

} // ODLMethod
