/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage
 * @generated
 */
public interface QDLFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  QDLFactory eINSTANCE = de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model</em>'.
   * @generated
   */
  QDLModel createQDLModel();

  /**
   * Returns a new object of class '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model Element</em>'.
   * @generated
   */
  QDLModelElement createQDLModelElement();

  /**
   * Returns a new object of class '<em>Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Query</em>'.
   * @generated
   */
  QDLQuery createQDLQuery();

  /**
   * Returns a new object of class '<em>IQL Instance Of Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>IQL Instance Of Expression</em>'.
   * @generated
   */
  IQLInstanceOfExpression createIQLInstanceOfExpression();

  /**
   * Returns a new object of class '<em>IQL Relational Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>IQL Relational Expression</em>'.
   * @generated
   */
  IQLRelationalExpression createIQLRelationalExpression();

  /**
   * Returns a new object of class '<em>IQL Subscribe Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>IQL Subscribe Expression</em>'.
   * @generated
   */
  IQLSubscribeExpression createIQLSubscribeExpression();

  /**
   * Returns a new object of class '<em>IQL Port Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>IQL Port Expression</em>'.
   * @generated
   */
  IQLPortExpression createIQLPortExpression();

  /**
   * Returns a new object of class '<em>Metadata Value Single ID</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Metadata Value Single ID</em>'.
   * @generated
   */
  QDLMetadataValueSingleID createQDLMetadataValueSingleID();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  QDLPackage getQDLPackage();

} //QDLFactory
