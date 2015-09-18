/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.xtext.common.types.TypesPackage;

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
 * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory
 * @model kind="package"
 * @generated
 */
public interface BasicIQLPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "basicIQL";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/iql/basic/BasicIQL";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "basicIQL";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  BasicIQLPackage eINSTANCE = de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelImpl <em>IQL Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLModel()
   * @generated
   */
  int IQL_MODEL = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL__NAME = 0;

  /**
   * The feature id for the '<em><b>Namespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL__NAMESPACES = 1;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL__ELEMENTS = 2;

  /**
   * The number of structural features of the '<em>IQL Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl <em>IQL Model Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLModelElement()
   * @generated
   */
  int IQL_MODEL_ELEMENT = 1;

  /**
   * The feature id for the '<em><b>Javametadata</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL_ELEMENT__JAVAMETADATA = 0;

  /**
   * The feature id for the '<em><b>Inner</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL_ELEMENT__INNER = 1;

  /**
   * The number of structural features of the '<em>IQL Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MODEL_ELEMENT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNamespaceImpl <em>IQL Namespace</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNamespaceImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLNamespace()
   * @generated
   */
  int IQL_NAMESPACE = 2;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NAMESPACE__STATIC = 0;

  /**
   * The feature id for the '<em><b>Imported Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NAMESPACE__IMPORTED_NAMESPACE = 1;

  /**
   * The number of structural features of the '<em>IQL Namespace</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NAMESPACE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMetadataImpl <em>IQL Java Metadata</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMetadataImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaMetadata()
   * @generated
   */
  int IQL_JAVA_METADATA = 3;

  /**
   * The feature id for the '<em><b>Java</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_METADATA__JAVA = 0;

  /**
   * The number of structural features of the '<em>IQL Java Metadata</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_METADATA_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataListImpl <em>IQL Metadata List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataListImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataList()
   * @generated
   */
  int IQL_METADATA_LIST = 4;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_LIST__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>IQL Metadata List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataImpl <em>IQL Metadata</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadata()
   * @generated
   */
  int IQL_METADATA = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA__NAME = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA__VALUE = 1;

  /**
   * The number of structural features of the '<em>IQL Metadata</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueImpl <em>IQL Metadata Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValue()
   * @generated
   */
  int IQL_METADATA_VALUE = 6;

  /**
   * The number of structural features of the '<em>IQL Metadata Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapElementImpl <em>IQL Metadata Value Map Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapElementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueMapElement()
   * @generated
   */
  int IQL_METADATA_VALUE_MAP_ELEMENT = 7;

  /**
   * The feature id for the '<em><b>Key</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_MAP_ELEMENT__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_MAP_ELEMENT__VALUE = 1;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Map Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_MAP_ELEMENT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableInitializationImpl <em>IQL Variable Initialization</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableInitializationImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableInitialization()
   * @generated
   */
  int IQL_VARIABLE_INITIALIZATION = 8;

  /**
   * The feature id for the '<em><b>Args List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_INITIALIZATION__ARGS_LIST = 0;

  /**
   * The feature id for the '<em><b>Args Map</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_INITIALIZATION__ARGS_MAP = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_INITIALIZATION__VALUE = 2;

  /**
   * The number of structural features of the '<em>IQL Variable Initialization</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_INITIALIZATION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsListImpl <em>IQL Arguments List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsListImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsList()
   * @generated
   */
  int IQL_ARGUMENTS_LIST = 9;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_LIST__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>IQL Arguments List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_LIST_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapImpl <em>IQL Arguments Map</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsMap()
   * @generated
   */
  int IQL_ARGUMENTS_MAP = 10;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_MAP__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>IQL Arguments Map</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_MAP_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapKeyValueImpl <em>IQL Arguments Map Key Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapKeyValueImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsMapKeyValue()
   * @generated
   */
  int IQL_ARGUMENTS_MAP_KEY_VALUE = 11;

  /**
   * The feature id for the '<em><b>Key</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_MAP_KEY_VALUE__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_MAP_KEY_VALUE__VALUE = 1;

  /**
   * The number of structural features of the '<em>IQL Arguments Map Key Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARGUMENTS_MAP_KEY_VALUE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementImpl <em>IQL Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLStatement()
   * @generated
   */
  int IQL_STATEMENT = 12;

  /**
   * The number of structural features of the '<em>IQL Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_STATEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLCasePartImpl <em>IQL Case Part</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLCasePartImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLCasePart()
   * @generated
   */
  int IQL_CASE_PART = 13;

  /**
   * The feature id for the '<em><b>Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CASE_PART__EXPR = 0;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CASE_PART__STATEMENTS = 1;

  /**
   * The number of structural features of the '<em>IQL Case Part</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CASE_PART_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl <em>IQL Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLExpression()
   * @generated
   */
  int IQL_EXPRESSION = 14;

  /**
   * The number of structural features of the '<em>IQL Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EXPRESSION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionImpl <em>IQL Member Selection</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMemberSelection()
   * @generated
   */
  int IQL_MEMBER_SELECTION = 15;

  /**
   * The feature id for the '<em><b>Member</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION__MEMBER = 0;

  /**
   * The feature id for the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION__ARGS = 1;

  /**
   * The number of structural features of the '<em>IQL Member Selection</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl <em>IQL Literal Expression Map Key Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionMapKeyValue()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE = 16;

  /**
   * The feature id for the '<em><b>Key</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE = 1;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Map Key Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaImpl <em>IQL Java</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJava()
   * @generated
   */
  int IQL_JAVA = 17;

  /**
   * The feature id for the '<em><b>Text</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA__TEXT = 0;

  /**
   * The number of structural features of the '<em>IQL Java</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl <em>IQL Class</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLClass()
   * @generated
   */
  int IQL_CLASS = 18;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__ANNOTATIONS = TypesPackage.JVM_GENERIC_TYPE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__DECLARING_TYPE = TypesPackage.JVM_GENERIC_TYPE__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__VISIBILITY = TypesPackage.JVM_GENERIC_TYPE__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__SIMPLE_NAME = TypesPackage.JVM_GENERIC_TYPE__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__IDENTIFIER = TypesPackage.JVM_GENERIC_TYPE__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__DEPRECATED = TypesPackage.JVM_GENERIC_TYPE__DEPRECATED;

  /**
   * The feature id for the '<em><b>Array Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__ARRAY_TYPE = TypesPackage.JVM_GENERIC_TYPE__ARRAY_TYPE;

  /**
   * The feature id for the '<em><b>Super Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__SUPER_TYPES = TypesPackage.JVM_GENERIC_TYPE__SUPER_TYPES;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__MEMBERS = TypesPackage.JVM_GENERIC_TYPE__MEMBERS;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__ABSTRACT = TypesPackage.JVM_GENERIC_TYPE__ABSTRACT;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__STATIC = TypesPackage.JVM_GENERIC_TYPE__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__FINAL = TypesPackage.JVM_GENERIC_TYPE__FINAL;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__PACKAGE_NAME = TypesPackage.JVM_GENERIC_TYPE__PACKAGE_NAME;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__TYPE_PARAMETERS = TypesPackage.JVM_GENERIC_TYPE__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__INTERFACE = TypesPackage.JVM_GENERIC_TYPE__INTERFACE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__STRICT_FLOATING_POINT = TypesPackage.JVM_GENERIC_TYPE__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Anonymous</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__ANONYMOUS = TypesPackage.JVM_GENERIC_TYPE__ANONYMOUS;

  /**
   * The feature id for the '<em><b>Extended Class</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__EXTENDED_CLASS = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Extended Interfaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS__EXTENDED_INTERFACES = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Class</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CLASS_FEATURE_COUNT = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInterfaceImpl <em>IQL Interface</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInterfaceImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLInterface()
   * @generated
   */
  int IQL_INTERFACE = 19;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__ANNOTATIONS = TypesPackage.JVM_GENERIC_TYPE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__DECLARING_TYPE = TypesPackage.JVM_GENERIC_TYPE__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__VISIBILITY = TypesPackage.JVM_GENERIC_TYPE__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__SIMPLE_NAME = TypesPackage.JVM_GENERIC_TYPE__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__IDENTIFIER = TypesPackage.JVM_GENERIC_TYPE__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__DEPRECATED = TypesPackage.JVM_GENERIC_TYPE__DEPRECATED;

  /**
   * The feature id for the '<em><b>Array Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__ARRAY_TYPE = TypesPackage.JVM_GENERIC_TYPE__ARRAY_TYPE;

  /**
   * The feature id for the '<em><b>Super Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__SUPER_TYPES = TypesPackage.JVM_GENERIC_TYPE__SUPER_TYPES;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__MEMBERS = TypesPackage.JVM_GENERIC_TYPE__MEMBERS;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__ABSTRACT = TypesPackage.JVM_GENERIC_TYPE__ABSTRACT;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__STATIC = TypesPackage.JVM_GENERIC_TYPE__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__FINAL = TypesPackage.JVM_GENERIC_TYPE__FINAL;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__PACKAGE_NAME = TypesPackage.JVM_GENERIC_TYPE__PACKAGE_NAME;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__TYPE_PARAMETERS = TypesPackage.JVM_GENERIC_TYPE__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__INTERFACE = TypesPackage.JVM_GENERIC_TYPE__INTERFACE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__STRICT_FLOATING_POINT = TypesPackage.JVM_GENERIC_TYPE__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Anonymous</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__ANONYMOUS = TypesPackage.JVM_GENERIC_TYPE__ANONYMOUS;

  /**
   * The feature id for the '<em><b>Extended Interfaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE__EXTENDED_INTERFACES = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Interface</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INTERFACE_FEATURE_COUNT = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAttributeImpl <em>IQL Attribute</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAttributeImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAttribute()
   * @generated
   */
  int IQL_ATTRIBUTE = 20;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__ANNOTATIONS = TypesPackage.JVM_FIELD__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__DECLARING_TYPE = TypesPackage.JVM_FIELD__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__VISIBILITY = TypesPackage.JVM_FIELD__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__SIMPLE_NAME = TypesPackage.JVM_FIELD__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__IDENTIFIER = TypesPackage.JVM_FIELD__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__DEPRECATED = TypesPackage.JVM_FIELD__DEPRECATED;

  /**
   * The feature id for the '<em><b>Local Classes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__LOCAL_CLASSES = TypesPackage.JVM_FIELD__LOCAL_CLASSES;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__STATIC = TypesPackage.JVM_FIELD__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__FINAL = TypesPackage.JVM_FIELD__FINAL;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__TYPE = TypesPackage.JVM_FIELD__TYPE;

  /**
   * The feature id for the '<em><b>Volatile</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__VOLATILE = TypesPackage.JVM_FIELD__VOLATILE;

  /**
   * The feature id for the '<em><b>Transient</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__TRANSIENT = TypesPackage.JVM_FIELD__TRANSIENT;

  /**
   * The feature id for the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__CONSTANT = TypesPackage.JVM_FIELD__CONSTANT;

  /**
   * The feature id for the '<em><b>Constant Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__CONSTANT_VALUE = TypesPackage.JVM_FIELD__CONSTANT_VALUE;

  /**
   * The feature id for the '<em><b>Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE__INIT = TypesPackage.JVM_FIELD_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Attribute</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ATTRIBUTE_FEATURE_COUNT = TypesPackage.JVM_FIELD_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSimpleTypeRefImpl <em>IQL Simple Type Ref</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSimpleTypeRefImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSimpleTypeRef()
   * @generated
   */
  int IQL_SIMPLE_TYPE_REF = 21;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SIMPLE_TYPE_REF__TYPE = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Simple Type Ref</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SIMPLE_TYPE_REF_FEATURE_COUNT = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeRefImpl <em>IQL Array Type Ref</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeRefImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayTypeRef()
   * @generated
   */
  int IQL_ARRAY_TYPE_REF = 22;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_TYPE_REF__TYPE = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Array Type Ref</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_TYPE_REF_FEATURE_COUNT = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl <em>IQL Array Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayType()
   * @generated
   */
  int IQL_ARRAY_TYPE = 23;

  /**
   * The feature id for the '<em><b>Component Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_TYPE__COMPONENT_TYPE = TypesPackage.JVM_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Dimensions</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_TYPE__DIMENSIONS = TypesPackage.JVM_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Array Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_TYPE_FEATURE_COUNT = TypesPackage.JVM_TYPE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodImpl <em>IQL Method</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMethod()
   * @generated
   */
  int IQL_METHOD = 24;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__ANNOTATIONS = TypesPackage.JVM_OPERATION__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__DECLARING_TYPE = TypesPackage.JVM_OPERATION__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__VISIBILITY = TypesPackage.JVM_OPERATION__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__SIMPLE_NAME = TypesPackage.JVM_OPERATION__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__IDENTIFIER = TypesPackage.JVM_OPERATION__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__DEPRECATED = TypesPackage.JVM_OPERATION__DEPRECATED;

  /**
   * The feature id for the '<em><b>Local Classes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__LOCAL_CLASSES = TypesPackage.JVM_OPERATION__LOCAL_CLASSES;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__TYPE_PARAMETERS = TypesPackage.JVM_OPERATION__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__PARAMETERS = TypesPackage.JVM_OPERATION__PARAMETERS;

  /**
   * The feature id for the '<em><b>Exceptions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__EXCEPTIONS = TypesPackage.JVM_OPERATION__EXCEPTIONS;

  /**
   * The feature id for the '<em><b>Var Args</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__VAR_ARGS = TypesPackage.JVM_OPERATION__VAR_ARGS;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__STATIC = TypesPackage.JVM_OPERATION__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__FINAL = TypesPackage.JVM_OPERATION__FINAL;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__ABSTRACT = TypesPackage.JVM_OPERATION__ABSTRACT;

  /**
   * The feature id for the '<em><b>Return Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__RETURN_TYPE = TypesPackage.JVM_OPERATION__RETURN_TYPE;

  /**
   * The feature id for the '<em><b>Default Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__DEFAULT_VALUE = TypesPackage.JVM_OPERATION__DEFAULT_VALUE;

  /**
   * The feature id for the '<em><b>Synchronized</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__SYNCHRONIZED = TypesPackage.JVM_OPERATION__SYNCHRONIZED;

  /**
   * The feature id for the '<em><b>Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__DEFAULT = TypesPackage.JVM_OPERATION__DEFAULT;

  /**
   * The feature id for the '<em><b>Native</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__NATIVE = TypesPackage.JVM_OPERATION__NATIVE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__STRICT_FLOATING_POINT = TypesPackage.JVM_OPERATION__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Override</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__OVERRIDE = TypesPackage.JVM_OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD__BODY = TypesPackage.JVM_OPERATION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Method</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_FEATURE_COUNT = TypesPackage.JVM_OPERATION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodDeclarationImpl <em>IQL Method Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodDeclarationImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMethodDeclaration()
   * @generated
   */
  int IQL_METHOD_DECLARATION = 25;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__ANNOTATIONS = TypesPackage.JVM_OPERATION__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__DECLARING_TYPE = TypesPackage.JVM_OPERATION__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__VISIBILITY = TypesPackage.JVM_OPERATION__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__SIMPLE_NAME = TypesPackage.JVM_OPERATION__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__IDENTIFIER = TypesPackage.JVM_OPERATION__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__DEPRECATED = TypesPackage.JVM_OPERATION__DEPRECATED;

  /**
   * The feature id for the '<em><b>Local Classes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__LOCAL_CLASSES = TypesPackage.JVM_OPERATION__LOCAL_CLASSES;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__TYPE_PARAMETERS = TypesPackage.JVM_OPERATION__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__PARAMETERS = TypesPackage.JVM_OPERATION__PARAMETERS;

  /**
   * The feature id for the '<em><b>Exceptions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__EXCEPTIONS = TypesPackage.JVM_OPERATION__EXCEPTIONS;

  /**
   * The feature id for the '<em><b>Var Args</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__VAR_ARGS = TypesPackage.JVM_OPERATION__VAR_ARGS;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__STATIC = TypesPackage.JVM_OPERATION__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__FINAL = TypesPackage.JVM_OPERATION__FINAL;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__ABSTRACT = TypesPackage.JVM_OPERATION__ABSTRACT;

  /**
   * The feature id for the '<em><b>Return Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__RETURN_TYPE = TypesPackage.JVM_OPERATION__RETURN_TYPE;

  /**
   * The feature id for the '<em><b>Default Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__DEFAULT_VALUE = TypesPackage.JVM_OPERATION__DEFAULT_VALUE;

  /**
   * The feature id for the '<em><b>Synchronized</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__SYNCHRONIZED = TypesPackage.JVM_OPERATION__SYNCHRONIZED;

  /**
   * The feature id for the '<em><b>Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__DEFAULT = TypesPackage.JVM_OPERATION__DEFAULT;

  /**
   * The feature id for the '<em><b>Native</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__NATIVE = TypesPackage.JVM_OPERATION__NATIVE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION__STRICT_FLOATING_POINT = TypesPackage.JVM_OPERATION__STRICT_FLOATING_POINT;

  /**
   * The number of structural features of the '<em>IQL Method Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METHOD_DECLARATION_FEATURE_COUNT = TypesPackage.JVM_OPERATION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMemberImpl <em>IQL Java Member</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMemberImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaMember()
   * @generated
   */
  int IQL_JAVA_MEMBER = 26;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__ANNOTATIONS = TypesPackage.JVM_MEMBER__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__DECLARING_TYPE = TypesPackage.JVM_MEMBER__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__VISIBILITY = TypesPackage.JVM_MEMBER__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__SIMPLE_NAME = TypesPackage.JVM_MEMBER__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__IDENTIFIER = TypesPackage.JVM_MEMBER__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__DEPRECATED = TypesPackage.JVM_MEMBER__DEPRECATED;

  /**
   * The feature id for the '<em><b>Java</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER__JAVA = TypesPackage.JVM_MEMBER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Java Member</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_MEMBER_FEATURE_COUNT = TypesPackage.JVM_MEMBER_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleIntImpl <em>IQL Metadata Value Single Int</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleIntImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleInt()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_INT = 27;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_INT__VALUE = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single Int</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_INT_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleDoubleImpl <em>IQL Metadata Value Single Double</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleDoubleImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleDouble()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_DOUBLE = 28;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_DOUBLE__VALUE = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single Double</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_DOUBLE_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleStringImpl <em>IQL Metadata Value Single String</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleStringImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleString()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_STRING = 29;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_STRING__VALUE = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single String</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_STRING_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleBooleanImpl <em>IQL Metadata Value Single Boolean</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleBooleanImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleBoolean()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_BOOLEAN = 30;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_BOOLEAN__VALUE = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single Boolean</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_BOOLEAN_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleTypeRefImpl <em>IQL Metadata Value Single Type Ref</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleTypeRefImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleTypeRef()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_TYPE_REF = 31;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_TYPE_REF__VALUE = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single Type Ref</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_TYPE_REF_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleNullImpl <em>IQL Metadata Value Single Null</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleNullImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleNull()
   * @generated
   */
  int IQL_METADATA_VALUE_SINGLE_NULL = 32;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Single Null</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_SINGLE_NULL_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueListImpl <em>IQL Metadata Value List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueListImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueList()
   * @generated
   */
  int IQL_METADATA_VALUE_LIST = 33;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_LIST__ELEMENTS = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_LIST_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapImpl <em>IQL Metadata Value Map</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueMap()
   * @generated
   */
  int IQL_METADATA_VALUE_MAP = 34;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_MAP__ELEMENTS = IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Metadata Value Map</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_METADATA_VALUE_MAP_FEATURE_COUNT = IQL_METADATA_VALUE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableDeclarationImpl <em>IQL Variable Declaration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableDeclarationImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableDeclaration()
   * @generated
   */
  int IQL_VARIABLE_DECLARATION = 35;

  /**
   * The feature id for the '<em><b>Ref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_DECLARATION__REF = TypesPackage.JVM_IDENTIFIABLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_DECLARATION__NAME = TypesPackage.JVM_IDENTIFIABLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Variable Declaration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_DECLARATION_FEATURE_COUNT = TypesPackage.JVM_IDENTIFIABLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementBlockImpl <em>IQL Statement Block</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementBlockImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLStatementBlock()
   * @generated
   */
  int IQL_STATEMENT_BLOCK = 36;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_STATEMENT_BLOCK__STATEMENTS = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Statement Block</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_STATEMENT_BLOCK_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaStatementImpl <em>IQL Java Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaStatement()
   * @generated
   */
  int IQL_JAVA_STATEMENT = 37;

  /**
   * The feature id for the '<em><b>Java</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_STATEMENT__JAVA = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Java Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JAVA_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl <em>IQL If Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLIfStatement()
   * @generated
   */
  int IQL_IF_STATEMENT = 38;

  /**
   * The feature id for the '<em><b>Predicate</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_IF_STATEMENT__PREDICATE = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Then Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_IF_STATEMENT__THEN_BODY = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Else Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_IF_STATEMENT__ELSE_BODY = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL If Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_IF_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLWhileStatementImpl <em>IQL While Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLWhileStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLWhileStatement()
   * @generated
   */
  int IQL_WHILE_STATEMENT = 39;

  /**
   * The feature id for the '<em><b>Predicate</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_WHILE_STATEMENT__PREDICATE = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_WHILE_STATEMENT__BODY = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL While Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_WHILE_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLDoWhileStatementImpl <em>IQL Do While Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLDoWhileStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLDoWhileStatement()
   * @generated
   */
  int IQL_DO_WHILE_STATEMENT = 40;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_DO_WHILE_STATEMENT__BODY = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Predicate</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_DO_WHILE_STATEMENT__PREDICATE = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Do While Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_DO_WHILE_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl <em>IQL For Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLForStatement()
   * @generated
   */
  int IQL_FOR_STATEMENT = 41;

  /**
   * The feature id for the '<em><b>Var</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT__VAR = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT__VALUE = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Predicate</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT__PREDICATE = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Update Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT__UPDATE_EXPR = IQL_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT__BODY = IQL_STATEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>IQL For Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl <em>IQL For Each Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLForEachStatement()
   * @generated
   */
  int IQL_FOR_EACH_STATEMENT = 42;

  /**
   * The feature id for the '<em><b>Var</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_EACH_STATEMENT__VAR = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>For Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_EACH_STATEMENT__BODY = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL For Each Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_FOR_EACH_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSwitchStatementImpl <em>IQL Switch Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSwitchStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSwitchStatement()
   * @generated
   */
  int IQL_SWITCH_STATEMENT = 43;

  /**
   * The feature id for the '<em><b>Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SWITCH_STATEMENT__EXPR = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Cases</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SWITCH_STATEMENT__CASES = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SWITCH_STATEMENT__STATEMENTS = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Switch Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SWITCH_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionStatementImpl <em>IQL Expression Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLExpressionStatement()
   * @generated
   */
  int IQL_EXPRESSION_STATEMENT = 44;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EXPRESSION_STATEMENT__EXPRESSION = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Expression Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EXPRESSION_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableStatementImpl <em>IQL Variable Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableStatement()
   * @generated
   */
  int IQL_VARIABLE_STATEMENT = 45;

  /**
   * The feature id for the '<em><b>Var</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_STATEMENT__VAR = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_STATEMENT__INIT = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Variable Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_VARIABLE_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl <em>IQL Constructor Call Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLConstructorCallStatement()
   * @generated
   */
  int IQL_CONSTRUCTOR_CALL_STATEMENT = 46;

  /**
   * The feature id for the '<em><b>This</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CONSTRUCTOR_CALL_STATEMENT__THIS = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Super</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS = IQL_STATEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Constructor Call Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CONSTRUCTOR_CALL_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBreakStatementImpl <em>IQL Break Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBreakStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLBreakStatement()
   * @generated
   */
  int IQL_BREAK_STATEMENT = 47;

  /**
   * The number of structural features of the '<em>IQL Break Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_BREAK_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLContinueStatementImpl <em>IQL Continue Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLContinueStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLContinueStatement()
   * @generated
   */
  int IQL_CONTINUE_STATEMENT = 48;

  /**
   * The number of structural features of the '<em>IQL Continue Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_CONTINUE_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLReturnStatementImpl <em>IQL Return Statement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLReturnStatementImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLReturnStatement()
   * @generated
   */
  int IQL_RETURN_STATEMENT = 49;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RETURN_STATEMENT__EXPRESSION = IQL_STATEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Return Statement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RETURN_STATEMENT_FEATURE_COUNT = IQL_STATEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAssignmentExpressionImpl <em>IQL Assignment Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAssignmentExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAssignmentExpression()
   * @generated
   */
  int IQL_ASSIGNMENT_EXPRESSION = 50;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ASSIGNMENT_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ASSIGNMENT_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ASSIGNMENT_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Assignment Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ASSIGNMENT_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalOrExpressionImpl <em>IQL Logical Or Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalOrExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLogicalOrExpression()
   * @generated
   */
  int IQL_LOGICAL_OR_EXPRESSION = 51;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_OR_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_OR_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_OR_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Logical Or Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_OR_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalAndExpressionImpl <em>IQL Logical And Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalAndExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLogicalAndExpression()
   * @generated
   */
  int IQL_LOGICAL_AND_EXPRESSION = 52;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_AND_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_AND_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_AND_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Logical And Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LOGICAL_AND_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLEqualityExpressionImpl <em>IQL Equality Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLEqualityExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLEqualityExpression()
   * @generated
   */
  int IQL_EQUALITY_EXPRESSION = 53;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EQUALITY_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EQUALITY_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EQUALITY_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Equality Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_EQUALITY_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInstanceOfExpressionImpl <em>IQL Instance Of Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInstanceOfExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLInstanceOfExpression()
   * @generated
   */
  int IQL_INSTANCE_OF_EXPRESSION = 54;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target Ref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION__TARGET_REF = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Instance Of Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLRelationalExpressionImpl <em>IQL Relational Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLRelationalExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLRelationalExpression()
   * @generated
   */
  int IQL_RELATIONAL_EXPRESSION = 55;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Relational Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAdditiveExpressionImpl <em>IQL Additive Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAdditiveExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAdditiveExpression()
   * @generated
   */
  int IQL_ADDITIVE_EXPRESSION = 56;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ADDITIVE_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ADDITIVE_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ADDITIVE_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Additive Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ADDITIVE_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMultiplicativeExpressionImpl <em>IQL Multiplicative Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMultiplicativeExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMultiplicativeExpression()
   * @generated
   */
  int IQL_MULTIPLICATIVE_EXPRESSION = 57;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MULTIPLICATIVE_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MULTIPLICATIVE_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MULTIPLICATIVE_EXPRESSION__RIGHT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Multiplicative Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MULTIPLICATIVE_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPlusMinusExpressionImpl <em>IQL Plus Minus Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPlusMinusExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPlusMinusExpression()
   * @generated
   */
  int IQL_PLUS_MINUS_EXPRESSION = 58;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PLUS_MINUS_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PLUS_MINUS_EXPRESSION__OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Plus Minus Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PLUS_MINUS_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBooleanNotExpressionImpl <em>IQL Boolean Not Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBooleanNotExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLBooleanNotExpression()
   * @generated
   */
  int IQL_BOOLEAN_NOT_EXPRESSION = 59;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_BOOLEAN_NOT_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_BOOLEAN_NOT_EXPRESSION__OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Boolean Not Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_BOOLEAN_NOT_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPrefixExpressionImpl <em>IQL Prefix Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPrefixExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPrefixExpression()
   * @generated
   */
  int IQL_PREFIX_EXPRESSION = 60;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PREFIX_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PREFIX_EXPRESSION__OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Prefix Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PREFIX_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl <em>IQL Type Cast Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLTypeCastExpression()
   * @generated
   */
  int IQL_TYPE_CAST_EXPRESSION = 61;

  /**
   * The feature id for the '<em><b>Target Ref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_TYPE_CAST_EXPRESSION__TARGET_REF = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_TYPE_CAST_EXPRESSION__OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Type Cast Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_TYPE_CAST_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPostfixExpressionImpl <em>IQL Postfix Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPostfixExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPostfixExpression()
   * @generated
   */
  int IQL_POSTFIX_EXPRESSION = 62;

  /**
   * The feature id for the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_POSTFIX_EXPRESSION__OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_POSTFIX_EXPRESSION__OP = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Postfix Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_POSTFIX_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayExpressionImpl <em>IQL Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayExpression()
   * @generated
   */
  int IQL_ARRAY_EXPRESSION = 63;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Expressions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_EXPRESSION__EXPRESSIONS = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_ARRAY_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl <em>IQL Member Selection Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMemberSelectionExpression()
   * @generated
   */
  int IQL_MEMBER_SELECTION_EXPRESSION = 64;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Sel</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION_EXPRESSION__SEL = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Member Selection Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_MEMBER_SELECTION_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJvmElementCallExpressionImpl <em>IQL Jvm Element Call Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJvmElementCallExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJvmElementCallExpression()
   * @generated
   */
  int IQL_JVM_ELEMENT_CALL_EXPRESSION = 65;

  /**
   * The feature id for the '<em><b>Element</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JVM_ELEMENT_CALL_EXPRESSION__ELEMENT = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JVM_ELEMENT_CALL_EXPRESSION__ARGS = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Jvm Element Call Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_JVM_ELEMENT_CALL_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLThisExpressionImpl <em>IQL This Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLThisExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLThisExpression()
   * @generated
   */
  int IQL_THIS_EXPRESSION = 66;

  /**
   * The number of structural features of the '<em>IQL This Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_THIS_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSuperExpressionImpl <em>IQL Super Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSuperExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSuperExpression()
   * @generated
   */
  int IQL_SUPER_EXPRESSION = 67;

  /**
   * The number of structural features of the '<em>IQL Super Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SUPER_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLParenthesisExpressionImpl <em>IQL Parenthesis Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLParenthesisExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLParenthesisExpression()
   * @generated
   */
  int IQL_PARENTHESIS_EXPRESSION = 68;

  /**
   * The feature id for the '<em><b>Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PARENTHESIS_EXPRESSION__EXPR = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Parenthesis Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PARENTHESIS_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl <em>IQL New Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLNewExpression()
   * @generated
   */
  int IQL_NEW_EXPRESSION = 69;

  /**
   * The feature id for the '<em><b>Ref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NEW_EXPRESSION__REF = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Args List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NEW_EXPRESSION__ARGS_LIST = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Args Map</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NEW_EXPRESSION__ARGS_MAP = IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL New Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_NEW_EXPRESSION_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionIntImpl <em>IQL Literal Expression Int</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionIntImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionInt()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_INT = 70;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_INT__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Int</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_INT_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionDoubleImpl <em>IQL Literal Expression Double</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionDoubleImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionDouble()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_DOUBLE = 71;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_DOUBLE__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Double</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_DOUBLE_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionStringImpl <em>IQL Literal Expression String</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionStringImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionString()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_STRING = 72;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_STRING__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression String</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_STRING_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionBooleanImpl <em>IQL Literal Expression Boolean</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionBooleanImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionBoolean()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_BOOLEAN = 73;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_BOOLEAN__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Boolean</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_BOOLEAN_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionRangeImpl <em>IQL Literal Expression Range</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionRangeImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionRange()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_RANGE = 74;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_RANGE__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Range</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_RANGE_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionTypeImpl <em>IQL Literal Expression Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionTypeImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionType()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_TYPE = 75;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_TYPE__VALUE = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_TYPE_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionNullImpl <em>IQL Literal Expression Null</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionNullImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionNull()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_NULL = 76;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Null</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_NULL_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionListImpl <em>IQL Literal Expression List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionListImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionList()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_LIST = 77;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_LIST__ELEMENTS = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_LIST_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapImpl <em>IQL Literal Expression Map</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapImpl
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionMap()
   * @generated
   */
  int IQL_LITERAL_EXPRESSION_MAP = 78;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_MAP__ELEMENTS = IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>IQL Literal Expression Map</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_LITERAL_EXPRESSION_MAP_FEATURE_COUNT = IQL_EXPRESSION_FEATURE_COUNT + 1;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel <em>IQL Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Model</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel
   * @generated
   */
  EClass getIQLModel();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getName()
   * @see #getIQLModel()
   * @generated
   */
  EAttribute getIQLModel_Name();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getNamespaces <em>Namespaces</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Namespaces</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getNamespaces()
   * @see #getIQLModel()
   * @generated
   */
  EReference getIQLModel_Namespaces();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel#getElements()
   * @see #getIQLModel()
   * @generated
   */
  EReference getIQLModel_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement <em>IQL Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Model Element</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement
   * @generated
   */
  EClass getIQLModelElement();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement#getJavametadata <em>Javametadata</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Javametadata</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement#getJavametadata()
   * @see #getIQLModelElement()
   * @generated
   */
  EReference getIQLModelElement_Javametadata();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement#getInner <em>Inner</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Inner</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement#getInner()
   * @see #getIQLModelElement()
   * @generated
   */
  EReference getIQLModelElement_Inner();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace <em>IQL Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Namespace</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace
   * @generated
   */
  EClass getIQLNamespace();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace#isStatic <em>Static</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Static</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace#isStatic()
   * @see #getIQLNamespace()
   * @generated
   */
  EAttribute getIQLNamespace_Static();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace#getImportedNamespace <em>Imported Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Imported Namespace</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace#getImportedNamespace()
   * @see #getIQLNamespace()
   * @generated
   */
  EAttribute getIQLNamespace_ImportedNamespace();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata <em>IQL Java Metadata</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Java Metadata</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata
   * @generated
   */
  EClass getIQLJavaMetadata();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata#getJava <em>Java</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Java</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata#getJava()
   * @see #getIQLJavaMetadata()
   * @generated
   */
  EReference getIQLJavaMetadata_Java();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList <em>IQL Metadata List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList
   * @generated
   */
  EClass getIQLMetadataList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList#getElements()
   * @see #getIQLMetadataList()
   * @generated
   */
  EReference getIQLMetadataList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata <em>IQL Metadata</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata
   * @generated
   */
  EClass getIQLMetadata();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata#getName()
   * @see #getIQLMetadata()
   * @generated
   */
  EAttribute getIQLMetadata_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata#getValue()
   * @see #getIQLMetadata()
   * @generated
   */
  EReference getIQLMetadata_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue <em>IQL Metadata Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
   * @generated
   */
  EClass getIQLMetadataValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement <em>IQL Metadata Value Map Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Map Element</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement
   * @generated
   */
  EClass getIQLMetadataValueMapElement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Key</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement#getKey()
   * @see #getIQLMetadataValueMapElement()
   * @generated
   */
  EReference getIQLMetadataValueMapElement_Key();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement#getValue()
   * @see #getIQLMetadataValueMapElement()
   * @generated
   */
  EReference getIQLMetadataValueMapElement_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization <em>IQL Variable Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Variable Initialization</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization
   * @generated
   */
  EClass getIQLVariableInitialization();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getArgsList <em>Args List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getArgsList()
   * @see #getIQLVariableInitialization()
   * @generated
   */
  EReference getIQLVariableInitialization_ArgsList();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getArgsMap <em>Args Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args Map</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getArgsMap()
   * @see #getIQLVariableInitialization()
   * @generated
   */
  EReference getIQLVariableInitialization_ArgsMap();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization#getValue()
   * @see #getIQLVariableInitialization()
   * @generated
   */
  EReference getIQLVariableInitialization_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList <em>IQL Arguments List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Arguments List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList
   * @generated
   */
  EClass getIQLArgumentsList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList#getElements()
   * @see #getIQLArgumentsList()
   * @generated
   */
  EReference getIQLArgumentsList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap <em>IQL Arguments Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Arguments Map</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
   * @generated
   */
  EClass getIQLArgumentsMap();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap#getElements()
   * @see #getIQLArgumentsMap()
   * @generated
   */
  EReference getIQLArgumentsMap_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue <em>IQL Arguments Map Key Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Arguments Map Key Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue
   * @generated
   */
  EClass getIQLArgumentsMapKeyValue();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Key</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue#getKey()
   * @see #getIQLArgumentsMapKeyValue()
   * @generated
   */
  EReference getIQLArgumentsMapKeyValue_Key();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue#getValue()
   * @see #getIQLArgumentsMapKeyValue()
   * @generated
   */
  EReference getIQLArgumentsMapKeyValue_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement <em>IQL Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement
   * @generated
   */
  EClass getIQLStatement();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart <em>IQL Case Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Case Part</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart
   * @generated
   */
  EClass getIQLCasePart();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart#getExpr <em>Expr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expr</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart#getExpr()
   * @see #getIQLCasePart()
   * @generated
   */
  EReference getIQLCasePart_Expr();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart#getStatements <em>Statements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Statements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart#getStatements()
   * @see #getIQLCasePart()
   * @generated
   */
  EReference getIQLCasePart_Statements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression <em>IQL Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
   * @generated
   */
  EClass getIQLExpression();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection <em>IQL Member Selection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Member Selection</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection
   * @generated
   */
  EClass getIQLMemberSelection();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection#getMember <em>Member</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Member</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection#getMember()
   * @see #getIQLMemberSelection()
   * @generated
   */
  EReference getIQLMemberSelection_Member();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection#getArgs <em>Args</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection#getArgs()
   * @see #getIQLMemberSelection()
   * @generated
   */
  EReference getIQLMemberSelection_Args();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue <em>IQL Literal Expression Map Key Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Map Key Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue
   * @generated
   */
  EClass getIQLLiteralExpressionMapKeyValue();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Key</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue#getKey()
   * @see #getIQLLiteralExpressionMapKeyValue()
   * @generated
   */
  EReference getIQLLiteralExpressionMapKeyValue_Key();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue#getValue()
   * @see #getIQLLiteralExpressionMapKeyValue()
   * @generated
   */
  EReference getIQLLiteralExpressionMapKeyValue_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava <em>IQL Java</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Java</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava
   * @generated
   */
  EClass getIQLJava();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava#getText <em>Text</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Text</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava#getText()
   * @see #getIQLJava()
   * @generated
   */
  EAttribute getIQLJava_Text();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass <em>IQL Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Class</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass
   * @generated
   */
  EClass getIQLClass();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass#getExtendedClass <em>Extended Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Extended Class</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass#getExtendedClass()
   * @see #getIQLClass()
   * @generated
   */
  EReference getIQLClass_ExtendedClass();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass#getExtendedInterfaces <em>Extended Interfaces</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Extended Interfaces</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass#getExtendedInterfaces()
   * @see #getIQLClass()
   * @generated
   */
  EReference getIQLClass_ExtendedInterfaces();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface <em>IQL Interface</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Interface</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface
   * @generated
   */
  EClass getIQLInterface();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface#getExtendedInterfaces <em>Extended Interfaces</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Extended Interfaces</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface#getExtendedInterfaces()
   * @see #getIQLInterface()
   * @generated
   */
  EReference getIQLInterface_ExtendedInterfaces();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute <em>IQL Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Attribute</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute
   * @generated
   */
  EClass getIQLAttribute();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute#getInit <em>Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Init</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute#getInit()
   * @see #getIQLAttribute()
   * @generated
   */
  EReference getIQLAttribute_Init();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef <em>IQL Simple Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Simple Type Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef
   * @generated
   */
  EClass getIQLSimpleTypeRef();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef#getType()
   * @see #getIQLSimpleTypeRef()
   * @generated
   */
  EReference getIQLSimpleTypeRef_Type();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef <em>IQL Array Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Array Type Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef
   * @generated
   */
  EClass getIQLArrayTypeRef();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef#getType()
   * @see #getIQLArrayTypeRef()
   * @generated
   */
  EReference getIQLArrayTypeRef_Type();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType <em>IQL Array Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Array Type</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType
   * @generated
   */
  EClass getIQLArrayType();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getComponentType <em>Component Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Component Type</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getComponentType()
   * @see #getIQLArrayType()
   * @generated
   */
  EReference getIQLArrayType_ComponentType();

  /**
   * Returns the meta object for the attribute list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getDimensions <em>Dimensions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Dimensions</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType#getDimensions()
   * @see #getIQLArrayType()
   * @generated
   */
  EAttribute getIQLArrayType_Dimensions();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod <em>IQL Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Method</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod
   * @generated
   */
  EClass getIQLMethod();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod#isOverride <em>Override</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Override</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod#isOverride()
   * @see #getIQLMethod()
   * @generated
   */
  EAttribute getIQLMethod_Override();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod#getBody()
   * @see #getIQLMethod()
   * @generated
   */
  EReference getIQLMethod_Body();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration <em>IQL Method Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Method Declaration</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration
   * @generated
   */
  EClass getIQLMethodDeclaration();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember <em>IQL Java Member</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Java Member</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember
   * @generated
   */
  EClass getIQLJavaMember();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember#getJava <em>Java</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Java</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember#getJava()
   * @see #getIQLJavaMember()
   * @generated
   */
  EReference getIQLJavaMember_Java();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt <em>IQL Metadata Value Single Int</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single Int</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt
   * @generated
   */
  EClass getIQLMetadataValueSingleInt();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleInt#getValue()
   * @see #getIQLMetadataValueSingleInt()
   * @generated
   */
  EAttribute getIQLMetadataValueSingleInt_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble <em>IQL Metadata Value Single Double</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single Double</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble
   * @generated
   */
  EClass getIQLMetadataValueSingleDouble();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble#getValue()
   * @see #getIQLMetadataValueSingleDouble()
   * @generated
   */
  EAttribute getIQLMetadataValueSingleDouble_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString <em>IQL Metadata Value Single String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single String</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString
   * @generated
   */
  EClass getIQLMetadataValueSingleString();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString#getValue()
   * @see #getIQLMetadataValueSingleString()
   * @generated
   */
  EAttribute getIQLMetadataValueSingleString_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean <em>IQL Metadata Value Single Boolean</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single Boolean</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean
   * @generated
   */
  EClass getIQLMetadataValueSingleBoolean();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean#isValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean#isValue()
   * @see #getIQLMetadataValueSingleBoolean()
   * @generated
   */
  EAttribute getIQLMetadataValueSingleBoolean_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef <em>IQL Metadata Value Single Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single Type Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef
   * @generated
   */
  EClass getIQLMetadataValueSingleTypeRef();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef#getValue()
   * @see #getIQLMetadataValueSingleTypeRef()
   * @generated
   */
  EReference getIQLMetadataValueSingleTypeRef_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull <em>IQL Metadata Value Single Null</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Single Null</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull
   * @generated
   */
  EClass getIQLMetadataValueSingleNull();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList <em>IQL Metadata Value List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList
   * @generated
   */
  EClass getIQLMetadataValueList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList#getElements()
   * @see #getIQLMetadataValueList()
   * @generated
   */
  EReference getIQLMetadataValueList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap <em>IQL Metadata Value Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Metadata Value Map</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap
   * @generated
   */
  EClass getIQLMetadataValueMap();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap#getElements()
   * @see #getIQLMetadataValueMap()
   * @generated
   */
  EReference getIQLMetadataValueMap_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration <em>IQL Variable Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Variable Declaration</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
   * @generated
   */
  EClass getIQLVariableDeclaration();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration#getRef <em>Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration#getRef()
   * @see #getIQLVariableDeclaration()
   * @generated
   */
  EReference getIQLVariableDeclaration_Ref();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration#getName()
   * @see #getIQLVariableDeclaration()
   * @generated
   */
  EAttribute getIQLVariableDeclaration_Name();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock <em>IQL Statement Block</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Statement Block</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock
   * @generated
   */
  EClass getIQLStatementBlock();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock#getStatements <em>Statements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Statements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock#getStatements()
   * @see #getIQLStatementBlock()
   * @generated
   */
  EReference getIQLStatementBlock_Statements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement <em>IQL Java Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Java Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement
   * @generated
   */
  EClass getIQLJavaStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement#getJava <em>Java</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Java</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement#getJava()
   * @see #getIQLJavaStatement()
   * @generated
   */
  EReference getIQLJavaStatement_Java();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement <em>IQL If Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL If Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement
   * @generated
   */
  EClass getIQLIfStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getPredicate <em>Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Predicate</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getPredicate()
   * @see #getIQLIfStatement()
   * @generated
   */
  EReference getIQLIfStatement_Predicate();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getThenBody <em>Then Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Then Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getThenBody()
   * @see #getIQLIfStatement()
   * @generated
   */
  EReference getIQLIfStatement_ThenBody();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getElseBody <em>Else Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Else Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement#getElseBody()
   * @see #getIQLIfStatement()
   * @generated
   */
  EReference getIQLIfStatement_ElseBody();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement <em>IQL While Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL While Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement
   * @generated
   */
  EClass getIQLWhileStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement#getPredicate <em>Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Predicate</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement#getPredicate()
   * @see #getIQLWhileStatement()
   * @generated
   */
  EReference getIQLWhileStatement_Predicate();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement#getBody()
   * @see #getIQLWhileStatement()
   * @generated
   */
  EReference getIQLWhileStatement_Body();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement <em>IQL Do While Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Do While Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement
   * @generated
   */
  EClass getIQLDoWhileStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement#getBody()
   * @see #getIQLDoWhileStatement()
   * @generated
   */
  EReference getIQLDoWhileStatement_Body();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement#getPredicate <em>Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Predicate</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement#getPredicate()
   * @see #getIQLDoWhileStatement()
   * @generated
   */
  EReference getIQLDoWhileStatement_Predicate();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement <em>IQL For Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL For Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement
   * @generated
   */
  EClass getIQLForStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getVar <em>Var</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getVar()
   * @see #getIQLForStatement()
   * @generated
   */
  EReference getIQLForStatement_Var();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getValue()
   * @see #getIQLForStatement()
   * @generated
   */
  EReference getIQLForStatement_Value();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getPredicate <em>Predicate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Predicate</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getPredicate()
   * @see #getIQLForStatement()
   * @generated
   */
  EReference getIQLForStatement_Predicate();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getUpdateExpr <em>Update Expr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Update Expr</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getUpdateExpr()
   * @see #getIQLForStatement()
   * @generated
   */
  EReference getIQLForStatement_UpdateExpr();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement#getBody()
   * @see #getIQLForStatement()
   * @generated
   */
  EReference getIQLForStatement_Body();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement <em>IQL For Each Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL For Each Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement
   * @generated
   */
  EClass getIQLForEachStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getVar <em>Var</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getVar()
   * @see #getIQLForEachStatement()
   * @generated
   */
  EReference getIQLForEachStatement_Var();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getForExpression <em>For Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>For Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getForExpression()
   * @see #getIQLForEachStatement()
   * @generated
   */
  EReference getIQLForEachStatement_ForExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement#getBody()
   * @see #getIQLForEachStatement()
   * @generated
   */
  EReference getIQLForEachStatement_Body();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement <em>IQL Switch Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Switch Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement
   * @generated
   */
  EClass getIQLSwitchStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getExpr <em>Expr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expr</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getExpr()
   * @see #getIQLSwitchStatement()
   * @generated
   */
  EReference getIQLSwitchStatement_Expr();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getCases <em>Cases</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Cases</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getCases()
   * @see #getIQLSwitchStatement()
   * @generated
   */
  EReference getIQLSwitchStatement_Cases();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getStatements <em>Statements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Statements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement#getStatements()
   * @see #getIQLSwitchStatement()
   * @generated
   */
  EReference getIQLSwitchStatement_Statements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement <em>IQL Expression Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Expression Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement
   * @generated
   */
  EClass getIQLExpressionStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement#getExpression()
   * @see #getIQLExpressionStatement()
   * @generated
   */
  EReference getIQLExpressionStatement_Expression();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement <em>IQL Variable Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Variable Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement
   * @generated
   */
  EClass getIQLVariableStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement#getVar <em>Var</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Var</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement#getVar()
   * @see #getIQLVariableStatement()
   * @generated
   */
  EReference getIQLVariableStatement_Var();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement#getInit <em>Init</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Init</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement#getInit()
   * @see #getIQLVariableStatement()
   * @generated
   */
  EReference getIQLVariableStatement_Init();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement <em>IQL Constructor Call Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Constructor Call Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement
   * @generated
   */
  EClass getIQLConstructorCallStatement();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#isThis <em>This</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>This</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#isThis()
   * @see #getIQLConstructorCallStatement()
   * @generated
   */
  EAttribute getIQLConstructorCallStatement_This();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#isSuper <em>Super</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Super</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#isSuper()
   * @see #getIQLConstructorCallStatement()
   * @generated
   */
  EAttribute getIQLConstructorCallStatement_Super();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#getArgs <em>Args</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement#getArgs()
   * @see #getIQLConstructorCallStatement()
   * @generated
   */
  EReference getIQLConstructorCallStatement_Args();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement <em>IQL Break Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Break Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement
   * @generated
   */
  EClass getIQLBreakStatement();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement <em>IQL Continue Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Continue Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement
   * @generated
   */
  EClass getIQLContinueStatement();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement <em>IQL Return Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Return Statement</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement
   * @generated
   */
  EClass getIQLReturnStatement();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement#getExpression()
   * @see #getIQLReturnStatement()
   * @generated
   */
  EReference getIQLReturnStatement_Expression();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression <em>IQL Assignment Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Assignment Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression
   * @generated
   */
  EClass getIQLAssignmentExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getLeftOperand()
   * @see #getIQLAssignmentExpression()
   * @generated
   */
  EReference getIQLAssignmentExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getOp()
   * @see #getIQLAssignmentExpression()
   * @generated
   */
  EAttribute getIQLAssignmentExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression#getRightOperand()
   * @see #getIQLAssignmentExpression()
   * @generated
   */
  EReference getIQLAssignmentExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression <em>IQL Logical Or Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Logical Or Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression
   * @generated
   */
  EClass getIQLLogicalOrExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getLeftOperand()
   * @see #getIQLLogicalOrExpression()
   * @generated
   */
  EReference getIQLLogicalOrExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getOp()
   * @see #getIQLLogicalOrExpression()
   * @generated
   */
  EAttribute getIQLLogicalOrExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression#getRightOperand()
   * @see #getIQLLogicalOrExpression()
   * @generated
   */
  EReference getIQLLogicalOrExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression <em>IQL Logical And Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Logical And Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression
   * @generated
   */
  EClass getIQLLogicalAndExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getLeftOperand()
   * @see #getIQLLogicalAndExpression()
   * @generated
   */
  EReference getIQLLogicalAndExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getOp()
   * @see #getIQLLogicalAndExpression()
   * @generated
   */
  EAttribute getIQLLogicalAndExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression#getRightOperand()
   * @see #getIQLLogicalAndExpression()
   * @generated
   */
  EReference getIQLLogicalAndExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression <em>IQL Equality Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Equality Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression
   * @generated
   */
  EClass getIQLEqualityExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getLeftOperand()
   * @see #getIQLEqualityExpression()
   * @generated
   */
  EReference getIQLEqualityExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getOp()
   * @see #getIQLEqualityExpression()
   * @generated
   */
  EAttribute getIQLEqualityExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression#getRightOperand()
   * @see #getIQLEqualityExpression()
   * @generated
   */
  EReference getIQLEqualityExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression <em>IQL Instance Of Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Instance Of Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression
   * @generated
   */
  EClass getIQLInstanceOfExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression#getLeftOperand()
   * @see #getIQLInstanceOfExpression()
   * @generated
   */
  EReference getIQLInstanceOfExpression_LeftOperand();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression#getTargetRef <em>Target Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Target Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression#getTargetRef()
   * @see #getIQLInstanceOfExpression()
   * @generated
   */
  EReference getIQLInstanceOfExpression_TargetRef();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression <em>IQL Relational Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Relational Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression
   * @generated
   */
  EClass getIQLRelationalExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getLeftOperand()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EReference getIQLRelationalExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getOp()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EAttribute getIQLRelationalExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression#getRightOperand()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EReference getIQLRelationalExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression <em>IQL Additive Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Additive Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression
   * @generated
   */
  EClass getIQLAdditiveExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getLeftOperand()
   * @see #getIQLAdditiveExpression()
   * @generated
   */
  EReference getIQLAdditiveExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getOp()
   * @see #getIQLAdditiveExpression()
   * @generated
   */
  EAttribute getIQLAdditiveExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression#getRightOperand()
   * @see #getIQLAdditiveExpression()
   * @generated
   */
  EReference getIQLAdditiveExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression <em>IQL Multiplicative Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Multiplicative Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression
   * @generated
   */
  EClass getIQLMultiplicativeExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getLeftOperand()
   * @see #getIQLMultiplicativeExpression()
   * @generated
   */
  EReference getIQLMultiplicativeExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getOp()
   * @see #getIQLMultiplicativeExpression()
   * @generated
   */
  EAttribute getIQLMultiplicativeExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression#getRightOperand()
   * @see #getIQLMultiplicativeExpression()
   * @generated
   */
  EReference getIQLMultiplicativeExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression <em>IQL Plus Minus Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Plus Minus Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression
   * @generated
   */
  EClass getIQLPlusMinusExpression();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOp()
   * @see #getIQLPlusMinusExpression()
   * @generated
   */
  EAttribute getIQLPlusMinusExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression#getOperand()
   * @see #getIQLPlusMinusExpression()
   * @generated
   */
  EReference getIQLPlusMinusExpression_Operand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression <em>IQL Boolean Not Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Boolean Not Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression
   * @generated
   */
  EClass getIQLBooleanNotExpression();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression#getOp()
   * @see #getIQLBooleanNotExpression()
   * @generated
   */
  EAttribute getIQLBooleanNotExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression#getOperand()
   * @see #getIQLBooleanNotExpression()
   * @generated
   */
  EReference getIQLBooleanNotExpression_Operand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression <em>IQL Prefix Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Prefix Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression
   * @generated
   */
  EClass getIQLPrefixExpression();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression#getOp()
   * @see #getIQLPrefixExpression()
   * @generated
   */
  EAttribute getIQLPrefixExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression#getOperand()
   * @see #getIQLPrefixExpression()
   * @generated
   */
  EReference getIQLPrefixExpression_Operand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression <em>IQL Type Cast Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Type Cast Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression
   * @generated
   */
  EClass getIQLTypeCastExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression#getTargetRef <em>Target Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Target Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression#getTargetRef()
   * @see #getIQLTypeCastExpression()
   * @generated
   */
  EReference getIQLTypeCastExpression_TargetRef();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression#getOperand()
   * @see #getIQLTypeCastExpression()
   * @generated
   */
  EReference getIQLTypeCastExpression_Operand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression <em>IQL Postfix Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Postfix Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression
   * @generated
   */
  EClass getIQLPostfixExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression#getOperand <em>Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression#getOperand()
   * @see #getIQLPostfixExpression()
   * @generated
   */
  EReference getIQLPostfixExpression_Operand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression#getOp()
   * @see #getIQLPostfixExpression()
   * @generated
   */
  EAttribute getIQLPostfixExpression_Op();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression <em>IQL Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Array Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression
   * @generated
   */
  EClass getIQLArrayExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression#getLeftOperand()
   * @see #getIQLArrayExpression()
   * @generated
   */
  EReference getIQLArrayExpression_LeftOperand();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression#getExpressions <em>Expressions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Expressions</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression#getExpressions()
   * @see #getIQLArrayExpression()
   * @generated
   */
  EReference getIQLArrayExpression_Expressions();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression <em>IQL Member Selection Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Member Selection Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression
   * @generated
   */
  EClass getIQLMemberSelectionExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression#getLeftOperand()
   * @see #getIQLMemberSelectionExpression()
   * @generated
   */
  EReference getIQLMemberSelectionExpression_LeftOperand();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression#getSel <em>Sel</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Sel</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression#getSel()
   * @see #getIQLMemberSelectionExpression()
   * @generated
   */
  EReference getIQLMemberSelectionExpression_Sel();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression <em>IQL Jvm Element Call Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Jvm Element Call Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression
   * @generated
   */
  EClass getIQLJvmElementCallExpression();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Element</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression#getElement()
   * @see #getIQLJvmElementCallExpression()
   * @generated
   */
  EReference getIQLJvmElementCallExpression_Element();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression#getArgs <em>Args</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJvmElementCallExpression#getArgs()
   * @see #getIQLJvmElementCallExpression()
   * @generated
   */
  EReference getIQLJvmElementCallExpression_Args();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression <em>IQL This Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL This Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLThisExpression
   * @generated
   */
  EClass getIQLThisExpression();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression <em>IQL Super Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Super Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSuperExpression
   * @generated
   */
  EClass getIQLSuperExpression();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression <em>IQL Parenthesis Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Parenthesis Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression
   * @generated
   */
  EClass getIQLParenthesisExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression#getExpr <em>Expr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expr</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLParenthesisExpression#getExpr()
   * @see #getIQLParenthesisExpression()
   * @generated
   */
  EReference getIQLParenthesisExpression_Expr();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression <em>IQL New Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL New Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression
   * @generated
   */
  EClass getIQLNewExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getRef <em>Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getRef()
   * @see #getIQLNewExpression()
   * @generated
   */
  EReference getIQLNewExpression_Ref();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getArgsList <em>Args List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getArgsList()
   * @see #getIQLNewExpression()
   * @generated
   */
  EReference getIQLNewExpression_ArgsList();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getArgsMap <em>Args Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args Map</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression#getArgsMap()
   * @see #getIQLNewExpression()
   * @generated
   */
  EReference getIQLNewExpression_ArgsMap();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt <em>IQL Literal Expression Int</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Int</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt
   * @generated
   */
  EClass getIQLLiteralExpressionInt();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt#getValue()
   * @see #getIQLLiteralExpressionInt()
   * @generated
   */
  EAttribute getIQLLiteralExpressionInt_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble <em>IQL Literal Expression Double</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Double</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble
   * @generated
   */
  EClass getIQLLiteralExpressionDouble();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble#getValue()
   * @see #getIQLLiteralExpressionDouble()
   * @generated
   */
  EAttribute getIQLLiteralExpressionDouble_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString <em>IQL Literal Expression String</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression String</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString
   * @generated
   */
  EClass getIQLLiteralExpressionString();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString#getValue()
   * @see #getIQLLiteralExpressionString()
   * @generated
   */
  EAttribute getIQLLiteralExpressionString_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean <em>IQL Literal Expression Boolean</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Boolean</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean
   * @generated
   */
  EClass getIQLLiteralExpressionBoolean();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean#isValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean#isValue()
   * @see #getIQLLiteralExpressionBoolean()
   * @generated
   */
  EAttribute getIQLLiteralExpressionBoolean_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange <em>IQL Literal Expression Range</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Range</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange
   * @generated
   */
  EClass getIQLLiteralExpressionRange();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange#getValue()
   * @see #getIQLLiteralExpressionRange()
   * @generated
   */
  EAttribute getIQLLiteralExpressionRange_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType <em>IQL Literal Expression Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Type</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType
   * @generated
   */
  EClass getIQLLiteralExpressionType();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionType#getValue()
   * @see #getIQLLiteralExpressionType()
   * @generated
   */
  EReference getIQLLiteralExpressionType_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull <em>IQL Literal Expression Null</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Null</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull
   * @generated
   */
  EClass getIQLLiteralExpressionNull();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList <em>IQL Literal Expression List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList
   * @generated
   */
  EClass getIQLLiteralExpressionList();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList#getElements()
   * @see #getIQLLiteralExpressionList()
   * @generated
   */
  EReference getIQLLiteralExpressionList_Elements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap <em>IQL Literal Expression Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Literal Expression Map</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap
   * @generated
   */
  EClass getIQLLiteralExpressionMap();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap#getElements()
   * @see #getIQLLiteralExpressionMap()
   * @generated
   */
  EReference getIQLLiteralExpressionMap_Elements();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  BasicIQLFactory getBasicIQLFactory();

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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelImpl <em>IQL Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLModel()
     * @generated
     */
    EClass IQL_MODEL = eINSTANCE.getIQLModel();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_MODEL__NAME = eINSTANCE.getIQLModel_Name();

    /**
     * The meta object literal for the '<em><b>Namespaces</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MODEL__NAMESPACES = eINSTANCE.getIQLModel_Namespaces();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MODEL__ELEMENTS = eINSTANCE.getIQLModel_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl <em>IQL Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLModelElementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLModelElement()
     * @generated
     */
    EClass IQL_MODEL_ELEMENT = eINSTANCE.getIQLModelElement();

    /**
     * The meta object literal for the '<em><b>Javametadata</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MODEL_ELEMENT__JAVAMETADATA = eINSTANCE.getIQLModelElement_Javametadata();

    /**
     * The meta object literal for the '<em><b>Inner</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MODEL_ELEMENT__INNER = eINSTANCE.getIQLModelElement_Inner();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNamespaceImpl <em>IQL Namespace</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNamespaceImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLNamespace()
     * @generated
     */
    EClass IQL_NAMESPACE = eINSTANCE.getIQLNamespace();

    /**
     * The meta object literal for the '<em><b>Static</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_NAMESPACE__STATIC = eINSTANCE.getIQLNamespace_Static();

    /**
     * The meta object literal for the '<em><b>Imported Namespace</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_NAMESPACE__IMPORTED_NAMESPACE = eINSTANCE.getIQLNamespace_ImportedNamespace();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMetadataImpl <em>IQL Java Metadata</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMetadataImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaMetadata()
     * @generated
     */
    EClass IQL_JAVA_METADATA = eINSTANCE.getIQLJavaMetadata();

    /**
     * The meta object literal for the '<em><b>Java</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_JAVA_METADATA__JAVA = eINSTANCE.getIQLJavaMetadata_Java();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataListImpl <em>IQL Metadata List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataListImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataList()
     * @generated
     */
    EClass IQL_METADATA_LIST = eINSTANCE.getIQLMetadataList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_LIST__ELEMENTS = eINSTANCE.getIQLMetadataList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataImpl <em>IQL Metadata</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadata()
     * @generated
     */
    EClass IQL_METADATA = eINSTANCE.getIQLMetadata();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METADATA__NAME = eINSTANCE.getIQLMetadata_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA__VALUE = eINSTANCE.getIQLMetadata_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueImpl <em>IQL Metadata Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValue()
     * @generated
     */
    EClass IQL_METADATA_VALUE = eINSTANCE.getIQLMetadataValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapElementImpl <em>IQL Metadata Value Map Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapElementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueMapElement()
     * @generated
     */
    EClass IQL_METADATA_VALUE_MAP_ELEMENT = eINSTANCE.getIQLMetadataValueMapElement();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_VALUE_MAP_ELEMENT__KEY = eINSTANCE.getIQLMetadataValueMapElement_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_VALUE_MAP_ELEMENT__VALUE = eINSTANCE.getIQLMetadataValueMapElement_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableInitializationImpl <em>IQL Variable Initialization</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableInitializationImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableInitialization()
     * @generated
     */
    EClass IQL_VARIABLE_INITIALIZATION = eINSTANCE.getIQLVariableInitialization();

    /**
     * The meta object literal for the '<em><b>Args List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_INITIALIZATION__ARGS_LIST = eINSTANCE.getIQLVariableInitialization_ArgsList();

    /**
     * The meta object literal for the '<em><b>Args Map</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_INITIALIZATION__ARGS_MAP = eINSTANCE.getIQLVariableInitialization_ArgsMap();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_INITIALIZATION__VALUE = eINSTANCE.getIQLVariableInitialization_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsListImpl <em>IQL Arguments List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsListImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsList()
     * @generated
     */
    EClass IQL_ARGUMENTS_LIST = eINSTANCE.getIQLArgumentsList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARGUMENTS_LIST__ELEMENTS = eINSTANCE.getIQLArgumentsList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapImpl <em>IQL Arguments Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsMap()
     * @generated
     */
    EClass IQL_ARGUMENTS_MAP = eINSTANCE.getIQLArgumentsMap();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARGUMENTS_MAP__ELEMENTS = eINSTANCE.getIQLArgumentsMap_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapKeyValueImpl <em>IQL Arguments Map Key Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArgumentsMapKeyValueImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArgumentsMapKeyValue()
     * @generated
     */
    EClass IQL_ARGUMENTS_MAP_KEY_VALUE = eINSTANCE.getIQLArgumentsMapKeyValue();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARGUMENTS_MAP_KEY_VALUE__KEY = eINSTANCE.getIQLArgumentsMapKeyValue_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARGUMENTS_MAP_KEY_VALUE__VALUE = eINSTANCE.getIQLArgumentsMapKeyValue_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementImpl <em>IQL Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLStatement()
     * @generated
     */
    EClass IQL_STATEMENT = eINSTANCE.getIQLStatement();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLCasePartImpl <em>IQL Case Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLCasePartImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLCasePart()
     * @generated
     */
    EClass IQL_CASE_PART = eINSTANCE.getIQLCasePart();

    /**
     * The meta object literal for the '<em><b>Expr</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_CASE_PART__EXPR = eINSTANCE.getIQLCasePart_Expr();

    /**
     * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_CASE_PART__STATEMENTS = eINSTANCE.getIQLCasePart_Statements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl <em>IQL Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLExpression()
     * @generated
     */
    EClass IQL_EXPRESSION = eINSTANCE.getIQLExpression();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionImpl <em>IQL Member Selection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMemberSelection()
     * @generated
     */
    EClass IQL_MEMBER_SELECTION = eINSTANCE.getIQLMemberSelection();

    /**
     * The meta object literal for the '<em><b>Member</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MEMBER_SELECTION__MEMBER = eINSTANCE.getIQLMemberSelection_Member();

    /**
     * The meta object literal for the '<em><b>Args</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MEMBER_SELECTION__ARGS = eINSTANCE.getIQLMemberSelection_Args();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl <em>IQL Literal Expression Map Key Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapKeyValueImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionMapKeyValue()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE = eINSTANCE.getIQLLiteralExpressionMapKeyValue();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY = eINSTANCE.getIQLLiteralExpressionMapKeyValue_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE = eINSTANCE.getIQLLiteralExpressionMapKeyValue_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaImpl <em>IQL Java</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJava()
     * @generated
     */
    EClass IQL_JAVA = eINSTANCE.getIQLJava();

    /**
     * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_JAVA__TEXT = eINSTANCE.getIQLJava_Text();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl <em>IQL Class</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLClassImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLClass()
     * @generated
     */
    EClass IQL_CLASS = eINSTANCE.getIQLClass();

    /**
     * The meta object literal for the '<em><b>Extended Class</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_CLASS__EXTENDED_CLASS = eINSTANCE.getIQLClass_ExtendedClass();

    /**
     * The meta object literal for the '<em><b>Extended Interfaces</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_CLASS__EXTENDED_INTERFACES = eINSTANCE.getIQLClass_ExtendedInterfaces();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInterfaceImpl <em>IQL Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInterfaceImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLInterface()
     * @generated
     */
    EClass IQL_INTERFACE = eINSTANCE.getIQLInterface();

    /**
     * The meta object literal for the '<em><b>Extended Interfaces</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_INTERFACE__EXTENDED_INTERFACES = eINSTANCE.getIQLInterface_ExtendedInterfaces();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAttributeImpl <em>IQL Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAttributeImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAttribute()
     * @generated
     */
    EClass IQL_ATTRIBUTE = eINSTANCE.getIQLAttribute();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ATTRIBUTE__INIT = eINSTANCE.getIQLAttribute_Init();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSimpleTypeRefImpl <em>IQL Simple Type Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSimpleTypeRefImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSimpleTypeRef()
     * @generated
     */
    EClass IQL_SIMPLE_TYPE_REF = eINSTANCE.getIQLSimpleTypeRef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SIMPLE_TYPE_REF__TYPE = eINSTANCE.getIQLSimpleTypeRef_Type();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeRefImpl <em>IQL Array Type Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeRefImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayTypeRef()
     * @generated
     */
    EClass IQL_ARRAY_TYPE_REF = eINSTANCE.getIQLArrayTypeRef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARRAY_TYPE_REF__TYPE = eINSTANCE.getIQLArrayTypeRef_Type();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl <em>IQL Array Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayTypeImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayType()
     * @generated
     */
    EClass IQL_ARRAY_TYPE = eINSTANCE.getIQLArrayType();

    /**
     * The meta object literal for the '<em><b>Component Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARRAY_TYPE__COMPONENT_TYPE = eINSTANCE.getIQLArrayType_ComponentType();

    /**
     * The meta object literal for the '<em><b>Dimensions</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_ARRAY_TYPE__DIMENSIONS = eINSTANCE.getIQLArrayType_Dimensions();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodImpl <em>IQL Method</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMethod()
     * @generated
     */
    EClass IQL_METHOD = eINSTANCE.getIQLMethod();

    /**
     * The meta object literal for the '<em><b>Override</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METHOD__OVERRIDE = eINSTANCE.getIQLMethod_Override();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METHOD__BODY = eINSTANCE.getIQLMethod_Body();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodDeclarationImpl <em>IQL Method Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMethodDeclarationImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMethodDeclaration()
     * @generated
     */
    EClass IQL_METHOD_DECLARATION = eINSTANCE.getIQLMethodDeclaration();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMemberImpl <em>IQL Java Member</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaMemberImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaMember()
     * @generated
     */
    EClass IQL_JAVA_MEMBER = eINSTANCE.getIQLJavaMember();

    /**
     * The meta object literal for the '<em><b>Java</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_JAVA_MEMBER__JAVA = eINSTANCE.getIQLJavaMember_Java();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleIntImpl <em>IQL Metadata Value Single Int</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleIntImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleInt()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_INT = eINSTANCE.getIQLMetadataValueSingleInt();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METADATA_VALUE_SINGLE_INT__VALUE = eINSTANCE.getIQLMetadataValueSingleInt_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleDoubleImpl <em>IQL Metadata Value Single Double</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleDoubleImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleDouble()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_DOUBLE = eINSTANCE.getIQLMetadataValueSingleDouble();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METADATA_VALUE_SINGLE_DOUBLE__VALUE = eINSTANCE.getIQLMetadataValueSingleDouble_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleStringImpl <em>IQL Metadata Value Single String</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleStringImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleString()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_STRING = eINSTANCE.getIQLMetadataValueSingleString();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METADATA_VALUE_SINGLE_STRING__VALUE = eINSTANCE.getIQLMetadataValueSingleString_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleBooleanImpl <em>IQL Metadata Value Single Boolean</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleBooleanImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleBoolean()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_BOOLEAN = eINSTANCE.getIQLMetadataValueSingleBoolean();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_METADATA_VALUE_SINGLE_BOOLEAN__VALUE = eINSTANCE.getIQLMetadataValueSingleBoolean_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleTypeRefImpl <em>IQL Metadata Value Single Type Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleTypeRefImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleTypeRef()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_TYPE_REF = eINSTANCE.getIQLMetadataValueSingleTypeRef();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_VALUE_SINGLE_TYPE_REF__VALUE = eINSTANCE.getIQLMetadataValueSingleTypeRef_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleNullImpl <em>IQL Metadata Value Single Null</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueSingleNullImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueSingleNull()
     * @generated
     */
    EClass IQL_METADATA_VALUE_SINGLE_NULL = eINSTANCE.getIQLMetadataValueSingleNull();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueListImpl <em>IQL Metadata Value List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueListImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueList()
     * @generated
     */
    EClass IQL_METADATA_VALUE_LIST = eINSTANCE.getIQLMetadataValueList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_VALUE_LIST__ELEMENTS = eINSTANCE.getIQLMetadataValueList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapImpl <em>IQL Metadata Value Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMetadataValueMapImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMetadataValueMap()
     * @generated
     */
    EClass IQL_METADATA_VALUE_MAP = eINSTANCE.getIQLMetadataValueMap();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_METADATA_VALUE_MAP__ELEMENTS = eINSTANCE.getIQLMetadataValueMap_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableDeclarationImpl <em>IQL Variable Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableDeclarationImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableDeclaration()
     * @generated
     */
    EClass IQL_VARIABLE_DECLARATION = eINSTANCE.getIQLVariableDeclaration();

    /**
     * The meta object literal for the '<em><b>Ref</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_DECLARATION__REF = eINSTANCE.getIQLVariableDeclaration_Ref();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_VARIABLE_DECLARATION__NAME = eINSTANCE.getIQLVariableDeclaration_Name();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementBlockImpl <em>IQL Statement Block</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLStatementBlockImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLStatementBlock()
     * @generated
     */
    EClass IQL_STATEMENT_BLOCK = eINSTANCE.getIQLStatementBlock();

    /**
     * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_STATEMENT_BLOCK__STATEMENTS = eINSTANCE.getIQLStatementBlock_Statements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaStatementImpl <em>IQL Java Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJavaStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJavaStatement()
     * @generated
     */
    EClass IQL_JAVA_STATEMENT = eINSTANCE.getIQLJavaStatement();

    /**
     * The meta object literal for the '<em><b>Java</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_JAVA_STATEMENT__JAVA = eINSTANCE.getIQLJavaStatement_Java();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl <em>IQL If Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLIfStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLIfStatement()
     * @generated
     */
    EClass IQL_IF_STATEMENT = eINSTANCE.getIQLIfStatement();

    /**
     * The meta object literal for the '<em><b>Predicate</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_IF_STATEMENT__PREDICATE = eINSTANCE.getIQLIfStatement_Predicate();

    /**
     * The meta object literal for the '<em><b>Then Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_IF_STATEMENT__THEN_BODY = eINSTANCE.getIQLIfStatement_ThenBody();

    /**
     * The meta object literal for the '<em><b>Else Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_IF_STATEMENT__ELSE_BODY = eINSTANCE.getIQLIfStatement_ElseBody();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLWhileStatementImpl <em>IQL While Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLWhileStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLWhileStatement()
     * @generated
     */
    EClass IQL_WHILE_STATEMENT = eINSTANCE.getIQLWhileStatement();

    /**
     * The meta object literal for the '<em><b>Predicate</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_WHILE_STATEMENT__PREDICATE = eINSTANCE.getIQLWhileStatement_Predicate();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_WHILE_STATEMENT__BODY = eINSTANCE.getIQLWhileStatement_Body();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLDoWhileStatementImpl <em>IQL Do While Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLDoWhileStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLDoWhileStatement()
     * @generated
     */
    EClass IQL_DO_WHILE_STATEMENT = eINSTANCE.getIQLDoWhileStatement();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_DO_WHILE_STATEMENT__BODY = eINSTANCE.getIQLDoWhileStatement_Body();

    /**
     * The meta object literal for the '<em><b>Predicate</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_DO_WHILE_STATEMENT__PREDICATE = eINSTANCE.getIQLDoWhileStatement_Predicate();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl <em>IQL For Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLForStatement()
     * @generated
     */
    EClass IQL_FOR_STATEMENT = eINSTANCE.getIQLForStatement();

    /**
     * The meta object literal for the '<em><b>Var</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_STATEMENT__VAR = eINSTANCE.getIQLForStatement_Var();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_STATEMENT__VALUE = eINSTANCE.getIQLForStatement_Value();

    /**
     * The meta object literal for the '<em><b>Predicate</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_STATEMENT__PREDICATE = eINSTANCE.getIQLForStatement_Predicate();

    /**
     * The meta object literal for the '<em><b>Update Expr</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_STATEMENT__UPDATE_EXPR = eINSTANCE.getIQLForStatement_UpdateExpr();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_STATEMENT__BODY = eINSTANCE.getIQLForStatement_Body();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl <em>IQL For Each Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLForEachStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLForEachStatement()
     * @generated
     */
    EClass IQL_FOR_EACH_STATEMENT = eINSTANCE.getIQLForEachStatement();

    /**
     * The meta object literal for the '<em><b>Var</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_EACH_STATEMENT__VAR = eINSTANCE.getIQLForEachStatement_Var();

    /**
     * The meta object literal for the '<em><b>For Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION = eINSTANCE.getIQLForEachStatement_ForExpression();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_FOR_EACH_STATEMENT__BODY = eINSTANCE.getIQLForEachStatement_Body();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSwitchStatementImpl <em>IQL Switch Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSwitchStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSwitchStatement()
     * @generated
     */
    EClass IQL_SWITCH_STATEMENT = eINSTANCE.getIQLSwitchStatement();

    /**
     * The meta object literal for the '<em><b>Expr</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SWITCH_STATEMENT__EXPR = eINSTANCE.getIQLSwitchStatement_Expr();

    /**
     * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SWITCH_STATEMENT__CASES = eINSTANCE.getIQLSwitchStatement_Cases();

    /**
     * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SWITCH_STATEMENT__STATEMENTS = eINSTANCE.getIQLSwitchStatement_Statements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionStatementImpl <em>IQL Expression Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLExpressionStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLExpressionStatement()
     * @generated
     */
    EClass IQL_EXPRESSION_STATEMENT = eINSTANCE.getIQLExpressionStatement();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_EXPRESSION_STATEMENT__EXPRESSION = eINSTANCE.getIQLExpressionStatement_Expression();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableStatementImpl <em>IQL Variable Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLVariableStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLVariableStatement()
     * @generated
     */
    EClass IQL_VARIABLE_STATEMENT = eINSTANCE.getIQLVariableStatement();

    /**
     * The meta object literal for the '<em><b>Var</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_STATEMENT__VAR = eINSTANCE.getIQLVariableStatement_Var();

    /**
     * The meta object literal for the '<em><b>Init</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_VARIABLE_STATEMENT__INIT = eINSTANCE.getIQLVariableStatement_Init();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl <em>IQL Constructor Call Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLConstructorCallStatement()
     * @generated
     */
    EClass IQL_CONSTRUCTOR_CALL_STATEMENT = eINSTANCE.getIQLConstructorCallStatement();

    /**
     * The meta object literal for the '<em><b>This</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_CONSTRUCTOR_CALL_STATEMENT__THIS = eINSTANCE.getIQLConstructorCallStatement_This();

    /**
     * The meta object literal for the '<em><b>Super</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER = eINSTANCE.getIQLConstructorCallStatement_Super();

    /**
     * The meta object literal for the '<em><b>Args</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS = eINSTANCE.getIQLConstructorCallStatement_Args();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBreakStatementImpl <em>IQL Break Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBreakStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLBreakStatement()
     * @generated
     */
    EClass IQL_BREAK_STATEMENT = eINSTANCE.getIQLBreakStatement();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLContinueStatementImpl <em>IQL Continue Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLContinueStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLContinueStatement()
     * @generated
     */
    EClass IQL_CONTINUE_STATEMENT = eINSTANCE.getIQLContinueStatement();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLReturnStatementImpl <em>IQL Return Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLReturnStatementImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLReturnStatement()
     * @generated
     */
    EClass IQL_RETURN_STATEMENT = eINSTANCE.getIQLReturnStatement();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_RETURN_STATEMENT__EXPRESSION = eINSTANCE.getIQLReturnStatement_Expression();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAssignmentExpressionImpl <em>IQL Assignment Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAssignmentExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAssignmentExpression()
     * @generated
     */
    EClass IQL_ASSIGNMENT_EXPRESSION = eINSTANCE.getIQLAssignmentExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ASSIGNMENT_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLAssignmentExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_ASSIGNMENT_EXPRESSION__OP = eINSTANCE.getIQLAssignmentExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ASSIGNMENT_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLAssignmentExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalOrExpressionImpl <em>IQL Logical Or Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalOrExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLogicalOrExpression()
     * @generated
     */
    EClass IQL_LOGICAL_OR_EXPRESSION = eINSTANCE.getIQLLogicalOrExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LOGICAL_OR_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLLogicalOrExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LOGICAL_OR_EXPRESSION__OP = eINSTANCE.getIQLLogicalOrExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LOGICAL_OR_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLLogicalOrExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalAndExpressionImpl <em>IQL Logical And Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLogicalAndExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLogicalAndExpression()
     * @generated
     */
    EClass IQL_LOGICAL_AND_EXPRESSION = eINSTANCE.getIQLLogicalAndExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LOGICAL_AND_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLLogicalAndExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LOGICAL_AND_EXPRESSION__OP = eINSTANCE.getIQLLogicalAndExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LOGICAL_AND_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLLogicalAndExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLEqualityExpressionImpl <em>IQL Equality Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLEqualityExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLEqualityExpression()
     * @generated
     */
    EClass IQL_EQUALITY_EXPRESSION = eINSTANCE.getIQLEqualityExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_EQUALITY_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLEqualityExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_EQUALITY_EXPRESSION__OP = eINSTANCE.getIQLEqualityExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_EQUALITY_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLEqualityExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInstanceOfExpressionImpl <em>IQL Instance Of Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLInstanceOfExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLInstanceOfExpression()
     * @generated
     */
    EClass IQL_INSTANCE_OF_EXPRESSION = eINSTANCE.getIQLInstanceOfExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLInstanceOfExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Target Ref</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_INSTANCE_OF_EXPRESSION__TARGET_REF = eINSTANCE.getIQLInstanceOfExpression_TargetRef();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLRelationalExpressionImpl <em>IQL Relational Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLRelationalExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLRelationalExpression()
     * @generated
     */
    EClass IQL_RELATIONAL_EXPRESSION = eINSTANCE.getIQLRelationalExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLRelationalExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_RELATIONAL_EXPRESSION__OP = eINSTANCE.getIQLRelationalExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLRelationalExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAdditiveExpressionImpl <em>IQL Additive Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLAdditiveExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLAdditiveExpression()
     * @generated
     */
    EClass IQL_ADDITIVE_EXPRESSION = eINSTANCE.getIQLAdditiveExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ADDITIVE_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLAdditiveExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_ADDITIVE_EXPRESSION__OP = eINSTANCE.getIQLAdditiveExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ADDITIVE_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLAdditiveExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMultiplicativeExpressionImpl <em>IQL Multiplicative Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMultiplicativeExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMultiplicativeExpression()
     * @generated
     */
    EClass IQL_MULTIPLICATIVE_EXPRESSION = eINSTANCE.getIQLMultiplicativeExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MULTIPLICATIVE_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLMultiplicativeExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_MULTIPLICATIVE_EXPRESSION__OP = eINSTANCE.getIQLMultiplicativeExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MULTIPLICATIVE_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLMultiplicativeExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPlusMinusExpressionImpl <em>IQL Plus Minus Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPlusMinusExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPlusMinusExpression()
     * @generated
     */
    EClass IQL_PLUS_MINUS_EXPRESSION = eINSTANCE.getIQLPlusMinusExpression();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_PLUS_MINUS_EXPRESSION__OP = eINSTANCE.getIQLPlusMinusExpression_Op();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_PLUS_MINUS_EXPRESSION__OPERAND = eINSTANCE.getIQLPlusMinusExpression_Operand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBooleanNotExpressionImpl <em>IQL Boolean Not Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLBooleanNotExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLBooleanNotExpression()
     * @generated
     */
    EClass IQL_BOOLEAN_NOT_EXPRESSION = eINSTANCE.getIQLBooleanNotExpression();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_BOOLEAN_NOT_EXPRESSION__OP = eINSTANCE.getIQLBooleanNotExpression_Op();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_BOOLEAN_NOT_EXPRESSION__OPERAND = eINSTANCE.getIQLBooleanNotExpression_Operand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPrefixExpressionImpl <em>IQL Prefix Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPrefixExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPrefixExpression()
     * @generated
     */
    EClass IQL_PREFIX_EXPRESSION = eINSTANCE.getIQLPrefixExpression();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_PREFIX_EXPRESSION__OP = eINSTANCE.getIQLPrefixExpression_Op();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_PREFIX_EXPRESSION__OPERAND = eINSTANCE.getIQLPrefixExpression_Operand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl <em>IQL Type Cast Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLTypeCastExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLTypeCastExpression()
     * @generated
     */
    EClass IQL_TYPE_CAST_EXPRESSION = eINSTANCE.getIQLTypeCastExpression();

    /**
     * The meta object literal for the '<em><b>Target Ref</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_TYPE_CAST_EXPRESSION__TARGET_REF = eINSTANCE.getIQLTypeCastExpression_TargetRef();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_TYPE_CAST_EXPRESSION__OPERAND = eINSTANCE.getIQLTypeCastExpression_Operand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPostfixExpressionImpl <em>IQL Postfix Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLPostfixExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLPostfixExpression()
     * @generated
     */
    EClass IQL_POSTFIX_EXPRESSION = eINSTANCE.getIQLPostfixExpression();

    /**
     * The meta object literal for the '<em><b>Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_POSTFIX_EXPRESSION__OPERAND = eINSTANCE.getIQLPostfixExpression_Operand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_POSTFIX_EXPRESSION__OP = eINSTANCE.getIQLPostfixExpression_Op();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayExpressionImpl <em>IQL Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLArrayExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLArrayExpression()
     * @generated
     */
    EClass IQL_ARRAY_EXPRESSION = eINSTANCE.getIQLArrayExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARRAY_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLArrayExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Expressions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_ARRAY_EXPRESSION__EXPRESSIONS = eINSTANCE.getIQLArrayExpression_Expressions();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl <em>IQL Member Selection Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLMemberSelectionExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLMemberSelectionExpression()
     * @generated
     */
    EClass IQL_MEMBER_SELECTION_EXPRESSION = eINSTANCE.getIQLMemberSelectionExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLMemberSelectionExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Sel</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_MEMBER_SELECTION_EXPRESSION__SEL = eINSTANCE.getIQLMemberSelectionExpression_Sel();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJvmElementCallExpressionImpl <em>IQL Jvm Element Call Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLJvmElementCallExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLJvmElementCallExpression()
     * @generated
     */
    EClass IQL_JVM_ELEMENT_CALL_EXPRESSION = eINSTANCE.getIQLJvmElementCallExpression();

    /**
     * The meta object literal for the '<em><b>Element</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_JVM_ELEMENT_CALL_EXPRESSION__ELEMENT = eINSTANCE.getIQLJvmElementCallExpression_Element();

    /**
     * The meta object literal for the '<em><b>Args</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_JVM_ELEMENT_CALL_EXPRESSION__ARGS = eINSTANCE.getIQLJvmElementCallExpression_Args();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLThisExpressionImpl <em>IQL This Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLThisExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLThisExpression()
     * @generated
     */
    EClass IQL_THIS_EXPRESSION = eINSTANCE.getIQLThisExpression();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSuperExpressionImpl <em>IQL Super Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLSuperExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLSuperExpression()
     * @generated
     */
    EClass IQL_SUPER_EXPRESSION = eINSTANCE.getIQLSuperExpression();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLParenthesisExpressionImpl <em>IQL Parenthesis Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLParenthesisExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLParenthesisExpression()
     * @generated
     */
    EClass IQL_PARENTHESIS_EXPRESSION = eINSTANCE.getIQLParenthesisExpression();

    /**
     * The meta object literal for the '<em><b>Expr</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_PARENTHESIS_EXPRESSION__EXPR = eINSTANCE.getIQLParenthesisExpression_Expr();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl <em>IQL New Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLNewExpression()
     * @generated
     */
    EClass IQL_NEW_EXPRESSION = eINSTANCE.getIQLNewExpression();

    /**
     * The meta object literal for the '<em><b>Ref</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_NEW_EXPRESSION__REF = eINSTANCE.getIQLNewExpression_Ref();

    /**
     * The meta object literal for the '<em><b>Args List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_NEW_EXPRESSION__ARGS_LIST = eINSTANCE.getIQLNewExpression_ArgsList();

    /**
     * The meta object literal for the '<em><b>Args Map</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_NEW_EXPRESSION__ARGS_MAP = eINSTANCE.getIQLNewExpression_ArgsMap();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionIntImpl <em>IQL Literal Expression Int</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionIntImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionInt()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_INT = eINSTANCE.getIQLLiteralExpressionInt();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LITERAL_EXPRESSION_INT__VALUE = eINSTANCE.getIQLLiteralExpressionInt_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionDoubleImpl <em>IQL Literal Expression Double</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionDoubleImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionDouble()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_DOUBLE = eINSTANCE.getIQLLiteralExpressionDouble();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LITERAL_EXPRESSION_DOUBLE__VALUE = eINSTANCE.getIQLLiteralExpressionDouble_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionStringImpl <em>IQL Literal Expression String</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionStringImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionString()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_STRING = eINSTANCE.getIQLLiteralExpressionString();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LITERAL_EXPRESSION_STRING__VALUE = eINSTANCE.getIQLLiteralExpressionString_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionBooleanImpl <em>IQL Literal Expression Boolean</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionBooleanImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionBoolean()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_BOOLEAN = eINSTANCE.getIQLLiteralExpressionBoolean();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LITERAL_EXPRESSION_BOOLEAN__VALUE = eINSTANCE.getIQLLiteralExpressionBoolean_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionRangeImpl <em>IQL Literal Expression Range</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionRangeImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionRange()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_RANGE = eINSTANCE.getIQLLiteralExpressionRange();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_LITERAL_EXPRESSION_RANGE__VALUE = eINSTANCE.getIQLLiteralExpressionRange_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionTypeImpl <em>IQL Literal Expression Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionTypeImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionType()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_TYPE = eINSTANCE.getIQLLiteralExpressionType();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LITERAL_EXPRESSION_TYPE__VALUE = eINSTANCE.getIQLLiteralExpressionType_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionNullImpl <em>IQL Literal Expression Null</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionNullImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionNull()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_NULL = eINSTANCE.getIQLLiteralExpressionNull();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionListImpl <em>IQL Literal Expression List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionListImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionList()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_LIST = eINSTANCE.getIQLLiteralExpressionList();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LITERAL_EXPRESSION_LIST__ELEMENTS = eINSTANCE.getIQLLiteralExpressionList_Elements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapImpl <em>IQL Literal Expression Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLLiteralExpressionMapImpl
     * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.BasicIQLPackageImpl#getIQLLiteralExpressionMap()
     * @generated
     */
    EClass IQL_LITERAL_EXPRESSION_MAP = eINSTANCE.getIQLLiteralExpressionMap();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_LITERAL_EXPRESSION_MAP__ELEMENTS = eINSTANCE.getIQLLiteralExpressionMap_Elements();

  }

} //BasicIQLPackage
