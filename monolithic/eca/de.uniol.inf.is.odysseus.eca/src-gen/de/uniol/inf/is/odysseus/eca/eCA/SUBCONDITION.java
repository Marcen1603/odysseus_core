/**
 */
package de.uniol.inf.is.odysseus.eca.eCA;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SUBCONDITION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubfree <em>Subfree</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubmap <em>Submap</em>}</li>
 *   <li>{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getQueryCond <em>Query Cond</em>}</li>
 * </ul>
 *
 * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSUBCONDITION()
 * @model
 * @generated
 */
public interface SUBCONDITION extends Expression
{
  /**
   * Returns the value of the '<em><b>Subfree</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subfree</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subfree</em>' containment reference.
   * @see #setSubfree(FREECONDITION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSUBCONDITION_Subfree()
   * @model containment="true"
   * @generated
   */
  FREECONDITION getSubfree();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubfree <em>Subfree</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subfree</em>' containment reference.
   * @see #getSubfree()
   * @generated
   */
  void setSubfree(FREECONDITION value);

  /**
   * Returns the value of the '<em><b>Submap</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Submap</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Submap</em>' containment reference.
   * @see #setSubmap(MAPCONDITION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSUBCONDITION_Submap()
   * @model containment="true"
   * @generated
   */
  MAPCONDITION getSubmap();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getSubmap <em>Submap</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Submap</em>' containment reference.
   * @see #getSubmap()
   * @generated
   */
  void setSubmap(MAPCONDITION value);

  /**
   * Returns the value of the '<em><b>Query Cond</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Query Cond</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Query Cond</em>' containment reference.
   * @see #setQueryCond(QUERYCONDITION)
   * @see de.uniol.inf.is.odysseus.eca.eCA.ECAPackage#getSUBCONDITION_QueryCond()
   * @model containment="true"
   * @generated
   */
  QUERYCONDITION getQueryCond();

  /**
   * Sets the value of the '{@link de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION#getQueryCond <em>Query Cond</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Query Cond</em>' containment reference.
   * @see #getQueryCond()
   * @generated
   */
  void setQueryCond(QUERYCONDITION value);

} // SUBCONDITION
