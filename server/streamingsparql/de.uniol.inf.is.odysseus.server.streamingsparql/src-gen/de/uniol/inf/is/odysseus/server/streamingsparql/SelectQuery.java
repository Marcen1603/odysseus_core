/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Select Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getMethod <em>Method</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getBase <em>Base</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getPrefixes <em>Prefixes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getDatasetClauses <em>Dataset Clauses</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsDistinct <em>Is Distinct</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsReduced <em>Is Reduced</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getVariables <em>Variables</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getWhereClause <em>Where Clause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilterclause <em>Filterclause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getAggregateClause <em>Aggregate Clause</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilesinkclause <em>Filesinkclause</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery()
 * @model
 * @generated
 */
public interface SelectQuery extends SPARQLQuery
{
  /**
   * Returns the value of the '<em><b>Method</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method</em>' attribute.
   * @see #setMethod(String)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Method()
   * @model
   * @generated
   */
  String getMethod();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getMethod <em>Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Method</em>' attribute.
   * @see #getMethod()
   * @generated
   */
  void setMethod(String value);

  /**
   * Returns the value of the '<em><b>Base</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Base</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Base</em>' containment reference.
   * @see #setBase(Base)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Base()
   * @model containment="true"
   * @generated
   */
  Base getBase();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getBase <em>Base</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Base</em>' containment reference.
   * @see #getBase()
   * @generated
   */
  void setBase(Base value);

  /**
   * Returns the value of the '<em><b>Prefixes</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prefixes</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prefixes</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Prefixes()
   * @model containment="true"
   * @generated
   */
  EList<Prefix> getPrefixes();

  /**
   * Returns the value of the '<em><b>Dataset Clauses</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Dataset Clauses</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dataset Clauses</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_DatasetClauses()
   * @model containment="true"
   * @generated
   */
  EList<DatasetClause> getDatasetClauses();

  /**
   * Returns the value of the '<em><b>Is Distinct</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Is Distinct</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Is Distinct</em>' attribute.
   * @see #setIsDistinct(boolean)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_IsDistinct()
   * @model
   * @generated
   */
  boolean isIsDistinct();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsDistinct <em>Is Distinct</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Distinct</em>' attribute.
   * @see #isIsDistinct()
   * @generated
   */
  void setIsDistinct(boolean value);

  /**
   * Returns the value of the '<em><b>Is Reduced</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Is Reduced</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Is Reduced</em>' attribute.
   * @see #setIsReduced(boolean)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_IsReduced()
   * @model
   * @generated
   */
  boolean isIsReduced();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsReduced <em>Is Reduced</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Reduced</em>' attribute.
   * @see #isIsReduced()
   * @generated
   */
  void setIsReduced(boolean value);

  /**
   * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variables</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variables</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Variables()
   * @model containment="true"
   * @generated
   */
  EList<Variable> getVariables();

  /**
   * Returns the value of the '<em><b>Where Clause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Where Clause</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Where Clause</em>' containment reference.
   * @see #setWhereClause(WhereClause)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_WhereClause()
   * @model containment="true"
   * @generated
   */
  WhereClause getWhereClause();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getWhereClause <em>Where Clause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Where Clause</em>' containment reference.
   * @see #getWhereClause()
   * @generated
   */
  void setWhereClause(WhereClause value);

  /**
   * Returns the value of the '<em><b>Filterclause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filterclause</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filterclause</em>' containment reference.
   * @see #setFilterclause(Filterclause)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Filterclause()
   * @model containment="true"
   * @generated
   */
  Filterclause getFilterclause();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilterclause <em>Filterclause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filterclause</em>' containment reference.
   * @see #getFilterclause()
   * @generated
   */
  void setFilterclause(Filterclause value);

  /**
   * Returns the value of the '<em><b>Aggregate Clause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Aggregate Clause</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Aggregate Clause</em>' containment reference.
   * @see #setAggregateClause(Aggregate)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_AggregateClause()
   * @model containment="true"
   * @generated
   */
  Aggregate getAggregateClause();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getAggregateClause <em>Aggregate Clause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Aggregate Clause</em>' containment reference.
   * @see #getAggregateClause()
   * @generated
   */
  void setAggregateClause(Aggregate value);

  /**
   * Returns the value of the '<em><b>Filesinkclause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filesinkclause</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filesinkclause</em>' containment reference.
   * @see #setFilesinkclause(Filesinkclause)
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#getSelectQuery_Filesinkclause()
   * @model containment="true"
   * @generated
   */
  Filesinkclause getFilesinkclause();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilesinkclause <em>Filesinkclause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filesinkclause</em>' containment reference.
   * @see #getFilesinkclause()
   * @generated
   */
  void setFilesinkclause(Filesinkclause value);

} // SelectQuery
