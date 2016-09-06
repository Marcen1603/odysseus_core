/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Defined Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedSource <em>Defined Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedAttribute <em>Defined Attribute</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedOperator <em>Defined Operator</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedValue <em>Defined Value</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent()
 * @model
 * @generated
 */
public interface DefinedEvent extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Defined Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Defined Source</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Defined Source</em>' containment reference.
   * @see #setDefinedSource(Source)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent_DefinedSource()
   * @model containment="true"
   * @generated
   */
  Source getDefinedSource();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedSource <em>Defined Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Defined Source</em>' containment reference.
   * @see #getDefinedSource()
   * @generated
   */
  void setDefinedSource(Source value);

  /**
   * Returns the value of the '<em><b>Defined Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Defined Attribute</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Defined Attribute</em>' attribute.
   * @see #setDefinedAttribute(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent_DefinedAttribute()
   * @model
   * @generated
   */
  String getDefinedAttribute();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedAttribute <em>Defined Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Defined Attribute</em>' attribute.
   * @see #getDefinedAttribute()
   * @generated
   */
  void setDefinedAttribute(String value);

  /**
   * Returns the value of the '<em><b>Defined Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Defined Operator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Defined Operator</em>' attribute.
   * @see #setDefinedOperator(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent_DefinedOperator()
   * @model
   * @generated
   */
  String getDefinedOperator();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedOperator <em>Defined Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Defined Operator</em>' attribute.
   * @see #getDefinedOperator()
   * @generated
   */
  void setDefinedOperator(String value);

  /**
   * Returns the value of the '<em><b>Defined Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Defined Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Defined Value</em>' containment reference.
   * @see #setDefinedValue(EcaValue)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getDefinedEvent_DefinedValue()
   * @model containment="true"
   * @generated
   */
  EcaValue getDefinedValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedValue <em>Defined Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Defined Value</em>' containment reference.
   * @see #getDefinedValue()
   * @generated
   */
  void setDefinedValue(EcaValue value);

} // DefinedEvent
