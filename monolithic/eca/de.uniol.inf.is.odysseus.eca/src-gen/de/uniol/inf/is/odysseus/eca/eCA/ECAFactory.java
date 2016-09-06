/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage
 * @generated
 */
public interface ECAFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ECAFactory eINSTANCE = de.uniol.inf.is.odysseus.eca.eCA.impl.ECAFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model</em>'.
   * @generated
   */
  Model createModel();

  /**
   * Returns a new object of class '<em>Constant</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Constant</em>'.
   * @generated
   */
  Constant createConstant();

  /**
   * Returns a new object of class '<em>Window</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Window</em>'.
   * @generated
   */
  Window createWindow();

  /**
   * Returns a new object of class '<em>Timer</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Timer</em>'.
   * @generated
   */
  Timer createTimer();

  /**
   * Returns a new object of class '<em>Defined Event</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Defined Event</em>'.
   * @generated
   */
  DefinedEvent createDefinedEvent();

  /**
   * Returns a new object of class '<em>Rule</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Rule</em>'.
   * @generated
   */
  Rule createRule();

  /**
   * Returns a new object of class '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression</em>'.
   * @generated
   */
  Expression createExpression();

  /**
   * Returns a new object of class '<em>Rule Source</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Rule Source</em>'.
   * @generated
   */
  RuleSource createRuleSource();

  /**
   * Returns a new object of class '<em>SOURCECONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SOURCECONDITION</em>'.
   * @generated
   */
  SOURCECONDITION createSOURCECONDITION();

  /**
   * Returns a new object of class '<em>QUERYCONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>QUERYCONDITION</em>'.
   * @generated
   */
  QUERYCONDITION createQUERYCONDITION();

  /**
   * Returns a new object of class '<em>SYSTEMCONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SYSTEMCONDITION</em>'.
   * @generated
   */
  SYSTEMCONDITION createSYSTEMCONDITION();

  /**
   * Returns a new object of class '<em>FREECONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>FREECONDITION</em>'.
   * @generated
   */
  FREECONDITION createFREECONDITION();

  /**
   * Returns a new object of class '<em>MAPCONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>MAPCONDITION</em>'.
   * @generated
   */
  MAPCONDITION createMAPCONDITION();

  /**
   * Returns a new object of class '<em>COMMANDACTION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>COMMANDACTION</em>'.
   * @generated
   */
  COMMANDACTION createCOMMANDACTION();

  /**
   * Returns a new object of class '<em>RNDQUERY</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RNDQUERY</em>'.
   * @generated
   */
  RNDQUERY createRNDQUERY();

  /**
   * Returns a new object of class '<em>Source</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Source</em>'.
   * @generated
   */
  Source createSource();

  /**
   * Returns a new object of class '<em>Eca Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Eca Value</em>'.
   * @generated
   */
  EcaValue createEcaValue();

  /**
   * Returns a new object of class '<em>CONDITIONS</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>CONDITIONS</em>'.
   * @generated
   */
  CONDITIONS createCONDITIONS();

  /**
   * Returns a new object of class '<em>SUBCONDITION</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SUBCONDITION</em>'.
   * @generated
   */
  SUBCONDITION createSUBCONDITION();

  /**
   * Returns a new object of class '<em>ACTIONS</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>ACTIONS</em>'.
   * @generated
   */
  ACTIONS createACTIONS();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ECAPackage getECAPackage();

} //ECAFactory
