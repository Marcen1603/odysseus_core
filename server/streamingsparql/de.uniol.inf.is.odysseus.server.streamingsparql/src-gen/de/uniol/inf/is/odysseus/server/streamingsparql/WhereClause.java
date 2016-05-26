/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Where Clause</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause#getWhereclauses <em>Whereclauses</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getWhereClause()
 * @model
 * @generated
 */
public interface WhereClause extends EObject
{
  /**
   * Returns the value of the '<em><b>Whereclauses</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Whereclauses</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Whereclauses</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getWhereClause_Whereclauses()
   * @model containment="true"
   * @generated
   */
  EList<InnerWhereClause> getWhereclauses();

} // WhereClause
