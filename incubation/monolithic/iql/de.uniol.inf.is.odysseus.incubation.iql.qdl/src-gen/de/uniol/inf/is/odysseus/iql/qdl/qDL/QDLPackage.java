/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

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
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFactory
 * @model kind="package"
 * @generated
 */
public interface QDLPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "qDL";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/iql/qdl/QDL";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "qDL";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  QDLPackage eINSTANCE = de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLModel()
   * @generated
   */
  int QDL_MODEL = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL__NAME = BasicIQLPackage.IQL_MODEL__NAME;

  /**
   * The feature id for the '<em><b>Namespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL__NAMESPACES = BasicIQLPackage.IQL_MODEL__NAMESPACES;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL__ELEMENTS = BasicIQLPackage.IQL_MODEL__ELEMENTS;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL_FEATURE_COUNT = BasicIQLPackage.IQL_MODEL_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelElementImpl <em>Model Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelElementImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLModelElement()
   * @generated
   */
  int QDL_MODEL_ELEMENT = 1;

  /**
   * The feature id for the '<em><b>Javametadata</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL_ELEMENT__JAVAMETADATA = BasicIQLPackage.IQL_MODEL_ELEMENT__JAVAMETADATA;

  /**
   * The feature id for the '<em><b>Inner</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL_ELEMENT__INNER = BasicIQLPackage.IQL_MODEL_ELEMENT__INNER;

  /**
   * The number of structural features of the '<em>Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_MODEL_ELEMENT_FEATURE_COUNT = BasicIQLPackage.IQL_MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl <em>Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLQuery()
   * @generated
   */
  int QDL_QUERY = 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ANNOTATIONS = TypesPackage.JVM_GENERIC_TYPE__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__DECLARING_TYPE = TypesPackage.JVM_GENERIC_TYPE__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__VISIBILITY = TypesPackage.JVM_GENERIC_TYPE__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__SIMPLE_NAME = TypesPackage.JVM_GENERIC_TYPE__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__IDENTIFIER = TypesPackage.JVM_GENERIC_TYPE__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__DEPRECATED = TypesPackage.JVM_GENERIC_TYPE__DEPRECATED;

  /**
   * The feature id for the '<em><b>Array Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ARRAY_TYPE = TypesPackage.JVM_GENERIC_TYPE__ARRAY_TYPE;

  /**
   * The feature id for the '<em><b>Super Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__SUPER_TYPES = TypesPackage.JVM_GENERIC_TYPE__SUPER_TYPES;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__MEMBERS = TypesPackage.JVM_GENERIC_TYPE__MEMBERS;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ABSTRACT = TypesPackage.JVM_GENERIC_TYPE__ABSTRACT;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STATIC = TypesPackage.JVM_GENERIC_TYPE__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__FINAL = TypesPackage.JVM_GENERIC_TYPE__FINAL;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__PACKAGE_NAME = TypesPackage.JVM_GENERIC_TYPE__PACKAGE_NAME;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__TYPE_PARAMETERS = TypesPackage.JVM_GENERIC_TYPE__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__INTERFACE = TypesPackage.JVM_GENERIC_TYPE__INTERFACE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STRICT_FLOATING_POINT = TypesPackage.JVM_GENERIC_TYPE__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Anonymous</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ANONYMOUS = TypesPackage.JVM_GENERIC_TYPE__ANONYMOUS;

  /**
   * The feature id for the '<em><b>Metadata List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__METADATA_LIST = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STATEMENTS = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY_FEATURE_COUNT = TypesPackage.JVM_GENERIC_TYPE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl <em>IQL Instance Of Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLInstanceOfExpression()
   * @generated
   */
  int IQL_INSTANCE_OF_EXPRESSION = 3;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target Ref</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION__TARGET_REF = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>IQL Instance Of Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_INSTANCE_OF_EXPRESSION_FEATURE_COUNT = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl <em>IQL Relational Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLRelationalExpression()
   * @generated
   */
  int IQL_RELATIONAL_EXPRESSION = 4;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__OP = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Relational Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_RELATIONAL_EXPRESSION_FEATURE_COUNT = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl <em>IQL Subscribe Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLSubscribeExpression()
   * @generated
   */
  int IQL_SUBSCRIBE_EXPRESSION = 5;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SUBSCRIBE_EXPRESSION__LEFT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SUBSCRIBE_EXPRESSION__OP = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SUBSCRIBE_EXPRESSION__RIGHT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Subscribe Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_SUBSCRIBE_EXPRESSION_FEATURE_COUNT = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLPortExpressionImpl <em>IQL Port Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLPortExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLPortExpression()
   * @generated
   */
  int IQL_PORT_EXPRESSION = 6;

  /**
   * The feature id for the '<em><b>Left Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PORT_EXPRESSION__LEFT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PORT_EXPRESSION__OP = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PORT_EXPRESSION__RIGHT_OPERAND = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>IQL Port Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IQL_PORT_EXPRESSION_FEATURE_COUNT = BasicIQLPackage.IQL_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLMetadataValueSingleIDImpl <em>Metadata Value Single ID</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLMetadataValueSingleIDImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLMetadataValueSingleID()
   * @generated
   */
  int QDL_METADATA_VALUE_SINGLE_ID = 7;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_METADATA_VALUE_SINGLE_ID__VALUE = BasicIQLPackage.IQL_METADATA_VALUE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Metadata Value Single ID</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_METADATA_VALUE_SINGLE_ID_FEATURE_COUNT = BasicIQLPackage.IQL_METADATA_VALUE_FEATURE_COUNT + 1;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel
   * @generated
   */
  EClass getQDLModel();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Element</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModelElement
   * @generated
   */
  EClass getQDLModelElement();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Query</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery
   * @generated
   */
  EClass getQDLQuery();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getMetadataList <em>Metadata List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Metadata List</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getMetadataList()
   * @see #getQDLQuery()
   * @generated
   */
  EReference getQDLQuery_MetadataList();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getStatements <em>Statements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Statements</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery#getStatements()
   * @see #getQDLQuery()
   * @generated
   */
  EReference getQDLQuery_Statements();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression <em>IQL Instance Of Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Instance Of Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression
   * @generated
   */
  EClass getIQLInstanceOfExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression#getLeftOperand()
   * @see #getIQLInstanceOfExpression()
   * @generated
   */
  EReference getIQLInstanceOfExpression_LeftOperand();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression#getTargetRef <em>Target Ref</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Target Ref</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLInstanceOfExpression#getTargetRef()
   * @see #getIQLInstanceOfExpression()
   * @generated
   */
  EReference getIQLInstanceOfExpression_TargetRef();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression <em>IQL Relational Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Relational Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression
   * @generated
   */
  EClass getIQLRelationalExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getLeftOperand()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EReference getIQLRelationalExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getOp()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EAttribute getIQLRelationalExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLRelationalExpression#getRightOperand()
   * @see #getIQLRelationalExpression()
   * @generated
   */
  EReference getIQLRelationalExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression <em>IQL Subscribe Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Subscribe Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression
   * @generated
   */
  EClass getIQLSubscribeExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getLeftOperand()
   * @see #getIQLSubscribeExpression()
   * @generated
   */
  EReference getIQLSubscribeExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getOp()
   * @see #getIQLSubscribeExpression()
   * @generated
   */
  EAttribute getIQLSubscribeExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression#getRightOperand()
   * @see #getIQLSubscribeExpression()
   * @generated
   */
  EReference getIQLSubscribeExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression <em>IQL Port Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IQL Port Expression</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression
   * @generated
   */
  EClass getIQLPortExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getLeftOperand <em>Left Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getLeftOperand()
   * @see #getIQLPortExpression()
   * @generated
   */
  EReference getIQLPortExpression_LeftOperand();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getOp()
   * @see #getIQLPortExpression()
   * @generated
   */
  EAttribute getIQLPortExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getRightOperand <em>Right Operand</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right Operand</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression#getRightOperand()
   * @see #getIQLPortExpression()
   * @generated
   */
  EReference getIQLPortExpression_RightOperand();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID <em>Metadata Value Single ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Metadata Value Single ID</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID
   * @generated
   */
  EClass getQDLMetadataValueSingleID();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID#getValue()
   * @see #getQDLMetadataValueSingleID()
   * @generated
   */
  EAttribute getQDLMetadataValueSingleID_Value();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  QDLFactory getQDLFactory();

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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLModel()
     * @generated
     */
    EClass QDL_MODEL = eINSTANCE.getQDLModel();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelElementImpl <em>Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLModelElementImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLModelElement()
     * @generated
     */
    EClass QDL_MODEL_ELEMENT = eINSTANCE.getQDLModelElement();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl <em>Query</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLQuery()
     * @generated
     */
    EClass QDL_QUERY = eINSTANCE.getQDLQuery();

    /**
     * The meta object literal for the '<em><b>Metadata List</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QDL_QUERY__METADATA_LIST = eINSTANCE.getQDLQuery_MetadataList();

    /**
     * The meta object literal for the '<em><b>Statements</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QDL_QUERY__STATEMENTS = eINSTANCE.getQDLQuery_Statements();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl <em>IQL Instance Of Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLInstanceOfExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLInstanceOfExpression()
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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl <em>IQL Relational Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLRelationalExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLRelationalExpression()
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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl <em>IQL Subscribe Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLSubscribeExpression()
     * @generated
     */
    EClass IQL_SUBSCRIBE_EXPRESSION = eINSTANCE.getIQLSubscribeExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SUBSCRIBE_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLSubscribeExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_SUBSCRIBE_EXPRESSION__OP = eINSTANCE.getIQLSubscribeExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_SUBSCRIBE_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLSubscribeExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLPortExpressionImpl <em>IQL Port Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLPortExpressionImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLPortExpression()
     * @generated
     */
    EClass IQL_PORT_EXPRESSION = eINSTANCE.getIQLPortExpression();

    /**
     * The meta object literal for the '<em><b>Left Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_PORT_EXPRESSION__LEFT_OPERAND = eINSTANCE.getIQLPortExpression_LeftOperand();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IQL_PORT_EXPRESSION__OP = eINSTANCE.getIQLPortExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right Operand</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IQL_PORT_EXPRESSION__RIGHT_OPERAND = eINSTANCE.getIQLPortExpression_RightOperand();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLMetadataValueSingleIDImpl <em>Metadata Value Single ID</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLMetadataValueSingleIDImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLMetadataValueSingleID()
     * @generated
     */
    EClass QDL_METADATA_VALUE_SINGLE_ID = eINSTANCE.getQDLMetadataValueSingleID();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QDL_METADATA_VALUE_SINGLE_ID__VALUE = eINSTANCE.getQDLMetadataValueSingleID_Value();

  }

} //QDLPackage
