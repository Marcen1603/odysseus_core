/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.*;

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
public class CQLFactoryImpl extends EFactoryImpl implements CQLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static CQLFactory init()
  {
    try
    {
      CQLFactory theCQLFactory = (CQLFactory)EPackage.Registry.INSTANCE.getEFactory(CQLPackage.eNS_URI);
      if (theCQLFactory != null)
      {
        return theCQLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new CQLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CQLFactoryImpl()
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
      case CQLPackage.MODEL: return createModel();
      case CQLPackage.STATEMENT: return createStatement();
      case CQLPackage.COMMAND: return createCommand();
      case CQLPackage.SELECT: return createSelect();
      case CQLPackage.INNER_SELECT: return createInnerSelect();
      case CQLPackage.INNER_SELECT2: return createInnerSelect2();
      case CQLPackage.SELECT_ARGUMENT: return createSelectArgument();
      case CQLPackage.SOURCE: return createSource();
      case CQLPackage.ATTRIBUTE: return createAttribute();
      case CQLPackage.ATTRIBUTE_WITH_NESTED_STATEMENT: return createAttributeWithNestedStatement();
      case CQLPackage.SELECT_EXPRESSION: return createSelectExpression();
      case CQLPackage.EXPRESSION_COMPONENT: return createExpressionComponent();
      case CQLPackage.SET_OPERATOR: return createSetOperator();
      case CQLPackage.ALIAS: return createAlias();
      case CQLPackage.ACCESS_FRAMEWORK: return createAccessFramework();
      case CQLPackage.SCHEMA_DEFINITION: return createSchemaDefinition();
      case CQLPackage.CREATE: return createCreate();
      case CQLPackage.CREATE_ACCESS_FRAMEWORK: return createCreateAccessFramework();
      case CQLPackage.CREATE_CHANNEL_FRAMEWORK_VIA_PORT: return createCreateChannelFrameworkViaPort();
      case CQLPackage.CREATE_CHANNEL_FORMAT_VIA_FILE: return createCreateChannelFormatViaFile();
      case CQLPackage.CREATE_DATABASE_STREAM: return createCreateDatabaseStream();
      case CQLPackage.CREATE_DATABASE_SINK: return createCreateDatabaseSink();
      case CQLPackage.CREATE_VIEW: return createCreateView();
      case CQLPackage.CONTEXT_STORE_TYPE: return createContextStoreType();
      case CQLPackage.STREAM_TO: return createStreamTo();
      case CQLPackage.WINDOW_OPERATOR: return createWindowOperator();
      case CQLPackage.EXPRESSIONS_MODEL: return createExpressionsModel();
      case CQLPackage.EXPRESSION: return createExpression();
      case CQLPackage.DATA_TYPE: return createDataType();
      case CQLPackage.SIMPLE_SOURCE: return createSimpleSource();
      case CQLPackage.NESTED_SOURCE: return createNestedSource();
      case CQLPackage.FUNCTION: return createFunction();
      case CQLPackage.EXPRESSION_COMPONENT_AS_ATTRIBUTE: return createExpressionComponentAsAttribute();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC: return createCreateDataBaseConnectionJDBC();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC: return createCreateDataBaseConnectionGeneric();
      case CQLPackage.DROP_DATABASE_CONNECTION: return createDropDatabaseConnection();
      case CQLPackage.CREATE_CONTEXT_STORE: return createCreateContextStore();
      case CQLPackage.DROP_CONTEXT_STORE: return createDropContextStore();
      case CQLPackage.DROP_STREAM: return createDropStream();
      case CQLPackage.USER_MANAGEMENT: return createUserManagement();
      case CQLPackage.RIGHTS_MANAGEMENT: return createRightsManagement();
      case CQLPackage.ROLE_MANAGEMENT: return createRoleManagement();
      case CQLPackage.UNDBOUNDED_WINDOW: return createUndboundedWindow();
      case CQLPackage.TIMEBASED_WINDOW: return createTimebasedWindow();
      case CQLPackage.TUPLEBASED_WINDOW: return createTuplebasedWindow();
      case CQLPackage.OR_PREDICATE: return createOrPredicate();
      case CQLPackage.AND_PREDICATE: return createAndPredicate();
      case CQLPackage.EQUALITY: return createEquality();
      case CQLPackage.COMPARISION: return createComparision();
      case CQLPackage.PLUS: return createPlus();
      case CQLPackage.MINUS: return createMinus();
      case CQLPackage.MUL_OR_DIV: return createMulOrDiv();
      case CQLPackage.BRACKET: return createBracket();
      case CQLPackage.NOT: return createNOT();
      case CQLPackage.INT_CONSTANT: return createIntConstant();
      case CQLPackage.FLOAT_CONSTANT: return createFloatConstant();
      case CQLPackage.STRING_CONSTANT: return createStringConstant();
      case CQLPackage.BOOL_CONSTANT: return createBoolConstant();
      case CQLPackage.ATTRIBUTE_REF: return createAttributeRef();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Model createModel()
  {
    ModelImpl model = new ModelImpl();
    return model;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Statement createStatement()
  {
    StatementImpl statement = new StatementImpl();
    return statement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Command createCommand()
  {
    CommandImpl command = new CommandImpl();
    return command;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Select createSelect()
  {
    SelectImpl select = new SelectImpl();
    return select;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InnerSelect createInnerSelect()
  {
    InnerSelectImpl innerSelect = new InnerSelectImpl();
    return innerSelect;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InnerSelect2 createInnerSelect2()
  {
    InnerSelect2Impl innerSelect2 = new InnerSelect2Impl();
    return innerSelect2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SelectArgument createSelectArgument()
  {
    SelectArgumentImpl selectArgument = new SelectArgumentImpl();
    return selectArgument;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Source createSource()
  {
    SourceImpl source = new SourceImpl();
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Attribute createAttribute()
  {
    AttributeImpl attribute = new AttributeImpl();
    return attribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttributeWithNestedStatement createAttributeWithNestedStatement()
  {
    AttributeWithNestedStatementImpl attributeWithNestedStatement = new AttributeWithNestedStatementImpl();
    return attributeWithNestedStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SelectExpression createSelectExpression()
  {
    SelectExpressionImpl selectExpression = new SelectExpressionImpl();
    return selectExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionComponent createExpressionComponent()
  {
    ExpressionComponentImpl expressionComponent = new ExpressionComponentImpl();
    return expressionComponent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetOperator createSetOperator()
  {
    SetOperatorImpl setOperator = new SetOperatorImpl();
    return setOperator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Alias createAlias()
  {
    AliasImpl alias = new AliasImpl();
    return alias;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AccessFramework createAccessFramework()
  {
    AccessFrameworkImpl accessFramework = new AccessFrameworkImpl();
    return accessFramework;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SchemaDefinition createSchemaDefinition()
  {
    SchemaDefinitionImpl schemaDefinition = new SchemaDefinitionImpl();
    return schemaDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Create createCreate()
  {
    CreateImpl create = new CreateImpl();
    return create;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateAccessFramework createCreateAccessFramework()
  {
    CreateAccessFrameworkImpl createAccessFramework = new CreateAccessFrameworkImpl();
    return createAccessFramework;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateChannelFrameworkViaPort createCreateChannelFrameworkViaPort()
  {
    CreateChannelFrameworkViaPortImpl createChannelFrameworkViaPort = new CreateChannelFrameworkViaPortImpl();
    return createChannelFrameworkViaPort;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateChannelFormatViaFile createCreateChannelFormatViaFile()
  {
    CreateChannelFormatViaFileImpl createChannelFormatViaFile = new CreateChannelFormatViaFileImpl();
    return createChannelFormatViaFile;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateDatabaseStream createCreateDatabaseStream()
  {
    CreateDatabaseStreamImpl createDatabaseStream = new CreateDatabaseStreamImpl();
    return createDatabaseStream;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateDatabaseSink createCreateDatabaseSink()
  {
    CreateDatabaseSinkImpl createDatabaseSink = new CreateDatabaseSinkImpl();
    return createDatabaseSink;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateView createCreateView()
  {
    CreateViewImpl createView = new CreateViewImpl();
    return createView;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ContextStoreType createContextStoreType()
  {
    ContextStoreTypeImpl contextStoreType = new ContextStoreTypeImpl();
    return contextStoreType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamTo createStreamTo()
  {
    StreamToImpl streamTo = new StreamToImpl();
    return streamTo;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WindowOperator createWindowOperator()
  {
    WindowOperatorImpl windowOperator = new WindowOperatorImpl();
    return windowOperator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionsModel createExpressionsModel()
  {
    ExpressionsModelImpl expressionsModel = new ExpressionsModelImpl();
    return expressionsModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Expression createExpression()
  {
    ExpressionImpl expression = new ExpressionImpl();
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DataType createDataType()
  {
    DataTypeImpl dataType = new DataTypeImpl();
    return dataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SimpleSource createSimpleSource()
  {
    SimpleSourceImpl simpleSource = new SimpleSourceImpl();
    return simpleSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NestedSource createNestedSource()
  {
    NestedSourceImpl nestedSource = new NestedSourceImpl();
    return nestedSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Function createFunction()
  {
    FunctionImpl function = new FunctionImpl();
    return function;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionComponentAsAttribute createExpressionComponentAsAttribute()
  {
    ExpressionComponentAsAttributeImpl expressionComponentAsAttribute = new ExpressionComponentAsAttributeImpl();
    return expressionComponentAsAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateDataBaseConnectionJDBC createCreateDataBaseConnectionJDBC()
  {
    CreateDataBaseConnectionJDBCImpl createDataBaseConnectionJDBC = new CreateDataBaseConnectionJDBCImpl();
    return createDataBaseConnectionJDBC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateDataBaseConnectionGeneric createCreateDataBaseConnectionGeneric()
  {
    CreateDataBaseConnectionGenericImpl createDataBaseConnectionGeneric = new CreateDataBaseConnectionGenericImpl();
    return createDataBaseConnectionGeneric;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DropDatabaseConnection createDropDatabaseConnection()
  {
    DropDatabaseConnectionImpl dropDatabaseConnection = new DropDatabaseConnectionImpl();
    return dropDatabaseConnection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateContextStore createCreateContextStore()
  {
    CreateContextStoreImpl createContextStore = new CreateContextStoreImpl();
    return createContextStore;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DropContextStore createDropContextStore()
  {
    DropContextStoreImpl dropContextStore = new DropContextStoreImpl();
    return dropContextStore;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DropStream createDropStream()
  {
    DropStreamImpl dropStream = new DropStreamImpl();
    return dropStream;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UserManagement createUserManagement()
  {
    UserManagementImpl userManagement = new UserManagementImpl();
    return userManagement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RightsManagement createRightsManagement()
  {
    RightsManagementImpl rightsManagement = new RightsManagementImpl();
    return rightsManagement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RoleManagement createRoleManagement()
  {
    RoleManagementImpl roleManagement = new RoleManagementImpl();
    return roleManagement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UndboundedWindow createUndboundedWindow()
  {
    UndboundedWindowImpl undboundedWindow = new UndboundedWindowImpl();
    return undboundedWindow;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TimebasedWindow createTimebasedWindow()
  {
    TimebasedWindowImpl timebasedWindow = new TimebasedWindowImpl();
    return timebasedWindow;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TuplebasedWindow createTuplebasedWindow()
  {
    TuplebasedWindowImpl tuplebasedWindow = new TuplebasedWindowImpl();
    return tuplebasedWindow;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OrPredicate createOrPredicate()
  {
    OrPredicateImpl orPredicate = new OrPredicateImpl();
    return orPredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AndPredicate createAndPredicate()
  {
    AndPredicateImpl andPredicate = new AndPredicateImpl();
    return andPredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Equality createEquality()
  {
    EqualityImpl equality = new EqualityImpl();
    return equality;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Comparision createComparision()
  {
    ComparisionImpl comparision = new ComparisionImpl();
    return comparision;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Plus createPlus()
  {
    PlusImpl plus = new PlusImpl();
    return plus;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Minus createMinus()
  {
    MinusImpl minus = new MinusImpl();
    return minus;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MulOrDiv createMulOrDiv()
  {
    MulOrDivImpl mulOrDiv = new MulOrDivImpl();
    return mulOrDiv;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Bracket createBracket()
  {
    BracketImpl bracket = new BracketImpl();
    return bracket;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NOT createNOT()
  {
    NOTImpl not = new NOTImpl();
    return not;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IntConstant createIntConstant()
  {
    IntConstantImpl intConstant = new IntConstantImpl();
    return intConstant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FloatConstant createFloatConstant()
  {
    FloatConstantImpl floatConstant = new FloatConstantImpl();
    return floatConstant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StringConstant createStringConstant()
  {
    StringConstantImpl stringConstant = new StringConstantImpl();
    return stringConstant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BoolConstant createBoolConstant()
  {
    BoolConstantImpl boolConstant = new BoolConstantImpl();
    return boolConstant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttributeRef createAttributeRef()
  {
    AttributeRefImpl attributeRef = new AttributeRefImpl();
    return attributeRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CQLPackage getCQLPackage()
  {
    return (CQLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static CQLPackage getPackage()
  {
    return CQLPackage.eINSTANCE;
  }

} //CQLFactoryImpl
