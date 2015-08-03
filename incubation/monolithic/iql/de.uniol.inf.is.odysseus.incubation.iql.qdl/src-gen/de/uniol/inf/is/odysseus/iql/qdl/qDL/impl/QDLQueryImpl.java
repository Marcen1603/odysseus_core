/**
 */
package de.uniol.inf.is.odysseus.iql.qdl.qDL.impl;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadataList;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatement;

import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.impl.JvmGenericTypeImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl#getMetadataList <em>Metadata List</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.iql.qdl.qDL.impl.QDLQueryImpl#getStatements <em>Statements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QDLQueryImpl extends JvmGenericTypeImpl implements QDLQuery
{
  /**
   * The cached value of the '{@link #getMetadataList() <em>Metadata List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMetadataList()
   * @generated
   * @ordered
   */
  protected IQLMetadataList metadataList;

  /**
   * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatements()
   * @generated
   * @ordered
   */
  protected IQLStatement statements;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected QDLQueryImpl()
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
    return QDLPackage.Literals.QDL_QUERY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLMetadataList getMetadataList()
  {
    return metadataList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMetadataList(IQLMetadataList newMetadataList, NotificationChain msgs)
  {
    IQLMetadataList oldMetadataList = metadataList;
    metadataList = newMetadataList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.QDL_QUERY__METADATA_LIST, oldMetadataList, newMetadataList);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMetadataList(IQLMetadataList newMetadataList)
  {
    if (newMetadataList != metadataList)
    {
      NotificationChain msgs = null;
      if (metadataList != null)
        msgs = ((InternalEObject)metadataList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.QDL_QUERY__METADATA_LIST, null, msgs);
      if (newMetadataList != null)
        msgs = ((InternalEObject)newMetadataList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.QDL_QUERY__METADATA_LIST, null, msgs);
      msgs = basicSetMetadataList(newMetadataList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.QDL_QUERY__METADATA_LIST, newMetadataList, newMetadataList));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IQLStatement getStatements()
  {
    return statements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetStatements(IQLStatement newStatements, NotificationChain msgs)
  {
    IQLStatement oldStatements = statements;
    statements = newStatements;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QDLPackage.QDL_QUERY__STATEMENTS, oldStatements, newStatements);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStatements(IQLStatement newStatements)
  {
    if (newStatements != statements)
    {
      NotificationChain msgs = null;
      if (statements != null)
        msgs = ((InternalEObject)statements).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QDLPackage.QDL_QUERY__STATEMENTS, null, msgs);
      if (newStatements != null)
        msgs = ((InternalEObject)newStatements).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QDLPackage.QDL_QUERY__STATEMENTS, null, msgs);
      msgs = basicSetStatements(newStatements, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QDLPackage.QDL_QUERY__STATEMENTS, newStatements, newStatements));
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
      case QDLPackage.QDL_QUERY__METADATA_LIST:
        return basicSetMetadataList(null, msgs);
      case QDLPackage.QDL_QUERY__STATEMENTS:
        return basicSetStatements(null, msgs);
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
      case QDLPackage.QDL_QUERY__METADATA_LIST:
        return getMetadataList();
      case QDLPackage.QDL_QUERY__STATEMENTS:
        return getStatements();
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
      case QDLPackage.QDL_QUERY__METADATA_LIST:
        setMetadataList((IQLMetadataList)newValue);
        return;
      case QDLPackage.QDL_QUERY__STATEMENTS:
        setStatements((IQLStatement)newValue);
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
      case QDLPackage.QDL_QUERY__METADATA_LIST:
        setMetadataList((IQLMetadataList)null);
        return;
      case QDLPackage.QDL_QUERY__STATEMENTS:
        setStatements((IQLStatement)null);
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
      case QDLPackage.QDL_QUERY__METADATA_LIST:
        return metadataList != null;
      case QDLPackage.QDL_QUERY__STATEMENTS:
        return statements != null;
    }
    return super.eIsSet(featureID);
  }

} //QDLQueryImpl
