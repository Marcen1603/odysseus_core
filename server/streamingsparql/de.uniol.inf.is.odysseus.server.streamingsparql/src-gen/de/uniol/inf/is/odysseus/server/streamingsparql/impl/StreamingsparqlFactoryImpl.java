/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StreamingsparqlFactoryImpl extends EFactoryImpl implements StreamingsparqlFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static StreamingsparqlFactory init()
  {
    try
    {
      StreamingsparqlFactory theStreamingsparqlFactory = (StreamingsparqlFactory)EPackage.Registry.INSTANCE.getEFactory(StreamingsparqlPackage.eNS_URI);
      if (theStreamingsparqlFactory != null)
      {
        return theStreamingsparqlFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new StreamingsparqlFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamingsparqlFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case StreamingsparqlPackage.SPARQL_QUERY: return createSPARQLQuery();
      case StreamingsparqlPackage.PREFIX: return createPrefix();
      case StreamingsparqlPackage.BASE: return createBase();
      case StreamingsparqlPackage.SELECT_QUERY: return createSelectQuery();
      case StreamingsparqlPackage.AGGREGATE: return createAggregate();
      case StreamingsparqlPackage.GROUP_BY: return createGroupBy();
      case StreamingsparqlPackage.AGGREGATION: return createAggregation();
      case StreamingsparqlPackage.FILESINKCLAUSE: return createFilesinkclause();
      case StreamingsparqlPackage.FILTERCLAUSE: return createFilterclause();
      case StreamingsparqlPackage.GROUP_CLAUSE: return createGroupClause();
      case StreamingsparqlPackage.DATASET_CLAUSE: return createDatasetClause();
      case StreamingsparqlPackage.WHERE_CLAUSE: return createWhereClause();
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE: return createInnerWhereClause();
      case StreamingsparqlPackage.GROUP_GRAPH_PATTERN_SUB: return createGroupGraphPatternSub();
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT: return createTriplesSameSubject();
      case StreamingsparqlPackage.PROPERTY_LIST: return createPropertyList();
      case StreamingsparqlPackage.GRAPH_NODE: return createGraphNode();
      case StreamingsparqlPackage.VARIABLE: return createVariable();
      case StreamingsparqlPackage.UN_NAMED_VARIABLE: return createUnNamedVariable();
      case StreamingsparqlPackage.NAMED_VARIABLE: return createNamedVariable();
      case StreamingsparqlPackage.IRI: return createIRI();
      case StreamingsparqlPackage.TYPE_TAG: return createTypeTag();
      case StreamingsparqlPackage.LANG_TAG: return createLangTag();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case StreamingsparqlPackage.OPERATOR:
        return createOperatorFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case StreamingsparqlPackage.OPERATOR:
        return convertOperatorToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SPARQLQuery createSPARQLQuery()
  {
    SPARQLQueryImpl sparqlQuery = new SPARQLQueryImpl();
    return sparqlQuery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Prefix createPrefix()
  {
    PrefixImpl prefix = new PrefixImpl();
    return prefix;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Base createBase()
  {
    BaseImpl base = new BaseImpl();
    return base;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SelectQuery createSelectQuery()
  {
    SelectQueryImpl selectQuery = new SelectQueryImpl();
    return selectQuery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Aggregate createAggregate()
  {
    AggregateImpl aggregate = new AggregateImpl();
    return aggregate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupBy createGroupBy()
  {
    GroupByImpl groupBy = new GroupByImpl();
    return groupBy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Aggregation createAggregation()
  {
    AggregationImpl aggregation = new AggregationImpl();
    return aggregation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Filesinkclause createFilesinkclause()
  {
    FilesinkclauseImpl filesinkclause = new FilesinkclauseImpl();
    return filesinkclause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Filterclause createFilterclause()
  {
    FilterclauseImpl filterclause = new FilterclauseImpl();
    return filterclause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupClause createGroupClause()
  {
    GroupClauseImpl groupClause = new GroupClauseImpl();
    return groupClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatasetClause createDatasetClause()
  {
    DatasetClauseImpl datasetClause = new DatasetClauseImpl();
    return datasetClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WhereClause createWhereClause()
  {
    WhereClauseImpl whereClause = new WhereClauseImpl();
    return whereClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InnerWhereClause createInnerWhereClause()
  {
    InnerWhereClauseImpl innerWhereClause = new InnerWhereClauseImpl();
    return innerWhereClause;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupGraphPatternSub createGroupGraphPatternSub()
  {
    GroupGraphPatternSubImpl groupGraphPatternSub = new GroupGraphPatternSubImpl();
    return groupGraphPatternSub;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TriplesSameSubject createTriplesSameSubject()
  {
    TriplesSameSubjectImpl triplesSameSubject = new TriplesSameSubjectImpl();
    return triplesSameSubject;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PropertyList createPropertyList()
  {
    PropertyListImpl propertyList = new PropertyListImpl();
    return propertyList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GraphNode createGraphNode()
  {
    GraphNodeImpl graphNode = new GraphNodeImpl();
    return graphNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable createVariable()
  {
    VariableImpl variable = new VariableImpl();
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UnNamedVariable createUnNamedVariable()
  {
    UnNamedVariableImpl unNamedVariable = new UnNamedVariableImpl();
    return unNamedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NamedVariable createNamedVariable()
  {
    NamedVariableImpl namedVariable = new NamedVariableImpl();
    return namedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IRI createIRI()
  {
    IRIImpl iri = new IRIImpl();
    return iri;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TypeTag createTypeTag()
  {
    TypeTagImpl typeTag = new TypeTagImpl();
    return typeTag;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LangTag createLangTag()
  {
    LangTagImpl langTag = new LangTagImpl();
    return langTag;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operator createOperatorFromString(EDataType eDataType, String initialValue)
  {
    Operator result = Operator.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertOperatorToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamingsparqlPackage getStreamingsparqlPackage()
  {
    return (StreamingsparqlPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static StreamingsparqlPackage getPackage()
  {
    return StreamingsparqlPackage.eINSTANCE;
  }

} //StreamingsparqlFactoryImpl
