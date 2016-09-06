/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.Expression;
import de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl#getSubsource <em>Subsource</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl#getSubsys <em>Subsys</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ExpressionImpl#getComAction <em>Com Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExpressionImpl extends MinimalEObjectImpl.Container implements Expression
{
  /**
   * The cached value of the '{@link #getSubsource() <em>Subsource</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubsource()
   * @generated
   * @ordered
   */
  protected SOURCECONDITION subsource;

  /**
   * The cached value of the '{@link #getSubsys() <em>Subsys</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubsys()
   * @generated
   * @ordered
   */
  protected SYSTEMCONDITION subsys;

  /**
   * The cached value of the '{@link #getComAction() <em>Com Action</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComAction()
   * @generated
   * @ordered
   */
  protected COMMANDACTION comAction;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExpressionImpl()
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
    return ECAPackage.Literals.EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOURCECONDITION getSubsource()
  {
    return subsource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubsource(SOURCECONDITION newSubsource, NotificationChain msgs)
  {
    SOURCECONDITION oldSubsource = subsource;
    subsource = newSubsource;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__SUBSOURCE, oldSubsource, newSubsource);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubsource(SOURCECONDITION newSubsource)
  {
    if (newSubsource != subsource)
    {
      NotificationChain msgs = null;
      if (subsource != null)
        msgs = ((InternalEObject)subsource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__SUBSOURCE, null, msgs);
      if (newSubsource != null)
        msgs = ((InternalEObject)newSubsource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__SUBSOURCE, null, msgs);
      msgs = basicSetSubsource(newSubsource, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__SUBSOURCE, newSubsource, newSubsource));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SYSTEMCONDITION getSubsys()
  {
    return subsys;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubsys(SYSTEMCONDITION newSubsys, NotificationChain msgs)
  {
    SYSTEMCONDITION oldSubsys = subsys;
    subsys = newSubsys;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__SUBSYS, oldSubsys, newSubsys);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubsys(SYSTEMCONDITION newSubsys)
  {
    if (newSubsys != subsys)
    {
      NotificationChain msgs = null;
      if (subsys != null)
        msgs = ((InternalEObject)subsys).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__SUBSYS, null, msgs);
      if (newSubsys != null)
        msgs = ((InternalEObject)newSubsys).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__SUBSYS, null, msgs);
      msgs = basicSetSubsys(newSubsys, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__SUBSYS, newSubsys, newSubsys));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public COMMANDACTION getComAction()
  {
    return comAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetComAction(COMMANDACTION newComAction, NotificationChain msgs)
  {
    COMMANDACTION oldComAction = comAction;
    comAction = newComAction;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__COM_ACTION, oldComAction, newComAction);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComAction(COMMANDACTION newComAction)
  {
    if (newComAction != comAction)
    {
      NotificationChain msgs = null;
      if (comAction != null)
        msgs = ((InternalEObject)comAction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__COM_ACTION, null, msgs);
      if (newComAction != null)
        msgs = ((InternalEObject)newComAction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.EXPRESSION__COM_ACTION, null, msgs);
      msgs = basicSetComAction(newComAction, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.EXPRESSION__COM_ACTION, newComAction, newComAction));
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
      case ECAPackage.EXPRESSION__SUBSOURCE:
        return basicSetSubsource(null, msgs);
      case ECAPackage.EXPRESSION__SUBSYS:
        return basicSetSubsys(null, msgs);
      case ECAPackage.EXPRESSION__COM_ACTION:
        return basicSetComAction(null, msgs);
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
      case ECAPackage.EXPRESSION__SUBSOURCE:
        return getSubsource();
      case ECAPackage.EXPRESSION__SUBSYS:
        return getSubsys();
      case ECAPackage.EXPRESSION__COM_ACTION:
        return getComAction();
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
      case ECAPackage.EXPRESSION__SUBSOURCE:
        setSubsource((SOURCECONDITION)newValue);
        return;
      case ECAPackage.EXPRESSION__SUBSYS:
        setSubsys((SYSTEMCONDITION)newValue);
        return;
      case ECAPackage.EXPRESSION__COM_ACTION:
        setComAction((COMMANDACTION)newValue);
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
      case ECAPackage.EXPRESSION__SUBSOURCE:
        setSubsource((SOURCECONDITION)null);
        return;
      case ECAPackage.EXPRESSION__SUBSYS:
        setSubsys((SYSTEMCONDITION)null);
        return;
      case ECAPackage.EXPRESSION__COM_ACTION:
        setComAction((COMMANDACTION)null);
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
      case ECAPackage.EXPRESSION__SUBSOURCE:
        return subsource != null;
      case ECAPackage.EXPRESSION__SUBSYS:
        return subsys != null;
      case ECAPackage.EXPRESSION__COM_ACTION:
        return comAction != null;
    }
    return super.eIsSet(featureID);
  }

} //ExpressionImpl
