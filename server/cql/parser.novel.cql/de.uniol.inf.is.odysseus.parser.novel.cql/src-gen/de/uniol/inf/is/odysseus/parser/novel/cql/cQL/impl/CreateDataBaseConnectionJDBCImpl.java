/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionJDBC;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Create Data Base Connection JDBC</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionJDBCImpl#getServer <em>Server</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionJDBCImpl#getUser <em>User</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionJDBCImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateDataBaseConnectionJDBCImpl#getLazy <em>Lazy</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CreateDataBaseConnectionJDBCImpl extends CommandImpl implements CreateDataBaseConnectionJDBC
{
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
   * The default value of the '{@link #getLazy() <em>Lazy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLazy()
   * @generated
   * @ordered
   */
  protected static final String LAZY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLazy() <em>Lazy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLazy()
   * @generated
   * @ordered
   */
  protected String lazy = LAZY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CreateDataBaseConnectionJDBCImpl()
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
    return CQLPackage.Literals.CREATE_DATA_BASE_CONNECTION_JDBC;
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
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__SERVER, oldServer, server));
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
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__USER, oldUser, user));
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
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__PASSWORD, oldPassword, password));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLazy()
  {
    return lazy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLazy(String newLazy)
  {
    String oldLazy = lazy;
    lazy = newLazy;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__LAZY, oldLazy, lazy));
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__SERVER:
        return getServer();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__USER:
        return getUser();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__PASSWORD:
        return getPassword();
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__LAZY:
        return getLazy();
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__SERVER:
        setServer((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__USER:
        setUser((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__PASSWORD:
        setPassword((String)newValue);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__LAZY:
        setLazy((String)newValue);
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__SERVER:
        setServer(SERVER_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__USER:
        setUser(USER_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__PASSWORD:
        setPassword(PASSWORD_EDEFAULT);
        return;
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__LAZY:
        setLazy(LAZY_EDEFAULT);
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
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__SERVER:
        return SERVER_EDEFAULT == null ? server != null : !SERVER_EDEFAULT.equals(server);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__USER:
        return USER_EDEFAULT == null ? user != null : !USER_EDEFAULT.equals(user);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__PASSWORD:
        return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
      case CQLPackage.CREATE_DATA_BASE_CONNECTION_JDBC__LAZY:
        return LAZY_EDEFAULT == null ? lazy != null : !LAZY_EDEFAULT.equals(lazy);
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
    result.append(" (server: ");
    result.append(server);
    result.append(", user: ");
    result.append(user);
    result.append(", password: ");
    result.append(password);
    result.append(", lazy: ");
    result.append(lazy);
    result.append(')');
    return result.toString();
  }

} //CreateDataBaseConnectionJDBCImpl
