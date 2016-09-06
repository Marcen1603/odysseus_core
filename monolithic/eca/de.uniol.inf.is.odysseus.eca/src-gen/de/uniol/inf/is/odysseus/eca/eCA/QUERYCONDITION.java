/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>QUERYCONDITION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryNot <em>Query Not</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryFunct <em>Query Funct</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getQUERYCONDITION()
 * @model
 * @generated
 */
public interface QUERYCONDITION extends EObject
{
  /**
   * Returns the value of the '<em><b>Query Not</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Query Not</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Query Not</em>' attribute.
   * @see #setQueryNot(String)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getQUERYCONDITION_QueryNot()
   * @model
   * @generated
   */
  String getQueryNot();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryNot <em>Query Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Query Not</em>' attribute.
   * @see #getQueryNot()
   * @generated
   */
  void setQueryNot(String value);

  /**
   * Returns the value of the '<em><b>Query Funct</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Query Funct</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Query Funct</em>' containment reference.
   * @see #setQueryFunct(RNDQUERY)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getQUERYCONDITION_QueryFunct()
   * @model containment="true"
   * @generated
   */
  RNDQUERY getQueryFunct();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION#getQueryFunct <em>Query Funct</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Query Funct</em>' containment reference.
   * @see #getQueryFunct()
   * @generated
   */
  void setQueryFunct(RNDQUERY value);

} // QUERYCONDITION
