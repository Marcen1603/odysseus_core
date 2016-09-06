/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PQL Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.pql2.PQLModel#getQueries <em>Queries</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getPQLModel()
 * @model
 * @generated
 */
public interface PQLModel extends EObject
{
  /**
   * Returns the value of the '<em><b>Queries</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.pql2.Query}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Queries</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Queries</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.pql2.Pql2Package#getPQLModel_Queries()
   * @model containment="true"
   * @generated
   */
  EList<Query> getQueries();

} // PQLModel
