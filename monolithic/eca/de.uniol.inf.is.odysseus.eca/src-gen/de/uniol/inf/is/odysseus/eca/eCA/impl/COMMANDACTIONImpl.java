/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>COMMANDACTION</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl#getSubActname <em>Sub Actname</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl#getFunctAction <em>Funct Action</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl#getActionValue <em>Action Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.COMMANDACTIONImpl#getInnerAction <em>Inner Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class COMMANDACTIONImpl extends MinimalEObjectImpl.Container implements COMMANDACTION
{
  /**
   * The default value of the '{@link #getSubActname() <em>Sub Actname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubActname()
   * @generated
   * @ordered
   */
  protected static final String SUB_ACTNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubActname() <em>Sub Actname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubActname()
   * @generated
   * @ordered
   */
  protected String subActname = SUB_ACTNAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getFunctAction() <em>Funct Action</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunctAction()
   * @generated
   * @ordered
   */
  protected RNDQUERY functAction;

  /**
   * The cached value of the '{@link #getActionValue() <em>Action Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActionValue()
   * @generated
   * @ordered
   */
  protected EcaValue actionValue;

  /**
   * The cached value of the '{@link #getInnerAction() <em>Inner Action</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInnerAction()
   * @generated
   * @ordered
   */
  protected EList<COMMANDACTION> innerAction;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected COMMANDACTIONImpl()
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
    return ECAPackage.Literals.COMMANDACTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubActname()
  {
    return subActname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubActname(String newSubActname)
  {
    String oldSubActname = subActname;
    subActname = newSubActname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.COMMANDACTION__SUB_ACTNAME, oldSubActname, subActname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RNDQUERY getFunctAction()
  {
    return functAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFunctAction(RNDQUERY newFunctAction, NotificationChain msgs)
  {
    RNDQUERY oldFunctAction = functAction;
    functAction = newFunctAction;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.COMMANDACTION__FUNCT_ACTION, oldFunctAction, newFunctAction);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFunctAction(RNDQUERY newFunctAction)
  {
    if (newFunctAction != functAction)
    {
      NotificationChain msgs = null;
      if (functAction != null)
        msgs = ((InternalEObject)functAction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.COMMANDACTION__FUNCT_ACTION, null, msgs);
      if (newFunctAction != null)
        msgs = ((InternalEObject)newFunctAction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.COMMANDACTION__FUNCT_ACTION, null, msgs);
      msgs = basicSetFunctAction(newFunctAction, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.COMMANDACTION__FUNCT_ACTION, newFunctAction, newFunctAction));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EcaValue getActionValue()
  {
    return actionValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetActionValue(EcaValue newActionValue, NotificationChain msgs)
  {
    EcaValue oldActionValue = actionValue;
    actionValue = newActionValue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.COMMANDACTION__ACTION_VALUE, oldActionValue, newActionValue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActionValue(EcaValue newActionValue)
  {
    if (newActionValue != actionValue)
    {
      NotificationChain msgs = null;
      if (actionValue != null)
        msgs = ((InternalEObject)actionValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.COMMANDACTION__ACTION_VALUE, null, msgs);
      if (newActionValue != null)
        msgs = ((InternalEObject)newActionValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.COMMANDACTION__ACTION_VALUE, null, msgs);
      msgs = basicSetActionValue(newActionValue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.COMMANDACTION__ACTION_VALUE, newActionValue, newActionValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<COMMANDACTION> getInnerAction()
  {
    if (innerAction == null)
    {
      innerAction = new EObjectContainmentEList<COMMANDACTION>(COMMANDACTION.class, this, ECAPackage.COMMANDACTION__INNER_ACTION);
    }
    return innerAction;
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
      case ECAPackage.COMMANDACTION__FUNCT_ACTION:
        return basicSetFunctAction(null, msgs);
      case ECAPackage.COMMANDACTION__ACTION_VALUE:
        return basicSetActionValue(null, msgs);
      case ECAPackage.COMMANDACTION__INNER_ACTION:
        return ((InternalEList<?>)getInnerAction()).basicRemove(otherEnd, msgs);
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
      case ECAPackage.COMMANDACTION__SUB_ACTNAME:
        return getSubActname();
      case ECAPackage.COMMANDACTION__FUNCT_ACTION:
        return getFunctAction();
      case ECAPackage.COMMANDACTION__ACTION_VALUE:
        return getActionValue();
      case ECAPackage.COMMANDACTION__INNER_ACTION:
        return getInnerAction();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ECAPackage.COMMANDACTION__SUB_ACTNAME:
        setSubActname((String)newValue);
        return;
      case ECAPackage.COMMANDACTION__FUNCT_ACTION:
        setFunctAction((RNDQUERY)newValue);
        return;
      case ECAPackage.COMMANDACTION__ACTION_VALUE:
        setActionValue((EcaValue)newValue);
        return;
      case ECAPackage.COMMANDACTION__INNER_ACTION:
        getInnerAction().clear();
        getInnerAction().addAll((Collection<? extends COMMANDACTION>)newValue);
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
      case ECAPackage.COMMANDACTION__SUB_ACTNAME:
        setSubActname(SUB_ACTNAME_EDEFAULT);
        return;
      case ECAPackage.COMMANDACTION__FUNCT_ACTION:
        setFunctAction((RNDQUERY)null);
        return;
      case ECAPackage.COMMANDACTION__ACTION_VALUE:
        setActionValue((EcaValue)null);
        return;
      case ECAPackage.COMMANDACTION__INNER_ACTION:
        getInnerAction().clear();
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
      case ECAPackage.COMMANDACTION__SUB_ACTNAME:
        return SUB_ACTNAME_EDEFAULT == null ? subActname != null : !SUB_ACTNAME_EDEFAULT.equals(subActname);
      case ECAPackage.COMMANDACTION__FUNCT_ACTION:
        return functAction != null;
      case ECAPackage.COMMANDACTION__ACTION_VALUE:
        return actionValue != null;
      case ECAPackage.COMMANDACTION__INNER_ACTION:
        return innerAction != null && !innerAction.isEmpty();
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
    result.append(" (subActname: ");
    result.append(subActname);
    result.append(')');
    return result.toString();
  }

} //COMMANDACTIONImpl
