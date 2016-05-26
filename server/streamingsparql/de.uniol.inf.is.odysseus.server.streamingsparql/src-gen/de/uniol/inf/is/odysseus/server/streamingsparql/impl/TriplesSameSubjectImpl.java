/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode;
import de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Triples Same Subject</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl#getSubject <em>Subject</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.TriplesSameSubjectImpl#getPropertyList <em>Property List</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TriplesSameSubjectImpl extends MinimalEObjectImpl.Container implements TriplesSameSubject
{
  /**
   * The cached value of the '{@link #getSubject() <em>Subject</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubject()
   * @generated
   * @ordered
   */
  protected GraphNode subject;

  /**
   * The cached value of the '{@link #getPropertyList() <em>Property List</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPropertyList()
   * @generated
   * @ordered
   */
  protected EList<PropertyList> propertyList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TriplesSameSubjectImpl()
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
    return StreamingsparqlPackage.Literals.TRIPLES_SAME_SUBJECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GraphNode getSubject()
  {
    return subject;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubject(GraphNode newSubject, NotificationChain msgs)
  {
    GraphNode oldSubject = subject;
    subject = newSubject;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT, oldSubject, newSubject);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubject(GraphNode newSubject)
  {
    if (newSubject != subject)
    {
      NotificationChain msgs = null;
      if (subject != null)
        msgs = ((InternalEObject)subject).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT, null, msgs);
      if (newSubject != null)
        msgs = ((InternalEObject)newSubject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT, null, msgs);
      msgs = basicSetSubject(newSubject, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT, newSubject, newSubject));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<PropertyList> getPropertyList()
  {
    if (propertyList == null)
    {
      propertyList = new EObjectContainmentEList<PropertyList>(PropertyList.class, this, StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST);
    }
    return propertyList;
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
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT:
        return basicSetSubject(null, msgs);
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST:
        return ((InternalEList<?>)getPropertyList()).basicRemove(otherEnd, msgs);
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
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT:
        return getSubject();
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST:
        return getPropertyList();
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
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT:
        setSubject((GraphNode)newValue);
        return;
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST:
        getPropertyList().clear();
        getPropertyList().addAll((Collection<? extends PropertyList>)newValue);
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
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT:
        setSubject((GraphNode)null);
        return;
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST:
        getPropertyList().clear();
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
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__SUBJECT:
        return subject != null;
      case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT__PROPERTY_LIST:
        return propertyList != null && !propertyList.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //TriplesSameSubjectImpl
