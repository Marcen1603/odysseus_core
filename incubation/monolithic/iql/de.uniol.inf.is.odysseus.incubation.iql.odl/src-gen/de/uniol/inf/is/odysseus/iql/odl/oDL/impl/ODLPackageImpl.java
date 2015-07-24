/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;

import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFactory;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLFile;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ODLPackageImpl extends EPackageImpl implements ODLPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass odlFileEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass odlOperatorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass odlParameterEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass odlMethodEClass = null;

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
   * @see de.uniol.inf.is.odysseus.iql.odl.oDL.ODLPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ODLPackageImpl()
  {
    super(eNS_URI, ODLFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link ODLPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ODLPackage init()
  {
    if (isInited) return (ODLPackage)EPackage.Registry.INSTANCE.getEPackage(ODLPackage.eNS_URI);

    // Obtain or create and register package
    ODLPackageImpl theODLPackage = (ODLPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ODLPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ODLPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    BasicIQLPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theODLPackage.createPackageContents();

    // Initialize created meta-data
    theODLPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theODLPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ODLPackage.eNS_URI, theODLPackage);
    return theODLPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getODLFile()
  {
    return odlFileEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getODLOperator()
  {
    return odlOperatorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getODLOperator_MetadataList()
  {
    return (EReference)odlOperatorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getODLParameter()
  {
    return odlParameterEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getODLParameter_Optional()
  {
    return (EAttribute)odlParameterEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getODLParameter_Parameter()
  {
    return (EAttribute)odlParameterEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getODLParameter_MetadataList()
  {
    return (EReference)odlParameterEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getODLMethod()
  {
    return odlMethodEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getODLMethod_On()
  {
    return (EAttribute)odlMethodEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getODLMethod_Validate()
  {
    return (EAttribute)odlMethodEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLFactory getODLFactory()
  {
    return (ODLFactory)getEFactoryInstance();
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
    odlFileEClass = createEClass(ODL_FILE);

    odlOperatorEClass = createEClass(ODL_OPERATOR);
    createEReference(odlOperatorEClass, ODL_OPERATOR__METADATA_LIST);

    odlParameterEClass = createEClass(ODL_PARAMETER);
    createEAttribute(odlParameterEClass, ODL_PARAMETER__OPTIONAL);
    createEAttribute(odlParameterEClass, ODL_PARAMETER__PARAMETER);
    createEReference(odlParameterEClass, ODL_PARAMETER__METADATA_LIST);

    odlMethodEClass = createEClass(ODL_METHOD);
    createEAttribute(odlMethodEClass, ODL_METHOD__ON);
    createEAttribute(odlMethodEClass, ODL_METHOD__VALIDATE);
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
    BasicIQLPackage theBasicIQLPackage = (BasicIQLPackage)EPackage.Registry.INSTANCE.getEPackage(BasicIQLPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    odlFileEClass.getESuperTypes().add(theBasicIQLPackage.getIQLFile());
    odlOperatorEClass.getESuperTypes().add(theBasicIQLPackage.getIQLTypeDef());
    odlParameterEClass.getESuperTypes().add(theBasicIQLPackage.getIQLAttribute());
    odlMethodEClass.getESuperTypes().add(theBasicIQLPackage.getIQLMethod());

    // Initialize classes and features; add operations and parameters
    initEClass(odlFileEClass, ODLFile.class, "ODLFile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(odlOperatorEClass, ODLOperator.class, "ODLOperator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getODLOperator_MetadataList(), theBasicIQLPackage.getIQLMetadataList(), null, "metadataList", null, 0, 1, ODLOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(odlParameterEClass, ODLParameter.class, "ODLParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getODLParameter_Optional(), ecorePackage.getEBoolean(), "optional", null, 0, 1, ODLParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getODLParameter_Parameter(), ecorePackage.getEBoolean(), "parameter", null, 0, 1, ODLParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getODLParameter_MetadataList(), theBasicIQLPackage.getIQLMetadataList(), null, "metadataList", null, 0, 1, ODLParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(odlMethodEClass, ODLMethod.class, "ODLMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getODLMethod_On(), ecorePackage.getEBoolean(), "on", null, 0, 1, ODLMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getODLMethod_Validate(), ecorePackage.getEBoolean(), "validate", null, 0, 1, ODLMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //ODLPackageImpl
