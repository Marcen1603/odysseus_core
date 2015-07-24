/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Java</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava#getText <em>Text</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLJava()
 * @model
 * @generated
 */
public interface IQLJava extends EObject
{
  /**
   * Returns the value of the '<em><b>Text</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Text</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Text</em>' attribute list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLJava_Text()
   * @model unique="false"
   * @generated
   */
  EList<String> getText();

  /**
   * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Keywords</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Keywords</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLJava_Keywords()
   * @model containment="true"
   * @generated
   */
  EList<IQLJavaKeywords> getKeywords();

} // IQLJava
