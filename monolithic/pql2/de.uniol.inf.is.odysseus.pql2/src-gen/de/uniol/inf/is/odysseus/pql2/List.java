/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.List#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getList()
 * @model
 * @generated
 */
public interface List extends ListParameterValue
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.pql2.ParameterValue}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getList_Elements()
   * @model containment="true"
   * @generated
   */
  EList<ParameterValue> getElements();

} // List
