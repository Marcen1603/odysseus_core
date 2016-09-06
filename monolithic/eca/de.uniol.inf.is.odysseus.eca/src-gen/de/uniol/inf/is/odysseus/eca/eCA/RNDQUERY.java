/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RNDQUERY</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriOperator <em>Pri Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriVal <em>Pri Val</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getSel <em>Sel</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getStateName <em>State Name</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRNDQUERY()
 * @model
 * @generated
 */
public interface RNDQUERY extends EObject
{
  /**
   * Returns the value of the '<em><b>Pri Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pri Operator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pri Operator</em>' attribute.
   * @see #setPriOperator(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRNDQUERY_PriOperator()
   * @model
   * @generated
   */
  String getPriOperator();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriOperator <em>Pri Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pri Operator</em>' attribute.
   * @see #getPriOperator()
   * @generated
   */
  void setPriOperator(String value);

  /**
   * Returns the value of the '<em><b>Pri Val</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pri Val</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pri Val</em>' attribute.
   * @see #setPriVal(int)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRNDQUERY_PriVal()
   * @model
   * @generated
   */
  int getPriVal();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriVal <em>Pri Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pri Val</em>' attribute.
   * @see #getPriVal()
   * @generated
   */
  void setPriVal(int value);

  /**
   * Returns the value of the '<em><b>Sel</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sel</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sel</em>' attribute.
   * @see #setSel(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRNDQUERY_Sel()
   * @model
   * @generated
   */
  String getSel();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getSel <em>Sel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sel</em>' attribute.
   * @see #getSel()
   * @generated
   */
  void setSel(String value);

  /**
   * Returns the value of the '<em><b>State Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>State Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>State Name</em>' attribute.
   * @see #setStateName(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getRNDQUERY_StateName()
   * @model
   * @generated
   */
  String getStateName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getStateName <em>State Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State Name</em>' attribute.
   * @see #getStateName()
   * @generated
   */
  void setStateName(String value);

} // RNDQUERY
