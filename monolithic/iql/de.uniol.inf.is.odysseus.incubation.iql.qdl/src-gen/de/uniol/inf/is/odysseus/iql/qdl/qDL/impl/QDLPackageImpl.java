/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLFactory;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.xtext.common.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QDLPackageImpl extends EPackageImpl implements QDLPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass qdlQueryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlSubscribeExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlPortExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass qdlMetadataValueSingleIDEClass = null;

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
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private QDLPackageImpl()
  {
    super(eNS_URI, QDLFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link QDLPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static QDLPackage init()
  {
    if (isInited) return (QDLPackage)EPackage.Registry.INSTANCE.getEPackage(QDLPackage.eNS_URI);

    // Obtain or create and register package
    QDLPackageImpl theQDLPackage = (QDLPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof QDLPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new QDLPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    BasicIQLPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theQDLPackage.createPackageContents();

    // Initialize created meta-data
    theQDLPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theQDLPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(QDLPackage.eNS_URI, theQDLPackage);
    return theQDLPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQDLQuery()
  {
    return qdlQueryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQDLQuery_MetadataList()
  {
    return (EReference)qdlQueryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQDLQuery_Statements()
  {
    return (EReference)qdlQueryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLSubscribeExpression()
  {
    return iqlSubscribeExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSubscribeExpression_LeftOperand()
  {
    return (EReference)iqlSubscribeExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLSubscribeExpression_Op()
  {
    return (EAttribute)iqlSubscribeExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSubscribeExpression_RightOperand()
  {
    return (EReference)iqlSubscribeExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLPortExpression()
  {
    return iqlPortExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLPortExpression_LeftOperand()
  {
    return (EReference)iqlPortExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLPortExpression_Op()
  {
    return (EAttribute)iqlPortExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLPortExpression_RightOperand()
  {
    return (EReference)iqlPortExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQDLMetadataValueSingleID()
  {
    return qdlMetadataValueSingleIDEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQDLMetadataValueSingleID_Value()
  {
    return (EAttribute)qdlMetadataValueSingleIDEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLFactory getQDLFactory()
  {
    return (QDLFactory)getEFactoryInstance();
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
    qdlQueryEClass = createEClass(QDL_QUERY);
    createEReference(qdlQueryEClass, QDL_QUERY__METADATA_LIST);
    createEReference(qdlQueryEClass, QDL_QUERY__STATEMENTS);

    iqlSubscribeExpressionEClass = createEClass(IQL_SUBSCRIBE_EXPRESSION);
    createEReference(iqlSubscribeExpressionEClass, IQL_SUBSCRIBE_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlSubscribeExpressionEClass, IQL_SUBSCRIBE_EXPRESSION__OP);
    createEReference(iqlSubscribeExpressionEClass, IQL_SUBSCRIBE_EXPRESSION__RIGHT_OPERAND);

    iqlPortExpressionEClass = createEClass(IQL_PORT_EXPRESSION);
    createEReference(iqlPortExpressionEClass, IQL_PORT_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlPortExpressionEClass, IQL_PORT_EXPRESSION__OP);
    createEReference(iqlPortExpressionEClass, IQL_PORT_EXPRESSION__RIGHT_OPERAND);

    qdlMetadataValueSingleIDEClass = createEClass(QDL_METADATA_VALUE_SINGLE_ID);
    createEAttribute(qdlMetadataValueSingleIDEClass, QDL_METADATA_VALUE_SINGLE_ID__VALUE);
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

    // Obtain other dependent packages
    TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
    BasicIQLPackage theBasicIQLPackage = (BasicIQLPackage)EPackage.Registry.INSTANCE.getEPackage(BasicIQLPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    qdlQueryEClass.getESuperTypes().add(theTypesPackage.getJvmGenericType());
    iqlSubscribeExpressionEClass.getESuperTypes().add(theBasicIQLPackage.getIQLExpression());
    iqlPortExpressionEClass.getESuperTypes().add(theBasicIQLPackage.getIQLExpression());
    qdlMetadataValueSingleIDEClass.getESuperTypes().add(theBasicIQLPackage.getIQLMetadataValue());

    // Initialize classes and features; add operations and parameters
    initEClass(qdlQueryEClass, QDLQuery.class, "QDLQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getQDLQuery_MetadataList(), theBasicIQLPackage.getIQLMetadataList(), null, "metadataList", null, 0, 1, QDLQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getQDLQuery_Statements(), theBasicIQLPackage.getIQLStatement(), null, "statements", null, 0, 1, QDLQuery.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlSubscribeExpressionEClass, IQLSubscribeExpression.class, "IQLSubscribeExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLSubscribeExpression_LeftOperand(), theBasicIQLPackage.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLSubscribeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLSubscribeExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLSubscribeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLSubscribeExpression_RightOperand(), theBasicIQLPackage.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLSubscribeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlPortExpressionEClass, IQLPortExpression.class, "IQLPortExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLPortExpression_LeftOperand(), theBasicIQLPackage.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLPortExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLPortExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLPortExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLPortExpression_RightOperand(), theBasicIQLPackage.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLPortExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(qdlMetadataValueSingleIDEClass, QDLMetadataValueSingleID.class, "QDLMetadataValueSingleID", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getQDLMetadataValueSingleID_Value(), ecorePackage.getEString(), "value", null, 0, 1, QDLMetadataValueSingleID.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //QDLPackageImpl
