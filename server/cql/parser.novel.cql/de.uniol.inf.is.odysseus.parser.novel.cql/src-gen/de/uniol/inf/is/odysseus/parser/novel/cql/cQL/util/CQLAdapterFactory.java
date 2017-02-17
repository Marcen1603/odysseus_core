/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.util;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage
 * @generated
 */
public class CQLAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static CQLPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CQLAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = CQLPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CQLSwitch<Adapter> modelSwitch =
    new CQLSwitch<Adapter>()
    {
      @Override
      public Adapter caseModel(Model object)
      {
        return createModelAdapter();
      }
      @Override
      public Adapter caseStatement(Statement object)
      {
        return createStatementAdapter();
      }
      @Override
      public Adapter caseSelect(Select object)
      {
        return createSelectAdapter();
      }
      @Override
      public Adapter caseSource(Source object)
      {
        return createSourceAdapter();
      }
      @Override
      public Adapter caseAttribute(Attribute object)
      {
        return createAttributeAdapter();
      }
      @Override
      public Adapter caseAttributeWithNestedStatement(AttributeWithNestedStatement object)
      {
        return createAttributeWithNestedStatementAdapter();
      }
      @Override
      public Adapter caseAggregation(Aggregation object)
      {
        return createAggregationAdapter();
      }
      @Override
      public Adapter caseExpressionComponent(ExpressionComponent object)
      {
        return createExpressionComponentAdapter();
      }
      @Override
      public Adapter caseSelectExpression(SelectExpression object)
      {
        return createSelectExpressionAdapter();
      }
      @Override
      public Adapter caseSelectExpressionWithoutAliasDefinition(SelectExpressionWithoutAliasDefinition object)
      {
        return createSelectExpressionWithoutAliasDefinitionAdapter();
      }
      @Override
      public Adapter caseAlias(Alias object)
      {
        return createAliasAdapter();
      }
      @Override
      public Adapter caseCreateParameters(CreateParameters object)
      {
        return createCreateParametersAdapter();
      }
      @Override
      public Adapter caseAttributeDefinition(AttributeDefinition object)
      {
        return createAttributeDefinitionAdapter();
      }
      @Override
      public Adapter caseCreateStream1(CreateStream1 object)
      {
        return createCreateStream1Adapter();
      }
      @Override
      public Adapter caseCreateSink1(CreateSink1 object)
      {
        return createCreateSink1Adapter();
      }
      @Override
      public Adapter caseCreateStreamChannel(CreateStreamChannel object)
      {
        return createCreateStreamChannelAdapter();
      }
      @Override
      public Adapter caseCreateStreamFile(CreateStreamFile object)
      {
        return createCreateStreamFileAdapter();
      }
      @Override
      public Adapter caseCreateView(CreateView object)
      {
        return createCreateViewAdapter();
      }
      @Override
      public Adapter caseStreamTo(StreamTo object)
      {
        return createStreamToAdapter();
      }
      @Override
      public Adapter caseCommand(Command object)
      {
        return createCommandAdapter();
      }
      @Override
      public Adapter caseWindow_Timebased(Window_Timebased object)
      {
        return createWindow_TimebasedAdapter();
      }
      @Override
      public Adapter caseWindow_Tuplebased(Window_Tuplebased object)
      {
        return createWindow_TuplebasedAdapter();
      }
      @Override
      public Adapter caseExpressionsModel(ExpressionsModel object)
      {
        return createExpressionsModelAdapter();
      }
      @Override
      public Adapter caseExpression(Expression object)
      {
        return createExpressionAdapter();
      }
      @Override
      public Adapter caseDataType(DataType object)
      {
        return createDataTypeAdapter();
      }
      @Override
      public Adapter caseMapper(Mapper object)
      {
        return createMapperAdapter();
      }
      @Override
      public Adapter caseOr(Or object)
      {
        return createOrAdapter();
      }
      @Override
      public Adapter caseAnd(And object)
      {
        return createAndAdapter();
      }
      @Override
      public Adapter caseEquality(Equality object)
      {
        return createEqualityAdapter();
      }
      @Override
      public Adapter caseComparision(Comparision object)
      {
        return createComparisionAdapter();
      }
      @Override
      public Adapter casePlus(Plus object)
      {
        return createPlusAdapter();
      }
      @Override
      public Adapter caseMinus(Minus object)
      {
        return createMinusAdapter();
      }
      @Override
      public Adapter caseMulOrDiv(MulOrDiv object)
      {
        return createMulOrDivAdapter();
      }
      @Override
      public Adapter caseBracket(Bracket object)
      {
        return createBracketAdapter();
      }
      @Override
      public Adapter caseNOT(NOT object)
      {
        return createNOTAdapter();
      }
      @Override
      public Adapter caseIntConstant(IntConstant object)
      {
        return createIntConstantAdapter();
      }
      @Override
      public Adapter caseFloatConstant(FloatConstant object)
      {
        return createFloatConstantAdapter();
      }
      @Override
      public Adapter caseStringConstant(StringConstant object)
      {
        return createStringConstantAdapter();
      }
      @Override
      public Adapter caseBoolConstant(BoolConstant object)
      {
        return createBoolConstantAdapter();
      }
      @Override
      public Adapter caseAttributeRef(AttributeRef object)
      {
        return createAttributeRefAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
   * @generated
   */
  public Adapter createModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement <em>Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement
   * @generated
   */
  public Adapter createStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select <em>Select</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select
   * @generated
   */
  public Adapter createSelectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source
   * @generated
   */
  public Adapter createSourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute <em>Attribute</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
   * @generated
   */
  public Adapter createAttributeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeWithNestedStatement <em>Attribute With Nested Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeWithNestedStatement
   * @generated
   */
  public Adapter createAttributeWithNestedStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Aggregation <em>Aggregation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Aggregation
   * @generated
   */
  public Adapter createAggregationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionComponent <em>Expression Component</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionComponent
   * @generated
   */
  public Adapter createExpressionComponentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectExpression <em>Select Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectExpression
   * @generated
   */
  public Adapter createSelectExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectExpressionWithoutAliasDefinition <em>Select Expression Without Alias Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SelectExpressionWithoutAliasDefinition
   * @generated
   */
  public Adapter createSelectExpressionWithoutAliasDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Alias <em>Alias</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Alias
   * @generated
   */
  public Adapter createAliasAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateParameters <em>Create Parameters</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateParameters
   * @generated
   */
  public Adapter createCreateParametersAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition <em>Attribute Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition
   * @generated
   */
  public Adapter createAttributeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStream1 <em>Create Stream1</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStream1
   * @generated
   */
  public Adapter createCreateStream1Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateSink1 <em>Create Sink1</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateSink1
   * @generated
   */
  public Adapter createCreateSink1Adapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStreamChannel <em>Create Stream Channel</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStreamChannel
   * @generated
   */
  public Adapter createCreateStreamChannelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStreamFile <em>Create Stream File</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateStreamFile
   * @generated
   */
  public Adapter createCreateStreamFileAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateView <em>Create View</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateView
   * @generated
   */
  public Adapter createCreateViewAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StreamTo <em>Stream To</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StreamTo
   * @generated
   */
  public Adapter createStreamToAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command <em>Command</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command
   * @generated
   */
  public Adapter createCommandAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Timebased <em>Window Timebased</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Timebased
   * @generated
   */
  public Adapter createWindow_TimebasedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Tuplebased <em>Window Tuplebased</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Tuplebased
   * @generated
   */
  public Adapter createWindow_TuplebasedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionsModel <em>Expressions Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionsModel
   * @generated
   */
  public Adapter createExpressionsModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
   * @generated
   */
  public Adapter createExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DataType <em>Data Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DataType
   * @generated
   */
  public Adapter createDataTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Mapper <em>Mapper</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Mapper
   * @generated
   */
  public Adapter createMapperAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or <em>Or</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or
   * @generated
   */
  public Adapter createOrAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And <em>And</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And
   * @generated
   */
  public Adapter createAndAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality <em>Equality</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
   * @generated
   */
  public Adapter createEqualityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision <em>Comparision</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
   * @generated
   */
  public Adapter createComparisionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus <em>Plus</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
   * @generated
   */
  public Adapter createPlusAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus <em>Minus</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
   * @generated
   */
  public Adapter createMinusAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv <em>Mul Or Div</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
   * @generated
   */
  public Adapter createMulOrDivAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Bracket <em>Bracket</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Bracket
   * @generated
   */
  public Adapter createBracketAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT <em>NOT</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
   * @generated
   */
  public Adapter createNOTAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant <em>Int Constant</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant
   * @generated
   */
  public Adapter createIntConstantAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant <em>Float Constant</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant
   * @generated
   */
  public Adapter createFloatConstantAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant <em>String Constant</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant
   * @generated
   */
  public Adapter createStringConstantAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant <em>Bool Constant</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant
   * @generated
   */
  public Adapter createBoolConstantAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef <em>Attribute Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef
   * @generated
   */
  public Adapter createAttributeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //CQLAdapterFactory
