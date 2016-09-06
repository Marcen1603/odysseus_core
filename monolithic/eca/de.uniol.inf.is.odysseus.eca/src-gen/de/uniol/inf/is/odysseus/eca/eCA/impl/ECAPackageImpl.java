/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.Constant;
import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.ECAFactory;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.Expression;
import de.uniol.inf.is.odysseus.eca.eCA.Model;
import de.uniol.inf.is.odysseus.eca.eCA.Rule;
import de.uniol.inf.is.odysseus.eca.eCA.RuleSource;
import de.uniol.inf.is.odysseus.eca.eCA.Source;
import de.uniol.inf.is.odysseus.eca.eCA.Timer;
import de.uniol.inf.is.odysseus.eca.eCA.Window;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ECAPackageImpl extends EPackageImpl implements ECAPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass constantEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass windowEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass timerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass definedEventEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ruleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ruleSourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sourceconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass queryconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass systemconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass freeconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mapconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass commandactionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rndqueryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ecaValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass conditionsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass subconditionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass actionsEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ECAPackageImpl()
  {
    super(eNS_URI, ECAFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link ECAPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ECAPackage init()
  {
    if (isInited) return (ECAPackage)EPackage.Registry.INSTANCE.getEPackage(ECAPackage.eNS_URI);

    // Obtain or create and register package
    ECAPackageImpl theECAPackage = (ECAPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ECAPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ECAPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theECAPackage.createPackageContents();

    // Initialize created meta-data
    theECAPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theECAPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ECAPackage.eNS_URI, theECAPackage);
    return theECAPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModel()
  {
    return modelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_Constants()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_DefEvents()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_WindowSize()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_TimeIntervall()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_Rules()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConstant()
  {
    return constantEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConstant_Name()
  {
    return (EAttribute)constantEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConstant_ConstValue()
  {
    return (EAttribute)constantEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWindow()
  {
    return windowEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWindow_WindowValue()
  {
    return (EAttribute)windowEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTimer()
  {
    return timerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTimer_TimerIntervallValue()
  {
    return (EAttribute)timerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDefinedEvent()
  {
    return definedEventEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinedEvent_Name()
  {
    return (EAttribute)definedEventEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinedEvent_DefinedSource()
  {
    return (EReference)definedEventEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinedEvent_DefinedAttribute()
  {
    return (EAttribute)definedEventEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinedEvent_DefinedOperator()
  {
    return (EAttribute)definedEventEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinedEvent_DefinedValue()
  {
    return (EReference)definedEventEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRule()
  {
    return ruleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRule_Name()
  {
    return (EAttribute)ruleEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRule_Source()
  {
    return (EReference)ruleEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRule_RuleConditions()
  {
    return (EReference)ruleEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRule_RuleActions()
  {
    return (EReference)ruleEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpression()
  {
    return expressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExpression_Subsource()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExpression_Subsys()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExpression_ComAction()
  {
    return (EReference)expressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRuleSource()
  {
    return ruleSourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRuleSource_DefSource()
  {
    return (EReference)ruleSourceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRuleSource_NewSource()
  {
    return (EReference)ruleSourceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRuleSource_PreSource()
  {
    return (EAttribute)ruleSourceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOURCECONDITION()
  {
    return sourceconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOURCECONDITION_CondAttribute()
  {
    return (EAttribute)sourceconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOURCECONDITION_Operator()
  {
    return (EAttribute)sourceconditionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOURCECONDITION_Value()
  {
    return (EReference)sourceconditionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQUERYCONDITION()
  {
    return queryconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQUERYCONDITION_QueryNot()
  {
    return (EAttribute)queryconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQUERYCONDITION_QueryFunct()
  {
    return (EReference)queryconditionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSYSTEMCONDITION()
  {
    return systemconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSYSTEMCONDITION_SystemAttribute()
  {
    return (EAttribute)systemconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSYSTEMCONDITION_Operator()
  {
    return (EAttribute)systemconditionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSYSTEMCONDITION_Value()
  {
    return (EReference)systemconditionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFREECONDITION()
  {
    return freeconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFREECONDITION_FreeCondition()
  {
    return (EAttribute)freeconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMAPCONDITION()
  {
    return mapconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMAPCONDITION_MapCond()
  {
    return (EAttribute)mapconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCOMMANDACTION()
  {
    return commandactionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getCOMMANDACTION_SubActname()
  {
    return (EAttribute)commandactionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCOMMANDACTION_FunctAction()
  {
    return (EReference)commandactionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCOMMANDACTION_ActionValue()
  {
    return (EReference)commandactionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCOMMANDACTION_InnerAction()
  {
    return (EReference)commandactionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRNDQUERY()
  {
    return rndqueryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRNDQUERY_PriOperator()
  {
    return (EAttribute)rndqueryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRNDQUERY_PriVal()
  {
    return (EAttribute)rndqueryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRNDQUERY_Sel()
  {
    return (EAttribute)rndqueryEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRNDQUERY_StateName()
  {
    return (EAttribute)rndqueryEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSource()
  {
    return sourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSource_Name()
  {
    return (EAttribute)sourceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getEcaValue()
  {
    return ecaValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEcaValue_IntValue()
  {
    return (EAttribute)ecaValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEcaValue_IdValue()
  {
    return (EAttribute)ecaValueEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getEcaValue_ConstValue()
  {
    return (EReference)ecaValueEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEcaValue_StringValue()
  {
    return (EAttribute)ecaValueEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEcaValue_DoubleValue()
  {
    return (EAttribute)ecaValueEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCONDITIONS()
  {
    return conditionsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCONDITIONS_Left()
  {
    return (EReference)conditionsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCONDITIONS_Right()
  {
    return (EReference)conditionsEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSUBCONDITION()
  {
    return subconditionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSUBCONDITION_Subfree()
  {
    return (EReference)subconditionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSUBCONDITION_Submap()
  {
    return (EReference)subconditionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSUBCONDITION_QueryCond()
  {
    return (EReference)subconditionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getACTIONS()
  {
    return actionsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getACTIONS_Left()
  {
    return (EReference)actionsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getACTIONS_Right()
  {
    return (EReference)actionsEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ECAFactory getECAFactory()
  {
    return (ECAFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    modelEClass = createEClass(MODEL);
    createEReference(modelEClass, MODEL__CONSTANTS);
    createEReference(modelEClass, MODEL__DEF_EVENTS);
    createEReference(modelEClass, MODEL__WINDOW_SIZE);
    createEReference(modelEClass, MODEL__TIME_INTERVALL);
    createEReference(modelEClass, MODEL__RULES);

    constantEClass = createEClass(CONSTANT);
    createEAttribute(constantEClass, CONSTANT__NAME);
    createEAttribute(constantEClass, CONSTANT__CONST_VALUE);

    windowEClass = createEClass(WINDOW);
    createEAttribute(windowEClass, WINDOW__WINDOW_VALUE);

    timerEClass = createEClass(TIMER);
    createEAttribute(timerEClass, TIMER__TIMER_INTERVALL_VALUE);

    definedEventEClass = createEClass(DEFINED_EVENT);
    createEAttribute(definedEventEClass, DEFINED_EVENT__NAME);
    createEReference(definedEventEClass, DEFINED_EVENT__DEFINED_SOURCE);
    createEAttribute(definedEventEClass, DEFINED_EVENT__DEFINED_ATTRIBUTE);
    createEAttribute(definedEventEClass, DEFINED_EVENT__DEFINED_OPERATOR);
    createEReference(definedEventEClass, DEFINED_EVENT__DEFINED_VALUE);

    ruleEClass = createEClass(RULE);
    createEAttribute(ruleEClass, RULE__NAME);
    createEReference(ruleEClass, RULE__SOURCE);
    createEReference(ruleEClass, RULE__RULE_CONDITIONS);
    createEReference(ruleEClass, RULE__RULE_ACTIONS);

    expressionEClass = createEClass(EXPRESSION);
    createEReference(expressionEClass, EXPRESSION__SUBSOURCE);
    createEReference(expressionEClass, EXPRESSION__SUBSYS);
    createEReference(expressionEClass, EXPRESSION__COM_ACTION);

    ruleSourceEClass = createEClass(RULE_SOURCE);
    createEReference(ruleSourceEClass, RULE_SOURCE__DEF_SOURCE);
    createEReference(ruleSourceEClass, RULE_SOURCE__NEW_SOURCE);
    createEAttribute(ruleSourceEClass, RULE_SOURCE__PRE_SOURCE);

    sourceconditionEClass = createEClass(SOURCECONDITION);
    createEAttribute(sourceconditionEClass, SOURCECONDITION__COND_ATTRIBUTE);
    createEAttribute(sourceconditionEClass, SOURCECONDITION__OPERATOR);
    createEReference(sourceconditionEClass, SOURCECONDITION__VALUE);

    queryconditionEClass = createEClass(QUERYCONDITION);
    createEAttribute(queryconditionEClass, QUERYCONDITION__QUERY_NOT);
    createEReference(queryconditionEClass, QUERYCONDITION__QUERY_FUNCT);

    systemconditionEClass = createEClass(SYSTEMCONDITION);
    createEAttribute(systemconditionEClass, SYSTEMCONDITION__SYSTEM_ATTRIBUTE);
    createEAttribute(systemconditionEClass, SYSTEMCONDITION__OPERATOR);
    createEReference(systemconditionEClass, SYSTEMCONDITION__VALUE);

    freeconditionEClass = createEClass(FREECONDITION);
    createEAttribute(freeconditionEClass, FREECONDITION__FREE_CONDITION);

    mapconditionEClass = createEClass(MAPCONDITION);
    createEAttribute(mapconditionEClass, MAPCONDITION__MAP_COND);

    commandactionEClass = createEClass(COMMANDACTION);
    createEAttribute(commandactionEClass, COMMANDACTION__SUB_ACTNAME);
    createEReference(commandactionEClass, COMMANDACTION__FUNCT_ACTION);
    createEReference(commandactionEClass, COMMANDACTION__ACTION_VALUE);
    createEReference(commandactionEClass, COMMANDACTION__INNER_ACTION);

    rndqueryEClass = createEClass(RNDQUERY);
    createEAttribute(rndqueryEClass, RNDQUERY__PRI_OPERATOR);
    createEAttribute(rndqueryEClass, RNDQUERY__PRI_VAL);
    createEAttribute(rndqueryEClass, RNDQUERY__SEL);
    createEAttribute(rndqueryEClass, RNDQUERY__STATE_NAME);

    sourceEClass = createEClass(SOURCE);
    createEAttribute(sourceEClass, SOURCE__NAME);

    ecaValueEClass = createEClass(ECA_VALUE);
    createEAttribute(ecaValueEClass, ECA_VALUE__INT_VALUE);
    createEAttribute(ecaValueEClass, ECA_VALUE__ID_VALUE);
    createEReference(ecaValueEClass, ECA_VALUE__CONST_VALUE);
    createEAttribute(ecaValueEClass, ECA_VALUE__STRING_VALUE);
    createEAttribute(ecaValueEClass, ECA_VALUE__DOUBLE_VALUE);

    conditionsEClass = createEClass(CONDITIONS);
    createEReference(conditionsEClass, CONDITIONS__LEFT);
    createEReference(conditionsEClass, CONDITIONS__RIGHT);

    subconditionEClass = createEClass(SUBCONDITION);
    createEReference(subconditionEClass, SUBCONDITION__SUBFREE);
    createEReference(subconditionEClass, SUBCONDITION__SUBMAP);
    createEReference(subconditionEClass, SUBCONDITION__QUERY_COND);

    actionsEClass = createEClass(ACTIONS);
    createEReference(actionsEClass, ACTIONS__LEFT);
    createEReference(actionsEClass, ACTIONS__RIGHT);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    conditionsEClass.getESuperTypes().add(this.getExpression());
    subconditionEClass.getESuperTypes().add(this.getExpression());
    actionsEClass.getESuperTypes().add(this.getExpression());

    // Initialize classes and features; add operations and parameters
    initEClass(modelEClass, Model.class, "Model", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getModel_Constants(), this.getConstant(), null, "constants", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_DefEvents(), this.getDefinedEvent(), null, "defEvents", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_WindowSize(), this.getWindow(), null, "windowSize", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_TimeIntervall(), this.getTimer(), null, "timeIntervall", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_Rules(), this.getRule(), null, "rules", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(constantEClass, Constant.class, "Constant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConstant_Name(), ecorePackage.getEString(), "name", null, 0, 1, Constant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConstant_ConstValue(), ecorePackage.getEInt(), "constValue", null, 0, 1, Constant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(windowEClass, Window.class, "Window", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getWindow_WindowValue(), ecorePackage.getEInt(), "windowValue", null, 0, 1, Window.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(timerEClass, Timer.class, "Timer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTimer_TimerIntervallValue(), ecorePackage.getEInt(), "timerIntervallValue", null, 0, 1, Timer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(definedEventEClass, DefinedEvent.class, "DefinedEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDefinedEvent_Name(), ecorePackage.getEString(), "name", null, 0, 1, DefinedEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDefinedEvent_DefinedSource(), this.getSource(), null, "definedSource", null, 0, 1, DefinedEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDefinedEvent_DefinedAttribute(), ecorePackage.getEString(), "definedAttribute", null, 0, 1, DefinedEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDefinedEvent_DefinedOperator(), ecorePackage.getEString(), "definedOperator", null, 0, 1, DefinedEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDefinedEvent_DefinedValue(), this.getEcaValue(), null, "definedValue", null, 0, 1, DefinedEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ruleEClass, Rule.class, "Rule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRule_Name(), ecorePackage.getEString(), "name", null, 0, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRule_Source(), this.getRuleSource(), null, "source", null, 0, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRule_RuleConditions(), this.getExpression(), null, "ruleConditions", null, 0, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRule_RuleActions(), this.getExpression(), null, "ruleActions", null, 0, 1, Rule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getExpression_Subsource(), this.getSOURCECONDITION(), null, "subsource", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpression_Subsys(), this.getSYSTEMCONDITION(), null, "subsys", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExpression_ComAction(), this.getCOMMANDACTION(), null, "comAction", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ruleSourceEClass, RuleSource.class, "RuleSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRuleSource_DefSource(), this.getDefinedEvent(), null, "defSource", null, 0, 1, RuleSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRuleSource_NewSource(), this.getSource(), null, "newSource", null, 0, 1, RuleSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRuleSource_PreSource(), ecorePackage.getEString(), "preSource", null, 0, 1, RuleSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(sourceconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION.class, "SOURCECONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSOURCECONDITION_CondAttribute(), ecorePackage.getEString(), "condAttribute", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSOURCECONDITION_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSOURCECONDITION_Value(), this.getEcaValue(), null, "value", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(queryconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION.class, "QUERYCONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getQUERYCONDITION_QueryNot(), ecorePackage.getEString(), "queryNot", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getQUERYCONDITION_QueryFunct(), this.getRNDQUERY(), null, "queryFunct", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(systemconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION.class, "SYSTEMCONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSYSTEMCONDITION_SystemAttribute(), ecorePackage.getEString(), "systemAttribute", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSYSTEMCONDITION_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSYSTEMCONDITION_Value(), this.getEcaValue(), null, "value", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(freeconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION.class, "FREECONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFREECONDITION_FreeCondition(), ecorePackage.getEString(), "freeCondition", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mapconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION.class, "MAPCONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMAPCONDITION_MapCond(), ecorePackage.getEString(), "mapCond", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(commandactionEClass, de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION.class, "COMMANDACTION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCOMMANDACTION_SubActname(), ecorePackage.getEString(), "subActname", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCOMMANDACTION_FunctAction(), this.getRNDQUERY(), null, "functAction", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCOMMANDACTION_ActionValue(), this.getEcaValue(), null, "actionValue", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCOMMANDACTION_InnerAction(), this.getCOMMANDACTION(), null, "innerAction", null, 0, -1, de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rndqueryEClass, de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY.class, "RNDQUERY", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRNDQUERY_PriOperator(), ecorePackage.getEString(), "priOperator", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRNDQUERY_PriVal(), ecorePackage.getEInt(), "priVal", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRNDQUERY_Sel(), ecorePackage.getEString(), "sel", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRNDQUERY_StateName(), ecorePackage.getEString(), "stateName", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(sourceEClass, Source.class, "Source", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSource_Name(), ecorePackage.getEString(), "name", null, 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(ecaValueEClass, EcaValue.class, "EcaValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEcaValue_IntValue(), ecorePackage.getEInt(), "intValue", null, 0, 1, EcaValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEcaValue_IdValue(), ecorePackage.getEString(), "idValue", null, 0, 1, EcaValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getEcaValue_ConstValue(), this.getConstant(), null, "constValue", null, 0, 1, EcaValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEcaValue_StringValue(), ecorePackage.getEString(), "stringValue", null, 0, 1, EcaValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEcaValue_DoubleValue(), ecorePackage.getEDouble(), "doubleValue", null, 0, 1, EcaValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(conditionsEClass, de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS.class, "CONDITIONS", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCONDITIONS_Left(), this.getExpression(), null, "left", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCONDITIONS_Right(), this.getExpression(), null, "right", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(subconditionEClass, de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION.class, "SUBCONDITION", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSUBCONDITION_Subfree(), this.getFREECONDITION(), null, "subfree", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSUBCONDITION_Submap(), this.getMAPCONDITION(), null, "submap", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSUBCONDITION_QueryCond(), this.getQUERYCONDITION(), null, "queryCond", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(actionsEClass, de.uniol.inf.is.odysseus.eca.eCA.ACTIONS.class, "ACTIONS", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getACTIONS_Left(), this.getExpression(), null, "left", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.ACTIONS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getACTIONS_Right(), this.getExpression(), null, "right", null, 0, 1, de.uniol.inf.is.odysseus.eca.eCA.ACTIONS.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //ECAPackageImpl
