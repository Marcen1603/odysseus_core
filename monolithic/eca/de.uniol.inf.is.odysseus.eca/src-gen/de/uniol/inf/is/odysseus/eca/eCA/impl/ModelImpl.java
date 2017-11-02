/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.Constant;
import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.Model;
import de.uniol.inf.is.odysseus.eca.eCA.Rule;
import de.uniol.inf.is.odysseus.eca.eCA.Timer;
import de.uniol.inf.is.odysseus.eca.eCA.Window;

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
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl#getConstants <em>Constants</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl#getDefEvents <em>Def Events</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl#getTimeIntervall <em>Time Intervall</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.ModelImpl#getRules <em>Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelImpl extends MinimalEObjectImpl.Container implements Model
{
  /**
   * The cached value of the '{@link #getConstants() <em>Constants</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConstants()
   * @generated
   * @ordered
   */
  protected EList<Constant> constants;

  /**
   * The cached value of the '{@link #getDefEvents() <em>Def Events</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefEvents()
   * @generated
   * @ordered
   */
  protected EList<DefinedEvent> defEvents;

  /**
   * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWindowSize()
   * @generated
   * @ordered
   */
  protected Window windowSize;

  /**
   * The cached value of the '{@link #getTimeIntervall() <em>Time Intervall</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimeIntervall()
   * @generated
   * @ordered
   */
  protected Timer timeIntervall;

  /**
   * The cached value of the '{@link #getRules() <em>Rules</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRules()
   * @generated
   * @ordered
   */
  protected EList<Rule> rules;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ModelImpl()
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
    return ECAPackage.Literals.MODEL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Constant> getConstants()
  {
    if (constants == null)
    {
      constants = new EObjectContainmentEList<Constant>(Constant.class, this, ECAPackage.MODEL__CONSTANTS);
    }
    return constants;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<DefinedEvent> getDefEvents()
  {
    if (defEvents == null)
    {
      defEvents = new EObjectContainmentEList<DefinedEvent>(DefinedEvent.class, this, ECAPackage.MODEL__DEF_EVENTS);
    }
    return defEvents;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Window getWindowSize()
  {
    return windowSize;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWindowSize(Window newWindowSize, NotificationChain msgs)
  {
    Window oldWindowSize = windowSize;
    windowSize = newWindowSize;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.MODEL__WINDOW_SIZE, oldWindowSize, newWindowSize);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWindowSize(Window newWindowSize)
  {
    if (newWindowSize != windowSize)
    {
      NotificationChain msgs = null;
      if (windowSize != null)
        msgs = ((InternalEObject)windowSize).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.MODEL__WINDOW_SIZE, null, msgs);
      if (newWindowSize != null)
        msgs = ((InternalEObject)newWindowSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.MODEL__WINDOW_SIZE, null, msgs);
      msgs = basicSetWindowSize(newWindowSize, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.MODEL__WINDOW_SIZE, newWindowSize, newWindowSize));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Timer getTimeIntervall()
  {
    return timeIntervall;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTimeIntervall(Timer newTimeIntervall, NotificationChain msgs)
  {
    Timer oldTimeIntervall = timeIntervall;
    timeIntervall = newTimeIntervall;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.MODEL__TIME_INTERVALL, oldTimeIntervall, newTimeIntervall);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTimeIntervall(Timer newTimeIntervall)
  {
    if (newTimeIntervall != timeIntervall)
    {
      NotificationChain msgs = null;
      if (timeIntervall != null)
        msgs = ((InternalEObject)timeIntervall).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.MODEL__TIME_INTERVALL, null, msgs);
      if (newTimeIntervall != null)
        msgs = ((InternalEObject)newTimeIntervall).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.MODEL__TIME_INTERVALL, null, msgs);
      msgs = basicSetTimeIntervall(newTimeIntervall, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.MODEL__TIME_INTERVALL, newTimeIntervall, newTimeIntervall));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Rule> getRules()
  {
    if (rules == null)
    {
      rules = new EObjectContainmentEList<Rule>(Rule.class, this, ECAPackage.MODEL__RULES);
    }
    return rules;
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
      case ECAPackage.MODEL__CONSTANTS:
        return ((InternalEList<?>)getConstants()).basicRemove(otherEnd, msgs);
      case ECAPackage.MODEL__DEF_EVENTS:
        return ((InternalEList<?>)getDefEvents()).basicRemove(otherEnd, msgs);
      case ECAPackage.MODEL__WINDOW_SIZE:
        return basicSetWindowSize(null, msgs);
      case ECAPackage.MODEL__TIME_INTERVALL:
        return basicSetTimeIntervall(null, msgs);
      case ECAPackage.MODEL__RULES:
        return ((InternalEList<?>)getRules()).basicRemove(otherEnd, msgs);
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
      case ECAPackage.MODEL__CONSTANTS:
        return getConstants();
      case ECAPackage.MODEL__DEF_EVENTS:
        return getDefEvents();
      case ECAPackage.MODEL__WINDOW_SIZE:
        return getWindowSize();
      case ECAPackage.MODEL__TIME_INTERVALL:
        return getTimeIntervall();
      case ECAPackage.MODEL__RULES:
        return getRules();
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
      case ECAPackage.MODEL__CONSTANTS:
        getConstants().clear();
        getConstants().addAll((Collection<? extends Constant>)newValue);
        return;
      case ECAPackage.MODEL__DEF_EVENTS:
        getDefEvents().clear();
        getDefEvents().addAll((Collection<? extends DefinedEvent>)newValue);
        return;
      case ECAPackage.MODEL__WINDOW_SIZE:
        setWindowSize((Window)newValue);
        return;
      case ECAPackage.MODEL__TIME_INTERVALL:
        setTimeIntervall((Timer)newValue);
        return;
      case ECAPackage.MODEL__RULES:
        getRules().clear();
        getRules().addAll((Collection<? extends Rule>)newValue);
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
      case ECAPackage.MODEL__CONSTANTS:
        getConstants().clear();
        return;
      case ECAPackage.MODEL__DEF_EVENTS:
        getDefEvents().clear();
        return;
      case ECAPackage.MODEL__WINDOW_SIZE:
        setWindowSize((Window)null);
        return;
      case ECAPackage.MODEL__TIME_INTERVALL:
        setTimeIntervall((Timer)null);
        return;
      case ECAPackage.MODEL__RULES:
        getRules().clear();
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
      case ECAPackage.MODEL__CONSTANTS:
        return constants != null && !constants.isEmpty();
      case ECAPackage.MODEL__DEF_EVENTS:
        return defEvents != null && !defEvents.isEmpty();
      case ECAPackage.MODEL__WINDOW_SIZE:
        return windowSize != null;
      case ECAPackage.MODEL__TIME_INTERVALL:
        return timeIntervall != null;
      case ECAPackage.MODEL__RULES:
        return rules != null && !rules.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ModelImpl
