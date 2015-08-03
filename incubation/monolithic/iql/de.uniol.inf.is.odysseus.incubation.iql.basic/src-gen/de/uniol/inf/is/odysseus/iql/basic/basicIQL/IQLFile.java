/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL File</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile#getNamespaces <em>Namespaces</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLFile()
 * @model
 * @generated
 */
public interface IQLFile extends EObject
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
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLFile_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Namespaces</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Namespaces</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Namespaces</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLFile_Namespaces()
   * @model containment="true"
   * @generated
   */
  EList<IQLNamespace> getNamespaces();

  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLFile_Elements()
   * @model containment="true"
   * @generated
   */
  EList<IQLTypeDefinition> getElements();

} // IQLFile
