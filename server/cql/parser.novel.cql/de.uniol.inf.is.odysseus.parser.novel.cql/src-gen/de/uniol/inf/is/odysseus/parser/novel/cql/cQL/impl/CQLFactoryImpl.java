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
      case CQLPackage.SELECT: return createSelect();
      case CQLPackage.SOURCE: return createSource();
      case CQLPackage.ATTRIBUTE: return createAttribute();
      case CQLPackage.ATTRIBUTE_WITH_NESTED_STATEMENT: return createAttributeWithNestedStatement();
      case CQLPackage.AGGREGATION: return createAggregation();
      case CQLPackage.ALIAS: return createAlias();
      case CQLPackage.CREATE: return createCreate();
      case CQLPackage.ACCESS_FRAMEWORK: return createAccessFramework();
      case CQLPackage.CHANNEL_FORMAT: return createChannelFormat();
      case CQLPackage.CHANNEL_FORMAT_STREAM: return createChannelFormatStream();
      case CQLPackage.CHANNEL_FORMAT_VIEW: return createChannelFormatView();
      case CQLPackage.STREAM_TO: return createStreamTo();
      case CQLPackage.DROP: return createDrop();
      case CQLPackage.WINDOW_TIMEBASED: return createWindow_Timebased();
      case CQLPackage.WINDOW_TUPLEBASED: return createWindow_Tuplebased();
      case CQLPackage.EXPRESSIONS_MODEL: return createExpressionsModel();
      case CQLPackage.EXPRESSION: return createExpression();
      case CQLPackage.DATA_TYPE: return createDataType();
      case CQLPackage.OR: return createOr();
      case CQLPackage.AND: return createAnd();
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
  public Aggregation createAggregation()
  {
    AggregationImpl aggregation = new AggregationImpl();
    return aggregation;
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
  public ChannelFormat createChannelFormat()
  {
    ChannelFormatImpl channelFormat = new ChannelFormatImpl();
    return channelFormat;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ChannelFormatStream createChannelFormatStream()
  {
    ChannelFormatStreamImpl channelFormatStream = new ChannelFormatStreamImpl();
    return channelFormatStream;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ChannelFormatView createChannelFormatView()
  {
    ChannelFormatViewImpl channelFormatView = new ChannelFormatViewImpl();
    return channelFormatView;
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
  public Drop createDrop()
  {
    DropImpl drop = new DropImpl();
    return drop;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Window_Timebased createWindow_Timebased()
  {
    Window_TimebasedImpl window_Timebased = new Window_TimebasedImpl();
    return window_Timebased;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Window_Tuplebased createWindow_Tuplebased()
  {
    Window_TuplebasedImpl window_Tuplebased = new Window_TuplebasedImpl();
    return window_Tuplebased;
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
  public Or createOr()
  {
    OrImpl or = new OrImpl();
    return or;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public And createAnd()
  {
    AndImpl and = new AndImpl();
    return and;
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
