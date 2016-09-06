/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Eca Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIntValue <em>Int Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIdValue <em>Id Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getConstValue <em>Const Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getStringValue <em>String Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getDoubleValue <em>Double Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue()
 * @model
 * @generated
 */
public interface EcaValue extends EObject
{
  /**
   * Returns the value of the '<em><b>Int Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Int Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Int Value</em>' attribute.
   * @see #setIntValue(int)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue_IntValue()
   * @model
   * @generated
   */
  int getIntValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIntValue <em>Int Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Int Value</em>' attribute.
   * @see #getIntValue()
   * @generated
   */
  void setIntValue(int value);

  /**
   * Returns the value of the '<em><b>Id Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id Value</em>' attribute.
   * @see #setIdValue(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue_IdValue()
   * @model
   * @generated
   */
  String getIdValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIdValue <em>Id Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id Value</em>' attribute.
   * @see #getIdValue()
   * @generated
   */
  void setIdValue(String value);

  /**
   * Returns the value of the '<em><b>Const Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Const Value</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Const Value</em>' reference.
   * @see #setConstValue(Constant)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue_ConstValue()
   * @model
   * @generated
   */
  Constant getConstValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getConstValue <em>Const Value</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Const Value</em>' reference.
   * @see #getConstValue()
   * @generated
   */
  void setConstValue(Constant value);

  /**
   * Returns the value of the '<em><b>String Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>String Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>String Value</em>' attribute.
   * @see #setStringValue(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue_StringValue()
   * @model
   * @generated
   */
  String getStringValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getStringValue <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>String Value</em>' attribute.
   * @see #getStringValue()
   * @generated
   */
  void setStringValue(String value);

  /**
   * Returns the value of the '<em><b>Double Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Double Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Double Value</em>' attribute.
   * @see #setDoubleValue(double)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getEcaValue_DoubleValue()
   * @model
   * @generated
   */
  double getDoubleValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getDoubleValue <em>Double Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Double Value</em>' attribute.
   * @see #getDoubleValue()
   * @generated
   */
  void setDoubleValue(double value);

} // EcaValue
