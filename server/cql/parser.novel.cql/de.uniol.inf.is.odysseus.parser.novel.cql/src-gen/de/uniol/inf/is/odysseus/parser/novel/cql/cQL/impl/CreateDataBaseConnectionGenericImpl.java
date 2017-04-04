/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionGeneric;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Create Data Base Connection Generic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionGenericImpl#getDriver <em>Driver</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionGenericImpl#getSource <em>Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionGenericImpl#getServer <em>Server</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionGenericImpl#getUser <em>User</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionGenericImpl#getPassword <em>Password</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CreateDataBaseConnectionGenericImpl extends CommandImpl implements CreateDataBaseConnectionGeneric
{
  /**
   * The default value of the '{@link #getDriver() <em>Driver</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriver()
   * @generated
   * @ordered
   */
  protected static final String DRIVER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDriver() <em>Driver</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriver()
   * @generated
   * @ordered
   */
  protected String driver = DRIVER_EDEFAULT;

  /**
   * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected static final String SOURCE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected String source = SOURCE_EDEFAULT;

  /**
   * The default value of the '{@link #getServer() <em>Server</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServer()
   * @generated
   * @ordered
   */
  protected static final String SERVER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getServer() <em>Server</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServer()
   * @generated
   * @ordered
   */
  protected String server = SERVER_EDEFAULT;

  /**
   * The default value of the '{@link #getUser() <em>User</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUser()
   * @generated
   * @ordered
   */
  protected static final String USER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUser() <em>User</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUser()
   * @generated
   * @ordered
   */
  protected String user = USER_EDEFAULT;

  /**
   * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected static final String PASSWORD_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected String password = PASSWORD_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CreateDataBaseConnectionGenericImpl()
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
    return CQLPackage.Literals.CREATE_DATA_BASE_CONNECTION_GENERIC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDriver()
  {
    return driver;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDriver(String newDriver)
  {
    String oldDriver = driver;
    driver = newDriver;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__DRIVER, oldDriver, driver));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSource()
  {
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSource(String newSource)
  {
    String oldSource = source;
    source = newSource;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SOURCE, oldSource, source));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getServer()
  {
    return server;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServer(String newServer)
  {
    String oldServer = server;
    server = newServer;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SERVER, oldServer, server));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUser()
  {
    return user;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUser(String newUser)
  {
    String oldUser = user;
    user = newUser;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__USER, oldUser, user));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPassword(String newPassword)
  {
    String oldPassword = password;
    password = newPassword;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__PASSWORD, oldPassword, password));
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__DRIVER:
        return getDriver();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SOURCE:
        return getSource();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SERVER:
        return getServer();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__USER:
        return getUser();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__PASSWORD:
        return getPassword();
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__DRIVER:
        setDriver((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SOURCE:
        setSource((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SERVER:
        setServer((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__USER:
        setUser((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__PASSWORD:
        setPassword((String)newValue);
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__DRIVER:
        setDriver(DRIVER_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SOURCE:
        setSource(SOURCE_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SERVER:
        setServer(SERVER_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__USER:
        setUser(USER_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__PASSWORD:
        setPassword(PASSWORD_EDEFAULT);
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__DRIVER:
        return DRIVER_EDEFAULT == null ? driver != null : !DRIVER_EDEFAULT.equals(driver);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SOURCE:
        return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__SERVER:
        return SERVER_EDEFAULT == null ? server != null : !SERVER_EDEFAULT.equals(server);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__USER:
        return USER_EDEFAULT == null ? user != null : !USER_EDEFAULT.equals(user);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_GENERIC__PASSWORD:
        return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
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
    result.append(" (driver: ");
    result.append(driver);
    result.append(", source: ");
    result.append(source);
    result.append(", server: ");
    result.append(server);
    result.append(", user: ");
    result.append(user);
    result.append(", password: ");
    result.append(password);
    result.append(')');
    return result.toString();
  }

} //CreateDataBaseConnectionGenericImpl
