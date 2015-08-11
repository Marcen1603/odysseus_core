/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.impl;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.*;

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
public class QDLFactoryImpl extends EFactoryImpl implements QDLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static QDLFactory init()
  {
    try
    {
      QDLFactory theQDLFactory = (QDLFactory)EPackage.Registry.INSTANCE.getEFactory(QDLPackage.eNS_URI);
      if (theQDLFactory != null)
      {
        return theQDLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new QDLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLFactoryImpl()
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
      case QDLPackage.QDL_MODEL: return createQDLModel();
      case QDLPackage.QDL_TYPE_DEFINITION: return createQDLTypeDefinition();
      case QDLPackage.QDL_QUERY: return createQDLQuery();
      case QDLPackage.IQL_SUBSCRIBE_EXPRESSION: return createIQLSubscribeExpression();
      case QDLPackage.IQL_PORT_EXPRESSION: return createIQLPortExpression();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_INT: return createIQLMetadataValueSingleInt();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_DOUBLE: return createIQLMetadataValueSingleDouble();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_STRING: return createIQLMetadataValueSingleString();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_BOOLEAN: return createIQLMetadataValueSingleBoolean();
      case QDLPackage.QDL_METADATA_VALUE_SINGLE_ID: return createQDLMetadataValueSingleID();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_TYPE_REF: return createIQLMetadataValueSingleTypeRef();
      case QDLPackage.IQL_METADATA_VALUE_SINGLE_NULL: return createIQLMetadataValueSingleNull();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLModel createQDLModel()
  {
    QDLModelImpl qdlModel = new QDLModelImpl();
    return qdlModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLTypeDefinition createQDLTypeDefinition()
  {
    QDLTypeDefinitionImpl qdlTypeDefinition = new QDLTypeDefinitionImpl();
    return qdlTypeDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLQuery createQDLQuery()
  {
    QDLQueryImpl qdlQuery = new QDLQueryImpl();
    return qdlQuery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLSubscribeExpression createIQLSubscribeExpression()
  {
    IQLSubscribeExpressionImpl iqlSubscribeExpression = new IQLSubscribeExpressionImpl();
    return iqlSubscribeExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLPortExpression createIQLPortExpression()
  {
    IQLPortExpressionImpl iqlPortExpression = new IQLPortExpressionImpl();
    return iqlPortExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataValueSingleInt createIQLMetadataValueSingleInt()
  {
    IQLMetadataValueSingleIntImpl iqlMetadataValueSingleInt = new IQLMetadataValueSingleIntImpl();
    return iqlMetadataValueSingleInt;
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
  public QDLMetadataValueSingleID createQDLMetadataValueSingleID()
  {
    QDLMetadataValueSingleIDImpl qdlMetadataValueSingleID = new QDLMetadataValueSingleIDImpl();
    return qdlMetadataValueSingleID;
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
  public QDLPackage getQDLPackage()
  {
    return (QDLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static QDLPackage getPackage()
  {
    return QDLPackage.eINSTANCE;
  }

} //QDLFactoryImpl
