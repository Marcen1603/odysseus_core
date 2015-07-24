/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAdditiveExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAssignmentExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttributeSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBooleanNotExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLBreakStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLCasePart;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLContinueStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLDoWhileStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLEqualityExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpressionStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForEachStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLForStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLIfStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInstanceOfExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJava;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaKeywords;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionInt;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionMapKeyValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionRange;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLiteralExpressionString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalAndExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLLogicalOrExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMemberSelectionExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueMapElement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleBoolean;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleChar;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleDouble;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleLong;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleNull;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleString;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValueSingleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMultiplicativeExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPlusMinusExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPostfixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLPrefixExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLRelationalExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLReturnStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSwitchStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionNew;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionParenthesis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionSuper;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionThis;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTerminalExpressionVariable;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeCastExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableInitialization;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableStatement;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLWhileStatement;

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
public class BasicIQLPackageImpl extends EPackageImpl implements BasicIQLPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlFileEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTypeDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlNamespaceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlJavaMetadataEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlSimpleTypeRefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArrayTypeRefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlSimpleTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArrayTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlVariableDeclarationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueMapElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlVariableInitializationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArgumentsListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArgumentsMapEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArgumentsMapKeyValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlCasePartEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMemberSelectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionMapKeyValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlJavaEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlJavaKeywordsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlClassEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlInterfaceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlAttributeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMethodEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMethodDeclarationMemberEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlJavaMemberEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleLongEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleDoubleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleStringEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleBooleanEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleCharEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleTypeRefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueSingleNullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMetadataValueMapEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlStatementBlockEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlJavaStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlIfStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlWhileStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlDoWhileStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlForStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlForEachStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlSwitchStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlExpressionStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlVariableStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlConstructorCallStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlBreakStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlContinueStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlReturnStatementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlAssignmentExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLogicalOrExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLogicalAndExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlEqualityExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlInstanceOfExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlRelationalExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlAdditiveExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMultiplicativeExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlPlusMinusExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlBooleanNotExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlPrefixExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTypeCastExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlPostfixExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlArrayExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMemberSelectionExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlAttributeSelectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlMethodSelectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTerminalExpressionVariableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTerminalExpressionThisEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTerminalExpressionSuperEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTerminalExpressionParenthesisEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlTerminalExpressionNewEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionIntEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionDoubleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionStringEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionBooleanEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionCharEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionRangeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionNullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iqlLiteralExpressionMapEClass = null;

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
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private BasicIQLPackageImpl()
  {
    super(eNS_URI, BasicIQLFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link BasicIQLPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static BasicIQLPackage init()
  {
    if (isInited) return (BasicIQLPackage)EPackage.Registry.INSTANCE.getEPackage(BasicIQLPackage.eNS_URI);

    // Obtain or create and register package
    BasicIQLPackageImpl theBasicIQLPackage = (BasicIQLPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof BasicIQLPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new BasicIQLPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    TypesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theBasicIQLPackage.createPackageContents();

    // Initialize created meta-data
    theBasicIQLPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theBasicIQLPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(BasicIQLPackage.eNS_URI, theBasicIQLPackage);
    return theBasicIQLPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLFile()
  {
    return iqlFileEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLFile_Name()
  {
    return (EAttribute)iqlFileEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLFile_Namespaces()
  {
    return (EReference)iqlFileEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLFile_Elements()
  {
    return (EReference)iqlFileEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTypeDef()
  {
    return iqlTypeDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTypeDef_Javametadata()
  {
    return (EReference)iqlTypeDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTypeDef_ExtendedInterfaces()
  {
    return (EReference)iqlTypeDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLNamespace()
  {
    return iqlNamespaceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLNamespace_ImportedNamespace()
  {
    return (EAttribute)iqlNamespaceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLJavaMetadata()
  {
    return iqlJavaMetadataEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLJavaMetadata_Text()
  {
    return (EReference)iqlJavaMetadataEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLSimpleTypeRef()
  {
    return iqlSimpleTypeRefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSimpleTypeRef_Type()
  {
    return (EReference)iqlSimpleTypeRefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArrayTypeRef()
  {
    return iqlArrayTypeRefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArrayTypeRef_Type()
  {
    return (EReference)iqlArrayTypeRefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLSimpleType()
  {
    return iqlSimpleTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSimpleType_Type()
  {
    return (EReference)iqlSimpleTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArrayType()
  {
    return iqlArrayTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArrayType_Type()
  {
    return (EReference)iqlArrayTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLArrayType_Dimensions()
  {
    return (EAttribute)iqlArrayTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLVariableDeclaration()
  {
    return iqlVariableDeclarationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableDeclaration_Ref()
  {
    return (EReference)iqlVariableDeclarationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataList()
  {
    return iqlMetadataListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataList_Elements()
  {
    return (EReference)iqlMetadataListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadata()
  {
    return iqlMetadataEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadata_Name()
  {
    return (EAttribute)iqlMetadataEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadata_Value()
  {
    return (EReference)iqlMetadataEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValue()
  {
    return iqlMetadataValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueMapElement()
  {
    return iqlMetadataValueMapElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataValueMapElement_Key()
  {
    return (EReference)iqlMetadataValueMapElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataValueMapElement_Value()
  {
    return (EReference)iqlMetadataValueMapElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLVariableInitialization()
  {
    return iqlVariableInitializationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableInitialization_ArgsList()
  {
    return (EReference)iqlVariableInitializationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableInitialization_ArgsMap()
  {
    return (EReference)iqlVariableInitializationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableInitialization_Value()
  {
    return (EReference)iqlVariableInitializationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArgumentsList()
  {
    return iqlArgumentsListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArgumentsList_Elements()
  {
    return (EReference)iqlArgumentsListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArgumentsMap()
  {
    return iqlArgumentsMapEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArgumentsMap_Elements()
  {
    return (EReference)iqlArgumentsMapEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArgumentsMapKeyValue()
  {
    return iqlArgumentsMapKeyValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLArgumentsMapKeyValue_Key()
  {
    return (EAttribute)iqlArgumentsMapKeyValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArgumentsMapKeyValue_Value()
  {
    return (EReference)iqlArgumentsMapKeyValueEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLStatement()
  {
    return iqlStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLCasePart()
  {
    return iqlCasePartEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLCasePart_Expr()
  {
    return (EReference)iqlCasePartEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLCasePart_Body()
  {
    return (EReference)iqlCasePartEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLExpression()
  {
    return iqlExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMemberSelection()
  {
    return iqlMemberSelectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionMapKeyValue()
  {
    return iqlLiteralExpressionMapKeyValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLiteralExpressionMapKeyValue_Key()
  {
    return (EReference)iqlLiteralExpressionMapKeyValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLiteralExpressionMapKeyValue_Value()
  {
    return (EReference)iqlLiteralExpressionMapKeyValueEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLJava()
  {
    return iqlJavaEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLJava_Text()
  {
    return (EAttribute)iqlJavaEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLJava_Keywords()
  {
    return (EReference)iqlJavaEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLJavaKeywords()
  {
    return iqlJavaKeywordsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLJavaKeywords_Keyword()
  {
    return (EAttribute)iqlJavaKeywordsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLClass()
  {
    return iqlClassEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLClass_ExtendedClass()
  {
    return (EReference)iqlClassEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLInterface()
  {
    return iqlInterfaceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLAttribute()
  {
    return iqlAttributeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAttribute_Init()
  {
    return (EReference)iqlAttributeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMethod()
  {
    return iqlMethodEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMethod_Body()
  {
    return (EReference)iqlMethodEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMethodDeclarationMember()
  {
    return iqlMethodDeclarationMemberEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLJavaMember()
  {
    return iqlJavaMemberEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLJavaMember_Text()
  {
    return (EReference)iqlJavaMemberEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleLong()
  {
    return iqlMetadataValueSingleLongEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleLong_Value()
  {
    return (EAttribute)iqlMetadataValueSingleLongEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleDouble()
  {
    return iqlMetadataValueSingleDoubleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleDouble_Value()
  {
    return (EAttribute)iqlMetadataValueSingleDoubleEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleString()
  {
    return iqlMetadataValueSingleStringEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleString_Value()
  {
    return (EAttribute)iqlMetadataValueSingleStringEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleBoolean()
  {
    return iqlMetadataValueSingleBooleanEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleBoolean_Value()
  {
    return (EAttribute)iqlMetadataValueSingleBooleanEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleChar()
  {
    return iqlMetadataValueSingleCharEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleChar_Value()
  {
    return (EAttribute)iqlMetadataValueSingleCharEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleTypeRef()
  {
    return iqlMetadataValueSingleTypeRefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataValueSingleTypeRef_Value()
  {
    return (EReference)iqlMetadataValueSingleTypeRefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueSingleNull()
  {
    return iqlMetadataValueSingleNullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMetadataValueSingleNull_Value()
  {
    return (EAttribute)iqlMetadataValueSingleNullEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueList()
  {
    return iqlMetadataValueListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataValueList_Elements()
  {
    return (EReference)iqlMetadataValueListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMetadataValueMap()
  {
    return iqlMetadataValueMapEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMetadataValueMap_Elements()
  {
    return (EReference)iqlMetadataValueMapEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLStatementBlock()
  {
    return iqlStatementBlockEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLStatementBlock_Statements()
  {
    return (EReference)iqlStatementBlockEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLJavaStatement()
  {
    return iqlJavaStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLJavaStatement_Text()
  {
    return (EReference)iqlJavaStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLIfStatement()
  {
    return iqlIfStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLIfStatement_Predicate()
  {
    return (EReference)iqlIfStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLIfStatement_ThenBody()
  {
    return (EReference)iqlIfStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLIfStatement_ElseBody()
  {
    return (EReference)iqlIfStatementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLWhileStatement()
  {
    return iqlWhileStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLWhileStatement_Predicate()
  {
    return (EReference)iqlWhileStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLWhileStatement_Body()
  {
    return (EReference)iqlWhileStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLDoWhileStatement()
  {
    return iqlDoWhileStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLDoWhileStatement_Body()
  {
    return (EReference)iqlDoWhileStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLDoWhileStatement_Predicate()
  {
    return (EReference)iqlDoWhileStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLForStatement()
  {
    return iqlForStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForStatement_Var()
  {
    return (EReference)iqlForStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForStatement_Predicate()
  {
    return (EReference)iqlForStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForStatement_UpdateExpr()
  {
    return (EReference)iqlForStatementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForStatement_Body()
  {
    return (EReference)iqlForStatementEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLForEachStatement()
  {
    return iqlForEachStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForEachStatement_Var()
  {
    return (EReference)iqlForEachStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForEachStatement_ForExpression()
  {
    return (EReference)iqlForEachStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLForEachStatement_Body()
  {
    return (EReference)iqlForEachStatementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLSwitchStatement()
  {
    return iqlSwitchStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSwitchStatement_Expr()
  {
    return (EReference)iqlSwitchStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSwitchStatement_Cases()
  {
    return (EReference)iqlSwitchStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLSwitchStatement_Default()
  {
    return (EReference)iqlSwitchStatementEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLExpressionStatement()
  {
    return iqlExpressionStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLExpressionStatement_Expression()
  {
    return (EReference)iqlExpressionStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLVariableStatement()
  {
    return iqlVariableStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableStatement_Var()
  {
    return (EReference)iqlVariableStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLVariableStatement_Init()
  {
    return (EReference)iqlVariableStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLConstructorCallStatement()
  {
    return iqlConstructorCallStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLConstructorCallStatement_Keyword()
  {
    return (EAttribute)iqlConstructorCallStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLConstructorCallStatement_Args()
  {
    return (EReference)iqlConstructorCallStatementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLBreakStatement()
  {
    return iqlBreakStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLContinueStatement()
  {
    return iqlContinueStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLReturnStatement()
  {
    return iqlReturnStatementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLReturnStatement_Expression()
  {
    return (EReference)iqlReturnStatementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLAssignmentExpression()
  {
    return iqlAssignmentExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAssignmentExpression_LeftOperand()
  {
    return (EReference)iqlAssignmentExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLAssignmentExpression_Op()
  {
    return (EAttribute)iqlAssignmentExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAssignmentExpression_RightOperand()
  {
    return (EReference)iqlAssignmentExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLogicalOrExpression()
  {
    return iqlLogicalOrExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLogicalOrExpression_LeftOperand()
  {
    return (EReference)iqlLogicalOrExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLogicalOrExpression_Op()
  {
    return (EAttribute)iqlLogicalOrExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLogicalOrExpression_RightOperand()
  {
    return (EReference)iqlLogicalOrExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLogicalAndExpression()
  {
    return iqlLogicalAndExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLogicalAndExpression_LeftOperand()
  {
    return (EReference)iqlLogicalAndExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLogicalAndExpression_Op()
  {
    return (EAttribute)iqlLogicalAndExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLogicalAndExpression_RightOperand()
  {
    return (EReference)iqlLogicalAndExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLEqualityExpression()
  {
    return iqlEqualityExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLEqualityExpression_LeftOperand()
  {
    return (EReference)iqlEqualityExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLEqualityExpression_Op()
  {
    return (EAttribute)iqlEqualityExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLEqualityExpression_RightOperand()
  {
    return (EReference)iqlEqualityExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLInstanceOfExpression()
  {
    return iqlInstanceOfExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLInstanceOfExpression_LeftOperand()
  {
    return (EReference)iqlInstanceOfExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLInstanceOfExpression_TargetRef()
  {
    return (EReference)iqlInstanceOfExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLRelationalExpression()
  {
    return iqlRelationalExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLRelationalExpression_LeftOperand()
  {
    return (EReference)iqlRelationalExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLRelationalExpression_Op()
  {
    return (EAttribute)iqlRelationalExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLRelationalExpression_RightOperand()
  {
    return (EReference)iqlRelationalExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLAdditiveExpression()
  {
    return iqlAdditiveExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAdditiveExpression_LeftOperand()
  {
    return (EReference)iqlAdditiveExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLAdditiveExpression_Op()
  {
    return (EAttribute)iqlAdditiveExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAdditiveExpression_RightOperand()
  {
    return (EReference)iqlAdditiveExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMultiplicativeExpression()
  {
    return iqlMultiplicativeExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMultiplicativeExpression_LeftOperand()
  {
    return (EReference)iqlMultiplicativeExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMultiplicativeExpression_Op()
  {
    return (EAttribute)iqlMultiplicativeExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMultiplicativeExpression_RightOperand()
  {
    return (EReference)iqlMultiplicativeExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLPlusMinusExpression()
  {
    return iqlPlusMinusExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLPlusMinusExpression_Op()
  {
    return (EAttribute)iqlPlusMinusExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLPlusMinusExpression_Operand()
  {
    return (EReference)iqlPlusMinusExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLBooleanNotExpression()
  {
    return iqlBooleanNotExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLBooleanNotExpression_Op()
  {
    return (EAttribute)iqlBooleanNotExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLBooleanNotExpression_Operand()
  {
    return (EReference)iqlBooleanNotExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLPrefixExpression()
  {
    return iqlPrefixExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLPrefixExpression_Op()
  {
    return (EAttribute)iqlPrefixExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLPrefixExpression_Operand()
  {
    return (EReference)iqlPrefixExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTypeCastExpression()
  {
    return iqlTypeCastExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTypeCastExpression_TargetRef()
  {
    return (EReference)iqlTypeCastExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTypeCastExpression_Operand()
  {
    return (EReference)iqlTypeCastExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLPostfixExpression()
  {
    return iqlPostfixExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLPostfixExpression_Operand()
  {
    return (EReference)iqlPostfixExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLPostfixExpression_Op()
  {
    return (EAttribute)iqlPostfixExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLArrayExpression()
  {
    return iqlArrayExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArrayExpression_LeftOperand()
  {
    return (EReference)iqlArrayExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLArrayExpression_Expr()
  {
    return (EReference)iqlArrayExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMemberSelectionExpression()
  {
    return iqlMemberSelectionExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMemberSelectionExpression_LeftOperand()
  {
    return (EReference)iqlMemberSelectionExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMemberSelectionExpression_RightOperand()
  {
    return (EReference)iqlMemberSelectionExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLAttributeSelection()
  {
    return iqlAttributeSelectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLAttributeSelection_Var()
  {
    return (EReference)iqlAttributeSelectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLMethodSelection()
  {
    return iqlMethodSelectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLMethodSelection_Method()
  {
    return (EAttribute)iqlMethodSelectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLMethodSelection_Args()
  {
    return (EReference)iqlMethodSelectionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTerminalExpressionVariable()
  {
    return iqlTerminalExpressionVariableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTerminalExpressionVariable_Var()
  {
    return (EReference)iqlTerminalExpressionVariableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTerminalExpressionThis()
  {
    return iqlTerminalExpressionThisEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTerminalExpressionSuper()
  {
    return iqlTerminalExpressionSuperEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTerminalExpressionParenthesis()
  {
    return iqlTerminalExpressionParenthesisEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTerminalExpressionParenthesis_Expr()
  {
    return (EReference)iqlTerminalExpressionParenthesisEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLTerminalExpressionNew()
  {
    return iqlTerminalExpressionNewEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTerminalExpressionNew_Ref()
  {
    return (EReference)iqlTerminalExpressionNewEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTerminalExpressionNew_ArgsList()
  {
    return (EReference)iqlTerminalExpressionNewEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLTerminalExpressionNew_ArgsMap()
  {
    return (EReference)iqlTerminalExpressionNewEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionInt()
  {
    return iqlLiteralExpressionIntEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionInt_Value()
  {
    return (EAttribute)iqlLiteralExpressionIntEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionDouble()
  {
    return iqlLiteralExpressionDoubleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionDouble_Value()
  {
    return (EAttribute)iqlLiteralExpressionDoubleEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionString()
  {
    return iqlLiteralExpressionStringEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionString_Value()
  {
    return (EAttribute)iqlLiteralExpressionStringEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionBoolean()
  {
    return iqlLiteralExpressionBooleanEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionBoolean_Value()
  {
    return (EAttribute)iqlLiteralExpressionBooleanEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionChar()
  {
    return iqlLiteralExpressionCharEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionChar_Value()
  {
    return (EAttribute)iqlLiteralExpressionCharEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionRange()
  {
    return iqlLiteralExpressionRangeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIQLLiteralExpressionRange_Value()
  {
    return (EAttribute)iqlLiteralExpressionRangeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionNull()
  {
    return iqlLiteralExpressionNullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionList()
  {
    return iqlLiteralExpressionListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLiteralExpressionList_Elements()
  {
    return (EReference)iqlLiteralExpressionListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIQLLiteralExpressionMap()
  {
    return iqlLiteralExpressionMapEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIQLLiteralExpressionMap_Elements()
  {
    return (EReference)iqlLiteralExpressionMapEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasicIQLFactory getBasicIQLFactory()
  {
    return (BasicIQLFactory)getEFactoryInstance();
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
    iqlFileEClass = createEClass(IQL_FILE);
    createEAttribute(iqlFileEClass, IQL_FILE__NAME);
    createEReference(iqlFileEClass, IQL_FILE__NAMESPACES);
    createEReference(iqlFileEClass, IQL_FILE__ELEMENTS);

    iqlTypeDefEClass = createEClass(IQL_TYPE_DEF);
    createEReference(iqlTypeDefEClass, IQL_TYPE_DEF__JAVAMETADATA);
    createEReference(iqlTypeDefEClass, IQL_TYPE_DEF__EXTENDED_INTERFACES);

    iqlNamespaceEClass = createEClass(IQL_NAMESPACE);
    createEAttribute(iqlNamespaceEClass, IQL_NAMESPACE__IMPORTED_NAMESPACE);

    iqlJavaMetadataEClass = createEClass(IQL_JAVA_METADATA);
    createEReference(iqlJavaMetadataEClass, IQL_JAVA_METADATA__TEXT);

    iqlSimpleTypeRefEClass = createEClass(IQL_SIMPLE_TYPE_REF);
    createEReference(iqlSimpleTypeRefEClass, IQL_SIMPLE_TYPE_REF__TYPE);

    iqlArrayTypeRefEClass = createEClass(IQL_ARRAY_TYPE_REF);
    createEReference(iqlArrayTypeRefEClass, IQL_ARRAY_TYPE_REF__TYPE);

    iqlSimpleTypeEClass = createEClass(IQL_SIMPLE_TYPE);
    createEReference(iqlSimpleTypeEClass, IQL_SIMPLE_TYPE__TYPE);

    iqlArrayTypeEClass = createEClass(IQL_ARRAY_TYPE);
    createEReference(iqlArrayTypeEClass, IQL_ARRAY_TYPE__TYPE);
    createEAttribute(iqlArrayTypeEClass, IQL_ARRAY_TYPE__DIMENSIONS);

    iqlVariableDeclarationEClass = createEClass(IQL_VARIABLE_DECLARATION);
    createEReference(iqlVariableDeclarationEClass, IQL_VARIABLE_DECLARATION__REF);

    iqlMetadataListEClass = createEClass(IQL_METADATA_LIST);
    createEReference(iqlMetadataListEClass, IQL_METADATA_LIST__ELEMENTS);

    iqlMetadataEClass = createEClass(IQL_METADATA);
    createEAttribute(iqlMetadataEClass, IQL_METADATA__NAME);
    createEReference(iqlMetadataEClass, IQL_METADATA__VALUE);

    iqlMetadataValueEClass = createEClass(IQL_METADATA_VALUE);

    iqlMetadataValueMapElementEClass = createEClass(IQL_METADATA_VALUE_MAP_ELEMENT);
    createEReference(iqlMetadataValueMapElementEClass, IQL_METADATA_VALUE_MAP_ELEMENT__KEY);
    createEReference(iqlMetadataValueMapElementEClass, IQL_METADATA_VALUE_MAP_ELEMENT__VALUE);

    iqlVariableInitializationEClass = createEClass(IQL_VARIABLE_INITIALIZATION);
    createEReference(iqlVariableInitializationEClass, IQL_VARIABLE_INITIALIZATION__ARGS_LIST);
    createEReference(iqlVariableInitializationEClass, IQL_VARIABLE_INITIALIZATION__ARGS_MAP);
    createEReference(iqlVariableInitializationEClass, IQL_VARIABLE_INITIALIZATION__VALUE);

    iqlArgumentsListEClass = createEClass(IQL_ARGUMENTS_LIST);
    createEReference(iqlArgumentsListEClass, IQL_ARGUMENTS_LIST__ELEMENTS);

    iqlArgumentsMapEClass = createEClass(IQL_ARGUMENTS_MAP);
    createEReference(iqlArgumentsMapEClass, IQL_ARGUMENTS_MAP__ELEMENTS);

    iqlArgumentsMapKeyValueEClass = createEClass(IQL_ARGUMENTS_MAP_KEY_VALUE);
    createEAttribute(iqlArgumentsMapKeyValueEClass, IQL_ARGUMENTS_MAP_KEY_VALUE__KEY);
    createEReference(iqlArgumentsMapKeyValueEClass, IQL_ARGUMENTS_MAP_KEY_VALUE__VALUE);

    iqlStatementEClass = createEClass(IQL_STATEMENT);

    iqlCasePartEClass = createEClass(IQL_CASE_PART);
    createEReference(iqlCasePartEClass, IQL_CASE_PART__EXPR);
    createEReference(iqlCasePartEClass, IQL_CASE_PART__BODY);

    iqlExpressionEClass = createEClass(IQL_EXPRESSION);

    iqlMemberSelectionEClass = createEClass(IQL_MEMBER_SELECTION);

    iqlLiteralExpressionMapKeyValueEClass = createEClass(IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE);
    createEReference(iqlLiteralExpressionMapKeyValueEClass, IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__KEY);
    createEReference(iqlLiteralExpressionMapKeyValueEClass, IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE__VALUE);

    iqlJavaEClass = createEClass(IQL_JAVA);
    createEAttribute(iqlJavaEClass, IQL_JAVA__TEXT);
    createEReference(iqlJavaEClass, IQL_JAVA__KEYWORDS);

    iqlJavaKeywordsEClass = createEClass(IQL_JAVA_KEYWORDS);
    createEAttribute(iqlJavaKeywordsEClass, IQL_JAVA_KEYWORDS__KEYWORD);

    iqlClassEClass = createEClass(IQL_CLASS);
    createEReference(iqlClassEClass, IQL_CLASS__EXTENDED_CLASS);

    iqlInterfaceEClass = createEClass(IQL_INTERFACE);

    iqlAttributeEClass = createEClass(IQL_ATTRIBUTE);
    createEReference(iqlAttributeEClass, IQL_ATTRIBUTE__INIT);

    iqlMethodEClass = createEClass(IQL_METHOD);
    createEReference(iqlMethodEClass, IQL_METHOD__BODY);

    iqlMethodDeclarationMemberEClass = createEClass(IQL_METHOD_DECLARATION_MEMBER);

    iqlJavaMemberEClass = createEClass(IQL_JAVA_MEMBER);
    createEReference(iqlJavaMemberEClass, IQL_JAVA_MEMBER__TEXT);

    iqlMetadataValueSingleLongEClass = createEClass(IQL_METADATA_VALUE_SINGLE_LONG);
    createEAttribute(iqlMetadataValueSingleLongEClass, IQL_METADATA_VALUE_SINGLE_LONG__VALUE);

    iqlMetadataValueSingleDoubleEClass = createEClass(IQL_METADATA_VALUE_SINGLE_DOUBLE);
    createEAttribute(iqlMetadataValueSingleDoubleEClass, IQL_METADATA_VALUE_SINGLE_DOUBLE__VALUE);

    iqlMetadataValueSingleStringEClass = createEClass(IQL_METADATA_VALUE_SINGLE_STRING);
    createEAttribute(iqlMetadataValueSingleStringEClass, IQL_METADATA_VALUE_SINGLE_STRING__VALUE);

    iqlMetadataValueSingleBooleanEClass = createEClass(IQL_METADATA_VALUE_SINGLE_BOOLEAN);
    createEAttribute(iqlMetadataValueSingleBooleanEClass, IQL_METADATA_VALUE_SINGLE_BOOLEAN__VALUE);

    iqlMetadataValueSingleCharEClass = createEClass(IQL_METADATA_VALUE_SINGLE_CHAR);
    createEAttribute(iqlMetadataValueSingleCharEClass, IQL_METADATA_VALUE_SINGLE_CHAR__VALUE);

    iqlMetadataValueSingleTypeRefEClass = createEClass(IQL_METADATA_VALUE_SINGLE_TYPE_REF);
    createEReference(iqlMetadataValueSingleTypeRefEClass, IQL_METADATA_VALUE_SINGLE_TYPE_REF__VALUE);

    iqlMetadataValueSingleNullEClass = createEClass(IQL_METADATA_VALUE_SINGLE_NULL);
    createEAttribute(iqlMetadataValueSingleNullEClass, IQL_METADATA_VALUE_SINGLE_NULL__VALUE);

    iqlMetadataValueListEClass = createEClass(IQL_METADATA_VALUE_LIST);
    createEReference(iqlMetadataValueListEClass, IQL_METADATA_VALUE_LIST__ELEMENTS);

    iqlMetadataValueMapEClass = createEClass(IQL_METADATA_VALUE_MAP);
    createEReference(iqlMetadataValueMapEClass, IQL_METADATA_VALUE_MAP__ELEMENTS);

    iqlStatementBlockEClass = createEClass(IQL_STATEMENT_BLOCK);
    createEReference(iqlStatementBlockEClass, IQL_STATEMENT_BLOCK__STATEMENTS);

    iqlJavaStatementEClass = createEClass(IQL_JAVA_STATEMENT);
    createEReference(iqlJavaStatementEClass, IQL_JAVA_STATEMENT__TEXT);

    iqlIfStatementEClass = createEClass(IQL_IF_STATEMENT);
    createEReference(iqlIfStatementEClass, IQL_IF_STATEMENT__PREDICATE);
    createEReference(iqlIfStatementEClass, IQL_IF_STATEMENT__THEN_BODY);
    createEReference(iqlIfStatementEClass, IQL_IF_STATEMENT__ELSE_BODY);

    iqlWhileStatementEClass = createEClass(IQL_WHILE_STATEMENT);
    createEReference(iqlWhileStatementEClass, IQL_WHILE_STATEMENT__PREDICATE);
    createEReference(iqlWhileStatementEClass, IQL_WHILE_STATEMENT__BODY);

    iqlDoWhileStatementEClass = createEClass(IQL_DO_WHILE_STATEMENT);
    createEReference(iqlDoWhileStatementEClass, IQL_DO_WHILE_STATEMENT__BODY);
    createEReference(iqlDoWhileStatementEClass, IQL_DO_WHILE_STATEMENT__PREDICATE);

    iqlForStatementEClass = createEClass(IQL_FOR_STATEMENT);
    createEReference(iqlForStatementEClass, IQL_FOR_STATEMENT__VAR);
    createEReference(iqlForStatementEClass, IQL_FOR_STATEMENT__PREDICATE);
    createEReference(iqlForStatementEClass, IQL_FOR_STATEMENT__UPDATE_EXPR);
    createEReference(iqlForStatementEClass, IQL_FOR_STATEMENT__BODY);

    iqlForEachStatementEClass = createEClass(IQL_FOR_EACH_STATEMENT);
    createEReference(iqlForEachStatementEClass, IQL_FOR_EACH_STATEMENT__VAR);
    createEReference(iqlForEachStatementEClass, IQL_FOR_EACH_STATEMENT__FOR_EXPRESSION);
    createEReference(iqlForEachStatementEClass, IQL_FOR_EACH_STATEMENT__BODY);

    iqlSwitchStatementEClass = createEClass(IQL_SWITCH_STATEMENT);
    createEReference(iqlSwitchStatementEClass, IQL_SWITCH_STATEMENT__EXPR);
    createEReference(iqlSwitchStatementEClass, IQL_SWITCH_STATEMENT__CASES);
    createEReference(iqlSwitchStatementEClass, IQL_SWITCH_STATEMENT__DEFAULT);

    iqlExpressionStatementEClass = createEClass(IQL_EXPRESSION_STATEMENT);
    createEReference(iqlExpressionStatementEClass, IQL_EXPRESSION_STATEMENT__EXPRESSION);

    iqlVariableStatementEClass = createEClass(IQL_VARIABLE_STATEMENT);
    createEReference(iqlVariableStatementEClass, IQL_VARIABLE_STATEMENT__VAR);
    createEReference(iqlVariableStatementEClass, IQL_VARIABLE_STATEMENT__INIT);

    iqlConstructorCallStatementEClass = createEClass(IQL_CONSTRUCTOR_CALL_STATEMENT);
    createEAttribute(iqlConstructorCallStatementEClass, IQL_CONSTRUCTOR_CALL_STATEMENT__KEYWORD);
    createEReference(iqlConstructorCallStatementEClass, IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS);

    iqlBreakStatementEClass = createEClass(IQL_BREAK_STATEMENT);

    iqlContinueStatementEClass = createEClass(IQL_CONTINUE_STATEMENT);

    iqlReturnStatementEClass = createEClass(IQL_RETURN_STATEMENT);
    createEReference(iqlReturnStatementEClass, IQL_RETURN_STATEMENT__EXPRESSION);

    iqlAssignmentExpressionEClass = createEClass(IQL_ASSIGNMENT_EXPRESSION);
    createEReference(iqlAssignmentExpressionEClass, IQL_ASSIGNMENT_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlAssignmentExpressionEClass, IQL_ASSIGNMENT_EXPRESSION__OP);
    createEReference(iqlAssignmentExpressionEClass, IQL_ASSIGNMENT_EXPRESSION__RIGHT_OPERAND);

    iqlLogicalOrExpressionEClass = createEClass(IQL_LOGICAL_OR_EXPRESSION);
    createEReference(iqlLogicalOrExpressionEClass, IQL_LOGICAL_OR_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlLogicalOrExpressionEClass, IQL_LOGICAL_OR_EXPRESSION__OP);
    createEReference(iqlLogicalOrExpressionEClass, IQL_LOGICAL_OR_EXPRESSION__RIGHT_OPERAND);

    iqlLogicalAndExpressionEClass = createEClass(IQL_LOGICAL_AND_EXPRESSION);
    createEReference(iqlLogicalAndExpressionEClass, IQL_LOGICAL_AND_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlLogicalAndExpressionEClass, IQL_LOGICAL_AND_EXPRESSION__OP);
    createEReference(iqlLogicalAndExpressionEClass, IQL_LOGICAL_AND_EXPRESSION__RIGHT_OPERAND);

    iqlEqualityExpressionEClass = createEClass(IQL_EQUALITY_EXPRESSION);
    createEReference(iqlEqualityExpressionEClass, IQL_EQUALITY_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlEqualityExpressionEClass, IQL_EQUALITY_EXPRESSION__OP);
    createEReference(iqlEqualityExpressionEClass, IQL_EQUALITY_EXPRESSION__RIGHT_OPERAND);

    iqlInstanceOfExpressionEClass = createEClass(IQL_INSTANCE_OF_EXPRESSION);
    createEReference(iqlInstanceOfExpressionEClass, IQL_INSTANCE_OF_EXPRESSION__LEFT_OPERAND);
    createEReference(iqlInstanceOfExpressionEClass, IQL_INSTANCE_OF_EXPRESSION__TARGET_REF);

    iqlRelationalExpressionEClass = createEClass(IQL_RELATIONAL_EXPRESSION);
    createEReference(iqlRelationalExpressionEClass, IQL_RELATIONAL_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlRelationalExpressionEClass, IQL_RELATIONAL_EXPRESSION__OP);
    createEReference(iqlRelationalExpressionEClass, IQL_RELATIONAL_EXPRESSION__RIGHT_OPERAND);

    iqlAdditiveExpressionEClass = createEClass(IQL_ADDITIVE_EXPRESSION);
    createEReference(iqlAdditiveExpressionEClass, IQL_ADDITIVE_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlAdditiveExpressionEClass, IQL_ADDITIVE_EXPRESSION__OP);
    createEReference(iqlAdditiveExpressionEClass, IQL_ADDITIVE_EXPRESSION__RIGHT_OPERAND);

    iqlMultiplicativeExpressionEClass = createEClass(IQL_MULTIPLICATIVE_EXPRESSION);
    createEReference(iqlMultiplicativeExpressionEClass, IQL_MULTIPLICATIVE_EXPRESSION__LEFT_OPERAND);
    createEAttribute(iqlMultiplicativeExpressionEClass, IQL_MULTIPLICATIVE_EXPRESSION__OP);
    createEReference(iqlMultiplicativeExpressionEClass, IQL_MULTIPLICATIVE_EXPRESSION__RIGHT_OPERAND);

    iqlPlusMinusExpressionEClass = createEClass(IQL_PLUS_MINUS_EXPRESSION);
    createEAttribute(iqlPlusMinusExpressionEClass, IQL_PLUS_MINUS_EXPRESSION__OP);
    createEReference(iqlPlusMinusExpressionEClass, IQL_PLUS_MINUS_EXPRESSION__OPERAND);

    iqlBooleanNotExpressionEClass = createEClass(IQL_BOOLEAN_NOT_EXPRESSION);
    createEAttribute(iqlBooleanNotExpressionEClass, IQL_BOOLEAN_NOT_EXPRESSION__OP);
    createEReference(iqlBooleanNotExpressionEClass, IQL_BOOLEAN_NOT_EXPRESSION__OPERAND);

    iqlPrefixExpressionEClass = createEClass(IQL_PREFIX_EXPRESSION);
    createEAttribute(iqlPrefixExpressionEClass, IQL_PREFIX_EXPRESSION__OP);
    createEReference(iqlPrefixExpressionEClass, IQL_PREFIX_EXPRESSION__OPERAND);

    iqlTypeCastExpressionEClass = createEClass(IQL_TYPE_CAST_EXPRESSION);
    createEReference(iqlTypeCastExpressionEClass, IQL_TYPE_CAST_EXPRESSION__TARGET_REF);
    createEReference(iqlTypeCastExpressionEClass, IQL_TYPE_CAST_EXPRESSION__OPERAND);

    iqlPostfixExpressionEClass = createEClass(IQL_POSTFIX_EXPRESSION);
    createEReference(iqlPostfixExpressionEClass, IQL_POSTFIX_EXPRESSION__OPERAND);
    createEAttribute(iqlPostfixExpressionEClass, IQL_POSTFIX_EXPRESSION__OP);

    iqlArrayExpressionEClass = createEClass(IQL_ARRAY_EXPRESSION);
    createEReference(iqlArrayExpressionEClass, IQL_ARRAY_EXPRESSION__LEFT_OPERAND);
    createEReference(iqlArrayExpressionEClass, IQL_ARRAY_EXPRESSION__EXPR);

    iqlMemberSelectionExpressionEClass = createEClass(IQL_MEMBER_SELECTION_EXPRESSION);
    createEReference(iqlMemberSelectionExpressionEClass, IQL_MEMBER_SELECTION_EXPRESSION__LEFT_OPERAND);
    createEReference(iqlMemberSelectionExpressionEClass, IQL_MEMBER_SELECTION_EXPRESSION__RIGHT_OPERAND);

    iqlAttributeSelectionEClass = createEClass(IQL_ATTRIBUTE_SELECTION);
    createEReference(iqlAttributeSelectionEClass, IQL_ATTRIBUTE_SELECTION__VAR);

    iqlMethodSelectionEClass = createEClass(IQL_METHOD_SELECTION);
    createEAttribute(iqlMethodSelectionEClass, IQL_METHOD_SELECTION__METHOD);
    createEReference(iqlMethodSelectionEClass, IQL_METHOD_SELECTION__ARGS);

    iqlTerminalExpressionVariableEClass = createEClass(IQL_TERMINAL_EXPRESSION_VARIABLE);
    createEReference(iqlTerminalExpressionVariableEClass, IQL_TERMINAL_EXPRESSION_VARIABLE__VAR);

    iqlTerminalExpressionThisEClass = createEClass(IQL_TERMINAL_EXPRESSION_THIS);

    iqlTerminalExpressionSuperEClass = createEClass(IQL_TERMINAL_EXPRESSION_SUPER);

    iqlTerminalExpressionParenthesisEClass = createEClass(IQL_TERMINAL_EXPRESSION_PARENTHESIS);
    createEReference(iqlTerminalExpressionParenthesisEClass, IQL_TERMINAL_EXPRESSION_PARENTHESIS__EXPR);

    iqlTerminalExpressionNewEClass = createEClass(IQL_TERMINAL_EXPRESSION_NEW);
    createEReference(iqlTerminalExpressionNewEClass, IQL_TERMINAL_EXPRESSION_NEW__REF);
    createEReference(iqlTerminalExpressionNewEClass, IQL_TERMINAL_EXPRESSION_NEW__ARGS_LIST);
    createEReference(iqlTerminalExpressionNewEClass, IQL_TERMINAL_EXPRESSION_NEW__ARGS_MAP);

    iqlLiteralExpressionIntEClass = createEClass(IQL_LITERAL_EXPRESSION_INT);
    createEAttribute(iqlLiteralExpressionIntEClass, IQL_LITERAL_EXPRESSION_INT__VALUE);

    iqlLiteralExpressionDoubleEClass = createEClass(IQL_LITERAL_EXPRESSION_DOUBLE);
    createEAttribute(iqlLiteralExpressionDoubleEClass, IQL_LITERAL_EXPRESSION_DOUBLE__VALUE);

    iqlLiteralExpressionStringEClass = createEClass(IQL_LITERAL_EXPRESSION_STRING);
    createEAttribute(iqlLiteralExpressionStringEClass, IQL_LITERAL_EXPRESSION_STRING__VALUE);

    iqlLiteralExpressionBooleanEClass = createEClass(IQL_LITERAL_EXPRESSION_BOOLEAN);
    createEAttribute(iqlLiteralExpressionBooleanEClass, IQL_LITERAL_EXPRESSION_BOOLEAN__VALUE);

    iqlLiteralExpressionCharEClass = createEClass(IQL_LITERAL_EXPRESSION_CHAR);
    createEAttribute(iqlLiteralExpressionCharEClass, IQL_LITERAL_EXPRESSION_CHAR__VALUE);

    iqlLiteralExpressionRangeEClass = createEClass(IQL_LITERAL_EXPRESSION_RANGE);
    createEAttribute(iqlLiteralExpressionRangeEClass, IQL_LITERAL_EXPRESSION_RANGE__VALUE);

    iqlLiteralExpressionNullEClass = createEClass(IQL_LITERAL_EXPRESSION_NULL);

    iqlLiteralExpressionListEClass = createEClass(IQL_LITERAL_EXPRESSION_LIST);
    createEReference(iqlLiteralExpressionListEClass, IQL_LITERAL_EXPRESSION_LIST__ELEMENTS);

    iqlLiteralExpressionMapEClass = createEClass(IQL_LITERAL_EXPRESSION_MAP);
    createEReference(iqlLiteralExpressionMapEClass, IQL_LITERAL_EXPRESSION_MAP__ELEMENTS);
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

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    iqlTypeDefEClass.getESuperTypes().add(theTypesPackage.getJvmGenericType());
    iqlSimpleTypeRefEClass.getESuperTypes().add(theTypesPackage.getJvmTypeReference());
    iqlArrayTypeRefEClass.getESuperTypes().add(theTypesPackage.getJvmTypeReference());
    iqlSimpleTypeEClass.getESuperTypes().add(theTypesPackage.getJvmType());
    iqlArrayTypeEClass.getESuperTypes().add(theTypesPackage.getJvmType());
    iqlVariableDeclarationEClass.getESuperTypes().add(theTypesPackage.getJvmFormalParameter());
    iqlClassEClass.getESuperTypes().add(this.getIQLTypeDef());
    iqlInterfaceEClass.getESuperTypes().add(this.getIQLTypeDef());
    iqlAttributeEClass.getESuperTypes().add(theTypesPackage.getJvmField());
    iqlMethodEClass.getESuperTypes().add(theTypesPackage.getJvmOperation());
    iqlMethodDeclarationMemberEClass.getESuperTypes().add(theTypesPackage.getJvmOperation());
    iqlJavaMemberEClass.getESuperTypes().add(theTypesPackage.getJvmMember());
    iqlMetadataValueSingleLongEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleDoubleEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleStringEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleBooleanEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleCharEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleTypeRefEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueSingleNullEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueListEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlMetadataValueMapEClass.getESuperTypes().add(this.getIQLMetadataValue());
    iqlStatementBlockEClass.getESuperTypes().add(this.getIQLStatement());
    iqlJavaStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlIfStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlWhileStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlDoWhileStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlForStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlForEachStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlSwitchStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlExpressionStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlVariableStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlConstructorCallStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlBreakStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlContinueStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlReturnStatementEClass.getESuperTypes().add(this.getIQLStatement());
    iqlAssignmentExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLogicalOrExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLogicalAndExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlEqualityExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlInstanceOfExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlRelationalExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlAdditiveExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlMultiplicativeExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlPlusMinusExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlBooleanNotExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlPrefixExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlTypeCastExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlPostfixExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlArrayExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlMemberSelectionExpressionEClass.getESuperTypes().add(this.getIQLExpression());
    iqlAttributeSelectionEClass.getESuperTypes().add(this.getIQLMemberSelection());
    iqlMethodSelectionEClass.getESuperTypes().add(this.getIQLMemberSelection());
    iqlTerminalExpressionVariableEClass.getESuperTypes().add(this.getIQLExpression());
    iqlTerminalExpressionThisEClass.getESuperTypes().add(this.getIQLExpression());
    iqlTerminalExpressionSuperEClass.getESuperTypes().add(this.getIQLExpression());
    iqlTerminalExpressionParenthesisEClass.getESuperTypes().add(this.getIQLExpression());
    iqlTerminalExpressionNewEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionIntEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionDoubleEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionStringEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionBooleanEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionCharEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionRangeEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionNullEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionListEClass.getESuperTypes().add(this.getIQLExpression());
    iqlLiteralExpressionMapEClass.getESuperTypes().add(this.getIQLExpression());

    // Initialize classes and features; add operations and parameters
    initEClass(iqlFileEClass, IQLFile.class, "IQLFile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLFile_Name(), ecorePackage.getEString(), "name", null, 0, 1, IQLFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLFile_Namespaces(), this.getIQLNamespace(), null, "namespaces", null, 0, -1, IQLFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLFile_Elements(), this.getIQLTypeDef(), null, "elements", null, 0, -1, IQLFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlTypeDefEClass, IQLTypeDef.class, "IQLTypeDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLTypeDef_Javametadata(), this.getIQLJavaMetadata(), null, "javametadata", null, 0, -1, IQLTypeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLTypeDef_ExtendedInterfaces(), theTypesPackage.getJvmTypeReference(), null, "extendedInterfaces", null, 0, -1, IQLTypeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlNamespaceEClass, IQLNamespace.class, "IQLNamespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLNamespace_ImportedNamespace(), ecorePackage.getEString(), "importedNamespace", null, 0, 1, IQLNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlJavaMetadataEClass, IQLJavaMetadata.class, "IQLJavaMetadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLJavaMetadata_Text(), this.getIQLJava(), null, "text", null, 0, 1, IQLJavaMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlSimpleTypeRefEClass, IQLSimpleTypeRef.class, "IQLSimpleTypeRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLSimpleTypeRef_Type(), this.getIQLSimpleType(), null, "type", null, 0, 1, IQLSimpleTypeRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArrayTypeRefEClass, IQLArrayTypeRef.class, "IQLArrayTypeRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLArrayTypeRef_Type(), this.getIQLArrayType(), null, "type", null, 0, 1, IQLArrayTypeRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlSimpleTypeEClass, IQLSimpleType.class, "IQLSimpleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLSimpleType_Type(), theTypesPackage.getJvmType(), null, "type", null, 0, 1, IQLSimpleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArrayTypeEClass, IQLArrayType.class, "IQLArrayType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLArrayType_Type(), theTypesPackage.getJvmType(), null, "type", null, 0, 1, IQLArrayType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLArrayType_Dimensions(), ecorePackage.getEString(), "dimensions", null, 0, -1, IQLArrayType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlVariableDeclarationEClass, IQLVariableDeclaration.class, "IQLVariableDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLVariableDeclaration_Ref(), theTypesPackage.getJvmTypeReference(), null, "ref", null, 0, 1, IQLVariableDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataListEClass, IQLMetadataList.class, "IQLMetadataList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMetadataList_Elements(), this.getIQLMetadata(), null, "elements", null, 0, -1, IQLMetadataList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataEClass, IQLMetadata.class, "IQLMetadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadata_Name(), ecorePackage.getEString(), "name", null, 0, 1, IQLMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLMetadata_Value(), this.getIQLMetadataValue(), null, "value", null, 0, 1, IQLMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueEClass, IQLMetadataValue.class, "IQLMetadataValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlMetadataValueMapElementEClass, IQLMetadataValueMapElement.class, "IQLMetadataValueMapElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMetadataValueMapElement_Key(), this.getIQLMetadataValue(), null, "key", null, 0, 1, IQLMetadataValueMapElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLMetadataValueMapElement_Value(), this.getIQLMetadataValue(), null, "value", null, 0, 1, IQLMetadataValueMapElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlVariableInitializationEClass, IQLVariableInitialization.class, "IQLVariableInitialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLVariableInitialization_ArgsList(), this.getIQLArgumentsList(), null, "argsList", null, 0, 1, IQLVariableInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLVariableInitialization_ArgsMap(), this.getIQLArgumentsMap(), null, "argsMap", null, 0, 1, IQLVariableInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLVariableInitialization_Value(), this.getIQLExpression(), null, "value", null, 0, 1, IQLVariableInitialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArgumentsListEClass, IQLArgumentsList.class, "IQLArgumentsList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLArgumentsList_Elements(), this.getIQLExpression(), null, "elements", null, 0, -1, IQLArgumentsList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArgumentsMapEClass, IQLArgumentsMap.class, "IQLArgumentsMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLArgumentsMap_Elements(), this.getIQLArgumentsMapKeyValue(), null, "elements", null, 0, -1, IQLArgumentsMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArgumentsMapKeyValueEClass, IQLArgumentsMapKeyValue.class, "IQLArgumentsMapKeyValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLArgumentsMapKeyValue_Key(), ecorePackage.getEString(), "key", null, 0, 1, IQLArgumentsMapKeyValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLArgumentsMapKeyValue_Value(), this.getIQLExpression(), null, "value", null, 0, 1, IQLArgumentsMapKeyValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlStatementEClass, IQLStatement.class, "IQLStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlCasePartEClass, IQLCasePart.class, "IQLCasePart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLCasePart_Expr(), this.getIQLExpression(), null, "expr", null, 0, 1, IQLCasePart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLCasePart_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLCasePart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlExpressionEClass, IQLExpression.class, "IQLExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlMemberSelectionEClass, IQLMemberSelection.class, "IQLMemberSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlLiteralExpressionMapKeyValueEClass, IQLLiteralExpressionMapKeyValue.class, "IQLLiteralExpressionMapKeyValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLLiteralExpressionMapKeyValue_Key(), this.getIQLExpression(), null, "key", null, 0, 1, IQLLiteralExpressionMapKeyValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLLiteralExpressionMapKeyValue_Value(), this.getIQLExpression(), null, "value", null, 0, 1, IQLLiteralExpressionMapKeyValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlJavaEClass, IQLJava.class, "IQLJava", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLJava_Text(), ecorePackage.getEString(), "text", null, 0, -1, IQLJava.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLJava_Keywords(), this.getIQLJavaKeywords(), null, "keywords", null, 0, -1, IQLJava.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlJavaKeywordsEClass, IQLJavaKeywords.class, "IQLJavaKeywords", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLJavaKeywords_Keyword(), ecorePackage.getEString(), "keyword", null, 0, 1, IQLJavaKeywords.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlClassEClass, IQLClass.class, "IQLClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLClass_ExtendedClass(), theTypesPackage.getJvmTypeReference(), null, "extendedClass", null, 0, 1, IQLClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlInterfaceEClass, IQLInterface.class, "IQLInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlAttributeEClass, IQLAttribute.class, "IQLAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLAttribute_Init(), this.getIQLVariableInitialization(), null, "init", null, 0, 1, IQLAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMethodEClass, IQLMethod.class, "IQLMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMethod_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMethodDeclarationMemberEClass, IQLMethodDeclarationMember.class, "IQLMethodDeclarationMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlJavaMemberEClass, IQLJavaMember.class, "IQLJavaMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLJavaMember_Text(), this.getIQLJava(), null, "text", null, 0, 1, IQLJavaMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleLongEClass, IQLMetadataValueSingleLong.class, "IQLMetadataValueSingleLong", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleLong_Value(), ecorePackage.getEInt(), "value", null, 0, 1, IQLMetadataValueSingleLong.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleDoubleEClass, IQLMetadataValueSingleDouble.class, "IQLMetadataValueSingleDouble", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleDouble_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, IQLMetadataValueSingleDouble.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleStringEClass, IQLMetadataValueSingleString.class, "IQLMetadataValueSingleString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleString_Value(), ecorePackage.getEString(), "value", null, 0, 1, IQLMetadataValueSingleString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleBooleanEClass, IQLMetadataValueSingleBoolean.class, "IQLMetadataValueSingleBoolean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleBoolean_Value(), ecorePackage.getEBoolean(), "value", null, 0, 1, IQLMetadataValueSingleBoolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleCharEClass, IQLMetadataValueSingleChar.class, "IQLMetadataValueSingleChar", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleChar_Value(), ecorePackage.getEChar(), "value", null, 0, 1, IQLMetadataValueSingleChar.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleTypeRefEClass, IQLMetadataValueSingleTypeRef.class, "IQLMetadataValueSingleTypeRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMetadataValueSingleTypeRef_Value(), theTypesPackage.getJvmTypeReference(), null, "value", null, 0, 1, IQLMetadataValueSingleTypeRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueSingleNullEClass, IQLMetadataValueSingleNull.class, "IQLMetadataValueSingleNull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMetadataValueSingleNull_Value(), ecorePackage.getEString(), "value", null, 0, 1, IQLMetadataValueSingleNull.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueListEClass, IQLMetadataValueList.class, "IQLMetadataValueList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMetadataValueList_Elements(), this.getIQLMetadataValue(), null, "elements", null, 0, -1, IQLMetadataValueList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMetadataValueMapEClass, IQLMetadataValueMap.class, "IQLMetadataValueMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMetadataValueMap_Elements(), this.getIQLMetadataValueMapElement(), null, "elements", null, 0, -1, IQLMetadataValueMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlStatementBlockEClass, IQLStatementBlock.class, "IQLStatementBlock", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLStatementBlock_Statements(), this.getIQLStatement(), null, "statements", null, 0, -1, IQLStatementBlock.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlJavaStatementEClass, IQLJavaStatement.class, "IQLJavaStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLJavaStatement_Text(), this.getIQLJava(), null, "text", null, 0, 1, IQLJavaStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlIfStatementEClass, IQLIfStatement.class, "IQLIfStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLIfStatement_Predicate(), this.getIQLExpression(), null, "predicate", null, 0, 1, IQLIfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLIfStatement_ThenBody(), this.getIQLStatement(), null, "thenBody", null, 0, 1, IQLIfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLIfStatement_ElseBody(), this.getIQLStatement(), null, "elseBody", null, 0, 1, IQLIfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlWhileStatementEClass, IQLWhileStatement.class, "IQLWhileStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLWhileStatement_Predicate(), this.getIQLExpression(), null, "predicate", null, 0, 1, IQLWhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLWhileStatement_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLWhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlDoWhileStatementEClass, IQLDoWhileStatement.class, "IQLDoWhileStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLDoWhileStatement_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLDoWhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLDoWhileStatement_Predicate(), this.getIQLExpression(), null, "predicate", null, 0, 1, IQLDoWhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlForStatementEClass, IQLForStatement.class, "IQLForStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLForStatement_Var(), this.getIQLStatement(), null, "var", null, 0, 1, IQLForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLForStatement_Predicate(), this.getIQLStatement(), null, "predicate", null, 0, 1, IQLForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLForStatement_UpdateExpr(), this.getIQLExpression(), null, "updateExpr", null, 0, 1, IQLForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLForStatement_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlForEachStatementEClass, IQLForEachStatement.class, "IQLForEachStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLForEachStatement_Var(), this.getIQLVariableDeclaration(), null, "var", null, 0, 1, IQLForEachStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLForEachStatement_ForExpression(), this.getIQLExpression(), null, "forExpression", null, 0, 1, IQLForEachStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLForEachStatement_Body(), this.getIQLStatement(), null, "body", null, 0, 1, IQLForEachStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlSwitchStatementEClass, IQLSwitchStatement.class, "IQLSwitchStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLSwitchStatement_Expr(), this.getIQLExpression(), null, "expr", null, 0, 1, IQLSwitchStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLSwitchStatement_Cases(), this.getIQLCasePart(), null, "cases", null, 0, -1, IQLSwitchStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLSwitchStatement_Default(), this.getIQLStatement(), null, "default", null, 0, 1, IQLSwitchStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlExpressionStatementEClass, IQLExpressionStatement.class, "IQLExpressionStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLExpressionStatement_Expression(), this.getIQLExpression(), null, "expression", null, 0, 1, IQLExpressionStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlVariableStatementEClass, IQLVariableStatement.class, "IQLVariableStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLVariableStatement_Var(), this.getIQLVariableDeclaration(), null, "var", null, 0, 1, IQLVariableStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLVariableStatement_Init(), this.getIQLVariableInitialization(), null, "init", null, 0, 1, IQLVariableStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlConstructorCallStatementEClass, IQLConstructorCallStatement.class, "IQLConstructorCallStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLConstructorCallStatement_Keyword(), ecorePackage.getEString(), "keyword", null, 0, 1, IQLConstructorCallStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLConstructorCallStatement_Args(), this.getIQLArgumentsList(), null, "args", null, 0, 1, IQLConstructorCallStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlBreakStatementEClass, IQLBreakStatement.class, "IQLBreakStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlContinueStatementEClass, IQLContinueStatement.class, "IQLContinueStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlReturnStatementEClass, IQLReturnStatement.class, "IQLReturnStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLReturnStatement_Expression(), this.getIQLExpression(), null, "expression", null, 0, 1, IQLReturnStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlAssignmentExpressionEClass, IQLAssignmentExpression.class, "IQLAssignmentExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLAssignmentExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLAssignmentExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLAssignmentExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLAssignmentExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLAssignmentExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLAssignmentExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLogicalOrExpressionEClass, IQLLogicalOrExpression.class, "IQLLogicalOrExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLLogicalOrExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLLogicalOrExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLLogicalOrExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLLogicalOrExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLLogicalOrExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLLogicalOrExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLogicalAndExpressionEClass, IQLLogicalAndExpression.class, "IQLLogicalAndExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLLogicalAndExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLLogicalAndExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLLogicalAndExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLLogicalAndExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLLogicalAndExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLLogicalAndExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlEqualityExpressionEClass, IQLEqualityExpression.class, "IQLEqualityExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLEqualityExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLEqualityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLEqualityExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLEqualityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLEqualityExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLEqualityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlInstanceOfExpressionEClass, IQLInstanceOfExpression.class, "IQLInstanceOfExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLInstanceOfExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLInstanceOfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLInstanceOfExpression_TargetRef(), theTypesPackage.getJvmTypeReference(), null, "targetRef", null, 0, 1, IQLInstanceOfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlRelationalExpressionEClass, IQLRelationalExpression.class, "IQLRelationalExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLRelationalExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLRelationalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLRelationalExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLRelationalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLRelationalExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLRelationalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlAdditiveExpressionEClass, IQLAdditiveExpression.class, "IQLAdditiveExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLAdditiveExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLAdditiveExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLAdditiveExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLAdditiveExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLAdditiveExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLAdditiveExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMultiplicativeExpressionEClass, IQLMultiplicativeExpression.class, "IQLMultiplicativeExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMultiplicativeExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLMultiplicativeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLMultiplicativeExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLMultiplicativeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLMultiplicativeExpression_RightOperand(), this.getIQLExpression(), null, "rightOperand", null, 0, 1, IQLMultiplicativeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlPlusMinusExpressionEClass, IQLPlusMinusExpression.class, "IQLPlusMinusExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLPlusMinusExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLPlusMinusExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLPlusMinusExpression_Operand(), this.getIQLExpression(), null, "operand", null, 0, 1, IQLPlusMinusExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlBooleanNotExpressionEClass, IQLBooleanNotExpression.class, "IQLBooleanNotExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLBooleanNotExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLBooleanNotExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLBooleanNotExpression_Operand(), this.getIQLExpression(), null, "operand", null, 0, 1, IQLBooleanNotExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlPrefixExpressionEClass, IQLPrefixExpression.class, "IQLPrefixExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLPrefixExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLPrefixExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLPrefixExpression_Operand(), this.getIQLExpression(), null, "operand", null, 0, 1, IQLPrefixExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlTypeCastExpressionEClass, IQLTypeCastExpression.class, "IQLTypeCastExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLTypeCastExpression_TargetRef(), theTypesPackage.getJvmTypeReference(), null, "targetRef", null, 0, 1, IQLTypeCastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLTypeCastExpression_Operand(), this.getIQLExpression(), null, "operand", null, 0, 1, IQLTypeCastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlPostfixExpressionEClass, IQLPostfixExpression.class, "IQLPostfixExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLPostfixExpression_Operand(), this.getIQLExpression(), null, "operand", null, 0, 1, IQLPostfixExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIQLPostfixExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, IQLPostfixExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlArrayExpressionEClass, IQLArrayExpression.class, "IQLArrayExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLArrayExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLArrayExpression_Expr(), this.getIQLExpression(), null, "expr", null, 0, 1, IQLArrayExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMemberSelectionExpressionEClass, IQLMemberSelectionExpression.class, "IQLMemberSelectionExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLMemberSelectionExpression_LeftOperand(), this.getIQLExpression(), null, "leftOperand", null, 0, 1, IQLMemberSelectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLMemberSelectionExpression_RightOperand(), this.getIQLMemberSelection(), null, "rightOperand", null, 0, 1, IQLMemberSelectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlAttributeSelectionEClass, IQLAttributeSelection.class, "IQLAttributeSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLAttributeSelection_Var(), theTypesPackage.getJvmField(), null, "var", null, 0, 1, IQLAttributeSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlMethodSelectionEClass, IQLMethodSelection.class, "IQLMethodSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLMethodSelection_Method(), ecorePackage.getEString(), "method", null, 0, 1, IQLMethodSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLMethodSelection_Args(), this.getIQLArgumentsList(), null, "args", null, 0, 1, IQLMethodSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlTerminalExpressionVariableEClass, IQLTerminalExpressionVariable.class, "IQLTerminalExpressionVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLTerminalExpressionVariable_Var(), this.getIQLVariableDeclaration(), null, "var", null, 0, 1, IQLTerminalExpressionVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlTerminalExpressionThisEClass, IQLTerminalExpressionThis.class, "IQLTerminalExpressionThis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlTerminalExpressionSuperEClass, IQLTerminalExpressionSuper.class, "IQLTerminalExpressionSuper", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlTerminalExpressionParenthesisEClass, IQLTerminalExpressionParenthesis.class, "IQLTerminalExpressionParenthesis", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLTerminalExpressionParenthesis_Expr(), this.getIQLExpression(), null, "expr", null, 0, 1, IQLTerminalExpressionParenthesis.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlTerminalExpressionNewEClass, IQLTerminalExpressionNew.class, "IQLTerminalExpressionNew", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLTerminalExpressionNew_Ref(), theTypesPackage.getJvmTypeReference(), null, "ref", null, 0, 1, IQLTerminalExpressionNew.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLTerminalExpressionNew_ArgsList(), this.getIQLArgumentsList(), null, "argsList", null, 0, 1, IQLTerminalExpressionNew.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIQLTerminalExpressionNew_ArgsMap(), this.getIQLArgumentsMap(), null, "argsMap", null, 0, 1, IQLTerminalExpressionNew.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionIntEClass, IQLLiteralExpressionInt.class, "IQLLiteralExpressionInt", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionInt_Value(), ecorePackage.getEInt(), "value", null, 0, 1, IQLLiteralExpressionInt.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionDoubleEClass, IQLLiteralExpressionDouble.class, "IQLLiteralExpressionDouble", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionDouble_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, IQLLiteralExpressionDouble.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionStringEClass, IQLLiteralExpressionString.class, "IQLLiteralExpressionString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionString_Value(), ecorePackage.getEString(), "value", null, 0, 1, IQLLiteralExpressionString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionBooleanEClass, IQLLiteralExpressionBoolean.class, "IQLLiteralExpressionBoolean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionBoolean_Value(), ecorePackage.getEBoolean(), "value", null, 0, 1, IQLLiteralExpressionBoolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionCharEClass, IQLLiteralExpressionChar.class, "IQLLiteralExpressionChar", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionChar_Value(), ecorePackage.getEChar(), "value", null, 0, 1, IQLLiteralExpressionChar.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionRangeEClass, IQLLiteralExpressionRange.class, "IQLLiteralExpressionRange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIQLLiteralExpressionRange_Value(), ecorePackage.getEString(), "value", null, 0, 1, IQLLiteralExpressionRange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionNullEClass, IQLLiteralExpressionNull.class, "IQLLiteralExpressionNull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(iqlLiteralExpressionListEClass, IQLLiteralExpressionList.class, "IQLLiteralExpressionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLLiteralExpressionList_Elements(), this.getIQLExpression(), null, "elements", null, 0, -1, IQLLiteralExpressionList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(iqlLiteralExpressionMapEClass, IQLLiteralExpressionMap.class, "IQLLiteralExpressionMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getIQLLiteralExpressionMap_Elements(), this.getIQLLiteralExpressionMapKeyValue(), null, "elements", null, 0, -1, IQLLiteralExpressionMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //BasicIQLPackageImpl
