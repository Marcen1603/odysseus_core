/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateParameters;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateSink1;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Create Sink1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateSink1Impl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CreateSink1Impl#getPars <em>Pars</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CreateSink1Impl extends MinimalEObjectImpl.Container implements CreateSink1
{
  /**
   * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttributes()
   * @generated
   * @ordered
   */
  protected AttributeDefinition attributes;

  /**
   * The cached value of the '{@link #getPars() <em>Pars</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPars()
   * @generated
   * @ordered
   */
  protected CreateParameters pars;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CreateSink1Impl()
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
    return CQLPackage.Literals.CREATE_SINK1;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttributeDefinition getAttributes()
  {
    return attributes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAttributes(AttributeDefinition newAttributes, NotificationChain msgs)
  {
    AttributeDefinition oldAttributes = attributes;
    attributes = newAttributes;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_SINK1__ATTRIBUTES, oldAttributes, newAttributes);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAttributes(AttributeDefinition newAttributes)
  {
    if (newAttributes != attributes)
    {
      NotificationChain msgs = null;
      if (attributes != null)
        msgs = ((InternalEObject)attributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.CREATE_SINK1__ATTRIBUTES, null, msgs);
      if (newAttributes != null)
        msgs = ((InternalEObject)newAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.CREATE_SINK1__ATTRIBUTES, null, msgs);
      msgs = basicSetAttributes(newAttributes, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_SINK1__ATTRIBUTES, newAttributes, newAttributes));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CreateParameters getPars()
  {
    return pars;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPars(CreateParameters newPars, NotificationChain msgs)
  {
    CreateParameters oldPars = pars;
    pars = newPars;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_SINK1__PARS, oldPars, newPars);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPars(CreateParameters newPars)
  {
    if (newPars != pars)
    {
      NotificationChain msgs = null;
      if (pars != null)
        msgs = ((InternalEObject)pars).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CQLPackage.CREATE_SINK1__PARS, null, msgs);
      if (newPars != null)
        msgs = ((InternalEObject)newPars).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CQLPackage.CREATE_SINK1__PARS, null, msgs);
      msgs = basicSetPars(newPars, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, CQLPackage.CREATE_SINK1__PARS, newPars, newPars));
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
      case CQLPackage.CREATE_SINK1__ATTRIBUTES:
        return basicSetAttributes(null, msgs);
      case CQLPackage.CREATE_SINK1__PARS:
        return basicSetPars(null, msgs);
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
      case CQLPackage.CREATE_SINK1__ATTRIBUTES:
        return getAttributes();
      case CQLPackage.CREATE_SINK1__PARS:
        return getPars();
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
      case CQLPackage.CREATE_SINK1__ATTRIBUTES:
        setAttributes((AttributeDefinition)newValue);
        return;
      case CQLPackage.CREATE_SINK1__PARS:
        setPars((CreateParameters)newValue);
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
      case CQLPackage.CREATE_SINK1__ATTRIBUTES:
        setAttributes((AttributeDefinition)null);
        return;
      case CQLPackage.CREATE_SINK1__PARS:
        setPars((CreateParameters)null);
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
      case CQLPackage.CREATE_SINK1__ATTRIBUTES:
        return attributes != null;
      case CQLPackage.CREATE_SINK1__PARS:
        return pars != null;
    }
    return super.eIsSet(featureID);
  }

} //CreateSink1Impl
