/**
 */
package de.uniol.inf.is.odysseus.pql2.impl;

import de.uniol.inf.is.odysseus.pql2.*;

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
public class Pql2FactoryImpl extends EFactoryImpl implements Pql2Factory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static Pql2Factory init()
  {
    try
    {
      Pql2Factory thePql2Factory = (Pql2Factory)EPackage.Registry.INSTANCE.getEFactory(Pql2Package.eNS_URI);
      if (thePql2Factory != null)
      {
        return thePql2Factory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new Pql2FactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pql2FactoryImpl()
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
      case Pql2Package.PQL_MODEL: return createPQLModel();
      case Pql2Package.QUERY: return createQuery();
      case Pql2Package.TEMPORARY_STREAM: return createTemporaryStream();
      case Pql2Package.VIEW: return createView();
      case Pql2Package.SHARED_STREAM: return createSharedStream();
      case Pql2Package.OPERATOR: return createOperator();
      case Pql2Package.OPERATOR_LIST: return createOperatorList();
      case Pql2Package.OPERATOR_OR_QUERY: return createOperatorOrQuery();
      case Pql2Package.PARAMETER_LIST: return createParameterList();
      case Pql2Package.PARAMETER: return createParameter();
      case Pql2Package.PARAMETER_VALUE: return createParameterValue();
      case Pql2Package.LONG_PARAMETER_VALUE: return createLongParameterValue();
      case Pql2Package.DOUBLE_PARAMETER_VALUE: return createDoubleParameterValue();
      case Pql2Package.STRING_PARAMETER_VALUE: return createStringParameterValue();
      case Pql2Package.LIST_PARAMETER_VALUE: return createListParameterValue();
      case Pql2Package.MAP_PARAMETER_VALUE: return createMapParameterValue();
      case Pql2Package.MAP_ENTRY: return createMapEntry();
      case Pql2Package.LIST: return createList();
      case Pql2Package.MAP: return createMap();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PQLModel createPQLModel()
  {
    PQLModelImpl pqlModel = new PQLModelImpl();
    return pqlModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Query createQuery()
  {
    QueryImpl query = new QueryImpl();
    return query;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TemporaryStream createTemporaryStream()
  {
    TemporaryStreamImpl temporaryStream = new TemporaryStreamImpl();
    return temporaryStream;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public View createView()
  {
    ViewImpl view = new ViewImpl();
    return view;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SharedStream createSharedStream()
  {
    SharedStreamImpl sharedStream = new SharedStreamImpl();
    return sharedStream;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operator createOperator()
  {
    OperatorImpl operator = new OperatorImpl();
    return operator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OperatorList createOperatorList()
  {
    OperatorListImpl operatorList = new OperatorListImpl();
    return operatorList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OperatorOrQuery createOperatorOrQuery()
  {
    OperatorOrQueryImpl operatorOrQuery = new OperatorOrQueryImpl();
    return operatorOrQuery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParameterList createParameterList()
  {
    ParameterListImpl parameterList = new ParameterListImpl();
    return parameterList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Parameter createParameter()
  {
    ParameterImpl parameter = new ParameterImpl();
    return parameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParameterValue createParameterValue()
  {
    ParameterValueImpl parameterValue = new ParameterValueImpl();
    return parameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LongParameterValue createLongParameterValue()
  {
    LongParameterValueImpl longParameterValue = new LongParameterValueImpl();
    return longParameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DoubleParameterValue createDoubleParameterValue()
  {
    DoubleParameterValueImpl doubleParameterValue = new DoubleParameterValueImpl();
    return doubleParameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StringParameterValue createStringParameterValue()
  {
    StringParameterValueImpl stringParameterValue = new StringParameterValueImpl();
    return stringParameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ListParameterValue createListParameterValue()
  {
    ListParameterValueImpl listParameterValue = new ListParameterValueImpl();
    return listParameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MapParameterValue createMapParameterValue()
  {
    MapParameterValueImpl mapParameterValue = new MapParameterValueImpl();
    return mapParameterValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MapEntry createMapEntry()
  {
    MapEntryImpl mapEntry = new MapEntryImpl();
    return mapEntry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List createList()
  {
    ListImpl list = new ListImpl();
    return list;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map createMap()
  {
    MapImpl map = new MapImpl();
    return map;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pql2Package getPql2Package()
  {
    return (Pql2Package)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static Pql2Package getPackage()
  {
    return Pql2Package.eINSTANCE;
  }

} //Pql2FactoryImpl
