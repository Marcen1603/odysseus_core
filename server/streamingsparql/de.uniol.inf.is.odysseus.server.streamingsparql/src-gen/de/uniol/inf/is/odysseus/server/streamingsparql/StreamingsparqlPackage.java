/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlFactory
 * @model kind="package"
 * @generated
 */
public interface StreamingsparqlPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "streamingsparql";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/server/Streamingsparql";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "streamingsparql";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  StreamingsparqlPackage eINSTANCE = de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SPARQLQueryImpl <em>SPARQL Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.SPARQLQueryImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getSPARQLQuery()
   * @generated
   */
  int SPARQL_QUERY = 0;

  /**
   * The number of structural features of the '<em>SPARQL Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SPARQL_QUERY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.PrefixImpl <em>Prefix</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.PrefixImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getPrefix()
   * @generated
   */
  int PREFIX = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFIX__NAME = 0;

  /**
   * The feature id for the '<em><b>Iref</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFIX__IREF = 1;

  /**
   * The number of structural features of the '<em>Prefix</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFIX_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.BaseImpl <em>Base</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.BaseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getBase()
   * @generated
   */
  int BASE = 2;

  /**
   * The feature id for the '<em><b>Iref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BASE__IREF = 0;

  /**
   * The number of structural features of the '<em>Base</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BASE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl <em>Select Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getSelectQuery()
   * @generated
   */
  int SELECT_QUERY = 3;

  /**
   * The feature id for the '<em><b>Method</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__METHOD = SPARQL_QUERY_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Base</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__BASE = SPARQL_QUERY_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Prefixes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__PREFIXES = SPARQL_QUERY_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Dataset Clauses</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__DATASET_CLAUSES = SPARQL_QUERY_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Is Distinct</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__IS_DISTINCT = SPARQL_QUERY_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Is Reduced</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__IS_REDUCED = SPARQL_QUERY_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Variables</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__VARIABLES = SPARQL_QUERY_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Where Clause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__WHERE_CLAUSE = SPARQL_QUERY_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Filterclause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__FILTERCLAUSE = SPARQL_QUERY_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Aggregate Clause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__AGGREGATE_CLAUSE = SPARQL_QUERY_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>Filesinkclause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY__FILESINKCLAUSE = SPARQL_QUERY_FEATURE_COUNT + 10;

  /**
   * The number of structural features of the '<em>Select Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SELECT_QUERY_FEATURE_COUNT = SPARQL_QUERY_FEATURE_COUNT + 11;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl <em>Aggregate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getAggregate()
   * @generated
   */
  int AGGREGATE = 4;

  /**
   * The feature id for the '<em><b>Aggregations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATE__AGGREGATIONS = 0;

  /**
   * The feature id for the '<em><b>Groupby</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATE__GROUPBY = 1;

  /**
   * The number of structural features of the '<em>Aggregate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupByImpl <em>Group By</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupByImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupBy()
   * @generated
   */
  int GROUP_BY = 5;

  /**
   * The feature id for the '<em><b>Variables</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_BY__VARIABLES = 0;

  /**
   * The number of structural features of the '<em>Group By</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_BY_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl <em>Aggregation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getAggregation()
   * @generated
   */
  int AGGREGATION = 6;

  /**
   * The feature id for the '<em><b>Function</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATION__FUNCTION = 0;

  /**
   * The feature id for the '<em><b>Var To Agg</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATION__VAR_TO_AGG = 1;

  /**
   * The feature id for the '<em><b>Agg Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATION__AGG_NAME = 2;

  /**
   * The feature id for the '<em><b>Datatype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATION__DATATYPE = 3;

  /**
   * The number of structural features of the '<em>Aggregation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGGREGATION_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilesinkclauseImpl <em>Filesinkclause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilesinkclauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getFilesinkclause()
   * @generated
   */
  int FILESINKCLAUSE = 7;

  /**
   * The feature id for the '<em><b>Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILESINKCLAUSE__PATH = 0;

  /**
   * The number of structural features of the '<em>Filesinkclause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILESINKCLAUSE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilterclauseImpl <em>Filterclause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilterclauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getFilterclause()
   * @generated
   */
  int FILTERCLAUSE = 8;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERCLAUSE__LEFT = 0;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERCLAUSE__OPERATOR = 1;

  /**
   * The feature id for the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERCLAUSE__RIGHT = 2;

  /**
   * The number of structural features of the '<em>Filterclause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERCLAUSE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupClauseImpl <em>Group Clause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupClauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupClause()
   * @generated
   */
  int GROUP_CLAUSE = 9;

  /**
   * The feature id for the '<em><b>Conditions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_CLAUSE__CONDITIONS = 0;

  /**
   * The number of structural features of the '<em>Group Clause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_CLAUSE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.DatasetClauseImpl <em>Dataset Clause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.DatasetClauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getDatasetClause()
   * @generated
   */
  int DATASET_CLAUSE = 10;

  /**
   * The feature id for the '<em><b>Data Set</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__DATA_SET = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__NAME = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__TYPE = 2;

  /**
   * The feature id for the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__SIZE = 3;

  /**
   * The feature id for the '<em><b>Advance</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__ADVANCE = 4;

  /**
   * The feature id for the '<em><b>Unit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE__UNIT = 5;

  /**
   * The number of structural features of the '<em>Dataset Clause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATASET_CLAUSE_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.WhereClauseImpl <em>Where Clause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.WhereClauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getWhereClause()
   * @generated
   */
  int WHERE_CLAUSE = 11;

  /**
   * The feature id for the '<em><b>Whereclauses</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WHERE_CLAUSE__WHERECLAUSES = 0;

  /**
   * The number of structural features of the '<em>Where Clause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WHERE_CLAUSE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl <em>Inner Where Clause</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getInnerWhereClause()
   * @generated
   */
  int INNER_WHERE_CLAUSE = 12;

  /**
   * The feature id for the '<em><b>Name</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INNER_WHERE_CLAUSE__NAME = 0;

  /**
   * The feature id for the '<em><b>Group Graph Pattern</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN = 1;

  /**
   * The number of structural features of the '<em>Inner Where Clause</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INNER_WHERE_CLAUSE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupGraphPatternSubImpl <em>Group Graph Pattern Sub</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupGraphPatternSubImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupGraphPatternSub()
   * @generated
   */
  int GROUP_GRAPH_PATTERN_SUB = 13;

  /**
   * The feature id for the '<em><b>Graph Patterns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_GRAPH_PATTERN_SUB__GRAPH_PATTERNS = 0;

  /**
   * The number of structural features of the '<em>Group Graph Pattern Sub</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_GRAPH_PATTERN_SUB_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl <em>Triples Same Subject</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getTriplesSameSubject()
   * @generated
   */
  int TRIPLES_SAME_SUBJECT = 14;

  /**
   * The feature id for the '<em><b>Subject</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLES_SAME_SUBJECT__SUBJECT = 0;

  /**
   * The feature id for the '<em><b>Property List</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLES_SAME_SUBJECT__PROPERTY_LIST = 1;

  /**
   * The number of structural features of the '<em>Triples Same Subject</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRIPLES_SAME_SUBJECT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.PropertyListImpl <em>Property List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.PropertyListImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getPropertyList()
   * @generated
   */
  int PROPERTY_LIST = 15;

  /**
   * The feature id for the '<em><b>Property</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_LIST__PROPERTY = 0;

  /**
   * The feature id for the '<em><b>Object</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_LIST__OBJECT = 1;

  /**
   * The number of structural features of the '<em>Property List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_LIST_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GraphNodeImpl <em>Graph Node</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GraphNodeImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGraphNode()
   * @generated
   */
  int GRAPH_NODE = 16;

  /**
   * The feature id for the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GRAPH_NODE__VARIABLE = 0;

  /**
   * The feature id for the '<em><b>Literal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GRAPH_NODE__LITERAL = 1;

  /**
   * The feature id for the '<em><b>Iri</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GRAPH_NODE__IRI = 2;

  /**
   * The number of structural features of the '<em>Graph Node</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GRAPH_NODE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl <em>Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getVariable()
   * @generated
   */
  int VARIABLE = 17;

  /**
   * The feature id for the '<em><b>Unnamed</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__UNNAMED = 0;

  /**
   * The feature id for the '<em><b>Property</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__PROPERTY = 1;

  /**
   * The number of structural features of the '<em>Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.UnNamedVariableImpl <em>Un Named Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.UnNamedVariableImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getUnNamedVariable()
   * @generated
   */
  int UN_NAMED_VARIABLE = 18;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UN_NAMED_VARIABLE__NAME = 0;

  /**
   * The number of structural features of the '<em>Un Named Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UN_NAMED_VARIABLE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl <em>Named Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getNamedVariable()
   * @generated
   */
  int NAMED_VARIABLE = 19;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_VARIABLE__PREFIX = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_VARIABLE__NAME = 1;

  /**
   * The number of structural features of the '<em>Named Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMED_VARIABLE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.IRIImpl <em>IRI</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.IRIImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getIRI()
   * @generated
   */
  int IRI = 20;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IRI__VALUE = 0;

  /**
   * The number of structural features of the '<em>IRI</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IRI_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TypeTagImpl <em>Type Tag</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.TypeTagImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getTypeTag()
   * @generated
   */
  int TYPE_TAG = 21;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TAG__TYPE = 0;

  /**
   * The number of structural features of the '<em>Type Tag</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TAG_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.LangTagImpl <em>Lang Tag</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.LangTagImpl
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getLangTag()
   * @generated
   */
  int LANG_TAG = 22;

  /**
   * The feature id for the '<em><b>Lang</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LANG_TAG__LANG = 0;

  /**
   * The number of structural features of the '<em>Lang Tag</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LANG_TAG_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Operator <em>Operator</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Operator
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getOperator()
   * @generated
   */
  int OPERATOR = 23;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery <em>SPARQL Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>SPARQL Query</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery
   * @generated
   */
  EClass getSPARQLQuery();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Prefix</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Prefix
   * @generated
   */
  EClass getPrefix();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getName()
   * @see #getPrefix()
   * @generated
   */
  EAttribute getPrefix_Name();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getIref <em>Iref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Iref</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Prefix#getIref()
   * @see #getPrefix()
   * @generated
   */
  EAttribute getPrefix_Iref();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Base <em>Base</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Base</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Base
   * @generated
   */
  EClass getBase();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Base#getIref <em>Iref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Iref</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Base#getIref()
   * @see #getBase()
   * @generated
   */
  EReference getBase_Iref();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery <em>Select Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Select Query</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery
   * @generated
   */
  EClass getSelectQuery();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getMethod <em>Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Method</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getMethod()
   * @see #getSelectQuery()
   * @generated
   */
  EAttribute getSelectQuery_Method();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getBase <em>Base</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Base</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getBase()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_Base();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getPrefixes <em>Prefixes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Prefixes</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getPrefixes()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_Prefixes();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getDatasetClauses <em>Dataset Clauses</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Dataset Clauses</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getDatasetClauses()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_DatasetClauses();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsDistinct <em>Is Distinct</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Is Distinct</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsDistinct()
   * @see #getSelectQuery()
   * @generated
   */
  EAttribute getSelectQuery_IsDistinct();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsReduced <em>Is Reduced</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Is Reduced</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#isIsReduced()
   * @see #getSelectQuery()
   * @generated
   */
  EAttribute getSelectQuery_IsReduced();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getVariables <em>Variables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Variables</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getVariables()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_Variables();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getWhereClause <em>Where Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Where Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getWhereClause()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_WhereClause();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilterclause <em>Filterclause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Filterclause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilterclause()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_Filterclause();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getAggregateClause <em>Aggregate Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Aggregate Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getAggregateClause()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_AggregateClause();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilesinkclause <em>Filesinkclause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Filesinkclause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery#getFilesinkclause()
   * @see #getSelectQuery()
   * @generated
   */
  EReference getSelectQuery_Filesinkclause();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate <em>Aggregate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Aggregate</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate
   * @generated
   */
  EClass getAggregate();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getAggregations <em>Aggregations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Aggregations</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getAggregations()
   * @see #getAggregate()
   * @generated
   */
  EReference getAggregate_Aggregations();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getGroupby <em>Groupby</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Groupby</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate#getGroupby()
   * @see #getAggregate()
   * @generated
   */
  EReference getAggregate_Groupby();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy <em>Group By</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Group By</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy
   * @generated
   */
  EClass getGroupBy();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy#getVariables <em>Variables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Variables</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy#getVariables()
   * @see #getGroupBy()
   * @generated
   */
  EReference getGroupBy_Variables();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation <em>Aggregation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Aggregation</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation
   * @generated
   */
  EClass getAggregation();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getFunction <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Function</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getFunction()
   * @see #getAggregation()
   * @generated
   */
  EAttribute getAggregation_Function();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getVarToAgg <em>Var To Agg</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var To Agg</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getVarToAgg()
   * @see #getAggregation()
   * @generated
   */
  EReference getAggregation_VarToAgg();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getAggName <em>Agg Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Agg Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getAggName()
   * @see #getAggregation()
   * @generated
   */
  EAttribute getAggregation_AggName();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getDatatype <em>Datatype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Datatype</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation#getDatatype()
   * @see #getAggregation()
   * @generated
   */
  EAttribute getAggregation_Datatype();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause <em>Filesinkclause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Filesinkclause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause
   * @generated
   */
  EClass getFilesinkclause();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause#getPath <em>Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Path</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause#getPath()
   * @see #getFilesinkclause()
   * @generated
   */
  EAttribute getFilesinkclause_Path();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause <em>Filterclause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Filterclause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause
   * @generated
   */
  EClass getFilterclause();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getLeft()
   * @see #getFilterclause()
   * @generated
   */
  EReference getFilterclause_Left();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getOperator()
   * @see #getFilterclause()
   * @generated
   */
  EAttribute getFilterclause_Operator();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause#getRight()
   * @see #getFilterclause()
   * @generated
   */
  EReference getFilterclause_Right();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause <em>Group Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Group Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause
   * @generated
   */
  EClass getGroupClause();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause#getConditions <em>Conditions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Conditions</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause#getConditions()
   * @see #getGroupClause()
   * @generated
   */
  EReference getGroupClause_Conditions();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause <em>Dataset Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Dataset Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause
   * @generated
   */
  EClass getDatasetClause();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getDataSet <em>Data Set</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Data Set</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getDataSet()
   * @see #getDatasetClause()
   * @generated
   */
  EReference getDatasetClause_DataSet();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getName()
   * @see #getDatasetClause()
   * @generated
   */
  EAttribute getDatasetClause_Name();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getType()
   * @see #getDatasetClause()
   * @generated
   */
  EAttribute getDatasetClause_Type();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getSize <em>Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Size</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getSize()
   * @see #getDatasetClause()
   * @generated
   */
  EAttribute getDatasetClause_Size();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getAdvance <em>Advance</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Advance</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getAdvance()
   * @see #getDatasetClause()
   * @generated
   */
  EAttribute getDatasetClause_Advance();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getUnit <em>Unit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Unit</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause#getUnit()
   * @see #getDatasetClause()
   * @generated
   */
  EAttribute getDatasetClause_Unit();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause <em>Where Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Where Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause
   * @generated
   */
  EClass getWhereClause();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause#getWhereclauses <em>Whereclauses</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Whereclauses</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause#getWhereclauses()
   * @see #getWhereClause()
   * @generated
   */
  EReference getWhereClause_Whereclauses();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause <em>Inner Where Clause</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Inner Where Clause</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause
   * @generated
   */
  EClass getInnerWhereClause();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getName()
   * @see #getInnerWhereClause()
   * @generated
   */
  EReference getInnerWhereClause_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getGroupGraphPattern <em>Group Graph Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Group Graph Pattern</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause#getGroupGraphPattern()
   * @see #getInnerWhereClause()
   * @generated
   */
  EReference getInnerWhereClause_GroupGraphPattern();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub <em>Group Graph Pattern Sub</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Group Graph Pattern Sub</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub
   * @generated
   */
  EClass getGroupGraphPatternSub();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub#getGraphPatterns <em>Graph Patterns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Graph Patterns</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub#getGraphPatterns()
   * @see #getGroupGraphPatternSub()
   * @generated
   */
  EReference getGroupGraphPatternSub_GraphPatterns();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject <em>Triples Same Subject</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Triples Same Subject</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject
   * @generated
   */
  EClass getTriplesSameSubject();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getSubject <em>Subject</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Subject</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getSubject()
   * @see #getTriplesSameSubject()
   * @generated
   */
  EReference getTriplesSameSubject_Subject();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getPropertyList <em>Property List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Property List</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject#getPropertyList()
   * @see #getTriplesSameSubject()
   * @generated
   */
  EReference getTriplesSameSubject_PropertyList();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList <em>Property List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property List</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList
   * @generated
   */
  EClass getPropertyList();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getProperty <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Property</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getProperty()
   * @see #getPropertyList()
   * @generated
   */
  EReference getPropertyList_Property();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getObject <em>Object</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Object</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList#getObject()
   * @see #getPropertyList()
   * @generated
   */
  EReference getPropertyList_Object();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode <em>Graph Node</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Graph Node</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode
   * @generated
   */
  EClass getGraphNode();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getVariable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Variable</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getVariable()
   * @see #getGraphNode()
   * @generated
   */
  EReference getGraphNode_Variable();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getLiteral <em>Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Literal</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getLiteral()
   * @see #getGraphNode()
   * @generated
   */
  EAttribute getGraphNode_Literal();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getIri <em>Iri</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Iri</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode#getIri()
   * @see #getGraphNode()
   * @generated
   */
  EReference getGraphNode_Iri();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Variable
   * @generated
   */
  EClass getVariable();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getUnnamed <em>Unnamed</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Unnamed</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getUnnamed()
   * @see #getVariable()
   * @generated
   */
  EReference getVariable_Unnamed();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getProperty <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Property</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Variable#getProperty()
   * @see #getVariable()
   * @generated
   */
  EReference getVariable_Property();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable <em>Un Named Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Un Named Variable</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable
   * @generated
   */
  EClass getUnNamedVariable();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable#getName()
   * @see #getUnNamedVariable()
   * @generated
   */
  EAttribute getUnNamedVariable_Name();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable <em>Named Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Named Variable</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable
   * @generated
   */
  EClass getNamedVariable();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Prefix</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable#getPrefix()
   * @see #getNamedVariable()
   * @generated
   */
  EReference getNamedVariable_Prefix();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable#getName()
   * @see #getNamedVariable()
   * @generated
   */
  EAttribute getNamedVariable_Name();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.IRI <em>IRI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IRI</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.IRI
   * @generated
   */
  EClass getIRI();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.IRI#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.IRI#getValue()
   * @see #getIRI()
   * @generated
   */
  EAttribute getIRI_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag <em>Type Tag</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type Tag</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag
   * @generated
   */
  EClass getTypeTag();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag#getType()
   * @see #getTypeTag()
   * @generated
   */
  EReference getTypeTag_Type();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.LangTag <em>Lang Tag</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Lang Tag</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.LangTag
   * @generated
   */
  EClass getLangTag();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.server.streamingsparql.LangTag#getLang <em>Lang</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Lang</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.LangTag#getLang()
   * @see #getLangTag()
   * @generated
   */
  EAttribute getLangTag_Lang();

  /**
   * Returns the meta object for enum '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Operator
   * @generated
   */
  EEnum getOperator();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  StreamingsparqlFactory getStreamingsparqlFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SPARQLQueryImpl <em>SPARQL Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.SPARQLQueryImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getSPARQLQuery()
     * @generated
     */
    EClass SPARQL_QUERY = eINSTANCE.getSPARQLQuery();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.PrefixImpl <em>Prefix</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.PrefixImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getPrefix()
     * @generated
     */
    EClass PREFIX = eINSTANCE.getPrefix();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFIX__NAME = eINSTANCE.getPrefix_Name();

    /**
     * The meta object literal for the '<em><b>Iref</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFIX__IREF = eINSTANCE.getPrefix_Iref();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.BaseImpl <em>Base</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.BaseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getBase()
     * @generated
     */
    EClass BASE = eINSTANCE.getBase();

    /**
     * The meta object literal for the '<em><b>Iref</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BASE__IREF = eINSTANCE.getBase_Iref();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl <em>Select Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.SelectQueryImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getSelectQuery()
     * @generated
     */
    EClass SELECT_QUERY = eINSTANCE.getSelectQuery();

    /**
     * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SELECT_QUERY__METHOD = eINSTANCE.getSelectQuery_Method();

    /**
     * The meta object literal for the '<em><b>Base</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__BASE = eINSTANCE.getSelectQuery_Base();

    /**
     * The meta object literal for the '<em><b>Prefixes</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__PREFIXES = eINSTANCE.getSelectQuery_Prefixes();

    /**
     * The meta object literal for the '<em><b>Dataset Clauses</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__DATASET_CLAUSES = eINSTANCE.getSelectQuery_DatasetClauses();

    /**
     * The meta object literal for the '<em><b>Is Distinct</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SELECT_QUERY__IS_DISTINCT = eINSTANCE.getSelectQuery_IsDistinct();

    /**
     * The meta object literal for the '<em><b>Is Reduced</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SELECT_QUERY__IS_REDUCED = eINSTANCE.getSelectQuery_IsReduced();

    /**
     * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__VARIABLES = eINSTANCE.getSelectQuery_Variables();

    /**
     * The meta object literal for the '<em><b>Where Clause</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__WHERE_CLAUSE = eINSTANCE.getSelectQuery_WhereClause();

    /**
     * The meta object literal for the '<em><b>Filterclause</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__FILTERCLAUSE = eINSTANCE.getSelectQuery_Filterclause();

    /**
     * The meta object literal for the '<em><b>Aggregate Clause</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__AGGREGATE_CLAUSE = eINSTANCE.getSelectQuery_AggregateClause();

    /**
     * The meta object literal for the '<em><b>Filesinkclause</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SELECT_QUERY__FILESINKCLAUSE = eINSTANCE.getSelectQuery_Filesinkclause();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl <em>Aggregate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregateImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getAggregate()
     * @generated
     */
    EClass AGGREGATE = eINSTANCE.getAggregate();

    /**
     * The meta object literal for the '<em><b>Aggregations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AGGREGATE__AGGREGATIONS = eINSTANCE.getAggregate_Aggregations();

    /**
     * The meta object literal for the '<em><b>Groupby</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AGGREGATE__GROUPBY = eINSTANCE.getAggregate_Groupby();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupByImpl <em>Group By</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupByImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupBy()
     * @generated
     */
    EClass GROUP_BY = eINSTANCE.getGroupBy();

    /**
     * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GROUP_BY__VARIABLES = eINSTANCE.getGroupBy_Variables();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl <em>Aggregation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.AggregationImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getAggregation()
     * @generated
     */
    EClass AGGREGATION = eINSTANCE.getAggregation();

    /**
     * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute AGGREGATION__FUNCTION = eINSTANCE.getAggregation_Function();

    /**
     * The meta object literal for the '<em><b>Var To Agg</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AGGREGATION__VAR_TO_AGG = eINSTANCE.getAggregation_VarToAgg();

    /**
     * The meta object literal for the '<em><b>Agg Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute AGGREGATION__AGG_NAME = eINSTANCE.getAggregation_AggName();

    /**
     * The meta object literal for the '<em><b>Datatype</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute AGGREGATION__DATATYPE = eINSTANCE.getAggregation_Datatype();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilesinkclauseImpl <em>Filesinkclause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilesinkclauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getFilesinkclause()
     * @generated
     */
    EClass FILESINKCLAUSE = eINSTANCE.getFilesinkclause();

    /**
     * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILESINKCLAUSE__PATH = eINSTANCE.getFilesinkclause_Path();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilterclauseImpl <em>Filterclause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.FilterclauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getFilterclause()
     * @generated
     */
    EClass FILTERCLAUSE = eINSTANCE.getFilterclause();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILTERCLAUSE__LEFT = eINSTANCE.getFilterclause_Left();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FILTERCLAUSE__OPERATOR = eINSTANCE.getFilterclause_Operator();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILTERCLAUSE__RIGHT = eINSTANCE.getFilterclause_Right();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupClauseImpl <em>Group Clause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupClauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupClause()
     * @generated
     */
    EClass GROUP_CLAUSE = eINSTANCE.getGroupClause();

    /**
     * The meta object literal for the '<em><b>Conditions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GROUP_CLAUSE__CONDITIONS = eINSTANCE.getGroupClause_Conditions();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.DatasetClauseImpl <em>Dataset Clause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.DatasetClauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getDatasetClause()
     * @generated
     */
    EClass DATASET_CLAUSE = eINSTANCE.getDatasetClause();

    /**
     * The meta object literal for the '<em><b>Data Set</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATASET_CLAUSE__DATA_SET = eINSTANCE.getDatasetClause_DataSet();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATASET_CLAUSE__NAME = eINSTANCE.getDatasetClause_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATASET_CLAUSE__TYPE = eINSTANCE.getDatasetClause_Type();

    /**
     * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATASET_CLAUSE__SIZE = eINSTANCE.getDatasetClause_Size();

    /**
     * The meta object literal for the '<em><b>Advance</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATASET_CLAUSE__ADVANCE = eINSTANCE.getDatasetClause_Advance();

    /**
     * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATASET_CLAUSE__UNIT = eINSTANCE.getDatasetClause_Unit();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.WhereClauseImpl <em>Where Clause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.WhereClauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getWhereClause()
     * @generated
     */
    EClass WHERE_CLAUSE = eINSTANCE.getWhereClause();

    /**
     * The meta object literal for the '<em><b>Whereclauses</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference WHERE_CLAUSE__WHERECLAUSES = eINSTANCE.getWhereClause_Whereclauses();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl <em>Inner Where Clause</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getInnerWhereClause()
     * @generated
     */
    EClass INNER_WHERE_CLAUSE = eINSTANCE.getInnerWhereClause();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INNER_WHERE_CLAUSE__NAME = eINSTANCE.getInnerWhereClause_Name();

    /**
     * The meta object literal for the '<em><b>Group Graph Pattern</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN = eINSTANCE.getInnerWhereClause_GroupGraphPattern();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupGraphPatternSubImpl <em>Group Graph Pattern Sub</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GroupGraphPatternSubImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGroupGraphPatternSub()
     * @generated
     */
    EClass GROUP_GRAPH_PATTERN_SUB = eINSTANCE.getGroupGraphPatternSub();

    /**
     * The meta object literal for the '<em><b>Graph Patterns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GROUP_GRAPH_PATTERN_SUB__GRAPH_PATTERNS = eINSTANCE.getGroupGraphPatternSub_GraphPatterns();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl <em>Triples Same Subject</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getTriplesSameSubject()
     * @generated
     */
    EClass TRIPLES_SAME_SUBJECT = eINSTANCE.getTriplesSameSubject();

    /**
     * The meta object literal for the '<em><b>Subject</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRIPLES_SAME_SUBJECT__SUBJECT = eINSTANCE.getTriplesSameSubject_Subject();

    /**
     * The meta object literal for the '<em><b>Property List</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRIPLES_SAME_SUBJECT__PROPERTY_LIST = eINSTANCE.getTriplesSameSubject_PropertyList();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.PropertyListImpl <em>Property List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.PropertyListImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getPropertyList()
     * @generated
     */
    EClass PROPERTY_LIST = eINSTANCE.getPropertyList();

    /**
     * The meta object literal for the '<em><b>Property</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY_LIST__PROPERTY = eINSTANCE.getPropertyList_Property();

    /**
     * The meta object literal for the '<em><b>Object</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY_LIST__OBJECT = eINSTANCE.getPropertyList_Object();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.GraphNodeImpl <em>Graph Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.GraphNodeImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getGraphNode()
     * @generated
     */
    EClass GRAPH_NODE = eINSTANCE.getGraphNode();

    /**
     * The meta object literal for the '<em><b>Variable</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GRAPH_NODE__VARIABLE = eINSTANCE.getGraphNode_Variable();

    /**
     * The meta object literal for the '<em><b>Literal</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute GRAPH_NODE__LITERAL = eINSTANCE.getGraphNode_Literal();

    /**
     * The meta object literal for the '<em><b>Iri</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference GRAPH_NODE__IRI = eINSTANCE.getGraphNode_Iri();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl <em>Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getVariable()
     * @generated
     */
    EClass VARIABLE = eINSTANCE.getVariable();

    /**
     * The meta object literal for the '<em><b>Unnamed</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VARIABLE__UNNAMED = eINSTANCE.getVariable_Unnamed();

    /**
     * The meta object literal for the '<em><b>Property</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VARIABLE__PROPERTY = eINSTANCE.getVariable_Property();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.UnNamedVariableImpl <em>Un Named Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.UnNamedVariableImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getUnNamedVariable()
     * @generated
     */
    EClass UN_NAMED_VARIABLE = eINSTANCE.getUnNamedVariable();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute UN_NAMED_VARIABLE__NAME = eINSTANCE.getUnNamedVariable_Name();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl <em>Named Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getNamedVariable()
     * @generated
     */
    EClass NAMED_VARIABLE = eINSTANCE.getNamedVariable();

    /**
     * The meta object literal for the '<em><b>Prefix</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference NAMED_VARIABLE__PREFIX = eINSTANCE.getNamedVariable_Prefix();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NAMED_VARIABLE__NAME = eINSTANCE.getNamedVariable_Name();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.IRIImpl <em>IRI</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.IRIImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getIRI()
     * @generated
     */
    EClass IRI = eINSTANCE.getIRI();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IRI__VALUE = eINSTANCE.getIRI_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TypeTagImpl <em>Type Tag</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.TypeTagImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getTypeTag()
     * @generated
     */
    EClass TYPE_TAG = eINSTANCE.getTypeTag();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_TAG__TYPE = eINSTANCE.getTypeTag_Type();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.LangTagImpl <em>Lang Tag</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.LangTagImpl
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getLangTag()
     * @generated
     */
    EClass LANG_TAG = eINSTANCE.getLangTag();

    /**
     * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LANG_TAG__LANG = eINSTANCE.getLangTag_Lang();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Operator <em>Operator</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.Operator
     * @see de.uniol.inf.is.odysseus.server.streamingsparql.impl.StreamingsparqlPackageImpl#getOperator()
     * @generated
     */
    EEnum OPERATOR = eINSTANCE.getOperator();

  }

} //StreamingsparqlPackage
