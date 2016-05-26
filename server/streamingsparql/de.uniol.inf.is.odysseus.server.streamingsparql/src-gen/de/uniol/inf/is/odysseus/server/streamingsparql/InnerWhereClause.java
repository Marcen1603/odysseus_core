/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inner Where Clause</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getGroupGraphPattern <em>Group Graph Pattern</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getInnerWhereClause()
 * @model
 * @generated
 */
public interface InnerWhereClause extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' reference.
   * @see #setName(DatasetClause)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getInnerWhereClause_Name()
   * @model
   * @generated
   */
  DatasetClause getName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getName <em>Name</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' reference.
   * @see #getName()
   * @generated
   */
  void setName(DatasetClause value);

  /**
   * Returns the value of the '<em><b>Group Graph Pattern</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Group Graph Pattern</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Group Graph Pattern</em>' containment reference.
   * @see #setGroupGraphPattern(GroupGraphPatternSub)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getInnerWhereClause_GroupGraphPattern()
   * @model containment="true"
   * @generated
   */
  GroupGraphPatternSub getGroupGraphPattern();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getGroupGraphPattern <em>Group Graph Pattern</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Group Graph Pattern</em>' containment reference.
   * @see #getGroupGraphPattern()
   * @generated
   */
  void setGroupGraphPattern(GroupGraphPatternSub value);

} // InnerWhereClause
