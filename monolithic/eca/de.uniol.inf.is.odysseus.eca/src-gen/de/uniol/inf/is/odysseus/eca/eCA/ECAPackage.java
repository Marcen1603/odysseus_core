/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAFactory
 * @model kind="package"
 * @generated
 */
public interface ECAPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "eCA";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.uniol.de/inf/is/odysseus/eca/ECA";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "eCA";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ECAPackage eINSTANCE = de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl.init();

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Constants</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__CONSTANTS = 0;

  /**
   * The feature id for the '<em><b>Def Events</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__DEF_EVENTS = 1;

  /**
   * The feature id for the '<em><b>Window Size</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__WINDOW_SIZE = 2;

  /**
   * The feature id for the '<em><b>Time Intervall</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__TIME_INTERVALL = 3;

  /**
   * The feature id for the '<em><b>Rules</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__RULES = 4;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ConstantImpl <em>Constant</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ConstantImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getConstant()
   * @generated
   */
  int CONSTANT = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__NAME = 0;

  /**
   * The feature id for the '<em><b>Const Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT__CONST_VALUE = 1;

  /**
   * The number of structural features of the '<em>Constant</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTANT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.WindowImpl <em>Window</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.WindowImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getWindow()
   * @generated
   */
  int WINDOW = 2;

  /**
   * The feature id for the '<em><b>Window Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WINDOW__WINDOW_VALUE = 0;

  /**
   * The number of structural features of the '<em>Window</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WINDOW_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.TimerImpl <em>Timer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.TimerImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getTimer()
   * @generated
   */
  int TIMER = 3;

  /**
   * The feature id for the '<em><b>Timer Intervall Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIMER__TIMER_INTERVALL_VALUE = 0;

  /**
   * The number of structural features of the '<em>Timer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TIMER_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl <em>Defined Event</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getDefinedEvent()
   * @generated
   */
  int DEFINED_EVENT = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT__NAME = 0;

  /**
   * The feature id for the '<em><b>Defined Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT__DEFINED_SOURCE = 1;

  /**
   * The feature id for the '<em><b>Defined Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT__DEFINED_ATTRIBUTE = 2;

  /**
   * The feature id for the '<em><b>Defined Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT__DEFINED_OPERATOR = 3;

  /**
   * The feature id for the '<em><b>Defined Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT__DEFINED_VALUE = 4;

  /**
   * The number of structural features of the '<em>Defined Event</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINED_EVENT_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl <em>Rule</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRule()
   * @generated
   */
  int RULE = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__NAME = 0;

  /**
   * The feature id for the '<em><b>Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__SOURCE = 1;

  /**
   * The feature id for the '<em><b>Rule Conditions</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__RULE_CONDITIONS = 2;

  /**
   * The feature id for the '<em><b>Rule Actions</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE__RULE_ACTIONS = 3;

  /**
   * The number of structural features of the '<em>Rule</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl <em>Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getExpression()
   * @generated
   */
  int EXPRESSION = 6;

  /**
   * The feature id for the '<em><b>Subsource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__SUBSOURCE = 0;

  /**
   * The feature id for the '<em><b>Subsys</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__SUBSYS = 1;

  /**
   * The feature id for the '<em><b>Com Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION__COM_ACTION = 2;

  /**
   * The number of structural features of the '<em>Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl <em>Rule Source</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRuleSource()
   * @generated
   */
  int RULE_SOURCE = 7;

  /**
   * The feature id for the '<em><b>Def Source</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_SOURCE__DEF_SOURCE = 0;

  /**
   * The feature id for the '<em><b>New Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_SOURCE__NEW_SOURCE = 1;

  /**
   * The feature id for the '<em><b>Pre Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_SOURCE__PRE_SOURCE = 2;

  /**
   * The number of structural features of the '<em>Rule Source</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RULE_SOURCE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SOURCECONDITIONImpl <em>SOURCECONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SOURCECONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSOURCECONDITION()
   * @generated
   */
  int SOURCECONDITION = 8;

  /**
   * The feature id for the '<em><b>Cond Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCECONDITION__COND_ATTRIBUTE = 0;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCECONDITION__OPERATOR = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCECONDITION__VALUE = 2;

  /**
   * The number of structural features of the '<em>SOURCECONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCECONDITION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl <em>QUERYCONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getQUERYCONDITION()
   * @generated
   */
  int QUERYCONDITION = 9;

  /**
   * The feature id for the '<em><b>Query Not</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYCONDITION__QUERY_NOT = 0;

  /**
   * The feature id for the '<em><b>Query Funct</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYCONDITION__QUERY_FUNCT = 1;

  /**
   * The number of structural features of the '<em>QUERYCONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYCONDITION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl <em>SYSTEMCONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSYSTEMCONDITION()
   * @generated
   */
  int SYSTEMCONDITION = 10;

  /**
   * The feature id for the '<em><b>System Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEMCONDITION__SYSTEM_ATTRIBUTE = 0;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEMCONDITION__OPERATOR = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEMCONDITION__VALUE = 2;

  /**
   * The number of structural features of the '<em>SYSTEMCONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEMCONDITION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.FREECONDITIONImpl <em>FREECONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.FREECONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getFREECONDITION()
   * @generated
   */
  int FREECONDITION = 11;

  /**
   * The feature id for the '<em><b>Free Condition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FREECONDITION__FREE_CONDITION = 0;

  /**
   * The number of structural features of the '<em>FREECONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FREECONDITION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.MAPCONDITIONImpl <em>MAPCONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.MAPCONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getMAPCONDITION()
   * @generated
   */
  int MAPCONDITION = 12;

  /**
   * The feature id for the '<em><b>Map Cond</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAPCONDITION__MAP_COND = 0;

  /**
   * The number of structural features of the '<em>MAPCONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MAPCONDITION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl <em>COMMANDACTION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getCOMMANDACTION()
   * @generated
   */
  int COMMANDACTION = 13;

  /**
   * The feature id for the '<em><b>Sub Actname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMANDACTION__SUB_ACTNAME = 0;

  /**
   * The feature id for the '<em><b>Funct Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMANDACTION__FUNCT_ACTION = 1;

  /**
   * The feature id for the '<em><b>Action Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMANDACTION__ACTION_VALUE = 2;

  /**
   * The feature id for the '<em><b>Inner Action</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMANDACTION__INNER_ACTION = 3;

  /**
   * The number of structural features of the '<em>COMMANDACTION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMMANDACTION_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl <em>RNDQUERY</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRNDQUERY()
   * @generated
   */
  int RNDQUERY = 14;

  /**
   * The feature id for the '<em><b>Pri Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RNDQUERY__PRI_OPERATOR = 0;

  /**
   * The feature id for the '<em><b>Pri Val</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RNDQUERY__PRI_VAL = 1;

  /**
   * The feature id for the '<em><b>Sel</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RNDQUERY__SEL = 2;

  /**
   * The feature id for the '<em><b>State Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RNDQUERY__STATE_NAME = 3;

  /**
   * The number of structural features of the '<em>RNDQUERY</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RNDQUERY_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SourceImpl <em>Source</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SourceImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSource()
   * @generated
   */
  int SOURCE = 15;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE__NAME = 0;

  /**
   * The number of structural features of the '<em>Source</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOURCE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl <em>Eca Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getEcaValue()
   * @generated
   */
  int ECA_VALUE = 16;

  /**
   * The feature id for the '<em><b>Int Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE__INT_VALUE = 0;

  /**
   * The feature id for the '<em><b>Id Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE__ID_VALUE = 1;

  /**
   * The feature id for the '<em><b>Const Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE__CONST_VALUE = 2;

  /**
   * The feature id for the '<em><b>String Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE__STRING_VALUE = 3;

  /**
   * The feature id for the '<em><b>Double Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE__DOUBLE_VALUE = 4;

  /**
   * The number of structural features of the '<em>Eca Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECA_VALUE_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.CONDITIONSImpl <em>CONDITIONS</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.CONDITIONSImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getCONDITIONS()
   * @generated
   */
  int CONDITIONS = 17;

  /**
   * The feature id for the '<em><b>Subsource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS__SUBSOURCE = EXPRESSION__SUBSOURCE;

  /**
   * The feature id for the '<em><b>Subsys</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS__SUBSYS = EXPRESSION__SUBSYS;

  /**
   * The feature id for the '<em><b>Com Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS__COM_ACTION = EXPRESSION__COM_ACTION;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS__LEFT = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS__RIGHT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>CONDITIONS</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONDITIONS_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl <em>SUBCONDITION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSUBCONDITION()
   * @generated
   */
  int SUBCONDITION = 18;

  /**
   * The feature id for the '<em><b>Subsource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__SUBSOURCE = EXPRESSION__SUBSOURCE;

  /**
   * The feature id for the '<em><b>Subsys</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__SUBSYS = EXPRESSION__SUBSYS;

  /**
   * The feature id for the '<em><b>Com Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__COM_ACTION = EXPRESSION__COM_ACTION;

  /**
   * The feature id for the '<em><b>Subfree</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__SUBFREE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Submap</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__SUBMAP = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Query Cond</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION__QUERY_COND = EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>SUBCONDITION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUBCONDITION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ACTIONSImpl <em>ACTIONS</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ACTIONSImpl
   * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getACTIONS()
   * @generated
   */
  int ACTIONS = 19;

  /**
   * The feature id for the '<em><b>Subsource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS__SUBSOURCE = EXPRESSION__SUBSOURCE;

  /**
   * The feature id for the '<em><b>Subsys</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS__SUBSYS = EXPRESSION__SUBSYS;

  /**
   * The feature id for the '<em><b>Com Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS__COM_ACTION = EXPRESSION__COM_ACTION;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS__LEFT = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS__RIGHT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>ACTIONS</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIONS_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getConstants <em>Constants</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Constants</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model#getConstants()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Constants();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getDefEvents <em>Def Events</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Def Events</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model#getDefEvents()
   * @see #getModel()
   * @generated
   */
  EReference getModel_DefEvents();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getWindowSize <em>Window Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Window Size</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model#getWindowSize()
   * @see #getModel()
   * @generated
   */
  EReference getModel_WindowSize();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getTimeIntervall <em>Time Intervall</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Time Intervall</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model#getTimeIntervall()
   * @see #getModel()
   * @generated
   */
  EReference getModel_TimeIntervall();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.eca.eCA.Model#getRules <em>Rules</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Rules</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Model#getRules()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Rules();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Constant <em>Constant</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Constant</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Constant
   * @generated
   */
  EClass getConstant();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Constant#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Constant#getName()
   * @see #getConstant()
   * @generated
   */
  EAttribute getConstant_Name();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Constant#getConstValue <em>Const Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Const Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Constant#getConstValue()
   * @see #getConstant()
   * @generated
   */
  EAttribute getConstant_ConstValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Window <em>Window</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Window</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Window
   * @generated
   */
  EClass getWindow();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Window#getWindowValue <em>Window Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Window Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Window#getWindowValue()
   * @see #getWindow()
   * @generated
   */
  EAttribute getWindow_WindowValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Timer <em>Timer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Timer</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Timer
   * @generated
   */
  EClass getTimer();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Timer#getTimerIntervallValue <em>Timer Intervall Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Timer Intervall Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Timer#getTimerIntervallValue()
   * @see #getTimer()
   * @generated
   */
  EAttribute getTimer_TimerIntervallValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent <em>Defined Event</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Defined Event</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent
   * @generated
   */
  EClass getDefinedEvent();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getName()
   * @see #getDefinedEvent()
   * @generated
   */
  EAttribute getDefinedEvent_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedSource <em>Defined Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Defined Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedSource()
   * @see #getDefinedEvent()
   * @generated
   */
  EReference getDefinedEvent_DefinedSource();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedAttribute <em>Defined Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Defined Attribute</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedAttribute()
   * @see #getDefinedEvent()
   * @generated
   */
  EAttribute getDefinedEvent_DefinedAttribute();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedOperator <em>Defined Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Defined Operator</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedOperator()
   * @see #getDefinedEvent()
   * @generated
   */
  EAttribute getDefinedEvent_DefinedOperator();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedValue <em>Defined Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Defined Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent#getDefinedValue()
   * @see #getDefinedEvent()
   * @generated
   */
  EReference getDefinedEvent_DefinedValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule <em>Rule</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rule</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Rule
   * @generated
   */
  EClass getRule();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Rule#getName()
   * @see #getRule()
   * @generated
   */
  EAttribute getRule_Name();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Rule#getSource()
   * @see #getRule()
   * @generated
   */
  EReference getRule_Source();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleConditions <em>Rule Conditions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Rule Conditions</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleConditions()
   * @see #getRule()
   * @generated
   */
  EReference getRule_RuleConditions();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleActions <em>Rule Actions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Rule Actions</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Rule#getRuleActions()
   * @see #getRule()
   * @generated
   */
  EReference getRule_RuleActions();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Expression
   * @generated
   */
  EClass getExpression();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsource <em>Subsource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Subsource</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsource()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Subsource();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsys <em>Subsys</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Subsys</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Expression#getSubsys()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_Subsys();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.Expression#getComAction <em>Com Action</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Com Action</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Expression#getComAction()
   * @see #getExpression()
   * @generated
   */
  EReference getExpression_ComAction();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource <em>Rule Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rule Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RuleSource
   * @generated
   */
  EClass getRuleSource();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getDefSource <em>Def Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Def Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getDefSource()
   * @see #getRuleSource()
   * @generated
   */
  EReference getRuleSource_DefSource();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getNewSource <em>New Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>New Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getNewSource()
   * @see #getRuleSource()
   * @generated
   */
  EReference getRuleSource_NewSource();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getPreSource <em>Pre Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pre Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RuleSource#getPreSource()
   * @see #getRuleSource()
   * @generated
   */
  EAttribute getRuleSource_PreSource();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION <em>SOURCECONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>SOURCECONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION
   * @generated
   */
  EClass getSOURCECONDITION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getCondAttribute <em>Cond Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Cond Attribute</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getCondAttribute()
   * @see #getSOURCECONDITION()
   * @generated
   */
  EAttribute getSOURCECONDITION_CondAttribute();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getOperator()
   * @see #getSOURCECONDITION()
   * @generated
   */
  EAttribute getSOURCECONDITION_Operator();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION#getValue()
   * @see #getSOURCECONDITION()
   * @generated
   */
  EReference getSOURCECONDITION_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION <em>QUERYCONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>QUERYCONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION
   * @generated
   */
  EClass getQUERYCONDITION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryNot <em>Query Not</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Query Not</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryNot()
   * @see #getQUERYCONDITION()
   * @generated
   */
  EAttribute getQUERYCONDITION_QueryNot();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryFunct <em>Query Funct</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Query Funct</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryFunct()
   * @see #getQUERYCONDITION()
   * @generated
   */
  EReference getQUERYCONDITION_QueryFunct();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION <em>SYSTEMCONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>SYSTEMCONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION
   * @generated
   */
  EClass getSYSTEMCONDITION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getSystemAttribute <em>System Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>System Attribute</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getSystemAttribute()
   * @see #getSYSTEMCONDITION()
   * @generated
   */
  EAttribute getSYSTEMCONDITION_SystemAttribute();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getOperator()
   * @see #getSYSTEMCONDITION()
   * @generated
   */
  EAttribute getSYSTEMCONDITION_Operator();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION#getValue()
   * @see #getSYSTEMCONDITION()
   * @generated
   */
  EReference getSYSTEMCONDITION_Value();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION <em>FREECONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>FREECONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION
   * @generated
   */
  EClass getFREECONDITION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION#getFreeCondition <em>Free Condition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Free Condition</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION#getFreeCondition()
   * @see #getFREECONDITION()
   * @generated
   */
  EAttribute getFREECONDITION_FreeCondition();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION <em>MAPCONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MAPCONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION
   * @generated
   */
  EClass getMAPCONDITION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION#getMapCond <em>Map Cond</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Map Cond</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION#getMapCond()
   * @see #getMAPCONDITION()
   * @generated
   */
  EAttribute getMAPCONDITION_MapCond();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION <em>COMMANDACTION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>COMMANDACTION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION
   * @generated
   */
  EClass getCOMMANDACTION();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getSubActname <em>Sub Actname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sub Actname</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getSubActname()
   * @see #getCOMMANDACTION()
   * @generated
   */
  EAttribute getCOMMANDACTION_SubActname();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getFunctAction <em>Funct Action</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Funct Action</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getFunctAction()
   * @see #getCOMMANDACTION()
   * @generated
   */
  EReference getCOMMANDACTION_FunctAction();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getActionValue <em>Action Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Action Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getActionValue()
   * @see #getCOMMANDACTION()
   * @generated
   */
  EReference getCOMMANDACTION_ActionValue();

  /**
   * Returns the meta object for the containment reference list '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getInnerAction <em>Inner Action</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Inner Action</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getInnerAction()
   * @see #getCOMMANDACTION()
   * @generated
   */
  EReference getCOMMANDACTION_InnerAction();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY <em>RNDQUERY</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RNDQUERY</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY
   * @generated
   */
  EClass getRNDQUERY();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriOperator <em>Pri Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pri Operator</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriOperator()
   * @see #getRNDQUERY()
   * @generated
   */
  EAttribute getRNDQUERY_PriOperator();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriVal <em>Pri Val</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pri Val</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getPriVal()
   * @see #getRNDQUERY()
   * @generated
   */
  EAttribute getRNDQUERY_PriVal();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getSel <em>Sel</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sel</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getSel()
   * @see #getRNDQUERY()
   * @generated
   */
  EAttribute getRNDQUERY_Sel();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getStateName <em>State Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>State Name</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY#getStateName()
   * @see #getRNDQUERY()
   * @generated
   */
  EAttribute getRNDQUERY_StateName();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.Source <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Source</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Source
   * @generated
   */
  EClass getSource();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.Source#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.Source#getName()
   * @see #getSource()
   * @generated
   */
  EAttribute getSource_Name();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue <em>Eca Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Eca Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue
   * @generated
   */
  EClass getEcaValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIntValue <em>Int Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Int Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIntValue()
   * @see #getEcaValue()
   * @generated
   */
  EAttribute getEcaValue_IntValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIdValue <em>Id Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getIdValue()
   * @see #getEcaValue()
   * @generated
   */
  EAttribute getEcaValue_IdValue();

  /**
   * Returns the meta object for the reference '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getConstValue <em>Const Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Const Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getConstValue()
   * @see #getEcaValue()
   * @generated
   */
  EReference getEcaValue_ConstValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getStringValue <em>String Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>String Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getStringValue()
   * @see #getEcaValue()
   * @generated
   */
  EAttribute getEcaValue_StringValue();

  /**
   * Returns the meta object for the attribute '{@link de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getDoubleValue <em>Double Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Double Value</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.EcaValue#getDoubleValue()
   * @see #getEcaValue()
   * @generated
   */
  EAttribute getEcaValue_DoubleValue();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS <em>CONDITIONS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>CONDITIONS</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS
   * @generated
   */
  EClass getCONDITIONS();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS#getLeft()
   * @see #getCONDITIONS()
   * @generated
   */
  EReference getCONDITIONS_Left();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS#getRight()
   * @see #getCONDITIONS()
   * @generated
   */
  EReference getCONDITIONS_Right();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION <em>SUBCONDITION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>SUBCONDITION</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION
   * @generated
   */
  EClass getSUBCONDITION();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubfree <em>Subfree</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Subfree</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubfree()
   * @see #getSUBCONDITION()
   * @generated
   */
  EReference getSUBCONDITION_Subfree();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubmap <em>Submap</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Submap</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubmap()
   * @see #getSUBCONDITION()
   * @generated
   */
  EReference getSUBCONDITION_Submap();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getQueryCond <em>Query Cond</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Query Cond</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getQueryCond()
   * @see #getSUBCONDITION()
   * @generated
   */
  EReference getSUBCONDITION_QueryCond();

  /**
   * Returns the meta object for class '{@link de.uniol.inf.is.odysseus.eca.eCA.ACTIONS <em>ACTIONS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ACTIONS</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ACTIONS
   * @generated
   */
  EClass getACTIONS();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.ACTIONS#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ACTIONS#getLeft()
   * @see #getACTIONS()
   * @generated
   */
  EReference getACTIONS_Left();

  /**
   * Returns the meta object for the containment reference '{@link de.uniol.inf.is.odysseus.eca.eCA.ACTIONS#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right</em>'.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ACTIONS#getRight()
   * @see #getACTIONS()
   * @generated
   */
  EReference getACTIONS_Right();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ECAFactory getECAFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Constants</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__CONSTANTS = eINSTANCE.getModel_Constants();

    /**
     * The meta object literal for the '<em><b>Def Events</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__DEF_EVENTS = eINSTANCE.getModel_DefEvents();

    /**
     * The meta object literal for the '<em><b>Window Size</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__WINDOW_SIZE = eINSTANCE.getModel_WindowSize();

    /**
     * The meta object literal for the '<em><b>Time Intervall</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__TIME_INTERVALL = eINSTANCE.getModel_TimeIntervall();

    /**
     * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__RULES = eINSTANCE.getModel_Rules();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ConstantImpl <em>Constant</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ConstantImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getConstant()
     * @generated
     */
    EClass CONSTANT = eINSTANCE.getConstant();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONSTANT__NAME = eINSTANCE.getConstant_Name();

    /**
     * The meta object literal for the '<em><b>Const Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CONSTANT__CONST_VALUE = eINSTANCE.getConstant_ConstValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.WindowImpl <em>Window</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.WindowImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getWindow()
     * @generated
     */
    EClass WINDOW = eINSTANCE.getWindow();

    /**
     * The meta object literal for the '<em><b>Window Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WINDOW__WINDOW_VALUE = eINSTANCE.getWindow_WindowValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.TimerImpl <em>Timer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.TimerImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getTimer()
     * @generated
     */
    EClass TIMER = eINSTANCE.getTimer();

    /**
     * The meta object literal for the '<em><b>Timer Intervall Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TIMER__TIMER_INTERVALL_VALUE = eINSTANCE.getTimer_TimerIntervallValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl <em>Defined Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.DefinedEventImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getDefinedEvent()
     * @generated
     */
    EClass DEFINED_EVENT = eINSTANCE.getDefinedEvent();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINED_EVENT__NAME = eINSTANCE.getDefinedEvent_Name();

    /**
     * The meta object literal for the '<em><b>Defined Source</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINED_EVENT__DEFINED_SOURCE = eINSTANCE.getDefinedEvent_DefinedSource();

    /**
     * The meta object literal for the '<em><b>Defined Attribute</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINED_EVENT__DEFINED_ATTRIBUTE = eINSTANCE.getDefinedEvent_DefinedAttribute();

    /**
     * The meta object literal for the '<em><b>Defined Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINED_EVENT__DEFINED_OPERATOR = eINSTANCE.getDefinedEvent_DefinedOperator();

    /**
     * The meta object literal for the '<em><b>Defined Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINED_EVENT__DEFINED_VALUE = eINSTANCE.getDefinedEvent_DefinedValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl <em>Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRule()
     * @generated
     */
    EClass RULE = eINSTANCE.getRule();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RULE__NAME = eINSTANCE.getRule_Name();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__SOURCE = eINSTANCE.getRule_Source();

    /**
     * The meta object literal for the '<em><b>Rule Conditions</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__RULE_CONDITIONS = eINSTANCE.getRule_RuleConditions();

    /**
     * The meta object literal for the '<em><b>Rule Actions</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE__RULE_ACTIONS = eINSTANCE.getRule_RuleActions();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getExpression()
     * @generated
     */
    EClass EXPRESSION = eINSTANCE.getExpression();

    /**
     * The meta object literal for the '<em><b>Subsource</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__SUBSOURCE = eINSTANCE.getExpression_Subsource();

    /**
     * The meta object literal for the '<em><b>Subsys</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__SUBSYS = eINSTANCE.getExpression_Subsys();

    /**
     * The meta object literal for the '<em><b>Com Action</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION__COM_ACTION = eINSTANCE.getExpression_ComAction();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl <em>Rule Source</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRuleSource()
     * @generated
     */
    EClass RULE_SOURCE = eINSTANCE.getRuleSource();

    /**
     * The meta object literal for the '<em><b>Def Source</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE_SOURCE__DEF_SOURCE = eINSTANCE.getRuleSource_DefSource();

    /**
     * The meta object literal for the '<em><b>New Source</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RULE_SOURCE__NEW_SOURCE = eINSTANCE.getRuleSource_NewSource();

    /**
     * The meta object literal for the '<em><b>Pre Source</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RULE_SOURCE__PRE_SOURCE = eINSTANCE.getRuleSource_PreSource();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SOURCECONDITIONImpl <em>SOURCECONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SOURCECONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSOURCECONDITION()
     * @generated
     */
    EClass SOURCECONDITION = eINSTANCE.getSOURCECONDITION();

    /**
     * The meta object literal for the '<em><b>Cond Attribute</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCECONDITION__COND_ATTRIBUTE = eINSTANCE.getSOURCECONDITION_CondAttribute();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCECONDITION__OPERATOR = eINSTANCE.getSOURCECONDITION_Operator();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOURCECONDITION__VALUE = eINSTANCE.getSOURCECONDITION_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl <em>QUERYCONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.QUERYCONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getQUERYCONDITION()
     * @generated
     */
    EClass QUERYCONDITION = eINSTANCE.getQUERYCONDITION();

    /**
     * The meta object literal for the '<em><b>Query Not</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute QUERYCONDITION__QUERY_NOT = eINSTANCE.getQUERYCONDITION_QueryNot();

    /**
     * The meta object literal for the '<em><b>Query Funct</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERYCONDITION__QUERY_FUNCT = eINSTANCE.getQUERYCONDITION_QueryFunct();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl <em>SYSTEMCONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SYSTEMCONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSYSTEMCONDITION()
     * @generated
     */
    EClass SYSTEMCONDITION = eINSTANCE.getSYSTEMCONDITION();

    /**
     * The meta object literal for the '<em><b>System Attribute</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEMCONDITION__SYSTEM_ATTRIBUTE = eINSTANCE.getSYSTEMCONDITION_SystemAttribute();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEMCONDITION__OPERATOR = eINSTANCE.getSYSTEMCONDITION_Operator();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYSTEMCONDITION__VALUE = eINSTANCE.getSYSTEMCONDITION_Value();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.FREECONDITIONImpl <em>FREECONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.FREECONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getFREECONDITION()
     * @generated
     */
    EClass FREECONDITION = eINSTANCE.getFREECONDITION();

    /**
     * The meta object literal for the '<em><b>Free Condition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FREECONDITION__FREE_CONDITION = eINSTANCE.getFREECONDITION_FreeCondition();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.MAPCONDITIONImpl <em>MAPCONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.MAPCONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getMAPCONDITION()
     * @generated
     */
    EClass MAPCONDITION = eINSTANCE.getMAPCONDITION();

    /**
     * The meta object literal for the '<em><b>Map Cond</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MAPCONDITION__MAP_COND = eINSTANCE.getMAPCONDITION_MapCond();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl <em>COMMANDACTION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getCOMMANDACTION()
     * @generated
     */
    EClass COMMANDACTION = eINSTANCE.getCOMMANDACTION();

    /**
     * The meta object literal for the '<em><b>Sub Actname</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMMANDACTION__SUB_ACTNAME = eINSTANCE.getCOMMANDACTION_SubActname();

    /**
     * The meta object literal for the '<em><b>Funct Action</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMMANDACTION__FUNCT_ACTION = eINSTANCE.getCOMMANDACTION_FunctAction();

    /**
     * The meta object literal for the '<em><b>Action Value</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMMANDACTION__ACTION_VALUE = eINSTANCE.getCOMMANDACTION_ActionValue();

    /**
     * The meta object literal for the '<em><b>Inner Action</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMMANDACTION__INNER_ACTION = eINSTANCE.getCOMMANDACTION_InnerAction();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl <em>RNDQUERY</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.RNDQUERYImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getRNDQUERY()
     * @generated
     */
    EClass RNDQUERY = eINSTANCE.getRNDQUERY();

    /**
     * The meta object literal for the '<em><b>Pri Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RNDQUERY__PRI_OPERATOR = eINSTANCE.getRNDQUERY_PriOperator();

    /**
     * The meta object literal for the '<em><b>Pri Val</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RNDQUERY__PRI_VAL = eINSTANCE.getRNDQUERY_PriVal();

    /**
     * The meta object literal for the '<em><b>Sel</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RNDQUERY__SEL = eINSTANCE.getRNDQUERY_Sel();

    /**
     * The meta object literal for the '<em><b>State Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RNDQUERY__STATE_NAME = eINSTANCE.getRNDQUERY_StateName();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SourceImpl <em>Source</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SourceImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSource()
     * @generated
     */
    EClass SOURCE = eINSTANCE.getSource();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOURCE__NAME = eINSTANCE.getSource_Name();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl <em>Eca Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.EcaValueImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getEcaValue()
     * @generated
     */
    EClass ECA_VALUE = eINSTANCE.getEcaValue();

    /**
     * The meta object literal for the '<em><b>Int Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECA_VALUE__INT_VALUE = eINSTANCE.getEcaValue_IntValue();

    /**
     * The meta object literal for the '<em><b>Id Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECA_VALUE__ID_VALUE = eINSTANCE.getEcaValue_IdValue();

    /**
     * The meta object literal for the '<em><b>Const Value</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ECA_VALUE__CONST_VALUE = eINSTANCE.getEcaValue_ConstValue();

    /**
     * The meta object literal for the '<em><b>String Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECA_VALUE__STRING_VALUE = eINSTANCE.getEcaValue_StringValue();

    /**
     * The meta object literal for the '<em><b>Double Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECA_VALUE__DOUBLE_VALUE = eINSTANCE.getEcaValue_DoubleValue();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.CONDITIONSImpl <em>CONDITIONS</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.CONDITIONSImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getCONDITIONS()
     * @generated
     */
    EClass CONDITIONS = eINSTANCE.getCONDITIONS();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONDITIONS__LEFT = eINSTANCE.getCONDITIONS_Left();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONDITIONS__RIGHT = eINSTANCE.getCONDITIONS_Right();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl <em>SUBCONDITION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.SUBCONDITIONImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getSUBCONDITION()
     * @generated
     */
    EClass SUBCONDITION = eINSTANCE.getSUBCONDITION();

    /**
     * The meta object literal for the '<em><b>Subfree</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SUBCONDITION__SUBFREE = eINSTANCE.getSUBCONDITION_Subfree();

    /**
     * The meta object literal for the '<em><b>Submap</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SUBCONDITION__SUBMAP = eINSTANCE.getSUBCONDITION_Submap();

    /**
     * The meta object literal for the '<em><b>Query Cond</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SUBCONDITION__QUERY_COND = eINSTANCE.getSUBCONDITION_QueryCond();

    /**
     * The meta object literal for the '{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ACTIONSImpl <em>ACTIONS</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ACTIONSImpl
     * @see de.uniol.inf.is.odysseus.eca.eCA.impl.ECAPackageImpl#getACTIONS()
     * @generated
     */
    EClass ACTIONS = eINSTANCE.getACTIONS();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIONS__LEFT = eINSTANCE.getACTIONS_Left();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIONS__RIGHT = eINSTANCE.getACTIONS_Right();

  }

} //ECAPackage
