/**
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.cQL.impl;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.AccessFramework;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Access Framework</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getWrapper <em>Wrapper</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getProtocol <em>Protocol</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getTransport <em>Transport</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getDatahandler <em>Datahandler</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getKeys <em>Keys</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.AccessFrameworkImpl#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AccessFrameworkImpl extends MinimalEObjectImpl.Container implements AccessFramework
{
  /**
   * The default value of the '{@link #getWrapper() <em>Wrapper</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWrapper()
   * @generated
   * @ordered
   */
  protected static final String WRAPPER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getWrapper() <em>Wrapper</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWrapper()
   * @generated
   * @ordered
   */
  protected String wrapper = WRAPPER_EDEFAULT;

  /**
   * The default value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProtocol()
   * @generated
   * @ordered
   */
  protected static final String PROTOCOL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProtocol()
   * @generated
   * @ordered
   */
  protected String protocol = PROTOCOL_EDEFAULT;

  /**
   * The default value of the '{@link #getTransport() <em>Transport</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransport()
   * @generated
   * @ordered
   */
  protected static final String TRANSPORT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTransport() <em>Transport</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransport()
   * @generated
   * @ordered
   */
  protected String transport = TRANSPORT_EDEFAULT;

  /**
   * The default value of the '{@link #getDatahandler() <em>Datahandler</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatahandler()
   * @generated
   * @ordered
   */
  protected static final String DATAHANDLER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDatahandler() <em>Datahandler</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatahandler()
   * @generated
   * @ordered
   */
  protected String datahandler = DATAHANDLER_EDEFAULT;

  /**
   * The cached value of the '{@link #getKeys() <em>Keys</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKeys()
   * @generated
   * @ordered
   */
  protected EList<String> keys;

  /**
   * The cached value of the '{@link #getValues() <em>Values</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValues()
   * @generated
   * @ordered
   */
  protected EList<String> values;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AccessFrameworkImpl()
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
    return CQLPackage.Literals.ACCESS_FRAMEWORK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getWrapper()
  {
    return wrapper;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWrapper(String newWrapper)
  {
    String oldWrapper = wrapper;
    wrapper = newWrapper;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.ACCESS_FRAMEWORK__WRAPPER, oldWrapper, wrapper));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProtocol()
  {
    return protocol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProtocol(String newProtocol)
  {
    String oldProtocol = protocol;
    protocol = newProtocol;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.ACCESS_FRAMEWORK__PROTOCOL, oldProtocol, protocol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTransport()
  {
    return transport;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTransport(String newTransport)
  {
    String oldTransport = transport;
    transport = newTransport;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.ACCESS_FRAMEWORK__TRANSPORT, oldTransport, transport));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDatahandler()
  {
    return datahandler;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDatahandler(String newDatahandler)
  {
    String oldDatahandler = datahandler;
    datahandler = newDatahandler;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.ACCESS_FRAMEWORK__DATAHANDLER, oldDatahandler, datahandler));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getKeys()
  {
    if (keys == null)
    {
      keys = new EDataTypeEList<String>(String.class, this, CQLPackage.ACCESS_FRAMEWORK__KEYS);
    }
    return keys;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getValues()
  {
    if (values == null)
    {
      values = new EDataTypeEList<String>(String.class, this, CQLPackage.ACCESS_FRAMEWORK__VALUES);
    }
    return values;
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
      case CQLPackage.ACCESS_FRAMEWORK__WRAPPER:
        return getWrapper();
      case CQLPackage.ACCESS_FRAMEWORK__PROTOCOL:
        return getProtocol();
      case CQLPackage.ACCESS_FRAMEWORK__TRANSPORT:
        return getTransport();
      case CQLPackage.ACCESS_FRAMEWORK__DATAHANDLER:
        return getDatahandler();
      case CQLPackage.ACCESS_FRAMEWORK__KEYS:
        return getKeys();
      case CQLPackage.ACCESS_FRAMEWORK__VALUES:
        return getValues();
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
      case CQLPackage.ACCESS_FRAMEWORK__WRAPPER:
        setWrapper((String)newValue);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__PROTOCOL:
        setProtocol((String)newValue);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__TRANSPORT:
        setTransport((String)newValue);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__DATAHANDLER:
        setDatahandler((String)newValue);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__KEYS:
        getKeys().clear();
        getKeys().addAll((Collection<? extends String>)newValue);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__VALUES:
        getValues().clear();
        getValues().addAll((Collection<? extends String>)newValue);
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
      case CQLPackage.ACCESS_FRAMEWORK__WRAPPER:
        setWrapper(WRAPPER_EDEFAULT);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__PROTOCOL:
        setProtocol(PROTOCOL_EDEFAULT);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__TRANSPORT:
        setTransport(TRANSPORT_EDEFAULT);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__DATAHANDLER:
        setDatahandler(DATAHANDLER_EDEFAULT);
        return;
      case CQLPackage.ACCESS_FRAMEWORK__KEYS:
        getKeys().clear();
        return;
      case CQLPackage.ACCESS_FRAMEWORK__VALUES:
        getValues().clear();
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
      case CQLPackage.ACCESS_FRAMEWORK__WRAPPER:
        return WRAPPER_EDEFAULT == null ? wrapper != null : !WRAPPER_EDEFAULT.equals(wrapper);
      case CQLPackage.ACCESS_FRAMEWORK__PROTOCOL:
        return PROTOCOL_EDEFAULT == null ? protocol != null : !PROTOCOL_EDEFAULT.equals(protocol);
      case CQLPackage.ACCESS_FRAMEWORK__TRANSPORT:
        return TRANSPORT_EDEFAULT == null ? transport != null : !TRANSPORT_EDEFAULT.equals(transport);
      case CQLPackage.ACCESS_FRAMEWORK__DATAHANDLER:
        return DATAHANDLER_EDEFAULT == null ? datahandler != null : !DATAHANDLER_EDEFAULT.equals(datahandler);
      case CQLPackage.ACCESS_FRAMEWORK__KEYS:
        return keys != null && !keys.isEmpty();
      case CQLPackage.ACCESS_FRAMEWORK__VALUES:
        return values != null && !values.isEmpty();
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
    result.append(" (wrapper: ");
    result.append(wrapper);
    result.append(", protocol: ");
    result.append(protocol);
    result.append(", transport: ");
    result.append(transport);
    result.append(", datahandler: ");
    result.append(datahandler);
    result.append(", keys: ");
    result.append(keys);
    result.append(", values: ");
    result.append(values);
    result.append(')');
    return result.toString();
  }

} //AccessFrameworkImpl
