/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.ParameterList#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getParameterList()
 * @model
 * @generated
 */
public interface ParameterList extends EObject
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.pql2.Parameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getParameterList_Elements()
   * @model containment="true"
   * @generated
   */
  EList<Parameter> getElements();

} // ParameterList
