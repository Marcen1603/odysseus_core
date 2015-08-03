/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;

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
 * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFactory
 * @model kind="package"
 * @generated
 */
public interface ODLPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "oDL";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/iql/odl/ODL";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "oDL";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ODLPackage eINSTANCE = de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLFileImpl <em>File</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLFileImpl
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLFile()
   * @generated
   */
  int ODL_FILE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_FILE__NAME = BasicIQLPackage.IQL_FILE__NAME;

  /**
   * The feature id for the '<em><b>Namespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_FILE__NAMESPACES = BasicIQLPackage.IQL_FILE__NAMESPACES;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_FILE__ELEMENTS = BasicIQLPackage.IQL_FILE__ELEMENTS;

  /**
   * The number of structural features of the '<em>File</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_FILE_FEATURE_COUNT = BasicIQLPackage.IQL_FILE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLTypeDefinitionImpl <em>Type Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLTypeDefinitionImpl
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLTypeDefinition()
   * @generated
   */
  int ODL_TYPE_DEFINITION = 1;

  /**
   * The feature id for the '<em><b>Javametadata</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_TYPE_DEFINITION__JAVAMETADATA = BasicIQLPackage.IQL_TYPE_DEFINITION__JAVAMETADATA;

  /**
   * The feature id for the '<em><b>Inner</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_TYPE_DEFINITION__INNER = BasicIQLPackage.IQL_TYPE_DEFINITION__INNER;

  /**
   * The number of structural features of the '<em>Type Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_TYPE_DEFINITION_FEATURE_COUNT = BasicIQLPackage.IQL_TYPE_DEFINITION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLOperatorImpl <em>Operator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLOperatorImpl
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLOperator()
   * @generated
   */
  int ODL_OPERATOR = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__ANNOTATIONS = TypesPackage.JVM_GENERIC_TYPE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__DECLARING_TYPE = TypesPackage.JVM_GENERIC_TYPE__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__VISIBILITY = TypesPackage.JVM_GENERIC_TYPE__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__SIMPLE_NAME = TypesPackage.JVM_GENERIC_TYPE__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__IDENTIFIER = TypesPackage.JVM_GENERIC_TYPE__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__DEPRECATED = TypesPackage.JVM_GENERIC_TYPE__DEPRECATED;

  /**
   * The feature id for the '<em><b>Array Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__ARRAY_TYPE = TypesPackage.JVM_GENERIC_TYPE__ARRAY_TYPE;

  /**
   * The feature id for the '<em><b>Super Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__SUPER_TYPES = TypesPackage.JVM_GENERIC_TYPE__SUPER_TYPES;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__MEMBERS = TypesPackage.JVM_GENERIC_TYPE__MEMBERS;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__ABSTRACT = TypesPackage.JVM_GENERIC_TYPE__ABSTRACT;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__STATIC = TypesPackage.JVM_GENERIC_TYPE__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__FINAL = TypesPackage.JVM_GENERIC_TYPE__FINAL;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__PACKAGE_NAME = TypesPackage.JVM_GENERIC_TYPE__PACKAGE_NAME;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__TYPE_PARAMETERS = TypesPackage.JVM_GENERIC_TYPE__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__INTERFACE = TypesPackage.JVM_GENERIC_TYPE__INTERFACE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__STRICT_FLOATING_POINT = TypesPackage.JVM_GENERIC_TYPE__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Anonymous</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__ANONYMOUS = TypesPackage.JVM_GENERIC_TYPE__ANONYMOUS;

  /**
   * The feature id for the '<em><b>Metadata List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR__METADATA_LIST = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Operator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_OPERATOR_FEATURE_COUNT = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl <em>Parameter</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLParameter()
   * @generated
   */
  int ODL_PARAMETER = 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__ANNOTATIONS = BasicIQLPackage.IQL_ATTRIBUTE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__DECLARING_TYPE = BasicIQLPackage.IQL_ATTRIBUTE__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__VISIBILITY = BasicIQLPackage.IQL_ATTRIBUTE__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__SIMPLE_NAME = BasicIQLPackage.IQL_ATTRIBUTE__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__IDENTIFIER = BasicIQLPackage.IQL_ATTRIBUTE__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__DEPRECATED = BasicIQLPackage.IQL_ATTRIBUTE__DEPRECATED;

  /**
   * The feature id for the '<em><b>Local Classes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__LOCAL_CLASSES = BasicIQLPackage.IQL_ATTRIBUTE__LOCAL_CLASSES;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__STATIC = BasicIQLPackage.IQL_ATTRIBUTE__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__FINAL = BasicIQLPackage.IQL_ATTRIBUTE__FINAL;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__TYPE = BasicIQLPackage.IQL_ATTRIBUTE__TYPE;

  /**
   * The feature id for the '<em><b>Volatile</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__VOLATILE = BasicIQLPackage.IQL_ATTRIBUTE__VOLATILE;

  /**
   * The feature id for the '<em><b>Transient</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__TRANSIENT = BasicIQLPackage.IQL_ATTRIBUTE__TRANSIENT;

  /**
   * The feature id for the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__CONSTANT = BasicIQLPackage.IQL_ATTRIBUTE__CONSTANT;

  /**
   * The feature id for the '<em><b>Constant Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__CONSTANT_VALUE = BasicIQLPackage.IQL_ATTRIBUTE__CONSTANT_VALUE;

  /**
   * The feature id for the '<em><b>Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__INIT = BasicIQLPackage.IQL_ATTRIBUTE__INIT;

  /**
   * The feature id for the '<em><b>Optional</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__OPTIONAL = BasicIQLPackage.IQL_ATTRIBUTE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Parameter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__PARAMETER = BasicIQLPackage.IQL_ATTRIBUTE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Metadata List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER__METADATA_LIST = BasicIQLPackage.IQL_ATTRIBUTE_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Parameter</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_PARAMETER_FEATURE_COUNT = BasicIQLPackage.IQL_ATTRIBUTE_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl <em>Method</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLMethod()
   * @generated
   */
  int ODL_METHOD = 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__ANNOTATIONS = BasicIQLPackage.IQL_METHOD__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__DECLARING_TYPE = BasicIQLPackage.IQL_METHOD__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__VISIBILITY = BasicIQLPackage.IQL_METHOD__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__SIMPLE_NAME = BasicIQLPackage.IQL_METHOD__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__IDENTIFIER = BasicIQLPackage.IQL_METHOD__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__DEPRECATED = BasicIQLPackage.IQL_METHOD__DEPRECATED;

  /**
   * The feature id for the '<em><b>Local Classes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__LOCAL_CLASSES = BasicIQLPackage.IQL_METHOD__LOCAL_CLASSES;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__TYPE_PARAMETERS = BasicIQLPackage.IQL_METHOD__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__PARAMETERS = BasicIQLPackage.IQL_METHOD__PARAMETERS;

  /**
   * The feature id for the '<em><b>Exceptions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__EXCEPTIONS = BasicIQLPackage.IQL_METHOD__EXCEPTIONS;

  /**
   * The feature id for the '<em><b>Var Args</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__VAR_ARGS = BasicIQLPackage.IQL_METHOD__VAR_ARGS;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__STATIC = BasicIQLPackage.IQL_METHOD__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__FINAL = BasicIQLPackage.IQL_METHOD__FINAL;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__ABSTRACT = BasicIQLPackage.IQL_METHOD__ABSTRACT;

  /**
   * The feature id for the '<em><b>Return Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__RETURN_TYPE = BasicIQLPackage.IQL_METHOD__RETURN_TYPE;

  /**
   * The feature id for the '<em><b>Default Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__DEFAULT_VALUE = BasicIQLPackage.IQL_METHOD__DEFAULT_VALUE;

  /**
   * The feature id for the '<em><b>Synchronized</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__SYNCHRONIZED = BasicIQLPackage.IQL_METHOD__SYNCHRONIZED;

  /**
   * The feature id for the '<em><b>Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__DEFAULT = BasicIQLPackage.IQL_METHOD__DEFAULT;

  /**
   * The feature id for the '<em><b>Native</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__NATIVE = BasicIQLPackage.IQL_METHOD__NATIVE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__STRICT_FLOATING_POINT = BasicIQLPackage.IQL_METHOD__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Override</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__OVERRIDE = BasicIQLPackage.IQL_METHOD__OVERRIDE;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__BODY = BasicIQLPackage.IQL_METHOD__BODY;

  /**
   * The feature id for the '<em><b>On</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__ON = BasicIQLPackage.IQL_METHOD_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Validate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD__VALIDATE = BasicIQLPackage.IQL_METHOD_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Method</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ODL_METHOD_FEATURE_COUNT = BasicIQLPackage.IQL_METHOD_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFile <em>File</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFile
   * @generated
   */
  EClass getODLFile();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLTypeDefinition <em>Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type Definition</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLTypeDefinition
   * @generated
   */
  EClass getODLTypeDefinition();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator
   * @generated
   */
  EClass getODLOperator();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator#getMetadataList <em>Metadata List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Metadata List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator#getMetadataList()
   * @see #getODLOperator()
   * @generated
   */
  EReference getODLOperator_MetadataList();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter
   * @generated
   */
  EClass getODLParameter();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isOptional <em>Optional</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Optional</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isOptional()
   * @see #getODLParameter()
   * @generated
   */
  EAttribute getODLParameter_Optional();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isParameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parameter</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#isParameter()
   * @see #getODLParameter()
   * @generated
   */
  EAttribute getODLParameter_Parameter();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#getMetadataList <em>Metadata List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Metadata List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter#getMetadataList()
   * @see #getODLParameter()
   * @generated
   */
  EReference getODLParameter_MetadataList();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod <em>Method</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Method</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod
   * @generated
   */
  EClass getODLMethod();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isOn <em>On</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>On</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isOn()
   * @see #getODLMethod()
   * @generated
   */
  EAttribute getODLMethod_On();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isValidate <em>Validate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Validate</em>'.
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod#isValidate()
   * @see #getODLMethod()
   * @generated
   */
  EAttribute getODLMethod_Validate();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ODLFactory getODLFactory();

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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLFileImpl <em>File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLFileImpl
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLFile()
     * @generated
     */
    EClass ODL_FILE = eINSTANCE.getODLFile();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLTypeDefinitionImpl <em>Type Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLTypeDefinitionImpl
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLTypeDefinition()
     * @generated
     */
    EClass ODL_TYPE_DEFINITION = eINSTANCE.getODLTypeDefinition();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLOperatorImpl <em>Operator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLOperatorImpl
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLOperator()
     * @generated
     */
    EClass ODL_OPERATOR = eINSTANCE.getODLOperator();

    /**
     * The meta object literal for the '<em><b>Metadata List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ODL_OPERATOR__METADATA_LIST = eINSTANCE.getODLOperator_MetadataList();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl <em>Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLParameterImpl
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLParameter()
     * @generated
     */
    EClass ODL_PARAMETER = eINSTANCE.getODLParameter();

    /**
     * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ODL_PARAMETER__OPTIONAL = eINSTANCE.getODLParameter_Optional();

    /**
     * The meta object literal for the '<em><b>Parameter</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ODL_PARAMETER__PARAMETER = eINSTANCE.getODLParameter_Parameter();

    /**
     * The meta object literal for the '<em><b>Metadata List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ODL_PARAMETER__METADATA_LIST = eINSTANCE.getODLParameter_MetadataList();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl <em>Method</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLMethodImpl
     * @see de.uniol.inf.is.odysseus.iql.odl.oDL.impl.ODLPackageImpl#getODLMethod()
     * @generated
     */
    EClass ODL_METHOD = eINSTANCE.getODLMethod();

    /**
     * The meta object literal for the '<em><b>On</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ODL_METHOD__ON = eINSTANCE.getODLMethod_On();

    /**
     * The meta object literal for the '<em><b>Validate</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ODL_METHOD__VALIDATE = eINSTANCE.getODLMethod_Validate();

  }

} //ODLPackage
