/**
 */
package de.uniol.inf.is.odysseus.server.streamingsparql.impl;

import de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl#getUnnamed <em>Unnamed</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.server.streamingsparql.impl.VariableImpl#getProperty <em>Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VariableImpl extends MinimalEObjectImpl.Container implements Variable
{
  /**
   * The cached value of the '{@link #getUnnamed() <em>Unnamed</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnnamed()
   * @generated
   * @ordered
   */
  protected UnNamedVariable unnamed;

  /**
   * The cached value of the '{@link #getProperty() <em>Property</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProperty()
   * @generated
   * @ordered
   */
  protected NamedVariable property;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected VariableImpl()
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
    return StreamingsparqlPackage.Literals.VARIABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UnNamedVariable getUnnamed()
  {
    return unnamed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetUnnamed(UnNamedVariable newUnnamed, NotificationChain msgs)
  {
    UnNamedVariable oldUnnamed = unnamed;
    unnamed = newUnnamed;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.VARIABLE__UNNAMED, oldUnnamed, newUnnamed);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUnnamed(UnNamedVariable newUnnamed)
  {
    if (newUnnamed != unnamed)
    {
      NotificationChain msgs = null;
      if (unnamed != null)
        msgs = ((InternalEObject)unnamed).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.VARIABLE__UNNAMED, null, msgs);
      if (newUnnamed != null)
        msgs = ((InternalEObject)newUnnamed).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.VARIABLE__UNNAMED, null, msgs);
      msgs = basicSetUnnamed(newUnnamed, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.VARIABLE__UNNAMED, newUnnamed, newUnnamed));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NamedVariable getProperty()
  {
    return property;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProperty(NamedVariable newProperty, NotificationChain msgs)
  {
    NamedVariable oldProperty = property;
    property = newProperty;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.VARIABLE__PROPERTY, oldProperty, newProperty);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProperty(NamedVariable newProperty)
  {
    if (newProperty != property)
    {
      NotificationChain msgs = null;
      if (property != null)
        msgs = ((InternalEObject)property).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.VARIABLE__PROPERTY, null, msgs);
      if (newProperty != null)
        msgs = ((InternalEObject)newProperty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StreamingsparqlPackage.VARIABLE__PROPERTY, null, msgs);
      msgs = basicSetProperty(newProperty, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StreamingsparqlPackage.VARIABLE__PROPERTY, newProperty, newProperty));
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
      case StreamingsparqlPackage.VARIABLE__UNNAMED:
        return basicSetUnnamed(null, msgs);
      case StreamingsparqlPackage.VARIABLE__PROPERTY:
        return basicSetProperty(null, msgs);
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
      case StreamingsparqlPackage.VARIABLE__UNNAMED:
        return getUnnamed();
      case StreamingsparqlPackage.VARIABLE__PROPERTY:
        return getProperty();
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
      case StreamingsparqlPackage.VARIABLE__UNNAMED:
        setUnnamed((UnNamedVariable)newValue);
        return;
      case StreamingsparqlPackage.VARIABLE__PROPERTY:
        setProperty((NamedVariable)newValue);
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
      case StreamingsparqlPackage.VARIABLE__UNNAMED:
        setUnnamed((UnNamedVariable)null);
        return;
      case StreamingsparqlPackage.VARIABLE__PROPERTY:
        setProperty((NamedVariable)null);
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
      case StreamingsparqlPackage.VARIABLE__UNNAMED:
        return unnamed != null;
      case StreamingsparqlPackage.VARIABLE__PROPERTY:
        return property != null;
    }
    return super.eIsSet(featureID);
  }

} //VariableImpl
