/**
 */
package de.uniol.inf.is.odysseus.iql.odl.oDL.impl;

import de.uniol.inf.is.odysseus.iql.odl.oDL.*;

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
public class ODLFactoryImpl extends EFactoryImpl implements ODLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ODLFactory init()
  {
    try
    {
      ODLFactory theODLFactory = (ODLFactory)EPackage.Registry.INSTANCE.getEFactory(ODLPackage.eNS_URI);
      if (theODLFactory != null)
      {
        return theODLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ODLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLFactoryImpl()
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
      case ODLPackage.ODL_MODEL: return createODLModel();
      case ODLPackage.ODL_TYPE_DEFINITION: return createODLTypeDefinition();
      case ODLPackage.ODL_OPERATOR: return createODLOperator();
      case ODLPackage.ODL_PARAMETER: return createODLParameter();
      case ODLPackage.ODL_METHOD: return createODLMethod();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLModel createODLModel()
  {
    ODLModelImpl odlModel = new ODLModelImpl();
    return odlModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLTypeDefinition createODLTypeDefinition()
  {
    ODLTypeDefinitionImpl odlTypeDefinition = new ODLTypeDefinitionImpl();
    return odlTypeDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLOperator createODLOperator()
  {
    ODLOperatorImpl odlOperator = new ODLOperatorImpl();
    return odlOperator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLParameter createODLParameter()
  {
    ODLParameterImpl odlParameter = new ODLParameterImpl();
    return odlParameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLMethod createODLMethod()
  {
    ODLMethodImpl odlMethod = new ODLMethodImpl();
    return odlMethod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ODLPackage getODLPackage()
  {
    return (ODLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ODLPackage getPackage()
  {
    return ODLPackage.eINSTANCE;
  }

} //ODLFactoryImpl
