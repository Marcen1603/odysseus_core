/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.*;

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
public class ECAFactoryImpl extends EFactoryImpl implements ECAFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ECAFactory init()
  {
    try
    {
      ECAFactory theECAFactory = (ECAFactory)EPackage.Registry.INSTANCE.getEFactory(ECAPackage.eNS_URI);
      if (theECAFactory != null)
      {
        return theECAFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ECAFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ECAFactoryImpl()
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
      case ECAPackage.MODEL: return createModel();
      case ECAPackage.CONSTANT: return createConstant();
      case ECAPackage.WINDOW: return createWindow();
      case ECAPackage.TIMER: return createTimer();
      case ECAPackage.DEFINED_EVENT: return createDefinedEvent();
      case ECAPackage.RULE: return createRule();
      case ECAPackage.EXPRESSION: return createExpression();
      case ECAPackage.RULE_SOURCE: return createRuleSource();
      case ECAPackage.SOURCECONDITION: return createSOURCECONDITION();
      case ECAPackage.QUERYCONDITION: return createQUERYCONDITION();
      case ECAPackage.SYSTEMCONDITION: return createSYSTEMCONDITION();
      case ECAPackage.FREECONDITION: return createFREECONDITION();
      case ECAPackage.MAPCONDITION: return createMAPCONDITION();
      case ECAPackage.COMMANDACTION: return createCOMMANDACTION();
      case ECAPackage.RNDQUERY: return createRNDQUERY();
      case ECAPackage.SOURCE: return createSource();
      case ECAPackage.ECA_VALUE: return createEcaValue();
      case ECAPackage.CONDITIONS: return createCONDITIONS();
      case ECAPackage.SUBCONDITION: return createSUBCONDITION();
      case ECAPackage.ACTIONS: return createACTIONS();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Model createModel()
  {
    ModelImpl model = new ModelImpl();
    return model;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Constant createConstant()
  {
    ConstantImpl constant = new ConstantImpl();
    return constant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Window createWindow()
  {
    WindowImpl window = new WindowImpl();
    return window;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Timer createTimer()
  {
    TimerImpl timer = new TimerImpl();
    return timer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DefinedEvent createDefinedEvent()
  {
    DefinedEventImpl definedEvent = new DefinedEventImpl();
    return definedEvent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Rule createRule()
  {
    RuleImpl rule = new RuleImpl();
    return rule;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Expression createExpression()
  {
    ExpressionImpl expression = new ExpressionImpl();
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RuleSource createRuleSource()
  {
    RuleSourceImpl ruleSource = new RuleSourceImpl();
    return ruleSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOURCECONDITION createSOURCECONDITION()
  {
    SOURCECONDITIONImpl sourcecondition = new SOURCECONDITIONImpl();
    return sourcecondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QUERYCONDITION createQUERYCONDITION()
  {
    QUERYCONDITIONImpl querycondition = new QUERYCONDITIONImpl();
    return querycondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SYSTEMCONDITION createSYSTEMCONDITION()
  {
    SYSTEMCONDITIONImpl systemcondition = new SYSTEMCONDITIONImpl();
    return systemcondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FREECONDITION createFREECONDITION()
  {
    FREECONDITIONImpl freecondition = new FREECONDITIONImpl();
    return freecondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MAPCONDITION createMAPCONDITION()
  {
    MAPCONDITIONImpl mapcondition = new MAPCONDITIONImpl();
    return mapcondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public COMMANDACTION createCOMMANDACTION()
  {
    COMMANDACTIONImpl commandaction = new COMMANDACTIONImpl();
    return commandaction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RNDQUERY createRNDQUERY()
  {
    RNDQUERYImpl rndquery = new RNDQUERYImpl();
    return rndquery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Source createSource()
  {
    SourceImpl source = new SourceImpl();
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EcaValue createEcaValue()
  {
    EcaValueImpl ecaValue = new EcaValueImpl();
    return ecaValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CONDITIONS createCONDITIONS()
  {
    CONDITIONSImpl conditions = new CONDITIONSImpl();
    return conditions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SUBCONDITION createSUBCONDITION()
  {
    SUBCONDITIONImpl subcondition = new SUBCONDITIONImpl();
    return subcondition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ACTIONS createACTIONS()
  {
    ACTIONSImpl actions = new ACTIONSImpl();
    return actions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ECAPackage getECAPackage()
  {
    return (ECAPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ECAPackage getPackage()
  {
    return ECAPackage.eINSTANCE;
  }

} //ECAFactoryImpl
