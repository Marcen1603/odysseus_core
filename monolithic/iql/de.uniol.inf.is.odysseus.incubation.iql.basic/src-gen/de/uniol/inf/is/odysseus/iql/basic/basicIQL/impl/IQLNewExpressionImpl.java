/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL New Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl#getRef <em>Ref</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl#getArgsList <em>Args List</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLNewExpressionImpl#getArgsMap <em>Args Map</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IQLNewExpressionImpl extends IQLExpressionImpl implements IQLNewExpression
{
  /**
   * The cached value of the '{@link #getRef() <em>Ref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRef()
   * @generated
   * @ordered
   */
  protected JvmTypeReference ref;

  /**
   * The cached value of the '{@link #getArgsList() <em>Args List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArgsList()
   * @generated
   * @ordered
   */
  protected IQLArgumentsList argsList;

  /**
   * The cached value of the '{@link #getArgsMap() <em>Args Map</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArgsMap()
   * @generated
   * @ordered
   */
  protected IQLArgumentsMap argsMap;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLNewExpressionImpl()
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
    return BasicIQLPackage.Literals.IQL_NEW_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmTypeReference getRef()
  {
    return ref;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRef(JvmTypeReference newRef, NotificationChain msgs)
  {
    JvmTypeReference oldRef = ref;
    ref = newRef;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__REF, oldRef, newRef);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRef(JvmTypeReference newRef)
  {
    if (newRef != ref)
    {
      NotificationChain msgs = null;
      if (ref != null)
        msgs = ((InternalEObject)ref).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__REF, null, msgs);
      if (newRef != null)
        msgs = ((InternalEObject)newRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__REF, null, msgs);
      msgs = basicSetRef(newRef, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__REF, newRef, newRef));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsList getArgsList()
  {
    return argsList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetArgsList(IQLArgumentsList newArgsList, NotificationChain msgs)
  {
    IQLArgumentsList oldArgsList = argsList;
    argsList = newArgsList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST, oldArgsList, newArgsList);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArgsList(IQLArgumentsList newArgsList)
  {
    if (newArgsList != argsList)
    {
      NotificationChain msgs = null;
      if (argsList != null)
        msgs = ((InternalEObject)argsList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST, null, msgs);
      if (newArgsList != null)
        msgs = ((InternalEObject)newArgsList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST, null, msgs);
      msgs = basicSetArgsList(newArgsList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST, newArgsList, newArgsList));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsMap getArgsMap()
  {
    return argsMap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetArgsMap(IQLArgumentsMap newArgsMap, NotificationChain msgs)
  {
    IQLArgumentsMap oldArgsMap = argsMap;
    argsMap = newArgsMap;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP, oldArgsMap, newArgsMap);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArgsMap(IQLArgumentsMap newArgsMap)
  {
    if (newArgsMap != argsMap)
    {
      NotificationChain msgs = null;
      if (argsMap != null)
        msgs = ((InternalEObject)argsMap).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP, null, msgs);
      if (newArgsMap != null)
        msgs = ((InternalEObject)newArgsMap).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP, null, msgs);
      msgs = basicSetArgsMap(newArgsMap, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP, newArgsMap, newArgsMap));
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
      case BasicIQLPackage.IQL_NEW_EXPRESSION__REF:
        return basicSetRef(null, msgs);
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST:
        return basicSetArgsList(null, msgs);
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP:
        return basicSetArgsMap(null, msgs);
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
      case BasicIQLPackage.IQL_NEW_EXPRESSION__REF:
        return getRef();
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST:
        return getArgsList();
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP:
        return getArgsMap();
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
      case BasicIQLPackage.IQL_NEW_EXPRESSION__REF:
        setRef((JvmTypeReference)newValue);
        return;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST:
        setArgsList((IQLArgumentsList)newValue);
        return;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP:
        setArgsMap((IQLArgumentsMap)newValue);
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
      case BasicIQLPackage.IQL_NEW_EXPRESSION__REF:
        setRef((JvmTypeReference)null);
        return;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST:
        setArgsList((IQLArgumentsList)null);
        return;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP:
        setArgsMap((IQLArgumentsMap)null);
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
      case BasicIQLPackage.IQL_NEW_EXPRESSION__REF:
        return ref != null;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_LIST:
        return argsList != null;
      case BasicIQLPackage.IQL_NEW_EXPRESSION__ARGS_MAP:
        return argsMap != null;
    }
    return super.eIsSet(featureID);
  }

} //IQLNewExpressionImpl
