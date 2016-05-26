/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.util;

import de.uniol.inf.is.odysseus.server.streamingsparql.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage
 * @generated
 */
public class StreamingsparqlAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static StreamingsparqlPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StreamingsparqlAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = StreamingsparqlPackage.eINSTANCE;
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
  protected StreamingsparqlSwitch<Adapter> modelSwitch =
    new StreamingsparqlSwitch<Adapter>()
    {
      @Override
      public Adapter caseSPARQLQuery(SPARQLQuery object)
      {
        return createSPARQLQueryAdapter();
      }
      @Override
      public Adapter casePrefix(Prefix object)
      {
        return createPrefixAdapter();
      }
      @Override
      public Adapter caseBase(Base object)
      {
        return createBaseAdapter();
      }
      @Override
      public Adapter caseSelectQuery(SelectQuery object)
      {
        return createSelectQueryAdapter();
      }
      @Override
      public Adapter caseAggregate(Aggregate object)
      {
        return createAggregateAdapter();
      }
      @Override
      public Adapter caseGroupBy(GroupBy object)
      {
        return createGroupByAdapter();
      }
      @Override
      public Adapter caseAggregation(Aggregation object)
      {
        return createAggregationAdapter();
      }
      @Override
      public Adapter caseFilesinkclause(Filesinkclause object)
      {
        return createFilesinkclauseAdapter();
      }
      @Override
      public Adapter caseFilterclause(Filterclause object)
      {
        return createFilterclauseAdapter();
      }
      @Override
      public Adapter caseGroupClause(GroupClause object)
      {
        return createGroupClauseAdapter();
      }
      @Override
      public Adapter caseDatasetClause(DatasetClause object)
      {
        return createDatasetClauseAdapter();
      }
      @Override
      public Adapter caseWhereClause(WhereClause object)
      {
        return createWhereClauseAdapter();
      }
      @Override
      public Adapter caseInnerWhereClause(InnerWhereClause object)
      {
        return createInnerWhereClauseAdapter();
      }
      @Override
      public Adapter caseGroupGraphPatternSub(GroupGraphPatternSub object)
      {
        return createGroupGraphPatternSubAdapter();
      }
      @Override
      public Adapter caseTriplesSameSubject(TriplesSameSubject object)
      {
        return createTriplesSameSubjectAdapter();
      }
      @Override
      public Adapter casePropertyList(PropertyList object)
      {
        return createPropertyListAdapter();
      }
      @Override
      public Adapter caseGraphNode(GraphNode object)
      {
        return createGraphNodeAdapter();
      }
      @Override
      public Adapter caseVariable(Variable object)
      {
        return createVariableAdapter();
      }
      @Override
      public Adapter caseUnNamedVariable(UnNamedVariable object)
      {
        return createUnNamedVariableAdapter();
      }
      @Override
      public Adapter caseNamedVariable(NamedVariable object)
      {
        return createNamedVariableAdapter();
      }
      @Override
      public Adapter caseIRI(IRI object)
      {
        return createIRIAdapter();
      }
      @Override
      public Adapter caseTypeTag(TypeTag object)
      {
        return createTypeTagAdapter();
      }
      @Override
      public Adapter caseLangTag(LangTag object)
      {
        return createLangTagAdapter();
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
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery <em>SPARQL Query</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery
   * @generated
   */
  public Adapter createSPARQLQueryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Prefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Prefix
   * @generated
   */
  public Adapter createPrefixAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Base <em>Base</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Base
   * @generated
   */
  public Adapter createBaseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery <em>Select Query</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery
   * @generated
   */
  public Adapter createSelectQueryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate <em>Aggregate</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate
   * @generated
   */
  public Adapter createAggregateAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy <em>Group By</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy
   * @generated
   */
  public Adapter createGroupByAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation <em>Aggregation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation
   * @generated
   */
  public Adapter createAggregationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause <em>Filesinkclause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause
   * @generated
   */
  public Adapter createFilesinkclauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause <em>Filterclause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause
   * @generated
   */
  public Adapter createFilterclauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause <em>Group Clause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause
   * @generated
   */
  public Adapter createGroupClauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause <em>Dataset Clause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause
   * @generated
   */
  public Adapter createDatasetClauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause <em>Where Clause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause
   * @generated
   */
  public Adapter createWhereClauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause <em>Inner Where Clause</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause
   * @generated
   */
  public Adapter createInnerWhereClauseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub <em>Group Graph Pattern Sub</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub
   * @generated
   */
  public Adapter createGroupGraphPatternSubAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject <em>Triples Same Subject</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject
   * @generated
   */
  public Adapter createTriplesSameSubjectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList <em>Property List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList
   * @generated
   */
  public Adapter createPropertyListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode <em>Graph Node</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode
   * @generated
   */
  public Adapter createGraphNodeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.Variable
   * @generated
   */
  public Adapter createVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable <em>Un Named Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable
   * @generated
   */
  public Adapter createUnNamedVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable <em>Named Variable</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable
   * @generated
   */
  public Adapter createNamedVariableAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.IRI <em>IRI</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.IRI
   * @generated
   */
  public Adapter createIRIAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag <em>Type Tag</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag
   * @generated
   */
  public Adapter createTypeTagAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.server.streamingsparql.LangTag <em>Lang Tag</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.server.streamingsparql.LangTag
   * @generated
   */
  public Adapter createLangTagAdapter()
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

} //StreamingsparqlAdapterFactory
