/**
 */
package de.uniol.inf.is.odysseus.eca.eCA.impl;

import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.RuleSource;
import de.uniol.inf.is.odysseus.eca.eCA.Source;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl#getDefSource <em>Def Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl#getNewSource <em>New Source</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.impl.RuleSourceImpl#getPreSource <em>Pre Source</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RuleSourceImpl extends MinimalEObjectImpl.Container implements RuleSource
{
  /**
   * The cached value of the '{@link #getDefSource() <em>Def Source</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefSource()
   * @generated
   * @ordered
   */
  protected DefinedEvent defSource;

  /**
   * The cached value of the '{@link #getNewSource() <em>New Source</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNewSource()
   * @generated
   * @ordered
   */
  protected Source newSource;

  /**
   * The default value of the '{@link #getPreSource() <em>Pre Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreSource()
   * @generated
   * @ordered
   */
  protected static final String PRE_SOURCE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPreSource() <em>Pre Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreSource()
   * @generated
   * @ordered
   */
  protected String preSource = PRE_SOURCE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RuleSourceImpl()
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
    return ECAPackage.Literals.RULE_SOURCE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DefinedEvent getDefSource()
  {
    if (defSource != null && defSource.eIsProxy())
    {
      InternalEObject oldDefSource = (InternalEObject)defSource;
      defSource = (DefinedEvent)eResolveProxy(oldDefSource);
      if (defSource != oldDefSource)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ECAPackage.RULE_SOURCE__DEF_SOURCE, oldDefSource, defSource));
      }
    }
    return defSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DefinedEvent basicGetDefSource()
  {
    return defSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefSource(DefinedEvent newDefSource)
  {
    DefinedEvent oldDefSource = defSource;
    defSource = newDefSource;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE_SOURCE__DEF_SOURCE, oldDefSource, defSource));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Source getNewSource()
  {
    return newSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetNewSource(Source newNewSource, NotificationChain msgs)
  {
    Source oldNewSource = newSource;
    newSource = newNewSource;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ECAPackage.RULE_SOURCE__NEW_SOURCE, oldNewSource, newNewSource);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNewSource(Source newNewSource)
  {
    if (newNewSource != newSource)
    {
      NotificationChain msgs = null;
      if (newSource != null)
        msgs = ((InternalEObject)newSource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE_SOURCE__NEW_SOURCE, null, msgs);
      if (newNewSource != null)
        msgs = ((InternalEObject)newNewSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ECAPackage.RULE_SOURCE__NEW_SOURCE, null, msgs);
      msgs = basicSetNewSource(newNewSource, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE_SOURCE__NEW_SOURCE, newNewSource, newNewSource));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPreSource()
  {
    return preSource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPreSource(String newPreSource)
  {
    String oldPreSource = preSource;
    preSource = newPreSource;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ECAPackage.RULE_SOURCE__PRE_SOURCE, oldPreSource, preSource));
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
      case ECAPackage.RULE_SOURCE__NEW_SOURCE:
        return basicSetNewSource(null, msgs);
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
      case ECAPackage.RULE_SOURCE__DEF_SOURCE:
        if (resolve) return getDefSource();
        return basicGetDefSource();
      case ECAPackage.RULE_SOURCE__NEW_SOURCE:
        return getNewSource();
      case ECAPackage.RULE_SOURCE__PRE_SOURCE:
        return getPreSource();
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
      case ECAPackage.RULE_SOURCE__DEF_SOURCE:
        setDefSource((DefinedEvent)newValue);
        return;
      case ECAPackage.RULE_SOURCE__NEW_SOURCE:
        setNewSource((Source)newValue);
        return;
      case ECAPackage.RULE_SOURCE__PRE_SOURCE:
        setPreSource((String)newValue);
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
      case ECAPackage.RULE_SOURCE__DEF_SOURCE:
        setDefSource((DefinedEvent)null);
        return;
      case ECAPackage.RULE_SOURCE__NEW_SOURCE:
        setNewSource((Source)null);
        return;
      case ECAPackage.RULE_SOURCE__PRE_SOURCE:
        setPreSource(PRE_SOURCE_EDEFAULT);
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
      case ECAPackage.RULE_SOURCE__DEF_SOURCE:
        return defSource != null;
      case ECAPackage.RULE_SOURCE__NEW_SOURCE:
        return newSource != null;
      case ECAPackage.RULE_SOURCE__PRE_SOURCE:
        return PRE_SOURCE_EDEFAULT == null ? preSource != null : !PRE_SOURCE_EDEFAULT.equals(preSource);
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
    result.append(" (preSource: ");
    result.append(preSource);
    result.append(')');
    return result.toString();
  }

} //RuleSourceImpl
