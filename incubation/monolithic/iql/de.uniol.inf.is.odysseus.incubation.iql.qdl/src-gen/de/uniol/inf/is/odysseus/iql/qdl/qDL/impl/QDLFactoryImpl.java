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
      case QDLPackage.QDL_MODEL_ELEMENT: return createQDLModelElement();
      case QDLPackage.QDL_QUERY: return createQDLQuery();
      case QDLPackage.IQL_SUBSCRIBE_EXPRESSION: return createIQLSubscribeExpression();
      case QDLPackage.IQL_PORT_EXPRESSION: return createIQLPortExpression();
      case QDLPackage.QDL_METADATA_VALUE_SINGLE_ID: return createQDLMetadataValueSingleID();
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
  public QDLModelElement createQDLModelElement()
  {
    QDLModelElementImpl qdlModelElement = new QDLModelElementImpl();
    return qdlModelElement;
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
