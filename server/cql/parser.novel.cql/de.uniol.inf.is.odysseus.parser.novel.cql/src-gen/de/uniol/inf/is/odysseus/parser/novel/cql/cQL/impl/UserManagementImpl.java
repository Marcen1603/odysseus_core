/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.UserManagement;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Management</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.UserManagementImpl#getSubject <em>Subject</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.UserManagementImpl#getSubjectName <em>Subject Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.UserManagementImpl#getPassword <em>Password</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UserManagementImpl extends CommandImpl implements UserManagement
{
  /**
   * The default value of the '{@link #getSubject() <em>Subject</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubject()
   * @generated
   * @ordered
   */
  protected static final String SUBJECT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubject() <em>Subject</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubject()
   * @generated
   * @ordered
   */
  protected String subject = SUBJECT_EDEFAULT;

  /**
   * The default value of the '{@link #getSubjectName() <em>Subject Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubjectName()
   * @generated
   * @ordered
   */
  protected static final String SUBJECT_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubjectName() <em>Subject Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubjectName()
   * @generated
   * @ordered
   */
  protected String subjectName = SUBJECT_NAME_EDEFAULT;

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
  protected UserManagementImpl()
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
    return CQLPackage.Literals.USER_MANAGEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubject(String newSubject)
  {
    String oldSubject = subject;
    subject = newSubject;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.USER_MANAGEMENT__SUBJECT, oldSubject, subject));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubjectName()
  {
    return subjectName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubjectName(String newSubjectName)
  {
    String oldSubjectName = subjectName;
    subjectName = newSubjectName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.USER_MANAGEMENT__SUBJECT_NAME, oldSubjectName, subjectName));
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
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.USER_MANAGEMENT__PASSWORD, oldPassword, password));
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
      case CQLPackage.USER_MANAGEMENT__SUBJECT:
        return getSubject();
      case CQLPackage.USER_MANAGEMENT__SUBJECT_NAME:
        return getSubjectName();
      case CQLPackage.USER_MANAGEMENT__PASSWORD:
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
      case CQLPackage.USER_MANAGEMENT__SUBJECT:
        setSubject((String)newValue);
        return;
      case CQLPackage.USER_MANAGEMENT__SUBJECT_NAME:
        setSubjectName((String)newValue);
        return;
      case CQLPackage.USER_MANAGEMENT__PASSWORD:
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
      case CQLPackage.USER_MANAGEMENT__SUBJECT:
        setSubject(SUBJECT_EDEFAULT);
        return;
      case CQLPackage.USER_MANAGEMENT__SUBJECT_NAME:
        setSubjectName(SUBJECT_NAME_EDEFAULT);
        return;
      case CQLPackage.USER_MANAGEMENT__PASSWORD:
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
      case CQLPackage.USER_MANAGEMENT__SUBJECT:
        return SUBJECT_EDEFAULT == null ? subject != null : !SUBJECT_EDEFAULT.equals(subject);
      case CQLPackage.USER_MANAGEMENT__SUBJECT_NAME:
        return SUBJECT_NAME_EDEFAULT == null ? subjectName != null : !SUBJECT_NAME_EDEFAULT.equals(subjectName);
      case CQLPackage.USER_MANAGEMENT__PASSWORD:
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
    result.append(" (subject: ");
    result.append(subject);
    result.append(", subjectName: ");
    result.append(subjectName);
    result.append(", password: ");
    result.append(password);
    result.append(')');
    return result.toString();
  }

} //UserManagementImpl
