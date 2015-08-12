/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.util;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.common.types.JvmAnnotationTarget;
import org.eclipse.xtext.common.types.JvmComponentType;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeParameterDeclarator;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage
 * @generated
 */
public class QDLAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static QDLPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QDLAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = QDLPackage.eINSTANCE;
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
  protected QDLSwitch<Adapter> modelSwitch =
    new QDLSwitch<Adapter>()
    {
      @Override
      public Adapter caseQDLModel(QDLModel object)
      {
        return createQDLModelAdapter();
      }
      @Override
      public Adapter caseQDLTypeDefinition(QDLTypeDefinition object)
      {
        return createQDLTypeDefinitionAdapter();
      }
      @Override
      public Adapter caseQDLQuery(QDLQuery object)
      {
        return createQDLQueryAdapter();
      }
      @Override
      public Adapter caseIQLSubscribeExpression(IQLSubscribeExpression object)
      {
        return createIQLSubscribeExpressionAdapter();
      }
      @Override
      public Adapter caseIQLPortExpression(IQLPortExpression object)
      {
        return createIQLPortExpressionAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleInt(IQLMetadataValueSingleInt object)
      {
        return createIQLMetadataValueSingleIntAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleDouble(IQLMetadataValueSingleDouble object)
      {
        return createIQLMetadataValueSingleDoubleAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleString(IQLMetadataValueSingleString object)
      {
        return createIQLMetadataValueSingleStringAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleBoolean(IQLMetadataValueSingleBoolean object)
      {
        return createIQLMetadataValueSingleBooleanAdapter();
      }
      @Override
      public Adapter caseQDLMetadataValueSingleID(QDLMetadataValueSingleID object)
      {
        return createQDLMetadataValueSingleIDAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleTypeRef(IQLMetadataValueSingleTypeRef object)
      {
        return createIQLMetadataValueSingleTypeRefAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValueSingleNull(IQLMetadataValueSingleNull object)
      {
        return createIQLMetadataValueSingleNullAdapter();
      }
      @Override
      public Adapter caseIQLModel(IQLModel object)
      {
        return createIQLModelAdapter();
      }
      @Override
      public Adapter caseIQLTypeDefinition(IQLTypeDefinition object)
      {
        return createIQLTypeDefinitionAdapter();
      }
      @Override
      public Adapter caseJvmIdentifiableElement(JvmIdentifiableElement object)
      {
        return createJvmIdentifiableElementAdapter();
      }
      @Override
      public Adapter caseJvmAnnotationTarget(JvmAnnotationTarget object)
      {
        return createJvmAnnotationTargetAdapter();
      }
      @Override
      public Adapter caseJvmMember(JvmMember object)
      {
        return createJvmMemberAdapter();
      }
      @Override
      public Adapter caseJvmType(JvmType object)
      {
        return createJvmTypeAdapter();
      }
      @Override
      public Adapter caseJvmComponentType(JvmComponentType object)
      {
        return createJvmComponentTypeAdapter();
      }
      @Override
      public Adapter caseJvmDeclaredType(JvmDeclaredType object)
      {
        return createJvmDeclaredTypeAdapter();
      }
      @Override
      public Adapter caseJvmTypeParameterDeclarator(JvmTypeParameterDeclarator object)
      {
        return createJvmTypeParameterDeclaratorAdapter();
      }
      @Override
      public Adapter caseJvmGenericType(JvmGenericType object)
      {
        return createJvmGenericTypeAdapter();
      }
      @Override
      public Adapter caseIQLExpression(IQLExpression object)
      {
        return createIQLExpressionAdapter();
      }
      @Override
      public Adapter caseIQLMetadataValue(IQLMetadataValue object)
      {
        return createIQLMetadataValueAdapter();
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
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLModel
   * @generated
   */
  public Adapter createQDLModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLTypeDefinition <em>Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLTypeDefinition
   * @generated
   */
  public Adapter createQDLTypeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery <em>Query</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery
   * @generated
   */
  public Adapter createQDLQueryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression <em>IQL Subscribe Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLSubscribeExpression
   * @generated
   */
  public Adapter createIQLSubscribeExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression <em>IQL Port Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLPortExpression
   * @generated
   */
  public Adapter createIQLPortExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleInt <em>IQL Metadata Value Single Int</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleInt
   * @generated
   */
  public Adapter createIQLMetadataValueSingleIntAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleDouble <em>IQL Metadata Value Single Double</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleDouble
   * @generated
   */
  public Adapter createIQLMetadataValueSingleDoubleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleString <em>IQL Metadata Value Single String</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleString
   * @generated
   */
  public Adapter createIQLMetadataValueSingleStringAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleBoolean <em>IQL Metadata Value Single Boolean</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleBoolean
   * @generated
   */
  public Adapter createIQLMetadataValueSingleBooleanAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID <em>Metadata Value Single ID</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLMetadataValueSingleID
   * @generated
   */
  public Adapter createQDLMetadataValueSingleIDAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleTypeRef <em>IQL Metadata Value Single Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleTypeRef
   * @generated
   */
  public Adapter createIQLMetadataValueSingleTypeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleNull <em>IQL Metadata Value Single Null</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.IQLMetadataValueSingleNull
   * @generated
   */
  public Adapter createIQLMetadataValueSingleNullAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel <em>IQL Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel
   * @generated
   */
  public Adapter createIQLModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition <em>IQL Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition
   * @generated
   */
  public Adapter createIQLTypeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmIdentifiableElement <em>Jvm Identifiable Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmIdentifiableElement
   * @generated
   */
  public Adapter createJvmIdentifiableElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmAnnotationTarget <em>Jvm Annotation Target</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmAnnotationTarget
   * @generated
   */
  public Adapter createJvmAnnotationTargetAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmMember <em>Jvm Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmMember
   * @generated
   */
  public Adapter createJvmMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmType <em>Jvm Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmType
   * @generated
   */
  public Adapter createJvmTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmComponentType <em>Jvm Component Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmComponentType
   * @generated
   */
  public Adapter createJvmComponentTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmDeclaredType <em>Jvm Declared Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmDeclaredType
   * @generated
   */
  public Adapter createJvmDeclaredTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmTypeParameterDeclarator <em>Jvm Type Parameter Declarator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmTypeParameterDeclarator
   * @generated
   */
  public Adapter createJvmTypeParameterDeclaratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmGenericType <em>Jvm Generic Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmGenericType
   * @generated
   */
  public Adapter createJvmGenericTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression <em>IQL Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression
   * @generated
   */
  public Adapter createIQLExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue <em>IQL Metadata Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataValue
   * @generated
   */
  public Adapter createIQLMetadataValueAdapter()
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

} //QDLAdapterFactory