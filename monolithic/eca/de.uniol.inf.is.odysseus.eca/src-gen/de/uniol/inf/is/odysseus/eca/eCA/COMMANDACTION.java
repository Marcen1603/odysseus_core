/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>COMMANDACTION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getSubActname <em>Sub Actname</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getFunctAction <em>Funct Action</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getActionValue <em>Action Value</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getInnerAction <em>Inner Action</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getCOMMANDACTION()
 * @model
 * @generated
 */
public interface COMMANDACTION extends EObject
{
  /**
   * Returns the value of the '<em><b>Sub Actname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sub Actname</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub Actname</em>' attribute.
   * @see #setSubActname(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getCOMMANDACTION_SubActname()
   * @model
   * @generated
   */
  String getSubActname();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getSubActname <em>Sub Actname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sub Actname</em>' attribute.
   * @see #getSubActname()
   * @generated
   */
  void setSubActname(String value);

  /**
   * Returns the value of the '<em><b>Funct Action</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Funct Action</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Funct Action</em>' containment reference.
   * @see #setFunctAction(RNDQUERY)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getCOMMANDACTION_FunctAction()
   * @model containment="true"
   * @generated
   */
  RNDQUERY getFunctAction();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getFunctAction <em>Funct Action</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Funct Action</em>' containment reference.
   * @see #getFunctAction()
   * @generated
   */
  void setFunctAction(RNDQUERY value);

  /**
   * Returns the value of the '<em><b>Action Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Action Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Action Value</em>' containment reference.
   * @see #setActionValue(EcaValue)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getCOMMANDACTION_ActionValue()
   * @model containment="true"
   * @generated
   */
  EcaValue getActionValue();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION#getActionValue <em>Action Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Action Value</em>' containment reference.
   * @see #getActionValue()
   * @generated
   */
  void setActionValue(EcaValue value);

  /**
   * Returns the value of the '<em><b>Inner Action</b></em>' containment reference list.
   * The list contents are of type {@link de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inner Action</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inner Action</em>' containment reference list.
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getCOMMANDACTION_InnerAction()
   * @model containment="true"
   * @generated
   */
  EList<COMMANDACTION> getInnerAction();

} // COMMANDACTION
