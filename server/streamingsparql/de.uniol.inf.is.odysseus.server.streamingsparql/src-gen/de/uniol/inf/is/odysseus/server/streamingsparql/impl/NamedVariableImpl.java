/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Prefix;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Named Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.NamedVariableImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamedVariableImpl extends MinimalEObjectImpl.Container implements NamedVariable
{
  /**
   * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrefix()
   * @generated
   * @ordered
   */
  protected Prefix prefix;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NamedVariableImpl()
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
    return StreamingsparqlPackage.Literals.NAMED_VARIABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Prefix getPrefix()
  {
    if (prefix != null && prefix.eIsProxy())
    {
      InternalEObject oldPrefix = (InternalEObject)prefix;
      prefix = (Prefix)eResolveProxy(oldPrefix);
      if (prefix != oldPrefix)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StreamingsparqlPackage.NAMED_VARIABLE__PREFIX, oldPrefix, prefix));
      }
    }
    return prefix;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Prefix basicGetPrefix()
  {
    return prefix;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPrefix(Prefix newPrefix)
  {
    Prefix oldPrefix = prefix;
    prefix = newPrefix;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.NAMED_VARIABLE__PREFIX, oldPrefix, prefix));
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
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.NAMED_VARIABLE__NAME, oldName, name));
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
      case StreamingsparqlPackage.NAMED_VARIABLE__PREFIX:
        if (resolve) return getPrefix();
        return basicGetPrefix();
      case StreamingsparqlPackage.NAMED_VARIABLE__NAME:
        return getName();
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
      case StreamingsparqlPackage.NAMED_VARIABLE__PREFIX:
        setPrefix((Prefix)newValue);
        return;
      case StreamingsparqlPackage.NAMED_VARIABLE__NAME:
        setName((String)newValue);
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
      case StreamingsparqlPackage.NAMED_VARIABLE__PREFIX:
        setPrefix((Prefix)null);
        return;
      case StreamingsparqlPackage.NAMED_VARIABLE__NAME:
        setName(NAME_EDEFAULT);
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
      case StreamingsparqlPackage.NAMED_VARIABLE__PREFIX:
        return prefix != null;
      case StreamingsparqlPackage.NAMED_VARIABLE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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

} //NamedVariableImpl
