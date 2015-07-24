/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;

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
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLFileImpl <em>File</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLFileImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLFile()
   * @generated
   */
  int QDL_FILE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_FILE__NAME = BasicIQLPackage.IQL_FILE__NAME;

  /**
   * The feature id for the '<em><b>Namespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_FILE__NAMESPACES = BasicIQLPackage.IQL_FILE__NAMESPACES;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_FILE__ELEMENTS = BasicIQLPackage.IQL_FILE__ELEMENTS;

  /**
   * The number of structural features of the '<em>File</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_FILE_FEATURE_COUNT = BasicIQLPackage.IQL_FILE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl <em>Query</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLQuery()
   * @generated
   */
  int QDL_QUERY = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ANNOTATIONS = BasicIQLPackage.IQL_TYPE_DEF__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Declaring Type</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__DECLARING_TYPE = BasicIQLPackage.IQL_TYPE_DEF__DECLARING_TYPE;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__VISIBILITY = BasicIQLPackage.IQL_TYPE_DEF__VISIBILITY;

  /**
   * The feature id for the '<em><b>Simple Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__SIMPLE_NAME = BasicIQLPackage.IQL_TYPE_DEF__SIMPLE_NAME;

  /**
   * The feature id for the '<em><b>Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__IDENTIFIER = BasicIQLPackage.IQL_TYPE_DEF__IDENTIFIER;

  /**
   * The feature id for the '<em><b>Deprecated</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__DEPRECATED = BasicIQLPackage.IQL_TYPE_DEF__DEPRECATED;

  /**
   * The feature id for the '<em><b>Array Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ARRAY_TYPE = BasicIQLPackage.IQL_TYPE_DEF__ARRAY_TYPE;

  /**
   * The feature id for the '<em><b>Super Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__SUPER_TYPES = BasicIQLPackage.IQL_TYPE_DEF__SUPER_TYPES;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__MEMBERS = BasicIQLPackage.IQL_TYPE_DEF__MEMBERS;

  /**
   * The feature id for the '<em><b>Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ABSTRACT = BasicIQLPackage.IQL_TYPE_DEF__ABSTRACT;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STATIC = BasicIQLPackage.IQL_TYPE_DEF__STATIC;

  /**
   * The feature id for the '<em><b>Final</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__FINAL = BasicIQLPackage.IQL_TYPE_DEF__FINAL;

  /**
   * The feature id for the '<em><b>Package Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__PACKAGE_NAME = BasicIQLPackage.IQL_TYPE_DEF__PACKAGE_NAME;

  /**
   * The feature id for the '<em><b>Type Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__TYPE_PARAMETERS = BasicIQLPackage.IQL_TYPE_DEF__TYPE_PARAMETERS;

  /**
   * The feature id for the '<em><b>Interface</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__INTERFACE = BasicIQLPackage.IQL_TYPE_DEF__INTERFACE;

  /**
   * The feature id for the '<em><b>Strict Floating Point</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STRICT_FLOATING_POINT = BasicIQLPackage.IQL_TYPE_DEF__STRICT_FLOATING_POINT;

  /**
   * The feature id for the '<em><b>Anonymous</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__ANONYMOUS = BasicIQLPackage.IQL_TYPE_DEF__ANONYMOUS;

  /**
   * The feature id for the '<em><b>Javametadata</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__JAVAMETADATA = BasicIQLPackage.IQL_TYPE_DEF__JAVAMETADATA;

  /**
   * The feature id for the '<em><b>Extended Interfaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__EXTENDED_INTERFACES = BasicIQLPackage.IQL_TYPE_DEF__EXTENDED_INTERFACES;

  /**
   * The feature id for the '<em><b>Metadata List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__METADATA_LIST = BasicIQLPackage.IQL_TYPE_DEF_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Statements</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY__STATEMENTS = BasicIQLPackage.IQL_TYPE_DEF_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Query</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QDL_QUERY_FEATURE_COUNT = BasicIQLPackage.IQL_TYPE_DEF_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl <em>IQL Subscribe Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.IQLSubscribeExpressionImpl
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getIQLSubscribeExpression()
   * @generated
   */
  int IQL_SUBSCRIBE_EXPRESSION = 2;

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
  int IQL_PORT_EXPRESSION = 3;

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
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFile <em>File</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>File</em>'.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFile
   * @generated
   */
  EClass getQDLFile();

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
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLFileImpl <em>File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLFileImpl
     * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLPackageImpl#getQDLFile()
     * @generated
     */
    EClass QDL_FILE = eINSTANCE.getQDLFile();

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

  }

} //QDLPackage
