/**
 */
package de.uniol.inf.is.odysseus.pql2;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.pql2.Pql2Package
 * @generated
 */
public interface Pql2Factory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  Pql2Factory eINSTANCE = de.uniol.inf.is.odysseus.pql2.impl.Pql2FactoryImpl.init();

  /**
   * Returns a new object of class '<em>PQL Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>PQL Model</em>'.
   * @generated
   */
  PQLModel createPQLModel();

  /**
   * Returns a new object of class '<em>Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Query</em>'.
   * @generated
   */
  Query createQuery();

  /**
   * Returns a new object of class '<em>Temporary Stream</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Temporary Stream</em>'.
   * @generated
   */
  TemporaryStream createTemporaryStream();

  /**
   * Returns a new object of class '<em>View</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>View</em>'.
   * @generated
   */
  View createView();

  /**
   * Returns a new object of class '<em>Shared Stream</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Shared Stream</em>'.
   * @generated
   */
  SharedStream createSharedStream();

  /**
   * Returns a new object of class '<em>Operator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operator</em>'.
   * @generated
   */
  Operator createOperator();

  /**
   * Returns a new object of class '<em>Operator List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operator List</em>'.
   * @generated
   */
  OperatorList createOperatorList();

  /**
   * Returns a new object of class '<em>Operator Or Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operator Or Query</em>'.
   * @generated
   */
  OperatorOrQuery createOperatorOrQuery();

  /**
   * Returns a new object of class '<em>Parameter List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parameter List</em>'.
   * @generated
   */
  ParameterList createParameterList();

  /**
   * Returns a new object of class '<em>Parameter</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parameter</em>'.
   * @generated
   */
  Parameter createParameter();

  /**
   * Returns a new object of class '<em>Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parameter Value</em>'.
   * @generated
   */
  ParameterValue createParameterValue();

  /**
   * Returns a new object of class '<em>Long Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Long Parameter Value</em>'.
   * @generated
   */
  LongParameterValue createLongParameterValue();

  /**
   * Returns a new object of class '<em>Double Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Double Parameter Value</em>'.
   * @generated
   */
  DoubleParameterValue createDoubleParameterValue();

  /**
   * Returns a new object of class '<em>String Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Parameter Value</em>'.
   * @generated
   */
  StringParameterValue createStringParameterValue();

  /**
   * Returns a new object of class '<em>List Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>List Parameter Value</em>'.
   * @generated
   */
  ListParameterValue createListParameterValue();

  /**
   * Returns a new object of class '<em>Map Parameter Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Map Parameter Value</em>'.
   * @generated
   */
  MapParameterValue createMapParameterValue();

  /**
   * Returns a new object of class '<em>Map Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Map Entry</em>'.
   * @generated
   */
  MapEntry createMapEntry();

  /**
   * Returns a new object of class '<em>List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>List</em>'.
   * @generated
   */
  List createList();

  /**
   * Returns a new object of class '<em>Map</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Map</em>'.
   * @generated
   */
  Map createMap();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  Pql2Package getPql2Package();

} //Pql2Factory
