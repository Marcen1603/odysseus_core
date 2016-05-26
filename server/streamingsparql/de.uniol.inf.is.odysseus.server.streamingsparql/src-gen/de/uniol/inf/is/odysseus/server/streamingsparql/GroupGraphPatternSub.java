/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Graph Pattern Sub</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub#getGraphPatterns <em>Graph Patterns</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getGroupGraphPatternSub()
 * @model
 * @generated
 */
public interface GroupGraphPatternSub extends EObject
{
  /**
   * Returns the value of the '<em><b>Graph Patterns</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Graph Patterns</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Graph Patterns</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getGroupGraphPatternSub_GraphPatterns()
   * @model containment="true"
   * @generated
   */
  EList<TriplesSameSubject> getGraphPatterns();

} // GroupGraphPatternSub
