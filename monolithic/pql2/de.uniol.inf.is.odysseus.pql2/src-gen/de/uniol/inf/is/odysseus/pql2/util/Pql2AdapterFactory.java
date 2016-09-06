/**
 */
package de.uniol.inf.is.odysseus.pql2.util;

import de.uniol.inf.is.odysseus.pql2.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package
 * @generated
 */
public class Pql2AdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static Pql2Package modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pql2AdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = Pql2Package.eINSTANCE;
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
  protected Pql2Switch<Adapter> modelSwitch =
    new Pql2Switch<Adapter>()
    {
      @Override
      public Adapter casePQLModel(PQLModel object)
      {
        return createPQLModelAdapter();
      }
      @Override
      public Adapter caseQuery(Query object)
      {
        return createQueryAdapter();
      }
      @Override
      public Adapter caseTemporaryStream(TemporaryStream object)
      {
        return createTemporaryStreamAdapter();
      }
      @Override
      public Adapter caseView(View object)
      {
        return createViewAdapter();
      }
      @Override
      public Adapter caseSharedStream(SharedStream object)
      {
        return createSharedStreamAdapter();
      }
      @Override
      public Adapter caseOperator(Operator object)
      {
        return createOperatorAdapter();
      }
      @Override
      public Adapter caseOperatorList(OperatorList object)
      {
        return createOperatorListAdapter();
      }
      @Override
      public Adapter caseOperatorOrQuery(OperatorOrQuery object)
      {
        return createOperatorOrQueryAdapter();
      }
      @Override
      public Adapter caseParameterList(ParameterList object)
      {
        return createParameterListAdapter();
      }
      @Override
      public Adapter caseParameter(Parameter object)
      {
        return createParameterAdapter();
      }
      @Override
      public Adapter caseParameterValue(ParameterValue object)
      {
        return createParameterValueAdapter();
      }
      @Override
      public Adapter caseLongParameterValue(LongParameterValue object)
      {
        return createLongParameterValueAdapter();
      }
      @Override
      public Adapter caseDoubleParameterValue(DoubleParameterValue object)
      {
        return createDoubleParameterValueAdapter();
      }
      @Override
      public Adapter caseStringParameterValue(StringParameterValue object)
      {
        return createStringParameterValueAdapter();
      }
      @Override
      public Adapter caseListParameterValue(ListParameterValue object)
      {
        return createListParameterValueAdapter();
      }
      @Override
      public Adapter caseMapParameterValue(MapParameterValue object)
      {
        return createMapParameterValueAdapter();
      }
      @Override
      public Adapter caseMapEntry(MapEntry object)
      {
        return createMapEntryAdapter();
      }
      @Override
      public Adapter caseList(List object)
      {
        return createListAdapter();
      }
      @Override
      public Adapter caseMap(Map object)
      {
        return createMapAdapter();
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
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.PQLModel <em>PQL Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.PQLModel
   * @generated
   */
  public Adapter createPQLModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.Query <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.Query
   * @generated
   */
  public Adapter createQueryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.TemporaryStream <em>Temporary Stream</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.TemporaryStream
   * @generated
   */
  public Adapter createTemporaryStreamAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.View <em>View</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.View
   * @generated
   */
  public Adapter createViewAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.SharedStream <em>Shared Stream</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.SharedStream
   * @generated
   */
  public Adapter createSharedStreamAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.Operator
   * @generated
   */
  public Adapter createOperatorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.OperatorList <em>Operator List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorList
   * @generated
   */
  public Adapter createOperatorListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.OperatorOrQuery <em>Operator Or Query</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.OperatorOrQuery
   * @generated
   */
  public Adapter createOperatorOrQueryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.ParameterList <em>Parameter List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.ParameterList
   * @generated
   */
  public Adapter createParameterListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.Parameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.Parameter
   * @generated
   */
  public Adapter createParameterAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.ParameterValue <em>Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.ParameterValue
   * @generated
   */
  public Adapter createParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.LongParameterValue <em>Long Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.LongParameterValue
   * @generated
   */
  public Adapter createLongParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.DoubleParameterValue <em>Double Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.DoubleParameterValue
   * @generated
   */
  public Adapter createDoubleParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.StringParameterValue <em>String Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.StringParameterValue
   * @generated
   */
  public Adapter createStringParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.ListParameterValue <em>List Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.ListParameterValue
   * @generated
   */
  public Adapter createListParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.MapParameterValue <em>Map Parameter Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.MapParameterValue
   * @generated
   */
  public Adapter createMapParameterValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.MapEntry <em>Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.MapEntry
   * @generated
   */
  public Adapter createMapEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.List <em>List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.List
   * @generated
   */
  public Adapter createListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.pql2.Map <em>Map</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.pql2.Map
   * @generated
   */
  public Adapter createMapAdapter()
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

} //Pql2AdapterFactory
