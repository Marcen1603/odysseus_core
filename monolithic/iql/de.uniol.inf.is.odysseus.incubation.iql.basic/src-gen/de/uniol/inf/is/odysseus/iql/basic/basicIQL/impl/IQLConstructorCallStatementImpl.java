/**
 */
package de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLConstructorCallStatement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQL Constructor Call Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl#isThis <em>This</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl#isSuper <em>Super</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.basic.basicIQL.impl.IQLConstructorCallStatementImpl#getArgs <em>Args</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IQLConstructorCallStatementImpl extends IQLStatementImpl implements IQLConstructorCallStatement
{
  /**
   * The default value of the '{@link #isThis() <em>This</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isThis()
   * @generated
   * @ordered
   */
  protected static final boolean THIS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isThis() <em>This</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isThis()
   * @generated
   * @ordered
   */
  protected boolean this_ = THIS_EDEFAULT;

  /**
   * The default value of the '{@link #isSuper() <em>Super</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSuper()
   * @generated
   * @ordered
   */
  protected static final boolean SUPER_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isSuper() <em>Super</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSuper()
   * @generated
   * @ordered
   */
  protected boolean super_ = SUPER_EDEFAULT;

  /**
   * The cached value of the '{@link #getArgs() <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArgs()
   * @generated
   * @ordered
   */
  protected IQLArgumentsList args;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IQLConstructorCallStatementImpl()
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
    return BasicIQLPackage.Literals.IQL_CONSTRUCTOR_CALL_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isThis()
  {
    return this_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setThis(boolean newThis)
  {
    boolean oldThis = this_;
    this_ = newThis;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__THIS, oldThis, this_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSuper()
  {
    return super_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSuper(boolean newSuper)
  {
    boolean oldSuper = super_;
    super_ = newSuper;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER, oldSuper, super_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLArgumentsList getArgs()
  {
    return args;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetArgs(IQLArgumentsList newArgs, NotificationChain msgs)
  {
    IQLArgumentsList oldArgs = args;
    args = newArgs;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS, oldArgs, newArgs);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArgs(IQLArgumentsList newArgs)
  {
    if (newArgs != args)
    {
      NotificationChain msgs = null;
      if (args != null)
        msgs = ((InternalEObject)args).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS, null, msgs);
      if (newArgs != null)
        msgs = ((InternalEObject)newArgs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS, null, msgs);
      msgs = basicSetArgs(newArgs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS, newArgs, newArgs));
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
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS:
        return basicSetArgs(null, msgs);
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
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__THIS:
        return isThis();
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER:
        return isSuper();
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS:
        return getArgs();
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
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__THIS:
        setThis((Boolean)newValue);
        return;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER:
        setSuper((Boolean)newValue);
        return;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS:
        setArgs((IQLArgumentsList)newValue);
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
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__THIS:
        setThis(THIS_EDEFAULT);
        return;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER:
        setSuper(SUPER_EDEFAULT);
        return;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS:
        setArgs((IQLArgumentsList)null);
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
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__THIS:
        return this_ != THIS_EDEFAULT;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__SUPER:
        return super_ != SUPER_EDEFAULT;
      case BasicIQLPackage.IQL_CONSTRUCTOR_CALL_STATEMENT__ARGS:
        return args != null;
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
    result.append(" (this: ");
    result.append(this_);
    result.append(", super: ");
    result.append(super_);
    result.append(')');
    return result.toString();
  }

} //IQLConstructorCallStatementImpl
