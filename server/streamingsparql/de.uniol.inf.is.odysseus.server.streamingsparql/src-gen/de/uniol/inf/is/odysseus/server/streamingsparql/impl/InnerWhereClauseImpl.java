/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub;
import de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inner Where Clause</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.InnerWhereClauseImpl#getGroupGraphPattern <em>Group Graph Pattern</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InnerWhereClauseImpl extends MinimalEObjectImpl.Container implements InnerWhereClause
{
  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected DatasetClause name;

  /**
   * The cached value of the '{@link #getGroupGraphPattern() <em>Group Graph Pattern</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGroupGraphPattern()
   * @generated
   * @ordered
   */
  protected GroupGraphPatternSub groupGraphPattern;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InnerWhereClauseImpl()
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
    return StreamingsparqlPackage.Literals.INNER_WHERE_CLAUSE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatasetClause getName()
  {
    if (name != null && name.eIsProxy())
    {
      InternalEObject oldName = (InternalEObject)name;
      name = (DatasetClause)eResolveProxy(oldName);
      if (name != oldName)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME, oldName, name));
      }
    }
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatasetClause basicGetName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(DatasetClause newName)
  {
    DatasetClause oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupGraphPatternSub getGroupGraphPattern()
  {
    return groupGraphPattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGroupGraphPattern(GroupGraphPatternSub newGroupGraphPattern, NotificationChain msgs)
  {
    GroupGraphPatternSub oldGroupGraphPattern = groupGraphPattern;
    groupGraphPattern = newGroupGraphPattern;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN, oldGroupGraphPattern, newGroupGraphPattern);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGroupGraphPattern(GroupGraphPatternSub newGroupGraphPattern)
  {
    if (newGroupGraphPattern != groupGraphPattern)
    {
      NotificationChain msgs = null;
      if (groupGraphPattern != null)
        msgs = ((InternalEObject)groupGraphPattern).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN, null, msgs);
      if (newGroupGraphPattern != null)
        msgs = ((InternalEObject)newGroupGraphPattern).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN, null, msgs);
      msgs = basicSetGroupGraphPattern(newGroupGraphPattern, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN, newGroupGraphPattern, newGroupGraphPattern));
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
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN:
        return basicSetGroupGraphPattern(null, msgs);
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
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME:
        if (resolve) return getName();
        return basicGetName();
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN:
        return getGroupGraphPattern();
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
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME:
        setName((DatasetClause)newValue);
        return;
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN:
        setGroupGraphPattern((GroupGraphPatternSub)newValue);
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
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME:
        setName((DatasetClause)null);
        return;
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN:
        setGroupGraphPattern((GroupGraphPatternSub)null);
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
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__NAME:
        return name != null;
      case StreamingsparqlPackage.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN:
        return groupGraphPattern != null;
    }
    return super.eIsSet(featureID);
  }

} //InnerWhereClauseImpl
