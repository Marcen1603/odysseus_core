/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Aggregate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getAggregations <em>Aggregations</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getGroupby <em>Groupby</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregate()
 * @model
 * @generated
 */
public interface Aggregate extends EObject
{
  /**
   * Returns the value of the '<em><b>Aggregations</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Aggregations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Aggregations</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregate_Aggregations()
   * @model containment="true"
   * @generated
   */
  EList<Aggregation> getAggregations();

  /**
   * Returns the value of the '<em><b>Groupby</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Groupby</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Groupby</em>' containment reference.
   * @see #setGroupby(GroupBy)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregate_Groupby()
   * @model containment="true"
   * @generated
   */
  GroupBy getGroupby();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getGroupby <em>Groupby</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Groupby</em>' containment reference.
   * @see #getGroupby()
   * @generated
   */
  void setGroupby(GroupBy value);

} // Aggregate
