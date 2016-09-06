/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.Expression;
import de.uniol.inf.is.odysseus.eca.eCA.Rule;
import de.uniol.inf.is.odysseus.eca.eCA.RuleSource;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl#getSource <em>Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl#getRuleConditions <em>Rule Conditions</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleImpl#getRuleActions <em>Rule Actions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RuleImpl extends MinimalEObjectImpl.Container implements Rule
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getSource() <em>Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected RuleSource source;

  /**
   * The cached value of the '{@link #getRuleConditions() <em>Rule Conditions</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRuleConditions()
   * @generated
   * @ordered
   */
  protected Expression ruleConditions;

  /**
   * The cached value of the '{@link #getRuleActions() <em>Rule Actions</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRuleActions()
   * @generated
   * @ordered
   */
  protected Expression ruleActions;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RuleImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ECAPackage.Literals.RULE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RuleSource getSource()
  {
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSource(RuleSource newSource, NotificationChain msgs)
  {
    RuleSource oldSource = source;
    source = newSource;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__SOURCE, oldSource, newSource);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSource(RuleSource newSource)
  {
    if (newSource != source)
    {
      NotificationChain msgs = null;
      if (source != null)
        msgs = ((InternalEObject)source).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__SOURCE, null, msgs);
      if (newSource != null)
        msgs = ((InternalEObject)newSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__SOURCE, null, msgs);
      msgs = basicSetSource(newSource, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__SOURCE, newSource, newSource));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Expression getRuleConditions()
  {
    return ruleConditions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRuleConditions(Expression newRuleConditions, NotificationChain msgs)
  {
    Expression oldRuleConditions = ruleConditions;
    ruleConditions = newRuleConditions;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__RULE_CONDITIONS, oldRuleConditions, newRuleConditions);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRuleConditions(Expression newRuleConditions)
  {
    if (newRuleConditions != ruleConditions)
    {
      NotificationChain msgs = null;
      if (ruleConditions != null)
        msgs = ((InternalEObject)ruleConditions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__RULE_CONDITIONS, null, msgs);
      if (newRuleConditions != null)
        msgs = ((InternalEObject)newRuleConditions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__RULE_CONDITIONS, null, msgs);
      msgs = basicSetRuleConditions(newRuleConditions, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__RULE_CONDITIONS, newRuleConditions, newRuleConditions));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Expression getRuleActions()
  {
    return ruleActions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRuleActions(Expression newRuleActions, NotificationChain msgs)
  {
    Expression oldRuleActions = ruleActions;
    ruleActions = newRuleActions;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__RULE_ACTIONS, oldRuleActions, newRuleActions);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRuleActions(Expression newRuleActions)
  {
    if (newRuleActions != ruleActions)
    {
      NotificationChain msgs = null;
      if (ruleActions != null)
        msgs = ((InternalEObject)ruleActions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__RULE_ACTIONS, null, msgs);
      if (newRuleActions != null)
        msgs = ((InternalEObject)newRuleActions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE__RULE_ACTIONS, null, msgs);
      msgs = basicSetRuleActions(newRuleActions, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE__RULE_ACTIONS, newRuleActions, newRuleActions));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ECAPackage.RULE__SOURCE:
        return basicSetSource(null, msgs);
      case ECAPackage.RULE__RULE_CONDITIONS:
        return basicSetRuleConditions(null, msgs);
      case ECAPackage.RULE__RULE_ACTIONS:
        return basicSetRuleActions(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ECAPackage.RULE__NAME:
        return getName();
      case ECAPackage.RULE__SOURCE:
        return getSource();
      case ECAPackage.RULE__RULE_CONDITIONS:
        return getRuleConditions();
      case ECAPackage.RULE__RULE_ACTIONS:
        return getRuleActions();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ECAPackage.RULE__NAME:
        setName((String)newValue);
        return;
      case ECAPackage.RULE__SOURCE:
        setSource((RuleSource)newValue);
        return;
      case ECAPackage.RULE__RULE_CONDITIONS:
        setRuleConditions((Expression)newValue);
        return;
      case ECAPackage.RULE__RULE_ACTIONS:
        setRuleActions((Expression)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ECAPackage.RULE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ECAPackage.RULE__SOURCE:
        setSource((RuleSource)null);
        return;
      case ECAPackage.RULE__RULE_CONDITIONS:
        setRuleConditions((Expression)null);
        return;
      case ECAPackage.RULE__RULE_ACTIONS:
        setRuleActions((Expression)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ECAPackage.RULE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ECAPackage.RULE__SOURCE:
        return source != null;
      case ECAPackage.RULE__RULE_CONDITIONS:
        return ruleConditions != null;
      case ECAPackage.RULE__RULE_ACTIONS:
        return ruleActions != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //RuleImpl
