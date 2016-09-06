/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Factory
 * @model kind="package"
 * @generated
 */
public interface Pql2Package extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "pql2";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/Pql2";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "pql2";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  Pql2Package eINSTANCE = de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.PQLModelImpl <em>PQL Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.PQLModelImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getPQLModel()
   * @generated
   */
  int PQL_MODEL = 0;

  /**
   * The feature id for the '<em><b>Queries</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PQL_MODEL__QUERIES = 0;

  /**
   * The number of structural features of the '<em>PQL Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PQL_MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.QueryImpl <em>Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.QueryImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getQuery()
   * @generated
   */
  int QUERY = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__NAME = 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY__OP = 1;

  /**
   * The number of structural features of the '<em>Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERY_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.TemporaryStreamImpl <em>Temporary Stream</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.TemporaryStreamImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getTemporaryStream()
   * @generated
   */
  int TEMPORARY_STREAM = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEMPORARY_STREAM__NAME = QUERY__NAME;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEMPORARY_STREAM__OP = QUERY__OP;

  /**
   * The number of structural features of the '<em>Temporary Stream</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEMPORARY_STREAM_FEATURE_COUNT = QUERY_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ViewImpl <em>View</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ViewImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getView()
   * @generated
   */
  int VIEW = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VIEW__NAME = QUERY__NAME;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VIEW__OP = QUERY__OP;

  /**
   * The number of structural features of the '<em>View</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VIEW_FEATURE_COUNT = QUERY_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.SharedStreamImpl <em>Shared Stream</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.SharedStreamImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getSharedStream()
   * @generated
   */
  int SHARED_STREAM = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SHARED_STREAM__NAME = QUERY__NAME;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SHARED_STREAM__OP = QUERY__OP;

  /**
   * The number of structural features of the '<em>Shared Stream</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SHARED_STREAM_FEATURE_COUNT = QUERY_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl <em>Operator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperator()
   * @generated
   */
  int OPERATOR = 5;

  /**
   * The feature id for the '<em><b>Operator Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR__OPERATOR_TYPE = 0;

  /**
   * The feature id for the '<em><b>Operators</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR__OPERATORS = 1;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR__PARAMETERS = 2;

  /**
   * The number of structural features of the '<em>Operator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorListImpl <em>Operator List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorListImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperatorList()
   * @generated
   */
  int OPERATOR_LIST = 6;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_LIST__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>Operator List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl <em>Operator Or Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperatorOrQuery()
   * @generated
   */
  int OPERATOR_OR_QUERY = 7;

  /**
   * The feature id for the '<em><b>Output Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_OR_QUERY__OUTPUT_PORT = 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_OR_QUERY__OP = 1;

  /**
   * The feature id for the '<em><b>Query</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_OR_QUERY__QUERY = 2;

  /**
   * The number of structural features of the '<em>Operator Or Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATOR_OR_QUERY_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterListImpl <em>Parameter List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterListImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameterList()
   * @generated
   */
  int PARAMETER_LIST = 8;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_LIST__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>Parameter List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterImpl <em>Parameter</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameter()
   * @generated
   */
  int PARAMETER = 9;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__NAME = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__VALUE = 1;

  /**
   * The number of structural features of the '<em>Parameter</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterValueImpl <em>Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameterValue()
   * @generated
   */
  int PARAMETER_VALUE = 10;

  /**
   * The number of structural features of the '<em>Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_VALUE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.LongParameterValueImpl <em>Long Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.LongParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getLongParameterValue()
   * @generated
   */
  int LONG_PARAMETER_VALUE = 11;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_PARAMETER_VALUE__VALUE = PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Long Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_PARAMETER_VALUE_FEATURE_COUNT = PARAMETER_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.DoubleParameterValueImpl <em>Double Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.DoubleParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getDoubleParameterValue()
   * @generated
   */
  int DOUBLE_PARAMETER_VALUE = 12;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_PARAMETER_VALUE__VALUE = PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Double Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_PARAMETER_VALUE_FEATURE_COUNT = PARAMETER_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.StringParameterValueImpl <em>String Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.StringParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getStringParameterValue()
   * @generated
   */
  int STRING_PARAMETER_VALUE = 13;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_PARAMETER_VALUE__VALUE = PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>String Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_PARAMETER_VALUE_FEATURE_COUNT = PARAMETER_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ListParameterValueImpl <em>List Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ListParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getListParameterValue()
   * @generated
   */
  int LIST_PARAMETER_VALUE = 14;

  /**
   * The number of structural features of the '<em>List Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIST_PARAMETER_VALUE_FEATURE_COUNT = PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapParameterValueImpl <em>Map Parameter Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.MapParameterValueImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMapParameterValue()
   * @generated
   */
  int MAP_PARAMETER_VALUE = 15;

  /**
   * The number of structural features of the '<em>Map Parameter Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP_PARAMETER_VALUE_FEATURE_COUNT = PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapEntryImpl <em>Map Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.MapEntryImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMapEntry()
   * @generated
   */
  int MAP_ENTRY = 16;

  /**
   * The feature id for the '<em><b>Key</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP_ENTRY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP_ENTRY__VALUE = 1;

  /**
   * The number of structural features of the '<em>Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP_ENTRY_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ListImpl <em>List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.ListImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getList()
   * @generated
   */
  int LIST = 17;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIST__ELEMENTS = LIST_PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LIST_FEATURE_COUNT = LIST_PARAMETER_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapImpl <em>Map</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.pql2.impl.MapImpl
   * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMap()
   * @generated
   */
  int MAP = 18;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP__ELEMENTS = MAP_PARAMETER_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Map</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAP_FEATURE_COUNT = MAP_PARAMETER_VALUE_FEATURE_COUNT + 1;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.PQLModel <em>PQL Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>PQL Model</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.PQLModel
   * @generated
   */
  EClass getPQLModel();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.pql2.PQLModel#getQueries <em>Queries</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Queries</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.PQLModel#getQueries()
   * @see #getPQLModel()
   * @generated
   */
  EReference getPQLModel_Queries();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.Query <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Query</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Query
   * @generated
   */
  EClass getQuery();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.Query#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Query#getName()
   * @see #getQuery()
   * @generated
   */
  EAttribute getQuery_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.Query#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Query#getOp()
   * @see #getQuery()
   * @generated
   */
  EReference getQuery_Op();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.TemporaryStream <em>Temporary Stream</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Temporary Stream</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.TemporaryStream
   * @generated
   */
  EClass getTemporaryStream();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.View <em>View</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>View</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.View
   * @generated
   */
  EClass getView();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.SharedStream <em>Shared Stream</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Shared Stream</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.SharedStream
   * @generated
   */
  EClass getSharedStream();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Operator
   * @generated
   */
  EClass getOperator();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperatorType <em>Operator Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator Type</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Operator#getOperatorType()
   * @see #getOperator()
   * @generated
   */
  EAttribute getOperator_OperatorType();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.Operator#getOperators <em>Operators</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operators</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Operator#getOperators()
   * @see #getOperator()
   * @generated
   */
  EReference getOperator_Operators();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.Operator#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Parameters</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Operator#getParameters()
   * @see #getOperator()
   * @generated
   */
  EReference getOperator_Parameters();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.OperatorList <em>Operator List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operator List</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorList
   * @generated
   */
  EClass getOperatorList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.pql2.OperatorList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorList#getElements()
   * @see #getOperatorList()
   * @generated
   */
  EReference getOperatorList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery <em>Operator Or Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operator Or Query</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorOrQuery
   * @generated
   */
  EClass getOperatorOrQuery();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOutputPort <em>Output Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Output Port</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOutputPort()
   * @see #getOperatorOrQuery()
   * @generated
   */
  EAttribute getOperatorOrQuery_OutputPort();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getOp()
   * @see #getOperatorOrQuery()
   * @generated
   */
  EReference getOperatorOrQuery_Op();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getQuery <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Query</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorOrQuery#getQuery()
   * @see #getOperatorOrQuery()
   * @generated
   */
  EReference getOperatorOrQuery_Query();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.ParameterList <em>Parameter List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter List</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.ParameterList
   * @generated
   */
  EClass getParameterList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.pql2.ParameterList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.ParameterList#getElements()
   * @see #getParameterList()
   * @generated
   */
  EReference getParameterList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.Parameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Parameter
   * @generated
   */
  EClass getParameter();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.Parameter#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Parameter#getName()
   * @see #getParameter()
   * @generated
   */
  EAttribute getParameter_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.Parameter#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Parameter#getValue()
   * @see #getParameter()
   * @generated
   */
  EReference getParameter_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.ParameterValue <em>Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.ParameterValue
   * @generated
   */
  EClass getParameterValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.LongParameterValue <em>Long Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Long Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.LongParameterValue
   * @generated
   */
  EClass getLongParameterValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.LongParameterValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.LongParameterValue#getValue()
   * @see #getLongParameterValue()
   * @generated
   */
  EAttribute getLongParameterValue_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.DoubleParameterValue <em>Double Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Double Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.DoubleParameterValue
   * @generated
   */
  EClass getDoubleParameterValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.DoubleParameterValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.DoubleParameterValue#getValue()
   * @see #getDoubleParameterValue()
   * @generated
   */
  EAttribute getDoubleParameterValue_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.StringParameterValue <em>String Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.StringParameterValue
   * @generated
   */
  EClass getStringParameterValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.pql2.StringParameterValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.StringParameterValue#getValue()
   * @see #getStringParameterValue()
   * @generated
   */
  EAttribute getStringParameterValue_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.ListParameterValue <em>List Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>List Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.ListParameterValue
   * @generated
   */
  EClass getListParameterValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.MapParameterValue <em>Map Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Map Parameter Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.MapParameterValue
   * @generated
   */
  EClass getMapParameterValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.MapEntry <em>Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Map Entry</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.MapEntry
   * @generated
   */
  EClass getMapEntry();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Key</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.MapEntry#getKey()
   * @see #getMapEntry()
   * @generated
   */
  EReference getMapEntry_Key();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.pql2.MapEntry#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.MapEntry#getValue()
   * @see #getMapEntry()
   * @generated
   */
  EReference getMapEntry_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.List <em>List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>List</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.List
   * @generated
   */
  EClass getList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.pql2.List#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.List#getElements()
   * @see #getList()
   * @generated
   */
  EReference getList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.pql2.Map <em>Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Map</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Map
   * @generated
   */
  EClass getMap();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.pql2.Map#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.pql2.Map#getElements()
   * @see #getMap()
   * @generated
   */
  EReference getMap_Elements();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  Pql2Factory getPql2Factory();

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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.PQLModelImpl <em>PQL Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.PQLModelImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getPQLModel()
     * @generated
     */
    EClass PQL_MODEL = eINSTANCE.getPQLModel();

    /**
     * The meta object literal for the '<em><b>Queries</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PQL_MODEL__QUERIES = eINSTANCE.getPQLModel_Queries();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.QueryImpl <em>Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.QueryImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getQuery()
     * @generated
     */
    EClass QUERY = eINSTANCE.getQuery();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERY__NAME = eINSTANCE.getQuery_Name();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERY__OP = eINSTANCE.getQuery_Op();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.TemporaryStreamImpl <em>Temporary Stream</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.TemporaryStreamImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getTemporaryStream()
     * @generated
     */
    EClass TEMPORARY_STREAM = eINSTANCE.getTemporaryStream();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ViewImpl <em>View</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ViewImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getView()
     * @generated
     */
    EClass VIEW = eINSTANCE.getView();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.SharedStreamImpl <em>Shared Stream</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.SharedStreamImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getSharedStream()
     * @generated
     */
    EClass SHARED_STREAM = eINSTANCE.getSharedStream();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl <em>Operator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperator()
     * @generated
     */
    EClass OPERATOR = eINSTANCE.getOperator();

    /**
     * The meta object literal for the '<em><b>Operator Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATOR__OPERATOR_TYPE = eINSTANCE.getOperator_OperatorType();

    /**
     * The meta object literal for the '<em><b>Operators</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATOR__OPERATORS = eINSTANCE.getOperator_Operators();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATOR__PARAMETERS = eINSTANCE.getOperator_Parameters();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorListImpl <em>Operator List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorListImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperatorList()
     * @generated
     */
    EClass OPERATOR_LIST = eINSTANCE.getOperatorList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATOR_LIST__ELEMENTS = eINSTANCE.getOperatorList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl <em>Operator Or Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.OperatorOrQueryImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getOperatorOrQuery()
     * @generated
     */
    EClass OPERATOR_OR_QUERY = eINSTANCE.getOperatorOrQuery();

    /**
     * The meta object literal for the '<em><b>Output Port</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATOR_OR_QUERY__OUTPUT_PORT = eINSTANCE.getOperatorOrQuery_OutputPort();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATOR_OR_QUERY__OP = eINSTANCE.getOperatorOrQuery_Op();

    /**
     * The meta object literal for the '<em><b>Query</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATOR_OR_QUERY__QUERY = eINSTANCE.getOperatorOrQuery_Query();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterListImpl <em>Parameter List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterListImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameterList()
     * @generated
     */
    EClass PARAMETER_LIST = eINSTANCE.getParameterList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAMETER_LIST__ELEMENTS = eINSTANCE.getParameterList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterImpl <em>Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameter()
     * @generated
     */
    EClass PARAMETER = eINSTANCE.getParameter();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAMETER__VALUE = eINSTANCE.getParameter_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ParameterValueImpl <em>Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getParameterValue()
     * @generated
     */
    EClass PARAMETER_VALUE = eINSTANCE.getParameterValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.LongParameterValueImpl <em>Long Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.LongParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getLongParameterValue()
     * @generated
     */
    EClass LONG_PARAMETER_VALUE = eINSTANCE.getLongParameterValue();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LONG_PARAMETER_VALUE__VALUE = eINSTANCE.getLongParameterValue_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.DoubleParameterValueImpl <em>Double Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.DoubleParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getDoubleParameterValue()
     * @generated
     */
    EClass DOUBLE_PARAMETER_VALUE = eINSTANCE.getDoubleParameterValue();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_PARAMETER_VALUE__VALUE = eINSTANCE.getDoubleParameterValue_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.StringParameterValueImpl <em>String Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.StringParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getStringParameterValue()
     * @generated
     */
    EClass STRING_PARAMETER_VALUE = eINSTANCE.getStringParameterValue();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_PARAMETER_VALUE__VALUE = eINSTANCE.getStringParameterValue_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ListParameterValueImpl <em>List Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ListParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getListParameterValue()
     * @generated
     */
    EClass LIST_PARAMETER_VALUE = eINSTANCE.getListParameterValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapParameterValueImpl <em>Map Parameter Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.MapParameterValueImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMapParameterValue()
     * @generated
     */
    EClass MAP_PARAMETER_VALUE = eINSTANCE.getMapParameterValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapEntryImpl <em>Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.MapEntryImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMapEntry()
     * @generated
     */
    EClass MAP_ENTRY = eINSTANCE.getMapEntry();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MAP_ENTRY__KEY = eINSTANCE.getMapEntry_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MAP_ENTRY__VALUE = eINSTANCE.getMapEntry_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.ListImpl <em>List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.ListImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getList()
     * @generated
     */
    EClass LIST = eINSTANCE.getList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LIST__ELEMENTS = eINSTANCE.getList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.pql2.impl.MapImpl <em>Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.pql2.impl.MapImpl
     * @see de.uniol.inf.is.odysseus.pql2.impl.Pql2PackageImpl#getMap()
     * @generated
     */
    EClass MAP = eINSTANCE.getMap();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MAP__ELEMENTS = eINSTANCE.getMap_Elements();

  }

} //Pql2Package
