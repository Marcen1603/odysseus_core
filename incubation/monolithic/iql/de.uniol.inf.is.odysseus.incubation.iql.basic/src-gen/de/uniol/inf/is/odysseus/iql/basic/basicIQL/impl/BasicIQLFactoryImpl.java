/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.*;

import org.eclipse.emf.ecore.EClass;
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
public class BasicIQLFactoryImpl extends EFactoryImpl implements BasicIQLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static BasicIQLFactory init()
  {
    try
    {
      BasicIQLFactory theBasicIQLFactory = (BasicIQLFactory)EPackage.Registry.INSTANCE.getEFactory(BasicIQLPackage.eNS_URI);
      if (theBasicIQLFactory != null)
      {
        return theBasicIQLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new BasicIQLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasicIQLFactoryImpl()
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
      case BasicIQLPackage.IQL_FILE: return createIQLFile();
      case BasicIQLPackage.IQL_TYPE_DEF: return createIQLTypeDef();
      case BasicIQLPackage.IQL_NAMESPACE: return createIQLNamespace();
      case BasicIQLPackage.IQL_JAVA_METADATA: return createIQLJavaMetadata();
      case BasicIQLPackage.IQL_SIMPLE_TYPE_REF: return createIQLSimpleTypeRef();
      case BasicIQLPackage.IQL_ARRAY_TYPE_REF: return createIQLArrayTypeRef();
      case BasicIQLPackage.IQL_SIMPLE_TYPE: return createIQLSimpleType();
      case BasicIQLPackage.IQL_ARRAY_TYPE: return createIQLArrayType();
      case BasicIQLPackage.IQL_VARIABLE_DECLARATION: return createIQLVariableDeclaration();
      case BasicIQLPackage.IQL_METADATA_LIST: return createIQLMetadataList();
      case BasicIQLPackage.IQL_METADATA: return createIQLMetadata();
      case BasicIQLPackage.IQL_METADATA_VALUE: return createIQLMetadataValue();
      case BasicIQLPackage.IQL_METADATA_VALUE_MAP_ELEMENT: return createIQLMetadataValueMapElement();
      case BasicIQLPackage.IQL_VARIABLE_INITIALIZATION: return createIQLVariableInitialization();
      case BasicIQLPackage.IQL_ARGUMENTS_LIST: return createIQLArgumentsList();
      case BasicIQLPackage.IQL_ARGUMENTS_MAP: return createIQLArgumentsMap();
      case BasicIQLPackage.IQL_ARGUMENTS_MAP_KEY_VALUE: return createIQLArgumentsMapKeyValue();
      case BasicIQLPackage.IQL_STATEMENT: return createIQLStatement();
      case BasicIQLPackage.IQL_CASE_PART: return createIQLCasePart();
      case BasicIQLPackage.IQL_EXPRESSION: return createIQLExpression();
      case BasicIQLPackage.IQL_MEMBER_SELECTION: return createIQLMemberSelection();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP_KEY_VALUE: return createIQLLiteralExpressionMapKeyValue();
      case BasicIQLPackage.IQL_JAVA: return createIQLJava();
      case BasicIQLPackage.IQL_JAVA_KEYWORDS: return createIQLJavaKeywords();
      case BasicIQLPackage.IQL_CLASS: return createIQLClass();
      case BasicIQLPackage.IQL_INTERFACE: return createIQLInterface();
      case BasicIQLPackage.IQL_ATTRIBUTE: return createIQLAttribute();
      case BasicIQLPackage.IQL_METHOD: return createIQLMethod();
      case BasicIQLPackage.IQL_METHOD_DECLARATION_MEMBER: return createIQLMethodDeclarationMember();
      case BasicIQLPackage.IQL_JAVA_MEMBER: return createIQLJavaMember();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_LONG: return createIQLMetadataValueSingleLong();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_DOUBLE: return createIQLMetadataValueSingleDouble();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_STRING: return createIQLMetadataValueSingleString();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_BOOLEAN: return createIQLMetadataValueSingleBoolean();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_CHAR: return createIQLMetadataValueSingleChar();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_TYPE_REF: return createIQLMetadataValueSingleTypeRef();
      case BasicIQLPackage.IQL_METADATA_VALUE_SINGLE_NULL: return createIQLMetadataValueSingleNull();
      case BasicIQLPackage.IQL_METADATA_VALUE_LIST: return createIQLMetadataValueList();
      case BasicIQLPackage.IQL_METADATA_VALUE_MAP: return createIQLMetadataValueMap();
      case BasicIQLPackage.IQL_STATEMENT_BLOCK: return createIQLStatementBlock();
      case BasicIQLPackage.IQL_JAVA_STATEMENT: return createIQLJavaStatement();
      case BasicIQLPackage.IQL_IF_STATEMENT: return createIQLIfStatement();
      case BasicIQLPackage.IQL_WHILE_STATEMENT: return createIQLWhileStatement();
      case BasicIQLPackage.IQL_DO_WHILE_STATEMENT: return createIQLDoWhileStatement();
      case BasicIQLPackage.IQL_FOR_STATEMENT: return createIQLForStatement();
      case BasicIQLPackage.IQL_FOR_EACH_STATEMENT: return createIQLForEachStatement();
      case BasicIQLPackage.IQL_SWITCH_STATEMENT: return createIQLSwitchStatement();
      case BasicIQLPackage.IQL_EXPRESSION_STATEMENT: return createIQLExpressionStatement();
      case BasicIQLPackage.IQL_VARIABLE_STATEMENT: return createIQLVariableStatement();
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT: return createIQLConstructorCallStatement();
      case BasicIQLPackage.IQL_BREAK_STATEMENT: return createIQLBreakStatement();
      case BasicIQLPackage.IQL_CONTINUE_STATEMENT: return createIQLContinueStatement();
      case BasicIQLPackage.IQL_RETURN_STATEMENT: return createIQLReturnStatement();
      case BasicIQLPackage.IQL_ASSIGNMENT_EXPRESSION: return createIQLAssignmentExpression();
      case BasicIQLPackage.IQL_LOGICAL_OR_EXPRESSION: return createIQLLogicalOrExpression();
      case BasicIQLPackage.IQL_LOGICAL_AND_EXPRESSION: return createIQLLogicalAndExpression();
      case BasicIQLPackage.IQL_EQUALITY_EXPRESSION: return createIQLEqualityExpression();
      case BasicIQLPackage.IQL_INSTANCE_OF_EXPRESSION: return createIQLInstanceOfExpression();
      case BasicIQLPackage.IQL_RELATIONAL_EXPRESSION: return createIQLRelationalExpression();
      case BasicIQLPackage.IQL_ADDITIVE_EXPRESSION: return createIQLAdditiveExpression();
      case BasicIQLPackage.IQL_MULTIPLICATIVE_EXPRESSION: return createIQLMultiplicativeExpression();
      case BasicIQLPackage.IQL_PLUS_MINUS_EXPRESSION: return createIQLPlusMinusExpression();
      case BasicIQLPackage.IQL_BOOLEAN_NOT_EXPRESSION: return createIQLBooleanNotExpression();
      case BasicIQLPackage.IQL_PREFIX_EXPRESSION: return createIQLPrefixExpression();
      case BasicIQLPackage.IQL_TYPE_CAST_EXPRESSION: return createIQLTypeCastExpression();
      case BasicIQLPackage.IQL_POSTFIX_EXPRESSION: return createIQLPostfixExpression();
      case BasicIQLPackage.IQL_ARRAY_EXPRESSION: return createIQLArrayExpression();
      case BasicIQLPackage.IQL_MEMBER_SELECTION_EXPRESSION: return createIQLMemberSelectionExpression();
      case BasicIQLPackage.IQL_ATTRIBUTE_SELECTION: return createIQLAttributeSelection();
      case BasicIQLPackage.IQL_METHOD_SELECTION: return createIQLMethodSelection();
      case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_VARIABLE: return createIQLTerminalExpressionVariable();
      case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_THIS: return createIQLTerminalExpressionThis();
      case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_SUPER: return createIQLTerminalExpressionSuper();
      case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_PARENTHESIS: return createIQLTerminalExpressionParenthesis();
      case BasicIQLPackage.IQL_TERMINAL_EXPRESSION_NEW: return createIQLTerminalExpressionNew();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_INT: return createIQLLiteralExpressionInt();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_DOUBLE: return createIQLLiteralExpressionDouble();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_STRING: return createIQLLiteralExpressionString();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_BOOLEAN: return createIQLLiteralExpressionBoolean();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_CHAR: return createIQLLiteralExpressionChar();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_RANGE: return createIQLLiteralExpressionRange();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_NULL: return createIQLLiteralExpressionNull();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_LIST: return createIQLLiteralExpressionList();
      case BasicIQLPackage.IQL_LITERAL_EXPRESSION_MAP: return createIQLLiteralExpressionMap();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLFile createIQLFile()
  {
    IQLFileImpl iqlFile = new IQLFileImpl();
    return iqlFile;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTypeDef createIQLTypeDef()
  {
    IQLTypeDefImpl iqlTypeDef = new IQLTypeDefImpl();
    return iqlTypeDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLNamespace createIQLNamespace()
  {
    IQLNamespaceImpl iqlNamespace = new IQLNamespaceImpl();
    return iqlNamespace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLJavaMetadata createIQLJavaMetadata()
  {
    IQLJavaMetadataImpl iqlJavaMetadata = new IQLJavaMetadataImpl();
    return iqlJavaMetadata;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLSimpleTypeRef createIQLSimpleTypeRef()
  {
    IQLSimpleTypeRefImpl iqlSimpleTypeRef = new IQLSimpleTypeRefImpl();
    return iqlSimpleTypeRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArrayTypeRef createIQLArrayTypeRef()
  {
    IQLArrayTypeRefImpl iqlArrayTypeRef = new IQLArrayTypeRefImpl();
    return iqlArrayTypeRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLSimpleType createIQLSimpleType()
  {
    IQLSimpleTypeImpl iqlSimpleType = new IQLSimpleTypeImpl();
    return iqlSimpleType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArrayType createIQLArrayType()
  {
    IQLArrayTypeImpl iqlArrayType = new IQLArrayTypeImpl();
    return iqlArrayType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLVariableDeclaration createIQLVariableDeclaration()
  {
    IQLVariableDeclarationImpl iqlVariableDeclaration = new IQLVariableDeclarationImpl();
    return iqlVariableDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataList createIQLMetadataList()
  {
    IQLMetadataListImpl iqlMetadataList = new IQLMetadataListImpl();
    return iqlMetadataList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadata createIQLMetadata()
  {
    IQLMetadataImpl iqlMetadata = new IQLMetadataImpl();
    return iqlMetadata;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValue createIQLMetadataValue()
  {
    IQLMetadataValueImpl iqlMetadataValue = new IQLMetadataValueImpl();
    return iqlMetadataValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueMapElement createIQLMetadataValueMapElement()
  {
    IQLMetadataValueMapElementImpl iqlMetadataValueMapElement = new IQLMetadataValueMapElementImpl();
    return iqlMetadataValueMapElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLVariableInitialization createIQLVariableInitialization()
  {
    IQLVariableInitializationImpl iqlVariableInitialization = new IQLVariableInitializationImpl();
    return iqlVariableInitialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsList createIQLArgumentsList()
  {
    IQLArgumentsListImpl iqlArgumentsList = new IQLArgumentsListImpl();
    return iqlArgumentsList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsMap createIQLArgumentsMap()
  {
    IQLArgumentsMapImpl iqlArgumentsMap = new IQLArgumentsMapImpl();
    return iqlArgumentsMap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsMapKeyValue createIQLArgumentsMapKeyValue()
  {
    IQLArgumentsMapKeyValueImpl iqlArgumentsMapKeyValue = new IQLArgumentsMapKeyValueImpl();
    return iqlArgumentsMapKeyValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement createIQLStatement()
  {
    IQLStatementImpl iqlStatement = new IQLStatementImpl();
    return iqlStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLCasePart createIQLCasePart()
  {
    IQLCasePartImpl iqlCasePart = new IQLCasePartImpl();
    return iqlCasePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpression createIQLExpression()
  {
    IQLExpressionImpl iqlExpression = new IQLExpressionImpl();
    return iqlExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMemberSelection createIQLMemberSelection()
  {
    IQLMemberSelectionImpl iqlMemberSelection = new IQLMemberSelectionImpl();
    return iqlMemberSelection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionMapKeyValue createIQLLiteralExpressionMapKeyValue()
  {
    IQLLiteralExpressionMapKeyValueImpl iqlLiteralExpressionMapKeyValue = new IQLLiteralExpressionMapKeyValueImpl();
    return iqlLiteralExpressionMapKeyValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLJava createIQLJava()
  {
    IQLJavaImpl iqlJava = new IQLJavaImpl();
    return iqlJava;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLJavaKeywords createIQLJavaKeywords()
  {
    IQLJavaKeywordsImpl iqlJavaKeywords = new IQLJavaKeywordsImpl();
    return iqlJavaKeywords;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLClass createIQLClass()
  {
    IQLClassImpl iqlClass = new IQLClassImpl();
    return iqlClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLInterface createIQLInterface()
  {
    IQLInterfaceImpl iqlInterface = new IQLInterfaceImpl();
    return iqlInterface;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLAttribute createIQLAttribute()
  {
    IQLAttributeImpl iqlAttribute = new IQLAttributeImpl();
    return iqlAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMethod createIQLMethod()
  {
    IQLMethodImpl iqlMethod = new IQLMethodImpl();
    return iqlMethod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMethodDeclarationMember createIQLMethodDeclarationMember()
  {
    IQLMethodDeclarationMemberImpl iqlMethodDeclarationMember = new IQLMethodDeclarationMemberImpl();
    return iqlMethodDeclarationMember;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLJavaMember createIQLJavaMember()
  {
    IQLJavaMemberImpl iqlJavaMember = new IQLJavaMemberImpl();
    return iqlJavaMember;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleLong createIQLMetadataValueSingleLong()
  {
    IQLMetadataValueSingleLongImpl iqlMetadataValueSingleLong = new IQLMetadataValueSingleLongImpl();
    return iqlMetadataValueSingleLong;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleDouble createIQLMetadataValueSingleDouble()
  {
    IQLMetadataValueSingleDoubleImpl iqlMetadataValueSingleDouble = new IQLMetadataValueSingleDoubleImpl();
    return iqlMetadataValueSingleDouble;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleString createIQLMetadataValueSingleString()
  {
    IQLMetadataValueSingleStringImpl iqlMetadataValueSingleString = new IQLMetadataValueSingleStringImpl();
    return iqlMetadataValueSingleString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleBoolean createIQLMetadataValueSingleBoolean()
  {
    IQLMetadataValueSingleBooleanImpl iqlMetadataValueSingleBoolean = new IQLMetadataValueSingleBooleanImpl();
    return iqlMetadataValueSingleBoolean;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleChar createIQLMetadataValueSingleChar()
  {
    IQLMetadataValueSingleCharImpl iqlMetadataValueSingleChar = new IQLMetadataValueSingleCharImpl();
    return iqlMetadataValueSingleChar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleTypeRef createIQLMetadataValueSingleTypeRef()
  {
    IQLMetadataValueSingleTypeRefImpl iqlMetadataValueSingleTypeRef = new IQLMetadataValueSingleTypeRefImpl();
    return iqlMetadataValueSingleTypeRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleNull createIQLMetadataValueSingleNull()
  {
    IQLMetadataValueSingleNullImpl iqlMetadataValueSingleNull = new IQLMetadataValueSingleNullImpl();
    return iqlMetadataValueSingleNull;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueList createIQLMetadataValueList()
  {
    IQLMetadataValueListImpl iqlMetadataValueList = new IQLMetadataValueListImpl();
    return iqlMetadataValueList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueMap createIQLMetadataValueMap()
  {
    IQLMetadataValueMapImpl iqlMetadataValueMap = new IQLMetadataValueMapImpl();
    return iqlMetadataValueMap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatementBlock createIQLStatementBlock()
  {
    IQLStatementBlockImpl iqlStatementBlock = new IQLStatementBlockImpl();
    return iqlStatementBlock;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLJavaStatement createIQLJavaStatement()
  {
    IQLJavaStatementImpl iqlJavaStatement = new IQLJavaStatementImpl();
    return iqlJavaStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLIfStatement createIQLIfStatement()
  {
    IQLIfStatementImpl iqlIfStatement = new IQLIfStatementImpl();
    return iqlIfStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLWhileStatement createIQLWhileStatement()
  {
    IQLWhileStatementImpl iqlWhileStatement = new IQLWhileStatementImpl();
    return iqlWhileStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLDoWhileStatement createIQLDoWhileStatement()
  {
    IQLDoWhileStatementImpl iqlDoWhileStatement = new IQLDoWhileStatementImpl();
    return iqlDoWhileStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLForStatement createIQLForStatement()
  {
    IQLForStatementImpl iqlForStatement = new IQLForStatementImpl();
    return iqlForStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLForEachStatement createIQLForEachStatement()
  {
    IQLForEachStatementImpl iqlForEachStatement = new IQLForEachStatementImpl();
    return iqlForEachStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLSwitchStatement createIQLSwitchStatement()
  {
    IQLSwitchStatementImpl iqlSwitchStatement = new IQLSwitchStatementImpl();
    return iqlSwitchStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLExpressionStatement createIQLExpressionStatement()
  {
    IQLExpressionStatementImpl iqlExpressionStatement = new IQLExpressionStatementImpl();
    return iqlExpressionStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLVariableStatement createIQLVariableStatement()
  {
    IQLVariableStatementImpl iqlVariableStatement = new IQLVariableStatementImpl();
    return iqlVariableStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLConstructorCallStatement createIQLConstructorCallStatement()
  {
    IQLConstructorCallStatementImpl iqlConstructorCallStatement = new IQLConstructorCallStatementImpl();
    return iqlConstructorCallStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLBreakStatement createIQLBreakStatement()
  {
    IQLBreakStatementImpl iqlBreakStatement = new IQLBreakStatementImpl();
    return iqlBreakStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLContinueStatement createIQLContinueStatement()
  {
    IQLContinueStatementImpl iqlContinueStatement = new IQLContinueStatementImpl();
    return iqlContinueStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLReturnStatement createIQLReturnStatement()
  {
    IQLReturnStatementImpl iqlReturnStatement = new IQLReturnStatementImpl();
    return iqlReturnStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLAssignmentExpression createIQLAssignmentExpression()
  {
    IQLAssignmentExpressionImpl iqlAssignmentExpression = new IQLAssignmentExpressionImpl();
    return iqlAssignmentExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLogicalOrExpression createIQLLogicalOrExpression()
  {
    IQLLogicalOrExpressionImpl iqlLogicalOrExpression = new IQLLogicalOrExpressionImpl();
    return iqlLogicalOrExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLogicalAndExpression createIQLLogicalAndExpression()
  {
    IQLLogicalAndExpressionImpl iqlLogicalAndExpression = new IQLLogicalAndExpressionImpl();
    return iqlLogicalAndExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLEqualityExpression createIQLEqualityExpression()
  {
    IQLEqualityExpressionImpl iqlEqualityExpression = new IQLEqualityExpressionImpl();
    return iqlEqualityExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLInstanceOfExpression createIQLInstanceOfExpression()
  {
    IQLInstanceOfExpressionImpl iqlInstanceOfExpression = new IQLInstanceOfExpressionImpl();
    return iqlInstanceOfExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLRelationalExpression createIQLRelationalExpression()
  {
    IQLRelationalExpressionImpl iqlRelationalExpression = new IQLRelationalExpressionImpl();
    return iqlRelationalExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLAdditiveExpression createIQLAdditiveExpression()
  {
    IQLAdditiveExpressionImpl iqlAdditiveExpression = new IQLAdditiveExpressionImpl();
    return iqlAdditiveExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMultiplicativeExpression createIQLMultiplicativeExpression()
  {
    IQLMultiplicativeExpressionImpl iqlMultiplicativeExpression = new IQLMultiplicativeExpressionImpl();
    return iqlMultiplicativeExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLPlusMinusExpression createIQLPlusMinusExpression()
  {
    IQLPlusMinusExpressionImpl iqlPlusMinusExpression = new IQLPlusMinusExpressionImpl();
    return iqlPlusMinusExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLBooleanNotExpression createIQLBooleanNotExpression()
  {
    IQLBooleanNotExpressionImpl iqlBooleanNotExpression = new IQLBooleanNotExpressionImpl();
    return iqlBooleanNotExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLPrefixExpression createIQLPrefixExpression()
  {
    IQLPrefixExpressionImpl iqlPrefixExpression = new IQLPrefixExpressionImpl();
    return iqlPrefixExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTypeCastExpression createIQLTypeCastExpression()
  {
    IQLTypeCastExpressionImpl iqlTypeCastExpression = new IQLTypeCastExpressionImpl();
    return iqlTypeCastExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLPostfixExpression createIQLPostfixExpression()
  {
    IQLPostfixExpressionImpl iqlPostfixExpression = new IQLPostfixExpressionImpl();
    return iqlPostfixExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArrayExpression createIQLArrayExpression()
  {
    IQLArrayExpressionImpl iqlArrayExpression = new IQLArrayExpressionImpl();
    return iqlArrayExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMemberSelectionExpression createIQLMemberSelectionExpression()
  {
    IQLMemberSelectionExpressionImpl iqlMemberSelectionExpression = new IQLMemberSelectionExpressionImpl();
    return iqlMemberSelectionExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLAttributeSelection createIQLAttributeSelection()
  {
    IQLAttributeSelectionImpl iqlAttributeSelection = new IQLAttributeSelectionImpl();
    return iqlAttributeSelection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMethodSelection createIQLMethodSelection()
  {
    IQLMethodSelectionImpl iqlMethodSelection = new IQLMethodSelectionImpl();
    return iqlMethodSelection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTerminalExpressionVariable createIQLTerminalExpressionVariable()
  {
    IQLTerminalExpressionVariableImpl iqlTerminalExpressionVariable = new IQLTerminalExpressionVariableImpl();
    return iqlTerminalExpressionVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTerminalExpressionThis createIQLTerminalExpressionThis()
  {
    IQLTerminalExpressionThisImpl iqlTerminalExpressionThis = new IQLTerminalExpressionThisImpl();
    return iqlTerminalExpressionThis;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTerminalExpressionSuper createIQLTerminalExpressionSuper()
  {
    IQLTerminalExpressionSuperImpl iqlTerminalExpressionSuper = new IQLTerminalExpressionSuperImpl();
    return iqlTerminalExpressionSuper;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTerminalExpressionParenthesis createIQLTerminalExpressionParenthesis()
  {
    IQLTerminalExpressionParenthesisImpl iqlTerminalExpressionParenthesis = new IQLTerminalExpressionParenthesisImpl();
    return iqlTerminalExpressionParenthesis;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLTerminalExpressionNew createIQLTerminalExpressionNew()
  {
    IQLTerminalExpressionNewImpl iqlTerminalExpressionNew = new IQLTerminalExpressionNewImpl();
    return iqlTerminalExpressionNew;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionInt createIQLLiteralExpressionInt()
  {
    IQLLiteralExpressionIntImpl iqlLiteralExpressionInt = new IQLLiteralExpressionIntImpl();
    return iqlLiteralExpressionInt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionDouble createIQLLiteralExpressionDouble()
  {
    IQLLiteralExpressionDoubleImpl iqlLiteralExpressionDouble = new IQLLiteralExpressionDoubleImpl();
    return iqlLiteralExpressionDouble;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionString createIQLLiteralExpressionString()
  {
    IQLLiteralExpressionStringImpl iqlLiteralExpressionString = new IQLLiteralExpressionStringImpl();
    return iqlLiteralExpressionString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionBoolean createIQLLiteralExpressionBoolean()
  {
    IQLLiteralExpressionBooleanImpl iqlLiteralExpressionBoolean = new IQLLiteralExpressionBooleanImpl();
    return iqlLiteralExpressionBoolean;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionChar createIQLLiteralExpressionChar()
  {
    IQLLiteralExpressionCharImpl iqlLiteralExpressionChar = new IQLLiteralExpressionCharImpl();
    return iqlLiteralExpressionChar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionRange createIQLLiteralExpressionRange()
  {
    IQLLiteralExpressionRangeImpl iqlLiteralExpressionRange = new IQLLiteralExpressionRangeImpl();
    return iqlLiteralExpressionRange;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionNull createIQLLiteralExpressionNull()
  {
    IQLLiteralExpressionNullImpl iqlLiteralExpressionNull = new IQLLiteralExpressionNullImpl();
    return iqlLiteralExpressionNull;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionList createIQLLiteralExpressionList()
  {
    IQLLiteralExpressionListImpl iqlLiteralExpressionList = new IQLLiteralExpressionListImpl();
    return iqlLiteralExpressionList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLLiteralExpressionMap createIQLLiteralExpressionMap()
  {
    IQLLiteralExpressionMapImpl iqlLiteralExpressionMap = new IQLLiteralExpressionMapImpl();
    return iqlLiteralExpressionMap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasicIQLPackage getBasicIQLPackage()
  {
    return (BasicIQLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static BasicIQLPackage getPackage()
  {
    return BasicIQLPackage.eINSTANCE;
  }

} //BasicIQLFactoryImpl
