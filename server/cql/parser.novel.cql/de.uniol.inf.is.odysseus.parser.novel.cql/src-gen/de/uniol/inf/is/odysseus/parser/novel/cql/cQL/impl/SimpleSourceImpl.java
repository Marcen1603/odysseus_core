/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.WindowOperator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SimpleSourceImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.SimpleSourceImpl#getWindow <em>Window</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleSourceImpl extends SourceImpl implements SimpleSource
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
   * The cached value of the '{@link #getWindow() <em>Window</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWindow()
   * @generated
   * @ordered
   */
  protected WindowOperator window;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SimpleSourceImpl()
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
    return CQLPackage.Literals.SIMPLE_SOURCE;
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
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.SIMPLE_SOURCE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WindowOperator getWindow()
  {
    return window;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWindow(WindowOperator newWindow, NotificationChain msgs)
  {
    WindowOperator oldWindow = window;
    window = newWindow;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.SIMPLE_SOURCE__WINDOW, oldWindow, newWindow);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWindow(WindowOperator newWindow)
  {
    if (newWindow != window)
    {
      NotificationChain msgs = null;
      if (window != null)
        msgs = ((InternalEObject)window).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SIMPLE_SOURCE__WINDOW, null, msgs);
      if (newWindow != null)
        msgs = ((InternalEObject)newWindow).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.SIMPLE_SOURCE__WINDOW, null, msgs);
      msgs = basicSetWindow(newWindow, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.SIMPLE_SOURCE__WINDOW, newWindow, newWindow));
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
      case CQLPackage.SIMPLE_SOURCE__WINDOW:
        return basicSetWindow(null, msgs);
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
      case CQLPackage.SIMPLE_SOURCE__NAME:
        return getName();
      case CQLPackage.SIMPLE_SOURCE__WINDOW:
        return getWindow();
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
      case CQLPackage.SIMPLE_SOURCE__NAME:
        setName((String)newValue);
        return;
      case CQLPackage.SIMPLE_SOURCE__WINDOW:
        setWindow((WindowOperator)newValue);
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
      case CQLPackage.SIMPLE_SOURCE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case CQLPackage.SIMPLE_SOURCE__WINDOW:
        setWindow((WindowOperator)null);
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
      case CQLPackage.SIMPLE_SOURCE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case CQLPackage.SIMPLE_SOURCE__WINDOW:
        return window != null;
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

} //SimpleSourceImpl
