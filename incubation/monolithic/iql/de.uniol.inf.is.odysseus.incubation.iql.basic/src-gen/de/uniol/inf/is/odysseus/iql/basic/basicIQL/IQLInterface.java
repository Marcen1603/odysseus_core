/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.common.util.EList;

import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQL Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface#getExtendedInterfaces <em>Extended Interfaces</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLInterface()
 * @model
 * @generated
 */
public interface IQLInterface extends JvmGenericType
{
  /**
   * Returns the value of the '<em><b>Extended Interfaces</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.xtext.common.types.JvmTypeReference}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Extended Interfaces</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Extended Interfaces</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#getIQLInterface_ExtendedInterfaces()
   * @model containment="true"
   * @generated
   */
  EList<JvmTypeReference> getExtendedInterfaces();

} // IQLInterface
