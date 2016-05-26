/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage
 * @generated
 */
public interface StreamingsparqlFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  StreamingsparqlFactory eINSTANCE = de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlFactoryImpl.init();

  /**
   * Returns a new object of class '<em>SPARQL Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SPARQL Query</em>'.
   * @generated
   */
  SPARQLQuery createSPARQLQuery();

  /**
   * Returns a new object of class '<em>Prefix</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prefix</em>'.
   * @generated
   */
  Prefix createPrefix();

  /**
   * Returns a new object of class '<em>Base</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Base</em>'.
   * @generated
   */
  Base createBase();

  /**
   * Returns a new object of class '<em>Select Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Select Query</em>'.
   * @generated
   */
  SelectQuery createSelectQuery();

  /**
   * Returns a new object of class '<em>Aggregate</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Aggregate</em>'.
   * @generated
   */
  Aggregate createAggregate();

  /**
   * Returns a new object of class '<em>Group By</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Group By</em>'.
   * @generated
   */
  GroupBy createGroupBy();

  /**
   * Returns a new object of class '<em>Aggregation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Aggregation</em>'.
   * @generated
   */
  Aggregation createAggregation();

  /**
   * Returns a new object of class '<em>Filesinkclause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Filesinkclause</em>'.
   * @generated
   */
  Filesinkclause createFilesinkclause();

  /**
   * Returns a new object of class '<em>Filterclause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Filterclause</em>'.
   * @generated
   */
  Filterclause createFilterclause();

  /**
   * Returns a new object of class '<em>Group Clause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Group Clause</em>'.
   * @generated
   */
  GroupClause createGroupClause();

  /**
   * Returns a new object of class '<em>Dataset Clause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Dataset Clause</em>'.
   * @generated
   */
  DatasetClause createDatasetClause();

  /**
   * Returns a new object of class '<em>Where Clause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Where Clause</em>'.
   * @generated
   */
  WhereClause createWhereClause();

  /**
   * Returns a new object of class '<em>Inner Where Clause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Inner Where Clause</em>'.
   * @generated
   */
  InnerWhereClause createInnerWhereClause();

  /**
   * Returns a new object of class '<em>Group Graph Pattern Sub</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Group Graph Pattern Sub</em>'.
   * @generated
   */
  GroupGraphPatternSub createGroupGraphPatternSub();

  /**
   * Returns a new object of class '<em>Triples Same Subject</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Triples Same Subject</em>'.
   * @generated
   */
  TriplesSameSubject createTriplesSameSubject();

  /**
   * Returns a new object of class '<em>Property List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Property List</em>'.
   * @generated
   */
  PropertyList createPropertyList();

  /**
   * Returns a new object of class '<em>Graph Node</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Graph Node</em>'.
   * @generated
   */
  GraphNode createGraphNode();

  /**
   * Returns a new object of class '<em>Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Variable</em>'.
   * @generated
   */
  Variable createVariable();

  /**
   * Returns a new object of class '<em>Un Named Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Un Named Variable</em>'.
   * @generated
   */
  UnNamedVariable createUnNamedVariable();

  /**
   * Returns a new object of class '<em>Named Variable</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Named Variable</em>'.
   * @generated
   */
  NamedVariable createNamedVariable();

  /**
   * Returns a new object of class '<em>IRI</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>IRI</em>'.
   * @generated
   */
  IRI createIRI();

  /**
   * Returns a new object of class '<em>Type Tag</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Type Tag</em>'.
   * @generated
   */
  TypeTag createTypeTag();

  /**
   * Returns a new object of class '<em>Lang Tag</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Lang Tag</em>'.
   * @generated
   */
  LangTag createLangTag();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  StreamingsparqlPackage getStreamingsparqlPackage();

} //StreamingsparqlFactory
