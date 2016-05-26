/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation;
import de.uniol.inf.is.odysseus.server.streamingsparql.Base;
import de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub;
import de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.LangTag;
import de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Operator;
import de.uniol.inf.is.odysseus.server.streamingsparql.Prefix;
import de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList;
import de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlFactory;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject;
import de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag;
import de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;
import de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StreamingsparqlPackageImpl extends EPackageImpl implements StreamingsparqlPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sparqlQueryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass prefixEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass baseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass selectQueryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass aggregateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass groupByEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass aggregationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass filesinkclauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass filterclauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass groupClauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass datasetClauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass whereClauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass innerWhereClauseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass groupGraphPatternSubEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass triplesSameSubjectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass propertyListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass graphNodeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass unNamedVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass namedVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iriEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass typeTagEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass langTagEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum operatorEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private StreamingsparqlPackageImpl()
  {
    super(eNS_URI, StreamingsparqlFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link StreamingsparqlPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static StreamingsparqlPackage init()
  {
    if (isInited) return (StreamingsparqlPackage)EPackage.Registry.INSTANCE.getEPackage(StreamingsparqlPackage.eNS_URI);

    // Obtain or create and register package
    StreamingsparqlPackageImpl theStreamingsparqlPackage = (StreamingsparqlPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof StreamingsparqlPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new StreamingsparqlPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theStreamingsparqlPackage.createPackageContents();

    // Initialize created meta-data
    theStreamingsparqlPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theStreamingsparqlPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(StreamingsparqlPackage.eNS_URI, theStreamingsparqlPackage);
    return theStreamingsparqlPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSPARQLQuery()
  {
    return sparqlQueryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPrefix()
  {
    return prefixEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPrefix_Name()
  {
    return (EAttribute)prefixEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPrefix_Iref()
  {
    return (EAttribute)prefixEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBase()
  {
    return baseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBase_Iref()
  {
    return (EReference)baseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSelectQuery()
  {
    return selectQueryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelectQuery_Method()
  {
    return (EAttribute)selectQueryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_Base()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_Prefixes()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_DatasetClauses()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelectQuery_IsDistinct()
  {
    return (EAttribute)selectQueryEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelectQuery_IsReduced()
  {
    return (EAttribute)selectQueryEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_Variables()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_WhereClause()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_Filterclause()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_AggregateClause()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectQuery_Filesinkclause()
  {
    return (EReference)selectQueryEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAggregate()
  {
    return aggregateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getAggregate_Aggregations()
  {
    return (EReference)aggregateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getAggregate_Groupby()
  {
    return (EReference)aggregateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGroupBy()
  {
    return groupByEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGroupBy_Variables()
  {
    return (EReference)groupByEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAggregation()
  {
    return aggregationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAggregation_Function()
  {
    return (EAttribute)aggregationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getAggregation_VarToAgg()
  {
    return (EReference)aggregationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAggregation_AggName()
  {
    return (EAttribute)aggregationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAggregation_Datatype()
  {
    return (EAttribute)aggregationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFilesinkclause()
  {
    return filesinkclauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFilesinkclause_Path()
  {
    return (EAttribute)filesinkclauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFilterclause()
  {
    return filterclauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFilterclause_Left()
  {
    return (EReference)filterclauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFilterclause_Operator()
  {
    return (EAttribute)filterclauseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFilterclause_Right()
  {
    return (EReference)filterclauseEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGroupClause()
  {
    return groupClauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGroupClause_Conditions()
  {
    return (EReference)groupClauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDatasetClause()
  {
    return datasetClauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDatasetClause_DataSet()
  {
    return (EReference)datasetClauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatasetClause_Name()
  {
    return (EAttribute)datasetClauseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatasetClause_Type()
  {
    return (EAttribute)datasetClauseEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatasetClause_Size()
  {
    return (EAttribute)datasetClauseEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatasetClause_Advance()
  {
    return (EAttribute)datasetClauseEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatasetClause_Unit()
  {
    return (EAttribute)datasetClauseEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWhereClause()
  {
    return whereClauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWhereClause_Whereclauses()
  {
    return (EReference)whereClauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInnerWhereClause()
  {
    return innerWhereClauseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInnerWhereClause_Name()
  {
    return (EReference)innerWhereClauseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInnerWhereClause_GroupGraphPattern()
  {
    return (EReference)innerWhereClauseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGroupGraphPatternSub()
  {
    return groupGraphPatternSubEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGroupGraphPatternSub_GraphPatterns()
  {
    return (EReference)groupGraphPatternSubEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTriplesSameSubject()
  {
    return triplesSameSubjectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTriplesSameSubject_Subject()
  {
    return (EReference)triplesSameSubjectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTriplesSameSubject_PropertyList()
  {
    return (EReference)triplesSameSubjectEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPropertyList()
  {
    return propertyListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPropertyList_Property()
  {
    return (EReference)propertyListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPropertyList_Object()
  {
    return (EReference)propertyListEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGraphNode()
  {
    return graphNodeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGraphNode_Variable()
  {
    return (EReference)graphNodeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGraphNode_Literal()
  {
    return (EAttribute)graphNodeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGraphNode_Iri()
  {
    return (EReference)graphNodeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVariable()
  {
    return variableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getVariable_Unnamed()
  {
    return (EReference)variableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getVariable_Property()
  {
    return (EReference)variableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getUnNamedVariable()
  {
    return unNamedVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUnNamedVariable_Name()
  {
    return (EAttribute)unNamedVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNamedVariable()
  {
    return namedVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getNamedVariable_Prefix()
  {
    return (EReference)namedVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getNamedVariable_Name()
  {
    return (EAttribute)namedVariableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIRI()
  {
    return iriEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIRI_Value()
  {
    return (EAttribute)iriEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTypeTag()
  {
    return typeTagEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTypeTag_Type()
  {
    return (EReference)typeTagEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLangTag()
  {
    return langTagEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLangTag_Lang()
  {
    return (EAttribute)langTagEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getOperator()
  {
    return operatorEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamingsparqlFactory getStreamingsparqlFactory()
  {
    return (StreamingsparqlFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    sparqlQueryEClass = createEClass(SPARQL_QUERY);

    prefixEClass = createEClass(PREFIX);
    createEAttribute(prefixEClass, PREFIX__NAME);
    createEAttribute(prefixEClass, PREFIX__IREF);

    baseEClass = createEClass(BASE);
    createEReference(baseEClass, BASE__IREF);

    selectQueryEClass = createEClass(SELECT_QUERY);
    createEAttribute(selectQueryEClass, SELECT_QUERY__METHOD);
    createEReference(selectQueryEClass, SELECT_QUERY__BASE);
    createEReference(selectQueryEClass, SELECT_QUERY__PREFIXES);
    createEReference(selectQueryEClass, SELECT_QUERY__DATASET_CLAUSES);
    createEAttribute(selectQueryEClass, SELECT_QUERY__IS_DISTINCT);
    createEAttribute(selectQueryEClass, SELECT_QUERY__IS_REDUCED);
    createEReference(selectQueryEClass, SELECT_QUERY__VARIABLES);
    createEReference(selectQueryEClass, SELECT_QUERY__WHERE_CLAUSE);
    createEReference(selectQueryEClass, SELECT_QUERY__FILTERCLAUSE);
    createEReference(selectQueryEClass, SELECT_QUERY__AGGREGATE_CLAUSE);
    createEReference(selectQueryEClass, SELECT_QUERY__FILESINKCLAUSE);

    aggregateEClass = createEClass(AGGREGATE);
    createEReference(aggregateEClass, AGGREGATE__AGGREGATIONS);
    createEReference(aggregateEClass, AGGREGATE__GROUPBY);

    groupByEClass = createEClass(GROUP_BY);
    createEReference(groupByEClass, GROUP_BY__VARIABLES);

    aggregationEClass = createEClass(AGGREGATION);
    createEAttribute(aggregationEClass, AGGREGATION__FUNCTION);
    createEReference(aggregationEClass, AGGREGATION__VAR_TO_AGG);
    createEAttribute(aggregationEClass, AGGREGATION__AGG_NAME);
    createEAttribute(aggregationEClass, AGGREGATION__DATATYPE);

    filesinkclauseEClass = createEClass(FILESINKCLAUSE);
    createEAttribute(filesinkclauseEClass, FILESINKCLAUSE__PATH);

    filterclauseEClass = createEClass(FILTERCLAUSE);
    createEReference(filterclauseEClass, FILTERCLAUSE__LEFT);
    createEAttribute(filterclauseEClass, FILTERCLAUSE__OPERATOR);
    createEReference(filterclauseEClass, FILTERCLAUSE__RIGHT);

    groupClauseEClass = createEClass(GROUP_CLAUSE);
    createEReference(groupClauseEClass, GROUP_CLAUSE__CONDITIONS);

    datasetClauseEClass = createEClass(DATASET_CLAUSE);
    createEReference(datasetClauseEClass, DATASET_CLAUSE__DATA_SET);
    createEAttribute(datasetClauseEClass, DATASET_CLAUSE__NAME);
    createEAttribute(datasetClauseEClass, DATASET_CLAUSE__TYPE);
    createEAttribute(datasetClauseEClass, DATASET_CLAUSE__SIZE);
    createEAttribute(datasetClauseEClass, DATASET_CLAUSE__ADVANCE);
    createEAttribute(datasetClauseEClass, DATASET_CLAUSE__UNIT);

    whereClauseEClass = createEClass(WHERE_CLAUSE);
    createEReference(whereClauseEClass, WHERE_CLAUSE__WHERECLAUSES);

    innerWhereClauseEClass = createEClass(INNER_WHERE_CLAUSE);
    createEReference(innerWhereClauseEClass, INNER_WHERE_CLAUSE__NAME);
    createEReference(innerWhereClauseEClass, INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN);

    groupGraphPatternSubEClass = createEClass(GROUP_GRAPH_PATTERN_SUB);
    createEReference(groupGraphPatternSubEClass, GROUP_GRAPH_PATTERN_SUB__GRAPH_PATTERNS);

    triplesSameSubjectEClass = createEClass(TRIPLES_SAME_SUBJECT);
    createEReference(triplesSameSubjectEClass, TRIPLES_SAME_SUBJECT__SUBJECT);
    createEReference(triplesSameSubjectEClass, TRIPLES_SAME_SUBJECT__PROPERTY_LIST);

    propertyListEClass = createEClass(PROPERTY_LIST);
    createEReference(propertyListEClass, PROPERTY_LIST__PROPERTY);
    createEReference(propertyListEClass, PROPERTY_LIST__OBJECT);

    graphNodeEClass = createEClass(GRAPH_NODE);
    createEReference(graphNodeEClass, GRAPH_NODE__VARIABLE);
    createEAttribute(graphNodeEClass, GRAPH_NODE__LITERAL);
    createEReference(graphNodeEClass, GRAPH_NODE__IRI);

    variableEClass = createEClass(VARIABLE);
    createEReference(variableEClass, VARIABLE__UNNAMED);
    createEReference(variableEClass, VARIABLE__PROPERTY);

    unNamedVariableEClass = createEClass(UN_NAMED_VARIABLE);
    createEAttribute(unNamedVariableEClass, UN_NAMED_VARIABLE__NAME);

    namedVariableEClass = createEClass(NAMED_VARIABLE);
    createEReference(namedVariableEClass, NAMED_VARIABLE__PREFIX);
    createEAttribute(namedVariableEClass, NAMED_VARIABLE__NAME);

    iriEClass = createEClass(IRI);
    createEAttribute(iriEClass, IRI__VALUE);

    typeTagEClass = createEClass(TYPE_TAG);
    createEReference(typeTagEClass, TYPE_TAG__TYPE);

    langTagEClass = createEClass(LANG_TAG);
    createEAttribute(langTagEClass, LANG_TAG__LANG);

    // Create enums
    operatorEEnum = createEEnum(OPERATOR);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    selectQueryEClass.getESuperTypes().add(this.getSPARQLQuery());

    // Initialize classes and features; add operations and parameters
    initEClass(sparqlQueryEClass, SPARQLQuery.class, "SPARQLQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(prefixEClass, Prefix.class, "Prefix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPrefix_Name(), ecorePackage.getEString(), "name", null, 0, 1, Prefix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPrefix_Iref(), ecorePackage.getEString(), "iref", null, 0, 1, Prefix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(baseEClass, Base.class, "Base", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getBase_Iref(), this.getIRI(), null, "iref", null, 0, 1, Base.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(selectQueryEClass, SelectQuery.class, "SelectQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSelectQuery_Method(), ecorePackage.getEString(), "method", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_Base(), this.getBase(), null, "base", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_Prefixes(), this.getPrefix(), null, "prefixes", null, 0, -1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_DatasetClauses(), this.getDatasetClause(), null, "datasetClauses", null, 0, -1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSelectQuery_IsDistinct(), ecorePackage.getEBoolean(), "isDistinct", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSelectQuery_IsReduced(), ecorePackage.getEBoolean(), "isReduced", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_Variables(), this.getVariable(), null, "variables", null, 0, -1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_WhereClause(), this.getWhereClause(), null, "whereClause", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_Filterclause(), this.getFilterclause(), null, "filterclause", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_AggregateClause(), this.getAggregate(), null, "aggregateClause", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectQuery_Filesinkclause(), this.getFilesinkclause(), null, "filesinkclause", null, 0, 1, SelectQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(aggregateEClass, Aggregate.class, "Aggregate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getAggregate_Aggregations(), this.getAggregation(), null, "aggregations", null, 0, -1, Aggregate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAggregate_Groupby(), this.getGroupBy(), null, "groupby", null, 0, 1, Aggregate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(groupByEClass, GroupBy.class, "GroupBy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGroupBy_Variables(), this.getVariable(), null, "variables", null, 0, -1, GroupBy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(aggregationEClass, Aggregation.class, "Aggregation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAggregation_Function(), ecorePackage.getEString(), "function", null, 0, 1, Aggregation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getAggregation_VarToAgg(), this.getVariable(), null, "varToAgg", null, 0, 1, Aggregation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAggregation_AggName(), ecorePackage.getEString(), "aggName", null, 0, 1, Aggregation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAggregation_Datatype(), ecorePackage.getEString(), "datatype", null, 0, 1, Aggregation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(filesinkclauseEClass, Filesinkclause.class, "Filesinkclause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFilesinkclause_Path(), ecorePackage.getEString(), "path", null, 0, 1, Filesinkclause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(filterclauseEClass, Filterclause.class, "Filterclause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getFilterclause_Left(), this.getVariable(), null, "left", null, 0, 1, Filterclause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFilterclause_Operator(), this.getOperator(), "operator", null, 0, 1, Filterclause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFilterclause_Right(), this.getVariable(), null, "right", null, 0, 1, Filterclause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(groupClauseEClass, GroupClause.class, "GroupClause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGroupClause_Conditions(), this.getVariable(), null, "conditions", null, 0, -1, GroupClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(datasetClauseEClass, DatasetClause.class, "DatasetClause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDatasetClause_DataSet(), this.getIRI(), null, "dataSet", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatasetClause_Name(), ecorePackage.getEString(), "name", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatasetClause_Type(), ecorePackage.getEString(), "type", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatasetClause_Size(), ecorePackage.getEInt(), "size", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatasetClause_Advance(), ecorePackage.getEInt(), "advance", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatasetClause_Unit(), ecorePackage.getEString(), "unit", null, 0, 1, DatasetClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(whereClauseEClass, WhereClause.class, "WhereClause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getWhereClause_Whereclauses(), this.getInnerWhereClause(), null, "whereclauses", null, 0, -1, WhereClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(innerWhereClauseEClass, InnerWhereClause.class, "InnerWhereClause", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInnerWhereClause_Name(), this.getDatasetClause(), null, "name", null, 0, 1, InnerWhereClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInnerWhereClause_GroupGraphPattern(), this.getGroupGraphPatternSub(), null, "groupGraphPattern", null, 0, 1, InnerWhereClause.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(groupGraphPatternSubEClass, GroupGraphPatternSub.class, "GroupGraphPatternSub", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGroupGraphPatternSub_GraphPatterns(), this.getTriplesSameSubject(), null, "graphPatterns", null, 0, -1, GroupGraphPatternSub.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(triplesSameSubjectEClass, TriplesSameSubject.class, "TriplesSameSubject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTriplesSameSubject_Subject(), this.getGraphNode(), null, "subject", null, 0, 1, TriplesSameSubject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTriplesSameSubject_PropertyList(), this.getPropertyList(), null, "propertyList", null, 0, -1, TriplesSameSubject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(propertyListEClass, PropertyList.class, "PropertyList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPropertyList_Property(), this.getGraphNode(), null, "property", null, 0, 1, PropertyList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getPropertyList_Object(), this.getGraphNode(), null, "object", null, 0, 1, PropertyList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(graphNodeEClass, GraphNode.class, "GraphNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGraphNode_Variable(), this.getVariable(), null, "variable", null, 0, 1, GraphNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGraphNode_Literal(), ecorePackage.getEString(), "literal", null, 0, 1, GraphNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGraphNode_Iri(), this.getIRI(), null, "iri", null, 0, 1, GraphNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableEClass, Variable.class, "Variable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getVariable_Unnamed(), this.getUnNamedVariable(), null, "unnamed", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getVariable_Property(), this.getNamedVariable(), null, "property", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(unNamedVariableEClass, UnNamedVariable.class, "UnNamedVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getUnNamedVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, UnNamedVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(namedVariableEClass, NamedVariable.class, "NamedVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getNamedVariable_Prefix(), this.getPrefix(), null, "prefix", null, 0, 1, NamedVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getNamedVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iriEClass, de.uniol.inf.is.odysseus.server.streamingsparql.IRI.class, "IRI", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIRI_Value(), ecorePackage.getEString(), "value", null, 0, 1, de.uniol.inf.is.odysseus.server.streamingsparql.IRI.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(typeTagEClass, TypeTag.class, "TypeTag", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTypeTag_Type(), this.getGraphNode(), null, "type", null, 0, 1, TypeTag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(langTagEClass, LangTag.class, "LangTag", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLangTag_Lang(), ecorePackage.getEString(), "lang", null, 0, 1, LangTag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(operatorEEnum, Operator.class, "Operator");
    addEEnumLiteral(operatorEEnum, Operator.LESS_THEN);
    addEEnumLiteral(operatorEEnum, Operator.GREATER_THEN);
    addEEnumLiteral(operatorEEnum, Operator.LESS_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.GREATER_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.NOT_EQUAL);
    addEEnumLiteral(operatorEEnum, Operator.SUM);
    addEEnumLiteral(operatorEEnum, Operator.DIV);
    addEEnumLiteral(operatorEEnum, Operator.SUB);
    addEEnumLiteral(operatorEEnum, Operator.MULTIPLICITY);

    // Create resource
    createResource(eNS_URI);
  }

} //StreamingsparqlPackageImpl
