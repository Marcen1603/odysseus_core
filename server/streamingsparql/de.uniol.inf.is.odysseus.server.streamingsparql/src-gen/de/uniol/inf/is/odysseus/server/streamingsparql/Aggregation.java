/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Aggregation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getFunction <em>Function</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getVarToAgg <em>Var To Agg</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getAggName <em>Agg Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getDatatype <em>Datatype</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregation()
 * @model
 * @generated
 */
public interface Aggregation extends EObject
{
  /**
   * Returns the value of the '<em><b>Function</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Function</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Function</em>' attribute.
   * @see #setFunction(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregation_Function()
   * @model
   * @generated
   */
  String getFunction();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getFunction <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Function</em>' attribute.
   * @see #getFunction()
   * @generated
   */
  void setFunction(String value);

  /**
   * Returns the value of the '<em><b>Var To Agg</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Var To Agg</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var To Agg</em>' containment reference.
   * @see #setVarToAgg(Variable)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregation_VarToAgg()
   * @model containment="true"
   * @generated
   */
  Variable getVarToAgg();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getVarToAgg <em>Var To Agg</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var To Agg</em>' containment reference.
   * @see #getVarToAgg()
   * @generated
   */
  void setVarToAgg(Variable value);

  /**
   * Returns the value of the '<em><b>Agg Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Agg Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Agg Name</em>' attribute.
   * @see #setAggName(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregation_AggName()
   * @model
   * @generated
   */
  String getAggName();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getAggName <em>Agg Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Agg Name</em>' attribute.
   * @see #getAggName()
   * @generated
   */
  void setAggName(String value);

  /**
   * Returns the value of the '<em><b>Datatype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Datatype</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Datatype</em>' attribute.
   * @see #setDatatype(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getAggregation_Datatype()
   * @model
   * @generated
   */
  String getDatatype();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getDatatype <em>Datatype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Datatype</em>' attribute.
   * @see #getDatatype()
   * @generated
   */
  void setDatatype(String value);

} // Aggregation
