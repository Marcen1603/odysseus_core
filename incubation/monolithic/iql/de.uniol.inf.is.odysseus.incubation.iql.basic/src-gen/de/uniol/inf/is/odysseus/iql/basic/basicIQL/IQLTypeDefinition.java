/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.common.types.JvmGenericType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Type Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition#getJavametadata <em>Javametadata</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition#getInner <em>Inner</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLTypeDefinition()
 * @model
 * @generated
 */
public interface IQLTypeDefinition extends EObject
{
  /**
   * Returns the value of the '<em><b>Javametadata</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Javametadata</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Javametadata</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLTypeDefinition_Javametadata()
   * @model containment="true"
   * @generated
   */
  EList<IQLJavaMetadata> getJavametadata();

  /**
   * Returns the value of the '<em><b>Inner</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inner</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inner</em>' containment reference.
   * @see #setInner(JvmGenericType)
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLTypeDefinition_Inner()
   * @model containment="true"
   * @generated
   */
  JvmGenericType getInner();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition#getInner <em>Inner</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Inner</em>' containment reference.
   * @see #getInner()
   * @generated
   */
  void setInner(JvmGenericType value);

} // IQLTypeDefinition
